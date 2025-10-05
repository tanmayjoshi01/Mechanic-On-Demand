# 🚀 Mechanic On Demand - Complete Setup Guide

This guide will walk you through setting up the complete "Mechanic On Demand" Java Full Stack application from scratch.

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your system:

### Required Software
- **Java 11 or higher** - [Download here](https://adoptium.net/)
- **Maven 3.6+** - [Download here](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download here](https://dev.mysql.com/downloads/)
- **Git** - [Download here](https://git-scm.com/downloads)

### Optional (Recommended)
- **Docker & Docker Compose** - [Download here](https://www.docker.com/get-started)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)
- **Postman** - For API testing

## 🏗️ Step-by-Step Setup

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd mechanic-on-demand
```

### Step 2: Database Setup

#### Option A: Using Docker (Recommended)
```bash
# Start MySQL database
docker-compose -f docker/docker-compose.dev.yml up -d mysql-dev

# Wait for database to be ready
docker-compose -f docker/docker-compose.dev.yml logs -f mysql-dev
```

#### Option B: Manual MySQL Setup
1. Install MySQL 8.0+
2. Create database:
   ```sql
   CREATE DATABASE mechanic_on_demand;
   CREATE USER 'mechanic_user'@'localhost' IDENTIFIED BY 'mechanic_password';
   GRANT ALL PRIVILEGES ON mechanic_on_demand.* TO 'mechanic_user'@'localhost';
   FLUSH PRIVILEGES;
   ```
3. Import schema:
   ```bash
   mysql -u root -p mechanic_on_demand < database/schema.sql
   ```

### Step 3: Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Update database configuration:**
   Edit `src/main/resources/application.properties`:
   ```properties
   # Update these values if using different database settings
   spring.datasource.url=jdbc:mysql://localhost:3306/mechanic_on_demand?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=mechanic_user
   spring.datasource.password=mechanic_password
   ```

3. **Build the application:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Verify backend is running:**
   - Open browser: http://localhost:8080/api/auth/me
   - You should see an error (expected, since no auth token provided)

### Step 4: Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Serve the frontend:**
   
   **Option A: Using Python (if installed):**
   ```bash
   python -m http.server 3000
   ```
   
   **Option B: Using Node.js (if installed):**
   ```bash
   npx http-server -p 3000
   ```
   
   **Option C: Using any web server:**
   - Copy frontend files to your web server
   - Serve from any port (update API_BASE_URL in main.js if needed)

3. **Access the application:**
   - Open browser: http://localhost:3000

### Step 5: Full Stack with Docker (Alternative)

If you prefer to run everything with Docker:

```bash
# Start all services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

Access the application at: http://localhost

## 🧪 Testing the Application

### 1. Test User Registration

1. Open: http://localhost:3000/register.html
2. Fill in the registration form
3. Select user type (Customer or Mechanic)
4. Submit the form
5. Check console for success message

### 2. Test User Login

1. Open: http://localhost:3000/login.html
2. Use demo credentials:
   - **Customer**: customer@demo.com / password123
   - **Mechanic**: mechanic@demo.com / password123
3. Click "Sign In"
4. Should redirect to dashboard

### 3. Test API Endpoints

Using Postman or curl:

```bash
# Test registration
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "userType": "CUSTOMER"
  }'

# Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "test@example.com",
    "password": "password123"
  }'
```

## 🔧 Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/mechanic_on_demand?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=mechanic_user
spring.datasource.password=mechanic_password

# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000

# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:3000,http://localhost:8080
```

### Frontend Configuration

Edit `frontend/js/main.js`:

```javascript
// Update API base URL if needed
const API_BASE_URL = 'http://localhost:8080/api';
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error:**
   ```
   Error: Could not create connection to database server
   ```
   **Solution:** Ensure MySQL is running and credentials are correct

2. **Port Already in Use:**
   ```
   Error: Port 8080 is already in use
   ```
   **Solution:** 
   ```bash
   # Find process using port 8080
   lsof -i :8080
   
   # Kill the process
   kill -9 <PID>
   ```

3. **CORS Error:**
   ```
   Access to fetch at 'http://localhost:8080/api/auth/login' from origin 'http://localhost:3000' has been blocked by CORS policy
   ```
   **Solution:** Check CORS configuration in `application.properties`

4. **JWT Token Error:**
   ```
   Error: JWT token is expired
   ```
   **Solution:** Login again to get a new token

### Logs and Debugging

1. **Backend Logs:**
   ```bash
   # If running with Maven
   tail -f backend/target/spring-boot-app.log
   
   # If running with Docker
   docker-compose logs -f backend
   ```

2. **Database Logs:**
   ```bash
   # If running with Docker
   docker-compose logs -f mysql
   
   # If running locally
   tail -f /var/log/mysql/error.log
   ```

3. **Frontend Debugging:**
   - Open browser Developer Tools (F12)
   - Check Console tab for JavaScript errors
   - Check Network tab for API request failures

## 📊 Database Management

### Access Database

1. **Using phpMyAdmin (if using Docker):**
   - URL: http://localhost:8081
   - Username: root
   - Password: password

2. **Using MySQL Command Line:**
   ```bash
   mysql -u mechanic_user -p mechanic_on_demand
   ```

3. **Using Database GUI:**
   - MySQL Workbench
   - DBeaver
   - Any MySQL client

### Sample Data

The application includes sample data in `database/schema.sql`:
- Service categories
- Pricing plans
- Sample users (if you want to add them)

## 🚀 Deployment

### Development Deployment

1. **Local Development:**
   - Backend: http://localhost:8080
   - Frontend: http://localhost:3000
   - Database: localhost:3306

2. **Docker Development:**
   - Application: http://localhost
   - Database Admin: http://localhost:8081

### Production Deployment

1. **Update configuration for production**
2. **Set up proper database with backups**
3. **Configure reverse proxy (Nginx)**
4. **Set up SSL certificates**
5. **Configure monitoring and logging**

## 📚 Next Steps

1. **Explore the codebase:**
   - Backend: `backend/src/main/java/com/mechanicondemand/`
   - Frontend: `frontend/`
   - Database: `database/schema.sql`

2. **Read the documentation:**
   - API Documentation: `docs/API.md`
   - Docker Guide: `docker/README.md`

3. **Customize the application:**
   - Add new features
   - Modify UI/UX
   - Add new API endpoints

4. **Learn more:**
   - Spring Boot documentation
   - Bootstrap documentation
   - MySQL documentation

## 🆘 Getting Help

If you encounter issues:

1. Check the troubleshooting section above
2. Review the logs for error messages
3. Check the GitHub issues (if applicable)
4. Ask questions in the community forums

## 🎉 Congratulations!

You have successfully set up the Mechanic On Demand application! 

The application includes:
- ✅ User registration and authentication
- ✅ JWT-based security
- ✅ RESTful API endpoints
- ✅ Responsive frontend
- ✅ Database integration
- ✅ Docker containerization
- ✅ Complete documentation

Happy coding! 🚀