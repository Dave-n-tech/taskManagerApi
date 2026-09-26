# Task Manager API

A production-ready **Spring Boot RESTful API** for managing tasks, designed with clean architecture, strict input validation, central error handling, dynamic data chunking, and separate isolated database profiles.

## Features

- **Full CRUD Operations:** Create, read, update, and delete tasks.
- **Advanced Pagination & Sorting:** Fetch data efficiently with configurable chunk windows and optional sorting parameters.
- **Robust Field Validation:** Catch missing or invalid request payloads safely before business logic execution.
- **Centralized Error Handling:** Global exception handling wrapping all error types uniformly into structured API payloads.
- **Comprehensive Testing:** Complete test isolation with Unit Tests for business layers and Web Integration Tests (`MockMvc`) for REST routing.

---

## Architecture & Technology Stack

- **Java 17 / 21**
- **Framework:** Spring Boot 3.x (Web, Data JPA, Validation)
- **Production Database:** MySQL
- **Testing Database:** H2 (In-Memory)
- **Build Tool:** Maven

---

## Database Configuration & Profile Environments

The application uses an isolated multi-profile strategy to keep development data persistent and testing operations fast and stateless.

### Environment Breakdown

| Profile | Database Target | Target Property File | Environment Focus |
| :--- | :--- | :--- | :--- |
| **Development** (`local`) | **MySQL** (`task_manager_db`) | `src/main/resources/application.properties` | App Runtime Data Persistence |
| **Testing** (`test`) | **H2 In-Memory** | `src/test/resources/application-test.properties` | Discardable Fast Automated Test Executions |

### Configuration Properties

1. **`src/main/resources/application.properties`**
   This file activates the **`local`** profile which powers your active runtime connections targeting a local MySQL server instance:
   ```properties
   spring.profiles.active=local

   spring.datasource.url=jdbc:mysql://localhost:3306/task_manager_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   ```

2. **`src/test/resources/application-test.properties`**
   This test property isolation file houses the variables used exclusively during text executions via `mvn test`, decoupling test operations from touching your production/dev MySQL data:
   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
   spring.datasource.driver-class-name=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.jpa.hibernate.ddl-auto=update
   ```

---

## API Endpoints Spec & Data Shapes

### 1. Create Task
* **Method:** `POST`
* **Endpoint:** `/api/tasks`
* **Request Body (`TaskRequestDto`):**
  ```json
  {
    "title": "Build Spring Boot API",
    "description": "Implement CRUD operations, validation, and tests.",
    "status": "PENDING"
  }
  ```
* **Response Body (`201 Created`):**
  ```json
  {
    "id": 1,
    "title": "Build Spring Boot API",
    "description": "Implement CRUD operations, validation, and tests.",
    "status": "PENDING",
    "createdAt": "2026-09-26T14:45:00"
  }
  ```

### 2. Get All Tasks (Paged & Sorted)
* **Method:** `GET`
* **Endpoint:** `/api/tasks`
* **Query Parameters:** `page` (default 0), `size` (default 10), `sortBy` (default `none`), `direction` (default `ASC`)
* **Request Body:** None
* **Response Body (`200 OK`):**
  ```json
  {
    "content": [
      {
        "id": 1,
        "title": "Build Spring Boot API",
        "description": "Implement CRUD operations, validation, and tests.",
        "status": "PENDING",
        "createdAt": "2026-09-26T14:45:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```

### 3. Get Task By ID
* **Method:** `GET`
* **Endpoint:** `/api/tasks/{id}`
* **Request Body:** None
* **Response Body (`200 OK`):**
  ```json
  {
    "id": 1,
    "title": "Build Spring Boot API",
    "description": "Implement CRUD operations, validation, and tests.",
    "status": "PENDING",
    "createdAt": "2026-09-26T14:45:00"
  }
  ```

### 4. Update Task
* **Method:** `PUT`
* **Endpoint:** `/api/tasks/{id}`
* **Request Body (`TaskRequestDto`):**
  ```json
  {
    "title": "Build Spring Boot API",
    "description": "Optimized database layer and completed tests.",
    "status": "IN_PROGRESS"
  }
  ```
* **Response Body (`200 OK`):**
  ```json
  {
    "id": 1,
    "title": "Build Spring Boot API",
    "description": "Optimized database layer and completed tests.",
    "status": "IN_PROGRESS",
    "createdAt": "2026-09-26T14:45:00"
  }
  ```

### 5. Delete Task
* **Method:** `DELETE`
* **Endpoint:** `/api/tasks/{id}`
* **Request Body:** None
* **Response Body (`24 No Content`):** Empty Response

---

## Standard Error Formats

All error handling flows consistently through a root wrapper payload structure:

### Field Validation Error (`400 Bad Request`)
```json
{
  "response": {
    "title": "Title is required",
    "description": "Description must be under 500 characters"
  }
}
```

### Resource Not Found Error (`404 Not Found`)
```json
{
  "response": {
    "error": "Task not found with id: 99"
  }
}
```

---

## Getting Started Locally

### Prerequisites
- Install Java 17+ and Maven
- Have a local MySQL Server instance active running on default port `3306`

### 1. Build and Run Application
```bash
# Clone the repository
git clone <your-repo-url>
cd TaskManagerApi

# Build and package the executable artifact
mvn clean install

# Spin up the Spring Boot application
mvn spring-boot:run
```

### 2. Run Test Suite
```bash
# Triggers both unit tests and controller integration tests over H2 instantly
mvn test
```
