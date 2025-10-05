# 🚀 Mechanic On Demand - Complete Setup Guide

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Overview](#project-overview)
3. [Technology Stack Explained](#technology-stack-explained)
4. [Installation Steps](#installation-steps)
5. [Running the Application](#running-the-application)
6. [API Documentation](#api-documentation)
7. [Database Schema](#database-schema)
8. [Learning Resources](#learning-resources)
9. [Troubleshooting](#troubleshooting)

## 🔧 Prerequisites

Before starting, make sure you have the following installed:

### Required Software
- **Java 11 or higher** - [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **Git** - [Download Git](https://git-scm.com/downloads)

### Optional (for Docker)
- **Docker Desktop** - [Download Docker](https://www.docker.com/products/docker-desktop/)
- **Docker Compose** - Usually included with Docker Desktop

### IDE Recommendations
- **IntelliJ IDEA** (Community Edition) - [Download](https://www.jetbrains.com/idea/download/)
- **Eclipse** - [Download](https://www.eclipse.org/downloads/)
- **Visual Studio Code** - [Download](https://code.visualstudio.com/)

## 🎯 Project Overview

**Mechanic On Demand** is a full-stack web application that connects customers with nearby mechanics for vehicle repair services. Think of it as "Uber for mechanics" - when your car breaks down, you can quickly find and book a professional mechanic.

### Key Features
- **Customer Features:**
  - Find nearby mechanics using GPS location
  - Book mechanics instantly
  - View service pricing (monthly/yearly plans)
  - Real-time booking status updates
  - Rate and review mechanics

- **Mechanic Features:**
  - Accept or reject service requests
  - Manage availability status
  - View job history and earnings
  - Update profile and specialization

## 🛠️ Technology Stack Explained

### Backend (Server-Side)
- **Java 11** - Programming language
- **Spring Boot** - Framework that makes Java web development easier
- **Spring Security** - Handles authentication and security
- **Spring Data JPA** - Database operations without writing SQL
- **MySQL** - Database to store all data
- **JWT** - Secure token-based authentication
- **Maven** - Dependency management and build tool

### Frontend (Client-Side)
- **HTML5** - Structure of web pages
- **CSS3** - Styling and layout
- **Bootstrap 5** - CSS framework for responsive design
- **JavaScript (ES6+)** - Interactive functionality
- **Font Awesome** - Icons

### DevOps & Deployment
- **Docker** - Containerization for easy deployment
- **Docker Compose** - Multi-container application management
- **Nginx** - Web server for frontend

### What Each Technology Does

#### Spring Boot
Spring Boot is like a toolkit that makes Java web development much easier. Instead of writing hundreds of lines of configuration code, Spring Boot provides:
- Auto-configuration (sets up everything automatically)
- Embedded server (no need to install Tomcat separately)
- Starter dependencies (adds all needed libraries)

#### JPA (Java Persistence API)
JPA lets you work with databases using Java objects instead of writing SQL queries. For example:
```java
// Instead of: SELECT * FROM customers WHERE email = 'john@email.com'
Customer customer = customerRepository.findByEmail("john@email.com");
```

#### JWT (JSON Web Token)
JWT is like a digital passport. When you log in, the server gives you a token that proves who you are. You include this token with every request, so the server knows you're authenticated.

#### Bootstrap
Bootstrap is a CSS framework that provides pre-made styles and components. Instead of writing CSS from scratch, you use Bootstrap classes like `btn btn-primary` to create beautiful buttons.

## 📦 Installation Steps

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd mechanic-on-demand
```

### Step 2: Set Up MySQL Database

1. **Install MySQL** (if not already installed)
2. **Start MySQL service**
3. **Create database and user:**
```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE mechanic_ondemand;

-- Create user (optional, you can use root)
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'app_password';
GRANT ALL PRIVILEGES ON mechanic_ondemand.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

4. **Import the schema:**
```bash
mysql -u root -p mechanic_ondemand < database/schema.sql
```

### Step 3: Configure Backend

1. **Update database configuration** in `backend/src/main/resources/application.properties`:
```properties
# Update these values to match your MySQL setup
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### Step 4: Build and Run Backend

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Step 5: Open Frontend

1. **Open the frontend** in your browser:
   - Navigate to `frontend/index.html`
   - Or use a local server like Live Server in VS Code

2. **Test the application:**
   - Try registering as a customer
   - Try registering as a mechanic
   - Test the booking functionality

## 🚀 Running the Application

### Method 1: Manual Setup (Recommended for Learning)

1. **Start MySQL** service
2. **Run Backend:**
```bash
cd backend
mvn spring-boot:run
```
3. **Open Frontend:**
   - Open `frontend/index.html` in your browser
   - Or use a local server

### Method 2: Docker (Easier Deployment)

1. **Build and start all services:**
```bash
# Production setup
docker-compose up --build

# Development setup (with hot reloading)
docker-compose -f docker-compose.dev.yml up --build
```

2. **Access the application:**
   - Frontend: `http://localhost` (or `http://localhost:3000` for dev)
   - Backend API: `http://localhost:8080/api`
   - MySQL: `localhost:3306`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register Customer
```http
POST /auth/customer/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@email.com",
  "password": "password123",
  "phone": "555-0101"
}
```

#### Login Customer
```http
POST /auth/customer/login
Content-Type: application/json

{
  "email": "john@email.com",
  "password": "password123"
}
```

#### Register Mechanic
```http
POST /auth/mechanic/register
Content-Type: application/json

{
  "firstName": "Mike",
  "lastName": "Johnson",
  "email": "mike@mechanic.com",
  "password": "password123",
  "phone": "555-0201",
  "specialization": "Engine Specialist",
  "experienceYears": 8,
  "hourlyRate": 75.00
}
```

### Mechanic Endpoints

#### Get Available Mechanics
```http
GET /mechanics/available
```

#### Find Nearby Mechanics
```http
GET /mechanics/nearby?lat=40.7128&lng=-74.0060&radius=10
```

#### Update Mechanic Availability
```http
PUT /mechanics/{id}/availability
Content-Type: application/json

{
  "isAvailable": true
}
```

### Booking Endpoints

#### Create Booking
```http
POST /bookings
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "customerId": 1,
  "mechanicId": 1,
  "serviceType": "Engine Repair",
  "description": "Car won't start",
  "vehicleInfo": "2020 Toyota Camry Red",
  "bookingDate": "2024-01-15T10:00:00"
}
```

#### Accept Booking (Mechanic)
```http
PUT /bookings/{id}/accept
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "mechanicId": 1
}
```

#### Complete Booking
```http
PUT /bookings/{id}/complete
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "mechanicId": 1,
  "actualDuration": 120
}
```

### Pricing Endpoints

#### Get All Pricing
```http
GET /pricing
```

#### Get Pricing by Service Type
```http
GET /pricing/Engine Repair
```

## 🗄️ Database Schema

### Tables Overview

#### customers
- `id` - Primary key
- `first_name` - Customer's first name
- `last_name` - Customer's last name
- `email` - Email address (unique)
- `password` - Encrypted password
- `phone` - Phone number
- `address` - Home address
- `latitude` - GPS latitude
- `longitude` - GPS longitude
- `created_at` - Account creation timestamp
- `updated_at` - Last update timestamp

#### mechanics
- `id` - Primary key
- `first_name` - Mechanic's first name
- `last_name` - Mechanic's last name
- `email` - Email address (unique)
- `password` - Encrypted password
- `phone` - Phone number
- `address` - Service area
- `latitude` - GPS latitude
- `longitude` - GPS longitude
- `specialization` - Type of services offered
- `experience_years` - Years of experience
- `hourly_rate` - Rate per hour
- `is_available` - Currently accepting bookings
- `rating` - Average customer rating
- `total_ratings` - Number of ratings received

#### bookings
- `id` - Primary key
- `customer_id` - Foreign key to customers
- `mechanic_id` - Foreign key to mechanics
- `service_type` - Type of service requested
- `description` - Problem description
- `vehicle_info` - Vehicle details
- `booking_date` - Scheduled service date/time
- `status` - PENDING, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED, CANCELLED
- `estimated_duration` - Expected service time (minutes)
- `actual_duration` - Actual service time (minutes)
- `total_cost` - Final cost
- `customer_rating` - Customer's rating (1-5)
- `customer_feedback` - Customer's review

#### pricing
- `id` - Primary key
- `service_type` - Type of service
- `monthly_price` - Monthly subscription price
- `yearly_price` - Yearly subscription price
- `description` - Service description

## 📖 Learning Resources

### Java Full Stack Development

#### Beginner Level
1. **Java Basics**
   - [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
   - [Codecademy Java Course](https://www.codecademy.com/learn/learn-java)

2. **Spring Boot**
   - [Spring Boot Official Guide](https://spring.io/guides/gs/spring-boot/)
   - [Baeldung Spring Boot Tutorial](https://www.baeldung.com/spring-boot)

3. **Database & JPA**
   - [JPA Tutorial](https://www.baeldung.com/jpa-hibernate)
   - [MySQL Tutorial](https://www.mysql.com/tutorials/)

#### Intermediate Level
1. **Spring Security**
   - [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
   - [JWT with Spring Security](https://www.baeldung.com/spring-security-jwt)

2. **REST APIs**
   - [REST API Tutorial](https://restfulapi.net/)
   - [HTTP Methods Explained](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)

3. **Frontend Development**
   - [Bootstrap Documentation](https://getbootstrap.com/docs/)
   - [JavaScript MDN Guide](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide)

#### Advanced Level
1. **Docker & DevOps**
   - [Docker Getting Started](https://docs.docker.com/get-started/)
   - [Docker Compose Guide](https://docs.docker.com/compose/)

2. **Testing**
   - [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
   - [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

### Recommended Learning Path

1. **Week 1-2: Java Basics**
   - Learn Java syntax and OOP concepts
   - Practice with simple programs

2. **Week 3-4: Spring Boot**
   - Set up your first Spring Boot project
   - Learn about controllers, services, and repositories

3. **Week 5-6: Database Integration**
   - Learn JPA and Hibernate
   - Practice with MySQL

4. **Week 7-8: Security & Authentication**
   - Implement JWT authentication
   - Learn about Spring Security

5. **Week 9-10: Frontend Development**
   - Learn HTML, CSS, and JavaScript
   - Practice with Bootstrap

6. **Week 11-12: Full Stack Integration**
   - Connect frontend with backend
   - Learn about REST APIs

## 🔧 Troubleshooting

### Common Issues

#### 1. MySQL Connection Error
**Error:** `java.sql.SQLException: Access denied for user 'root'@'localhost'`

**Solution:**
- Check MySQL username and password in `application.properties`
- Ensure MySQL service is running
- Verify database exists: `SHOW DATABASES;`

#### 2. Port Already in Use
**Error:** `Port 8080 was already in use`

**Solution:**
- Find process using port 8080: `netstat -ano | findstr :8080` (Windows) or `lsof -i :8080` (Mac/Linux)
- Kill the process or change port in `application.properties`

#### 3. Maven Build Fails
**Error:** `Could not resolve dependencies`

**Solution:**
- Check internet connection
- Clear Maven cache: `mvn clean`
- Update Maven: `mvn clean install -U`

#### 4. Frontend Not Loading
**Error:** CORS errors or API calls failing

**Solution:**
- Ensure backend is running on port 8080
- Check browser console for errors
- Verify API_BASE_URL in `js/app.js`

#### 5. Docker Issues
**Error:** `docker-compose up` fails

**Solution:**
- Ensure Docker Desktop is running
- Check if ports 80, 8080, 3306 are available
- Try rebuilding: `docker-compose up --build`

### Getting Help

1. **Check Logs:**
   - Backend logs: Look in console where you ran `mvn spring-boot:run`
   - Docker logs: `docker-compose logs [service-name]`

2. **Common Commands:**
```bash
# Check if services are running
docker-compose ps

# View logs
docker-compose logs backend
docker-compose logs mysql

# Restart services
docker-compose restart

# Stop all services
docker-compose down
```

3. **Debug Mode:**
   - Add `logging.level.com.mechanicondemand=DEBUG` to `application.properties`
   - This will show detailed logs

## 🎉 Congratulations!

You now have a complete Java Full Stack application running! This project demonstrates:

- **Backend Development** with Spring Boot
- **Database Design** and JPA integration
- **Security Implementation** with JWT
- **REST API Development**
- **Frontend Development** with modern web technologies
- **Containerization** with Docker
- **Full Stack Integration**

### Next Steps

1. **Experiment** with the code - try adding new features
2. **Learn** from the codebase - understand how everything works together
3. **Extend** the application - add new endpoints, improve UI, etc.
4. **Deploy** to a cloud platform like AWS, Google Cloud, or Heroku

### Project Ideas for Practice

1. Add a notification system (email/SMS)
2. Implement real-time chat between customers and mechanics
3. Add a payment system integration
4. Create a mobile app using React Native or Flutter
5. Add advanced search and filtering
6. Implement a recommendation system
7. Add analytics and reporting features

Happy coding! 🚀