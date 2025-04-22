// src/main/java/com/sarms/model/Student.java
package com.sarms.model;

import java.time.LocalDateTime;

public class Student {
    private String rollNumber;
    private String name;
    private String email;
    private String address;
    private int semester;
    private String password;
    private LocalDateTime createdAt;

    // Default constructor
    public Student() {
    }

    // Constructor with all fields except createdAt
    public Student(String rollNumber, String name, String email, String address,
                   int semester, String password) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
        this.address = address;
        this.semester = semester;
        this.password = password;
    }

    // Getters and setters
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNumber='" + rollNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", semester=" + semester +
                ", createdAt=" + createdAt +
                '}';
    }
}