// src/main/java/com/sarms/dao/SemesterRecordDAO.java
package com.sarms.dao;

import com.sarms.model.SemesterRecord;
import java.sql.SQLException;
import java.util.List;

public interface SemesterRecordDAO {
    void save(SemesterRecord record) throws SQLException;
    SemesterRecord find(String rollNumber, int semester) throws SQLException;
    List<SemesterRecord> findByStudent(String rollNumber) throws SQLException;
    List<SemesterRecord> findByVCList(boolean onVCList) throws SQLException;
    List<SemesterRecord> findByConditionalStanding(boolean onConditionalStanding) throws SQLException;
    void update(SemesterRecord record) throws SQLException;
    void delete(String rollNumber, int semester) throws SQLException;
}