-- Seed basic data
INSERT INTO users (email, password_hash, role, full_name, phone) VALUES
('customer1@example.com', '$2a$10$7c0.3bQR8V0m1q6mQ3YpQeX0s1dQ5Y2B5FqH9o6A7z8Q9b0C1D2E2', 'CUSTOMER', 'Customer One', '111-111-1111'),
('mechanic1@example.com', '$2a$10$7c0.3bQR8V0m1q6mQ3YpQeX0s1dQ5Y2B5FqH9o6A7z8Q9b0C1D2E2', 'MECHANIC', 'Mechanic One', '222-222-2222');

INSERT INTO mechanic_profiles (id, specialties, available, hourly_rate)
SELECT id, 'engine,battery,tires', true, 50.0 FROM users WHERE email='mechanic1@example.com';
