-- Database Schema for Mechanic On Demand Application
-- This script creates all necessary tables for the application

-- Create database (run this first)
CREATE DATABASE IF NOT EXISTS mechanic_on_demand;
USE mechanic_on_demand;

-- Users table - Stores both customers and mechanics
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('CUSTOMER', 'MECHANIC') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Customer profiles - Additional info for customers
CREATE TABLE customer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    vehicle_info JSON, -- Store vehicle details as JSON
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Mechanic profiles - Additional info for mechanics
CREATE TABLE mechanic_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    specialization VARCHAR(100),
    experience_years INT,
    hourly_rate DECIMAL(10,2),
    service_radius INT DEFAULT 10, -- Service radius in kilometers
    rating DECIMAL(3,2) DEFAULT 0.0,
    total_ratings INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    current_latitude DECIMAL(10,8),
    current_longitude DECIMAL(11,8),
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Service categories - Types of services offered
CREATE TABLE service_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Mechanic services - Services offered by each mechanic
CREATE TABLE mechanic_services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mechanic_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2),
    estimated_duration INT, -- Duration in minutes
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (mechanic_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES service_categories(id) ON DELETE CASCADE
);

-- Bookings - Main booking table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIME NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(10,2),
    address TEXT NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    problem_description TEXT,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES mechanic_services(id) ON DELETE CASCADE
);

-- Reviews - Customer reviews for mechanics
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Notifications - System notifications
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('BOOKING', 'PAYMENT', 'REVIEW', 'SYSTEM') NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Pricing plans - Monthly and yearly plans
CREATE TABLE pricing_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    features JSON, -- Store plan features as JSON
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customer subscriptions - Customer subscription to plans
CREATE TABLE customer_subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES pricing_plans(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO service_categories (name, description) VALUES
('Engine Repair', 'Engine diagnostics and repair services'),
('Brake Service', 'Brake system inspection and repair'),
('Tire Service', 'Tire replacement and repair'),
('Electrical', 'Electrical system diagnostics and repair'),
('AC Service', 'Air conditioning system service'),
('General Maintenance', 'Regular vehicle maintenance');

INSERT INTO pricing_plans (name, description, price, duration_months, features) VALUES
('Basic Plan', 'Basic mechanic services', 29.99, 1, '["5 bookings per month", "Basic support", "Standard response time"]'),
('Premium Plan', 'Premium mechanic services', 49.99, 1, '["Unlimited bookings", "Priority support", "Fast response time", "Emergency service"]'),
('Annual Basic', 'Basic plan for 12 months', 299.99, 12, '["5 bookings per month", "Basic support", "Standard response time", "10% discount"]'),
('Annual Premium', 'Premium plan for 12 months', 499.99, 12, '["Unlimited bookings", "Priority support", "Fast response time", "Emergency service", "20% discount"]');