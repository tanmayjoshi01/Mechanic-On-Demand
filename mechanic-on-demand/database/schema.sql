-- 🔧 Mechanic On Demand Database Schema
-- This file contains the SQL commands to create our database structure

-- Create Database
CREATE DATABASE IF NOT EXISTS mechanic_ondemand;
USE mechanic_ondemand;

-- 👤 Customers Table
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 🔧 Mechanics Table
CREATE TABLE mechanics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    specialization VARCHAR(100),
    experience_years INT DEFAULT 0,
    hourly_rate DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    rating DECIMAL(3, 2) DEFAULT 0.0,
    total_ratings INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 📅 Bookings Table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    service_type VARCHAR(100) NOT NULL,
    description TEXT,
    vehicle_info VARCHAR(200),
    booking_date TIMESTAMP NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    estimated_duration INT, -- in minutes
    actual_duration INT, -- in minutes
    total_cost DECIMAL(10, 2),
    customer_rating INT CHECK (customer_rating >= 1 AND customer_rating <= 5),
    customer_feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE
);

-- 💰 Pricing Table
CREATE TABLE pricing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_type VARCHAR(100) NOT NULL,
    monthly_price DECIMAL(10, 2) NOT NULL,
    yearly_price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert Sample Pricing Data
INSERT INTO pricing (service_type, monthly_price, yearly_price, description) VALUES
('Basic Maintenance', 29.99, 299.99, 'Oil changes, tire rotation, basic checks'),
('Engine Repair', 49.99, 499.99, 'Engine diagnostics and repairs'),
('Electrical System', 39.99, 399.99, 'Battery, alternator, electrical issues'),
('Brake Service', 34.99, 349.99, 'Brake pad replacement, brake fluid'),
('Transmission', 59.99, 599.99, 'Transmission service and repair'),
('Emergency Service', 79.99, 799.99, '24/7 emergency roadside assistance');

-- Insert Sample Data
INSERT INTO customers (first_name, last_name, email, password, phone, address, latitude, longitude) VALUES
('John', 'Doe', 'john.doe@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '555-0101', '123 Main St, City, State', 40.7128, -74.0060),
('Jane', 'Smith', 'jane.smith@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '555-0102', '456 Oak Ave, City, State', 40.7589, -73.9851);

INSERT INTO mechanics (first_name, last_name, email, password, phone, address, latitude, longitude, specialization, experience_years, hourly_rate, rating, total_ratings) VALUES
('Mike', 'Johnson', 'mike.johnson@mechanic.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '555-0201', '789 Service St, City, State', 40.7505, -73.9934, 'Engine Specialist', 8, 75.00, 4.8, 25),
('Sarah', 'Wilson', 'sarah.wilson@mechanic.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '555-0202', '321 Repair Blvd, City, State', 40.7614, -73.9776, 'Electrical Systems', 5, 65.00, 4.6, 18),
('Tom', 'Brown', 'tom.brown@mechanic.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '555-0203', '654 Fix Lane, City, State', 40.7282, -73.7949, 'Brake Specialist', 12, 80.00, 4.9, 42);