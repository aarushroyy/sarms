// src/main/java/com/sarms/controller/FacultyController.java
package com.sarms.controller;

import com.sarms.model.Enrollment;
import com.sarms.model.Student;
import com.sarms.model.Course;
import com.sarms.service.EnrollmentService;
import com.sarms.service.GradingService;
import com.sarms.service.StudentService;
import com.sarms.service.CourseService;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class FacultyController {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final GradingService gradingService;
    private final CourseService courseService;


    public FacultyController(EnrollmentService enrollmentService, StudentService studentService,
                             GradingService gradingService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.gradingService = gradingService;
        this.courseService = courseService;
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

    // In FacultyController.java, add:
    public List<Course> getAllCourses() throws SQLException {
        return courseService.getAllCourses();
    }

    public List<Enrollment> getEnrollmentsByCourseAndSemester(String courseCode, int semester) throws SQLException {
        List<Enrollment> allEnrollments = enrollmentService.getEnrollmentsByCourse(courseCode);
        return allEnrollments.stream()
                .filter(e -> e.getSemester() == semester)
                .collect(Collectors.toList());
    }

    public Student getStudentByRollNumber(String rollNumber) throws SQLException {
        return studentService.getStudentByRollNumber(rollNumber);
    }
}