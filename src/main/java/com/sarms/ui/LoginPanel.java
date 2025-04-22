// src/main/java/com/sarms/ui/LoginPanel.java
package com.sarms.ui;

import com.sarms.controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private final AuthController authController;
    private final MainFrame parent;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginPanel(AuthController authController, MainFrame parent) {
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
        JLabel titleLabel = new JLabel("SARMS - Login");
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

        // Username label
        JLabel usernameLabel = new JLabel("Username/Roll Number:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameLabel, gbc);

        // Username field
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(loginButton, gbc);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(statusLabel, gbc);

        // Add the form panel to the center of the login panel
        add(formPanel, BorderLayout.CENTER);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Username and password are required");
                    return;
                }

                boolean success = authController.login(username, password);
                if (success) {
                    parent.showDashboard();
                } else {
                    statusLabel.setText("Invalid username or password");
                }
            }
        });
    }
}