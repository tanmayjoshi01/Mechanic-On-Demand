# 📚 Mechanic On Demand - API Documentation

## 🌐 Base URL
```
http://localhost:8080/api
```

## 🔐 Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📋 API Endpoints

### 🔑 Authentication Endpoints

#### Register Customer
```http
POST /auth/customer/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@email.com",
  "password": "password123",
  "phone": "555-0101",
  "address": "123 Main St, City, State"
}
```

**Response (201 Created):**
```json
{
  "message": "Customer registered successfully",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "customer": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@email.com",
    "phone": "555-0101"
  }
}
```

#### Login Customer
```http
POST /auth/customer/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john@email.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "customer": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@email.com",
    "phone": "555-0101"
  }
}
```

#### Register Mechanic
```http
POST /auth/mechanic/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "Mike",
  "lastName": "Johnson",
  "email": "mike@mechanic.com",
  "password": "password123",
  "phone": "555-0201",
  "address": "789 Service St, City, State",
  "specialization": "Engine Specialist",
  "experienceYears": 8,
  "hourlyRate": 75.00
}
```

**Response (201 Created):**
```json
{
  "message": "Mechanic registered successfully",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "mechanic": {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "email": "mike@mechanic.com",
    "phone": "555-0201",
    "specialization": "Engine Specialist",
    "hourlyRate": 75.00
  }
}
```

#### Login Mechanic
```http
POST /auth/mechanic/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "mike@mechanic.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "mechanic": {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "email": "mike@mechanic.com",
    "phone": "555-0201",
    "specialization": "Engine Specialist",
    "hourlyRate": 75.00,
    "rating": 4.8,
    "isAvailable": true
  }
}
```

### 🔧 Mechanic Endpoints

#### Get All Mechanics
```http
GET /mechanics
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "email": "mike@mechanic.com",
    "phone": "555-0201",
    "specialization": "Engine Specialist",
    "experienceYears": 8,
    "hourlyRate": 75.00,
    "isAvailable": true,
    "rating": 4.8,
    "totalRatings": 25
  }
]
```

#### Get Available Mechanics
```http
GET /mechanics/available
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "specialization": "Engine Specialist",
    "hourlyRate": 75.00,
    "isAvailable": true,
    "rating": 4.8
  }
]
```

#### Find Nearby Mechanics
```http
GET /mechanics/nearby?lat=40.7128&lng=-74.0060&radius=10
```

**Query Parameters:**
- `lat` (required): Latitude
- `lng` (required): Longitude
- `radius` (optional): Search radius in kilometers (default: 10)

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "specialization": "Engine Specialist",
    "hourlyRate": 75.00,
    "isAvailable": true,
    "rating": 4.8,
    "latitude": 40.7505,
    "longitude": -73.9934
  }
]
```

#### Find Mechanics by Specialization
```http
GET /mechanics/specialization?specialization=Engine
```

**Query Parameters:**
- `specialization` (required): Specialization to search for

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "specialization": "Engine Specialist",
    "hourlyRate": 75.00,
    "isAvailable": true,
    "rating": 4.8
  }
]
```

#### Find Mechanics by Rate Range
```http
GET /mechanics/rate-range?minRate=50&maxRate=100
```

**Query Parameters:**
- `minRate` (required): Minimum hourly rate
- `maxRate` (required): Maximum hourly rate

#### Find Mechanics by Minimum Rating
```http
GET /mechanics/rating?minRating=4.0
```

**Query Parameters:**
- `minRating` (required): Minimum rating (1-5)

#### Get Mechanic by ID
```http
GET /mechanics/{id}
```

**Path Parameters:**
- `id` (required): Mechanic ID

#### Update Mechanic Availability
```http
PUT /mechanics/{id}/availability
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "isAvailable": true
}
```

#### Update Mechanic Profile
```http
PUT /mechanics/{id}
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "firstName": "Mike",
  "lastName": "Johnson",
  "phone": "555-0201",
  "address": "789 Service St, City, State",
  "specialization": "Engine Specialist",
  "hourlyRate": 80.00
}
```

### 📅 Booking Endpoints

#### Create Booking
```http
POST /bookings
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "customerId": 1,
  "mechanicId": 1,
  "serviceType": "Engine Repair",
  "description": "Car won't start, engine makes clicking noise",
  "vehicleInfo": "2020 Toyota Camry Red",
  "bookingDate": "2024-01-15T10:00:00"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "customer": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@email.com"
  },
  "mechanic": {
    "id": 1,
    "firstName": "Mike",
    "lastName": "Johnson",
    "specialization": "Engine Specialist"
  },
  "serviceType": "Engine Repair",
  "description": "Car won't start, engine makes clicking noise",
  "vehicleInfo": "2020 Toyota Camry Red",
  "bookingDate": "2024-01-15T10:00:00",
  "status": "PENDING",
  "estimatedDuration": 60,
  "totalCost": 75.00,
  "createdAt": "2024-01-10T09:30:00"
}
```

#### Get All Bookings
```http
GET /bookings
Authorization: Bearer <token>
```

#### Get Booking by ID
```http
GET /bookings/{id}
Authorization: Bearer <token>
```

#### Get Customer Bookings
```http
GET /bookings/customer/{customerId}
Authorization: Bearer <token>
```

