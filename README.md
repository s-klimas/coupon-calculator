# 🎟️ Coupon Calculator

> 🧮 Intelligent backend service for optimizing coupon applications, powered by **Spring Boot**, **Java Streams**, **Heuristic Algorithms**, and **Swagger/OpenAPI**.

---

## 🚀 Tech Stack

| Category       | Technologies                                                                                                                                                                                                                                                                                     |
|----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Backend**    | ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)                                                                                            |
| **Algorithms** | ![Streams](https://img.shields.io/badge/Java_Streams-007396?style=for-the-badge&logo=openjdk&logoColor=white) ![Bitwise](https://img.shields.io/badge/Bitwise_Operations-2E86C1?style=for-the-badge) ![Heuristics](https://img.shields.io/badge/Heuristic_Algorithms-F39C12?style=for-the-badge) |
| **Tools**      | ![Lombok](https://img.shields.io/badge/Lombok-A50?style=for-the-badge) ![PriorityQueue](https://img.shields.io/badge/PriorityQueue-34495E?style=for-the-badge)                                                                                                                                   |
| **Docs**       | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)                                                                                                                                                                                         |
| **Testing**    | ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge) ![Mockito](https://img.shields.io/badge/Mockito-46B6AC?style=for-the-badge) ![AssertJ](https://img.shields.io/badge/AssertJ-0069C0?style=for-the-badge)                                                                |

---

## 📝 Description

**Coupon Calculator** is a backend service that calculates optimal ways to apply coupons to a list of products. It:

- 🧩 Generates all subsets of products using **bitwise operations**
- 🔄 Finds **valid coupon combinations** to cover the product set without reuse
- 💰 Calculates the **best possible solution*** with minimal total price
- 🎯 Integrates **heuristic algorithms** to improve performance on large datasets
- 💧 Uses **backtracking and Java Streams** to explore coupon applications efficiently
- 📄 Exposes a clean **REST API** documented via **Swagger/OpenAPI**
- ⚡ Handles large inputs by processing them in parallel using **Java Streams**

The service balances performance by chunking large product/coupon lists into smaller sets for processing and applying
heuristic optimizations to avoid exhaustive computation.

<sub><sup>* for small collections (depending on the variables `MAX_SIZE_PRODUCT_BUNCHES` and `MAX_SIZE_COUPON_BUNCHES`),
for larger collections, an optimally good solution</sup></sub>

---

## 💡 Interesting Techniques

- **Bitwise Subset Generation**  
  The `CombinationGenerator` class uses **bitwise shifts and masks** to generate all possible non-empty subsets of
  products efficiently — a concise and classic approach to power set generation.

- **Backtracking with Java Streams**  
  The `CouponOptimizer` employs **recursive backtracking** combined with **parallel Java Streams** (`parallelStream()`)
  to test all valid coupon assignments concurrently, optimizing large-scale performance.

- **PriorityQueue for Best Result Tracking**  
  The API service tracks the best coupon application using a **PriorityQueue** to efficiently update and retrieve the
  lowest total-cost order in real time.

- **Immutable Data Models via Lombok**  
  Models like `Product`, `Coupon`, and `Subset` use Lombok annotations (`@Getter`, `@Setter`, `@NoArgsConstructor`,
  `@AllArgsConstructor`) to reduce boilerplate and improve readability.

- **Swagger/OpenAPI Documentation**  
  The REST API is fully documented using **Swagger annotations**, configured in `SwaggerConfig`, enabling live API
  exploration and automatic client generation.

- **Heuristic Optimization for Scalability**  
  To handle large datasets, the service introduces **heuristic algorithms** that approximate the best coupon assignments
  without evaluating every possibility.

---

## 🧩 Project Structure

```
/src
  /main/java/pl/sebastianklimas/couponcalculator
    /config          → Swagger/OpenAPI configuration
    /controllers     → REST API endpoints
    /models          → Domain models (Product, Coupon, Subset, etc.)
    /services        → Core business logic and algorithms
  /test/java/pl/sebastianklimas/couponcalculator
    /services        → Unit tests with JUnit5, Mockito, AssertJ
```

- **/config** – sets up Swagger/OpenAPI documentation
- **/controllers** – defines REST endpoints for coupon calculation and result retrieval
- **/models** – defines data entities with Lombok and Swagger annotations
- **/services** – core algorithmic logic (subset generation, optimization)
- **/test** – unit tests covering all core features

---

## 🏫 Notable Technologies and Libraries

- **Lombok** – Reduces boilerplate for data classes
- **Spring Web** – REST API layer and dependency injection
- **Java Streams API** – Functional-style and parallel processing
- **Heuristic Algorithms** – Enhance scalability for large product and coupon sets
- **Swagger/OpenAPI** – Interactive API documentation
- **JUnit 5, Mockito, AssertJ** – Comprehensive testing stack

---

## 🧠 Key Features

✅ Efficient subset generation via bitwise logic  
✅ Recursive and parallel coupon optimization  
✅ Heuristic algorithms for scalable optimization  
✅ Complete REST API with Swagger/OpenAPI documentation  
✅ Immutable models with Lombok  
✅ Fully tested with JUnit, Mockito, and AssertJ
