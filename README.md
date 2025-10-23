# ChâTop - Rental Property Portal (Estate)

[![Angular](https://img.shields.io/badge/Angular-14.1.3-red.svg)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.java.com/)

> Full-stack application for a seasonal rental property portal connecting tenants and property owners in the Basque coast tourist area.

---

## Project Structure

This repository contains a complete full-stack application:

```
oc-p03-java-spring/
├── api/                    # 🔴 Backend (Spring Boot) - MAIN EVALUATION PART
│   ├── src/
│   ├── pom.xml
│   └── README.md          # 📖 Complete backend documentation
├── src/                   # Frontend (Angular 14)
├── ressources/            # Project resources
│   ├── sql/              # Database schema
│   ├── mockoon/          # API mock
│   └── postman/          # API collection
├── package.json
└── README.md             # This file

## ⚠️ Important: Backend Evaluation

**The main part of this project that will be evaluated is the BACKEND (Spring Boot API).**

### 📖 Backend Documentation

**For complete installation, setup, and API documentation, please refer to:**

### **[→ Backend README (api/README.md)](./api/README.md)**

The backend README contains:
- Complete installation guide
- Database setup instructions
- Environment configuration
- API documentation with Swagger
- Authentication flow (JWT)
- All API endpoints
- Troubleshooting guide

## Quick Start Guide

### Prerequisites

- **Node.js 14+** and npm (for frontend)
- **Java 17+** (for backend)
- **MySQL 8.0+** (for database)
- **Maven 3.6+** (comes with backend)

### 1. Database Setup

Create and configure the MySQL database:

```bash
# Create database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Run SQL schema
mysql -u root -p chatop < ressources/sql/script.sql
```

### 2. Backend Setup (Port 3001)

**See detailed instructions in [api/README.md](./api/README.md)**

```bash
# Navigate to backend directory
cd api

# Configure environment
cp .env.example .env
# Edit .env with your MySQL credentials and JWT secret

# Run backend
./mvnw spring-boot:run
```

Backend will start on **http://localhost:3001**

API Documentation (Swagger): **http://localhost:3001/swagger-ui/index.html**

### 3. Frontend Setup (Port 4200)

```bash
# From project root
npm install

# Start Angular dev server
npm run start
```

Frontend will start on **http://localhost:4200**

## Frontend - Angular Application

This is an Angular 14 application with Material Design that provides the user interface for the rental portal.

### Features

- User authentication (register/login)
- Browse rental properties
- View rental details
- Create and update rentals (with image upload)
- Send messages to property owners
- User profile management

### Technology Stack

- **Angular 14.1.3**: Frontend framework
- **Angular Material**: UI components with indigo-pink theme
- **Angular Flex Layout**: Responsive layout system
- **TypeScript 4.7.4**: Programming language
- **RxJS**: Reactive programming

### Project Structure

```
src/
├── app/
│   ├── components/          # Shared components
│   ├── features/            # Feature modules
│   │   ├── auth/           # Authentication (login/register)
│   │   └── rentals/        # Rental management
│   ├── guards/             # Route guards (auth, unauth)
│   ├── interceptors/       # HTTP interceptors (JWT)
│   ├── interfaces/         # TypeScript interfaces
│   └── services/           # Services (session, user, etc.)
├── assets/                 # Static assets
└── proxy.config.json       # Proxy configuration for API
```

### Development Commands

```bash
# Install dependencies
npm install

# Start development server
npm run start
# or
ng serve

# Build for production
npm run build

# Run tests
npm run test

# Lint code
npm run lint
```

### API Proxy Configuration

The Angular app uses a proxy configuration (`src/proxy.config.json`) to forward all `/api/*` requests to the backend at `http://localhost:3001`.

This means when the frontend makes a request to `/api/auth/login`, it's automatically forwarded to `http://localhost:3001/api/auth/login`.

## Testing the Application

### Option 1: With Real Backend (Recommended)

1. Start the backend: `cd api && ./mvnw spring-boot:run`
2. Start the frontend: `npm run start`
3. Access the application at http://localhost:4200

### Option 2: With Mockoon (API Mock)

For testing the frontend without the backend:

1. Download [Mockoon](https://mockoon.com/download/)
2. Load environment: `File > Open environment > ressources/mockoon/rental-oc.json`
3. Start Mockoon server (click play button)
4. Start the frontend: `npm run start`

### Option 3: With Postman (API Testing)

For testing the backend API directly:

1. Import collection: `ressources/postman/rental.postman_collection.json`
2. Test all API endpoints
3. Documentation: https://learning.postman.com/docs/getting-started/importing-and-exporting-data/

## Resources

### Documentation

- **Backend API**: [api/README.md](./api/README.md) - Complete backend documentation
- **Angular**: [Angular Documentation](https://angular.io/docs)
- **Spring Boot**: [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- **Angular Material**: [Material Design Components](https://material.angular.io/)

### Project Resources

- **SQL Schema**: `ressources/sql/script.sql` - Database structure
- **Mockoon Environment**: `ressources/mockoon/rental-oc.json` - API mock
- **Postman Collection**: `ressources/postman/rental.postman_collection.json` - API tests

## API Endpoints

For the complete list of API endpoints with examples, see [api/README.md](./api/README.md#api-endpoints)

**Quick Reference:**

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login (returns JWT token)
- `GET /api/auth/me` - Get current user

### Rentals
- `GET /api/rentals` - List all rentals
- `GET /api/rentals/{id}` - Get rental details
- `POST /api/rentals` - Create rental (with image)
- `PUT /api/rentals/{id}` - Update rental

### Messages
- `POST /api/messages` - Send message to owner

### Users
- `GET /api/user/{id}` - Get user info

All endpoints (except register/login) require JWT authentication via `Authorization: Bearer <token>` header.

## Troubleshooting

### Port Conflicts

- Frontend default port: **4200**
- Backend required port: **3001** (do not change - required by proxy config)

If port 4200 is in use, Angular will prompt for an alternative port.

### Authentication Issues

- Ensure backend is running on port 3001
- Check browser console for errors
- Verify JWT token in localStorage
- Check Swagger UI for API status: http://localhost:3001/swagger-ui/index.html

### More Troubleshooting

For backend-specific issues, see [api/README.md - Troubleshooting](./api/README.md#troubleshooting)


**🔴 Remember**: The backend (Spring Boot API) is the main part being evaluated. For complete documentation, installation instructions, and API details, please refer to **[api/README.md](./api/README.md)**
