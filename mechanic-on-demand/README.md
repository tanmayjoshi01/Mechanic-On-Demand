# 🔧 Mechanic On Demand - Java Full Stack Application

A web application similar to Uber, but for booking nearby mechanics when a vehicle breaks down.

## 📁 Project Structure

```
mechanic-on-demand/
├── backend/                    # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mechanicondemand/
│   │   │   │   ├── controller/     # REST Controllers
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── repository/     # Data Access Layer
│   │   │   │   ├── entity/         # JPA Entities
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── config/         # Configuration Classes
│   │   │   │   └── MechanicOnDemandApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── static/         # Static files
│   │   └── test/                   # Test files
│   └── pom.xml                     # Maven dependencies
├── frontend/                   # HTML/CSS/Bootstrap Frontend
│   ├── css/                   # Custom CSS files
│   ├── js/                    # JavaScript files
│   ├── images/                # Images and icons
│   ├── index.html             # Home page
│   ├── login.html             # Login page
│   ├── register.html          # Registration page
│   ├── booking.html           # Booking page
│   ├── pricing.html           # Pricing page
│   └── dashboard.html          # User dashboard
├── database/                   # Database scripts
│   └── schema.sql             # Database schema
├── docker/                     # Docker configuration
│   ├── Dockerfile.backend     # Backend Dockerfile
│   ├── Dockerfile.frontend    # Frontend Dockerfile
│   └── docker-compose.yml     # Docker Compose
└── docs/                      # Documentation
    └── API.md                 # API Documentation
```

## 🚀 Tech Stack Explained

### Backend (Java Spring Boot)
- **Spring Boot**: A framework that makes it easy to create stand-alone, production-grade Spring-based applications
- **Spring Data JPA**: Simplifies database operations using Java Persistence API
- **Spring Security**: Provides authentication and authorization
- **JWT**: JSON Web Tokens for secure authentication
- **Maven**: Dependency management and build tool

### Frontend (HTML/CSS/Bootstrap)
- **HTML5**: Structure of web pages
- **CSS3**: Styling and layout
- **Bootstrap 5**: Responsive design framework
- **JavaScript**: Interactive functionality

### Database
- **MySQL**: Relational database management system
- **JPA/Hibernate**: Object-Relational Mapping (ORM)

### Containerization
- **Docker**: Containerization platform
- **Docker Compose**: Multi-container Docker applications

## 🔧 Key Features

### Customer Features
- User registration and authentication
- Find nearby mechanics using location services
- Book mechanic appointments
- View pricing plans (monthly/yearly)
- Real-time notifications
- Booking history

### Mechanic Features
- Mechanic registration and profile management
- Accept/reject service requests
- Manage availability
- View job history and earnings
- Update service status

## 📚 Learning Path

This project will teach you:
1. **Java Basics**: Object-oriented programming concepts
2. **Spring Framework**: Dependency injection, MVC pattern
3. **REST APIs**: HTTP methods, status codes, API design
4. **Database Design**: Entity relationships, SQL queries
5. **Frontend Development**: HTML, CSS, JavaScript
6. **Authentication**: JWT tokens, security
7. **Containerization**: Docker basics

## 🛠️ Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker (optional)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Quick Start

1. Clone the repository
2. Set up MySQL database
3. Configure application.properties
4. Run the Spring Boot application
5. Open frontend in browser

For detailed setup instructions, see the individual component documentation.