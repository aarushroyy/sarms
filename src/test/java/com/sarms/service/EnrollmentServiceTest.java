package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentDAO enrollmentDAO;

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private CourseDAO courseDAO;

    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enrollmentService = new EnrollmentService(enrollmentDAO, studentDAO, courseDAO);
    }

    @Test
    void enrollStudent_ValidEnrollment_SavesEnrollment() throws SQLException {
        String rollNumber = "S001";
        String courseCode = "CS101";
        int semester = 1;

        Student student = new Student();
        student.setRollNumber(rollNumber);

        Course course = new Course();
        course.setCourseCode(courseCode);

        when(studentDAO.findByRollNumber(rollNumber)).thenReturn(student);
        when(courseDAO.findByCourseCode(courseCode)).thenReturn(course);
        when(enrollmentDAO.find(rollNumber, courseCode, semester)).thenReturn(null);

        enrollmentService.enrollStudent(rollNumber, courseCode, semester);

        verify(enrollmentDAO).save(any(Enrollment.class));
    }

    @Test
    void updateMarks_ValidData_UpdatesMarks() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        String courseCode = "CS101";
        int semester = 1;
        Double marks = 85.5;

        Enrollment enrollment = new Enrollment(rollNumber, courseCode, semester, null);
        when(enrollmentDAO.find(rollNumber, courseCode, semester)).thenReturn(enrollment);

        // Act
        enrollmentService.updateMarks(rollNumber, courseCode, semester, marks);

        // Assert
        verify(enrollmentDAO).updateMarks(rollNumber, courseCode, semester, marks);
    }

    @Test
    void updateMarks_InvalidMarks_ThrowsException() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        String courseCode = "CS101";
        int semester = 1;
        Double invalidMarks = 101.0; // Above 100

        Enrollment enrollment = new Enrollment(rollNumber, courseCode, semester, null);
        when(enrollmentDAO.find(rollNumber, courseCode, semester)).thenReturn(enrollment);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.updateMarks(rollNumber, courseCode, semester, invalidMarks);
        });

        // Verify updateMarks was never called
        verify(enrollmentDAO, never()).updateMarks(anyString(), anyString(), anyInt(), anyDouble());
    }
}