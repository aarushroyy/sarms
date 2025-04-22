// src/main/java/com/sarms/dao/impl/StudentDAOImpl.java
package com.sarms.dao.impl;

import com.sarms.dao.StudentDAO;
import com.sarms.model.Student;
import com.sarms.util.DatabaseUtil;
import com.sarms.util.PasswordUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

//    @Override
//    public void save(Student student) throws SQLException {
//        String query = "INSERT INTO students (roll_number, name, email, address, semester, password) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (Connection conn = DatabaseUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, student.getRollNumber());
//            stmt.setString(2, student.getName());
//            stmt.setString(3, student.getEmail());
//            stmt.setString(4, student.getAddress());
//            stmt.setInt(5, student.getSemester());
//            stmt.setString(6, student.getPassword());
//
//            stmt.executeUpdate();
//        }
//    }

//public void save(Student student) throws SQLException {
//    String query = "INSERT INTO students (roll_number, name, email, address, semester, password, password_salt) " +
//            "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//    try (Connection conn = DatabaseUtil.getConnection();
//         PreparedStatement stmt = conn.prepareStatement(query)) {
//
//        // Generate salt and hash password
//        String salt = PasswordUtil.generateSalt();
//        String hashedPassword = PasswordUtil.hashPassword(student.getPassword(), salt);
//
//        stmt.setString(1, student.getRollNumber());
//        stmt.setString(2, student.getName());
//        stmt.setString(3, student.getEmail());
//        stmt.setString(4, student.getAddress());
//        stmt.setInt(5, student.getSemester());
//        stmt.setString(6, hashedPassword);
//        stmt.setString(7, salt);
//
//        stmt.executeUpdate();
//    }
//}

@Override
public void save(Student student) throws SQLException {
    // Simpler save without password salting
    String query = "INSERT INTO students (roll_number, name, email, address, semester, password) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, student.getRollNumber());
        stmt.setString(2, student.getName());
        stmt.setString(3, student.getEmail());
        stmt.setString(4, student.getAddress());
        stmt.setInt(5, student.getSemester());
        stmt.setString(6, student.getPassword()); // Store password directly

        stmt.executeUpdate();
    }
}

    @Override
    public Student findByRollNumber(String rollNumber) throws SQLException {
        String query = "SELECT * FROM students WHERE roll_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        }

        return students;
    }

    @Override
    public void update(Student student) throws SQLException {
        String query = "UPDATE students SET name = ?, email = ?, address = ?, " +
                "semester = ?, password = ? WHERE roll_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getAddress());
            stmt.setInt(4, student.getSemester());
            stmt.setString(5, student.getPassword());
            stmt.setString(6, student.getRollNumber());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String rollNumber) throws SQLException {
        String query = "DELETE FROM students WHERE roll_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.executeUpdate();
        }
    }

//    public boolean authenticate(String rollNumber, String password) throws SQLException {
//        String query = "SELECT * FROM students WHERE roll_number = ? AND password = ?";
//
//        try (Connection conn = DatabaseUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, rollNumber);
//            stmt.setString(2, password);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                return rs.next();
//            }
//        }
//    }
//    @Override
//    public boolean authenticate(String rollNumber, String password) throws SQLException {
//        String query = "SELECT password, password_salt FROM students WHERE roll_number = ?";
//
//        try (Connection conn = DatabaseUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, rollNumber);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String hashedPassword = rs.getString("password");
//                    String salt = rs.getString("password_salt");
//
//                    return PasswordUtil.verifyPassword(password, salt, hashedPassword);
//                }
//            }
//        }
//
//        return false;
//    }

    //latest without  debugging
//    @Override
//    public boolean authenticate(String rollNumber, String password) throws SQLException {
//        // Simpler authentication without salting
//        String query = "SELECT password FROM students WHERE roll_number = ?";
//
//        try (Connection conn = DatabaseUtil.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, rollNumber);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String storedPassword = rs.getString("password");
//
//                    // For now, do a direct comparison (not secure but works for testing)
//                    return password.equals(storedPassword);
//                }
//            }
//        }
//
//        return false;
//    }

    @Override
    public boolean authenticate(String rollNumber, String password) throws SQLException {
        System.out.println("Attempting to authenticate student with rollNumber: " + rollNumber);

        String query = "SELECT password, password_salt FROM students WHERE roll_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            System.out.println("Executing query: " + query.replace("?", "'" + rollNumber + "'"));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    String salt = rs.getString("password_salt");

                    System.out.println("Found student record. Stored password: " + storedPassword);
                    System.out.println("Password salt: " + (salt != null ? salt : "null or empty"));

                    // For direct comparison (temporary)
                    boolean match = password.equals(storedPassword);
                    System.out.println("Password match result: " + match);
                    return match;
                } else {
                    System.out.println("No student found with roll number: " + rollNumber);
                }
            }
        }

        return false;
    }
    @Override
    public List<Student> findBySemester(int semester) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, semester);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }

        return students;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setRollNumber(rs.getString("roll_number"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setAddress(rs.getString("address"));
        student.setSemester(rs.getInt("semester"));
        student.setPassword(rs.getString("password"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }

        return student;
    }
}