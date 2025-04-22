// src/main/java/com/sarms/service/GradingService.java
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
import java.util.List;

public class GradingService {
    private final EnrollmentDAO enrollmentDAO;
    private final CourseDAO courseDAO;
    private final SemesterRecordDAO semesterRecordDAO;
    private final StudentDAO studentDAO;

    // Constant values for categorization
    private static final double VC_LIST_THRESHOLD = 85.0;
    private static final double CONDITIONAL_STANDING_THRESHOLD = 50.0;

    public GradingService(EnrollmentDAO enrollmentDAO, CourseDAO courseDAO,
                          SemesterRecordDAO semesterRecordDAO, StudentDAO studentDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.courseDAO = courseDAO;
        this.semesterRecordDAO = semesterRecordDAO;
        this.studentDAO = studentDAO;
    }

    public double calculateSWA(String rollNumber, int semester) throws SQLException {
        // Check if student exists
        Student student = studentDAO.findByRollNumber(rollNumber);
        if (student == null) {
            throw new IllegalArgumentException("Student with roll number " + rollNumber + " does not exist");
        }

        // Get all enrollments for the student in the given semester
        List<Enrollment> enrollments = enrollmentDAO.findByStudentAndSemester(rollNumber, semester);

        if (enrollments.isEmpty()) {
            throw new IllegalArgumentException("No enrollments found for this student in the given semester");
        }

        double totalWeightedMarks = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            // Skip courses without marks
            if (enrollment.getMarks() == null) {
                continue;
            }

            // Get course details to use credits as weight
            Course course = courseDAO.findByCourseCode(enrollment.getCourseCode());
            if (course == null) {
                // This should not happen if database integrity is maintained
                continue;
            }

            int credits = course.getCredits();
            totalWeightedMarks += enrollment.getMarks() * credits;
            totalCredits += credits;
        }

        // Avoid division by zero
        if (totalCredits == 0) {
            return 0.0;
        }

        // Calculate the weighted average
        return totalWeightedMarks / totalCredits;
    }

    public SemesterRecord categorizeStudent(String rollNumber, int semester) throws SQLException {
        // Calculate SWA
        double swa = calculateSWA(rollNumber, semester);

        // Create or update semester record
        SemesterRecord record = semesterRecordDAO.find(rollNumber, semester);

        if (record == null) {
            record = new SemesterRecord();
            record.setRollNumber(rollNumber);
            record.setSemester(semester);
        }

        record.setSwa(swa);

        // Categorize based on SWA
        record.setOnVCList(swa >= VC_LIST_THRESHOLD);
        record.setOnConditionalStanding(swa < CONDITIONAL_STANDING_THRESHOLD);

        // Save or update the record
        if (semesterRecordDAO.find(rollNumber, semester) == null) {
            semesterRecordDAO.save(record);
        } else {
            semesterRecordDAO.update(record);
        }

        return record;
    }

    public List<SemesterRecord> getStudentRecords(String rollNumber) throws SQLException {
        return semesterRecordDAO.findByStudent(rollNumber);
    }

    public SemesterRecord getSemesterRecord(String rollNumber, int semester) throws SQLException {
        return semesterRecordDAO.find(rollNumber, semester);
    }

    public List<SemesterRecord> getVCListStudents() throws SQLException {
        return semesterRecordDAO.findByVCList(true);
    }

    public List<SemesterRecord> getConditionalStandingStudents() throws SQLException {
        return semesterRecordDAO.findByConditionalStanding(true);
    }
}