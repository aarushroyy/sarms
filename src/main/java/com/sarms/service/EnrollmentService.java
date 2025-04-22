// src/main/java/com/sarms/service/EnrollmentService.java
package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.Student;

import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public EnrollmentService(EnrollmentDAO enrollmentDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    public void enrollStudent(String rollNumber, String courseCode, int semester) throws SQLException {
        // Validate student exists
        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new IllegalArgumentException("Student with roll number " + rollNumber + " does not exist");
        }

        // Validate course exists
        Course course = courseDAO.findByCourseCode(courseCode);
        if (course == null) {
            throw new IllegalArgumentException("Course with code " + courseCode + " does not exist");
        }

        // Check if already enrolled
        Enrollment existingEnrollment = enrollmentDAO.find(rollNumber, courseCode, semester);
        if (existingEnrollment != null) {
            throw new IllegalArgumentException("Student is already enrolled in this course for this semester");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setRollNumber(rollNumber);
        enrollment.setCourseCode(courseCode);
        enrollment.setSemester(semester);

        enrollmentDAO.save(enrollment);
    }

    public void updateMarks(String rollNumber, String courseCode, int semester, Double marks) throws SQLException {
        // Validate marks
        if (marks != null && (marks < 0 || marks > 100)) {
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        }

        // Check if enrollment exists
        Enrollment enrollment = enrollmentDAO.find(rollNumber, courseCode, semester);
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment record not found");
        }

        enrollmentDAO.updateMarks(rollNumber, courseCode, semester, marks);
    }

    public void dropCourse(String rollNumber, String courseCode, int semester) throws SQLException {
        // Check if enrollment exists
        Enrollment enrollment = enrollmentDAO.find(rollNumber, courseCode, semester);
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment record not found");
        }

        enrollmentDAO.delete(rollNumber, courseCode, semester);
    }

    public List<Enrollment> getEnrollmentsByStudent(String rollNumber) throws SQLException {
        return enrollmentDAO.findByStudent(rollNumber);
    }

    public List<Enrollment> getEnrollmentsByStudentAndSemester(String rollNumber, int semester) throws SQLException {
        return enrollmentDAO.findByStudentAndSemester(rollNumber, semester);
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseCode) throws SQLException {
        return enrollmentDAO.findByCourse(courseCode);
    }

    public Enrollment getEnrollment(String rollNumber, String courseCode, int semester) throws SQLException {
        return enrollmentDAO.find(rollNumber, courseCode, semester);
    }
}