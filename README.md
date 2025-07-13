[//]: # (![challenge banner]&#40;image/amaliTechLab.webp&#41;)
<img src="image/AmalitTech.png" alt="drawing" style="height:350px; width: 1000px"/>
# Advanced Lab7: ByteBites Restaurant

![Static Badge](https://img.shields.io/badge/Framework_used-0-green?style=flat)
![GitHub followers](https://img.shields.io/github/followers/karangwaajika)
![GitHub file size in bytes](https://img.shields.io/github/size/karangwaajika/codeOfAfrica-challenges/index.html)


A mini microservices architecture for connecting customers to local restaurants.  
Features service discovery, centralized config, JWT security, RBAC, and event-driven communication.

---

## 📦 **Overview**

ByteBites is a demo platform that showcases modern distributed systems design:
- Microservices for authentication, restaurants, orders, and notifications.
- Spring Cloud Config and Eureka for centralized configuration & discovery.
- API Gateway for secure routing and JWT validation.
- Role-Based Access Control (RBAC): `ROLE_CUSTOMER`, `ROLE_RESTAURANT_OWNER`, `ROLE_ADMIN`.
- Async communication using Kafka/RabbitMQ.
- Circuit breakers for resilience.

---

## 🧩 **Microservices**

| Service | Description |
|--------|-------------|
| **discovery-server** | Eureka registry to register & discover microservices |
| **config-server** | Centralized configuration from Git-backed repo |
| **api-gateway** | Single entry point; validates JWT and routes requests |
| **auth-service** | Handles login, registration, issues JWT tokens |
| **restaurant-service** | Manage restaurant info and menus |
| **order-service** | Place and track food orders |
| **notification-service** | Listens for events and simulates email/push notifications |

---

## 🔐 **Authentication & Authorization**

- **JWT-based security** issued by `auth-service`.
- API Gateway intercepts requests, validates tokens, and forwards claims as headers.
- **RBAC enforced** in downstream services using Spring Security annotations like `@PreAuthorize`.
- Example rules:
  - Only `ROLE_CUSTOMER` can place orders.
  - Only `ROLE_RESTAURANT_OWNER` can modify their own restaurants.
  - `ROLE_ADMIN` can manage users and perform admin tasks.

---

## ⚙ **System Design Highlights**

- Central config via Spring Cloud Config.
- Service discovery using Eureka.
- Kafka or RabbitMQ for async events (e.g., order placed notifications).
- Circuit breakers and resilience patterns with Spring Cloud.

---

## 🛠 **Planned Features / To-Do**

### ✅ Core Infrastructure
- [ ] `discovery-server` (Eureka)
- [ ] `config-server` (Git-backed configs)
- [ ] `api-gateway` (JWT validation, routing)

### ✅ Authentication & Users
- [ ] `auth-service`
  - BCrypt password hashing
  - JWT issuance with roles & expiry
  - Default `ROLE_CUSTOMER` registration

### ✅ Business Services
#### 🥘 `restaurant-service`
- CRUD restaurants & menus
- Only owners can manage their restaurants

#### 🛒 `order-service`
- Place/view orders with statuses like `PENDING`, `CONFIRMED`, etc.
- Customers only see their orders
- Owners can view orders for their restaurants

### 📣 Event-Driven Messaging
- [ ] `order-service`: publish `OrderPlacedEvent`
- [ ] `notification-service`: listen & send notifications
- [ ] `restaurant-service`: listen to start order prep

---

## 📚 **Tech Stack**
- Java 21
- Spring Boot & Spring Cloud (Eureka, Config, Gateway)
- Spring Security & OAuth2
- Kafka or RabbitMQ
- PostgreSQL
- Maven
- Lombok

---

## 🚀 **Getting Started**

```bash
# Build all modules
mvn clean install

# Start config-server & discovery-server first
cd config-server && mvn spring-boot:run
cd discovery-server && mvn spring-boot:run

# Start other services
cd api-gateway && mvn spring-boot:run
cd auth-service && mvn spring-boot:run
cd restaurant-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run


## Expected Learning Outcomes

- Solidify understanding of Java concurrency primitives and memory consistency
- Develop real-world concurrency patterns (producer-consumer, thread pools)
- Detect and fix synchronization issues and race conditions
- Build and test a robust multithreaded system with real-time status tracking