#### Get Mechanic Bookings
```http
GET /bookings/mechanic/{mechanicId}
Authorization: Bearer <token>
```

#### Get Pending Bookings for Mechanic
```http
GET /bookings/mechanic/{mechanicId}/pending
Authorization: Bearer <token>
```

#### Accept Booking
```http
PUT /bookings/{id}/accept
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "mechanicId": 1
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "status": "ACCEPTED",
  "updatedAt": "2024-01-10T10:15:00"
}
```

#### Reject Booking
```http
PUT /bookings/{id}/reject
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "mechanicId": 1
}
```

#### Complete Booking
```http
PUT /bookings/{id}/complete
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "mechanicId": 1,
  "actualDuration": 120
}
```

#### Rate Booking
```http
PUT /bookings/{id}/rate
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "customerId": 1,
  "rating": 5,
  "feedback": "Excellent service! Fixed my car quickly and professionally."
}
```

#### Cancel Booking
```http
PUT /bookings/{id}/cancel
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "userId": 1,
  "userType": "CUSTOMER"
}
```

#### Get Bookings by Status
```http
GET /bookings/status/{status}
Authorization: Bearer <token>
```

**Path Parameters:**
- `status`: PENDING, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED, CANCELLED

### 💰 Pricing Endpoints

#### Get All Pricing
```http
GET /pricing
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "serviceType": "Basic Maintenance",
    "monthlyPrice": 29.99,
    "yearlyPrice": 299.99,
    "description": "Oil changes, tire rotation, basic checks"
  },
  {
    "id": 2,
    "serviceType": "Engine Repair",
    "monthlyPrice": 49.99,
    "yearlyPrice": 499.99,
    "description": "Engine diagnostics and repairs"
  }
]
```

#### Get Pricing by Service Type
```http
GET /pricing/{serviceType}
```

**Path Parameters:**
- `serviceType` (required): Service type name

**Response (200 OK):**
```json
{
  "id": 1,
  "serviceType": "Basic Maintenance",
  "monthlyPrice": 29.99,
  "yearlyPrice": 299.99,
  "description": "Oil changes, tire rotation, basic checks"
}
```

#### Create Pricing (Admin)
```http
POST /pricing
Content-Type: application/json
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "serviceType": "New Service",
  "monthlyPrice": 39.99,
  "yearlyPrice": 399.99,
  "description": "Description of the new service"
}
```

#### Update Pricing (Admin)
```http
PUT /pricing/{id}
Content-Type: application/json
Authorization: Bearer <token>
```

#### Delete Pricing (Admin)
```http
DELETE /pricing/{id}
Authorization: Bearer <token>
```

## 📊 Response Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid request data |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Access denied |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

## 🔒 Error Responses

### 400 Bad Request
```json
{
  "error": "Validation failed",
  "details": [
    {
      "field": "email",
      "message": "Email should be valid"
    }
  ]
}
```

### 401 Unauthorized
```json
{
  "error": "Invalid email or password"
}
```

### 404 Not Found
```json
{
  "error": "Mechanic not found with ID: 999"
}
```

### 500 Internal Server Error
```json
{
  "error": "An unexpected error occurred"
}
```

## 🧪 Testing the API

### Using cURL

#### Register a Customer
```bash
curl -X POST http://localhost:8080/api/auth/customer/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@email.com",
    "password": "password123",
    "phone": "555-0101"
  }'
```

#### Login Customer
```bash
curl -X POST http://localhost:8080/api/auth/customer/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@email.com",
    "password": "password123"
  }'
```

#### Get Available Mechanics
```bash
curl -X GET http://localhost:8080/api/mechanics/available
```

#### Create Booking (with token)
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "customerId": 1,
    "mechanicId": 1,
    "serviceType": "Engine Repair",
    "description": "Car won't start",
    "vehicleInfo": "2020 Toyota Camry",
    "bookingDate": "2024-01-15T10:00:00"
  }'
```

### Using Postman

1. **Import Collection:** Create a new collection in Postman
2. **Set Base URL:** `http://localhost:8080/api`
3. **Add Headers:** 
   - `Content-Type: application/json`
   - `Authorization: Bearer <token>` (for protected endpoints)
4. **Test Endpoints:** Create requests for each endpoint

### Using JavaScript Fetch

```javascript
// Login and get token
const loginResponse = await fetch('http://localhost:8080/api/auth/customer/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'john@email.com',
    password: 'password123'
  })
});

const loginData = await loginResponse.json();
const token = loginData.token;

// Use token for protected requests
const bookingsResponse = await fetch('http://localhost:8080/api/bookings', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const bookings = await bookingsResponse.json();
```

## 📝 Notes

1. **JWT Tokens:** Tokens expire after 24 hours by default
2. **CORS:** The API supports CORS for frontend integration
3. **Validation:** All input data is validated on the server side
4. **Pagination:** Large result sets should implement pagination
5. **Rate Limiting:** Consider implementing rate limiting for production use
6. **HTTPS:** Use HTTPS in production for security
7. **Logging:** All API calls are logged for debugging

## 🔄 WebSocket Support (Future Enhancement)

Real-time features like live booking updates could be implemented using WebSockets:

```javascript
// Future WebSocket implementation
const socket = new WebSocket('ws://localhost:8080/ws');
socket.onmessage = (event) => {
  const update = JSON.parse(event.data);
  // Handle real-time updates
};
```