package com.langcen.controller;

import com.langcen.model.StudentModel;
import com.langcen.service.DashboardService;
import com.langcen.util.PasswordUtil;
import com.langcen.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/student-dashboard")
public class StudentDashboardController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StudentDashboardController.class.getName());
    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String loggedInUsername = (session != null) ? (String) session.getAttribute("username") : null;

        if (loggedInUsername == null) {
            response.sendRedirect("pages/login.jsp");
            return;
        }

        try {
            StudentModel student = dashboardService.getStudentByUsername(loggedInUsername);
            request.setAttribute("student", student);
            request.setAttribute("studentToUpdate", student);
            request.getRequestDispatcher("/pages/student_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching student data", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load your dashboard.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "update":
                    validateAndUpdateStudent(request, response);
                    break;
                case "delete":
                    deleteStudent(request, response);
                    break;
                default:
                    response.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing action: " + action, e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void validateAndUpdateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateInputs(request);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("studentToUpdate", extractStudentFromRequest(request));
            doGet(request, response);
            return;
        }

        StudentModel student = extractStudentFromRequest(request);
        boolean success = dashboardService.updateStudent(student);

        if (!success) {
            request.setAttribute("errorMessage", "Failed to update your profile.");
            request.setAttribute("studentToUpdate", student);
            doGet(request, response);
            return;
        }

        request.setAttribute("successMessage", "Profile updated successfully.");
        doGet(request, response);
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username == null || username.isEmpty()) {
            request.setAttribute("errorMessage", "Username is required to delete your profile.");
            doGet(request, response);
            return;
        }

        boolean success = dashboardService.deleteStudent(username);
        if (!success) {
            request.setAttribute("errorMessage", "Failed to delete your profile.");
            doGet(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // log out after deletion
        }

        response.sendRedirect("pages/login.jsp");
    }

    private List<String> validateInputs(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String roleId = request.getParameter("roleId");

        if (!ValidationUtil.validateFullName(fullName)) {
            errors.add("Full name must be 3-50 alphabetic characters.");
        }
        if (!ValidationUtil.validateUsername(username)) {
            errors.add("Username must be at least 7 characters long and include only letters, numbers, and underscores.");
        }
        if (!ValidationUtil.validateEmail(email)) {
            errors.add("Invalid email format.");
        }
        if (!ValidationUtil.validatePhone(phone)) {
            errors.add("Phone number must be in the format +XXXXXXXXXXXXX (13 digits).");
        }
        if (!ValidationUtil.validatePassword(password)) {
            errors.add("Password must include uppercase, lowercase, number, and special character.");
        }
        try {
            Integer.parseInt(roleId);
        } catch (NumberFormatException e) {
            errors.add("Role ID must be a valid integer.");
        }

        return errors;
    }

    private StudentModel extractStudentFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        String rawPassword = request.getParameter("password");
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String profilePicture = request.getParameter("profilePicture");
        int roleId = Integer.parseInt(request.getParameter("roleId"));

        StudentModel student = new StudentModel(fullName, username, email, phone, hashedPassword, null, profilePicture);
        student.setRoleId(roleId);
        return student;
    }
}
