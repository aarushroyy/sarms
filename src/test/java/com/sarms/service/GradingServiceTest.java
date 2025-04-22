// src/test/java/com/sarms/service/GradingServiceTest.java
package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.SemesterRecordDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradingServiceTest {

    @Mock
    private EnrollmentDAO enrollmentDAO;

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private SemesterRecordDAO semesterRecordDAO;

    @Mock
    private StudentDAO studentDAO;

    private GradingService gradingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gradingService = new GradingService(enrollmentDAO, courseDAO, semesterRecordDAO, studentDAO);
    }

    @Test
    void calculateSWA_WithValidMarks_ReturnsCorrectAverage() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        Student mockStudent = new Student();
        mockStudent.setRollNumber(rollNumber);
        when(studentDAO.findByRollNumber(rollNumber)).thenReturn(mockStudent);

        // Create mock courses with different credits
        Course course1 = new Course("CS101", "Programming", 3, "CS", "");
        Course course2 = new Course("CS102", "Data Structures", 4, "CS", "");

        // Create mock enrollments with marks
        Enrollment enrollment1 = new Enrollment(rollNumber, "CS101", semester, 80.0);
        Enrollment enrollment2 = new Enrollment(rollNumber, "CS102", semester, 90.0);

        List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentDAO.findByStudentAndSemester(rollNumber, semester)).thenReturn(enrollments);

        when(courseDAO.findByCourseCode("CS101")).thenReturn(course1);
        when(courseDAO.findByCourseCode("CS102")).thenReturn(course2);

        // Act
        double swa = gradingService.calculateSWA(rollNumber, semester);

        // Assert - expected SWA: (80*3 + 90*4) / (3+4) = 85.71...
        assertEquals(85.71, swa, 0.01);
    }

    @Test
    void calculateSWA_WithNoEnrollments_ThrowsException() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        Student mockStudent = new Student();
        mockStudent.setRollNumber(rollNumber);
        when(studentDAO.findByRollNumber(rollNumber)).thenReturn(mockStudent);

        // Return empty enrollments list
        when(enrollmentDAO.findByStudentAndSemester(rollNumber, semester))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gradingService.calculateSWA(rollNumber, semester);
        });
    }

    @Test
    void calculateSWA_WithNoMarks_ReturnsZero() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        Student mockStudent = new Student();
        mockStudent.setRollNumber(rollNumber);
        when(studentDAO.findByRollNumber(rollNumber)).thenReturn(mockStudent);

        // Create mock course with credits
        Course course1 = new Course("CS101", "Programming", 3, "CS", "");

        // Create mock enrollment with no marks
        Enrollment enrollment1 = new Enrollment(rollNumber, "CS101", semester, null);

        List<Enrollment> enrollments = Collections.singletonList(enrollment1);
        when(enrollmentDAO.findByStudentAndSemester(rollNumber, semester)).thenReturn(enrollments);

        when(courseDAO.findByCourseCode("CS101")).thenReturn(course1);

        // Act
        double swa = gradingService.calculateSWA(rollNumber, semester);

        // Assert - expected SWA: 0 (no marks)
        assertEquals(0.0, swa, 0.001);
    }

    @Test
    void categorizeStudent_WithHighSWA_MarksVCList() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        // Mock SWA calculation to return a high value (above VC threshold)
        // This requires reflection to access the private constant or knowledge of the value
        double highSWA = 90.0; // Above VC_LIST_THRESHOLD (85.0)
        GradingService spyService = spy(gradingService);
        doReturn(highSWA).when(spyService).calculateSWA(rollNumber, semester);

        // No existing record
        when(semesterRecordDAO.find(rollNumber, semester)).thenReturn(null);

        // Act
        com.sarms.model.SemesterRecord record = spyService.categorizeStudent(rollNumber, semester);

        // Assert
        assertTrue(record.isOnVCList());
        assertFalse(record.isOnConditionalStanding());
        assertEquals(highSWA, record.getSwa(), 0.001);

        // Verify record was saved
        verify(semesterRecordDAO).save(record);
    }

    @Test
    void categorizeStudent_WithLowSWA_MarksConditionalStanding() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        // Mock SWA calculation to return a low value (below conditional threshold)
        double lowSWA = 45.0; // Below CONDITIONAL_STANDING_THRESHOLD (50.0)
        GradingService spyService = spy(gradingService);
        doReturn(lowSWA).when(spyService).calculateSWA(rollNumber, semester);

        // No existing record
        when(semesterRecordDAO.find(rollNumber, semester)).thenReturn(null);

        // Act
        com.sarms.model.SemesterRecord record = spyService.categorizeStudent(rollNumber, semester);

        // Assert
        assertFalse(record.isOnVCList());
        assertTrue(record.isOnConditionalStanding());
        assertEquals(lowSWA, record.getSwa(), 0.001);

        // Verify record was saved
        verify(semesterRecordDAO).save(record);
    }
}