-- =====================================================
-- Mechanic On Demand - Database Schema
-- =====================================================
-- This SQL file creates the database structure manually
-- (Note: Spring Boot JPA can auto-create tables, but this is for reference)

-- Create Database
CREATE DATABASE IF NOT EXISTS mechanic_on_demand;
USE mechanic_on_demand;

-- =====================================================
-- CUSTOMERS TABLE
-- Stores customer information
-- =====================================================
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed password
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    latitude DOUBLE,                 -- GPS coordinates for location
    longitude DOUBLE,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- MECHANICS TABLE
-- Stores mechanic information
-- =====================================================
CREATE TABLE IF NOT EXISTS mechanics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    specialty VARCHAR(50),           -- e.g., 'Car', 'Bike', 'Truck', 'All'
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    available BOOLEAN NOT NULL DEFAULT TRUE,  -- Availability status
    rating DOUBLE DEFAULT 0.0,       -- Average rating (0-5 stars)
    total_jobs INT DEFAULT 0,        -- Total completed jobs
    role VARCHAR(20) NOT NULL DEFAULT 'MECHANIC',
    hourly_rate DOUBLE DEFAULT 50.0,
    monthly_subscription DOUBLE DEFAULT 999.0,
    yearly_subscription DOUBLE DEFAULT 9999.0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_available (available),
    INDEX idx_location (latitude, longitude),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- BOOKINGS TABLE
-- Stores booking/service request information
-- Links customers with mechanics
-- =====================================================
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,     -- 'Car', 'Bike', 'Truck', etc.
    vehicle_model VARCHAR(100) NOT NULL,
    problem_description TEXT NOT NULL,
    location VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- PENDING, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED, CANCELLED
    subscription_type VARCHAR(20) NOT NULL,         -- HOURLY, MONTHLY, YEARLY
    price DOUBLE NOT NULL,
    scheduled_time TIMESTAMP,
    completed_time TIMESTAMP NULL,
    rating INT,                            -- Customer rating after completion (1-5 stars)
    feedback TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Keys
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    
    -- Indexes for faster queries
    INDEX idx_customer (customer_id),
    INDEX idx_mechanic (mechanic_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Sample Data for Testing
-- =====================================================

-- Insert sample customers
-- Password: 'password123' hashed with BCrypt
INSERT INTO customers (name, email, password, phone, address, latitude, longitude) VALUES
('John Doe', 'john@example.com', '$2a$10$xgZv3qj9K3YvZQq3XvBZXe9Q0OmZ9mYKJ4vXJZ1X5JZ9X5YvZQq3X', '1234567890', '123 Main St, City', 40.7128, -74.0060),
('Jane Smith', 'jane@example.com', '$2a$10$xgZv3qj9K3YvZQq3XvBZXe9Q0OmZ9mYKJ4vXJZ1X5JZ9X5YvZQq3X', '0987654321', '456 Oak Ave, City', 40.7580, -73.9855);

-- Insert sample mechanics
INSERT INTO mechanics (name, email, password, phone, specialty, address, latitude, longitude, available, hourly_rate, monthly_subscription, yearly_subscription) VALUES
('Mike Mechanic', 'mike@example.com', '$2a$10$xgZv3qj9K3YvZQq3XvBZXe9Q0OmZ9mYKJ4vXJZ1X5JZ9X5YvZQq3X', '1112223333', 'Car', '789 Elm St, City', 40.7489, -73.9680, TRUE, 60.0, 1200.0, 12000.0),
('Sarah Service', 'sarah@example.com', '$2a$10$xgZv3qj9K3YvZQq3XvBZXe9Q0OmZ9mYKJ4vXJZ1X5JZ9X5YvZQq3X', '4445556666', 'Bike', '321 Pine Rd, City', 40.7306, -73.9352, TRUE, 50.0, 1000.0, 10000.0),
('Tom Technician', 'tom@example.com', '$2a$10$xgZv3qj9K3YvZQq3XvBZXe9Q0OmZ9mYKJ4vXJZ1X5JZ9X5YvZQq3X', '7778889999', 'All', '654 Maple Dr, City', 40.7589, -73.9851, TRUE, 55.0, 1100.0, 11000.0);

-- =====================================================
-- Useful Queries for Testing
-- =====================================================

-- Get all available mechanics
-- SELECT * FROM mechanics WHERE available = TRUE;

-- Find mechanics near a location (within 10km radius)
-- Uses Haversine formula to calculate distance
-- SELECT *, 
--   (6371 * acos(cos(radians(40.7128)) * cos(radians(latitude)) * 
--   cos(radians(longitude) - radians(-74.0060)) + 
--   sin(radians(40.7128)) * sin(radians(latitude)))) AS distance 
-- FROM mechanics 
-- WHERE available = TRUE 
-- HAVING distance <= 10 
-- ORDER BY distance;

-- Get all bookings with customer and mechanic details
-- SELECT b.*, c.name as customer_name, m.name as mechanic_name 
-- FROM bookings b 
-- JOIN customers c ON b.customer_id = c.id 
-- JOIN mechanics m ON b.mechanic_id = m.id;

-- Get mechanic's rating and total jobs
-- SELECT m.name, m.rating, m.total_jobs, COUNT(b.id) as completed_bookings 
-- FROM mechanics m 
-- LEFT JOIN bookings b ON m.id = b.mechanic_id AND b.status = 'COMPLETED' 
-- GROUP BY m.id;
