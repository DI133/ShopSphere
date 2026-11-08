# 🛒 ShopSphere – E-Commerce Backend

A robust and scalable e-commerce backend application developed using **Java and Spring Boot**. ShopSphere provides RESTful APIs to support customers, sellers, and administrators in managing an online shopping platform.

## 🎥 Project Demo

Watch the complete demonstration of ShopSphere below.

<!-- Upload your MP4 video here using GitHub's README editor. GitHub will generate a video attachment URL. -->

## 📌 Project Overview

ShopSphere is an e-commerce application designed to simplify online shopping and product management. It provides secure authentication, product management, shopping cart functionality, order processing, payment management, and seller-related operations.

The backend follows a REST API architecture and uses Spring Security and JWT for authentication and authorization.

## ✨ Key Features

### 👤 User Management
- User registration and login.
- JWT-based authentication.
- Role-based access control.
- Customer and seller management.

### 🛍️ Product Management
- Create, update, and manage products.
- Product categorization.
- Product image management.
- Product search functionality.

### 🛒 Shopping Cart
- Add products to cart.
- Update cart items.
- Remove items from cart.
- Manage cart information.

### 📦 Order Management
- Place and manage orders.
- Order item management.
- Track order-related information.

### 💳 Payment Management
- Payment-related API operations.
- Payment information management.

### 📊 Analytics
- Seller statistics.
- Sales and order-related analytics.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot | Backend framework |
| Spring Security | Application security |
| Spring Data JPA | Database operations |
| MySQL | Relational database |
| JWT | Authentication |
| Maven | Dependency management |
| REST APIs | Client-server communication |
| Docker | Containerization |

## 🏗️ Project Structure

```text
ShopSphere/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/project/ecommerce_app/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│           └── application.properties
│
├── Dockerfile
├── pom.xml
└── README.md
```

## ⚙️ Installation and Setup

### Prerequisites

Make sure you have installed:

- Java JDK
- Maven
- MySQL Server
- Git
- IntelliJ IDEA or any Java IDE

### 1. Clone the repository

```bash
git clone https://github.com/DI133/ShopSphere.git
```

### 2. Navigate to the project directory

```bash
cd ShopSphere
```

### 3. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE shopsphere;
```

Update your database configuration in `application.properties` using your own local credentials.

Do not commit actual passwords or secret keys to GitHub.

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The backend server will start on the configured application port.

## 🔐 Security

- JWT-based authentication.
- Spring Security integration.
- Role-based authorization.
- Secure password handling.

## 🔮 Future Enhancements

- API documentation using Swagger/OpenAPI.
- Automated unit and integration testing.
- Enhanced recommendation functionality.
- Cloud deployment and monitoring.

## 👩‍💻 Contributors

**Drashti Bhalodiya**

**Project:** ShopSphere – E-Commerce Application

## ⭐ Support

If you find this project interesting, consider giving this repository a ⭐ on GitHub!

---

*Developed as a software engineering project.*
