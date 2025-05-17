package com.langcen.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import com.langcen.model.StudentModel;
import com.langcen.model.ProgramModel;
import com.langcen.config.DbConfig;
import com.langcen.util.ValidationUtil;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/register")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1 MB
    maxFileSize = 1024 * 1024 * 5,    // 5 MB
    maxRequestSize = 1024 * 1024 * 10  // 10 MB
)
public class RegisterController extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "resources/images/students";
    private static final String CURRENT_USER = "Shaurya-BikramShah";
    private static final String CURRENT_TIME = "2025-04-29 17:39:49";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("currentTime", CURRENT_TIME);
        request.setAttribute("currentUser", CURRENT_USER);
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<String> errors = new ArrayList<>();
        
        try {
            // Set attributes
            request.setAttribute("currentTime", CURRENT_TIME);
            request.setAttribute("currentUser", CURRENT_USER);

            // Get form data
            String fullName = request.getParameter("fullName");
            String userName = request.getParameter("userName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String programName = request.getParameter("program");

            // Preserve form data
            request.setAttribute("fullName", fullName);
            request.setAttribute("userName", userName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("program", programName);

            // Basic validations
            if (fullName == null || fullName.trim().isEmpty()) {
                errors.add("Full name is required");
            }
            if (userName == null || userName.trim().isEmpty()) {
                errors.add("Username is required");
            }
            if (email == null || email.trim().isEmpty()) {
                errors.add("Email is required");
            }
            if (phone == null || phone.trim().isEmpty()) {
                errors.add("Phone number is required");
            }
            if (password == null || password.trim().isEmpty()) {
                errors.add("Password is required");
            }
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                errors.add("Confirm password is required");
            }
            if (programName == null || programName.trim().isEmpty()) {
                errors.add("Please select a program");
            }

            // If any required field is missing, return to form
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Format validations
            if (!ValidationUtil.validateFullName(fullName)) {
                errors.add("Full name must be 3-50 characters long and contain only letters and spaces");
            }

            if (!ValidationUtil.validateUsername(userName)) {
                errors.add("Username must be at least 7 characters long and contain only letters, numbers, and underscores");
            }

            if (!ValidationUtil.validateEmail(email)) {
                errors.add("Please enter a valid email address");
            }

            if (!ValidationUtil.validatePhone(phone)) {
                errors.add("Phone number must start with + and be exactly 14 characters long");
            }

            if (!ValidationUtil.validatePassword(password)) {
                errors.add("Password must be at least 7 characters long and include uppercase, lowercase, number and special character");
            }

            if (!ValidationUtil.validateConfirmPassword(password, confirmPassword)) {
                errors.add("Passwords do not match");
            }

            // If there are validation errors, return to form
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Check for duplicates
            if (DbConfig.isUsernameExists(userName)) {
                errors.add("Username '" + userName + "' is already taken");
            }
            if (DbConfig.isEmailExists(email)) {
                errors.add("Email '" + email + "' is already registered");
            }
            if (DbConfig.isPhoneNumberExists(phone)) {
                errors.add("Phone number '" + phone + "' is already registered");
            }

            // If there are duplicate errors, return to form
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Handle file upload
            String imageUrl = null;
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                if (filePart.getSize() > 5242880) {
                    errors.add("File size must not exceed 5MB");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    return;
                }

                String contentType = filePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    errors.add("Only image files are allowed");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    return;
                }

                String fileName = System.currentTimeMillis() + "_" + userName + "_" + 
                                getSubmittedFileName(filePart);
                String uploadPath = getServletContext().getRealPath("") + File.separator + 
                                  UPLOAD_DIRECTORY;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                filePart.write(uploadPath + File.separator + fileName);
                imageUrl = UPLOAD_DIRECTORY + "/" + fileName;
            }

            // Create and save student
            ProgramModel program = new ProgramModel(programName);
            StudentModel student = new StudentModel(fullName, userName, email, phone, 
                                                  password, program, imageUrl);
            
            int result = DbConfig.addStudent(student);
            
            if (result > 0) {
                response.sendRedirect(request.getContextPath() + 
                    "/login.jsp?registered=true&username=" + userName);
                return;
            } else {
                errors.add("Registration failed. Please try again.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add("An error occurred during registration. Please try again.");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                             .substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}