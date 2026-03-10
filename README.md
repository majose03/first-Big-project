# 🎂 CRUMB 2.0 — Artisan Bakery Web App

> **Full-stack bakery e-commerce app** — Spring Boot 3 + MySQL + Vanilla JS  
> Artisan cakes & brownies from Bangalore. Zero preservatives. Pure obsession.

---

## ✨ Features

### 🛍️ Customer Storefront
- **Live product menu** loaded from MySQL via REST API
- **Category filter tabs** — Brownies, Cakes, Cupcakes, Specials
- **Shopping cart** with persistent localStorage, quantity controls, live total
- **Order form** with cart summary, special notes, delivery date
- **Order submission** — saves to MySQL, returns Order ID
- Smooth scroll navigation, review cards, responsive footer

### 🔒 Admin Panel (`/admin-login.html`)
- **Password-protected** admin login (`Crumb@2025!`)
- **Dashboard** — total orders, revenue, pending count, recent activity
- **Products management** — add / edit / delete / toggle availability
- **Orders management** — view all, update status, delete, filter by status
- **Analytics tab** — revenue KPIs, status breakdown chart, top products, 7-day breakdown, product performance table
- Auto-refresh every 30 seconds

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2 |
| Database | MySQL |
| ORM | Spring Data JPA |
| Validation | Jakarta Bean Validation |
| Frontend | HTML5, Vanilla CSS, Vanilla JS |
| Build | Maven |

---

## 📁 Project Structure

```
cake-brownie-shop/
├── src/
│   ├── main/
│   │   ├── java/com/crumb/bakery/
│   │   │   ├── controller/
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── PageController.java
│   │   │   ├── model/
│   │   │   │   ├── Order.java
│   │   │   │   └── Product.java
│   │   │   ├── repository/
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── ProductRepository.java
│   │   │   └── service/
│   │   │       ├── OrderService.java
│   │   │       └── ProductService.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html          ← Customer store
│   │       │   ├── admin.html          ← Admin panel
│   │       │   ├── admin-login.html    ← Admin login
│   │       │   └── admin-analytics.html← Redirects to admin.html
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL Server (running on localhost:3306)

### 1. Clone the repo
```bash
git clone https://github.com/majose03/cake-brownie-shop.git
cd cake-brownie-shop
```

### 2. Configure MySQL

Ensure your local MySQL server is running. Create a database named `bakerydb`:
```sql
CREATE DATABASE bakerydb;
```

The application is pre-configured to log in as user `root` with password `root`. Update `src/main/resources/application.properties` if your credentials differ.

### 3. Run the app
```bash
mvn spring-boot:run
```

App starts at: **http://localhost:8080**

---

## 🔗 API Endpoints

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | All available products (public) |
| `GET` | `/api/products/all` | All products including hidden (admin) |
| `GET` | `/api/products/{id}` | Product by ID |
| `GET` | `/api/products/category/{cat}` | Filter by category |
| `GET` | `/api/products/search?q=` | Search by name |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/orders` | All orders |
| `GET` | `/api/orders/{id}` | Order by ID |
| `GET` | `/api/orders/by-email/{email}` | Orders by customer email |
| `GET` | `/api/orders/by-status/{status}` | Orders by status |
| `GET` | `/api/orders/summary` | Count by status |
| `GET` | `/api/orders/health` | Health check |
| `POST` | `/api/orders` | Place new order |
| `PUT` | `/api/orders/{id}/status?status=` | Update order status |
| `DELETE` | `/api/orders/{id}` | Delete order |

### Order Status Flow
```
pending → confirmed → preparing → out-for-delivery → delivered
                                                   ↘ cancelled
```

---

## 📮 Postman Testing

### Health Check
```
GET http://localhost:8080/api/orders/health
```

### Place an Order (POST body)
```json
{
  "name": "Arjun K",
  "phone": "9876543210",
  "email": "arjun@example.com",
  "item": "Dark Chocolate Fudge Brownie x2",
  "quantity": 2,
  "unitPrice": 299,
  "totalPrice": 598,
  "address": "42 MG Road, Bangalore 560001",
  "optionalItems": "gift wrap",
  "notes": "Happy Birthday! Write Meera on the box."
}
```

### Add a Product (POST body)
```json
{
  "name": "Mango Passion Cake",
  "description": "Real Alphonso mango layered cake.",
  "price": 799,
  "category": "cake",
  "imageEmoji": "🥭",
  "badge": "Summer Special",
  "available": true,
  "sortOrder": 5
}
```

---

## 🔐 Admin Access

| | |
|-|-|
| **Login URL** | http://localhost:8080/admin-login.html |
| **Password** | `Crumb@2025!` |
| **Session** | Stored in browser `localStorage` |

> ⚠️ Change the password in `admin-login.html` before deploying to production.

---

## 🗄️ Database

- **Type:** MySQL (Relational)
- **Database name:** `bakerydb`
- **Tables:** `products`, `orders`
- **View data:** Use [MySQL Workbench](https://www.mysql.com/products/workbench/) or DBeaver → connect to `jdbc:mysql://localhost:3306/bakerydb`

---

## 📸 Screenshots

| Store | Admin Panel |
|-------|-------------|
| Hero section with product cards | Dashboard with live stats |
| Cart drawer with item controls | Analytics — revenue, top products |
| Order form with cart summary | Orders management with status |

---

## 🧑‍💻 Author

**majose03** — Built with ☕ and 🍫 in Bangalore

---

## 📄 License

MIT License — free to use, modify, and distribute.
