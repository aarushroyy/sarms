// src/main/java/com/sarms/ui/MainFrame.java
package com.sarms.ui;

import com.sarms.controller.AdminController;
import com.sarms.controller.AuthController;
import com.sarms.controller.FacultyController;
import com.sarms.controller.StudentController;
import com.sarms.model.Role;
import com.sarms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final AuthController authController;
    private final StudentController studentController;
    private final FacultyController facultyController;
    private final AdminController adminController;

    private LoginPanel loginPanel;
    private JPanel contentPanel;
    private JPanel currentPanel;

    public MainFrame(AuthController authController, StudentController studentController,
                     FacultyController facultyController, AdminController adminController) {
        this.authController = authController;
        this.studentController = studentController;
        this.facultyController = facultyController;
        this.adminController = adminController;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Academic Record Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create content panel with card layout
        contentPanel = new JPanel(new CardLayout());

        // Create login panel
        loginPanel = new LoginPanel(authController, this);
        contentPanel.add(loginPanel, "login");

        // Set content panel as the content pane
        setContentPane(contentPanel);

        // Show login panel by default
        showLoginPanel();
    }

    public void showLoginPanel() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "login");
        currentPanel = loginPanel;
    }

    public void showDashboard() {
        User currentUser = authController.getCurrentUser();
        if (currentUser == null) {
            showLoginPanel();
            return;
        }

        // Create appropriate dashboard based on user role
        JPanel dashboard;
        switch (currentUser.getRole()) {
            case STUDENT:
                dashboard = new StudentDashboard(currentUser, studentController, this);
                break;
            case FACULTY:
                dashboard = new FacultyDashboard(currentUser, facultyController, this);
                break;
            case ADMIN:
                dashboard = new AdminDashboard(currentUser, adminController, this);
                break;
            default:
                showLoginPanel();
                return;
        }

        // Add the dashboard to the content panel
        contentPanel.add(dashboard, "dashboard");

        // Show the dashboard
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "dashboard");
        currentPanel = dashboard;
    }

    public void logout() {
        authController.logout();
        showLoginPanel();
    }
}