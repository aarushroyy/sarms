// src/main/java/com/sarms/controller/FacultyController.java
package com.sarms.controller;

import com.sarms.model.Enrollment;
import com.sarms.model.Student;
import com.sarms.service.EnrollmentService;
import com.sarms.service.GradingService;
import com.sarms.service.StudentService;

import java.sql.SQLException;
import java.util.List;

public class FacultyController {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final GradingService gradingService;

    public FacultyController(EnrollmentService enrollmentService, StudentService studentService,
                             GradingService gradingService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.gradingService = gradingService;
    }

    public void enterMarks(String rollNumber, String courseCode, int semester, Double marks) throws SQLException {
        enrollmentService.updateMarks(rollNumber, courseCode, semester, marks);

        // After entering marks, recalculate the SWA and categorize the student
        gradingService.categorizeStudent(rollNumber, semester);
    }

    public List<Student> getStudentsBySemester(int semester) throws SQLException {
        return studentService.getStudentsBySemester(semester);
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseCode) throws SQLException {
        return enrollmentService.getEnrollmentsByCourse(courseCode);
    }

    public List<Enrollment> getStudentEnrollments(String rollNumber, int semester) throws SQLException {
        return enrollmentService.getEnrollmentsByStudentAndSemester(rollNumber, semester);
    }
}