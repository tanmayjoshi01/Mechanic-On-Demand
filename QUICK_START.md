# ⚡ Quick Start Guide

Get the Mechanic On Demand application running in 5 minutes!

---

## 🚀 Fastest Way - Using Docker

### Prerequisites
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop)

### Steps

1. **Clone and Navigate:**
```bash
git clone <repository-url>
cd mechanic-on-demand
```

2. **Start Everything:**
```bash
docker-compose up -d
```

3. **Wait for Services to Start** (takes 1-2 minutes):
```bash
# Check if services are running
docker-compose ps
```

4. **Access the Application:**
- **Frontend:** http://localhost
- **Backend API:** http://localhost:8080/api

5. **Create Your First Account:**
- Go to http://localhost
- Click "Register"
- Fill in your details
- Select "Customer" or "Mechanic"
- Click "Get My Location" (allow browser permission)
- Submit!

6. **Test the Features:**

   **As Customer:**
   - Dashboard → "Find Nearby Mechanics"
   - Use your location to search
   - Book a mechanic
   
   **As Mechanic:**
   - Toggle availability on/off
   - Accept incoming bookings
   - Complete jobs

---

## 🛑 Stop the Application

```bash
docker-compose down
```

---

## 📱 Quick Test Without Registration

You can test the API directly:

```bash
# Register a test customer
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "phone": "1234567890",
    "role": "CUSTOMER",
    "latitude": 40.7128,
    "longitude": -74.0060
  }'
```

Response will include a JWT token - save it for API calls!

---

## 🐛 Troubleshooting

### Issue: Port already in use
```bash
# Stop existing services
docker-compose down

# Check what's using the port
lsof -i :8080  # or :80 for frontend

# Kill the process
kill -9 <PID>
```

### Issue: Services won't start
```bash
# View logs
docker-compose logs -f

# Restart everything fresh
docker-compose down -v
docker-compose up -d --build
```

### Issue: Can't get location
- Allow location permission in browser
- Or manually enter latitude/longitude:
  - New York: 40.7128, -74.0060
  - Los Angeles: 34.0522, -118.2437
  - Chicago: 41.8781, -87.6298

---

## 📚 Next Steps

1. Read the [full README](README.md) for detailed explanations
2. Check [API Testing Guide](API_TESTING_GUIDE.md) for API examples
3. Explore the code in:
   - `backend/src/main/java/com/mechanicondemand/`
   - `frontend/`

---

## 🎯 Features to Try

### Customer Journey
1. ✅ Register as customer
2. ✅ Find nearby mechanics
3. ✅ Create a booking
4. ✅ View booking status
5. ✅ Rate completed service

### Mechanic Journey
1. ✅ Register as mechanic
2. ✅ Set availability
3. ✅ Accept/reject bookings
4. ✅ Start and complete jobs
5. ✅ View job history and ratings

---

## 💻 Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost | Register new account |
| Backend API | http://localhost:8080/api | Use JWT token |
| MySQL | localhost:3306 | root / root123 |

---

## 🔑 Default Test Accounts

After starting with Docker, sample data is loaded:

**Customer:**
- Email: `john@example.com`
- Password: `password123`

**Mechanic:**
- Email: `mike@example.com`
- Password: `password123`

*(Note: These work only if you run the schema.sql with sample data)*

---

## 📞 Need Help?

- **Can't start?** → Check [Troubleshooting section](#-troubleshooting)
- **API not working?** → Check [API Testing Guide](API_TESTING_GUIDE.md)
- **Want to learn more?** → Read [full README](README.md)

---

**You're all set! 🎉**

Happy coding! 💻
