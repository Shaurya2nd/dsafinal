<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student.css">
</head>
<body>
    <div class="dashboard-container">
        <div class="sidebar">
            <h2>User Dashboard</h2>
            <nav>
                <ul>
                    <li><a href="#" class="active">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </div>

        <div class="main-content">
            <h1>Manage Your Profile</h1>

            <c:if test="${not empty errors}">
                <div class="alert alert-danger">
                    <ul>
                        <c:forEach var="error" items="${errors}">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>

            <div class="form-container">
                <h2>Update Information</h2>
                <form action="${pageContext.request.contextPath}/student-dashboard" method="post">
                    <input type="text" name="fullName" placeholder="Full Name" value="${studentToUpdate.fullName}" required>
                    <input type="text" name="username" placeholder="Username" value="${studentToUpdate.username}" required>
                    <input type="email" name="email" placeholder="Email" value="${studentToUpdate.email}" required>
                    <input type="password" name="password" placeholder="Password" required>
                    <input type="text" name="phone" placeholder="Phone Number" value="${studentToUpdate.phone}" required>
                    <input type="text" name="program" placeholder="Program" value="${studentToUpdate.programName}" required>
                    <input type="hidden" name="roleId" value="2">
                    <div class="button-group">
                        <button type="submit" name="action" value="update" class="btn-action">Update</button>
                        <button type="reset" class="btn-clear">Clear</button>
                        <button type="submit" name="action" value="delete" class="btn-danger">Delete</button>
                    </div>
                </form>
            </div>

            <div class="table-container">
                <h2>Your Information</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Student ID</th>
                            <th>Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Phone Number</th>
                            <th>Program</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${not empty student}">
                            <tr>
                                <td>${student.studentId}</td>
                                <td>${student.fullName}</td>
                                <td>${student.username}</td>
                                <td>${student.email}</td>
                                <td>${student.phone}</td>
                                <td>${student.programName}</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
