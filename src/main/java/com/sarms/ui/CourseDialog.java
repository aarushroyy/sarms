// src/main/java/com/sarms/ui/CourseDialog.java
package com.sarms.ui;

import com.sarms.model.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseDialog extends JDialog {
    private JTextField courseCodeField;
    private JTextField titleField;
    private JTextField creditsField;
    private JTextField departmentField;
    private JTextArea syllabusArea;
    private boolean approved;
    private Course course;

    public CourseDialog(JFrame parent, String title, Course course) {
        super(parent, title, true);
        this.course = course;

        // Initialize UI
        initializeUI();

        // If course is provided, populate fields for editing
        if (course != null) {
            populateFields();
        }

        // Set dialog properties
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Course Code
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Course Code:"), gbc);

        gbc.gridx = 1;
        courseCodeField = new JTextField(20);
        formPanel.add(courseCodeField, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Credits
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Credits:"), gbc);

        gbc.gridx = 1;
        creditsField = new JTextField(20);
        formPanel.add(creditsField, gbc);

        // Department
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        departmentField = new JTextField(20);
        formPanel.add(departmentField, gbc);

        // Syllabus
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Syllabus:"), gbc);

        gbc.gridx = 1;
        syllabusArea = new JTextArea(5, 20);
        syllabusArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(syllabusArea);
        formPanel.add(scrollPane, gbc);

        // Add form panel
        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    approved = true;
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approved = false;
                dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        courseCodeField.setText(course.getCourseCode());
        titleField.setText(course.getTitle());
        creditsField.setText(String.valueOf(course.getCredits()));
        departmentField.setText(course.getDepartment());
        syllabusArea.setText(course.getSyllabus());

        // If editing existing course, disable course code field
        courseCodeField.setEnabled(false);
    }

    private boolean validateInput() {
        try {
            // Check for empty fields
            if (courseCodeField.getText().trim().isEmpty() ||
                    titleField.getText().trim().isEmpty() ||
                    creditsField.getText().trim().isEmpty() ||
                    departmentField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields except Syllabus are required",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validate credits is a positive integer
            int credits = Integer.parseInt(creditsField.getText().trim());
            if (credits <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Credits must be a positive number",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Credits must be a valid number",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isApproved() {
        return approved;
    }

    public Course getCourse() {
        if (course == null) {
            course = new Course();
        }

        course.setCourseCode(courseCodeField.getText().trim());
        course.setTitle(titleField.getText().trim());
        course.setCredits(Integer.parseInt(creditsField.getText().trim()));
        course.setDepartment(departmentField.getText().trim());
        course.setSyllabus(syllabusArea.getText().trim());

        return course;
    }
}