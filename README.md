# ChâTop - Frontend Application

[![Angular](https://img.shields.io/badge/Angular-14.1.3-red.svg)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-4.7.4-blue.svg)](https://www.typescriptlang.org/)
[![Angular Material](https://img.shields.io/badge/Material-14.1.3-purple.svg)](https://material.angular.io/)

> Angular 14 frontend application for ChâTop, a seasonal rental property portal connecting tenants and property owners in the Basque coast tourist area.

---

## About This Project

This is the **frontend** component of the ChâTop full-stack application. It provides a modern, responsive user interface built with Angular 14 and Material Design.

### Full-Stack Architecture

```
oc-p03-java-spring/
├── api/                    # Backend (Spring Boot REST API)
│   └── README.md          # 📖 Complete backend documentation
├── src/                   # Frontend (Angular 14) - THIS PART
├── ressources/            # Project resources
│   ├── sql/              # Database schema
│   ├── mockoon/          # API mock for development
│   └── postman/          # API testing collection (Postman/Bruno)
└── README.md             # This file
```

### Backend Documentation

For backend installation, API documentation, and database setup, please refer to:

**[→ Backend Documentation (api/README.md)](./api/README.md)**

---

## Prerequisites

- **Node.js 14+** and npm
- **Backend API** running on port 3001 (see [api/README.md](./api/README.md))

To verify your installation:

```bash
node --version  # Should show v14+
npm --version   # Should show npm 6+
```

## Quick Start with Makefile ⚡

The easiest way to set up and run the entire application (frontend + backend + database):

```bash
make start-all
```

This **single command** automatically:
- ✅ Detects first run and creates `.env` with secure JWT secret
- ✅ Installs frontend dependencies (if needed)
- ✅ Starts MySQL with Docker and waits until ready
- ✅ Starts both frontend (port 4200) and backend (port 3001)

**Other useful commands:**
```bash
make stop          # Stop all services
make status        # Check services status
make logs-frontend # View frontend logs
make logs-backend  # View backend logs
make help          # See all available commands
```

---

## Manual Installation

If you prefer to set up manually without the Makefile:

### 1. Install Dependencies

```bash
npm install
```

### 2. Start Development Server

```bash
npm run start
# or
ng serve
```

The application will start on **http://localhost:4200**

**Note:** You'll also need to set up and start the backend manually (see [api/README.md](./api/README.md))

---

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

**Using Makefile (recommended):**
```bash
make start-all      # Start frontend & backend together
make stop           # Stop all services
make logs-frontend  # View frontend logs
make status         # Check services status
make help           # See all commands
```

**Using npm directly:**
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

---

## Testing the Application

### Option 1: With Real Backend (Recommended)

1. Start the backend: See [api/README.md](./api/README.md) for instructions
2. Start the frontend: `npm run start`
3. Access the application at http://localhost:4200

### Option 2: With Mockoon (API Mock)

For testing the frontend without the backend:

1. Download [Mockoon](https://mockoon.com/download/)
2. Load environment: `File > Open environment > ressources/mockoon/rental-oc.json`
3. Start Mockoon server (click play button)
4. Start the frontend: `npm run start`

---

## Architecture & Authentication

### Module Structure

The Angular app uses **feature modules with lazy loading**:
- **AuthModule**: Authentication (login/register) - loaded for unauthenticated users
- **RentalsModule**: Rental property listings and management - protected by AuthGuard

### Routing Strategy

- `/` - Redirects to auth module (login/register) if not authenticated
- `/rentals/*` - Protected rental property routes (requires authentication)
- `/me` - User profile page (protected)
- `/404` - Not found page with wildcard redirect

### Authentication Flow

1. **JWT Storage**: Token stored in localStorage upon successful login
2. **JwtInterceptor** (`src/app/interceptors/jwt.interceptor.ts`): Automatically attaches Bearer token to all HTTP requests
3. **AuthGuard** (`src/app/guards/auth.guard.ts`): Protects authenticated routes, redirects to login if not authenticated
4. **UnauthGuard** (`src/app/guards/unauth.guard.ts`): Redirects authenticated users away from login/register pages
5. **SessionService** (`src/app/services/session.service.ts`): Manages session state with RxJS BehaviorSubject for reactive updates

---

## Troubleshooting

### Port Conflicts

- Frontend default port: **4200**
- Backend required port: **3001** (do not change - required by proxy config)

If port 4200 is in use, Angular will prompt for an alternative port.

### Authentication Issues

- Ensure backend is running on port 3001
- Check browser console for errors
- Verify JWT token in localStorage
- Check Swagger UI for API status: http://localhost:3001/api/swagger-ui/index.html

### Backend Connection Issues

If the frontend cannot connect to the backend:
- Verify backend is running: `http://localhost:3001/api/swagger-ui/index.html`
- Check proxy configuration in `src/proxy.config.json`
- Ensure no CORS errors in browser console

---

## Resources

### Documentation

- **Backend API**: [api/README.md](./api/README.md) - Complete backend documentation
- **Angular**: [Angular Documentation](https://angular.io/docs)
- **Angular Material**: [Material Design Components](https://material.angular.io/)
- **TypeScript**: [TypeScript Handbook](https://www.typescriptlang.org/docs/)

### Project Resources

- **Mockoon Environment**: `ressources/mockoon/rental-oc.json` - API mock
- **SQL Schema**: `ressources/sql/script.sql` - Database structure (for backend)
- **API Testing Collection**: `ressources/postman/rental.postman_collection.json` - Postman/Bruno collection (for backend)

---

**🔴 Remember**: The backend (Spring Boot API) is the main part being evaluated. For complete documentation, installation instructions, and API details, please refer to **[api/README.md](./api/README.md)**
