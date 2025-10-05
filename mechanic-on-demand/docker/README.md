# 🐳 Docker Configuration for Mechanic On Demand

This directory contains Docker configuration files for containerizing the Mechanic On Demand application.

## 📁 Files Overview

- `Dockerfile.backend` - Multi-stage Dockerfile for Spring Boot backend
- `Dockerfile.frontend` - Dockerfile for frontend (Nginx)
- `nginx.conf` - Nginx configuration for frontend
- `docker-compose.yml` - Production Docker Compose configuration
- `docker-compose.dev.yml` - Development Docker Compose configuration

## 🚀 Quick Start

### Production Environment

1. **Start all services:**
   ```bash
   docker-compose up -d
   ```

2. **View logs:**
   ```bash
   docker-compose logs -f
   ```

3. **Stop services:**
   ```bash
   docker-compose down
   ```

### Development Environment

1. **Start development services:**
   ```bash
   docker-compose -f docker-compose.dev.yml up -d
   ```

2. **Run backend locally:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **Open frontend:**
   ```bash
   # Serve frontend files using any static server
   cd frontend
   python -m http.server 3000
   ```

## 🏗️ Architecture

### Production Stack
- **Frontend**: Nginx (Port 80)
- **Backend**: Spring Boot (Port 8080)
- **Database**: MySQL 8.0 (Port 3306)
- **Cache**: Redis (Port 6379)
- **Admin**: phpMyAdmin (Port 8081)

### Development Stack
- **Database**: MySQL 8.0 (Port 3307)
- **Cache**: Redis (Port 6380)
- **Admin**: phpMyAdmin (Port 8082)

## 🔧 Configuration

### Environment Variables

#### Backend
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret
- `JWT_EXPIRATION`: JWT token expiration time

#### Database
- `MYSQL_ROOT_PASSWORD`: MySQL root password
- `MYSQL_DATABASE`: Database name
- `MYSQL_USER`: Database user
- `MYSQL_PASSWORD`: Database password

## 📊 Services

### 1. MySQL Database
- **Image**: mysql:8.0
- **Port**: 3306 (production), 3307 (development)
- **Data**: Persistent volume
- **Health Check**: MySQL ping

### 2. Spring Boot Backend
- **Build**: Multi-stage Docker build
- **Port**: 8080
- **Dependencies**: MySQL
- **Health Check**: HTTP endpoint

### 3. Frontend (Nginx)
- **Build**: Custom Nginx image
- **Port**: 80
- **Features**: Gzip compression, security headers, API proxy
- **Health Check**: HTTP endpoint

### 4. Redis Cache
- **Image**: redis:7-alpine
- **Port**: 6379 (production), 6380 (development)
- **Data**: Persistent volume
- **Health Check**: Redis ping

### 5. phpMyAdmin
- **Image**: phpmyadmin/phpmyadmin
- **Port**: 8081 (production), 8082 (development)
- **Purpose**: Database management interface

## 🛠️ Development Workflow

### 1. Backend Development
```bash
# Start database and cache
docker-compose -f docker-compose.dev.yml up -d

# Run backend locally
cd backend
mvn spring-boot:run

# Access database
# URL: http://localhost:8082
# Username: root
# Password: password
```

### 2. Frontend Development
```bash
# Serve frontend files
cd frontend
python -m http.server 3000

# Access frontend
# URL: http://localhost:3000
```

### 3. Full Stack Development
```bash
# Start all services
docker-compose up -d

# Access application
# Frontend: http://localhost
# Backend API: http://localhost:8080/api
# Database Admin: http://localhost:8081
```

## 🔍 Monitoring and Debugging

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### Access Containers
```bash
# Backend container
docker exec -it mechanic_backend bash

# MySQL container
docker exec -it mechanic_mysql mysql -u root -p

# Redis container
docker exec -it mechanic_redis redis-cli
```

### Health Checks
```bash
# Check service health
docker-compose ps

# Check specific service
docker inspect mechanic_backend | grep Health
```

## 🚨 Troubleshooting

### Common Issues

1. **Port conflicts:**
   ```bash
   # Check port usage
   netstat -tulpn | grep :8080
   
   # Kill process using port
   sudo kill -9 <PID>
   ```

2. **Database connection issues:**
   ```bash
   # Check MySQL logs
   docker-compose logs mysql
   
   # Restart MySQL
   docker-compose restart mysql
   ```

3. **Backend startup issues:**
   ```bash
   # Check backend logs
   docker-compose logs backend
   
   # Check database connectivity
   docker exec -it mechanic_mysql mysql -u root -p -e "SHOW DATABASES;"
   ```

4. **Frontend not loading:**
   ```bash
   # Check nginx logs
   docker-compose logs frontend
   
   # Check nginx configuration
   docker exec -it mechanic_frontend nginx -t
   ```

### Reset Everything
```bash
# Stop and remove all containers
docker-compose down -v

# Remove all images
docker-compose down --rmi all

# Remove all volumes
docker volume prune

# Start fresh
docker-compose up -d --build
```

## 📈 Performance Optimization

### Production Optimizations
- Enable Nginx gzip compression
- Use Redis for session storage
- Configure MySQL for production
- Set up monitoring and logging
- Use Docker secrets for sensitive data

### Resource Limits
```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          memory: 512M
          cpus: '0.5'
        reservations:
          memory: 256M
          cpus: '0.25'
```

## 🔒 Security Considerations

1. **Change default passwords**
2. **Use Docker secrets for sensitive data**
3. **Enable SSL/TLS in production**
4. **Configure firewall rules**
5. **Regular security updates**
6. **Monitor container logs**

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Nginx Configuration Guide](https://nginx.org/en/docs/)