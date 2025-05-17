package com.langcen.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    // Regular expressions for validation
    private static final Pattern FULL_NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,50}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{7,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+[0-9]{13}$");
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$");

    public static boolean validateFullName(String fullName) {
        return fullName != null && FULL_NAME_PATTERN.matcher(fullName).matches();
    }

    public static boolean validateUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validatePhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean validatePassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean validateConfirmPassword(String password, String confirmPassword) {
        return password != null && confirmPassword != null && password.equals(confirmPassword);
    }
}