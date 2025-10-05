# 📚 Java Full Stack Development Learning Guide

## 🎯 Learning Objectives

By the end of this guide, you will understand:
- How to build a complete full-stack web application
- Java Spring Boot backend development
- Frontend development with HTML, CSS, and JavaScript
- Database design and JPA integration
- REST API development and consumption
- Authentication and security
- Docker containerization
- Modern web development practices

## 📖 Table of Contents

1. [Java Fundamentals](#java-fundamentals)
2. [Spring Boot Framework](#spring-boot-framework)
3. [Database & JPA](#database--jpa)
4. [REST API Development](#rest-api-development)
5. [Security & Authentication](#security--authentication)
6. [Frontend Development](#frontend-development)
7. [Full Stack Integration](#full-stack-integration)
8. [DevOps & Deployment](#devops--deployment)
9. [Best Practices](#best-practices)
10. [Next Steps](#next-steps)

## ☕ Java Fundamentals

### What is Java?
Java is a programming language that runs on any device with a Java Virtual Machine (JVM). It's:
- **Platform Independent**: Write once, run anywhere
- **Object-Oriented**: Everything is an object
- **Secure**: Built-in security features
- **Robust**: Strong memory management and error handling

### Key Java Concepts

#### 1. Classes and Objects
```java
// Class definition
public class Customer {
    // Fields (properties)
    private String firstName;
    private String lastName;
    private String email;
    
    // Constructor
    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    // Methods (behaviors)
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
```

#### 2. Inheritance
```java
// Parent class
public class User {
    protected String email;
    protected String password;
    
    public void login() {
        System.out.println("User logged in");
    }
}

// Child class
public class Customer extends User {
    private String phone;
    
    @Override
    public void login() {
        System.out.println("Customer logged in");
    }
}
```

#### 3. Interfaces
```java
// Interface definition
public interface UserService {
    void register(User user);
    User login(String email, String password);
}

// Implementation
public class CustomerService implements UserService {
    @Override
    public void register(User user) {
        // Implementation
    }
    
    @Override
    public User login(String email, String password) {
        // Implementation
        return null;
    }
}
```

#### 4. Annotations
Annotations provide metadata about your code:
```java
@Entity  // Marks this as a database entity
@Table(name = "customers")  // Specifies table name
public class Customer {
    @Id  // Primary key
    @GeneratedValue  // Auto-generated value
    private Long id;
    
    @NotBlank  // Validation annotation
    private String firstName;
}
```

### Practice Exercises

1. **Create a Vehicle class** with properties like make, model, year, and methods like start(), stop()
2. **Create a Mechanic class** that extends User and adds specialization and hourlyRate
3. **Create an interface ServiceProvider** with methods like provideService() and calculateCost()

## 🚀 Spring Boot Framework

### What is Spring Boot?
Spring Boot is a framework that makes Java web development easier by providing:
- **Auto-configuration**: Sets up everything automatically
- **Starter dependencies**: Adds all needed libraries
- **Embedded server**: No need to install Tomcat separately
- **Production-ready features**: Health checks, metrics, etc.

### Key Spring Boot Concepts

#### 1. Dependency Injection
Instead of creating objects manually, Spring creates and manages them:

```java
@Service  // Spring manages this class
public class CustomerService {
    @Autowired  // Spring injects this dependency
    private CustomerRepository customerRepository;
    
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
```

#### 2. Controllers
Handle HTTP requests and return responses:

```java
@RestController  // Marks this as a REST controller
@RequestMapping("/api/customers")  // Base URL for all methods
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping  // Handles GET requests
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    @PostMapping  // Handles POST requests
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }
}
```

#### 3. Services
Contain business logic:

```java
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Customer createCustomer(Customer customer) {
        // Business logic here
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return customerRepository.save(customer);
    }
}
```

#### 4. Repositories
Handle database operations:

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Spring Data JPA automatically creates these methods
    Optional<Customer> findByEmail(String email);
    List<Customer> findByFirstNameContaining(String firstName);
}
```

### Practice Exercises

1. **Create a ProductService** with methods like createProduct(), findById(), updateProduct()
2. **Create a ProductController** with REST endpoints for CRUD operations
3. **Add validation** to your Product entity using annotations

## 🗄️ Database & JPA

### What is JPA?
JPA (Java Persistence API) lets you work with databases using Java objects instead of writing SQL queries.

### Key JPA Concepts

#### 1. Entities
Represent database tables as Java classes:

```java
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(unique = true)
    private String email;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
```

#### 2. Relationships
Define how entities relate to each other:

```java
// One-to-Many relationship
@OneToMany(mappedBy = "customer")
private List<Booking> bookings;

// Many-to-One relationship
@ManyToOne
@JoinColumn(name = "customer_id")
private Customer customer;

// Many-to-Many relationship
@ManyToMany
@JoinTable(name = "customer_services")
private Set<Service> services;
```

#### 3. Repository Methods
Spring Data JPA creates implementations automatically:

```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Find by field
    Optional<Customer> findByEmail(String email);
    
    // Find with conditions
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
    
    // Custom query
    @Query("SELECT c FROM Customer c WHERE c.email LIKE %:email%")
    List<Customer> findByEmailContaining(@Param("email") String email);
}
```

### Practice Exercises

1. **Create a Service entity** with properties like name, description, price
2. **Create a Booking entity** that relates to Customer and Mechanic
3. **Add repository methods** for finding bookings by date range

## 🌐 REST API Development

### What is REST?
REST (Representational State Transfer) is an architectural style for designing web services:
- **Stateless**: Each request contains all needed information
- **Resource-based**: URLs represent resources
- **HTTP methods**: GET, POST, PUT, DELETE have specific meanings
- **JSON format**: Data is exchanged in JSON

### HTTP Methods Explained

| Method | Purpose | Example |
|--------|---------|---------|
| GET | Retrieve data | `GET /api/customers` |
| POST | Create new data | `POST /api/customers` |
| PUT | Update existing data | `PUT /api/customers/1` |
| DELETE | Remove data | `DELETE /api/customers/1` |

### REST API Best Practices

#### 1. URL Design
```
GET    /api/customers          # Get all customers
GET    /api/customers/1        # Get customer with ID 1
POST   /api/customers          # Create new customer
PUT    /api/customers/1        # Update customer 1
DELETE /api/customers/1        # Delete customer 1
```

#### 2. Status Codes
```java
@PostMapping
public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
    Customer saved = customerService.createCustomer(customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}

@GetMapping("/{id}")
public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
    Customer customer = customerService.findById(id);
    if (customer == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(customer);
}
```

#### 3. Error Handling
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
```

### Practice Exercises

1. **Create a complete REST API** for managing products
2. **Add proper error handling** with custom exception classes
3. **Implement pagination** for listing endpoints

## 🔐 Security & Authentication

### What is JWT?
JWT (JSON Web Token) is a secure way to transmit information between parties. It's like a digital passport that proves who you are.

### JWT Structure
```
Header.Payload.Signature
```

Example:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGVtYWlsLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.abc123
```

### Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()  // Public endpoints
                .anyRequest().authenticated()  // All others need authentication
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

### JWT Implementation

```java
@Component
public class JwtUtil {
    
    private String secret = "mySecretKey";
    private int expiration = 86400000; // 24 hours
    
    public String generateToken(String email, Long userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);
        return createToken(claims, email);
    }
    
    public Boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
}
```

### Practice Exercises

1. **Implement JWT authentication** for your API
2. **Add role-based access control** (admin, user, mechanic)
3. **Create a refresh token mechanism**

## 🎨 Frontend Development

### HTML Structure
HTML provides the structure of web pages:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Mechanic On Demand</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#">Mechanic On Demand</a>
        </div>
    </nav>
    
    <main class="container mt-4">
        <h1>Welcome to Mechanic On Demand</h1>
        <p>Find and book mechanics instantly!</p>
    </main>
</body>
</html>
```

### CSS Styling
CSS makes your HTML look beautiful:

```css
/* Custom styles */
.hero-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 100px 0;
}

.btn-custom {
    background: #ff6b6b;
    border: none;
    border-radius: 25px;
    padding: 12px 30px;
    color: white;
    font-weight: bold;
}

.btn-custom:hover {
    background: #ff5252;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}
```

### JavaScript Functionality
JavaScript makes your website interactive:

```javascript
// API Service
class ApiService {
    static async request(endpoint, options = {}) {
        const url = `http://localhost:8080/api${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };
        
        const response = await fetch(url, config);
        return response.json();
    }
    
    static async login(email, password) {
        return this.request('/auth/customer/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
    }
}

// Usage
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const response = await ApiService.login(
        formData.get('email'),
        formData.get('password')
    );
    console.log('Login successful:', response);
});
```

### Practice Exercises

1. **Create a responsive login form** with Bootstrap
2. **Build a mechanic listing page** with search and filter
3. **Implement real-time form validation**

## 🔗 Full Stack Integration

### Connecting Frontend to Backend

#### 1. CORS Configuration
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

#### 2. Frontend API Integration
```javascript
// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Authentication
let authToken = localStorage.getItem('token');

// Make authenticated requests
async function fetchBookings() {
    const response = await fetch(`${API_BASE_URL}/bookings`, {
        headers: {
            'Authorization': `Bearer ${authToken}`
        }
    });
    return response.json();
}
```

#### 3. Error Handling
```javascript
async function handleApiCall(apiFunction) {
    try {
        const result = await apiFunction();
        return { success: true, data: result };
    } catch (error) {
        console.error('API Error:', error);
        return { success: false, error: error.message };
    }
}
```

### Practice Exercises

1. **Connect your frontend** to the backend API
2. **Implement proper error handling** for API calls
3. **Add loading states** and user feedback

## 🐳 DevOps & Deployment

### What is Docker?
Docker packages your application and all its dependencies into a container that runs consistently anywhere.

### Dockerfile Example
```dockerfile
# Use Java 11 as base image
FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Copy application files
COPY target/mechanic-on-demand-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: mechanic_ondemand
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

### Practice Exercises

1. **Create Dockerfiles** for your application
2. **Set up Docker Compose** for local development
3. **Deploy to a cloud platform** like Heroku or AWS

## ✅ Best Practices

### Code Organization
```
src/main/java/com/mechanicondemand/
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entities
├── config/         # Configuration
└── security/       # Security configuration
```

### Naming Conventions
- **Classes**: PascalCase (CustomerService)
- **Methods**: camelCase (findById)
- **Variables**: camelCase (customerName)
- **Constants**: UPPER_SNAKE_CASE (MAX_RETRY_COUNT)

### Error Handling
```java
// Always handle exceptions
try {
    Customer customer = customerService.findById(id);
    return ResponseEntity.ok(customer);
} catch (CustomerNotFoundException e) {
    return ResponseEntity.notFound().build();
} catch (Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
}
```

### Security Best Practices
1. **Validate all inputs**
2. **Use HTTPS in production**
3. **Implement rate limiting**
4. **Log security events**
5. **Keep dependencies updated**

## 🚀 Next Steps

### Immediate Actions
1. **Complete the setup guide** and run the application
2. **Experiment with the code** - try adding new features
3. **Read the documentation** thoroughly
4. **Practice with the exercises** in each section

### Learning Path
1. **Week 1-2**: Master Java fundamentals
2. **Week 3-4**: Learn Spring Boot deeply
3. **Week 5-6**: Understand databases and JPA
4. **Week 7-8**: Build REST APIs
5. **Week 9-10**: Learn frontend development
6. **Week 11-12**: Integrate everything together

### Project Ideas
1. **E-commerce platform** with product catalog and shopping cart
2. **Task management system** with user roles and project tracking
3. **Social media app** with posts, comments, and likes
4. **Booking system** for restaurants or hotels
5. **Learning management system** with courses and assignments

### Resources
- **Official Documentation**: [Spring Boot Docs](https://spring.io/projects/spring-boot)
- **Tutorials**: [Baeldung](https://www.baeldung.com/)
- **Practice**: [LeetCode](https://leetcode.com/), [HackerRank](https://www.hackerrank.com/)
- **Community**: [Stack Overflow](https://stackoverflow.com/), [Reddit r/java](https://www.reddit.com/r/java/)

## 🎉 Conclusion

Congratulations! You now have a solid foundation in Java Full Stack development. Remember:

1. **Practice regularly** - coding is a skill that improves with practice
2. **Build projects** - apply what you learn in real projects
3. **Read code** - study open-source projects to learn best practices
4. **Join communities** - connect with other developers
5. **Never stop learning** - technology evolves constantly

The "Mechanic On Demand" project demonstrates all the key concepts you need to know. Use it as a reference and starting point for your own projects.

Happy coding! 🚀