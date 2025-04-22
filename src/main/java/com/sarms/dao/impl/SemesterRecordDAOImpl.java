// src/main/java/com/sarms/dao/impl/SemesterRecordDAOImpl.java
package com.sarms.dao.impl;

import com.sarms.dao.SemesterRecordDAO;
import com.sarms.model.SemesterRecord;
import com.sarms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SemesterRecordDAOImpl implements SemesterRecordDAO {

    @Override
    public void save(SemesterRecord record) throws SQLException {
        String query = "INSERT INTO semester_records (roll_number, semester, swa, on_vc_list, on_conditional_standing) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, record.getRollNumber());
            stmt.setInt(2, record.getSemester());
            stmt.setDouble(3, record.getSwa());
            stmt.setBoolean(4, record.isOnVCList());
            stmt.setBoolean(5, record.isOnConditionalStanding());

            stmt.executeUpdate();
        }
    }

    @Override
    public SemesterRecord find(String rollNumber, int semester) throws SQLException {
        String query = "SELECT * FROM semester_records WHERE roll_number = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.setInt(2, semester);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSemesterRecord(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<SemesterRecord> findByStudent(String rollNumber) throws SQLException {
        List<SemesterRecord> records = new ArrayList<>();
        String query = "SELECT * FROM semester_records WHERE roll_number = ? ORDER BY semester";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToSemesterRecord(rs));
                }
            }
        }

        return records;
    }

    @Override
    public List<SemesterRecord> findByVCList(boolean onVCList) throws SQLException {
        List<SemesterRecord> records = new ArrayList<>();
        String query = "SELECT * FROM semester_records WHERE on_vc_list = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, onVCList);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToSemesterRecord(rs));
                }
            }
        }

        return records;
    }

    @Override
    public List<SemesterRecord> findByConditionalStanding(boolean onConditionalStanding) throws SQLException {
        List<SemesterRecord> records = new ArrayList<>();
        String query = "SELECT * FROM semester_records WHERE on_conditional_standing = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, onConditionalStanding);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToSemesterRecord(rs));
                }
            }
        }

        return records;
    }

    @Override
    public void update(SemesterRecord record) throws SQLException {
        String query = "UPDATE semester_records SET swa = ?, on_vc_list = ?, on_conditional_standing = ? " +
                "WHERE roll_number = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, record.getSwa());
            stmt.setBoolean(2, record.isOnVCList());
            stmt.setBoolean(3, record.isOnConditionalStanding());
            stmt.setString(4, record.getRollNumber());
            stmt.setInt(5, record.getSemester());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String rollNumber, int semester) throws SQLException {
        String query = "DELETE FROM semester_records WHERE roll_number = ? AND semester = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            stmt.setInt(2, semester);

            stmt.executeUpdate();
        }
    }

    private SemesterRecord mapResultSetToSemesterRecord(ResultSet rs) throws SQLException {
        SemesterRecord record = new SemesterRecord();
        record.setRollNumber(rs.getString("roll_number"));
        record.setSemester(rs.getInt("semester"));
        record.setSwa(rs.getDouble("swa"));
        record.setOnVCList(rs.getBoolean("on_vc_list"));
        record.setOnConditionalStanding(rs.getBoolean("on_conditional_standing"));

        return record;
    }
}