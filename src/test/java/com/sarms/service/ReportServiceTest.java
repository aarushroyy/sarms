package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.SemesterRecordDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.SemesterRecord;
import com.sarms.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private EnrollmentDAO enrollmentDAO;

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private SemesterRecordDAO semesterRecordDAO;

    @Mock
    private GradingService gradingService;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(studentDAO, enrollmentDAO, courseDAO, semesterRecordDAO, gradingService);
    }

    @Test
    void generateSemesterReport_ValidData_ReturnsReportData() throws SQLException {
        // Arrange
        String rollNumber = "S001";
        int semester = 1;

        Student student = new Student(rollNumber, "John Doe", "john@example.com", "Address", semester, "pass");
        when(studentDAO.findByRollNumber(rollNumber)).thenReturn(student);

        Course course1 = new Course("CS101", "Programming", 3, "CS", "Syllabus");
        Course course2 = new Course("MATH101", "Calculus", 4, "MATH", "Syllabus");

        when(courseDAO.findByCourseCode("CS101")).thenReturn(course1);
        when(courseDAO.findByCourseCode("MATH101")).thenReturn(course2);

        Enrollment enrollment1 = new Enrollment(rollNumber, "CS101", semester, 85.0);
        Enrollment enrollment2 = new Enrollment(rollNumber, "MATH101", semester, 90.0);

        when(enrollmentDAO.findByStudentAndSemester(rollNumber, semester))
                .thenReturn(Arrays.asList(enrollment1, enrollment2));

        SemesterRecord record = new SemesterRecord(rollNumber, semester, 87.9, true, false);
        when(semesterRecordDAO.find(rollNumber, semester)).thenReturn(record);

        // Act
        Map<String, Object> report = reportService.generateSemesterReport(rollNumber, semester);

        // Assert
        assertNotNull(report);
        assertEquals(student, report.get("student"));
        assertEquals(semester, report.get("semester"));
        assertEquals(7, report.get("totalCredits")); // 3 + 4 credits
        assertEquals(87.9, report.get("swa"));
        assertEquals(true, report.get("onVCList"));
        assertEquals(false, report.get("onConditionalStanding"));
    }

    @Test
    void formatSemesterReport_ValidData_ReturnsFormattedString() {
        // This would test the formatting of the report text
        // Implementation would depend on your specific formatting requirements
    }
}