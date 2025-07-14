-- Create users table with authentication fields
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Clear existing data first
DELETE FROM users;

-- Insert sample data with encrypted passwords
-- Password for 'admin' is 'password' (BCrypt encrypted)
-- Password for 'user' is 'user123' (BCrypt encrypted)
-- Password for others is 'password123' (BCrypt encrypted)
INSERT INTO users (username, password, first_name, last_name, email, phone, role, enabled, created_at, updated_at) VALUES
('admin', '$2a$10$Mq3bLJ6Ac8OS53DPQOaVFuYXy0C8g9lC6S6XxD.9NGPhPE0dfHzVu', 'Admin', 'User', 'admin@example.com', '+1-555-0001', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VfPiYxeGO4fqprhLbWGOmLWpHluu4O', 'Regular', 'User', 'user@example.com', '+1-555-0002', 'USER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('john.doe', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi6', 'John', 'Doe', 'john.doe@example.com', '+1-555-0101', 'USER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane.smith', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi6', 'Jane', 'Smith', 'jane.smith@example.com', '+1-555-0102', 'USER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('bob.johnson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi6', 'Bob', 'Johnson', 'bob.johnson@example.com', '+1-555-0103', 'USER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);