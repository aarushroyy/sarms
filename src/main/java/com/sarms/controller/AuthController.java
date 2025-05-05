// src/main/java/com/sarms/controller/AuthController.java
package com.sarms.controller;

import com.sarms.model.User;
import com.sarms.model.Student;
import com.sarms.service.AuthenticationService;

import java.sql.SQLException;

public class AuthController {
    private final AuthenticationService authService;
    private User currentUser;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    public boolean login(String username, String password) {
        try {
            User user = authService.authenticate(username, password);
            if (user != null) {
                this.currentUser = user;
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

// Update src/main/java/com/sarms/controller/AuthController.java
// Add these methods:

    public boolean registerUser(User user) throws SQLException {
        return authService.registerUser(user);
    }

    public boolean registerStudent(Student student) throws SQLException {
        return authService.registerStudent(student);
    }
}