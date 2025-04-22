// src/test/java/com/sarms/service/StudentServiceTest.java
package com.sarms.service;

import com.sarms.dao.StudentDAO;
import com.sarms.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentDAO);
    }

    @Test
    void registerStudent_ValidStudent_SavesStudent() throws SQLException {
        // Arrange
        Student student = new Student(
                "S001",
                "John Doe",
                "john@example.com",
                "123 Main St",
                1,
                "password");

        when(studentDAO.findByRollNumber("S001")).thenReturn(null);

        // Act
        studentService.registerStudent(student);

        // Assert
        verify(studentDAO).save(student);
    }

    @Test
    void registerStudent_DuplicateRollNumber_ThrowsException() throws SQLException {
        // Arrange
        Student existingStudent = new Student(
                "S001",
                "John Doe",
                "john@example.com",
                "123 Main St",
                1,
                "password");

        Student newStudent = new Student(
                "S001",
                "Jane Doe",
                "jane@example.com",
                "456 Oak St",
                1,
                "password");

        when(studentDAO.findByRollNumber("S001")).thenReturn(existingStudent);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.registerStudent(newStudent);
        });

        // Verify save was never called
        verify(studentDAO, never()).save(any(Student.class));
    }

    @Test
    void registerStudent_EmptyRollNumber_ThrowsException() throws SQLException {
        // Arrange
        Student student = new Student(
                "",
                "John Doe",
                "john@example.com",
                "123 Main St",
                1,
                "password");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.registerStudent(student);
        });

        // Verify save was never called
        verify(studentDAO, never()).save(any(Student.class));
    }

    @Test
    void registerStudent_EmptyName_ThrowsException() throws SQLException {
        // Arrange
        Student student = new Student(
                "S001",
                "",
                "john@example.com",
                "123 Main St",
                1,
                "password");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.registerStudent(student);
        });

        // Verify save was never called
        verify(studentDAO, never()).save(any(Student.class));
    }
}