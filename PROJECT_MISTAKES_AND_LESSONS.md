# 🛑 CRUMB 2.0 - Mistakes & Solutions Guide
(A complete guide to never repeating the same mistakes in your next project)

This document maps out every major bug, blocker, and mistake we ran into while building this full-stack application, how we fixed it, and the golden rules to avoid it moving forward.


---

## Mistake 1: "Web server failed to start. Port 8080 was already in use."
**What Happened:** 
When hitting "Run" in VS Code/Eclipse, Spring Boot crashed immediately with a "Port 8080 in use" error.
**Why it Happened:** 
You ran the application, but didn't stop the previous version properly. An invisible Java process was still running in the background holding Port 8080 hostage.
**The Fix:** 
We had to open the terminal and forcefully kill the process stuck on port 8080.
**How to Avoid Next Time:**
Always click the bright red "Stop" (Square) button in your IDE's run panel before pressing "Run" or "Debug" again. If your IDE crashes, you must restart your computer or kill the Java task manually.

---

## Mistake 2: Leaking Database Passwords to GitHub
**What Happened:** 
When we tried to run `git push origin main` after setting up the Aiven online database, GitHub threw a massive red "Push Protection" error and blocked the upload.
**Why it Happened:** 
The `application.properties` file contained the raw text password (`AVNS_TMCs07DGiu...`) for your live database. GitHub's security scanners instantly detect passwords and block you from uploading them to public repositories, preventing hackers from stealing your data.
**The Fix:** 
We wiped the password from the text file and replaced it with an environment variable placeholder: `${AIVEN_DB_PASSWORD}`.
**How to Avoid Next Time:**
Never, ever write unencrypted passwords, API keys, or secret tokens directly into a file being tracked by `git`. Always use Environment Variables (`.env`) for live credentials and tell your hosting provider (Render/AWS) what the password actually is.

---

## Mistake 3: The "Login Bypass" Illusion (Admin Password)
**What Happened:** 
You felt like the javascript password logic for `Crumb@2025!` wasn't working because the admin panel opened immediately without asking for a password.
**Why it Happened:** 
The code was perfectly fine, but the browser's `localStorage` had permanently saved a token saying `{crumb_admin_v2_auth: "ok"}` from your past visit. Because the Admin Panel didn't have a `logout()` button, there was no way to delete this token, causing the browser to permanently bypass the login screen.
**The Fix:** 
We changed the secret token name to `crumb_admin_v3_auth` (to instantly invalidate everyone's browsers) and wrote a proper `logout()` function.
**How to Avoid Next Time:**
When writing Authentication logic, ALWAYS test it using a **Private / Incognito Window**. Incognito windows start with a perfectly blank `localStorage`, proving whether your password logic actually works. Also, always build a "Logout" button first!

---

## Mistake 4: Docker Build Failure on Render ("manifest not found")
**What Happened:** 
When deploying the application to Render, the build process completely failed exactly at `Step 5: FROM openjdk:17-jdk-slim`.
**Why it Happened:** 
The official `openjdk` Docker images were officially deprecated and deleted from Docker Hub's default registries by the maintainers. Render couldn't download a file that didn't exist anymore.
**The Fix:** 
We changed the Dockerfile to use the modern, officially supported open-source image: `eclipse-temurin:17-jre-alpine`.
**How to Avoid Next Time:**
Docker images expire and get deprecated constantly. Always Google "Official Docker image for [Technology]" right before creating a `Dockerfile` to ensure you are using the modern tag. As a bonus, using `-alpine` versions makes your app run 10x faster online.

---

## Mistake 5: Spring Boot Circular Dependency
**What Happened:** 
(Early in the project) Spring Boot refused to start because it complained about a "Circular Dependency". 
**Why it Happened:** 
The `SecurityConfig.java` file needed the `UserService`, but the `UserService` needed the `SecurityConfig` (to read the password encoder). They were waiting on each other in an infinite loop.
**The Fix:** 
We broke the loop by separating the `PasswordEncoder` into its own isolated bean and injecting it via constructor.
**How to Avoid Next Time:**
In Backend Engineering, draw out how your Services are connected. Avoid letting Service A talk to Service B if Service B also needs to talk to Service A. 

---

## Mistake 6: JSON Object Mismatch (The Pricing Bug 0.0)
**What Happened:** 
When submitting an order, the database correctly saved the customer name but saved the price as `0.0`. 
**Why it Happened:** 
The Frontend JavaScript was sending a simple text string (`"item": "Brownie"`) through the `fetch()` API, but the Java Backend was expecting a complex array of `OrderItem` objects. Because Java didn't understand the frontend data shape, it defaulted the prices to zero.
**The Fix:** 
We patched the `BakeryService.java` to detect if the items array was missing and gracefully process the total price anyway.
**How to Avoid Next Time:**
Whenever you build a Full-Stack app, the JSON format inside your Javascript `fetch()` body MUST exactly, 100% reflect the variables inside your Java Model class. If Java expects an Array list, Javascript must send an Array list.

---

## 🏆 The Ultimate Cheat-Code for your Next Project:
1. Start with the **Database Model** (Entities).
2. Build the **Repository**, then the **Service**, then the **Controller**.
3. Use **Postman** to hit your endpoints and prove the backend works flawlessly.
4. *Only then* write the Frontend HTML/JS and `fetch()` the data.
5. If something breaks, read the very first red line of the Terminal logs. It always tells you exactly which file line number failed.
