// src/main/java/com/sarms/service/ReportService.java
package com.sarms.service;

import com.sarms.dao.CourseDAO;
import com.sarms.dao.EnrollmentDAO;
import com.sarms.dao.SemesterRecordDAO;
import com.sarms.dao.StudentDAO;
import com.sarms.model.Course;
import com.sarms.model.Enrollment;
import com.sarms.model.SemesterRecord;
import com.sarms.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final CourseDAO courseDAO;
    private final SemesterRecordDAO semesterRecordDAO;
    private final GradingService gradingService;

    public ReportService(StudentDAO studentDAO, EnrollmentDAO enrollmentDAO,
                         CourseDAO courseDAO, SemesterRecordDAO semesterRecordDAO,
                         GradingService gradingService) {
        this.studentDAO = studentDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.courseDAO = courseDAO;
        this.semesterRecordDAO = semesterRecordDAO;
        this.gradingService = gradingService;
    }

    public Map<String, Object> generateSemesterReport(String rollNumber, int semester) throws SQLException {
        Map<String, Object> report = new HashMap<>();

        // Get student details
        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Get enrollments and course details
        List<Enrollment> enrollments = enrollmentDAO.findByStudentAndSemester(rollNumber, semester);
        List<Map<String, Object>> courseDetails = new ArrayList<>();

        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            Map<String, Object> courseDetail = new HashMap<>();

            Course course = courseDAO.findByCourseCode(enrollment.getCourseCode());
            if (course == null) {
                continue;
            }

            courseDetail.put("courseCode", course.getCourseCode());
            courseDetail.put("title", course.getTitle());
            courseDetail.put("credits", course.getCredits());
            courseDetail.put("marks", enrollment.getMarks());

            totalCredits += course.getCredits();
            courseDetails.add(courseDetail);
        }

        // Calculate SWA if not already calculated
        SemesterRecord semesterRecord = semesterRecordDAO.find(rollNumber, semester);
        if (semesterRecord == null) {
            semesterRecord = gradingService.categorizeStudent(rollNumber, semester);
        }

        // Add all details to the report
        report.put("student", student);
        report.put("semester", semester);
        report.put("courses", courseDetails);
        report.put("totalCredits", totalCredits);
        report.put("swa", semesterRecord.getSwa());
        report.put("onVCList", semesterRecord.isOnVCList());
        report.put("onConditionalStanding", semesterRecord.isOnConditionalStanding());

        return report;
    }

    public String formatSemesterReport(Map<String, Object> reportData) {
        StringBuilder report = new StringBuilder();

        Student student = (Student) reportData.get("student");
        int semester = (int) reportData.get("semester");

        report.append("SEMESTER REPORT\n");
        report.append("==============\n\n");

        report.append("Student Details:\n");
        report.append("---------------\n");
        report.append("Roll Number: ").append(student.getRollNumber()).append("\n");
        report.append("Name: ").append(student.getName()).append("\n");
        report.append("Email: ").append(student.getEmail()).append("\n");
        report.append("Semester: ").append(semester).append("\n\n");

        report.append("Course Details:\n");
        report.append("--------------\n");
        report.append(String.format("%-15s %-40s %-8s %-8s\n",
                "Course Code", "Title", "Credits", "Marks"));
        report.append(String.format("%-15s %-40s %-8s %-8s\n",
                "------------", "-----", "-------", "-----"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> courses = (List<Map<String, Object>>) reportData.get("courses");

        for (Map<String, Object> course : courses) {
            report.append(String.format("%-15s %-40s %-8d %-8.2f\n",
                    course.get("courseCode"),
                    course.get("title"),
                    course.get("credits"),
                    course.get("marks")));
        }

        report.append("\nTotal Credits: ").append(reportData.get("totalCredits")).append("\n\n");

        report.append("Academic Standing:\n");
        report.append("-----------------\n");
        report.append("Semester Weighted Average (SWA): ").append(String.format("%.2f", reportData.get("swa"))).append("\n");

        boolean onVCList = (boolean) reportData.get("onVCList");
        boolean onConditionalStanding = (boolean) reportData.get("onConditionalStanding");

        if (onVCList) {
            report.append("Congratulations! You are on the Vice Chancellor's List.\n");
        }

        if (onConditionalStanding) {
            report.append("Warning: You are on Conditional Standing.\n");
        }

        return report.toString();
    }
}