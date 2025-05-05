// src/main/java/com/sarms/dao/StudentDAO.java
package com.sarms.dao;

import com.sarms.model.Student;

import com.sarms.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    int save(User user) throws SQLException;
    User findByUsername(String username) throws SQLException;
    List<User> findAll() throws SQLException;
    List<User> findByRole(String role) throws SQLException;
    void update(User user) throws SQLException;
    void delete(String username) throws SQLException;
    boolean authenticate(String username, String password) throws SQLException;
}