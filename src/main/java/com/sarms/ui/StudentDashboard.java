// src/main/java/com/sarms/ui/StudentDashboard.java
package com.sarms.ui;

import com.sarms.controller.StudentController;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.SemesterRecord;
import com.sarms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StudentDashboard extends JPanel {
    private final User user;
    private final StudentController studentController;
    private final MainFrame parent;

    private JTabbedPane tabbedPane;
    private JPanel enrollmentsPanel;
    private JPanel coursesPanel;
    private JPanel reportsPanel;

    public StudentDashboard(User user, StudentController studentController, MainFrame parent) {
        this.user = user;
        this.studentController = studentController;
        this.parent = parent;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create tab panels
        enrollmentsPanel = createEnrollmentsPanel();
        coursesPanel = createCoursesPanel();
        reportsPanel = createReportsPanel();

        // Add tabs
        tabbedPane.addTab("My Enrollments", enrollmentsPanel);
        tabbedPane.addTab("Available Courses", coursesPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // Add tabbed pane to the center
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getDisplayName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel, BorderLayout.WEST);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.logout();
            }
        });
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createEnrollmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table model for enrollments
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Course Code", "Title", "Credits", "Semester", "Marks"}, 0);

        JTable enrollmentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load data
        try {
            List<Enrollment> enrollments = studentController.getStudentEnrollments(user.getUsername());
            for (Enrollment enrollment : enrollments) {
                // In a real application, you would fetch course details from the database
                // For simplicity, we're using placeholder values
                tableModel.addRow(new Object[]{
                        enrollment.getCourseCode(),
                        "Course Title", // This would come from the database
                        3, // Credits would come from the database
                        enrollment.getSemester(),
                        enrollment.getMarks() != null ? enrollment.getMarks() : "Not Graded"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading enrollments: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel createCoursesPanel() {
        // In a real application, this would show available courses for enrollment
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel placeholderLabel = new JLabel("Available courses for enrollment would be shown here.");
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(placeholderLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel semesterLabel = new JLabel("Select Semester:");
        JComboBox<Integer> semesterComboBox = new JComboBox<>();
        // Add semesters (in a real app, this would be populated from the database)
        semesterComboBox.addItem(1);
        semesterComboBox.addItem(2);

        JButton generateButton = new JButton("Generate Report");

        topPanel.add(semesterLabel);
        topPanel.add(semesterComboBox);
        topPanel.add(generateButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Text area for report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Generate report button action
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int semester = (int) semesterComboBox.getSelectedItem();
                    Map<String, Object> reportData = studentController.generateSemesterReport(
                            user.getUsername(), semester);
                    String reportText = studentController.formatSemesterReport(reportData);
                    reportTextArea.setText(reportText);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error generating report: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}