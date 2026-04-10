# 🌾 AgroValue Connect – Backend

A **Spring Boot–based REST API** powering AgroValue Connect, a farmer–buyer marketplace platform.
This backend provides secure authentication, product and order management, file uploads, and scalable architecture.

---

## 🚀 Features

* 🔐 JWT Authentication & Role-Based Authorization (Admin, Farmer, Buyer)
* 🌐 Google OAuth Login
* 📦 CRUD APIs (Products, Users, Orders, Cart, Wishlist)
* 🖼️ Multipart File Upload (Product Images)
* 🔗 JPA & Hibernate (Entity Relationships)
* 🚨 Global Exception Handling
* 🪵 Application Logging (SLF4J + Logback)
* 🔑 Single Sign-On (SSO via JWT)
* 📄 RESTful API Design (Clean Architecture)

---

## 🏗️ Tech Stack

* **Backend:** Spring Boot
* **Database:** MySQL / PostgreSQL
* **ORM:** Hibernate (JPA)
* **Security:** Spring Security + JWT
* **OAuth:** Google OAuth 2.0
* **Build Tool:** Maven

---

## 📁 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 ├── dto/
 ├── config/
 ├── security/
 ├── exception/
 └── util/
```

---

## 🔐 Authentication Flow

1. User logs in (email/password or Google OAuth)
2. Backend validates credentials
3. JWT token is generated
4. Client stores token
5. Token used in headers for protected APIs

---

## 📡 API Endpoints

### 🔑 Auth

* POST `/auth/register`
* POST `/auth/login`
* GET `/auth/me`

### 📦 Products

* GET `/products`
* GET `/products/{id}`
* POST `/products`
* PUT `/products/{id}`
* DELETE `/products/{id}`

### 🛒 Cart

* GET `/cart`
* POST `/cart/add`
* DELETE `/cart/remove`

### 📦 Orders

* POST `/orders`
* GET `/orders`
* GET `/orders/{id}`

### ❤️ Wishlist

* GET `/wishlist`
* POST `/wishlist`
* DELETE `/wishlist/{id}`

### 🖼️ Upload

* POST `/upload`

---

## 🗄️ Database Design (Overview)

* User ↔ Role (Many-to-Many)
* User → Orders (One-to-Many)
* Order → Product (Many-to-One)
* Product ↔ Category (Many-to-Many)

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository

```
git clone <your-repo-link>
cd agrovalue-connect-backend
```

### 2️⃣ Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/agrovalue
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3️⃣ Run Application

```
mvn spring-boot:run
```

---

## 🧪 API Testing

* Use Postman OR Swagger UI
* Default URL:

```
http://localhost:8080
```

---

## 🧠 Key Concepts Implemented

* JWT & Role-Based Security
* OAuth 2.0 Integration
* REST API Design
* JPA Relationships
* Multipart File Handling
* Exception Handling
* Logging

---

## 📌 Future Enhancements

* Payment Integration
* Microservices Architecture
* Cloud Deployment (AWS)
* Real-time Notifications

---

## 👨‍💻 Author

Developed as part of B.Tech CSE Project – AgroValue Connect

---
