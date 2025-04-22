// src/main/java/com/sarms/controller/AdminController.java
package com.sarms.controller;

import com.sarms.model.Course;
import com.sarms.model.SemesterRecord;
import com.sarms.model.Student;
import com.sarms.service.CourseService;
import com.sarms.service.GradingService;
import com.sarms.service.StudentService;

import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private final CourseService courseService;
    private final StudentService studentService;
    private final GradingService gradingService;

    public AdminController(CourseService courseService, StudentService studentService,
                           GradingService gradingService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.gradingService = gradingService;
    }

    public void createCourse(Course course) throws SQLException {
        courseService.createCourse(course);
    }

    public void updateCourse(Course course) throws SQLException {
        courseService.updateCourse(course);
    }

    public void deleteCourse(String courseCode) throws SQLException {
        courseService.deleteCourse(courseCode);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseService.getAllCourses();
    }

    public Course getCourseByCourseCode(String courseCode) throws SQLException {
        return courseService.getCourseByCourseCode(courseCode);
    }

    public void registerStudent(Student student) throws SQLException {
        studentService.registerStudent(student);
    }

    public void updateStudent(Student student) throws SQLException {
        studentService.updateStudent(student);
    }

    public void deleteStudent(String rollNumber) throws SQLException {
        studentService.deleteStudent(rollNumber);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentService.getAllStudents();
    }

    public List<SemesterRecord> getVCListStudents() throws SQLException {
        return gradingService.getVCListStudents();
    }

    public List<SemesterRecord> getConditionalStandingStudents() throws SQLException {
        return gradingService.getConditionalStandingStudents();
    }
}