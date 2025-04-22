// src/main/java/com/sarms/service/AuthenticationService.java
package com.sarms.service;

import com.sarms.dao.StudentDAO;
import com.sarms.dao.UserDAO;
import com.sarms.model.Role;
import com.sarms.model.Student;
import com.sarms.model.User;
import com.sarms.util.PasswordUtil;

import java.sql.SQLException;

public class AuthenticationService {
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;

    public AuthenticationService(StudentDAO studentDAO, UserDAO userDAO) {
        this.studentDAO = studentDAO;
        this.userDAO = userDAO;
    }

//    public User authenticate(String username, String password) throws SQLException {
//        // First check if it's a student login (using roll number as username)
//        if (studentDAO.authenticate(username, password)) {
//            Student student = studentDAO.findByRollNumber(username);
//            return new User(student.getRollNumber(), "", Role.STUDENT, student.getName());
//        }
//
//        // Then check faculty and admin users
//        User user = userDAO.findByUsername(username);
//        if (user != null && userDAO.authenticate(username, password)) {
//            // Set the password to empty for security
//            user.setPassword("");
//            return user;
//        }
//
//        return null; // Authentication failed
//    }

    public User authenticate(String username, String password) throws SQLException {
        System.out.println("Authentication attempt: username=" + username + ", password=" + password);

        // First check if it's a student login (using roll number as username)
        try {
            Student student = studentDAO.findByRollNumber(username);
            if (student != null) {
                System.out.println("Found student: " + student.getName());
                // Return a user object regardless of password (for testing)
                return new User(student.getRollNumber(), "", Role.STUDENT, student.getName());
            } else {
                System.out.println("No student found with roll number: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error checking student: " + e.getMessage());
        }

        // Then check faculty and admin users
        try {
            User user = userDAO.findByUsername(username);
            if (user != null) {
                System.out.println("Found user: " + user.getDisplayName() + ", Role: " + user.getRole());
                // Return user regardless of password (for testing)
                user.setPassword("");
                return user;
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error checking user: " + e.getMessage());
        }

        System.out.println("Authentication failed for: " + username);
        return null; // Authentication failed
    }

    public boolean registerUser(User user) throws SQLException {
        // Check if user already exists
        User existingUser = userDAO.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }

        userDAO.save(user);
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        // Authenticate with old password
        if (!userDAO.authenticate(username, oldPassword)) {
            return false;
        }

        // Get user
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return false;
        }

        // Update password
        user.setPassword(newPassword);
        userDAO.save(user);
        return true;
    }

    public boolean hasRole(User user, Role requiredRole) {
        return user != null && user.getRole() == requiredRole;
    }
}