CREATE DATABASE IF NOT EXISTS smart_hostel_db;
USE smart_hostel_db;

CREATE TABLE IF NOT EXISTS admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    gender VARCHAR(20),
    branch VARCHAR(50),
    year INT,
    parent_phone VARCHAR(20),
    hostel_block VARCHAR(50),
    room_number INT,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS rooms (
    room_number INT PRIMARY KEY,
    block VARCHAR(50) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0),
    occupied_beds INT NOT NULL DEFAULT 0 CHECK (occupied_beds >= 0 AND occupied_beds <= capacity)
);

CREATE TABLE IF NOT EXISTS complaints (
    complaint_id INT PRIMARY KEY,
    student_id INT NOT NULL,
    category VARCHAR(100),
    description TEXT,
    date VARCHAR(50),
    status VARCHAR(50) DEFAULT 'Pending'
);

INSERT INTO admin (username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (
    SELECT 1 FROM admin WHERE username = 'admin'
);
