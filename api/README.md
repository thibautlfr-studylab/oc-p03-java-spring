# ChâTop API - Backend REST API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

> Backend REST API for ChâTop, a seasonal rental property portal connecting tenants and property owners in the Basque coast tourist area.

## Table of Contents

- [About the Project](#about-the-project)
- [Quick Start](#quick-start)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Database Setup](#2-database-setup)
  - [3. Environment Configuration](#3-environment-configuration)
  - [4. Build and Run](#4-build-and-run)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Error Handling](#error-handling)
- [Project Structure](#project-structure)
- [Development](#development)
- [Testing](#testing)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)

## About the Project

**ChâTop API** is the backend component of a full-stack rental property portal developed as part of OpenClassrooms Project #3. This Spring Boot application provides a complete REST API with:

- JWT-based authentication and authorization
- CRUD operations for rental properties
- User management and messaging system
- Image upload and storage
- MySQL database integration
- Complete OpenAPI/Swagger documentation
- Standardized error handling using RFC 9457 (Problem Details for HTTP APIs)

The API is designed to work seamlessly with an Angular 14 frontend (available in the parent directory).

---

## Quick Start

### Option 1: Using Makefile ⚡ (Recommended)

The easiest way to set up everything from the **project root**:

```bash
# From project root (not api/ directory)
cd ..

# Start everything (auto-setup on first run)
make start-all
```

This **single command** automatically:
- ✅ Detects first run and creates `.env` with secure JWT secret
- ✅ Installs frontend dependencies (if needed)
- ✅ Starts MySQL with Docker and waits until ready
- ✅ Starts both frontend and backend services

**Useful Makefile commands** (run from project root):
```bash
make stop          # Stop all services
make status        # Check services status
make logs-backend  # View backend logs
make help          # See all available commands
```

---

### Option 2: Manual Setup

Get the backend up and running manually:

### 1. Database Setup

**Choose one of the following methods:**

#### Option A: Using Docker (Recommended)

```bash
# Navigate to backend directory
cd api

# Start MySQL container (creates database and tables automatically)
docker-compose up -d

# Verify container is running
docker-compose ps
```

This automatically:
- Creates the `chatop` database
- Runs the SQL schema from `../ressources/sql/script.sql`
- Configures root user with no password
- Handles case-insensitive table names

#### Option B: Using Local MySQL

> Need MySQL 8.0+ installed and running

```bash
# Create database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Run SQL schema
mysql -u root -p chatop < ../ressources/sql/script.sql
```

### 2. Environment Configuration

```bash
# Copy environment template
cp .env.example .env

# Edit .env with your configuration
```

**For Docker setup:**
```properties
DB_URL=jdbc:mysql://localhost:3306/chatop
DB_USERNAME=root
DB_PASSWORD=
JWT_SECRET=your-super-secret-jwt-key
```

**For local MySQL setup:**
```properties
DB_URL=jdbc:mysql://localhost:3306/chatop
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=your-super-secret-jwt-key
```

### 3. Run the Application

```bash
# Build and run
./mvnw spring-boot:run
```

Backend will start on **http://localhost:3001**

**Swagger UI**: [http://localhost:3001/api/swagger-ui/index.html](http://localhost:3001/api/swagger-ui/index.html)

📘 **Docker users**: See [DOCKER.md](./DOCKER.md) for detailed Docker setup and troubleshooting.

For detailed installation instructions, see [Installation](#installation) section below.

---

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17 | Programming language |
| **Spring Boot** | 3.5.6 | Application framework |
| **Spring Security** | 6.x | Authentication & authorization |
| **Spring Data JPA** | 3.x | Database ORM |
| **MySQL** | 8.0+ | Relational database |
| **JJWT** | 0.12.3 | JWT token generation/validation |
| **Springdoc OpenAPI** | 2.7.0 | API documentation |
| **Lombok** | Latest | Boilerplate code reduction |
| **Maven** | 3.6+ | Build tool |

## Architecture

The application follows a **layered MVC architecture** with clear separation :

```
┌─────────────────────────────────────┐
│         Controllers                 │  ← Handle HTTP requests/responses
│  (REST endpoints, validation)       │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│          Services                   │  ← Business logic
│  (Core functionality)               │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│        Repositories                 │  ← Data access layer
│  (Spring Data JPA)                  │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│      MySQL Database                 │  ← Persistent storage
└─────────────────────────────────────┘
```

**Key Design Patterns:**
- **DTO Pattern**: All API responses use DTOs (never expose entities directly)
- **Repository Pattern**: Spring Data JPA for database abstraction
- **Service Layer**: Business logic separated from controllers
- **Security Filter Chain**: JWT authentication via Spring Security filters

## Prerequisites

Before installation, ensure you have:

- **Java 17** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** (comes with the Maven wrapper in this project) (`./mvnw`)
- **Git** for cloning the repository

**For database, choose one:**
- **Docker** (recommended) - [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
- **MySQL 8.0+** (alternative) - [Download MySQL](https://dev.mysql.com/downloads/mysql/)

To verify your installations:

```bash
java -version    # Should show Java 17+
mvn -version     # or ./mvnw -version  # Should show Maven 3.6+

# If using Docker:
docker --version        # Should show Docker 20+
docker-compose --version  # Should show Docker Compose 2+

# If using local MySQL:
mysql --version  # Should show MySQL 8.0+
```

---

## Installation

### 1. Clone the Repository

```bash
git https://github.com/thibautlfr-studylab/oc-p03-java-spring.git
cd oc-p03-java-spring/api
```

### 2. Database Setup

**Choose one of the following methods:**

#### Option A: Using Docker (Recommended)

Docker provides an isolated, consistent environment with automatic database initialization.

**Step 1: Start MySQL Container**

```bash
# From the api/ directory
docker-compose up -d
```

This command automatically:
- Downloads MySQL 8.0 image (if not already present)
- Creates and starts the `chatop-mysql` container
- Creates the `chatop` database with utf8mb4 encoding
- Executes the SQL schema from `../ressources/sql/script.sql`
- Configures root user with no password
- Sets up case-insensitive table names (`lower_case_table_names=1`)

**Step 2: Verify Container is Running**

```bash
docker-compose ps
```

You should see:
```
NAME            STATUS    PORTS
chatop-mysql    running   0.0.0.0:3306->3306/tcp
```

**Step 3: Verify Tables**

```bash
docker exec chatop-mysql mysql -u root chatop -e "SHOW TABLES;"
```

Expected output:
```
Tables_in_chatop
messages
rentals
users
```

**Docker Management Commands:**

```bash
# View MySQL logs
docker-compose logs -f mysql

# Stop the container
docker-compose down

# Stop and remove data (reset database)
docker-compose down -v

# Restart container
docker-compose restart
```

**Troubleshooting Docker Setup:**

- **Port 3306 already in use**: If you have MySQL running locally via Homebrew/apt:
  ```bash
  # macOS
  brew services stop mysql

  # Linux
  sudo systemctl stop mysql
  ```

- **See detailed Docker documentation**: [DOCKER.md](./DOCKER.md)

#### Option B: Using Local MySQL Installation

If you prefer to use your local MySQL installation:

**Step 1: Start MySQL Server**

Make sure your MySQL server is running:

```bash
# On macOS (if installed via Homebrew)
brew services start mysql

# On Linux
sudo systemctl start mysql

# On Windows
# Start MySQL from Services or MySQL Workbench
```

**Step 2: Create the Database**

Connect to MySQL and create the database:

```bash
mysql -u root -p
```

Then execute:

```sql
CREATE DATABASE IF NOT EXISTS chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit;
```

**Step 3: Run the SQL Schema**

Execute the provided SQL script to create tables:

```bash
mysql -u root -p chatop < ../ressources/sql/script.sql
```

**Step 4: Verify Installation**

Check that tables were created successfully:

```bash
mysql -u root -p -e "USE chatop; SHOW TABLES;"
```

Expected output:
```
+------------------+
| Tables_in_chatop |
+------------------+
| MESSAGES         |
| RENTALS          |
| USERS            |
+------------------+
```

---

**Database Schema (Both Options):**

- **USERS**: User accounts (id, email, name, password, created_at, updated_at)
- **RENTALS**: Rental properties (id, name, surface, price, picture, description, owner_id, created_at, updated_at)
- **MESSAGES**: Messages between users (id, rental_id, user_id, message, created_at, updated_at)

### 3. Environment Configuration

#### Step 1: Create .env File

Copy the example environment file:

```bash
cp .env.example .env
```

#### Step 2: Configure Environment Variables

Edit the `.env` file with your settings:

```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/chatop
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION=86400000
```

**Important:**
- Replace `your_mysql_password` with your actual MySQL root password
- Generate a secure JWT secret for production:
  ```bash
  openssl rand -base64 64
  ```
- **Never commit the .env file to version control** (already in .gitignore)

#### Configuration Details

The application uses Spring profiles. Configuration is in:
- `src/main/resources/application.properties` - Main configuration
- `.env` - Environment-specific secrets (not committed)

Key configurations:
```properties
# Server runs on port 3001 (required by Angular frontend proxy)
server.port=3001

# Database connection (uses .env variables)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 4. Build and Run

#### Using Maven Wrapper (Recommended)

The Maven wrapper ensures everyone uses the same Maven version:

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

#### Using System Maven

If you have Maven installed globally:

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```


The API is now running at `http://localhost:3001`

## API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation:

**Swagger UI**: [http://localhost:3001/api/swagger-ui/index.html](http://localhost:3001/api/swagger-ui/index.html)

This provides:
- Interactive API explorer
- Request/response examples
- Schema definitions
- Try-it-out functionality

### OpenAPI Specification

Access the raw OpenAPI JSON:

**OpenAPI JSON**: [http://localhost:3001/api/v3/api-docs](http://localhost:3001/api/v3/api-docs)

---

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |
| GET | `/api/auth/me` | Get current user info | Yes |

### Rentals

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/rentals` | List all rentals | Yes |
| GET | `/api/rentals/{id}` | Get rental details | Yes |
| POST | `/api/rentals` | Create new rental | Yes |
| PUT | `/api/rentals/{id}` | Update rental | Yes |

### Messages

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/messages` | Send message to owner | Yes |

### Users

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/user/{id}` | Get user information | Yes |

### Example Requests

#### Register a New User

```bash
curl -X POST http://localhost:3001/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "name": "Test User",
    "password": "test123"
  }'
```

#### Login

```bash
curl -X POST http://localhost:3001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "password": "test123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Get Current User (with JWT)

```bash
curl -X GET http://localhost:3001/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Create a Rental (with file upload)

```bash
curl -X POST http://localhost:3001/api/rentals \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "name=Beautiful Beach House" \
  -F "surface=120" \
  -F "price=250" \
  -F "description=Stunning beach house with ocean view" \
  -F "picture=@/path/to/image.jpg"
```

---

## Authentication

### JWT Token Flow

1. **Register/Login**: User sends credentials to `/api/auth/register` or `/api/auth/login`
2. **Token Generation**: Server validates credentials and generates a JWT token
3. **Token Storage**: Client stores the token (usually in localStorage)
4. **Authenticated Requests**: Client includes token in Authorization header: `Bearer <token>`
5. **Token Validation**: Spring Security filter validates token on each protected request

### Security Features

- **BCrypt Password Hashing**: All passwords encrypted with BCrypt
- **JWT Expiration**: Tokens expire after 24 hours (configurable)
- **Protected Routes**: All endpoints except `/api/auth/register` and `/api/auth/login` require authentication

---

## Error Handling

### RFC 9457 - Problem Details for HTTP APIs

This API implements **RFC 9457** (Problem Details for HTTP APIs) for standardized error responses. All errors return a consistent JSON structure following the standard:

```json
{
  "type": "https://api.chatop.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Rental with id 999 not found",
  "instance": "/api/rentals/999"
}
```

### Error Response Structure

| Field | Type | Description |
|-------|------|-------------|
| `type` | URI | A URI reference identifying the problem type |
| `title` | string | A short, human-readable summary of the problem |
| `status` | integer | HTTP status code |
| `detail` | string | Human-readable explanation specific to this occurrence |
| `instance` | URI | URI reference identifying the specific occurrence |

### Common Error Types

- **400 Bad Request**: Invalid input data or validation errors
- **401 Unauthorized**: Missing or invalid JWT token
- **403 Forbidden**: Valid token but insufficient permissions
- **404 Not Found**: Requested resource doesn't exist
- **409 Conflict**: Resource already exists (e.g., duplicate email)
- **500 Internal Server Error**: Unexpected server error

### Implementation

Error handling is centralized in `GlobalExceptionHandler.java` using Spring's `@ControllerAdvice` and the `ProblemDetail` class introduced in Spring Framework 6.

**Reference**: [RFC 9457 Specification](https://www.rfc-editor.org/rfc/rfc9457.html)

---

## Project Structure

```
api/
├── src/
│   ├── main/
│   │   ├── java/com/openclassrooms/chatop/api/
│   │   │   ├── config/                    # Configuration classes
│   │   │   │   ├── SecurityConfig.java       # Spring Security + JWT configuration
│   │   │   │   ├── SwaggerConfig.java        # OpenAPI/Swagger documentation setup
│   │   │   │   └── WebConfig.java            # CORS and web MVC configuration
│   │   │   │
│   │   │   ├── controller/                # REST API endpoints
│   │   │   │   ├── AuthController.java       # Authentication (register/login/me)
│   │   │   │   ├── RentalController.java     # Rental CRUD operations
│   │   │   │   ├── MessageController.java    # Message sending
│   │   │   │   └── UserController.java       # User information
│   │   │   │
│   │   │   ├── dto/                       # Data Transfer Objects (API contracts)
│   │   │   │   ├── request/                  # API request DTOs
│   │   │   │   │   ├── AuthRequest.java         # Login/register credentials
│   │   │   │   │   ├── MessageRequest.java      # Message creation
│   │   │   │   │   └── RentalRequest.java       # Rental creation/update
│   │   │   │   ├── response/                 # API response DTOs
│   │   │   │   │   ├── AuthResponse.java        # JWT token response
│   │   │   │   │   ├── ErrorResponse.java       # Error messages
│   │   │   │   │   ├── RentalListResponse.java  # Rentals list wrapper
│   │   │   │   │   └── SuccessResponse.java     # Success messages
│   │   │   │   ├── RentalDTO.java            # Rental data representation
│   │   │   │   └── UserDTO.java              # User data representation
│   │   │   │
│   │   │   ├── exception/                 # Custom exceptions and global handler
│   │   │   │   ├── GlobalExceptionHandler.java  # Centralized error handling
│   │   │   │   ├── BusinessValidationException.java
│   │   │   │   ├── InvalidFileException.java
│   │   │   │   ├── ResourceAlreadyExistsException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   │
│   │   │   ├── model/                     # JPA entities (database tables)
│   │   │   │   ├── User.java                 # USERS table
│   │   │   │   ├── Rental.java               # RENTALS table
│   │   │   │   └── Message.java              # MESSAGES table
│   │   │   │
│   │   │   ├── repository/                # Data access layer
│   │   │   │   ├── UserRepository.java       # User queries
│   │   │   │   ├── RentalRepository.java     # Rental queries
│   │   │   │   └── MessageRepository.java    # Message queries
│   │   │   │
│   │   │   ├── security/                  # JWT authentication
│   │   │   │   └── JwtAuthenticationFilter.java # Token validation filter
│   │   │   │
│   │   │   ├── service/                   # Business logic layer
│   │   │   │   ├── interfaces/               # Service contracts
│   │   │   │   │   ├── IAuthService.java
│   │   │   │   │   ├── IJwtService.java
│   │   │   │   │   ├── IRentalService.java
│   │   │   │   │   ├── IMessageService.java
│   │   │   │   │   ├── IUserService.java
│   │   │   │   │   └── IFileStorageService.java
│   │   │   │   ├── implementations/          # Service implementations
│   │   │   │   │   ├── AuthServiceImpl.java     # Authentication logic
│   │   │   │   │   ├── JwtServiceImpl.java      # JWT token generation/validation
│   │   │   │   │   ├── RentalServiceImpl.java   # Rental business logic
│   │   │   │   │   ├── MessageServiceImpl.java  # Message handling
│   │   │   │   │   ├── UserServiceImpl.java     # User management
│   │   │   │   │   └── FileStorageServiceImpl.java # Image upload/storage
│   │   │   │   └── CustomUserDetailsService.java # Spring Security user details
│   │   │   │
│   │   │   └── ApiApplication.java        # Spring Boot main application
│   │   │
│   │   └── resources/
│   │       ├── application.properties     # Application configuration
│   │       ├── static/                    # Static resources (images)
│   │       └── templates/                 # Email templates (if needed)
│   │
│   └── test/                              # Unit and integration tests
│       └── java/com/openclassrooms/chatop/api/
│
├── uploads/                               # Uploaded images directory (runtime)
├── .env                                   # Environment variables (not committed)
├── .env.example                           # Environment template
├── pom.xml                                # Maven dependencies
└── README.md                              # This file
```

## Development

### Quick Development with Makefile

For rapid development, use the Makefile commands from the **project root**:

```bash
# Start backend only (from api/ directory)
cd api && ./mvnw spring-boot:run

# Or use Makefile from project root for full-stack development
cd .. && make start-all      # Start frontend + backend
cd .. && make logs-backend   # View backend logs
cd .. && make stop           # Stop all services
cd .. && make test           # Run all tests (frontend + backend)
```

See the [project root README](../README.md) for complete Makefile documentation.

---

### Code Style and Best Practices

This project follows **SOLID principles** and Spring Boot best practices:

- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Code is open for extension, closed for modification
- **Liskov Substitution**: Interfaces and abstractions properly implemented
- **Interface Segregation**: Focused, client-specific interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

**Additional Practices:**
- Always use DTOs for API responses (never expose entities)
- Validate input data with Spring Validation annotations
- Document all endpoints with Swagger annotations
- Use Lombok to reduce boilerplate code
- Follow RFC 9457 for standardized error responses

### Development Tools

Recommended IDE: **IntelliJ IDEA**

Useful plugins:
- Database Navigator


### Testing the API

You can test the API using Postman or Bruno:

#### Option 1: Postman

A complete Postman collection is available:

1. Import the collection: `../ressources/postman/rental.postman_collection.json`
2. Test all endpoints systematically
3. Collection includes example requests and environment variables
4. Documentation: [Postman Learning Center](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/)

#### Option 2: Bruno (Alternative)

Bruno is an open-source alternative to Postman:

1. Download: [https://www.usebruno.com/](https://www.usebruno.com/)
2. Import the Postman collection (Bruno supports Postman format)
3. Bruno is lightweight, fast, and Git-friendly (stores collections as plain text files)

---

## Testing

### Run All Tests

```bash
./mvnw test
```

### Test Coverage

Generate test coverage report:

```bash
./mvnw verify
```

---

## Deployment

### Build Production JAR

```bash
./mvnw clean package -DskipTests
```

The executable JAR will be created in `target/api-0.0.1-SNAPSHOT.jar`

### Run Production Build

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Common Issues

#### Port 3001 Already in Use

```bash
# Find process using port 3001
lsof -i :3001

# Kill the process
kill -9 <PID>
```

#### MySQL Connection Failed

Check:
1. MySQL is running: `mysql -u root -p`
2. Database exists: `SHOW DATABASES;`
3. Credentials in `.env` are correct
4. Port 3306 is accessible

#### JWT Token Issues

- Ensure `JWT_SECRET` is set in `.env`
- Check token hasn't expired (24h default)
- Verify Authorization header format: `Bearer <token>`

#### File Upload Errors

- Check `uploads/` directory exists and has write permissions
- Verify file size is under 10MB limit
- Ensure multipart configuration is correct

### Logs

Application logs are available in the console. For production, configure logging:

```properties
# application.properties
logging.level.com.openclassrooms.chatop=DEBUG
logging.file.name=logs/chatop-api.log
```

## Resources

### Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [Swagger/OpenAPI](https://springdoc.org/)

### API Testing Tools
- [Postman](https://www.postman.com/) - API testing platform (collection available in `../ressources/postman/`)
- [Bruno](https://www.usebruno.com/) - Open-source alternative to Postman (supports Postman format)

### Frontend
- [Frontend Documentation](../README.md) - Angular 14 frontend application

**Note**: This API is designed to work with the Angular frontend located in the parent directory. Make sure to run both frontend and backend simultaneously for the complete application experience.
