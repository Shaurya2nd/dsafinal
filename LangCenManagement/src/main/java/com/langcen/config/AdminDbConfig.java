package com.langcen.config;

import java.sql.*;

public class AdminDbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/langcen?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getAllStudents() throws SQLException {
        Connection conn = getConnection();
        String sql = "SELECT * FROM student_profile";
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    // ======= DUPLICATE CHECKS FOR ADD =======
    public static boolean isUsernameExists(String username) {
        return checkDuplicate("username", username, null);
    }

    public static boolean isEmailExists(String email) {
        return checkDuplicate("email", email, null);
    }

    public static boolean isPhoneNumberExists(String phone) {
        return checkDuplicate("phone", phone, null);
    }

    // ======= DUPLICATE CHECKS FOR UPDATE (exclude the current username) =======
    public static boolean isUsernameExists(String username, String excludeUsername) {
        return checkDuplicate("username", username, excludeUsername);
    }

    public static boolean isEmailExists(String email, String excludeUsername) {
        return checkDuplicate("email", email, excludeUsername);
    }

    public static boolean isPhoneNumberExists(String phone, String excludeUsername) {
        return checkDuplicate("phone", phone, excludeUsername);
    }

    // ======= SHARED METHOD FOR DUPLICATE CHECKS =======
    private static boolean checkDuplicate(String column, String value, String excludeUsername) {
        String sql = "SELECT COUNT(*) FROM student_profile WHERE " + column + " = ?";
        if (excludeUsername != null) {
            sql += " AND username <> ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, value);
            if (excludeUsername != null) {
                stmt.setString(2, excludeUsername);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking for duplicate " + column, e);
        }
        return false;
    }
}