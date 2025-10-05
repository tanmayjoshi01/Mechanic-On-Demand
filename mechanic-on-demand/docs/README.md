# 🔧 Mechanic On Demand - Complete Java Full Stack Application

A comprehensive web application similar to Uber, but for booking nearby mechanics when a vehicle breaks down. Built with Java Spring Boot backend and modern frontend technologies.

## 🌟 Features

### Customer Features
- ✅ User registration and authentication
- ✅ Find nearby mechanics using location services
- ✅ Book mechanic appointments instantly
- ✅ View pricing plans (monthly/yearly)
- ✅ Real-time notifications
- ✅ Booking history and management
- ✅ Review and rating system

### Mechanic Features
- ✅ Mechanic registration and profile management
- ✅ Accept/reject service requests
- ✅ Manage availability and services
- ✅ View job history and earnings
- ✅ Update service status
- ✅ Customer communication

### Admin Features
- ✅ User management
- ✅ Service category management
- ✅ Pricing plan management
- ✅ System monitoring

## 🛠️ Tech Stack

### Backend
- **Java 11+** - Programming language
- **Spring Boot 2.7** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer
- **MySQL 8.0** - Database
- **JWT** - Token-based authentication
- **Maven** - Build tool

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling
- **Bootstrap 5** - Responsive framework
- **JavaScript (ES6+)** - Interactivity
- **Font Awesome** - Icons

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Nginx** - Web server and reverse proxy

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker (optional)

### Option 1: Docker (Recommended)
```bash
# Clone the repository
git clone <repository-url>
cd mechanic-on-demand

# Start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8080/api
# Database Admin: http://localhost:8081
```

### Option 2: Manual Setup
```bash
# 1. Start MySQL database
# 2. Import database schema
mysql -u root -p mechanic_on_demand < database/schema.sql

# 3. Start backend
cd backend
mvn spring-boot:run

# 4. Start frontend
cd frontend
python -m http.server 3000

# Access the application
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080/api
```

## 📁 Project Structure

```
mechanic-on-demand/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/mechanicondemand/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # Business Logic
│   │   ├── repository/         # Data Access Layer
│   │   ├── entity/             # JPA Entities
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── config/             # Configuration Classes
│   │   └── security/           # Security Configuration
│   └── src/main/resources/
│       └── application.properties
├── frontend/                   # HTML/CSS/JS Frontend
│   ├── css/                    # Custom CSS files
│   ├── js/                     # JavaScript files
│   ├── images/                 # Images and icons
│   ├── index.html              # Home page
│   ├── login.html              # Login page
│   ├── register.html           # Registration page
│   ├── booking.html            # Booking page
│   └── pricing.html            # Pricing page
├── database/                   # Database Scripts
│   └── schema.sql              # Database schema
├── docker/                     # Docker Configuration
│   ├── Dockerfile.backend      # Backend Dockerfile
│   ├── Dockerfile.frontend     # Frontend Dockerfile
│   ├── nginx.conf              # Nginx configuration
│   └── docker-compose.yml      # Docker Compose
└── docs/                       # Documentation
    ├── README.md               # This file
    ├── SETUP_GUIDE.md          # Detailed setup guide
    ├── LEARNING_GUIDE.md       # Learning guide
    └── API.md                  # API documentation
```

## 🔧 Configuration

### Backend Configuration
Edit `backend/src/main/resources/application.properties`:

```properties
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

## 🧪 Testing

### Demo Credentials
- **Customer**: customer@demo.com / password123
- **Mechanic**: mechanic@demo.com / password123

### API Testing
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

## 📚 Documentation

- **[Setup Guide](docs/SETUP_GUIDE.md)** - Detailed setup instructions
- **[Learning Guide](docs/LEARNING_GUIDE.md)** - Learn Java Full Stack development
- **[API Documentation](docs/API.md)** - Complete API reference
- **[Docker Guide](docker/README.md)** - Docker configuration and usage

## 🎯 Learning Objectives

This project teaches you:

1. **Java Backend Development**
   - Object-oriented programming
   - Spring Framework and Spring Boot
   - REST API design and implementation
   - Database design and JPA/Hibernate
   - Security and authentication

2. **Frontend Development**
   - HTML5 and CSS3
   - Bootstrap framework
   - JavaScript (ES6+)
   - API integration
   - Responsive design

3. **Database Design**
   - Entity-relationship modeling
   - SQL queries and optimization
   - JPA annotations and relationships
   - Database migrations

4. **DevOps and Deployment**
   - Docker containerization
   - Docker Compose orchestration
   - Nginx configuration
   - Production deployment

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   (Nginx)       │◄──►│   (Spring Boot) │◄──►│   (MySQL)       │
│   Port: 80      │    │   Port: 8080    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Static Files  │    │   REST APIs     │    │   Data Storage  │
│   HTML/CSS/JS   │    │   JWT Auth      │    │   User Data     │
│   Bootstrap     │    │   Business Logic│    │   Bookings      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Hashing**: BCrypt password encryption
- **CORS Protection**: Cross-origin resource sharing configuration
- **Input Validation**: Server-side validation for all inputs
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries

## 📊 Database Schema

### Core Tables
- **users**: User accounts (customers and mechanics)
- **customer_profiles**: Additional customer information
- **mechanic_profiles**: Additional mechanic information
- **bookings**: Service appointments
- **reviews**: Customer reviews and ratings
- **service_categories**: Types of services offered
- **pricing_plans**: Subscription plans

### Relationships
- One-to-One: User ↔ CustomerProfile, User ↔ MechanicProfile
- One-to-Many: User → Bookings (as customer/mechanic)
- Many-to-One: Booking → User (customer/mechanic)
- Many-to-Many: User ↔ Services (through bookings)

## 🚀 Deployment

### Development
```bash
# Start development environment
docker-compose -f docker/docker-compose.dev.yml up -d

# Access services
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
# Database: localhost:3307
```

### Production
```bash
# Start production environment
docker-compose up -d

# Access services
# Application: http://localhost
# Database Admin: http://localhost:8081
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL is running
   - Check credentials in application.properties
   - Verify database exists

2. **Port Already in Use**
   - Check if port 8080 is available
   - Kill existing processes using the port
   - Change port in application.properties

3. **CORS Error**
   - Check CORS configuration
   - Verify frontend URL is allowed
   - Check browser console for errors

4. **JWT Token Error**
   - Login again to get new token
   - Check token expiration
   - Verify JWT secret configuration

### Getting Help

1. Check the [Setup Guide](docs/SETUP_GUIDE.md) for detailed instructions
2. Review the [Learning Guide](docs/LEARNING_GUIDE.md) for concepts
3. Check the [API Documentation](docs/API.md) for endpoint details
4. Look at the logs for error messages

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive CSS framework
- Font Awesome for the beautiful icons
- MySQL team for the reliable database
- Docker team for the containerization platform

## 📞 Support

If you have any questions or need help:

1. Check the documentation
2. Look at the code examples
3. Review the troubleshooting section
4. Ask questions in the community forums

---

**Happy Coding! 🚀**

Built with ❤️ for learning Java Full Stack development.