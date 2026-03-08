# 🍰 CRUMB 2.0 - The A to Z Execution Guide (Spring Boot)

This guide walks you through the exact **step-by-step, file-by-file execution flow** of your CRUMB 2.0 application. From the moment you press "Run" to the moment a customer sees the products on their screen.

---

## 🟢 PHASE 1: Application Startup (What happens when you run the app?)

When you run `mvn spring-boot:run` or start the app in your IDE, this is the exact sequence of files the computer runs:

### Step 1: `CrumbBakeryApplication.java`
* **Where is it?** Inside `src/main/java/com/crumb/bakery/CrumbBakeryApplication.java` (or similar main class name).
* **What it does:** This is the absolute starting point of your Java program. It contains the `public static void main(String[] args)` method.
* **How it works:** It calls `SpringApplication.run(...)`. This tells Spring Boot to wake up, look at all your code, and start assembling the application.

### Step 2: `pom.xml` & `application.properties`
* Before Java even starts properly, **Maven** looks at `pom.xml` to download Tomcat (the hidden web server) and MongoDB drivers.
* Spring Boot then reads `application.properties` (in `src/main/resources`). It finds the port `server.port=8080` (if set) and connects to MongoDB using `spring.data.mongodb.uri`.

### Step 3: Scanning for Annotations
* Spring Boot scans your entire project for special words (Annotations). 
* It finds `@RestController` in `ProductController`, `@Service` in `ProductService`, and `@Repository` in `ProductRepository` and creates "Beans" (living memory objects) for them. It automatically connects them together because you used `@Autowired`.

### Step 4: `DataInitializer.java` (Optional but common)
* **Where is it?** `src/main/java/com/crumb/bakery/config/DataInitializer.java` (Based on your open files, you have this!)
* **What it does:** Because this class likely implements `CommandLineRunner`, Spring Boot automatically runs its `run()` method right after the app finishes starting up. This is usually where you write code to insert default Admin users or initial products into MongoDB so your database isn't empty.

### Step 5: Web Server Ready
* The embedded Tomcat Server finishes starting on port `8080`. The console prints `"Tomcat started on port(s): 8080 (http)"`. The app is now waiting for customers!

---

## 🔵 PHASE 2: The Customer Request Flow (A to Z)

Let's trace exactly what happens when a customer opens their browser and visits `http://localhost:8080`.

### Step 1: The Browser asks for the Frontend (`index.html`)
1. You type `http://localhost:8080` in Chrome and press Enter.
2. The request goes to Tomcat (the server Spring Boot started).
3. Spring Boot automatically looks inside your `src/main/resources/static/` folder.
4. It finds `index.html` and sends this raw HTML file back to the browser.
5. The browser displays the shop layout.

### Step 2: The Browser asks for Data (JavaScript Fetch)
1. Your `index.html` file loads. At the bottom, or in a `<script>` tag, there is JavaScript code that says something like `fetch('/api/products')`.
2. The browser silently sends a new HTTP GET request to `http://localhost:8080/api/products` in the background.

### Step 3: `DispatcherServlet` (The Hidden Traffic Cop)
1. The request hits your Java backend.
2. Spring Boot has a built-in hidden class called `DispatcherServlet`.
3. It looks at the URL `/api/products` and asks, *"Do I have any Controllers mapped to this?"*

### Step 4: `ProductController.java` (The Waiter)
1. The `DispatcherServlet` finds your `ProductController.java` because it has `@RequestMapping("/api/products")`.
2. Because the request is a GET request, it routes the traffic into the method marked with `@GetMapping`.
   ```java
   @GetMapping
   public ResponseEntity<List<Product>> getAll() {
       return ResponseEntity.ok(productService.getAllAvailable());
   }
   ```
3. The Controller doesn't know *how* to get products from the database. It just calls `productService.getAllAvailable()`.

### Step 5: `ProductService.java` (The Chef / Business Logic)
1. The execution jumps to `src/main/java/com/crumb/bakery/service/ProductService.java`.
2. The `getAllAvailable()` method executes.
   ```java
   public List<Product> getAllAvailable() {
       return repo.findByAvailableTrueOrderBySortOrderAsc();
   }
   ```
3. The Service layer is where you put business rules. Here, you are making sure the customer only sees "available" products by calling that specific method on the repository.

### Step 6: `ProductRepository.java` (The Database Manager)
1. Execution jumps to `src/main/java/com/crumb/bakery/repository/ProductRepository.java`.
2. This is simply an Interface extending `MongoRepository`. You are calling a method named `findByAvailableTrueOrderBySortOrderAsc()`.
3. **Magic happens here:** Spring Data looks at the name of your method. It translates the English words into a MongoDB instruction: "Find all products where available is true, and sort them in ascending order by sortOrder".
4. Spring Boot opens the connection to MongoDB and fires the query.

