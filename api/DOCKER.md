# Docker Setup - ChâTop API

## Quick Start

### 1. Start MySQL Database with Docker

```bash
# From the api/ directory
docker-compose up -d
```

This command automatically:
- ✅ Downloads MySQL 8.0 image
- ✅ Creates the `chatop` database
- ✅ Executes the SQL script (`../ressources/sql/script.sql`)
- ✅ Creates USERS, RENTALS, MESSAGES tables
- ✅ Configures root user with no password

### 2. Verify Container is Running

```bash
docker-compose ps
```

You should see:
```
NAME            STATUS    PORTS
chatop-mysql    running   0.0.0.0:3306->3306/tcp
```

### 3. Configure Spring Boot Application

Make sure your `.env` file contains:

```properties
DB_URL=jdbc:mysql://localhost:3306/chatop
DB_USERNAME=root
DB_PASSWORD=
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000
```

**Important:** `DB_PASSWORD` must be empty to match the Docker configuration.

### 4. Start Spring Boot Application

```bash
./mvnw spring-boot:run
```

The API starts at http://localhost:3001

## Useful Commands

### View MySQL Logs

```bash
docker-compose logs -f mysql
```

### Connect to MySQL Container

```bash
docker exec -it chatop-mysql mysql -u root chatop
```

### Stop Container

```bash
docker-compose down
```

### Stop and Remove Data

```bash
docker-compose down -v
```

⚠️ This command deletes the volume and all database data!

### Restart Container

```bash
docker-compose restart
```

### Complete Database Reset

```bash
# Remove container and data
docker-compose down -v

# Recreate database with SQL script
docker-compose up -d
```

## Troubleshooting

### Port 3306 Already in Use

If you have MySQL installed locally on your machine:

**macOS (Homebrew):**
```bash
brew services stop mysql
```

**Linux:**
```bash
sudo systemctl stop mysql
```

**Windows:**
Stop MySQL service from Services or MySQL Workbench

### Container Won't Start

Check the logs:
```bash
docker-compose logs mysql
```

### Verify Tables Were Created

```bash
# Connect to MySQL in the container
docker exec -it chatop-mysql mysql -u root chatop

# Then in MySQL:
SHOW TABLES;
```

You should see:
```
+------------------+
| Tables_in_chatop |
+------------------+
| messages         |
| rentals          |
| users            |
+------------------+
```

### Connection Issues from Spring Boot

1. Verify container is running: `docker-compose ps`
2. Check credentials in `.env`: `DB_USERNAME=root` and `DB_PASSWORD=`
3. Verify URL: `DB_URL=jdbc:mysql://localhost:3306/chatop`
4. Check container logs: `docker-compose logs -f mysql`

## Configuration

### docker-compose.yml

The file configures:
- **Image**: MySQL 8.0
- **Container name**: chatop-mysql
- **Port**: 3306 (accessible at localhost:3306)
- **Database**: chatop
- **User**: root (no password)
- **Init script**: `../ressources/sql/script.sql` mounted automatically
- **Volume**: `chatop-mysql-data` to persist data
- **Character set**: utf8mb4 with unicode_ci collation

### Healthcheck

The container includes a healthcheck that verifies MySQL is ready:
- Test: `mysqladmin ping`
- Interval: every 3 seconds
- Timeout: 5 seconds
- Retries: 10 attempts

This ensures Spring Boot can connect immediately at startup.

## Production

⚠️ **This configuration is for local development only!**

For production:
1. DO NOT use an empty password
2. Configure `MYSQL_ROOT_PASSWORD` with a strong password
3. Create a dedicated MySQL user (not root)
4. Use Docker secrets or secure environment variables
5. Configure regular backups
6. Restrict network access to the container
