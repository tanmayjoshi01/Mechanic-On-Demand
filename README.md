# 🚗 Mechanic On Demand - Java Full Stack Mini Project

A comprehensive web application for booking mechanics on demand, similar to Uber but for vehicle repairs. Built with Java Spring Boot backend and HTML/CSS/Bootstrap frontend.

---

## 📚 Table of Contents

1. [Introduction](#introduction)
2. [Tech Stack Explained](#tech-stack-explained)
3. [Project Structure](#project-structure)
4. [Prerequisites](#prerequisites)
5. [Setup Instructions](#setup-instructions)
6. [Understanding REST APIs](#understanding-rest-apis)
7. [API Endpoints Documentation](#api-endpoints-documentation)
8. [Database Schema](#database-schema)
9. [JWT Authentication Explained](#jwt-authentication-explained)
10. [Running with Docker](#running-with-docker)
11. [Testing the Application](#testing-the-application)
12. [Learning Resources](#learning-resources)

---

## 🎯 Introduction

**Mechanic On Demand** is a full-stack web application that connects customers with nearby mechanics in real-time.

### Features:
- **For Customers:**
  - Find nearby mechanics using GPS
  - Book mechanics instantly
  - View pricing plans (Hourly, Monthly, Yearly)
  - Real-time booking notifications
  - Rate and review mechanics

- **For Mechanics:**
  - Accept/reject service requests
  - Manage availability
  - View job history
  - Track ratings and reviews

---

## 🛠️ Tech Stack Explained

### Backend:
- **Java 17**: Programming language
- **Spring Boot 3.1.5**: Framework for building Java applications
  - Spring Web: For REST APIs
  - Spring Data JPA: For database operations
  - Spring Security: For authentication
- **MySQL**: Relational database
- **JWT**: JSON Web Tokens for secure authentication
- **Maven**: Dependency management

### Frontend:
- **HTML5**: Structure
- **CSS3**: Styling
- **Bootstrap 5**: UI framework for responsive design
- **JavaScript (ES6)**: Interactivity and API calls

### DevOps:
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

---

## 📁 Project Structure

```
mechanic-on-demand/
│
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/mechanicondemand/
│   │   ├── model/                    # Entity classes (Database models)
│   │   │   ├── Customer.java
│   │   │   ├── Mechanic.java
│   │   │   ├── Booking.java
│   │   │   ├── BookingStatus.java
│   │   │   └── SubscriptionType.java
│   │   │
│   │   ├── repository/               # Data access layer (JPA repositories)
│   │   │   ├── CustomerRepository.java
│   │   │   ├── MechanicRepository.java
│   │   │   └── BookingRepository.java
│   │   │
│   │   ├── service/                  # Business logic layer
│   │   │   ├── AuthService.java
│   │   │   ├── BookingService.java
│   │   │   └── MechanicService.java
│   │   │
│   │   ├── controller/               # REST API endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── MechanicController.java
│   │   │   └── BookingController.java
│   │   │
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── BookingRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   └── ApiResponse.java
│   │   │
│   │   ├── security/                 # JWT & Security
│   │   │   ├── JwtUtil.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   │
│   │   ├── config/                   # Configuration classes
│   │   │   ├── SecurityConfig.java
│   │   │   └── CorsConfig.java
│   │   │
│   │   └── MechanicOnDemandApplication.java  # Main class
│   │
│   ├── src/main/resources/
│   │   └── application.properties    # Application configuration
│   │
│   ├── pom.xml                       # Maven dependencies
│   └── Dockerfile                    # Docker configuration
│
├── frontend/                         # Frontend files
│   ├── index.html                    # Home page
│   ├── login.html                    # Login page
│   ├── register.html                 # Registration page
│   ├── dashboard.html                # User dashboard
│   ├── find-mechanics.html           # Search mechanics
│   ├── pricing.html                  # Pricing plans
│   │
│   ├── css/
│   │   └── style.css                 # Custom styles
│   │
│   └── js/
│       ├── auth.js                   # Authentication logic
│       ├── dashboard.js              # Dashboard functionality
│       └── mechanics.js              # Mechanic search & booking
│
├── database/
│   └── schema.sql                    # Database schema
│
├── docker-compose.yml                # Docker orchestration
├── nginx.conf                        # Web server configuration
└── README.md                         # This file
```

---

## ✅ Prerequisites

Before starting, make sure you have these installed:

### Option 1: Run with Docker (Recommended for Beginners)
- **Docker Desktop**: [Download here](https://www.docker.com/products/docker-desktop)

### Option 2: Run Locally
- **Java 17**: [Download JDK](https://adoptium.net/)
- **Maven 3.6+**: [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0**: [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **IDE**: IntelliJ IDEA or VS Code

---

## 🚀 Setup Instructions

### Method 1: Using Docker (Easiest)

1. **Clone the repository:**
```bash
git clone <repository-url>
cd mechanic-on-demand
```

2. **Start all services with Docker Compose:**
```bash
docker-compose up -d
```

This single command will:
- Start MySQL database
- Build and start Spring Boot backend
- Start Nginx web server for frontend

3. **Access the application:**
- Frontend: http://localhost
- Backend API: http://localhost:8080/api

4. **Stop the application:**
```bash
docker-compose down
```

### Method 2: Running Locally

#### Step 1: Setup MySQL Database

1. Install MySQL and start the service
2. Create database:
```bash
mysql -u root -p
```

```sql
CREATE DATABASE mechanic_on_demand;
```

3. Run the schema file:
```bash
mysql -u root -p mechanic_on_demand < database/schema.sql
```

#### Step 2: Configure Backend

1. Update `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mechanic_on_demand
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

#### Step 3: Build and Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will start on http://localhost:8080/api

#### Step 4: Run Frontend

Open `frontend/index.html` in a web browser, or use a simple HTTP server:

```bash
cd frontend
python -m http.server 8000
```

Access frontend at http://localhost:8000

---

## 📖 Understanding REST APIs

### What is REST?

**REST (Representational State Transfer)** is an architectural style for building web services.

### Key Concepts:

1. **Resources**: Everything is a resource (Customer, Mechanic, Booking)
2. **HTTP Methods**: 
   - `GET`: Retrieve data (Read)
   - `POST`: Create new data (Create)
   - `PUT`: Update existing data (Update)
   - `DELETE`: Remove data (Delete)

3. **Stateless**: Each request contains all information needed
4. **JSON Format**: Data is sent/received as JSON

### REST vs SOAP

| Feature | REST | SOAP |
|---------|------|------|
| Protocol | HTTP | HTTP, SMTP, TCP |
| Format | JSON, XML | XML only |
| Performance | Faster, lightweight | Slower, heavyweight |
| Flexibility | More flexible | Strict standards |
| Use Case | Modern web APIs | Enterprise systems |

### HTTP Methods in Detail

#### GET - Retrieve Data
```http
GET /api/mechanics
GET /api/mechanics/1
```
- Used to fetch data
- No request body
- Safe and idempotent (multiple calls produce same result)

#### POST - Create Data
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```
- Used to create new resources
- Contains request body
- Not idempotent

#### PUT - Update Data
```http
PUT /api/mechanics/1/availability?available=true
```
- Used to update existing resources
- Idempotent

#### DELETE - Remove Data
```http
DELETE /api/bookings/1
```
- Used to delete resources
- Idempotent

---

## 🔌 API Endpoints Documentation

### Authentication Endpoints

#### 1. Register User
```http
POST /api/auth/register
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "1234567890",
  "address": "123 Main St",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "role": "CUSTOMER"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "CUSTOMER"
  }
}
```

#### 2. Login
```http
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** Same as register

### Mechanic Endpoints

#### 1. Get All Mechanics
```http
GET /api/mechanics
Authorization: Bearer <token>
```

#### 2. Get Mechanic by ID
```http
GET /api/mechanics/1
Authorization: Bearer <token>
```

#### 3. Find Nearby Mechanics
```http
GET /api/mechanics/nearby?latitude=40.7128&longitude=-74.0060&radius=10
Authorization: Bearer <token>
```

**Parameters:**
- `latitude`: Customer's latitude
- `longitude`: Customer's longitude
- `radius`: Search radius in kilometers (default: 10)

#### 4. Update Mechanic Availability
```http
PUT /api/mechanics/1/availability?available=true
Authorization: Bearer <token>
```

### Booking Endpoints

#### 1. Create Booking
```http
POST /api/bookings
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "customerId": 1,
  "mechanicId": 2,
  "vehicleType": "Car",
  "vehicleModel": "Toyota Camry",
  "problemDescription": "Engine overheating",
  "location": "123 Main St",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "subscriptionType": "HOURLY",
  "scheduledTime": "2024-10-05T14:30:00"
}
```

#### 2. Get Customer Bookings
```http
GET /api/bookings/customer/1
Authorization: Bearer <token>
```

#### 3. Get Mechanic Bookings
```http
GET /api/bookings/mechanic/2
Authorization: Bearer <token>
```

#### 4. Accept Booking (Mechanic)
```http
PUT /api/bookings/1/accept
Authorization: Bearer <token>
```

#### 5. Reject Booking (Mechanic)
```http
PUT /api/bookings/1/reject
Authorization: Bearer <token>
```

#### 6. Start Booking (Mechanic)
```http
PUT /api/bookings/1/start
Authorization: Bearer <token>
```

#### 7. Complete Booking (Mechanic)
```http
PUT /api/bookings/1/complete
Authorization: Bearer <token>
```

#### 8. Cancel Booking (Customer)
```http
PUT /api/bookings/1/cancel
Authorization: Bearer <token>
```

#### 9. Rate Booking (Customer)
```http
PUT /api/bookings/1/rate?rating=5&feedback=Excellent service!
Authorization: Bearer <token>
```

---

## 🗄️ Database Schema

### Tables Overview

#### 1. customers
```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 2. mechanics
```sql
CREATE TABLE mechanics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    specialty VARCHAR(50),
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    available BOOLEAN DEFAULT TRUE,
    rating DOUBLE DEFAULT 0.0,
    total_jobs INT DEFAULT 0,
    role VARCHAR(20) DEFAULT 'MECHANIC',
    hourly_rate DOUBLE DEFAULT 50.0,
    monthly_subscription DOUBLE DEFAULT 999.0,
    yearly_subscription DOUBLE DEFAULT 9999.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 3. bookings
```sql
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    vehicle_model VARCHAR(100) NOT NULL,
    problem_description TEXT NOT NULL,
    location VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    status VARCHAR(20) DEFAULT 'PENDING',
    subscription_type VARCHAR(20) NOT NULL,
    price DOUBLE NOT NULL,
    scheduled_time TIMESTAMP,
    completed_time TIMESTAMP,
    rating INT,
    feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id)
);
```

### Relationships

```
customers (1) ----< (many) bookings
mechanics (1) ----< (many) bookings
```

---

## 🔐 JWT Authentication Explained

### What is JWT?

**JWT (JSON Web Token)** is a secure way to transmit information between parties as a JSON object.

### Structure of JWT

JWT consists of three parts separated by dots (`.`):

```
Header.Payload.Signature
```

**Example:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### Parts Explained

1. **Header** (Red):
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
Contains token type and hashing algorithm

2. **Payload** (Purple):
```json
{
  "sub": "john@example.com",
  "role": "CUSTOMER",
  "iat": 1516239022,
  "exp": 1516325422
}
```
Contains user data (claims)

3. **Signature** (Blue):
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```
Ensures token hasn't been tampered with

### How JWT Works in Our App

1. **User logs in** with email/password
2. **Server verifies** credentials
3. **Server generates JWT** token
4. **Client stores** token (localStorage)
5. **Client sends token** in Authorization header for subsequent requests:
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
6. **Server validates** token and processes request

### Security Benefits

- **Stateless**: Server doesn't need to store sessions
- **Secure**: Signed with secret key
- **Efficient**: Reduces database queries
- **Scalable**: Works across multiple servers

---

## 🐳 Running with Docker

### What is Docker?

**Docker** is a platform for developing, shipping, and running applications in containers.

**Container**: A lightweight, standalone package that includes everything needed to run an application (code, runtime, libraries).

### Why Docker?

- **Consistency**: Same environment on all machines
- **Isolation**: Each service runs independently
- **Easy Setup**: One command to start everything
- **Portable**: Run anywhere Docker is installed

### Docker Compose

**Docker Compose** is a tool for defining and running multi-container Docker applications.

Our `docker-compose.yml` defines 3 services:
1. **MySQL**: Database
2. **Backend**: Spring Boot API
3. **Frontend**: Nginx web server

### Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up -d --build

# View running containers
docker ps

# Access MySQL shell
docker exec -it mechanic_mysql mysql -u root -p

# Access backend logs
docker logs mechanic_backend

# Remove all containers and volumes
docker-compose down -v
```

---

## 🧪 Testing the Application

### Test Flow

#### 1. Register as Customer
1. Go to http://localhost (or http://localhost:8000 if running locally)
2. Click "Register"
3. Fill in details, select "Customer"
4. Click "Get My Location" (allow browser permission)
5. Submit registration

#### 2. Register as Mechanic
1. Open a new incognito/private window
2. Go to registration page
3. Select "Mechanic"
4. Fill in details including specialty and hourly rate
5. Get location and register

#### 3. Customer: Find and Book Mechanic
1. Login as customer
2. Go to "Find Mechanics"
3. Click "Use My Location"
4. Click "Search"
5. See list of nearby mechanics
6. Click on a mechanic card
7. Fill in booking form
8. Submit booking

#### 4. Mechanic: Accept Booking
1. Login as mechanic
2. View dashboard
3. See pending bookings
4. Click "Accept" on a booking
5. Click "Start" when ready to work
6. Click "Complete" when finished

#### 5. Customer: Rate Mechanic
1. Go to customer dashboard
2. Find completed booking
3. Rate the service (1-5 stars)
4. Provide feedback

### Testing API with Postman/curl

#### Register Customer
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "customer@test.com",
    "password": "password123",
    "phone": "1234567890",
    "role": "CUSTOMER",
    "latitude": 40.7128,
    "longitude": -74.0060
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@test.com",
    "password": "password123"
  }'
```

#### Get Nearby Mechanics (use token from login response)
```bash
curl -X GET "http://localhost:8080/api/mechanics/nearby?latitude=40.7128&longitude=-74.0060&radius=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📚 Learning Resources

### Java & Spring Boot
- [Spring Boot Official Guide](https://spring.io/guides/gs/spring-boot/)
- [Java Tutorial](https://docs.oracle.com/javase/tutorial/)
- [Spring Boot Full Course](https://www.youtube.com/watch?v=9SGDpanrc8U)

### REST APIs
- [REST API Tutorial](https://restfulapi.net/)
- [HTTP Methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)

### JPA & Hibernate
- [JPA Tutorial](https://www.baeldung.com/learn-jpa-hibernate)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### JWT Authentication
- [JWT.io](https://jwt.io/)
- [Spring Security + JWT](https://www.baeldung.com/spring-security-jwt)

### Docker
- [Docker Getting Started](https://docs.docker.com/get-started/)
- [Docker Compose Tutorial](https://docs.docker.com/compose/gettingstarted/)

### Frontend
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [JavaScript Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)

---

## 🎓 Key Concepts to Learn

### 1. Layered Architecture
```
Controller Layer  →  REST API endpoints (user-facing)
Service Layer     →  Business logic
Repository Layer  →  Database access
Model Layer       →  Entity classes
```

### 2. Dependency Injection
Spring Boot automatically creates and injects dependencies using `@Autowired`.

### 3. Annotations
- `@Entity`: Marks a class as a database table
- `@RestController`: Marks a class as REST API controller
- `@Service`: Marks a class as a service
- `@Repository`: Marks a class as a repository
- `@Autowired`: Injects dependencies

### 4. JPA Relationships
- `@ManyToOne`: Many bookings belong to one customer/mechanic
- `@OneToMany`: One customer has many bookings

### 5. RESTful Design
- Use nouns for resources (`/customers`, `/bookings`)
- Use HTTP methods for actions (GET, POST, PUT, DELETE)
- Use status codes (200 OK, 201 Created, 400 Bad Request, 401 Unauthorized)

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or change port in application.properties
server.port=8081
```

#### 2. MySQL Connection Error
- Ensure MySQL is running
- Check credentials in `application.properties`
- Verify database exists

#### 3. JWT Token Invalid
- Token may have expired (24 hours by default)
- Login again to get new token

#### 4. CORS Error
- Backend `CorsConfig` is already configured
- Ensure `@CrossOrigin` is present in controllers

#### 5. Docker Issues
```bash
# Reset Docker
docker-compose down -v
docker system prune -a
docker-compose up -d --build
```

---

## 📞 Next Steps

1. **Add More Features:**
   - Real-time notifications (WebSocket)
   - Payment integration (Stripe)
   - Email notifications
   - Admin dashboard
   - Mechanic verification system

2. **Improve Security:**
   - Rate limiting
   - Input validation
   - HTTPS/SSL
   - Refresh tokens

3. **Optimize Performance:**
   - Database indexing
   - Caching (Redis)
   - Load balancing

4. **Deploy to Production:**
   - AWS/Azure/Google Cloud
   - CI/CD pipeline
   - Monitoring (Prometheus, Grafana)

---

## 📄 License

This project is created for educational purposes.

---

## 🙏 Acknowledgments

This project demonstrates:
- Java Full Stack Development
- RESTful API Design
- JWT Authentication
- Docker Containerization
- Database Design
- Frontend Development

**Happy Learning! 🚀**

---

**Note**: This is a learning project. For production use, implement additional security measures, error handling, and testing.
