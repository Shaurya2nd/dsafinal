<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LangCen - Language Learning Center</title>
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
                    <li><a href="${pageContext.request.contextPath}/pages/about.jsp">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/pages/contact.jsp">Contact</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Register</a></li>
                </ul>
            </nav>
        </div>
    </header>
    
    <section class="hero">
        <div class="container">
            <div class="hero-content">
                <h2>Learn Languages the Smart Way</h2>
                <p>Expand your horizons with our expert language instructors and interactive learning methods.</p>
                <a href="${pageContext.request.contextPath}/courses" class="btn btn-large">Explore Courses</a>
            </div>
        </div>
    </section>
    
    <section class="features">
        <div class="container">
            <h2>Why Choose LangCen?</h2>
            <div class="feature-grid">
                <div class="feature">
                    <h3>Expert Instructors</h3>
                    <p>Learn from certified language professionals with years of teaching experience.</p>
                </div>
                <div class="feature">
                    <h3>Interactive Learning</h3>
                    <p>Engage with our interactive learning platform designed to make language acquisition fun and effective.</p>
                </div>
                <div class="feature">
                    <h3>Flexible Schedule</h3>
                    <p>Choose courses that fit your schedule and learn at your own pace.</p>
                </div>
                <div class="feature">
                    <h3>Multiple Languages</h3>
                    <p>From Spanish to Mandarin, we offer courses in multiple languages to suit your interests.</p>
                </div>
            </div>
        </div>
    </section>
    
    <section class="cta">
        <div class="container">
            <h2>Ready to Start Your Language Journey?</h2>
            <p>Join thousands of satisfied learners who have achieved fluency through our programs.</p>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-large">Register Now</a>
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
                        <li><a href="${pageContext.request.contextPath}/pages/about.jsp">About</a></li>
                        <li><a href="${pageContext.request.contextPath}/pages/contact.jsp">Contact</a></li>
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