.PHONY: help setup-frontend setup-backend setup start-frontend start-backend start-all first-setup stop clean

# Colors for output
GREEN := \033[0;32m
YELLOW := \033[0;33m
NC := \033[0m # No Color

# Logs directory
LOGS_DIR := .logs

help: ## Show this help message
	@echo "$(GREEN)ChâTop - Development Makefile$(NC)"
	@echo ""
	@echo "Available targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-20s$(NC) %s\n", $$1, $$2}'


start-frontend: ## Start Angular development server (http://localhost:4200)
	@echo "$(GREEN)Starting frontend on http://localhost:4200...$(NC)"
	npm run start

start-backend: ## Start Spring Boot API (http://localhost:3001)
	@echo "$(GREEN)Starting backend on http://localhost:3001...$(NC)"
	@cd api && ./mvnw spring-boot:run

start-all: ## Start frontend and backend (auto-setup on first run)
	@echo "$(GREEN)Starting ChâTop application...$(NC)"
	@echo ""
	@# Check and create .env if needed
	@if [ ! -f api/.env ]; then \
		echo "$(YELLOW)First-time setup detected!$(NC)"; \
		echo "$(GREEN)Creating .env file...$(NC)"; \
		cp api/.env.example api/.env; \
		JWT_SECRET=$$(openssl rand -base64 64 | tr -d '\n') && \
		sed -i.bak "s|JWT_SECRET=changeme-generate-a-secure-key-for-production|JWT_SECRET=$$JWT_SECRET|g" api/.env && \
		rm -f api/.env.bak; \
		echo "$(GREEN)✓ Secure JWT secret generated!$(NC)"; \
		echo ""; \
	fi
	@# Check and install node_modules if needed
	@if [ ! -d "node_modules" ]; then \
		echo "$(YELLOW)Node modules not found. Installing dependencies...$(NC)"; \
		npm install; \
		echo "$(GREEN)✓ Frontend dependencies installed!$(NC)"; \
		echo ""; \
	fi
	@# Check and start MySQL if needed
	@echo "$(GREEN)Checking MySQL...$(NC)"
	@if ! docker ps | grep -q chatop-mysql; then \
		echo "$(YELLOW)MySQL container not running. Starting Docker...$(NC)"; \
		cd api && docker-compose up -d; \
		echo "$(YELLOW)Waiting for MySQL to be ready (this may take 10-30 seconds)...$(NC)"; \
		until docker exec chatop-mysql mysql -u root chatop -e "SELECT 1" >/dev/null 2>&1; do \
			printf "$(YELLOW).$(NC)"; \
			sleep 2; \
		done; \
		echo ""; \
		echo "$(GREEN)✓ MySQL is ready!$(NC)"; \
	else \
		echo "$(GREEN)✓ MySQL is already running$(NC)"; \
	fi
	@echo ""
	@# Start services
	@mkdir -p $(LOGS_DIR)
	@echo "$(GREEN)Starting services...$(NC)"
	nohup npm run start > $(LOGS_DIR)/frontend.log 2>&1 & echo $$! > $(LOGS_DIR)/frontend.pid
	@sleep 2
	nohup sh -c 'cd api && ./mvnw spring-boot:run' > $(LOGS_DIR)/backend.log 2>&1 & echo $$! > $(LOGS_DIR)/backend.pid
	@sleep 2
	@echo ""
	@echo "$(GREEN)━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$(NC)"
	@echo "$(GREEN)✓ ChâTop is running!$(NC)"
	@echo "$(GREEN)━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━$(NC)"
	@echo ""
	@echo "  Frontend:  http://localhost:4200"
	@echo "  Backend:   http://localhost:3001"
	@echo "  Swagger:   http://localhost:3001/api/swagger-ui/index.html"
	@echo ""
	@echo "  Logs:      .logs/frontend.log  |  .logs/backend.log"
	@echo ""
	@echo "$(YELLOW)Commands:$(NC)"
	@echo "  make stop           - Stop all services"
	@echo "  make logs-frontend  - View frontend logs"
	@echo "  make logs-backend   - View backend logs"
	@echo "  make status         - Check services status"
	@echo ""

stop: ## Stop all running services (keeps Docker volumes/data)
	@echo "$(YELLOW)Stopping services...$(NC)"
	@if [ -f $(LOGS_DIR)/frontend.pid ]; then \
		kill $$(cat $(LOGS_DIR)/frontend.pid) 2>/dev/null || true; \
		rm $(LOGS_DIR)/frontend.pid; \
	fi
	@pkill -f "ng serve" 2>/dev/null || true
	@pkill -f "npm run start" 2>/dev/null || true
	@echo "$(GREEN)Frontend stopped$(NC)"
	@if [ -f $(LOGS_DIR)/backend.pid ]; then \
		kill $$(cat $(LOGS_DIR)/backend.pid) 2>/dev/null || true; \
		rm $(LOGS_DIR)/backend.pid; \
	fi
	@pkill -f "spring-boot:run" 2>/dev/null || true
	@pkill -f "chatop" 2>/dev/null || true
	@echo "$(GREEN)Backend stopped$(NC)"
	@cd api && docker-compose down
	@echo "$(GREEN)Docker containers stopped (data preserved)$(NC)"

clean: ## Stop services and remove all build artifacts, logs, and Docker volumes
	@echo "$(YELLOW)Cleaning everything...$(NC)"
	@# Stop processes
	@if [ -f $(LOGS_DIR)/frontend.pid ]; then \
		kill $$(cat $(LOGS_DIR)/frontend.pid) 2>/dev/null || true; \
		rm $(LOGS_DIR)/frontend.pid; \
	fi
	@pkill -f "ng serve" 2>/dev/null || true
	@pkill -f "npm run start" 2>/dev/null || true
	@if [ -f $(LOGS_DIR)/backend.pid ]; then \
		kill $$(cat $(LOGS_DIR)/backend.pid) 2>/dev/null || true; \
		rm $(LOGS_DIR)/backend.pid; \
	fi
	@pkill -f "spring-boot:run" 2>/dev/null || true
	@pkill -f "chatop" 2>/dev/null || true
	@# Remove Docker with volumes
	@cd api && docker-compose down -v
	@echo "$(GREEN)Docker containers and volumes removed$(NC)"
	@# Clean build artifacts
	@rm -rf node_modules
	@cd api && ./mvnw clean 2>/dev/null || true
	@rm -rf $(LOGS_DIR)
	@echo "$(GREEN)✓ Complete cleanup finished!$(NC)"
	@echo "$(YELLOW)Database data has been deleted. Run 'make start-all' to recreate.$(NC)"

restart: stop start-all ## Restart all services

status: ## Show status of services
	@echo "$(GREEN)Service Status:$(NC)"
	@echo ""
	@if [ -f $(LOGS_DIR)/frontend.pid ] && kill -0 $$(cat $(LOGS_DIR)/frontend.pid) 2>/dev/null; then \
		echo "Frontend: $(GREEN)RUNNING$(NC) (PID: $$(cat $(LOGS_DIR)/frontend.pid))"; \
	else \
		echo "Frontend: $(YELLOW)STOPPED$(NC)"; \
	fi
	@if [ -f $(LOGS_DIR)/backend.pid ] && kill -0 $$(cat $(LOGS_DIR)/backend.pid) 2>/dev/null; then \
		echo "Backend:  $(GREEN)RUNNING$(NC) (PID: $$(cat $(LOGS_DIR)/backend.pid))"; \
	else \
		echo "Backend:  $(YELLOW)STOPPED$(NC)"; \
	fi
	@echo ""
	@echo "Docker containers:"
	@cd api && docker-compose ps

logs: ## Show logs (run 'make logs-frontend' or 'make logs-backend' for specific service)
	@echo "$(YELLOW)Showing combined logs (Ctrl+C to exit)...$(NC)"
	@tail -f $(LOGS_DIR)/frontend.log $(LOGS_DIR)/backend.log

logs-frontend: ## Show frontend logs
	@tail -f $(LOGS_DIR)/frontend.log

logs-backend: ## Show backend logs
	@tail -f $(LOGS_DIR)/backend.log

test: ## Run all tests
	@echo "$(GREEN)Running frontend tests...$(NC)"
	npm run test
	@echo "$(GREEN)Running backend tests...$(NC)"
	@cd api && ./mvnw test

build: ## Build production artifacts
	@echo "$(GREEN)Building frontend...$(NC)"
	npm run build
	@echo "$(GREEN)Building backend...$(NC)"
	@cd api && ./mvnw clean package -DskipTests
	@echo "$(GREEN)Build complete!$(NC)"
	@echo "Frontend build: dist/"
	@echo "Backend JAR: api/target/api-0.0.1-SNAPSHOT.jar"
