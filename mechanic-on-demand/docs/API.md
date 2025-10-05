# 🔧 Mechanic On Demand - REST API Documentation

## 📚 What is REST API?

**REST (Representational State Transfer)** is an architectural style for designing networked applications. It's based on HTTP protocol and uses standard HTTP methods.

### REST vs SOAP

| Feature | REST | SOAP |
|---------|------|------|
| **Protocol** | HTTP/HTTPS | HTTP, SMTP, TCP |
| **Data Format** | JSON, XML | XML only |
| **Performance** | Faster, lighter | Slower, heavier |
| **Caching** | Yes | No |
| **Learning Curve** | Easy | Complex |
| **Use Case** | Web APIs, Mobile | Enterprise systems |

### HTTP Methods Explained

| Method | Purpose | Example |
|--------|---------|---------|
| **GET** | Retrieve data | `GET /api/users` - Get all users |
| **POST** | Create new resource | `POST /api/users` - Create user |
| **PUT** | Update entire resource | `PUT /api/users/1` - Update user 1 |
| **DELETE** | Delete resource | `DELETE /api/users/1` - Delete user 1 |
| **PATCH** | Partial update | `PATCH /api/users/1` - Update user 1 partially |

## 🌐 Base URL
```
http://localhost:8080/api
```

## 🔐 Authentication

All protected endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📋 API Endpoints

### 1. Authentication Endpoints

#### POST /auth/login
**Purpose:** User login  
**Request Body:**
```json
{
  "usernameOrEmail": "john_doe",
  "password": "password123"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "CUSTOMER"
}
```

#### POST /auth/register
**Purpose:** User registration  
**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "userType": "CUSTOMER"
}
```
**Response:**
```json
{
  "message": "User registered successfully",
  "userId": "1"
}
```

#### GET /auth/me
**Purpose:** Get current user info  
**Headers:** `Authorization: Bearer <token>`  
**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "userType": "CUSTOMER",
  "isActive": true
}
```

### 2. User Management Endpoints

#### GET /users/mechanics
**Purpose:** Get all available mechanics  
**Response:**
```json
[
  {
    "id": 2,
    "username": "mechanic1",
    "firstName": "Mike",
    "lastName": "Smith",
    "specialization": "Engine Repair",
    "rating": 4.5,
    "hourlyRate": 50.00,
    "isAvailable": true
  }
]
```

#### GET /users/mechanics/nearby
**Purpose:** Find nearby mechanics  
**Query Parameters:**
- `latitude`: 40.7128
- `longitude`: -74.0060
- `radius`: 10 (km)

#### GET /users/mechanics/specialization/{specialization}
**Purpose:** Find mechanics by specialization  
**Example:** `/users/mechanics/specialization/Engine Repair`

### 3. Booking Endpoints

#### POST /bookings
**Purpose:** Create new booking  
**Headers:** `Authorization: Bearer <token>`  
**Request Body:**
```json
{
  "mechanicId": 2,
  "serviceId": 1,
  "bookingDate": "2024-01-15",
  "bookingTime": "10:00:00",
  "address": "123 Main St, New York, NY",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "problemDescription": "Car won't start",
  "specialInstructions": "Please call before arriving"
}
```

#### GET /bookings
**Purpose:** Get user's bookings  
**Headers:** `Authorization: Bearer <token>`  
**Query Parameters:**
- `status`: PENDING, ACCEPTED, COMPLETED, etc.

#### PUT /bookings/{id}/status
**Purpose:** Update booking status (for mechanics)  
**Headers:** `Authorization: Bearer <token>`  
**Request Body:**
```json
{
  "status": "ACCEPTED"
}
```

### 4. Service Endpoints

#### GET /services/categories
**Purpose:** Get all service categories  
**Response:**
```json
[
  {
    "id": 1,
    "name": "Engine Repair",
    "description": "Engine diagnostics and repair services",
    "isActive": true
  }
]
```

#### GET /services/mechanic/{mechanicId}
**Purpose:** Get services offered by a mechanic  
**Response:**
```json
[
  {
    "id": 1,
    "serviceName": "Engine Diagnostic",
    "description": "Complete engine health check",
    "basePrice": 75.00,
    "estimatedDuration": 60,
    "isAvailable": true
  }
]
```

### 5. Pricing Endpoints

#### GET /pricing/plans
**Purpose:** Get all pricing plans  
**Response:**
```json
[
  {
    "id": 1,
    "name": "Basic Plan",
    "description": "Basic mechanic services",
    "price": 29.99,
    "durationMonths": 1,
    "features": "[\"5 bookings per month\", \"Basic support\"]",
    "isActive": true
  }
]
```

## 📊 HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Access denied |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server error |

## 🔧 Error Response Format

```json
{
  "error": "Error message",
  "timestamp": "2024-01-15T10:30:00Z",
  "path": "/api/bookings"
}
```

## 🧪 Testing the API

### Using cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"john_doe","password":"password123"}'

# Get mechanics (with token)
curl -X GET http://localhost:8080/api/users/mechanics \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Using Postman

1. Import the API collection
2. Set base URL: `http://localhost:8080/api`
3. Add Authorization header for protected endpoints
4. Test each endpoint

## 📝 Notes

- All timestamps are in ISO 8601 format
- All prices are in USD
- JWT tokens expire in 24 hours
- Rate limiting: 100 requests per minute per IP
- CORS is enabled for frontend integration