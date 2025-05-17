package com.langcen.service;

import com.langcen.config.AdminDbConfig;
import com.langcen.model.StudentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {

    public List<StudentModel> getAllStudents() {
        List<StudentModel> students = new ArrayList<>();
        String sql = "SELECT sp.student_id, sp.username, sp.password, sp.full_name, sp.email, sp.phone, sp.profile_picture, sp.role_id, " +
                     "p.name AS program_name " +
                     "FROM student_profile sp " +
                     "LEFT JOIN student_enrollment se ON sp.student_id = se.student_id " +
                     "LEFT JOIN program p ON se.program_id = p.program_id";

        try (Connection conn = AdminDbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StudentModel student = new StudentModel(
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    null, // program is optional
                    rs.getString("profile_picture")
                );
                student.setRoleId(rs.getInt("role_id"));
                student.setStudentId(rs.getInt("student_id"));
                student.setProgramName(rs.getString("program_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging
        }
        return students;
    }

    public boolean addStudent(StudentModel student) {
        String sql = "INSERT INTO student_profile (username, password, full_name, email, phone, profile_picture, role_id, status, created_at, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'active', NOW(), 'admin-panel')";
        try (Connection conn = AdminDbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getFullName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getProfilePicture());
            stmt.setInt(7, student.getRoleId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(StudentModel student) {
        String sql = "UPDATE student_profile SET password = ?, full_name = ?, email = ?, phone = ?, profile_picture = ?, role_id = ? " +
                     "WHERE username = ?";
        try (Connection conn = AdminDbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getPassword());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getProfilePicture());
            stmt.setInt(6, student.getRoleId());
            stmt.setString(7, student.getUsername());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String username) {
        String selectIdSql = "SELECT student_id FROM student_profile WHERE username = ?";
        String deleteEnrollmentSql = "DELETE FROM student_enrollment WHERE student_id = ?";
        String deleteProfileSql = "DELETE FROM student_profile WHERE student_id = ?";

        try (Connection conn = AdminDbConfig.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            int studentId = -1;

            try (PreparedStatement selectStmt = conn.prepareStatement(selectIdSql)) {
                selectStmt.setString(1, username);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        studentId = rs.getInt("student_id");
                    } else {
                        return false; // No student with that username
                    }
                }
            }

            try (PreparedStatement deleteEnrollStmt = conn.prepareStatement(deleteEnrollmentSql)) {
                deleteEnrollStmt.setInt(1, studentId);
                deleteEnrollStmt.executeUpdate();
            }

            try (PreparedStatement deleteProfileStmt = conn.prepareStatement(deleteProfileSql)) {
                deleteProfileStmt.setInt(1, studentId);
                deleteProfileStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ New method for StudentDashboardController
    public StudentModel getStudentByUsername(String username) {
        String sql = "SELECT sp.student_id, sp.username, sp.password, sp.full_name, sp.email, sp.phone, sp.profile_picture, sp.role_id, " +
                     "p.name AS program_name " +
                     "FROM student_profile sp " +
                     "LEFT JOIN student_enrollment se ON sp.student_id = se.student_id " +
                     "LEFT JOIN program p ON se.program_id = p.program_id " +
                     "WHERE sp.username = ?";

        try (Connection conn = AdminDbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StudentModel student = new StudentModel(
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        null,
                        rs.getString("profile_picture")
                    );
                    student.setStudentId(rs.getInt("student_id"));
                    student.setRoleId(rs.getInt("role_id"));
                    student.setProgramName(rs.getString("program_name"));
                    return student;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
