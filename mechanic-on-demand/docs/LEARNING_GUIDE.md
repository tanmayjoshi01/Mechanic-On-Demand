# 📚 Java Full Stack Learning Guide - Mechanic On Demand

This comprehensive learning guide will help you understand Java Full Stack development through the "Mechanic On Demand" project.

## 🎯 Learning Objectives

By the end of this guide, you will understand:

1. **Java Fundamentals** - Object-oriented programming, collections, streams
2. **Spring Framework** - Dependency injection, MVC pattern, Spring Boot
3. **Database Design** - Entity relationships, SQL queries, JPA/Hibernate
4. **REST APIs** - HTTP methods, status codes, API design principles
5. **Frontend Development** - HTML5, CSS3, JavaScript, Bootstrap
6. **Authentication** - JWT tokens, security concepts
7. **Containerization** - Docker basics, microservices architecture

## 📖 Table of Contents

1. [Java Backend Concepts](#java-backend-concepts)
2. [Spring Framework](#spring-framework)
3. [Database and JPA](#database-and-jpa)
4. [REST API Design](#rest-api-design)
5. [Frontend Development](#frontend-development)
6. [Authentication & Security](#authentication--security)
7. [Docker & Deployment](#docker--deployment)
8. [Project Structure](#project-structure)
9. [Code Walkthrough](#code-walkthrough)
10. [Best Practices](#best-practices)

## 🍃 Java Backend Concepts

### 1. Object-Oriented Programming (OOP)

#### Classes and Objects
```java
// User.java - Example from our project
public class User {
    // Fields (attributes)
    private Long id;
    private String username;
    private String email;
    
    // Constructor
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    // Methods (behaviors)
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
```

**Key Concepts:**
- **Encapsulation**: Private fields with public getters/setters
- **Inheritance**: Classes can extend other classes
- **Polymorphism**: Same method name, different implementations
- **Abstraction**: Hide complex implementation details

#### Annotations
```java
@Entity  // Marks class as a JPA entity
@Table(name = "users")  // Specifies database table name
@Id  // Marks primary key field
@GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate ID
@NotBlank(message = "Username is required")  // Validation annotation
```

**Common Annotations:**
- `@Entity` - JPA entity
- `@Component` - Spring component
- `@Service` - Service layer
- `@Repository` - Data access layer
- `@Controller` - Web controller
- `@Autowired` - Dependency injection

### 2. Collections and Streams

#### Collections
```java
// List - Ordered collection
List<User> users = new ArrayList<>();
users.add(new User("john", "john@example.com"));

// Map - Key-value pairs
Map<String, User> userMap = new HashMap<>();
userMap.put("john", new User("john", "john@example.com"));

// Set - Unique elements
Set<String> usernames = new HashSet<>();
usernames.add("john");
```

#### Streams (Java 8+)
```java
// Filter and map operations
List<String> activeUsernames = users.stream()
    .filter(user -> user.getIsActive())
    .map(User::getUsername)
    .collect(Collectors.toList());

// Find operations
Optional<User> user = users.stream()
    .filter(u -> u.getUsername().equals("john"))
    .findFirst();
```

## 🌱 Spring Framework

### 1. Dependency Injection (DI)

#### What is Dependency Injection?
Dependency Injection is a design pattern where objects receive their dependencies from external sources rather than creating them internally.

```java
// Without DI (tightly coupled)
public class UserService {
    private UserRepository userRepository = new UserRepositoryImpl();
}

// With DI (loosely coupled)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

#### Benefits:
- **Loose Coupling**: Classes don't depend on concrete implementations
- **Testability**: Easy to mock dependencies for testing
- **Flexibility**: Can swap implementations without code changes

### 2. Spring Boot Auto-Configuration

Spring Boot automatically configures your application based on:
- Dependencies in `pom.xml`
- Properties in `application.properties`
- Annotations in your code

```java
@SpringBootApplication  // Enables auto-configuration
public class MechanicOnDemandApplication {
    public static void main(String[] args) {
        SpringApplication.run(MechanicOnDemandApplication.class, args);
    }
}
```

### 3. Spring MVC Pattern

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Client    │───▶│ Controller  │───▶│   Service   │───▶│ Repository  │
│ (Frontend)  │    │             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
                           │                   │                   │
                           ▼                   ▼                   ▼
                    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
                    │     View    │    │  Business   │    │  Database   │
                    │ (Response)  │    │   Logic     │    │             │
                    └─────────────┘    └─────────────┘    └─────────────┘
```

**Layers:**
- **Controller**: Handles HTTP requests and responses
- **Service**: Contains business logic
- **Repository**: Handles data access
- **Entity**: Represents database tables

## 🗄️ Database and JPA

### 1. Entity-Relationship Model

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    User     │    │  Booking    │    │   Review    │
│             │    │             │    │             │
│ id (PK)     │◄───┤ customer_id │    │ id (PK)     │
│ username    │    │ mechanic_id │───▶│ booking_id  │
│ email       │    │ service_id  │    │ customer_id │
│ user_type   │    │ status      │    │ rating      │
└─────────────┘    └─────────────┘    └─────────────┘
```

**Relationship Types:**
- **One-to-One**: User ↔ CustomerProfile
- **One-to-Many**: User → Bookings (as customer)
- **Many-to-One**: Booking → User (mechanic)
- **Many-to-Many**: User ↔ Services (through bookings)

### 2. JPA Annotations

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CustomerProfile customerProfile;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Booking> customerBookings;
}
```

**Key Annotations:**
- `@Entity`: Marks class as JPA entity
- `@Table`: Specifies table name
- `@Id`: Primary key
- `@GeneratedValue`: ID generation strategy
- `@Column`: Column mapping
- `@OneToOne`, `@OneToMany`, `@ManyToOne`: Relationships

### 3. Repository Pattern

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Query methods
    Optional<User> findByUsername(String username);
    List<User> findByUserType(User.UserType userType);
    
    // Custom queries
    @Query("SELECT u FROM User u WHERE u.userType = 'MECHANIC' AND u.isActive = true")
    List<User> findActiveMechanics();
}
```

**Benefits:**
- **Automatic Implementation**: Spring generates implementation
- **Query Methods**: Create queries by method names
- **Custom Queries**: Use `@Query` for complex queries
- **Pagination**: Built-in pagination support

## 🌐 REST API Design

### 1. HTTP Methods

| Method | Purpose | Example |
|--------|---------|---------|
| GET | Retrieve data | `GET /api/users` |
| POST | Create resource | `POST /api/users` |
| PUT | Update resource | `PUT /api/users/1` |
| DELETE | Delete resource | `DELETE /api/users/1` |
| PATCH | Partial update | `PATCH /api/users/1` |

### 2. RESTful URL Design

```
# Good RESTful URLs
GET    /api/users              # Get all users
GET    /api/users/1            # Get user by ID
POST   /api/users              # Create user
PUT    /api/users/1            # Update user
DELETE /api/users/1            # Delete user

GET    /api/users/1/bookings   # Get user's bookings
POST   /api/bookings           # Create booking
```

### 3. HTTP Status Codes

```java
@PostMapping("/api/auth/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        // Authentication logic
        return ResponseEntity.ok(response);  // 200 OK
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(error);  // 400 Bad Request
    }
}
```

**Common Status Codes:**
- `200 OK`: Request successful
- `201 Created`: Resource created
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Access denied
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

### 4. Request/Response Format

```java
// Request DTO
public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;
    
    @NotBlank
    private String password;
}

// Response DTO
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String userType;
}
```

## 🎨 Frontend Development

### 1. HTML5 Structure

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mechanic On Demand</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <!-- Navigation content -->
    </nav>
    
    <main class="container">
        <!-- Main content -->
    </main>
    
    <footer class="bg-dark text-white py-4">
        <!-- Footer content -->
    </footer>
</body>
</html>
```

### 2. CSS3 Styling

```css
/* Custom CSS variables */
:root {
    --primary-color: #0d6efd;
    --secondary-color: #6c757d;
}

/* Responsive design */
@media (max-width: 768px) {
    .hero-section h1 {
        font-size: 2.5rem;
    }
}

/* Animations */
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

.fade-in {
    animation: fadeIn 0.5s ease-in;
}
```

### 3. JavaScript (ES6+)

```javascript
// Modern JavaScript features
const API_BASE_URL = 'http://localhost:8080/api';

// Async/await for API calls
async function makeApiRequest(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.error || 'Request failed');
        }
        
        return data;
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

// Arrow functions
const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US');
};

// Template literals
const userInfo = `Welcome, ${user.firstName} ${user.lastName}!`;
```

### 4. Bootstrap Framework

```html
<!-- Grid system -->
<div class="container">
    <div class="row">
        <div class="col-md-6 col-lg-4">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Card Title</h5>
                    <p class="card-text">Card content</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Forms -->
<form>
    <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" id="email" required>
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
</form>
```

## 🔐 Authentication & Security

### 1. JWT (JSON Web Token)

#### What is JWT?
JWT is a compact, URL-safe means of representing claims to be transferred between two parties.

#### JWT Structure:
```
Header.Payload.Signature
```

**Example:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImV4cCI6MTYzMzQ4NzIwMH0.signature
```

#### Implementation:
```java
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
}
```

### 2. Password Security

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// Hash password
String hashedPassword = passwordEncoder.encode("plainPassword");

// Verify password
boolean matches = passwordEncoder.matches("plainPassword", hashedPassword);
```

### 3. CORS (Cross-Origin Resource Sharing)

```java
@Configuration
public class WebSecurityConfig {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated();
    }
}
```

## 🐳 Docker & Deployment

### 1. Docker Concepts

#### What is Docker?
Docker is a platform that uses containerization to package applications and their dependencies into lightweight, portable containers.

#### Key Benefits:
- **Consistency**: Same environment across development, testing, and production
- **Isolation**: Containers don't interfere with each other
- **Portability**: Run anywhere Docker is installed
- **Scalability**: Easy to scale applications

### 2. Dockerfile

```dockerfile
# Multi-stage build
FROM maven:3.8.6-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3. Docker Compose

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: mechanic_on_demand
    ports:
      - "3306:3306"
  
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
  
  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

## 📁 Project Structure

```
mechanic-on-demand/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/mechanicondemand/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # Business Logic
│   │   ├── repository/         # Data Access
│   │   ├── entity/             # JPA Entities
│   │   ├── dto/                # Data Transfer Objects
│   │   ├── config/             # Configuration
│   │   └── security/           # Security Configuration
│   └── src/main/resources/
│       └── application.properties
├── frontend/                   # HTML/CSS/JS Frontend
│   ├── css/                    # Custom CSS
│   ├── js/                     # JavaScript
│   ├── images/                 # Images
│   └── *.html                  # HTML Pages
├── database/                   # Database Scripts
│   └── schema.sql
├── docker/                     # Docker Configuration
│   ├── Dockerfile.backend
│   ├── Dockerfile.frontend
│   └── docker-compose.yml
└── docs/                       # Documentation
    ├── API.md
    └── SETUP_GUIDE.md
```

## 🔍 Code Walkthrough

### 1. User Registration Flow

```java
// 1. Controller receives request
@PostMapping("/api/auth/register")
public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
    User user = userService.registerUser(request);
    return ResponseEntity.status(201).body(response);
}

// 2. Service processes business logic
@Service
public class UserService {
    public User registerUser(RegisterRequest request) {
        // Validate data
        // Check if user exists
        // Hash password
        // Save to database
        return userRepository.save(user);
    }
}

// 3. Repository saves to database
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring generates implementation
}
```

### 2. Frontend API Integration

```javascript
// 1. Form submission
document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // 2. Collect form data
    const formData = new FormData(e.target);
    const registerData = {
        username: formData.get('username'),
        email: formData.get('email'),
        password: formData.get('password'),
        // ... other fields
    };
    
    // 3. Make API request
    try {
        const response = await makeApiRequest('/auth/register', {
            method: 'POST',
            body: JSON.stringify(registerData)
        });
        
        // 4. Handle response
        showAlert('Registration successful!', 'success');
    } catch (error) {
        showAlert(error.message, 'danger');
    }
});
```

## 🏆 Best Practices

### 1. Backend Best Practices

#### Code Organization
- **Single Responsibility**: Each class has one responsibility
- **Dependency Injection**: Use `@Autowired` for dependencies
- **Validation**: Validate input data with annotations
- **Error Handling**: Use try-catch blocks and proper HTTP status codes

#### Security
- **Password Hashing**: Always hash passwords with BCrypt
- **JWT Tokens**: Use secure, short-lived tokens
- **Input Validation**: Validate all user inputs
- **CORS Configuration**: Configure CORS properly

### 2. Frontend Best Practices

#### Code Organization
- **Separation of Concerns**: Separate HTML, CSS, and JavaScript
- **Modular JavaScript**: Use functions and modules
- **Error Handling**: Handle API errors gracefully
- **User Feedback**: Show loading states and success/error messages

#### Performance
- **Minimize HTTP Requests**: Combine CSS/JS files
- **Use CDNs**: Use CDNs for external libraries
- **Optimize Images**: Compress images
- **Lazy Loading**: Load content as needed

### 3. Database Best Practices

#### Design
- **Normalization**: Avoid data redundancy
- **Indexing**: Add indexes for frequently queried columns
- **Relationships**: Use proper foreign keys
- **Naming**: Use consistent naming conventions

#### Performance
- **Query Optimization**: Write efficient queries
- **Connection Pooling**: Use connection pooling
- **Caching**: Implement caching where appropriate

## 🎓 Learning Path

### Beginner (Weeks 1-2)
1. **Java Basics**: Variables, loops, conditionals, methods
2. **OOP Concepts**: Classes, objects, inheritance, polymorphism
3. **HTML/CSS**: Basic structure and styling
4. **JavaScript Basics**: Variables, functions, DOM manipulation

### Intermediate (Weeks 3-4)
1. **Spring Framework**: Dependency injection, annotations
2. **Spring Boot**: Auto-configuration, starters
3. **Database**: SQL basics, JPA/Hibernate
4. **REST APIs**: HTTP methods, JSON, status codes

### Advanced (Weeks 5-6)
1. **Security**: JWT, password hashing, CORS
2. **Frontend Frameworks**: Bootstrap, modern JavaScript
3. **Docker**: Containerization, microservices
4. **Testing**: Unit tests, integration tests

### Expert (Weeks 7-8)
1. **Performance**: Caching, optimization
2. **Monitoring**: Logging, metrics
3. **Deployment**: CI/CD, cloud deployment
4. **Architecture**: Microservices, design patterns

## 📚 Additional Resources

### Books
- "Spring in Action" by Craig Walls
- "Effective Java" by Joshua Bloch
- "Clean Code" by Robert Martin
- "Design Patterns" by Gang of Four

### Online Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [MDN Web Docs](https://developer.mozilla.org/)
- [Oracle Java Documentation](https://docs.oracle.com/en/java/)

### Practice Projects
1. **Todo List**: Simple CRUD application
2. **Blog System**: User management, posts, comments
3. **E-commerce**: Products, cart, orders, payments
4. **Social Media**: Users, posts, likes, follows

## 🎉 Conclusion

Congratulations! You've learned the fundamentals of Java Full Stack development through the Mechanic On Demand project. 

**Key Takeaways:**
- **Backend**: Spring Boot, JPA, REST APIs, Security
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap
- **Database**: MySQL, Entity relationships, Query optimization
- **DevOps**: Docker, Containerization, Deployment

**Next Steps:**
1. Practice with the codebase
2. Add new features
3. Deploy to cloud platforms
4. Learn advanced concepts
5. Build your own projects

Remember: The best way to learn is by doing. Keep coding, keep learning, and keep building! 🚀