// src/main/java/com/sarms/dao/impl/EnrollmentDAOImpl.java
package com.sarms.dao.impl;

import com.sarms.dao.EnrollmentDAO;
import com.sarms.model.Enrollment;
import com.sarms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public void save(Enrollment enrollment) throws SQLException {
        String query = "INSERT INTO enrollments (roll_number, course_code, semester, marks) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, enrollment.getRollNumber());
            stmt.setString(2, enrollment.getCourseCode());
            stmt.setInt(3, enrollment.getSemester());

            if (enrollment.getMarks() != null) {
                stmt.setDouble(4, enrollment.getMarks());
            } else {
                stmt.setNull(4, Types.DOUBLE);
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public Enrollment find(String rollNumber, String courseCode, int semester) throws SQLException {
        String query = "SELECT * FROM enrollments WHERE roll_number = ? AND course_code = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.setString(2, courseCode);
            stmt.setInt(3, semester);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        }

        return null;
    }

    // Continuing EnrollmentDAOImpl.java
    @Override
    public List<Enrollment> findByStudent(String rollNumber) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM enrollments WHERE roll_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> findByCourse(String courseCode) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM enrollments WHERE course_code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseCode);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> findByStudentAndSemester(String rollNumber, int semester) throws SQLException {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM enrollments WHERE roll_number = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.setInt(2, semester);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        }

        return enrollments;
    }

    @Override
    public void updateMarks(String rollNumber, String courseCode, int semester, Double marks) throws SQLException {
        String query = "UPDATE enrollments SET marks = ? WHERE roll_number = ? AND course_code = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (marks != null) {
                stmt.setDouble(1, marks);
            } else {
                stmt.setNull(1, Types.DOUBLE);
            }

            stmt.setString(2, rollNumber);
            stmt.setString(3, courseCode);
            stmt.setInt(4, semester);

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String rollNumber, String courseCode, int semester) throws SQLException {
        String query = "DELETE FROM enrollments WHERE roll_number = ? AND course_code = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.setString(2, courseCode);
            stmt.setInt(3, semester);

            stmt.executeUpdate();
        }
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setRollNumber(rs.getString("roll_number"));
        enrollment.setCourseCode(rs.getString("course_code"));
        enrollment.setSemester(rs.getInt("semester"));

        Double marks = rs.getDouble("marks");
        if (!rs.wasNull()) {
            enrollment.setMarks(marks);
        }

        return enrollment;
    }
}