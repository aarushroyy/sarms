// src/main/java/com/sarms/model/Course.java
package com.sarms.model;

import java.time.LocalDateTime;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String department;
    private String syllabus;
    private LocalDateTime createdAt;

    // Default constructor
    public Course() {
    }

    // Constructor with all fields except createdAt
    public Course(String courseCode, String title, int credits, String department, String syllabus) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.department = department;
        this.syllabus = syllabus;
    }

    // Getters and setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", title='" + title + '\'' +
                ", credits=" + credits +
                ", department='" + department + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}