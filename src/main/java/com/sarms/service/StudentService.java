// src/main/java/com/sarms/service/StudentService.java
package com.sarms.service;

import com.sarms.dao.StudentDAO;
import com.sarms.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void registerStudent(Student student) throws SQLException {
        // Validation logic
        if (student.getRollNumber() == null || student.getRollNumber().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }

        if (student.getName() == null || student.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if student already exists
        Student existingStudent = studentDAO.findByRollNumber(student.getRollNumber());
        if (existingStudent != null) {
            throw new IllegalArgumentException("Student with this roll number already exists");
        }

        studentDAO.save(student);
    }

    public Student getStudentByRollNumber(String rollNumber) throws SQLException {
        return studentDAO.findByRollNumber(rollNumber);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.findAll();
    }

    public List<Student> getStudentsBySemester(int semester) throws SQLException {
        return studentDAO.findBySemester(semester);
    }

    public void updateStudent(Student student) throws SQLException {
        // Validation logic
        if (student.getRollNumber() == null || student.getRollNumber().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }

        // Check if student exists
        Student existingStudent = studentDAO.findByRollNumber(student.getRollNumber());
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student with this roll number does not exist");
        }

        studentDAO.update(student);
    }

    public void deleteStudent(String rollNumber) throws SQLException {
        studentDAO.delete(rollNumber);
    }

    public boolean authenticateStudent(String rollNumber, String password) throws SQLException {
        return studentDAO.authenticate(rollNumber, password);
    }
}