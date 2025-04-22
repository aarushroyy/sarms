// src/main/java/com/sarms/ui/FacultyDashboard.java
package com.sarms.ui;

import com.sarms.controller.FacultyController;
import com.sarms.model.Enrollment;
import com.sarms.model.User;
import com.sarms.model.Student;
import com.sarms.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class FacultyDashboard extends JPanel {
    private final User user;
    private final FacultyController facultyController;
    private final MainFrame parent;

    private JTabbedPane tabbedPane;
    private JPanel enterMarksPanel;
    private JPanel viewStudentsPanel;

    public FacultyDashboard(User user, FacultyController facultyController, MainFrame parent) {
        this.user = user;
        this.facultyController = facultyController;
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
        enterMarksPanel = createEnterMarksPanel();
        viewStudentsPanel = createViewStudentsPanel();

        // Add tabs
        tabbedPane.addTab("Enter Marks", enterMarksPanel);
        tabbedPane.addTab("View Students", viewStudentsPanel);

        // Add tabbed pane to the center
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getDisplayName() + " (Faculty)");
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

//    private JPanel createEnterMarksPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Search controls
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//
//        JLabel courseLabel = new JLabel("Course:");
//        JComboBox<String> courseComboBox = new JComboBox<>();
//        // Add courses (in a real app, these would come from the database)
//        courseComboBox.addItem("CS101");
//        courseComboBox.addItem("CS102");
//
//        JLabel semesterLabel = new JLabel("Semester:");
//        JComboBox<Integer> semesterComboBox = new JComboBox<>();
//        semesterComboBox.addItem(1);
//        semesterComboBox.addItem(2);
//
//        JButton searchButton = new JButton("Search");
//
//        searchPanel.add(courseLabel);
//        searchPanel.add(courseComboBox);
//        searchPanel.add(semesterLabel);
//        searchPanel.add(semesterComboBox);
//        searchPanel.add(searchButton);
//
//        panel.add(searchPanel, BorderLayout.NORTH);
//
//        // Table for student marks
//        DefaultTableModel tableModel = new DefaultTableModel(
//                new Object[]{"Roll Number", "Name", "Marks", "Actions"}, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return column == 2; // Only marks column is editable
//            }
//        };
//
//        JTable studentTable = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(studentTable);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        // Save button
//        JButton saveButton = new JButton("Save All Marks");
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttonPanel.add(saveButton);
//        panel.add(buttonPanel, BorderLayout.SOUTH);
//
//        // Search button action
//        searchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String courseCode = (String) courseComboBox.getSelectedItem();
//                int semester = (int) semesterComboBox.getSelectedItem();
//
//                // Clear the table
//                tableModel.setRowCount(0);
//
//                try {
//                    // Get enrollments for the course
//                    List<Enrollment> enrollments = facultyController.getEnrollmentsByCourse(courseCode);
//
//                    // Filter for the selected semester
//                    for (Enrollment enrollment : enrollments) {
//                        if (enrollment.getSemester() == semester) {
//                            // In a real app, you'd fetch the student name from the database
//                            tableModel.addRow(new Object[]{
//                                    enrollment.getRollNumber(),
//                                    "Student Name", // This would come from the database
//                                    enrollment.getMarks(),
//                                    "Update"
//                            });
//                        }
//                    }
//                } catch (SQLException ex) {
//                    JOptionPane.showMessageDialog(panel, "Error loading enrollments: " + ex.getMessage(),
//                            "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        // Save button action
//        saveButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String courseCode = (String) courseComboBox.getSelectedItem();
//                int semester = (int) semesterComboBox.getSelectedItem();
//
//                // Save marks for each student
//                for (int i = 0; i < tableModel.getRowCount(); i++) {
//                    String rollNumber = (String) tableModel.getValueAt(i, 0);
//                    Object marksObj = tableModel.getValueAt(i, 2);
//
//                    if (marksObj != null && !marksObj.toString().isEmpty()) {
//                        try {
//                            double marks = Double.parseDouble(marksObj.toString());
//                            facultyController.enterMarks(rollNumber, courseCode, semester, marks);
//                        } catch (NumberFormatException ex) {
//                            JOptionPane.showMessageDialog(panel,
//                                    "Invalid marks format for student " + rollNumber,
//                                    "Error", JOptionPane.ERROR_MESSAGE);
//                        } catch (SQLException ex) {
//                            JOptionPane.showMessageDialog(panel,
//                                    "Error saving marks: " + ex.getMessage(),
//                                    "Error", JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                }
//
//                JOptionPane.showMessageDialog(panel, "Marks saved successfully!",
//                        "Success", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        return panel;
//    }

    // In FacultyDashboard.java, update the createEnterMarksPanel method:
    private JPanel createEnterMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel courseLabel = new JLabel("Course:");
        final JComboBox<String> courseComboBox = new JComboBox<>();

        JLabel semesterLabel = new JLabel("Semester:");
        final JComboBox<Integer> semesterComboBox = new JComboBox<>();
        semesterComboBox.addItem(1);
        semesterComboBox.addItem(2);

        JButton searchButton = new JButton("Search");

        searchPanel.add(courseLabel);
        searchPanel.add(courseComboBox);
        searchPanel.add(semesterLabel);
        searchPanel.add(semesterComboBox);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Load courses for the faculty
        try {
            List<Course> courses = facultyController.getAllCourses();
            for (Course course : courses) {
                courseComboBox.addItem(course.getCourseCode());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(panel,
                    "Error loading courses: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Table for student marks
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Roll Number", "Name", "Marks", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only marks column is editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Double.class; // Marks are doubles
                }
                return String.class;
            }
        };

        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Save button
        JButton saveButton = new JButton("Save All Marks");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = (String) courseComboBox.getSelectedItem();
                int semester = (int) semesterComboBox.getSelectedItem();

                if (courseCode == null) {
                    JOptionPane.showMessageDialog(panel,
                            "Please select a course",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Clear the table
                tableModel.setRowCount(0);

                try {
                    // Get enrollments for the course and semester
                    List<Enrollment> enrollments = facultyController.getEnrollmentsByCourseAndSemester(
                            courseCode, semester);

                    for (Enrollment enrollment : enrollments) {
                        // Get student name
                        Student student = facultyController.getStudentByRollNumber(
                                enrollment.getRollNumber());

                        if (student != null) {
                            tableModel.addRow(new Object[]{
                                    enrollment.getRollNumber(),
                                    student.getName(),
                                    enrollment.getMarks(),
                                    "Update"
                            });
                        }
                    }

                    // If no enrollments found
                    if (tableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(panel,
                                "No students enrolled in this course for the selected semester",
                                "No Data", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error loading enrollments: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Save button action
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = (String) courseComboBox.getSelectedItem();
                int semester = (int) semesterComboBox.getSelectedItem();

                if (courseCode == null) {
                    JOptionPane.showMessageDialog(panel,
                            "Please select a course",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean hasErrors = false;
                StringBuilder errorMessage = new StringBuilder("Errors found:\n");

                // Save marks for each student
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String rollNumber = (String) tableModel.getValueAt(i, 0);
                    Object marksObj = tableModel.getValueAt(i, 2);

                    if (marksObj != null) {
                        try {
                            Double marks;
                            if (marksObj instanceof String) {
                                marks = Double.parseDouble((String) marksObj);
                            } else {
                                marks = (Double) marksObj;
                            }

                            // Validate marks (0-100)
                            if (marks < 0 || marks > 100) {
                                hasErrors = true;
                                errorMessage.append("Invalid marks for student ")
                                        .append(rollNumber)
                                        .append(" (must be between 0 and 100)\n");
                                continue;
                            }

                            facultyController.enterMarks(rollNumber, courseCode, semester, marks);
                        } catch (NumberFormatException ex) {
                            hasErrors = true;
                            errorMessage.append("Invalid marks format for student ")
                                    .append(rollNumber)
                                    .append("\n");
                        } catch (SQLException ex) {
                            hasErrors = true;
                            errorMessage.append("Error saving marks for ")
                                    .append(rollNumber)
                                    .append(": ")
                                    .append(ex.getMessage())
                                    .append("\n");
                        }
                    }
                }

                if (hasErrors) {
                    JOptionPane.showMessageDialog(panel,
                            errorMessage.toString(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "All marks saved successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Refresh the view
                    searchButton.doClick();
                }
            }
        });

        return panel;
    }

    // Continuing FacultyDashboard.java
    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel semesterLabel = new JLabel("Semester:");
        JComboBox<Integer> semesterComboBox = new JComboBox<>();
        semesterComboBox.addItem(1);
        semesterComboBox.addItem(2);

        JButton searchButton = new JButton("Search");

        searchPanel.add(semesterLabel);
        searchPanel.add(semesterComboBox);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Table for students
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Roll Number", "Name", "Email", "Address"}, 0);

        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int semester = (int) semesterComboBox.getSelectedItem();

                // Clear the table
                tableModel.setRowCount(0);

                try {
                    // Get students for the semester
                    List<Student> students = facultyController.getStudentsBySemester(semester);

                    for (Student student : students) {
                        tableModel.addRow(new Object[]{
                                student.getRollNumber(),
                                student.getName(),
                                student.getEmail(),
                                student.getAddress()
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error loading students: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}