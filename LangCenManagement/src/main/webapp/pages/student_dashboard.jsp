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

            <!-- Alerts -->
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

            <!-- Update Profile Form -->
            <div class="form-container">
                <h2>Update Information</h2>
                <form action="${pageContext.request.contextPath}/student-dashboard" method="post">
                    <input type="text" name="fullName" placeholder="Full Name" value="${studentToUpdate.fullName}" required>
                    <input type="text" name="username" placeholder="Username" value="${studentToUpdate.username}" required>
                    <input type="email" name="email" placeholder="Email" value="${studentToUpdate.email}" required>
                    <input type="password" name="password" placeholder="Password" required>
                    <input type="text" name="phone" placeholder="Phone Number" value="${studentToUpdate.phone}" required>

                    <input type="hidden" name="roleId" value="2">
                    <div class="button-group">
                        <button type="submit" name="action" value="update" class="btn-action">Update</button>
                        <button type="reset" class="btn-clear">Clear</button>
                        <button type="submit" name="action" value="delete" class="btn-danger">Delete</button>
                    </div>
                </form>

                <!-- View All Users Button -->
                <form method="post" action="${pageContext.request.contextPath}/student-dashboard" style="margin-top: 10px;">
                    <input type="hidden" name="action" value="view">
                    <button type="submit" class="btn-action">View All Users</button>
                </form>
            </div>

            <!-- Table Section -->
            <div class="table-container">
                <h2>User Information</h2>

                <div class="table-actions" style="display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 15px;">
                    <input type="text" id="searchInput" class="search-bar" placeholder="Search..." onkeyup="searchTable()" />
                    <button class="btn-action" onclick="sortTable(0)">Sort by Student ID</button>
                    <button class="btn-action" onclick="sortTable(5)">Sort by Program</button>
                </div>

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
                        <!-- Display all students if available -->
                        <c:if test="${not empty allStudents}">
                            <c:forEach var="s" items="${allStudents}">
                                <tr>
                                    <td>${s.studentId}</td>
                                    <td>${s.fullName}</td>
                                    <td>${s.username}</td>
                                    <td>${s.email}</td>
                                    <td>${s.phone}</td>
                                    <td>${s.programName}</td>
                                </tr>
                            </c:forEach>
                        </c:if>

                        <!-- Otherwise show the logged-in student -->
                        <c:if test="${empty allStudents && not empty student}">
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

    <!-- JavaScript Sorting & Searching -->
    <script>
    let sortDirections = {};

    function searchTable() {
        const input = document.getElementById("searchInput").value.toUpperCase();
        const table = document.querySelector(".table-container table");
        const tr = table.getElementsByTagName("tr");
        for (let i = 1; i < tr.length; i++) {
            const tds = tr[i].getElementsByTagName("td");
            let rowContainsSearch = false;
            for (let j = 0; j < tds.length; j++) {
                if (tds[j] && tds[j].textContent.toUpperCase().includes(input)) {
                    rowContainsSearch = true;
                    break;
                }
            }
            tr[i].style.display = rowContainsSearch ? "" : "none";
        }
    }

    function sortTable(columnIndex) {
        const table = document.querySelector(".table-container table");
        const rows = Array.from(table.rows).slice(1);
        sortDirections[columnIndex] = !sortDirections[columnIndex];
        const isAscending = sortDirections[columnIndex];
        const isNumericColumn = columnIndex === 0;

        const sortedRows = rows.sort((a, b) => {
            const cellA = a.cells[columnIndex].textContent.trim();
            const cellB = b.cells[columnIndex].textContent.trim();

            if (isNumericColumn) {
                return isAscending
                    ? parseFloat(cellA) - parseFloat(cellB)
                    : parseFloat(cellB) - parseFloat(cellA);
            } else {
                return isAscending
                    ? cellA.localeCompare(cellB)
                    : cellB.localeCompare(cellA);
            }
        });

        sortedRows.forEach(row => table.appendChild(row));
    }
    </script>
</body>
</html>
