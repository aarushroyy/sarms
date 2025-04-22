// src/main/java/com/sarms/ui/StudentDashboard.java
package com.sarms.ui;

import com.sarms.controller.StudentController;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.Student;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Point;

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

//    private JPanel createEnrollmentsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Table model for enrollments
//        DefaultTableModel tableModel = new DefaultTableModel(
//                new Object[]{"Course Code", "Title", "Credits", "Semester", "Marks"}, 0);
//
//        JTable enrollmentsTable = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        // Load data
//        try {
//            List<Enrollment> enrollments = studentController.getStudentEnrollments(user.getUsername());
//            for (Enrollment enrollment : enrollments) {
//                // In a real application, you would fetch course details from the database
//                // For simplicity, we're using placeholder values
//                tableModel.addRow(new Object[]{
//                        enrollment.getCourseCode(),
//                        "Course Title", // This would come from the database
//                        3, // Credits would come from the database
//                        enrollment.getSemester(),
//                        enrollment.getMarks() != null ? enrollment.getMarks() : "Not Graded"
//                });
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Error loading enrollments: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        return panel;
//    }

    // In StudentDashboard.java, update the createEnrollmentsPanel method:
    private JPanel createEnrollmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for semester selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel semesterLabel = new JLabel("Select Semester:");
        JComboBox<Integer> semesterComboBox = new JComboBox<>();

        try {
            // Get current student
            Student student = studentController.getStudentByRollNumber(user.getUsername());
            if (student != null) {
                // Add current semester and possibly previous semesters
                for (int i = 1; i <= student.getSemester(); i++) {
                    semesterComboBox.addItem(i);
                }
            } else {
                // Default to semesters 1 and 2 if student info not available
                semesterComboBox.addItem(1);
                semesterComboBox.addItem(2);
            }
        } catch (SQLException ex) {
            // Default semesters if error
            semesterComboBox.addItem(1);
            semesterComboBox.addItem(2);
        }

        JButton viewButton = new JButton("View Enrollments");

        topPanel.add(semesterLabel);
        topPanel.add(semesterComboBox);
        topPanel.add(viewButton);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table model for enrollments
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Course Code", "Title", "Credits", "Marks", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only actions column is editable
            }
        };

        JTable enrollmentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // View button action
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedSemester = (int) semesterComboBox.getSelectedItem();

                // Clear the table
                tableModel.setRowCount(0);

                try {
                    // Get enrollments for the selected semester
                    List<Enrollment> enrollments = studentController.getStudentSemesterEnrollments(
                            user.getUsername(), selectedSemester);

                    // Add to table
                    for (Enrollment enrollment : enrollments) {
                        // Get course details
                        Course course = studentController.getCourseByCourseCode(enrollment.getCourseCode());

                        if (course != null) {
                            tableModel.addRow(new Object[]{
                                    course.getCourseCode(),
                                    course.getTitle(),
                                    course.getCredits(),
                                    enrollment.getMarks() != null ? enrollment.getMarks() : "Not Graded",
                                    "Drop" // Action button
                            });
                        }
                    }

                    // Add button renderer/editor for the actions column
                    enrollmentsTable.getColumnModel().getColumn(4).setCellRenderer(
                            new ButtonRenderer("Drop"));
                    enrollmentsTable.getColumnModel().getColumn(4).setCellEditor(
                            new ButtonEditor(new JCheckBox(), "Drop"));

                    // If no enrollments
                    if (tableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(panel,
                                "No enrollments found for semester " + selectedSemester,
                                "No Data", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error loading enrollments: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Table button click handler
        enrollmentsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = enrollmentsTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / enrollmentsTable.getRowHeight();

                if (row < enrollmentsTable.getRowCount() && row >= 0 &&
                        column < enrollmentsTable.getColumnCount() && column >= 0) {
                    if (column == 4) { // Actions column
                        String courseCode = (String) tableModel.getValueAt(row, 0);
                        int selectedSemester = (int) semesterComboBox.getSelectedItem();

                        // Confirm before dropping
                        int confirm = JOptionPane.showConfirmDialog(
                                panel,
                                "Are you sure you want to drop " + courseCode + "?",
                                "Confirm Drop Course",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                studentController.dropCourse(
                                        user.getUsername(),
                                        courseCode,
                                        selectedSemester);

                                JOptionPane.showMessageDialog(
                                        panel,
                                        "Successfully dropped " + courseCode,
                                        "Drop Successful",
                                        JOptionPane.INFORMATION_MESSAGE);

                                // Refresh the table
                                viewButton.doClick();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(
                                        panel,
                                        "Error dropping course: " + ex.getMessage(),
                                        "Drop Failed",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        // Load enrollments for the first semester in the list
        if (semesterComboBox.getItemCount() > 0) {
            viewButton.doClick();
        }

        return panel;
    }

//    private JPanel createCoursesPanel() {
//        // In a real application, this would show available courses for enrollment
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel placeholderLabel = new JLabel("Available courses for enrollment would be shown here.");
//        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
//        panel.add(placeholderLabel, BorderLayout.CENTER);
//
//        return panel;
//    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table model for available courses
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Course Code", "Title", "Credits", "Department", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only actions column is editable
            }
        };

        JTable coursesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Top panel with semester selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel semesterLabel = new JLabel("Semester:");
        JComboBox<Integer> semesterComboBox = new JComboBox<>();

        // Add current semester and next semester
        int currentSemester = 1; // This should ideally come from the student object
        semesterComboBox.addItem(currentSemester);
        semesterComboBox.addItem(currentSemester + 1);

        JButton loadButton = new JButton("Load Available Courses");

        topPanel.add(semesterLabel);
        topPanel.add(semesterComboBox);
        topPanel.add(loadButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Load button action
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedSemester = (int) semesterComboBox.getSelectedItem();

                // Clear the table
                tableModel.setRowCount(0);

                try {
                    // Get all available courses (in a real app, this might be filtered)
                    List<Course> courses = studentController.getAllCourses();

                    // Get student's current enrollments to avoid showing already enrolled courses
                    List<Enrollment> enrollments = studentController.getStudentSemesterEnrollments(
                            user.getUsername(), selectedSemester);

                    // Create a set of course codes that student is already enrolled in
                    Set<String> enrolledCourses = new HashSet<>();
                    for (Enrollment enrollment : enrollments) {
                        enrolledCourses.add(enrollment.getCourseCode());
                    }

                    // Add courses to table if not already enrolled
                    for (Course course : courses) {
                        if (!enrolledCourses.contains(course.getCourseCode())) {
                            tableModel.addRow(new Object[]{
                                    course.getCourseCode(),
                                    course.getTitle(),
                                    course.getCredits(),
                                    course.getDepartment(),
                                    "Enroll"
                            });
                        }
                    }

                    // Add button renderer/editor for the actions column
                    coursesTable.getColumnModel().getColumn(4).setCellRenderer(
                            new ButtonRenderer("Enroll"));
                    coursesTable.getColumnModel().getColumn(4).setCellEditor(
                            new ButtonEditor(new JCheckBox(), "Enroll"));

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Error loading courses: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Table button click handler
        coursesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = coursesTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / coursesTable.getRowHeight();

                if (row < coursesTable.getRowCount() && row >= 0 &&
                        column < coursesTable.getColumnCount() && column >= 0) {
                    if (column == 4) { // Actions column
                        String courseCode = (String) tableModel.getValueAt(row, 0);
                        int selectedSemester = (int) semesterComboBox.getSelectedItem();

                        int confirm = JOptionPane.showConfirmDialog(
                                panel,
                                "Are you sure you want to enroll in " + courseCode + "?",
                                "Confirm Enrollment",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                studentController.enrollInCourse(
                                        user.getUsername(),
                                        courseCode,
                                        selectedSemester);

                                JOptionPane.showMessageDialog(
                                        panel,
                                        "Successfully enrolled in " + courseCode,
                                        "Enrollment Successful",
                                        JOptionPane.INFORMATION_MESSAGE);

                                // Refresh the table
                                loadButton.doClick();

                                // Refresh the enrollments panel
                                refreshEnrollmentsPanel();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(
                                        panel,
                                        "Error enrolling in course: " + ex.getMessage(),
                                        "Enrollment Failed",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        return panel;
    }

    // Add the helper method to refresh enrollments panel
    private void refreshEnrollmentsPanel() {
        // Implementation to refresh enrollments table
        // This would depend on how your enrollments panel is structured
    }

    // Add these helper classes for the button in table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private JButton button;

        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return button;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, String label) {
            super(checkBox);
            this.label = label;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

//    private JPanel createReportsPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//
//        JLabel semesterLabel = new JLabel("Select Semester:");
//        JComboBox<Integer> semesterComboBox = new JComboBox<>();
//        // Add semesters (in a real app, this would be populated from the database)
//        semesterComboBox.addItem(1);
//        semesterComboBox.addItem(2);
//
//        JButton generateButton = new JButton("Generate Report");
//
//        topPanel.add(semesterLabel);
//        topPanel.add(semesterComboBox);
//        topPanel.add(generateButton);
//
//        panel.add(topPanel, BorderLayout.NORTH);
//
//        // Text area for report
//        JTextArea reportTextArea = new JTextArea();
//        reportTextArea.setEditable(false);
//        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        JScrollPane scrollPane = new JScrollPane(reportTextArea);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        // Generate report button action
//        generateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    int semester = (int) semesterComboBox.getSelectedItem();
//                    Map<String, Object> reportData = studentController.generateSemesterReport(
//                            user.getUsername(), semester);
//                    String reportText = studentController.formatSemesterReport(reportData);
//                    reportTextArea.setText(reportText);
//                } catch (SQLException ex) {
//                    JOptionPane.showMessageDialog(panel, "Error generating report: " + ex.getMessage(),
//                            "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        return panel;
//    }
//}

    // In StudentDashboard.java, update the createReportsPanel method:
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel semesterLabel = new JLabel("Select Semester:");
        JComboBox<Integer> semesterComboBox = new JComboBox<>();

        try {
            // Get all semester records for the student
            List<SemesterRecord> records = studentController.getAllSemesterRecords(user.getUsername());
            Set<Integer> semesters = new HashSet<>();

            // Add all semesters from records
            for (SemesterRecord record : records) {
                semesters.add(record.getSemester());
            }

            // Add current semester if not in records
            Student student = studentController.getStudentByRollNumber(user.getUsername());
            if (student != null) {
                semesters.add(student.getSemester());
            }

            // Sort and add to combo box
            // In StudentDashboard.java, update the createReportsPanel method (continued):
            // Sort and add to combo box
            List<Integer> sortedSemesters = new ArrayList<>(semesters);
            Collections.sort(sortedSemesters);

            for (Integer semester : sortedSemesters) {
                semesterComboBox.addItem(semester);
            }

            // If no semesters available, add at least semester 1
            if (semesterComboBox.getItemCount() == 0) {
                semesterComboBox.addItem(1);
            }
        } catch (SQLException ex) {
            // If error, add default semesters
            semesterComboBox.addItem(1);
            semesterComboBox.addItem(2);
        }

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

// Export button
        JButton exportButton = new JButton("Export to Text File");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(exportButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        exportButton.setEnabled(false); // Initially disabled until report is generated

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
                    exportButton.setEnabled(true); // Enable export once report is generated
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error generating report: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

// Export button action
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Report");
                fileChooser.setSelectedFile(new File("semester_report.txt"));

                int userSelection = fileChooser.showSaveDialog(panel);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        writer.write(reportTextArea.getText());
                        JOptionPane.showMessageDialog(panel,
                                "Report exported successfully to " + fileToSave.getName(),
                                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(panel,
                                "Error exporting report: " + ex.getMessage(),
                                "Export Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return panel;
    }
}