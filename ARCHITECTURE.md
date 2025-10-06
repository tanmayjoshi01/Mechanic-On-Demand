# 🏗️ Mechanic On Demand - Architecture Diagram

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│                         USER (Browser/Mobile)                               │
│                                                                             │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 │ HTTP/HTTPS
                                 │
┌────────────────────────────────▼────────────────────────────────────────────┐
│                                                                             │
│                      FRONTEND (Port 80)                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                          NGINX                                       │  │
│  │  - Serves static files (HTML, CSS, JS)                             │  │
│  │  - Proxies API requests to backend                                 │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
│  HTML Pages:              CSS:                JavaScript:                  │
│  • index.html             • style.css         • auth.js                    │
│  • login.html                                 • dashboard.js               │
│  • register.html                              • mechanics.js               │
│  • dashboard.html                                                          │
│  • find-mechanics.html                                                     │
│  • pricing.html                                                            │
│                                                                             │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 │ REST API Calls
                                 │ Authorization: Bearer JWT
                                 │
┌────────────────────────────────▼────────────────────────────────────────────┐
│                                                                             │
│                    BACKEND (Port 8080)                                      │
│                    Spring Boot Application                                  │
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                     SECURITY LAYER                                   │  │
│  │  ┌──────────────────┐        ┌────────────────────────────────┐    │  │
│  │  │  JwtAuthFilter   │───────▶│  SecurityConfig                │    │  │
│  │  │  - Validate JWT  │        │  - Configure security          │    │  │
│  │  │  - Extract user  │        │  - Public/protected endpoints  │    │  │
│  │  └──────────────────┘        └────────────────────────────────┘    │  │
│  │                                                                       │  │
│  │  ┌──────────────────────────────────────────────────────────────┐  │  │
│  │  │                       JwtUtil                                 │  │  │
│  │  │  - Generate JWT tokens                                       │  │  │
│  │  │  - Validate tokens                                           │  │  │
│  │  └──────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                 │                                           │
│                                 ▼                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                     CONTROLLER LAYER                                 │  │
│  │  ┌──────────────┐  ┌──────────────────┐  ┌──────────────────────┐  │  │
│  │  │ AuthController│  │ MechanicController│ │ BookingController    │  │  │
│  │  │              │  │                  │  │                      │  │  │
│  │  │ /auth/       │  │ /mechanics/      │  │ /bookings/           │  │  │
│  │  │ - register   │  │ - GET all        │  │ - POST create        │  │  │
│  │  │ - login      │  │ - GET nearby     │  │ - PUT accept/reject  │  │  │
│  │  │              │  │ - PUT availability│  │ - PUT complete       │  │  │
│  │  └──────┬───────┘  └────────┬─────────┘  └──────────┬───────────┘  │  │
│  └─────────┼─────────────────────┼────────────────────────┼────────────┘  │
│            │                     │                        │                │
│            ▼                     ▼                        ▼                │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                       SERVICE LAYER                                  │  │
│  │  ┌────────────┐     ┌─────────────────┐     ┌─────────────────┐    │  │
│  │  │AuthService │     │ MechanicService │     │ BookingService  │    │  │
│  │  │            │     │                 │     │                 │    │  │
│  │  │- register  │     │- findNearby     │     │- createBooking  │    │  │
│  │  │- login     │     │- updateProfile  │     │- acceptBooking  │    │  │
│  │  │- validate  │     │- availability   │     │- rateBooking    │    │  │
│  │  │- hash pwd  │     │                 │     │- calculatePrice │    │  │
│  │  └────┬───────┘     └────────┬────────┘     └────────┬────────┘    │  │
│  └───────┼──────────────────────┼───────────────────────┼─────────────┘  │
│          │                      │                       │                 │
│          ▼                      ▼                       ▼                 │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                     REPOSITORY LAYER (JPA)                           │  │
│  │  ┌──────────────────┐  ┌───────────────────┐  ┌──────────────────┐ │  │
│  │  │CustomerRepository│  │MechanicRepository │  │BookingRepository │ │  │
│  │  │                  │  │                   │  │                  │ │  │
│  │  │- findByEmail     │  │- findByEmail      │  │- findByCustomer  │ │  │
│  │  │- save            │  │- findNearby(GPS)  │  │- findByMechanic  │ │  │
│  │  │- existsByEmail   │  │- findAvailable    │  │- findByStatus    │ │  │
│  │  └────────┬─────────┘  └─────────┬─────────┘  └────────┬─────────┘ │  │
│  └───────────┼─────────────────────┼─────────────────────┼────────────┘  │
│              │                     │                      │               │
│              └─────────────────────┴──────────────────────┘               │
│                                    │                                      │
│                                    │ JDBC                                 │
│                                    ▼                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                       MODEL LAYER (Entities)                         │  │
│  │  ┌─────────────┐      ┌──────────┐      ┌──────────────────────┐   │  │
│  │  │  Customer   │      │ Mechanic │      │     Booking          │   │  │
│  │  │             │      │          │      │                      │   │  │
│  │  │- id         │      │- id      │      │- id                  │   │  │
│  │  │- name       │      │- name    │      │- customer_id (FK)    │   │  │
│  │  │- email      │      │- email   │      │- mechanic_id (FK)    │   │  │
│  │  │- password   │      │- password│      │- vehicleType         │   │  │
│  │  │- phone      │      │- phone   │      │- status              │   │  │
│  │  │- latitude   │      │- latitude│      │- subscriptionType    │   │  │
│  │  │- longitude  │      │- longitude│     │- price               │   │  │
│  │  │- role       │      │- specialty│     │- rating              │   │  │
│  │  └─────────────┘      │- rating  │      └──────────────────────┘   │  │
│  │                       │- available│                                  │  │
│  │                       │- role     │                                  │  │
│  │                       └──────────┘                                   │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 │ SQL Queries
                                 │
