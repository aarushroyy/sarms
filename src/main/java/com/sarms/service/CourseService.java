// src/main/java/com/sarms/service/CourseService.java
package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.model.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public void createCourse(Course course) throws SQLException {
        // Validation logic
        if (course.getCourseCode() == null || course.getCourseCode().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }

        if (course.getTitle() == null || course.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (course.getCredits() <= 0) {
            throw new IllegalArgumentException("Credits must be greater than zero");
        }

        // Check if course already exists
        Course existingCourse = courseDAO.findByCourseCode(course.getCourseCode());
        if (existingCourse != null) {
            throw new IllegalArgumentException("Course with this code already exists");
        }

        courseDAO.save(course);
    }

    public Course getCourseByCourseCode(String courseCode) throws SQLException {
        return courseDAO.findByCourseCode(courseCode);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.findAll();
    }

    public List<Course> getCoursesByDepartment(String department) throws SQLException {
        return courseDAO.findByDepartment(department);
    }

    public void updateCourse(Course course) throws SQLException {
        // Validation logic
        if (course.getCourseCode() == null || course.getCourseCode().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }

        // Check if course exists
        Course existingCourse = courseDAO.findByCourseCode(course.getCourseCode());
        if (existingCourse == null) {
            throw new IllegalArgumentException("Course with this code does not exist");
        }

        courseDAO.update(course);
    }

    public void deleteCourse(String courseCode) throws SQLException {
        courseDAO.delete(courseCode);
    }
}