-- Mechanic On Demand Database Schema
-- This script creates all necessary tables for the application

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS mechanic_on_demand_db;
USE mechanic_on_demand_db;

-- Users table (base table for customers and mechanics)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    user_type ENUM('CUSTOMER', 'MECHANIC') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT PRIMARY KEY,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Mechanics table
CREATE TABLE IF NOT EXISTS mechanics (
    id BIGINT PRIMARY KEY,
    experience_years INT DEFAULT 0,
    rating DECIMAL(3, 2) DEFAULT 0.0,
    total_jobs_completed INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    service_radius_km DECIMAL(5, 2) DEFAULT 10.0,
    current_latitude DECIMAL(10, 8),
    current_longitude DECIMAL(11, 8),
    license_number VARCHAR(100),
    specializations TEXT, -- JSON array of specializations
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Services table
CREATE TABLE IF NOT EXISTS services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10, 2) NOT NULL,
    hourly_rate DECIMAL(10, 2),
    estimated_duration_hours DECIMAL(4, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Mechanic Services junction table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS mechanic_services (
    mechanic_id BIGINT,
    service_id BIGINT,
    custom_price DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (mechanic_id, service_id),
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    scheduled_datetime DATETIME NOT NULL,
    estimated_duration_hours DECIMAL(4, 2),
    total_price DECIMAL(10, 2),
    customer_address TEXT,
    customer_latitude DECIMAL(10, 8),
    customer_longitude DECIMAL(11, 8),
    mechanic_notes TEXT,
    customer_rating INT CHECK (customer_rating >= 1 AND customer_rating <= 5),
    mechanic_rating INT CHECK (mechanic_rating >= 1 AND mechanic_rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    notification_type ENUM('BOOKING_REQUEST', 'BOOKING_CONFIRMED', 'BOOKING_CANCELLED', 'MECHANIC_ARRIVED', 'JOB_COMPLETED') NOT NULL,
    related_booking_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (related_booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

-- Reviews table
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    reviewee_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewee_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample data for development
-- Sample services
INSERT INTO services (name, description, base_price, hourly_rate, estimated_duration_hours) VALUES
('Engine Repair', 'Complete engine diagnostic and repair services', 150.00, 75.00, 2.0),
('Brake Service', 'Brake pad replacement and brake system maintenance', 80.00, 40.00, 1.5),
('Oil Change', 'Complete oil change with filter replacement', 50.00, NULL, 0.5),
('Battery Replacement', 'Car battery testing and replacement', 100.00, 30.00, 0.75),
('Tire Service', 'Tire rotation, balancing, and replacement', 60.00, 35.00, 1.0);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_mechanics_location ON mechanics(current_latitude, current_longitude);
CREATE INDEX idx_mechanics_available ON mechanics(is_available);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_mechanic ON bookings(mechanic_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_datetime ON bookings(scheduled_datetime);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_unread ON notifications(is_read);

-- Create a view for active mechanics with their services
CREATE OR REPLACE VIEW active_mechanics_with_services AS
SELECT
    m.id,
    u.first_name,
    u.last_name,
    u.phone_number,
    m.experience_years,
    m.rating,
    m.total_jobs_completed,
    m.is_available,
    m.service_radius_km,
    m.current_latitude,
    m.current_longitude,
    GROUP_CONCAT(s.name SEPARATOR ', ') as services,
    GROUP_CONCAT(ms.custom_price SEPARATOR ', ') as service_prices
FROM mechanics m
JOIN users u ON m.id = u.id
LEFT JOIN mechanic_services ms ON m.id = ms.mechanic_id
LEFT JOIN services s ON ms.service_id = s.id
WHERE u.is_active = TRUE AND m.is_available = TRUE
GROUP BY m.id, u.first_name, u.last_name, u.phone_number, m.experience_years,
         m.rating, m.total_jobs_completed, m.is_available, m.service_radius_km,
         m.current_latitude, m.current_longitude;