┌────────────────────────────────▼────────────────────────────────────────────┐
│                                                                             │
│                       DATABASE (Port 3306)                                  │
│                          MySQL 8.0                                          │
│                                                                             │
│  Tables:                                                                    │
│  ┌──────────────┐      ┌──────────────┐      ┌──────────────┐            │
│  │  customers   │      │  mechanics   │      │   bookings   │            │
│  │              │      │              │      │              │            │
│  │  PK: id      │      │  PK: id      │      │  PK: id      │            │
│  │  UK: email   │      │  UK: email   │      │  FK: cust_id │            │
│  │              │      │              │      │  FK: mech_id │            │
│  └──────────────┘      └──────────────┘      └──────────────┘            │
│                                                                             │
│  Indexes:                                                                   │
│  • customers(email)       • mechanics(email)      • bookings(status)       │
│  • customers(lat, lng)    • mechanics(lat, lng)   • bookings(customer_id)  │
│                          • mechanics(available)   • bookings(mechanic_id)  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Request Flow Diagram

### Example: Customer Books a Mechanic

```
┌────────┐
│ STEP 1 │ User clicks "Book Mechanic" button
└────┬───┘
     │
     ▼
┌─────────────────────────────────────────────┐
│ FRONTEND (JavaScript - mechanics.js)         │
│                                              │
│  1. Collect form data                       │
│  2. Create JSON request body                │
│  3. Get JWT token from localStorage         │
│  4. Make POST request to backend            │
└─────────────────┬───────────────────────────┘
                  │
                  │ POST /api/bookings
                  │ Headers: { Authorization: Bearer <token> }
                  │ Body: { customerId, mechanicId, vehicleType, ... }
                  │
                  ▼
┌─────────────────────────────────────────────┐
│ BACKEND - Security Layer                     │
│                                              │
│  JwtAuthenticationFilter:                   │
│  1. Extract JWT from Authorization header   │
│  2. Validate token signature                │
│  3. Extract user email and role             │
│  4. Set authentication in SecurityContext   │
└─────────────────┬───────────────────────────┘
                  │ ✓ Authenticated
                  ▼
┌─────────────────────────────────────────────┐
│ BACKEND - Controller Layer                   │
│                                              │
│  BookingController.createBooking():         │
│  1. @Valid annotation validates input       │
│  2. Calls BookingService                    │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│ BACKEND - Service Layer                      │
│                                              │
│  BookingService.createBooking():            │
│  1. Find customer by ID                     │
│  2. Find mechanic by ID                     │
│  3. Calculate price based on subscription   │
│  4. Create Booking entity                   │
│  5. Set status to PENDING                   │
│  6. Call BookingRepository.save()           │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│ BACKEND - Repository Layer                   │
│                                              │
│  BookingRepository.save(booking):           │
│  1. JPA converts entity to SQL              │
│  2. Generate INSERT statement               │
└─────────────────┬───────────────────────────┘
                  │
                  │ SQL: INSERT INTO bookings (customer_id, mechanic_id, ...)
                  │      VALUES (1, 2, ...)
                  ▼
┌─────────────────────────────────────────────┐
│ DATABASE (MySQL)                             │
│                                              │
│  1. Execute INSERT query                    │
│  2. Generate auto-increment ID              │
│  3. Save to bookings table                  │
│  4. Return saved record                     │
└─────────────────┬───────────────────────────┘
                  │
                  │ Booking saved with ID: 123
                  │
    ┌─────────────┴─────────────┐
    │    Response flows back     │
    │    through all layers      │
    └─────────────┬─────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│ BACKEND - HTTP Response                      │
│                                              │
│  Status: 201 Created                        │
│  Body: {                                    │
│    "success": true,                         │
│    "message": "Booking created",            │
│    "data": { ... booking details ... }      │
│  }                                          │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│ FRONTEND - JavaScript                        │
│                                              │
│  1. Receive response                        │
│  2. Parse JSON                              │
│  3. Update UI (show success message)        │
│  4. Redirect to dashboard                   │
└─────────────────────────────────────────────┘
```

