// src/main/java/com/sarms/dao/CourseDAO.java
package com.sarms.dao;

import com.sarms.model.Course;
import java.sql.SQLException;
import java.util.List;

public interface CourseDAO {
    void save(Course course) throws SQLException;
    Course findByCourseCode(String courseCode) throws SQLException;
    List<Course> findAll() throws SQLException;
    void update(Course course) throws SQLException;
    void delete(String courseCode) throws SQLException;
    List<Course> findByDepartment(String department) throws SQLException;
}