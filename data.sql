CREATE DATABASE IF NOT EXISTS dormitory_management;
USE dormitory_management;

CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT,
    user_id INT UNIQUE,

    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    student_code VARCHAR(20) UNIQUE,
    class_name VARCHAR(50),
    faculty VARCHAR(100),
    citizen_id VARCHAR(20) UNIQUE,
    phone_number VARCHAR(15) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE dormitories (
    dormitory_id INT AUTO_INCREMENT PRIMARY KEY,
    dormitory_name VARCHAR(100),
    address VARCHAR(255),
    description TEXT
);

CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    dormitory_id INT,
    room_number VARCHAR(20),
    capacity INT,
    current_occupancy INT DEFAULT 0,
    room_price DECIMAL(10,2),
    description TEXT,
    FOREIGN KEY (dormitory_id) REFERENCES dormitories(dormitory_id)
);

CREATE TABLE room_bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    room_id INT,
    booking_date DATE,
    check_in_date DATE,
    check_out_date DATE,
    status ENUM('pending','approved','rejected') DEFAULT 'pending',
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT,
    payment_month INT NOT NULL,
    payment_year INT NOT NULL,
    amount DECIMAL(10,2),
    payment_method VARCHAR(50),
    payment_status ENUM('unpaid','paid') DEFAULT 'unpaid',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES room_bookings(booking_id)
);

CREATE TABLE chatbot_logs (
    chat_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    question TEXT,
    answer TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE access_statistics (
    statistic_id INT AUTO_INCREMENT PRIMARY KEY,
    access_date DATE,
    visit_count INT
);