---

## Authentication Flow

```
┌─────────────┐
│   USER      │
└──────┬──────┘
       │
       │ 1. Enter email & password
       │
       ▼
┌──────────────────────────────┐
│  POST /api/auth/login        │
│  { email, password }         │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  AuthController              │
│  • Receive credentials       │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  AuthService                 │
│  1. Find user by email       │
│  2. Verify password (BCrypt) │
│  3. Generate JWT token       │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  JwtUtil                     │
│  • Create token with claims  │
│  • Sign with secret key      │
│  • Set expiration (24h)      │
└──────┬───────────────────────┘
       │
       │ Return: { token: "eyJhbG...", user: {...} }
       │
       ▼
┌──────────────────────────────┐
│  Frontend JavaScript         │
│  1. Store token in localStorage
│  2. Store user info          │
│  3. Redirect to dashboard    │
└──────┬───────────────────────┘
       │
       │ Subsequent API calls
       │
       ▼
┌──────────────────────────────┐
│  GET /api/bookings           │
│  Authorization: Bearer token │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  JwtAuthenticationFilter     │
│  1. Extract token            │
│  2. Validate signature       │
│  3. Check expiration         │
│  4. Set authentication       │
└──────┬───────────────────────┘
       │
       │ ✓ Request authenticated
       │
       ▼
┌──────────────────────────────┐
│  Process request normally    │
└──────────────────────────────┘
```

---

## Docker Container Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         DOCKER HOST                              │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    mechanic-network (Bridge)              │  │
│  │                                                            │  │
│  │  ┌──────────────┐   ┌──────────────┐   ┌──────────────┐ │  │
│  │  │  Container   │   │  Container   │   │  Container   │ │  │
│  │  │   NGINX      │   │ Spring Boot  │   │    MySQL     │ │  │
│  │  │  (Frontend)  │   │  (Backend)   │   │  (Database)  │ │  │
│  │  │              │   │              │   │              │ │  │
│  │  │  Port: 80    │   │  Port: 8080  │   │  Port: 3306  │ │  │
│  │  │              │   │              │   │              │ │  │
│  │  │  Serves:     │   │  Spring Boot │   │  MySQL 8.0   │ │  │
│  │  │  - HTML/CSS  │   │  - REST API  │   │  - Database  │ │  │
│  │  │  - JS files  │   │  - JWT Auth  │   │              │ │  │
│  │  │              │   │  - JPA       │   │              │ │  │
│  │  │  Proxies to  │   │              │   │              │ │  │
│  │  │  Backend ────┼───┼──────────────┼───┼──────────────┤ │  │
│  │  │              │   │              │   │              │ │  │
│  │  └──────────────┘   └──────────────┘   └──────────────┘ │  │
│  │                                                            │  │
│  │  Volumes:                                                  │  │
│  │  • ./frontend → /usr/share/nginx/html                     │  │
│  │  • mysql_data → /var/lib/mysql                            │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Exposed Ports:                                                 │
│  • 80   → nginx (Frontend)                                      │
│  • 8080 → backend (API)                                         │
│  • 3306 → mysql (Database)                                      │
└─────────────────────────────────────────────────────────────────┘

