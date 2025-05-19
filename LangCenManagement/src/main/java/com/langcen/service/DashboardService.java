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
                    null,
                    rs.getString("profile_picture")
                );
                student.setRoleId(rs.getInt("role_id"));
                student.setStudentId(rs.getInt("student_id"));
                student.setProgramName(rs.getString("program_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean addStudent(StudentModel student) {
        String insertProfileSql = "INSERT INTO student_profile (username, password, full_name, email, phone, profile_picture, role_id, status, created_at, created_by) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, 'active', NOW(), 'admin-panel')";
        String getProgramIdSql = "SELECT program_id FROM program WHERE name = ?";
        // FIXED: use LOWER() for case-insensitive matching of level_code
        String getBeginnerLevelSql = "SELECT level_id FROM program_level WHERE program_id = ? AND LOWER(level_code) LIKE LOWER('%-B')";
        String insertEnrollmentSql = "INSERT INTO student_enrollment (student_id, program_id, level_id, enrollment_date, created_by) VALUES (?, ?, ?, NOW(), 'admin-panel')";

        try (Connection conn = AdminDbConfig.getConnection()) {
            conn.setAutoCommit(false);
            int studentId;

            // Insert into student_profile
            try (PreparedStatement stmt = conn.prepareStatement(insertProfileSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, student.getUsername());
                stmt.setString(2, student.getPassword());
                stmt.setString(3, student.getFullName());
                stmt.setString(4, student.getEmail());
                stmt.setString(5, student.getPhone());
                stmt.setString(6, student.getProfilePicture());
                stmt.setInt(7, student.getRoleId());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        studentId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Only insert enrollment if program name is provided
            String programName = student.getProgramName();
            if (programName != null && !programName.trim().isEmpty()) {
                int programId = -1;
                int levelId = -1;

                // Check if the program name is valid
                try (PreparedStatement stmt = conn.prepareStatement(getProgramIdSql)) {
                    stmt.setString(1, programName);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            programId = rs.getInt("program_id");
                        } else {
                            conn.rollback();
                            System.err.println("Invalid program name: " + programName);
                            return false; // Program not found
                        }
                    }
                }

                // Get beginner level (case-insensitive)
                try (PreparedStatement stmt = conn.prepareStatement(getBeginnerLevelSql)) {
                    stmt.setInt(1, programId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            levelId = rs.getInt("level_id");
                        } else {
                            conn.rollback();
                            System.err.println("No beginner level for program: " + programName);
                            return false; // No level found
                        }
                    }
                }

                // Insert into student_enrollment
                try (PreparedStatement stmt = conn.prepareStatement(insertEnrollmentSql)) {
                    stmt.setInt(1, studentId);
                    stmt.setInt(2, programId);
                    stmt.setInt(3, levelId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(StudentModel student) {
        String updateProfileSql = "UPDATE student_profile SET password = ?, full_name = ?, email = ?, phone = ?, profile_picture = ?, role_id = ? WHERE username = ?";
        String getStudentIdSql = "SELECT student_id FROM student_profile WHERE username = ?";
        String getProgramIdSql = "SELECT program_id FROM program WHERE name = ?";
        String updateEnrollmentSql = "UPDATE student_enrollment SET program_id = ? WHERE student_id = ?";

        try (Connection conn = AdminDbConfig.getConnection()) {
            conn.setAutoCommit(false);

            // Update student profile
            try (PreparedStatement stmt = conn.prepareStatement(updateProfileSql)) {
                stmt.setString(1, student.getPassword());
                stmt.setString(2, student.getFullName());
                stmt.setString(3, student.getEmail());
                stmt.setString(4, student.getPhone());
                stmt.setString(5, student.getProfilePicture());
                stmt.setInt(6, student.getRoleId());
                stmt.setString(7, student.getUsername());
                stmt.executeUpdate();
            }

            // Get student_id
            int studentId;
            try (PreparedStatement stmt = conn.prepareStatement(getStudentIdSql)) {
                stmt.setString(1, student.getUsername());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        studentId = rs.getInt("student_id");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Update program if program name is provided
            if (student.getProgramName() != null && !student.getProgramName().isEmpty()) {
                int programId;
                try (PreparedStatement stmt = conn.prepareStatement(getProgramIdSql)) {
                    stmt.setString(1, student.getProgramName());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            programId = rs.getInt("program_id");
                        } else {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(updateEnrollmentSql)) {
                    stmt.setInt(1, programId);
                    stmt.setInt(2, studentId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

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
            conn.setAutoCommit(false);
            int studentId = -1;

            try (PreparedStatement stmt = conn.prepareStatement(selectIdSql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        studentId = rs.getInt("student_id");
                    } else {
                        return false;
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteEnrollmentSql)) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteProfileSql)) {
                stmt.setInt(1, studentId);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