### Step 7: The Database (MongoDB)
1. MongoDB searches the `products` collection.
2. It finds all the matching JSON/BSON documents.
3. It returns the raw data back to Spring Boot.

### Step 8: `Product.java` (The Blueprint / Model)
1. Spring Boot receives the raw data from MongoDB.
2. It looks at your `Product.java` model blueprint in `src/main/java/com/crumb/bakery/model/Product.java`.
3. It maps the database fields exactly to your Java variables (e.g., the MongoDB `price` goes into `Double price`, `name` goes to `String name`). It creates a `List<Product>` (a list of fully formed Java objects).

### Step 9: The Return Journey (Back up the chain)
1. The `ProductRepository` hands the `List<Product>` to the `ProductService`.
2. The `ProductService` hands it to the `ProductController`.
3. The `ProductController` puts it inside a `ResponseEntity.ok()` and returns it.

### Step 10: JSON Conversion (`Jackson` Library)
1. Before leaving Java, Spring Boot realizes it is holding Java objects, but the browser only understands JSON text.
2. Spring Boot automatically uses a hidden library called **Jackson**. Jackson converts your `List<Product>` into a JSON string that looks like this:
   ```json
   [
     { "id": "1", "name": "Dark Chocolate Fudge Brownie", "price": 299.0, "available": true },
     { "id": "2", "name": "Mango Passion Cake", "price": 799.0, "available": true }
   ]
   ```
3. This textual string is zipped across the internet back to the customer's browser.

### Step 11: The Frontend Renders the Page
1. The `fetch()` command in your JavaScript finally receives the JSON data.
2. Your JavaScript iterates through the JSON.
3. It dynamically generates HTML layout code (like `<div class="card">...</div>`) for each cake and brownie.
4. It injects the HTML into the website page using something like `document.getElementById('products-container').innerHTML`.
5. The customer sees the beautiful gallery of brownies and cakes on their screen!

---

## 🛑 PHASE 3: Error Handling (What if things go wrong?)

What if the frontend sends a bad request (e.g., placing an order with no name or a negative price)?

1. **Validation Checks Out**: 
   * When an order arrives at `OrderController`, the `@Valid` annotation checks it against the rules in `Order.java` (like `@NotBlank` or `@NotNull`).
   * If it fails, an exception is thrown.
2. **`GlobalExceptionHandler.java` (The Safety Net)**: 
   * (You have this file in `/config/GlobalExceptionHandler.java`). 
   * If any error happens anywhere (Controller, Service, Database validation failure), Spring Boot immediately stops the normal flow and forwards the error to this file.
   * This class intercepts the error, formats it nicely (like `"error": "Name cannot be blank"`), and returns a `400 Bad Request` perfectly formatted as JSON back to the browser, so the app doesn't just crash.

---

## 📝 Summary Diagram
Whenever you write new features, remember this **left-to-right** data flow:

`Browser (HTML/JS)` ➡️ `Controller (Java)` ➡️ `Service (Java)` ➡️ `Repository (Java)` ➡️ `Database (MongoDB)`

And the response always returns **right-to-left**:

`Database (MongoDB)` ➡️ `Repository (Java)` ➡️ `Service (Java)` ➡️ `Controller (Java)` ➡️ `Jackson (JSON)` ➡️ `Browser (HTML/JS)`

---

## 🔑 Your Absolute Beginner Cheat Sheet (Must Know)
As a beginner, you don't need to memorize everything at once. Focus on these 5 **Annotations** (the words starting with `@`). They do 90% of the heavy lifting in your app:

1. **`@RestController`**: Turns a normal Java class into an API that talks to the frontend by returning JSON data instead of HTML pages.
2. **`@GetMapping` / `@PostMapping`**: Maps a specific URL (like `/api/products`) to a Java function, telling Spring exactly when to run that function.
3. **`@Autowired`**: The magic connector. It asks Spring to give you an object you need (like getting the Repository inside the Service) without you ever having to type `new ObjectName()`.
4. **`@Service`**: Marks a class as the place where your business rules and logic live (The Chef).
5. **`@Document(collection="...")`**: Tells the database that this Java class should be saved directly as a document inside a MongoDB collection.

---

## 📁 Where to put your code? (The Golden Rule)
Whenever you want to add something new to your bakery (like a `Review` system or a `PromoCode` feature), follow this exact order:

1. Create **`Model`**: Define what it is (`Review.java`).
2. Create **`Repository`**: Make a way to save it to MongoDB (`ReviewRepository.java`).
3. Create **`Service`**: Write the rules for it (`ReviewService.java`).
4. Create **`Controller`**: Expose it to the internet (`ReviewController.java`).
5. Update **`Frontend`**: Use `fetch()` in your HTML/JS to connect the UI to the Controller.
