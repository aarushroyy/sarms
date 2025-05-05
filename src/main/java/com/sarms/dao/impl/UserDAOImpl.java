// src/main/java/com/sarms/dao/impl/UserDAOImpl.java
package com.sarms.dao.impl;

import com.sarms.dao.UserDAO;
import com.sarms.model.Role;
import com.sarms.model.User;
import com.sarms.util.DatabaseUtil;
import com.sarms.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int save(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, password_salt, role, display_name) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Generate salt and hash password
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);
            stmt.setString(4, user.getRole().toString());
            stmt.setString(5, user.getDisplayName());

            return stmt.executeUpdate();
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }

        return users;
    }

    @Override
    public List<User> findByRole(String role) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        }

        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE users SET display_name = ?, role = ? WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getDisplayName());
            stmt.setString(2, user.getRole().toString());
            stmt.setString(3, user.getUsername());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String username) throws SQLException {
        String query = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT password, password_salt FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    String salt = rs.getString("password_salt");

                    return PasswordUtil.verifyPassword(password, salt, hashedPassword);
                }
            }
        }

        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // This is the hashed password
        user.setPasswordSalt(rs.getString("password_salt"));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setDisplayName(rs.getString("display_name"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        return user;
    }
}