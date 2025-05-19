<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - LangCen</title>
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
                    <li><a href="${pageContext.request.contextPath}/pages/contact.jsp">Contact</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <div class="about-section">
            <div class="image-section">
                <img src="${pageContext.request.contextPath}/resources/images/system/lang.png" alt="LangCen Team" class="team-image">
                <div class="caption">
                    <h2>A pre-arrival welcome to new students</h2>
                    <p><em>In the Language Centre</em></p>
                </div>
            </div>
            <div class="content-section">
                <div class="container">
                    <h3>About LangCen</h3>
                    <p>LangCen is a premier language learning center dedicated to helping students master new languages and embrace cultural diversity. We provide a supportive and inclusive environment where learners of all levels can thrive.</p>
                    <p>Our mission is to make language learning accessible, engaging, and effective for everyone. Through expert-designed courses, interactive tools, and a vibrant community, LangCen empowers students to achieve fluency and build connections across the globe.</p>
                    
                    <h3>What We Offer</h3>
                    <ul class="offers-list">
                        <li><strong>Interactive Courses:</strong> Our courses combine traditional teaching methods with modern technology to create an immersive learning experience.</li>
                        <li><strong>Expert Instructors:</strong> Learn from certified language professionals who bring years of experience and passion to every class.</li>
                        <li><strong>Cultural Exchange:</strong> Engage with a global community of learners and participate in cultural exchange programs to deepen your understanding.</li>
                        <li><strong>Personalized Learning Plans:</strong> Tailor your language journey to fit your goals and schedule with our adaptable learning plans.</li>
                    </ul>
                    
                    <h3>Our Vision</h3>
                    <p>At LangCen, we believe that language is more than just a tool for communication—it’s a gateway to understanding, collaboration, and cultural appreciation. Our vision is to create a world where language barriers no longer limit opportunities or connections.</p>
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