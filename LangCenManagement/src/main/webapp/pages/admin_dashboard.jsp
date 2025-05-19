<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css" />
</head>
<body>
<div class="dashboard-container">
    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Admin Dashboard</h2>
        <nav>
            <ul>
                <li><a href="#" class="active">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <h1>Manage Students</h1>

        <!-- Display Validation Errors -->
        <c:if test="${not empty errors}">
            <div class="alert alert-danger">
                <ul>
                    <c:forEach var="error" items="${errors}">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <!-- Display Success or Error Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <!-- Manage Student Form -->
        <div class="form-container">
            <h2>Manage Student</h2>
            <form action="${pageContext.request.contextPath}/admin-dashboard" method="post">
                <!-- Hidden studentId for update/delete -->
                <input type="hidden" name="studentId" value="${studentToUpdate.studentId}" />

                <input type="text" name="fullName" placeholder="Full Name" value="${studentToUpdate.fullName}" required />
                <input type="text" name="username" placeholder="Username" value="${studentToUpdate.username}" required />
                <input type="email" name="email" placeholder="Email" value="${studentToUpdate.email}" required />

                <!-- Password is required only on add -->
                <input type="password" name="password" placeholder="Password"
                       <c:if test="${param.action == 'add' || empty studentToUpdate.studentId}">required</c:if> autocomplete="off" />

                <input type="text" name="phone" placeholder="Phone Number" value="${studentToUpdate.phone}" required />

<select name="programName" required>
    <option value="" disabled ${studentToUpdate.programName == null ? "selected" : ""}>Select Program</option>
    <option value="English" ${studentToUpdate.programName == 'English' ? "selected" : ""}>English</option>
    <option value="Japanese" ${studentToUpdate.programName == 'Japanese' ? "selected" : ""}>Japanese</option>
    <option value="Chinese" ${studentToUpdate.programName == 'Chinese' ? "selected" : ""}>Chinese</option>
    <option value="Nepali" ${studentToUpdate.programName == 'Nepali' ? "selected" : ""}>Nepali</option>
    <option value="Korean" ${studentToUpdate.programName == 'Korean' ? "selected" : ""}>Korean</option>
</select>

                <input type="hidden" name="roleId" value="2" /> <!-- Default role for students -->

                <div class="button-group">
                    <button type="submit" name="action" value="add" class="btn-action">Add</button>
                    <button type="submit" name="action" value="update" class="btn-action">Update</button>
                    <button type="reset" class="btn-clear">Clear</button>
                    <button type="submit" name="action" value="delete" class="btn-danger"
                            onclick="return confirm('Are you sure you want to delete this student?');">Delete</button>
                </div>
            </form>
        </div>

        <!-- Student List Table -->
        <div class="table-container">
            <h2>Student List</h2>
            <table>
                <thead>
                <tr>
                    <th>Student ID</th>
                    <th>Name</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Program</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="student" items="${students}">
                    <tr>
                        <td>${student.studentId}</td>
                        <td>${student.fullName}</td>
                        <td>${student.username}</td>
                        <td>${student.email}</td>
                        <td>${student.phone}</td>
                        <td>${student.programName}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin-dashboard" method="post" class="action-form">
                                <input type="hidden" name="studentId" value="${student.studentId}" />
                                <input type="hidden" name="fullName" value="${student.fullName}" />
                                <input type="hidden" name="username" value="${student.username}" />
                                <input type="hidden" name="email" value="${student.email}" />
                                <input type="hidden" name="phone" value="${student.phone}" />
                                <input type="hidden" name="programName" value="${student.programName}" />

                                <button type="submit" name="action" value="update" class="btn-action">Update</button>
                                <button type="submit" name="action" value="delete" class="btn-danger"
                                        onclick="return confirm('Are you sure you want to delete this student?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
