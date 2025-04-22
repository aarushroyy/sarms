# Student Academic Record Management System (SARMS)

A desktop application for managing student academic records, course enrollments, and grade calculations.

## Overview

SARMS is a Java-based desktop application that helps educational institutions manage student records, course enrollments, and academic performance tracking. It provides different interfaces for students, faculty, and administrators.

## Features

- **User Authentication**: Secure login for students, faculty, and administrators
- **Student Management**: Add, update, and remove students
- **Course Management**: Create, update, and delete courses 
- **Enrollment System**: Enroll students in courses and drop courses
- **Marks Entry**: Faculty can enter and update student marks
- **SWA Calculation**: Automatically calculate Semester Weighted Average (SWA)
- **Academic Standing**: Categorize students based on performance (Vice Chancellor's list, Conditional Standing)
- **Reports**: Generate semester reports for students

## Technology Stack

- **Language**: Java 11
- **Build Tool**: Maven
- **Database**: MySQL
- **UI Framework**: Java Swing
- **Testing**: JUnit 5, Mockito, AssertJ Swing

## System Requirements

- Java 11 or higher
- MySQL 5.7 or higher
- Maven 3.6 or higher

## Setup and Installation

### 1. Database Setup

1. Create a MySQL database named `sarms`:
   ```sql
   CREATE DATABASE IF NOT EXISTS sarms;
   ```

2. Configure database connection in `src/main/resources/config/database.properties`:
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/sarms?useSSL=false&serverTimezone=UTC
   db.username=your_username
   db.password=your_password
   ```

3. Run the database schema script:
   ```sql
   -- Run the contents of src/main/resources/sql/schema.sql
   ```

4. If needed, alter the tables to add password_salt columns:
   ```sql
   ALTER TABLE users ADD COLUMN IF NOT EXISTS password_salt VARCHAR(255) NOT NULL DEFAULT '';
   ALTER TABLE students ADD COLUMN IF NOT EXISTS password_salt VARCHAR(255) NOT NULL DEFAULT '';
   ```

5. (Optional) Insert test data:
   ```sql
   -- See test-data.sql for sample data
   ```

### 2. Build the Application

```bash
# Clone the repository
git clone https://github.com/yourusername/sarms.git
cd sarms

# Build the application
mvn clean package
```

### 3. Run the Application

```bash
java -jar target/sarms-1.0-SNAPSHOT.jar
```

## Usage Guide

### Login

- **Admin**: Username: admin1, Password: admin123
- **Faculty**: Username: faculty1, Password: faculty123
- **Student**: Username: S001, Password: student123

### Admin Dashboard

- **Manage Students**: View, add, edit, and delete student records
- **Manage Courses**: Create new courses, update existing ones, delete courses
- **Reports**: View students on Vice Chancellor's list and Conditional Standing

### Faculty Dashboard

- **Enter Marks**: Enter or update student marks for courses
- **View Students**: Browse students by semester

### Student Dashboard

- **My Enrollments**: View enrolled courses and marks
- **Available Courses**: Browse and enroll in available courses
- **Reports**: Generate and view semester reports with SWA and academic standing

## Testing

### Running Unit Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=GradingServiceTest

# Run a specific test method
mvn test -Dtest=GradingServiceTest#calculateSWA_WithValidMarks_ReturnsCorrectAverage
```

### Running UI Tests

```bash
# Requires AssertJ Swing dependency in pom.xml
mvn test -Dtest=LoginUITest
```

## Project Structure

```
sarms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sarms/
│   │   │           ├── controller/     # Application controllers
│   │   │           ├── dao/            # Data Access Objects and implementations
│   │   │           ├── model/          # Domain model classes
│   │   │           ├── service/        # Business logic services
│   │   │           ├── ui/             # User interface components
│   │   │           ├── util/           # Utility classes
│   │   │           └── Main.java       # Application entry point
│   │   └── resources/
│   │       ├── config/                 # Configuration files
│   │       └── sql/                    # SQL scripts
│   └── test/
│       └── java/
│           └── com/
│               └── sarms/
│                   ├── service/        # Service tests
│                   ├── integration/    # Integration tests 
│                   └── ui/             # UI tests
└── pom.xml                            # Maven configuration
```

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check if MySQL is running
   - Verify database.properties has correct credentials
   - Ensure the sarms database exists

2. **Login Issues**
   - Verify user credentials in the database
   - Check console for authentication errors

3. **Build Failures**
   - Ensure Java 11 is installed and configured
   - Make sure Maven is correctly installed

## Code Explanation for Non-Programmers

- **Models**: These are like digital forms that hold information (Student, Course, etc.)
- **DAOs**: These connect to the database to save and retrieve information
- **Services**: These contain the business rules (like calculating grades)
- **Controllers**: These coordinate between the user interface and services
- **UI**: These are the screens users see and interact with

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Contributors

- Your Name
- Other Contributors