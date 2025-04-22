// src/main/java/com/sarms/dao/EnrollmentDAO.java
package com.sarms.dao;

import com.sarms.model.Enrollment;
import java.sql.SQLException;
import java.util.List;

public interface EnrollmentDAO {
    void save(Enrollment enrollment) throws SQLException;
    Enrollment find(String rollNumber, String courseCode, int semester) throws SQLException;
    List<Enrollment> findByStudent(String rollNumber) throws SQLException;
    List<Enrollment> findByCourse(String courseCode) throws SQLException;
    List<Enrollment> findByStudentAndSemester(String rollNumber, int semester) throws SQLException;
    void updateMarks(String rollNumber, String courseCode, int semester, Double marks) throws SQLException;
    void delete(String rollNumber, String courseCode, int semester) throws SQLException;
}