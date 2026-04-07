# Vela Route - Logistics Management System

Vela Route is a high-performance backend application designed to manage and track global shipments. Built with a focus on **type safety**, **scalability**, and **enterprise design patterns**, this project marks my transition from the MERN stack into the robust world of Java and Spring Boot.

## 🚀 The Mission
To build a logistics dashboard that handles real-time data filtering and optimized search while maintaining strict architectural standards.

## 🛠️ Tech Stack & Environment
* **Language:** Java 21+
* **Framework:** Spring Boot 4.x
* **Database:** PostgreSQL (Running in Docker)
* **Environment:** macOS (ZSH / Homebrew)
* **Dev Tools:** IntelliJ IDEA, Docker Desktop, Postman

---

## 🧠 Key Learnings & Architecture

### 1. The Layered Pattern (Tiered Architecture)
Unlike the more fluid structure of Express.js, Vela Route follows a strict tiered approach to ensure a clean separation of concerns:
* **Controller:** Manages REST endpoints and handles HTTP serialization/deserialization.
* **Service:** The "Brain." Contains all business logic and orchestrates data flow.
* **Repository:** Uses **Spring Data JPA** to interface with PostgreSQL, removing the need for manual SQL boilerplate.
* **Model:** Defines the data structure (Entities) using JPA annotations.

### 2. Enterprise-Grade Search (JPA Specifications)
I implemented a dynamic search system using the **JPA Specification Executor**. 
* **The Problem:** Standard repositories require a new method for every search combination.
* **The Solution:** Using `CriteriaBuilder` to create reusable "LEGO bricks" of logic. This allows users to filter by `status`, `destination`, or both, without polluting the repository with messy `if/else` logic.
* **Fixing Ambiguity:** I mastered Java's strict typing by resolving `Specification.where(null)` ambiguity using `cb.conjunction()` to ensure a 100% type-safe search foundation.

### 3. Global Exception Handling
To ensure a professional user experience, I moved away from local `try/catch` blocks.
* **@ControllerAdvice:** Implemented a global "Security Guard" that watches the entire application for errors.
* **Custom Exceptions:** Created `ResourceNotFoundException` to return standardized, clean JSON error messages (404/500) instead of raw Java stack traces.

### 4. Containerization with Docker
I use **Docker Compose** to manage the PostgreSQL environment. This ensures that the development environment is identical regardless of the machine, making the setup portable and production-ready.

---

## 🚦 Getting Started (macOS)

### Prerequisites
1.  Ensure **Docker Desktop** is running.
2.  Install **HTTPie** for easier testing: `brew install httpie`

### Running the App
1.  Spin up the database:
    ```zsh
    docker-compose up -d
    ```
2.  Run the application via IntelliJ IDEA.
3.  The API will be live at `http://localhost:8080/api/shipments`.

### Example API Usage
**Create a Shipment:**
```zsh
http POST :8080/api/shipments trackingNumber="VELA-TX-101" origin="Miami" destination="Austin" status="PENDING"
