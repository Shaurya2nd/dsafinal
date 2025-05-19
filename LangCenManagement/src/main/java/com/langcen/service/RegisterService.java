package com.langcen.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.langcen.model.StudentModel;
import com.langcen.model.ProgramModel;
import com.langcen.config.DbConfig;
import com.langcen.util.PasswordUtil;

import java.io.File;
import java.io.IOException;

@WebServlet("/register-service")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1 MB
    maxFileSize = 1024 * 1024 * 5,    // 5 MB
    maxRequestSize = 1024 * 1024 * 10  // 10 MB
)
public class RegisterService extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "resources/images/students";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validate form inputs
            String validationMessage = validateRegistrationForm(request);
            if (validationMessage != null) {
                handleError(request, response, validationMessage);
                return;
            }

            // Extract student data
            StudentModel studentModel = extractStudentModel(request);

            // Hash the password
            String plainPassword = request.getParameter("password");
            String hashedPassword = PasswordUtil.hashPassword(plainPassword); // Hash the password
            studentModel.setPassword(hashedPassword); // Store the hashed password

            // Add student to the database
            int result = DbConfig.addStudent(studentModel);

            if (result > 0) {
                handleSuccess(request, response, "Registration successful! Please log in.", "/pages/login.jsp");
            } else {
                handleError(request, response, "Registration failed. Please try again.");
            }
        } catch (Exception e) {
            handleError(request, response, "An unexpected error occurred. Please try again later!");
            e.printStackTrace();
        }
    }

    private String validateRegistrationForm(HttpServletRequest request) {
        String fullName = request.getParameter("fullName");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String programName = request.getParameter("program");

        if (isNullOrEmpty(fullName)) return "Full name is required.";
        if (isNullOrEmpty(userName)) return "Username is required.";
        if (isNullOrEmpty(email)) return "Email is required.";
        if (isNullOrEmpty(phone)) return "Phone number is required.";
        if (isNullOrEmpty(password)) return "Password is required.";
        if (isNullOrEmpty(programName)) return "Program is required.";

        if (DbConfig.isUsernameExists(userName)) return "Username already exists.";
        if (DbConfig.isEmailExists(email)) return "Email already exists.";
        if (DbConfig.isPhoneNumberExists(phone)) return "Phone number already exists.";

        return null;
    }

    private StudentModel extractStudentModel(HttpServletRequest request) throws IOException, ServletException {
        String fullName = request.getParameter("fullName");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String programName = request.getParameter("program");

        ProgramModel program = new ProgramModel(programName);

        // Handle image upload
        String imageUrl = null;
        Part imagePart = request.getPart("image");
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + userName + "_" + getSubmittedFileName(imagePart);
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            imagePart.write(uploadPath + File.separator + fileName);
            imageUrl = UPLOAD_DIRECTORY + "/" + fileName;
        }

        return new StudentModel(fullName, userName, email, phone, null, program, imageUrl);
    }

    private void handleSuccess(HttpServletRequest request, HttpServletResponse response, String message, String redirectPage)
            throws ServletException, IOException {
        request.setAttribute("success", message);
        request.getRequestDispatcher(redirectPage).forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        preserveFormData(request);
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    private void preserveFormData(HttpServletRequest request) {
        request.setAttribute("fullName", request.getParameter("fullName"));
        request.setAttribute("userName", request.getParameter("userName"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("phone", request.getParameter("phone"));
        request.setAttribute("program", request.getParameter("program"));
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}