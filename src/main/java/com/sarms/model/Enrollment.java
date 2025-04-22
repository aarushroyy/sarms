// src/main/java/com/sarms/model/Enrollment.java
package com.sarms.model;

public class Enrollment {
    private String rollNumber;
    private String courseCode;
    private int semester;
    private Double marks;

    // Default constructor
    public Enrollment() {
    }

    // Constructor with all fields
    public Enrollment(String rollNumber, String courseCode, int semester, Double marks) {
        this.rollNumber = rollNumber;
        this.courseCode = courseCode;
        this.semester = semester;
        this.marks = marks;
    }

    // Getters and setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "rollNumber='" + rollNumber + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", semester=" + semester +
                ", marks=" + marks +
                '}';
    }
}