Commands:
docker-compose up -d     → Start all containers
docker-compose down      → Stop all containers
docker-compose logs -f   → View logs
docker ps                → List running containers
```

---

## Database Schema Relationships

```
┌─────────────────────────┐
│      customers          │
│─────────────────────────│
│ PK  id                  │
│     name                │
│ UK  email               │
│     password (hashed)   │
│     phone               │
│     latitude            │
│     longitude           │
│     role                │
│     created_at          │
└──────────┬──────────────┘
           │
           │ 1
           │
           │ has many
           │
           │ *
           ▼
┌─────────────────────────┐
│       bookings          │
│─────────────────────────│
│ PK  id                  │
│ FK  customer_id         │───┐
│ FK  mechanic_id         │───┼───────────────────┐
│     vehicle_type        │   │                   │
│     vehicle_model       │   │                   │
│     problem_description │   │                   │
│     status              │   │                   │
│     subscription_type   │   │                   │
│     price               │   │                   │
│     rating              │   │                   │
│     created_at          │   │                   │
└─────────────────────────┘   │                   │
                              │                   │
                              │ belongs to        │
                              │ (Many-to-One)     │
                              │                   │
                              └───────────────────┼───────┐
                                                  │       │
                                                  ▼       │
                                      ┌─────────────────────────┐
                                      │      mechanics          │
                                      │─────────────────────────│
                                      │ PK  id                  │
                                      │     name                │
                                      │ UK  email               │
                                      │     password            │
                                      │     phone               │
                                      │     specialty           │
                                      │     latitude            │
                                      │     longitude           │
                                      │     available           │
                                      │     rating              │
                                      │     total_jobs          │
                                      │     hourly_rate         │
                                      │     monthly_subscription│
                                      │     yearly_subscription │
                                      │     role                │
                                      └─────────────────────────┘
```

---

## Technology Stack Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  • HTML5 • CSS3 • Bootstrap 5 • JavaScript ES6              │
│  • Responsive Design • AJAX/Fetch API                       │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                      API LAYER (REST)                        │
│  • HTTP Methods (GET, POST, PUT, DELETE)                    │
│  • JSON Format • Status Codes • CORS                        │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                     SECURITY LAYER                           │
│  • JWT Authentication • BCrypt Password Hashing             │
│  • Spring Security • Authorization Headers                  │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                   BUSINESS LOGIC LAYER                       │
│  • Spring Boot 3.x • Service Classes • Validation           │
│  • Business Rules • Price Calculation • GPS Logic           │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                  PERSISTENCE LAYER (ORM)                     │
│  • Spring Data JPA • Hibernate • Entity Classes             │
│  • Repository Pattern • Query Methods                       │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                     DATABASE LAYER                           │
│  • MySQL 8.0 • Relational Schema • Indexes                  │
│  • Foreign Keys • Transactions                              │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                       │
│  • Docker Containers • Docker Compose • Nginx               │
│  • Volume Management • Networking                           │
└─────────────────────────────────────────────────────────────┘
```

---

## Deployment Architecture

```
                        PRODUCTION DEPLOYMENT

┌─────────────────────────────────────────────────────────────┐
│                        CLOUD PROVIDER                        │
│                    (AWS / Azure / GCP)                       │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │                    LOAD BALANCER                        │ │
│  │            (Distribute traffic)                         │ │
│  └─────────────────┬────────────────────────────────────┬──┘ │
│                    │                                     │    │
│       ┌────────────▼──────────┐          ┌────────────▼─────┐│
│       │  BACKEND INSTANCE 1   │          │ BACKEND INSTANCE 2││
│       │  (Spring Boot)        │          │ (Spring Boot)     ││
│       └────────────┬──────────┘          └─────────┬────────┘│
│                    │                                │         │
│                    └────────────┬───────────────────┘         │
│                                 │                             │
│                    ┌────────────▼──────────┐                  │
│                    │     DATABASE          │                  │
│                    │     (RDS MySQL)       │                  │
│                    │     • Replicas        │                  │
│                    │     • Backups         │                  │
│                    └───────────────────────┘                  │
│                                                               │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              FRONTEND (S3 + CloudFront)                │  │
│  │              • Static file hosting                     │  │
│  │              • CDN distribution                        │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

---

**This architecture ensures:**
- ✅ Scalability
- ✅ Security
- ✅ Maintainability
- ✅ Performance
- ✅ Separation of Concerns

---

For more details, see:
- [README.md](README.md) - Complete documentation
- [CONCEPTS_EXPLAINED.md](CONCEPTS_EXPLAINED.md) - Detailed explanations
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Project overview
