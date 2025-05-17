<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - LangCen</title>
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
                    <li><a href="${pageContext.request.contextPath}/pages/about.jsp">About</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <div class="contact-section">
            <div class="image-section">
                <img src="${pageContext.request.contextPath}/resources/images/system/contact.png" alt="Welcome to LangCen" class="contact-image">
                <div class="caption">
                    <h2>We'd love to hear from you</h2>
                    <p><em>Get in touch with the Language Centre</em></p>
                </div>
            </div>
            <div class="content-section">
                <div class="container">
                    <h3>Contact Information</h3>
                    <p>If you have any questions, feedback, or need assistance, feel free to reach out to us. We're here to help!</p>
                    
                    <h4>Email</h4>
                    <p><a href="mailto:support@langcen.com">support@langcen.com</a></p>
                    
                    <h4>Phone</h4>
                    <p>+123-456-7890</p>
                    
                    <h4>Address</h4>
                    <p>123 Language Lane, Learning City, LC 45678</p>
                    
                    <h4>Follow Us</h4>
                    <ul class="social-links">
                        <li><a href="#">Facebook</a></li>
                        <li><a href="#">Twitter</a></li>
                        <li><a href="#">Instagram</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </main>
    <footer>
        <div class="container">
            <p>&copy; 2025 LangCen. All Rights Reserved.</p>
        </div>
    </footer>
</body>
</html>