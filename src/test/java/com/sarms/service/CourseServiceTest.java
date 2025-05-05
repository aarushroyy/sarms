package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseDAO courseDAO;

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService(courseDAO);
    }

    @Test
    void createCourse_ValidCourse_SavesCourse() throws SQLException {
        Course course = new Course("CS201", "Advanced Programming", 4, "CS", "Syllabus");
        when(courseDAO.findByCourseCode("CS201")).thenReturn(null);

        courseService.createCourse(course);

        verify(courseDAO).save(course);
    }

    @Test
    void createCourse_DuplicateCourseCode_ThrowsException() throws SQLException {
        Course existingCourse = new Course("CS201", "Programming", 3, "CS", "Old syllabus");
        Course newCourse = new Course("CS201", "Advanced Programming", 4, "CS", "New syllabus");

        when(courseDAO.findByCourseCode("CS201")).thenReturn(existingCourse);

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.createCourse(newCourse);
        });

        verify(courseDAO, never()).save(any(Course.class));
    }

    @Test
    void getAllCourses_ReturnsCourseList() throws SQLException {
        // Arrange
        List<Course> expectedCourses = Arrays.asList(
                new Course("CS101", "Intro Programming", 3, "CS", "Syllabus 1"),
                new Course("CS102", "Data Structures", 4, "CS", "Syllabus 2")
        );

        when(courseDAO.findAll()).thenReturn(expectedCourses);

        // Act
        List<Course> actualCourses = courseService.getAllCourses();

        // Assert
        assertEquals(expectedCourses, actualCourses);
    }
}