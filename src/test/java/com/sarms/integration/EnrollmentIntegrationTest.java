// src/test/java/com/sarms/integration/EnrollmentIntegrationTest.java
package com.sarms.integration;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.dao.impl.CourseDAOImpl;
import com.sarms.dao.impl.EnrollmentDAOImpl;
import com.sarms.dao.impl.StudentDAOImpl;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.Student;
import com.sarms.service.EnrollmentService;
import com.sarms.util.DatabaseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for EnrollmentService.
 *
 * Note: This test requires an actual database connection.
 * It will create test data, run tests, and then clean up.
 */
class EnrollmentIntegrationTest {

    private EnrollmentService enrollmentService;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private Connection connection;

    private static final String TEST_ROLL_NUMBER = "TEST001";
    private static final String TEST_COURSE_CODE = "TEST101";

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize DAOs
        studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
        enrollmentDAO = new EnrollmentDAOImpl();

        // Initialize service
        enrollmentService = new EnrollmentService(enrollmentDAO, studentDAO, courseDAO);

        // Set up test data
        connection = DatabaseUtil.getConnection();
        setupTestData();
    }

    private void setupTestData() throws SQLException {
        // Create test student
        Student student = new Student(
                TEST_ROLL_NUMBER,
                "Test Student",
                "test@example.com",
                "Test Address",
                1,
                "password");

        try {
            studentDAO.save(student);
        } catch (SQLException e) {
            // Student might already exist
            System.out.println("Student already exists or error: " + e.getMessage());
        }

        // Create test course
        Course course = new Course(
                TEST_COURSE_CODE,
                "Test Course",
                3,
                "TEST",
                "Test Syllabus");

        try {
            courseDAO.save(course);
        } catch (SQLException e) {
            // Course might already exist
            System.out.println("Course already exists or error: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up test data
        try {
            // Delete test enrollments
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM enrollments WHERE roll_number = '" +
                        TEST_ROLL_NUMBER + "'");
            }

            // Delete test course
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM courses WHERE course_code = '" +
                        TEST_COURSE_CODE + "'");
            }

            // Delete test student
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM students WHERE roll_number = '" +
                        TEST_ROLL_NUMBER + "'");
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    void enrollAndRetrieveAndDrop() throws SQLException {
        // Test enrolling a student in a course
        enrollmentService.enrollStudent(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1);

        // Verify enrollment exists
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(TEST_ROLL_NUMBER);

        assertNotNull(enrollments);
        assertFalse(enrollments.isEmpty());

        Enrollment enrollment = enrollmentService.getEnrollment(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1);
        assertNotNull(enrollment);
        assertEquals(TEST_ROLL_NUMBER, enrollment.getRollNumber());
        assertEquals(TEST_COURSE_CODE, enrollment.getCourseCode());
        assertEquals(1, enrollment.getSemester());

        // Test updating marks
        double testMarks = 85.5;
        enrollmentService.updateMarks(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1, testMarks);

        enrollment = enrollmentService.getEnrollment(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1);
        assertNotNull(enrollment);
        assertEquals(testMarks, enrollment.getMarks(), 0.001);

        // Test dropping the course
        enrollmentService.dropCourse(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1);

        enrollment = enrollmentService.getEnrollment(TEST_ROLL_NUMBER, TEST_COURSE_CODE, 1);
        assertNull(enrollment);
    }
}