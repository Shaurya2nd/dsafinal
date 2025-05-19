package com.langcen.util;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public class SessionUtil {
    private static final Map<String, HttpSession> sessionMap = new HashMap<>();

    // Store session
    public static void storeSession(HttpSession session) {
        sessionMap.put(session.getId(), session);
    }

    // Retrieve session by ID
    public static HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    // Invalidate session
    public static void invalidateSession(String sessionId) {
        HttpSession session = sessionMap.get(sessionId);
        if (session != null) {
            session.invalidate();
            sessionMap.remove(sessionId);
        }
    }
}