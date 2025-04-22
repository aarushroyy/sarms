// src/main/java/com/sarms/dao/impl/CourseDAOImpl.java
package com.sarms.dao.impl;

import com.sarms.dao.CourseDAO;
import com.sarms.model.Course;
import com.sarms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void save(Course course) throws SQLException {
        String query = "INSERT INTO courses (course_code, title, credits, department, syllabus) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());
            stmt.setString(5, course.getSyllabus());

            stmt.executeUpdate();
        }
    }

    @Override
    public Course findByCourseCode(String courseCode) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Course> findAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }

        return courses;
    }

    @Override
    public void update(Course course) throws SQLException {
        String query = "UPDATE courses SET title = ?, credits = ?, department = ?, " +
                "syllabus = ? WHERE course_code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, course.getTitle());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, course.getDepartment());
            stmt.setString(4, course.getSyllabus());
            stmt.setString(5, course.getCourseCode());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String courseCode) throws SQLException {
        String query = "DELETE FROM courses WHERE course_code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseCode);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Course> findByDepartment(String department) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE department = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, department);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        }

        return courses;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseCode(rs.getString("course_code"));
        course.setTitle(rs.getString("title"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartment(rs.getString("department"));
        course.setSyllabus(rs.getString("syllabus"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            course.setCreatedAt(createdAt.toLocalDateTime());
        }

        return course;
    }
}