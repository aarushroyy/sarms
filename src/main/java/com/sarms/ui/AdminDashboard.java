// src/main/java/com/sarms/ui/AdminDashboard.java
package com.sarms.ui;

import com.sarms.controller.AdminController;
import com.sarms.model.Course;
import com.sarms.model.SemesterRecord;
import com.sarms.model.Student;
import com.sarms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JPanel {
    private final User user;
    private final AdminController adminController;
    private final MainFrame parent;

    private JTabbedPane tabbedPane;
    private JPanel studentsPanel;
    private JPanel coursesPanel;
    private JPanel reportsPanel;

    public AdminDashboard(User user, AdminController adminController, MainFrame parent) {
        this.user = user;
        this.adminController = adminController;
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
        studentsPanel = createStudentsPanel();
        coursesPanel = createCoursesPanel();
        reportsPanel = createReportsPanel();

        // Add tabs
        tabbedPane.addTab("Manage Students", studentsPanel);
        tabbedPane.addTab("Manage Courses", coursesPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // Add tabbed pane to the center
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getDisplayName() + " (Admin)");
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

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        panel.add(buttonsPanel, BorderLayout.NORTH);

        // Table for students
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Roll Number", "Name", "Email", "Address", "Semester"}, 0);

        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load students
        loadStudentsTable(tableModel);

        // Add student button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // In a real application, you would open a dialog to add a student
                JOptionPane.showMessageDialog(panel, "Add Student dialog would be shown here.",
                        "Add Student", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit student button action
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);
                    // In a real application, you would open a dialog to edit the student
                    JOptionPane.showMessageDialog(panel, "Edit Student dialog for " + rollNumber,
                            "Edit Student", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a student to edit.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Delete student button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String rollNumber = (String) tableModel.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(panel,
                            "Are you sure you want to delete student " + rollNumber + "?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            adminController.deleteStudent(rollNumber);
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(panel, "Student deleted successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(panel, "Error deleting student: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a student to delete.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void loadStudentsTable(DefaultTableModel tableModel) {
        // Clear the table
        tableModel.setRowCount(0);

        try {
            List<Student> students = adminController.getAllStudents();

            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getRollNumber(),
                        student.getName(),
                        student.getEmail(),
                        student.getAddress(),
                        student.getSemester()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Course");
        JButton editButton = new JButton("Edit Course");
        JButton deleteButton = new JButton("Delete Course");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        panel.add(buttonsPanel, BorderLayout.NORTH);

        // Table for courses
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Course Code", "Title", "Credits", "Department", "Syllabus"}, 0);

        JTable courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load courses
        loadCoursesTable(tableModel);

        // Add course button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // In a real application, you would open a dialog to add a course
                JOptionPane.showMessageDialog(panel, "Add Course dialog would be shown here.",
                        "Add Course", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Edit course button action
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
                    // In a real application, you would open a dialog to edit the course
                    JOptionPane.showMessageDialog(panel, "Edit Course dialog for " + courseCode,
                            "Edit Course", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a course to edit.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Delete course button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String courseCode = (String) tableModel.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(panel,
                            "Are you sure you want to delete course " + courseCode + "?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            adminController.deleteCourse(courseCode);
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(panel, "Course deleted successfully.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(panel, "Error deleting course: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a course to delete.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return panel;
    }

    private void loadCoursesTable(DefaultTableModel tableModel) {
        // Clear the table
        tableModel.setRowCount(0);

        try {
            List<Course> courses = adminController.getAllCourses();

            for (Course course : courses) {
                tableModel.addRow(new Object[]{
                        course.getCourseCode(),
                        course.getTitle(),
                        course.getCredits(),
                        course.getDepartment(),
                        course.getSyllabus() != null ? "Available" : "Not Available"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton vcListButton = new JButton("View VC List");
        JButton conditionalListButton = new JButton("View Conditional Standing");

        topPanel.add(vcListButton);
        topPanel.add(conditionalListButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table for students
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Roll Number", "Semester", "SWA", "Status"}, 0);

        JTable reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // VC List button action
        vcListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the table
                tableModel.setRowCount(0);

                try {
                    List<SemesterRecord> records = adminController.getVCListStudents();

                    for (SemesterRecord record : records) {
                        tableModel.addRow(new Object[]{
                                record.getRollNumber(),
                                record.getSemester(),
                                record.getSwa(),
                                "Vice Chancellor's List"
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error loading VC List: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Conditional Standing button action
        conditionalListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the table
                tableModel.setRowCount(0);

                try {
                    List<SemesterRecord> records = adminController.getConditionalStandingStudents();

                    for (SemesterRecord record : records) {
                        tableModel.addRow(new Object[]{
                                record.getRollNumber(),
                                record.getSemester(),
                                record.getSwa(),
                                "Conditional Standing"
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error loading Conditional Standing list: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}