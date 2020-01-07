<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>任务管理</title>

    <style type="text/css">
        table tr td {
            text-align: center;
        }

        a {
            text-decoration: none;
            color: #5d48dd;
        }
    </style>

</head>
<body>

<div>
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">个人任务列表</div>
    <table border="1" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>创建时间</th>
            <th>办理人</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${requestScope.tasks}" var="task">
            <tr>
                <td>${task.id}</td>
                <td>${task.name}</td>
                <td>
                    <fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>${task.assignee}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/workflow/viewTaskForm?id=${task.id}">办理任务</a>
                    <a target="_blank"
                       href="${pageContext.request.contextPath}
                       /workflow/viewCurrentImage?taskId=${task.id}">
                        查看当前流程图</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>
