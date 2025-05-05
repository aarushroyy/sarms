// src/main/java/com/sarms/ui/RegistrationPanel.java
package com.sarms.ui;

import com.sarms.controller.AuthController;
import com.sarms.model.Role;
import com.sarms.model.Student;
import com.sarms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegistrationPanel extends JPanel {
    private final AuthController authController;
    private final MainFrame parent;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField displayNameField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<Integer> semesterComboBox;
    private JComboBox<Role> roleComboBox;
    private JLabel statusLabel;
    private JPanel studentFieldsPanel;

    public RegistrationPanel(AuthController authController, MainFrame parent) {
        this.authController = authController;
        this.parent = parent;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create a panel for the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title label
        JLabel titleLabel = new JLabel("SARMS - User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 5, 30, 5);
        formPanel.add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;

        // Role selection
        JLabel roleLabel = new JLabel("Register as:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(roleLabel, gbc);

        roleComboBox = new JComboBox<>(Role.values());
        roleComboBox.setName("roleComboBox");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(roleComboBox, gbc);

        // Username/Roll Number label
        JLabel usernameLabel = new JLabel("Username/Roll Number:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameLabel, gbc);

        // Username field
        usernameField = new JTextField(20);
        usernameField.setName("usernameField");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);

        // Display Name label
        JLabel displayNameLabel = new JLabel("Full Name:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(displayNameLabel, gbc);

        // Display Name field
        displayNameField = new JTextField(20);
        displayNameField.setName("displayNameField");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(displayNameField, gbc);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setName("passwordField");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Confirm Password label
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(confirmPasswordLabel, gbc);

        // Confirm Password field
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setName("confirmPasswordField");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(confirmPasswordField, gbc);

        // Student-specific fields panel
        studentFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints sgbc = new GridBagConstraints();
        sgbc.insets = new Insets(5, 5, 5, 5);
        sgbc.anchor = GridBagConstraints.WEST;

        // Email label
        JLabel emailLabel = new JLabel("Email:");
        sgbc.gridx = 0;
        sgbc.gridy = 0;
        sgbc.anchor = GridBagConstraints.EAST;
        studentFieldsPanel.add(emailLabel, sgbc);

        // Email field
        emailField = new JTextField(20);
        emailField.setName("emailField");
        sgbc.gridx = 1;
        sgbc.gridy = 0;
        sgbc.anchor = GridBagConstraints.WEST;
        studentFieldsPanel.add(emailField, sgbc);

        // Address label
        JLabel addressLabel = new JLabel("Address:");
        sgbc.gridx = 0;
        sgbc.gridy = 1;
        sgbc.anchor = GridBagConstraints.EAST;
        studentFieldsPanel.add(addressLabel, sgbc);

        // Address field
        addressField = new JTextField(20);
        addressField.setName("addressField");
        sgbc.gridx = 1;
        sgbc.gridy = 1;
        sgbc.anchor = GridBagConstraints.WEST;
        studentFieldsPanel.add(addressField, sgbc);

        // Semester label
        JLabel semesterLabel = new JLabel("Semester:");
        sgbc.gridx = 0;
        sgbc.gridy = 2;
        sgbc.anchor = GridBagConstraints.EAST;
        studentFieldsPanel.add(semesterLabel, sgbc);

        // Semester combobox
        semesterComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        semesterComboBox.setName("semesterComboBox");
        sgbc.gridx = 1;
        sgbc.gridy = 2;
        sgbc.anchor = GridBagConstraints.WEST;
        studentFieldsPanel.add(semesterComboBox, sgbc);

        // Add student-specific fields panel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(studentFieldsPanel, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setName("registerButton");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(registerButton, gbc);

        // Back to login button
        JButton backButton = new JButton("Back to Login");
        backButton.setName("backButton");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(backButton, gbc);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setName("statusLabel");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(statusLabel, gbc);

        // Add the form panel to the center of the login panel
        add(formPanel, BorderLayout.CENTER);

        // Add role change listener to show/hide student fields
        roleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Role selectedRole = (Role) roleComboBox.getSelectedItem();
                studentFieldsPanel.setVisible(selectedRole == Role.STUDENT);
                revalidate();
                repaint();
            }
        });

        // Initial state - hide student fields if not student role
        Role initialRole = (Role) roleComboBox.getSelectedItem();
        studentFieldsPanel.setVisible(initialRole == Role.STUDENT);

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showLoginPanel();
            }
        });
    }

    private void registerUser() {
        // Get form values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String displayName = displayNameField.getText().trim();
        Role role = (Role) roleComboBox.getSelectedItem();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
            statusLabel.setText("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            return;
        }

        try {
            boolean success = false;

            if (role == Role.STUDENT) {
                // Additional student-specific validation
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();
                int semester = (Integer) semesterComboBox.getSelectedItem();

                if (email.isEmpty() || address.isEmpty()) {
                    statusLabel.setText("All fields are required");
                    return;
                }

                // Create and register student
                Student student = new Student(
                        username,
                        displayName,
                        email,
                        address,
                        semester,
                        password
                );

                success = authController.registerStudent(student);
            } else {
                // Create and register faculty or admin
                User user = new User(
                        username,
                        password,
                        role,
                        displayName
                );

                success = authController.registerUser(user);
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful! You can now login.",
                        "Registration Success",
                        JOptionPane.INFORMATION_MESSAGE);
                parent.showLoginPanel();
            } else {
                statusLabel.setText("Registration failed. Username may already exist.");
            }
        } catch (SQLException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}