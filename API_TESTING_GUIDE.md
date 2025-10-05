# 🧪 API Testing Guide

Complete guide for testing all REST API endpoints using curl, Postman, or browser.

---

## 📋 Prerequisites

- Backend running on `http://localhost:8080/api`
- Tools: curl (command line) or Postman (GUI)

---

## 🔐 Authentication Flow

### Step 1: Register a Customer

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Customer",
    "email": "john@customer.com",
    "password": "password123",
    "phone": "1234567890",
    "address": "123 Main St, New York",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "role": "CUSTOMER"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "name": "John Customer",
    "email": "john@customer.com",
    "role": "CUSTOMER"
  }
}
```

**Save the token!** You'll need it for authenticated requests.

### Step 2: Register a Mechanic

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mike Mechanic",
    "email": "mike@mechanic.com",
    "password": "password123",
    "phone": "9876543210",
    "address": "456 Service Ave, New York",
    "latitude": 40.7589,
    "longitude": -73.9851,
    "role": "MECHANIC",
    "specialty": "Car",
    "hourlyRate": 60.0,
    "monthlySubscription": 1200.0,
    "yearlySubscription": 12000.0
  }'
```

### Step 3: Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@customer.com",
    "password": "password123"
  }'
```

---

## 👨‍🔧 Mechanic Endpoints

### 1. Get All Mechanics

```bash
curl -X GET http://localhost:8080/api/mechanics \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2. Get Specific Mechanic

```bash
curl -X GET http://localhost:8080/api/mechanics/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 3. Get Available Mechanics

```bash
curl -X GET http://localhost:8080/api/mechanics/available \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 4. Find Nearby Mechanics

```bash
curl -X GET "http://localhost:8080/api/mechanics/nearby?latitude=40.7128&longitude=-74.0060&radius=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Parameters:**
- `latitude`: Your current latitude (required)
- `longitude`: Your current longitude (required)
- `radius`: Search radius in kilometers (default: 10)

### 5. Update Mechanic Availability

```bash
# Set available to true
curl -X PUT "http://localhost:8080/api/mechanics/2/availability?available=true" \
  -H "Authorization: Bearer MECHANIC_TOKEN"

# Set available to false
curl -X PUT "http://localhost:8080/api/mechanics/2/availability?available=false" \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 6. Update Mechanic Profile

```bash
curl -X PUT http://localhost:8080/api/mechanics/2 \
  -H "Authorization: Bearer MECHANIC_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "1112223333",
    "address": "789 New Address",
    "specialty": "All",
    "hourlyRate": 75.0
  }'
```

---

## 📅 Booking Endpoints

### 1. Create a Booking

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer CUSTOMER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "mechanicId": 2,
    "vehicleType": "Car",
    "vehicleModel": "Toyota Camry 2020",
    "problemDescription": "Engine making strange noise, possible timing belt issue",
    "location": "123 Main St, New York",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "subscriptionType": "HOURLY",
    "scheduledTime": "2024-10-05T14:30:00"
  }'
```

**Subscription Types:**
- `HOURLY`: Pay per hour
- `MONTHLY`: Monthly subscription
- `YEARLY`: Yearly subscription

### 2. Get All Bookings (Admin)

```bash
curl -X GET http://localhost:8080/api/bookings \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3. Get Booking by ID

```bash
curl -X GET http://localhost:8080/api/bookings/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4. Get Customer's Bookings

```bash
curl -X GET http://localhost:8080/api/bookings/customer/1 \
  -H "Authorization: Bearer CUSTOMER_TOKEN"
```

### 5. Get Mechanic's Bookings

```bash
curl -X GET http://localhost:8080/api/bookings/mechanic/2 \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 6. Accept Booking (Mechanic)

```bash
curl -X PUT http://localhost:8080/api/bookings/1/accept \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 7. Reject Booking (Mechanic)

```bash
curl -X PUT http://localhost:8080/api/bookings/1/reject \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 8. Start Booking (Mechanic)

```bash
curl -X PUT http://localhost:8080/api/bookings/1/start \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 9. Complete Booking (Mechanic)

```bash
curl -X PUT http://localhost:8080/api/bookings/1/complete \
  -H "Authorization: Bearer MECHANIC_TOKEN"
```

### 10. Cancel Booking (Customer)

```bash
curl -X PUT http://localhost:8080/api/bookings/1/cancel \
  -H "Authorization: Bearer CUSTOMER_TOKEN"
```

### 11. Rate Booking (Customer)

```bash
curl -X PUT "http://localhost:8080/api/bookings/1/rate?rating=5&feedback=Excellent%20service!" \
  -H "Authorization: Bearer CUSTOMER_TOKEN"
```

**Parameters:**
- `rating`: 1-5 stars (required)
- `feedback`: Text feedback (optional)

---

## 🔄 Complete Workflow Test

### Scenario: Customer books a mechanic

```bash
# 1. Register Customer
CUSTOMER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "test@customer.com",
    "password": "pass123",
    "phone": "1234567890",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "role": "CUSTOMER"
  }')

# Extract customer token and ID
CUSTOMER_TOKEN=$(echo $CUSTOMER_RESPONSE | jq -r '.data.token')
CUSTOMER_ID=$(echo $CUSTOMER_RESPONSE | jq -r '.data.id')

