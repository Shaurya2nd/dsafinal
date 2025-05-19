package com.langcen.config;

import java.sql.*;
import com.langcen.model.StudentModel;
import com.langcen.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/langcen?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String CURRENT_USER = "Shaurya-BikramShah";
    private static final String CURRENT_TIME = "2025-04-29 16:18:17";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<String> checkDuplicates(String username, String email, String phone) {
        List<String> errors = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            // Check username
            String sql = "SELECT username FROM student_profile WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                errors.add("Username '" + username + "' is already taken. Please choose a different username.");
            }

            // Check email
            sql = "SELECT email FROM student_profile WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                errors.add("Email '" + email + "' is already registered. Please use a different email address.");
            }

            // Check phone
            sql = "SELECT phone FROM student_profile WHERE phone = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            if (rs.next()) {
                errors.add("Phone number '" + phone + "' is already registered. Please use a different phone number.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("Database error occurred. Please try again later.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return errors;
    }

    public static int addStudent(StudentModel student) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Insert into student_profile
            String sql = "INSERT INTO student_profile (username, password, full_name, " +
                        "email, phone, profile_picture, role_id, status, created_at, created_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 2, 'active', NOW(), ?)";
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getFullName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getProfilePicture());
            stmt.setString(7, CURRENT_USER);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int studentId = rs.getInt(1);
                    
                    // Get program ID
                    sql = "SELECT program_id FROM program WHERE name = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, student.getProgram().getName());
                    rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        int programId = rs.getInt("program_id");
                        
                        // Get beginner level ID
                        sql = "SELECT level_id FROM program_level WHERE program_id = ? AND level_code LIKE '%-B'";
                        stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, programId);
                        rs = stmt.executeQuery();
                        
                        if (rs.next()) {
                            int levelId = rs.getInt("level_id");
                            
                            // Insert enrollment
                            sql = "INSERT INTO student_enrollment (student_id, program_id, level_id, " +
                                  "enrollment_date, status, created_at, created_by) " +
                                  "VALUES (?, ?, ?, NOW(), 'active', NOW(), ?)";
                            
                            stmt = conn.prepareStatement(sql);
                            stmt.setInt(1, studentId);
                            stmt.setInt(2, programId);
                            stmt.setInt(3, levelId);
                            stmt.setString(4, CURRENT_USER);
                            
                            result = stmt.executeUpdate();
                            
                            if (result > 0) {
                                // Log the successful registration
                                sql = "INSERT INTO audit_log (table_name, record_id, action_type, " +
                                      "performed_by, performed_at) VALUES (?, ?, 'INSERT', ?, NOW())";
                                
                                stmt = conn.prepareStatement(sql);
                                stmt.setString(1, "student_profile");
                                stmt.setInt(2, studentId);
                                stmt.setString(3, CURRENT_USER);
                                stmt.executeUpdate();
                                
                                conn.commit();
                                return studentId;
                            }
                        }
                    }
                }
            }
            
            conn.rollback();
            return 0;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isUsernameExists(String username) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM student_profile WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isEmailExists(String email) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM student_profile WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isPhoneNumberExists(String phone) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM student_profile WHERE phone = ?")) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}