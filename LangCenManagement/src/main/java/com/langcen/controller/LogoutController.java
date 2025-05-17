package com.langcen.controller;

import com.langcen.util.CookieUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LogoutController", urlPatterns = "/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear cookies
        CookieUtil.removeCookie(response, "username");
        CookieUtil.removeCookie(response, "role");

        // Redirect to the login page
        response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
    }
}