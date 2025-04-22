// src/main/java/com/sarms/dao/StudentDAO.java
package com.sarms.dao;

import com.sarms.model.Student;
import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    void save(Student student) throws SQLException;
    Student findByRollNumber(String rollNumber) throws SQLException;
    List<Student> findAll() throws SQLException;
    void update(Student student) throws SQLException;
    void delete(String rollNumber) throws SQLException;
    boolean authenticate(String rollNumber, String password) throws SQLException;
    List<Student> findBySemester(int semester) throws SQLException;
}