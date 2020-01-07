<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div style=" width: 600px ;margin: 100px auto;text-align: center;">
    <h2>登录</h2>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <select style="width: 300px;height: 30px" id="userSelect" name="name">
            <c:forEach items="${requestScope.employeeList}" var="employee">
                <option value="${employee.name}">${employee.name}</option>
            </c:forEach>
        </select>
        <input type="submit" value="登录"/>
    </form>

</div>
</body>
</html>
