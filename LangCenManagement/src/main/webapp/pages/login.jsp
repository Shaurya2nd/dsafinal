<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - LangCen</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <div class="logo">
                <h1>LangCen</h1>
            </div>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/about">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Register</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <section class="form-section">
        <div class="container">
            <div class="form-container">
                <h2>Login to Your Account</h2>
                
                <% if (request.getParameter("registered") != null) { %>
                    <div class="alert alert-success">
                        Registration successful! Please login with your credentials.
                    </div>
                <% } %>
                
                <% if (request.getParameter("logout") != null) { %>
                    <div class="alert alert-info">
                        You have been logged out successfully.
                    </div>
                <% } %>
                
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                <% } %>
                
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group checkbox">
                        <input type="checkbox" id="rememberMe" name="rememberMe">
                        <label for="rememberMe">Remember me</label>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Login</button>
                    </div>
                    <div class="form-footer">
                        <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
                    </div>
                </form>
            </div>
        </div>
    </section>
    
    <footer>
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>LangCen</h3>
                    <p>Your premier destination for language learning excellence.</p>
                </div>
                <div class="footer-section">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/courses">Courses</a></li>
                        <li><a href="${pageContext.request.contextPath}/about">About</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Contact Us</h3>
                    <p>Email: info@langcen.com</p>
                    <p>Phone: +1 (555) 123-4567</p>
                </div>
            </div>
            <div class="copyright">
                <p>&copy; 2025 LangCen. All rights reserved.</p>
            </div>
        </div>
    </footer>
</body>
</html>