// src/main/java/com/sarms/Main.java
package com.sarms;

import com.sarms.controller.AdminController;
import com.sarms.controller.AuthController;
import com.sarms.controller.FacultyController;
import com.sarms.controller.StudentController;
import com.sarms.dao.CourseDAO;
import com.sarms.dao.UserDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.SemesterRecordDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.dao.impl.CourseDAOImpl;
import com.sarms.dao.impl.UserDAOImpl;
import com.sarms.dao.impl.EnrollmentDAOImpl;
import com.sarms.dao.impl.SemesterRecordDAOImpl;
import com.sarms.dao.impl.StudentDAOImpl;
import com.sarms.service.AuthenticationService;
import com.sarms.service.CourseService;
import com.sarms.service.EnrollmentService;
import com.sarms.service.GradingService;
import com.sarms.service.ReportService;
import com.sarms.service.StudentService;
import com.sarms.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Setup database (in a real application, this would be done by DB migration scripts)
        // setupDatabase();

        // Initialize DAOs
        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
        SemesterRecordDAO semesterRecordDAO = new SemesterRecordDAOImpl();
        UserDAO userDAO = new UserDAOImpl();


        // Initialize services
        // Initialize services
        AuthenticationService authService = new AuthenticationService(studentDAO, userDAO);
        StudentService studentService = new StudentService(studentDAO);
        CourseService courseService = new CourseService(courseDAO);
        EnrollmentService enrollmentService = new EnrollmentService(enrollmentDAO, studentDAO, courseDAO);
        GradingService gradingService = new GradingService(enrollmentDAO, courseDAO, semesterRecordDAO, studentDAO);
        ReportService reportService = new ReportService(studentDAO, enrollmentDAO, courseDAO, semesterRecordDAO, gradingService);

        // Initialize controllers
        AuthController authController = new AuthController(authService);
        StudentController studentController = new StudentController(studentService, enrollmentService, gradingService, reportService);
        FacultyController facultyController = new FacultyController(enrollmentService, studentService, gradingService);
        AdminController adminController = new AdminController(courseService, studentService, gradingService);

        // Initialize and show the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set look and feel to the system's look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainFrame mainFrame = new MainFrame(authController, studentController, facultyController, adminController);
                mainFrame.setVisible(true);
            }
        });
    }

    private static void setupDatabase() {
        // In a real application, you would run the SQL scripts to create the database and tables
        System.out.println("Setting up database...");

        try {
            // Create tables here or run SQL scripts
            // This is just a placeholder
            System.out.println("Database setup complete.");
        } catch (Exception e) {
            System.err.println("Error setting up database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}