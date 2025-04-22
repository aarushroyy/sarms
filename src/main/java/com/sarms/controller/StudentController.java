// src/main/java/com/sarms/controller/StudentController.java
package com.sarms.controller;

import com.sarms.model.Enrollment;
import com.sarms.model.SemesterRecord;
import com.sarms.model.Student;
import com.sarms.service.EnrollmentService;
import com.sarms.service.GradingService;
import com.sarms.service.ReportService;
import com.sarms.service.StudentService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StudentController {
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final GradingService gradingService;
    private final ReportService reportService;

    public StudentController(StudentService studentService, EnrollmentService enrollmentService,
                             GradingService gradingService, ReportService reportService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.gradingService = gradingService;
        this.reportService = reportService;
    }

    public void registerStudent(Student student) throws SQLException {
        studentService.registerStudent(student);
    }

    public Student getStudentByRollNumber(String rollNumber) throws SQLException {
        return studentService.getStudentByRollNumber(rollNumber);
    }

    public void enrollInCourse(String rollNumber, String courseCode, int semester) throws SQLException {
        enrollmentService.enrollStudent(rollNumber, courseCode, semester);
    }

    public void dropCourse(String rollNumber, String courseCode, int semester) throws SQLException {
        enrollmentService.dropCourse(rollNumber, courseCode, semester);
    }

    public List<Enrollment> getStudentEnrollments(String rollNumber) throws SQLException {
        return enrollmentService.getEnrollmentsByStudent(rollNumber);
    }

    public List<Enrollment> getStudentSemesterEnrollments(String rollNumber, int semester) throws SQLException {
        return enrollmentService.getEnrollmentsByStudentAndSemester(rollNumber, semester);
    }

    public SemesterRecord getSemesterRecord(String rollNumber, int semester) throws SQLException {
        return gradingService.getSemesterRecord(rollNumber, semester);
    }

    public List<SemesterRecord> getAllSemesterRecords(String rollNumber) throws SQLException {
        return gradingService.getStudentRecords(rollNumber);
    }

    public Map<String, Object> generateSemesterReport(String rollNumber, int semester) throws SQLException {
        return reportService.generateSemesterReport(rollNumber, semester);
    }

    public String formatSemesterReport(Map<String, Object> reportData) {
        return reportService.formatSemesterReport(reportData);
    }
}