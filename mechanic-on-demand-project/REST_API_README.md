# REST API Documentation - Mechanic On Demand

## What is REST API?

REST (Representational State Transfer) API is an architectural style for designing networked applications. It uses HTTP methods to perform operations on resources, which are identified by URLs (endpoints).

### Key Principles of REST:

1. **Stateless**: Each request contains all information needed to process it
2. **Client-Server Architecture**: Clear separation between client and server
3. **Uniform Interface**: Consistent way to interact with resources
4. **Resource-Based**: Everything is treated as resources with unique identifiers
5. **Representation**: Resources can have multiple representations (JSON, XML, etc.)

## REST vs SOAP

| REST | SOAP |
|------|------|
| Uses HTTP methods (GET, POST, PUT, DELETE) | Uses XML-based protocol |
| Lightweight, faster | Heavy, more complex |
| JSON (most common) or XML | Only XML |
| Stateless | Can be stateful or stateless |
| Better for mobile/web apps | Better for enterprise systems |
| Easy to learn and implement | Steeper learning curve |

## HTTP Methods in REST

### 1. GET - Retrieve Data
- **Purpose**: Fetch information about a resource or collection of resources
- **Idempotent**: Multiple identical requests have same effect as single request
- **Safe**: Doesn't modify server state
- **Examples**:
  - `GET /api/mechanics` - Get all mechanics
  - `GET /api/mechanics/123` - Get specific mechanic
  - `GET /api/bookings?status=pending` - Get bookings with query parameters

### 2. POST - Create New Resources
- **Purpose**: Create new resources on the server
- **Not idempotent**: Multiple identical requests may create multiple resources
- **Not safe**: Modifies server state
- **Examples**:
  - `POST /api/customers` - Create new customer
  - `POST /api/bookings` - Create new booking

### 3. PUT - Update/Replace Resources
- **Purpose**: Update existing resource completely
- **Idempotent**: Multiple identical requests have same effect
- **Examples**:
  - `PUT /api/mechanics/123` - Update mechanic completely
  - `PUT /api/bookings/456` - Update booking completely

### 4. PATCH - Partial Update
- **Purpose**: Update part of an existing resource
- **Idempotent**: Multiple identical requests have same effect
- **Examples**:
  - `PATCH /api/mechanics/123` - Update only specific fields of mechanic

### 5. DELETE - Remove Resources
- **Purpose**: Delete a resource
- **Idempotent**: Multiple identical requests have same effect
- **Examples**:
  - `DELETE /api/bookings/456` - Delete a booking

## HTTP Status Codes

### Success Codes (2xx)
- **200 OK**: Request successful (GET, PUT, PATCH)
- **201 Created**: Resource successfully created (POST)
- **204 No Content**: Request successful but no content to return (DELETE)

### Client Error Codes (4xx)
- **400 Bad Request**: Invalid request syntax
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **409 Conflict**: Request conflicts with current server state

### Server Error Codes (5xx)
- **500 Internal Server Error**: Server encountered an error
- **503 Service Unavailable**: Server temporarily unavailable

## REST API Endpoints for Mechanic On Demand

### Authentication Endpoints
```
POST   /api/auth/register        - Register new user (customer/mechanic)
POST   /api/auth/login           - Login and get JWT token
POST   /api/auth/logout          - Logout user
```

### Customer Endpoints
```
GET    /api/customers/profile    - Get customer profile
PUT    /api/customers/profile    - Update customer profile
GET    /api/customers/bookings   - Get customer's bookings
```

### Mechanic Endpoints
```
GET    /api/mechanics/profile    - Get mechanic profile
PUT    /api/mechanics/profile    - Update mechanic profile
GET    /api/mechanics/nearby     - Get nearby mechanics
GET    /api/mechanics/bookings   - Get mechanic's bookings
PUT    /api/mechanics/availability - Update availability status
```

### Service Endpoints
```
GET    /api/services             - Get all available services
GET    /api/services/{id}        - Get specific service details
```

### Booking Endpoints
```
POST   /api/bookings             - Create new booking
GET    /api/bookings             - Get bookings (with filters)
GET    /api/bookings/{id}        - Get specific booking
PUT    /api/bookings/{id}/status - Update booking status
POST   /api/bookings/{id}/rate   - Rate a completed booking
DELETE /api/bookings/{id}        - Cancel booking
```

### Notification Endpoints
```
GET    /api/notifications        - Get user notifications
PUT    /api/notifications/{id}/read - Mark notification as read
GET    /api/notifications/unread - Get unread notifications count
```

## Request/Response Format

### Request Headers
```
Content-Type: application/json
Authorization: Bearer <jwt_token>
```

### Response Format
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    // Response data here
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

### Error Response Format
```json
{
  "success": false,
  "message": "Error description",
  "errorCode": "ERROR_CODE",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## API Security

1. **JWT Authentication**: All endpoints (except login/register) require JWT token
2. **Role-based Access**: Different permissions for customers and mechanics
3. **Input Validation**: All inputs are validated before processing
4. **CORS Configuration**: Proper CORS headers for frontend integration

## Best Practices Implemented

1. **Consistent Naming**: Resource-based URL naming
2. **Proper HTTP Methods**: Using appropriate methods for operations
3. **Status Codes**: Meaningful status codes for different scenarios
4. **Error Handling**: Comprehensive error responses
5. **Validation**: Input validation and sanitization
6. **Documentation**: Clear endpoint documentation