package com.langcen.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    // Add a cookie
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // Make the cookie HTTP-only for security
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // Get a cookie by name
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Remove a cookie
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // Set to zero to delete the cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // Clear multiple cookies
    public static void clearCookies(HttpServletResponse response, String... cookieNames) {
        for (String name : cookieNames) {
            removeCookie(response, name);
        }
    }
}