# Estate

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P3-Full-Stack-portail-locataire

Go inside folder:

> cd P3-Full-Stack-portail-locataire

Install dependencies:

> npm install

Launch Front-end:

> npm run start;


## Ressources

### Mockoon env

Download Mockoon here: https://mockoon.com/download/

After installing you could load the environement

> ressources/mockoon/rental-oc.json

directly inside Mockoon 

> File > Open environmement

For launching the Mockoon server click on play bouton

Mockoon documentation: https://mockoon.com/docs/latest/about/

### Postman collection

For Postman import the collection

> ressources/postman/rental.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

#### Database Installation

To set up the MySQL database for the ChâTop API:

**Prerequisites:**
- MySQL installed and running on your machine
- Default MySQL connection: `root` user (adjust if needed)

**Installation steps:**

1. Create the database:
```bash
mysql -u root -e "CREATE DATABASE IF NOT EXISTS chatop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

2. Execute the SQL script:
```bash
mysql -u root chatop < ressources/sql/script.sql
```

3. Verify installation:
```bash
mysql -u root -e "USE chatop; SHOW TABLES;"
```

**Database structure:**

The script creates 3 tables:
- **USERS**: User accounts (id, email, name, password, created_at, updated_at)
- **RENTALS**: Rental properties (id, name, surface, price, picture, description, owner_id, created_at, updated_at)
- **MESSAGES**: Messages between users (id, rental_id, user_id, message, created_at, updated_at)

Including:
- Foreign key constraints (RENTALS.owner_id → USERS.id, MESSAGES.user_id → USERS.id, MESSAGES.rental_id → RENTALS.id)
- Unique index on USERS.email

**Configuration:**

Set your database credentials as environment variables for Spring Boot:
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret_key
```

Or configure them in `src/main/resources/application.properties`
