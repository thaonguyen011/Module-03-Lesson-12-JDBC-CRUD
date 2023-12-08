<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 07/12/2023
  Time: 07:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Manager Application</title>
</head>
<body>
<div style="text-align: center;">
  <h1>User Manager</h1>
  <h2>
    <a href="${pageContext.request.contextPath}/users?action=create">Add New User</a>
  </h2>
  <h2>
    <a href="${pageContext.request.contextPath}/users?action=search">Search User By Country</a>
  </h2>
  <h2>
    <a href="${pageContext.request.contextPath}/users?action=sort">Sort User List By Name</a>
  </h2>
</div>
<div  style="text-align: center;">
  <table style="border: 1px; cell-padding: 5">
    <caption><h2>List of Users</h2></caption>
    <tr>
      <th>ID</th>
      <th>Name</th>
      <th>Email</th>
      <th>Country</th>
      <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${userList}">
      <tr>
      <td><c:out value="${user.id}"/></td>
      <td><c:out value="${user.name}"/></td>
      <td><c:out value="${user.email}"/></td>
      <td><c:out value="${user.country}"/></td>
      <td>
        <a href="${pageContext.request.contextPath}/users?action=edit&&id=${user.id}">Edit</a>
        <a href="${pageContext.request.contextPath}/users?action=delete&&id=${user.id}">Delete</a>
      </td>
      </tr>
    </c:forEach>
  </table>
</div>
</body>
</html>
