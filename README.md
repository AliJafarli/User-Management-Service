# User Management Service

A Spring Boot REST API service for managing users. The service supports CRUD operations, paging, and logging. Deployed on [Render](https://render.com).

---

## Table of Contents
- [Technologies](#technologies)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Admins](#admins)
- [API Endpoints](#api-endpoints)
- [Example API Calls](#example-api-calls)

---

## Technologies
- Java 20
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- Jakarta Validation
- PostgreSQL (via Render managed database)
- Liquibase for DB migrations
- Docker & Docker Compose
- SLF4J for logging

---

## Setup

The deployment uses the provided `Dockerfile`.

**Render deployment URL:**  
`https://user-management-service-rgfs.onrender.com`

---

## Running the Application

The application is running in a Docker container on Render. All endpoints are available via the base URL:

https://user-management-service-rgfs.onrender.com/api/users

---

## Project Structure (simplified)

User Management Service
├─ src/main/java/com/looyt/usermanagementservice
│ ├─ controller
│ │ └─ UserController.java
│ ├─ service
│ │ ├─ UserService.java
│ │ └─ impl/UserServiceImpl.java
│ ├─ model
│ │ ├─ User.java
│ │ └─ UserRole.java
│ ├─ dto
│ │ ├─ UserRequest.java
│ │ ├─ UserResponse.java
│ │ └─ PageResponse.java
│ ├─ repository
│ │ └─ UserRepository.java
│ ├─ mapper
│ │ └─ UserMapper.java
│ ├─ exception
│ │ ├─ NotFoundException.java
│ │ ├─ AccessDeniedException.java
│ │ └─ DataExistException.java
│ ├─ advice
│ │ └─ GlobalExceptionHandler.java
│ └─ util
│ └─ RoleChecker.java
├─ src/main/resources/db/changelog
│ ├─ db.changelog-master.yml
│ └─ changes
│ └─ V1__create_users_table.yml
├─ Dockerfile
├─ docker-compose.yml
├─ build.gradle
└─ README.md

---

## Admins

The system includes two admin users:

| ID  | Username |
|-----|---------|
| 3   | Admin   |
| 4   | Ali     |

---

## API Endpoints

Base URL: `https://user-management-service-rgfs.onrender.com`

| Method | Endpoint            | Description                     |
|--------|-------------------|---------------------------------|
| GET    | `/api/users`       | Get paginated list of users     |
| GET    | `/api/users/{id}`  | Get user by ID                  |
| POST   | `/api/users`       | Create a new user               |
| PUT    | `/api/users/{id}`  | Update user by ID               |
| DELETE | `/api/users/{id}`  | Delete user by ID               |
| GET    | `/api/users/health`| Health check endpoint           |

---

## Example API Calls (Postman style)

### Get All Users
**Method:** GET  
**URL:** `https://user-management-service-rgfs.onrender.com/api/users`  

_No headers or body required._

---

### Get User by ID
**Method:** GET  
**URL:** `https://user-management-service-rgfs.onrender.com/api/users/6`  

_No headers or body required._

---

### Create User
**Method:** POST  
**URL:** `https://user-management-service-rgfs.onrender.com/api/users`  

**Headers:**  
Content-Type: application/json
X-User-Id: 3 or 4 (who has role ADMIN)

**Body (raw JSON):**

{
  "username": "john_doe",
  "password": "securePass123",
  "email": "john@example.com",
  "phone": "1234567890",
  "role": "USER"
}

---

### Update User
**Method:** PUT
**URL:** `https://user-management-service-rgfs.onrender.com/api/users/6`

**Headers:**
Content-Type: application/json
X-User-Id: 3 or 4 (who has role ADMIN)

**Body (raw JSON):**

{
  "username": "john_updated",
  "password": "newPass456",
  "email": "john_updated@example.com",
  "phone": "0987654321",
  "role": "ADMIN"
}

---

### Delete User
**Method:** DELETE
**URL:** `https://user-management-service-rgfs.onrender.com/api/users/1`

**Headers:**
X-User-Id: 3

---

### Health Check
**Method:** GET
URL: `https://user-management-service-rgfs.onrender.com/api/users/health`

No headers or body required.

---

Notes
POST, PUT, and DELETE endpoints require the X-User-Id header to indicate the current user performing the action.

Paging is supported via page and size query parameters.

Sorting is supported via sort query parameter, e.g., sort=createdAt,desc.
