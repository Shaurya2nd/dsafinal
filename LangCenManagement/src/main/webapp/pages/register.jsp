<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Registration - Language Center</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <header>
        <div class="container">
            <div class="logo">
                <h1>Language Center</h1>
            </div>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <section class="form-section">
        <div class="form-container">
            <h2>Student Registration</h2>
            <div class="system-info">
                <small>Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): 2025-04-29 18:59:15</small><br>
                <small>Current User's Login: Shaurya-BikramShah</small>
            </div>

            <!-- Display server-side error messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <ul>
                        <li>${error}</li>
                    </ul>
                </div>
            </c:if>

            <!-- Display multiple server-side errors -->
            <c:if test="${not empty errors}">
                <div class="alert alert-danger">
                    <ul>
                        <c:forEach items="${errors}" var="error">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <!-- Client-side error container -->
            <div id="errorMessages" class="alert alert-danger" style="display: none;">
                <ul></ul>
            </div>

            <form action="${pageContext.request.contextPath}/register-service" method="post"
                  enctype="multipart/form-data" id="registrationForm">

                <div class="form-group">
                    <label for="fullName">Full Name:</label>
                    <input type="text" id="fullName" name="fullName"
                           value="${fullName}" required pattern="[A-Za-z\s]{3,50}">
                    <small>3-50 characters, letters and spaces only</small>
                </div>

                <div class="form-group">
                    <label for="userName">Username:</label>
                    <input type="text" id="userName" name="userName"
                           value="${userName}" required pattern="[a-zA-Z0-9_]{7,}">
                    <small>At least 7 characters, letters, numbers, and underscores only</small>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email"
                           value="${email}" required>
                    <small>We'll never share your email with anyone else</small>
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number:</label>
                    <input type="tel" id="phone" name="phone"
                           value="${phone}" required pattern="\+[0-9]{13}">
                    <small>Format: +977 followed by 10 digits</small>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                    <small>At least 7 characters with uppercase, lowercase, number, and special character</small>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>

                <div class="form-group">
                    <label for="program">Program:</label>
                    <select id="program" name="program" required>
                        <option value="">Select a program</option>
                        <option value="English" ${program == 'English' ? 'selected' : ''}>English</option>
                        <option value="Japanese" ${program == 'Japanese' ? 'selected' : ''}>Japanese</option>
                        <option value="Nepali" ${program == 'Nepali' ? 'selected' : ''}>Nepali</option>
                        <option value="Korean" ${program == 'Korean' ? 'selected' : ''}>Korean</option>
                        <option value="Chinese" ${program == 'Chinese' ? 'selected' : ''}>Chinese</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="image">Profile Picture:</label>
                    <input type="file" id="image" name="image" accept="image/*">
                    <small>Maximum file size: 5MB. Supported formats: JPG, PNG, GIF</small>
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Register</button>
                </div>
            </form>

            <div class="form-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
            </div>
        </div>
    </section>

    <footer>
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>Contact Us</h3>
                    <ul>
                        <li>Email: info@langcen.edu.np</li>
                        <li>Phone: +977-01-4444444</li>
                        <li>Address: Kathmandu, Nepal</li>
                    </ul>
                </div>
            </div>
            <div class="copyright">
                <p>&copy; 2025 Language Center. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <!-- Client-Side Validation -->
    <script>
        document.getElementById('registrationForm').addEventListener('submit', function(e) {
            const errorMessages = [];
            const errorDiv = document.getElementById('errorMessages');
            const errorList = erroSrDiv.querySelector('ul');

            // Clear previous errors
            errorList.innerHTML = '';
            errorDiv.style.display = 'none';

            // Password match validation
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                errorMessages.push("Passwords do not match");
            }

            // If there are errors, prevent form submission and show errors
            if (errorMessages.length > 0) {
                e.preventDefault();
                errorMessages.forEach(message => {
                    const li = document.createElement('li');
                    li.textContent = message;
                    errorList.appendChild(li);
                });
                errorDiv.style.display = 'block';

                // Scroll to error messages
                errorDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    </script>
</body>
</html>