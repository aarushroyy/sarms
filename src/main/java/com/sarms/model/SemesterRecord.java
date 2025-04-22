// src/main/java/com/sarms/model/SemesterRecord.java
package com.sarms.model;

public class SemesterRecord {
    private String rollNumber;
    private int semester;
    private double swa;
    private boolean onVCList;
    private boolean onConditionalStanding;

    // Default constructor
    public SemesterRecord() {
    }

    // Constructor with all fields
    public SemesterRecord(String rollNumber, int semester, double swa,
                          boolean onVCList, boolean onConditionalStanding) {
        this.rollNumber = rollNumber;
        this.semester = semester;
        this.swa = swa;
        this.onVCList = onVCList;
        this.onConditionalStanding = onConditionalStanding;
    }

    // Getters and setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getSwa() {
        return swa;
    }

    public void setSwa(double swa) {
        this.swa = swa;
    }

    public boolean isOnVCList() {
        return onVCList;
    }

    public void setOnVCList(boolean onVCList) {
        this.onVCList = onVCList;
    }

    public boolean isOnConditionalStanding() {
        return onConditionalStanding;
    }

    public void setOnConditionalStanding(boolean onConditionalStanding) {
        this.onConditionalStanding = onConditionalStanding;
    }

    @Override
    public String toString() {
        return "SemesterRecord{" +
                "rollNumber='" + rollNumber + '\'' +
                ", semester=" + semester +
                ", swa=" + swa +
                ", onVCList=" + onVCList +
                ", onConditionalStanding=" + onConditionalStanding +
                '}';
    }
}