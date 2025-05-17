package com.langcen.controller;

import com.langcen.config.DbConfig;
import com.langcen.util.PasswordUtil;
import com.langcen.util.CookieUtil;
import com.langcen.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is already logged in via session or cookies
        HttpSession session = request.getSession(false);
        if (session != null && "admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/pages/admin_dashboard.jsp");
            return;
        }

        String usernameCookie = CookieUtil.getCookie(request, "username");
        String roleCookie = CookieUtil.getCookie(request, "role");

        if (usernameCookie != null && "admin".equals(roleCookie)) {
            response.sendRedirect(request.getContextPath() + "/pages/admin_dashboard.jsp");
            return;
        }

        // Forward to the login page
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate inputs
        if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
            handleError(request, response, "Username and password cannot be empty.");
            return;
        }

        try (Connection conn = DbConfig.getConnection()) {
            // Query to get user details
            String query = "SELECT password, role_id, status FROM student_profile WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                int roleId = rs.getInt("role_id");
                String status = rs.getString("status");

                // Check if account is active
                if (!"active".equalsIgnoreCase(status)) {
                    handleError(request, response, "Your account is inactive. Please contact support.");
                    return;
                }

                // Validate the password using PasswordUtil
                boolean isPasswordValid = PasswordUtil.validatePassword(password, storedHash);
                if (isPasswordValid) {
                    // Create session and store user details
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("role", roleId == 1 ? "admin" : "student");

                    // Store session in SessionUtil
                    SessionUtil.storeSession(session);

                    // Create cookies to store user details
                    CookieUtil.addCookie(response, "username", username, 3600); // 1-hour lifespan
                    CookieUtil.addCookie(response, "role", roleId == 1 ? "admin" : "student", 3600);

                    // Redirect based on role
                    if (roleId == 1) { // Admin
                        response.sendRedirect(request.getContextPath() + "/pages/admin_dashboard.jsp");
                    } else { // Student
                        response.sendRedirect(request.getContextPath() + "/pages/student_dashboard.jsp");
                    }
                } else {
                    // Incorrect password
                    handleError(request, response, "Incorrect password. Please try again.");
                }
            } else {
                // Username not found
                handleError(request, response, "Username not found. Please register first.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError(request, response, "An unexpected error occurred. Please try again later.");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}