CREATE DATABASE IF NOT EXISTS sarms;
USE sarms;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    roll_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    semester INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    course_code VARCHAR(20) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    department VARCHAR(50) NOT NULL,
    syllabus TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    roll_number VARCHAR(20),
    course_code VARCHAR(20),
    semester INT NOT NULL,
    marks DOUBLE,
    PRIMARY KEY (roll_number, course_code, semester),
    FOREIGN KEY (roll_number) REFERENCES students(roll_number) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE
);

-- Semester Records table
CREATE TABLE IF NOT EXISTS semester_records (
    roll_number VARCHAR(20),
    semester INT,
    swa DOUBLE,
    on_vc_list BOOLEAN DEFAULT FALSE,
    on_conditional_standing BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (roll_number, semester),
    FOREIGN KEY (roll_number) REFERENCES students(roll_number) ON DELETE CASCADE
);