# 2. Register Mechanic
MECHANIC_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Mechanic",
    "email": "test@mechanic.com",
    "password": "pass123",
    "phone": "9876543210",
    "latitude": 40.7489,
    "longitude": -73.9680,
    "role": "MECHANIC",
    "specialty": "Car",
    "hourlyRate": 60.0
  }')

MECHANIC_TOKEN=$(echo $MECHANIC_RESPONSE | jq -r '.data.token')
MECHANIC_ID=$(echo $MECHANIC_RESPONSE | jq -r '.data.id')

# 3. Customer finds nearby mechanics
curl -X GET "http://localhost:8080/api/mechanics/nearby?latitude=40.7128&longitude=-74.0060&radius=10" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"

# 4. Customer creates booking
BOOKING_RESPONSE=$(curl -s -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer $CUSTOMER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"customerId\": $CUSTOMER_ID,
    \"mechanicId\": $MECHANIC_ID,
    \"vehicleType\": \"Car\",
    \"vehicleModel\": \"Honda Civic\",
    \"problemDescription\": \"Brake pads need replacement\",
    \"subscriptionType\": \"HOURLY\",
    \"latitude\": 40.7128,
    \"longitude\": -74.0060
  }")

BOOKING_ID=$(echo $BOOKING_RESPONSE | jq -r '.data.id')

# 5. Mechanic accepts booking
curl -X PUT http://localhost:8080/api/bookings/$BOOKING_ID/accept \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# 6. Mechanic starts work
curl -X PUT http://localhost:8080/api/bookings/$BOOKING_ID/start \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# 7. Mechanic completes work
curl -X PUT http://localhost:8080/api/bookings/$BOOKING_ID/complete \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# 8. Customer rates the service
curl -X PUT "http://localhost:8080/api/bookings/$BOOKING_ID/rate?rating=5&feedback=Great%20service!" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"
```

---

## 🎨 Testing with Postman

### Setup Postman Collection

1. **Create New Collection**: "Mechanic On Demand API"

2. **Set Environment Variables:**
   - `base_url`: `http://localhost:8080/api`
   - `customer_token`: (will be set after login)
   - `mechanic_token`: (will be set after login)

3. **Import Requests:**

#### Folder 1: Authentication
- Register Customer (POST)
- Register Mechanic (POST)
- Login (POST)

#### Folder 2: Mechanics
- Get All Mechanics (GET)
- Get Mechanic by ID (GET)
- Find Nearby Mechanics (GET)
- Update Availability (PUT)

#### Folder 3: Bookings
- Create Booking (POST)
- Get Customer Bookings (GET)
- Get Mechanic Bookings (GET)
- Accept Booking (PUT)
- Start Booking (PUT)
- Complete Booking (PUT)
- Cancel Booking (PUT)
- Rate Booking (PUT)

### Postman Request Example

**Get Nearby Mechanics:**

```
Method: GET
URL: {{base_url}}/mechanics/nearby?latitude=40.7128&longitude=-74.0060&radius=10

Headers:
- Authorization: Bearer {{customer_token}}

Tests (JavaScript):
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has mechanics data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.be.true;
    pm.expect(jsonData.data).to.be.an('array');
});
```

---

## 📊 Response Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | Successful GET, PUT |
| 201 | Created | Successful POST |
| 400 | Bad Request | Invalid input data |
| 401 | Unauthorized | Missing or invalid token |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Internal server error |

---

## 🐛 Common Errors

### 1. 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required"
}
```
**Solution:** Include valid JWT token in Authorization header

### 2. 400 Bad Request
```json
{
  "success": false,
  "message": "Email already exists"
}
```
**Solution:** Use unique email or check input validation

### 3. 404 Not Found
```json
{
  "success": false,
  "message": "Mechanic not found"
}
```
**Solution:** Verify resource ID exists

---

## 💡 Tips

1. **Save Tokens:** After login/register, save the token for subsequent requests
2. **Check Headers:** Always include `Content-Type: application/json` for POST/PUT
3. **URL Encoding:** Encode special characters in query parameters
4. **Test Order:** Follow workflow order (register → login → create booking → etc.)
5. **Fresh State:** Use different email addresses for each test run

---

## 📝 Sample Test Data

### Customer Data
```json
{
  "name": "Alice Johnson",
  "email": "alice@test.com",
  "password": "secure123",
  "phone": "5551234567",
  "address": "100 Broadway, NYC",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "role": "CUSTOMER"
}
```

### Mechanic Data
```json
{
  "name": "Bob Technician",
  "email": "bob@test.com",
  "password": "secure123",
  "phone": "5559876543",
  "address": "200 Service Blvd, NYC",
  "latitude": 40.7589,
  "longitude": -73.9851,
  "role": "MECHANIC",
  "specialty": "Car",
  "hourlyRate": 55.0
}
```

### Booking Data
```json
{
  "customerId": 1,
  "mechanicId": 2,
  "vehicleType": "Car",
  "vehicleModel": "Tesla Model 3",
  "problemDescription": "Battery charging issue",
  "subscriptionType": "HOURLY",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

---

**Happy Testing! 🚀**
