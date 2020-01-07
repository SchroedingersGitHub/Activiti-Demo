<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>请假管理</title>

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
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">请假申请列表</div>
    <a href="${pageContext.request.contextPath}/leaveBill/details">添加请假申请</a>
    <table border="1" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <th>ID</th>
            <th>请假人</th>
            <th>请假天数</th>
            <th>请假事由</th>
            <th>请假备注</th>
            <th>请假时间</th>
            <th>请假状态</th>
            <th>操作</th>
        </tr>

        <c:forEach items="${requestScope.leaveBills}" var="leaveBill">
            <tr>
                <td>${leaveBill.id}</td>
                <td>${leaveBill.employee.name}</td>
                <td>${leaveBill.days}</td>
                <td>${leaveBill.content}</td>
                <td>${leaveBill.remark}</td>
                <td>
                    <fmt:formatDate value="${leaveBill.leavedate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    <c:if test="${leaveBill.state == 0}">
                        初始录入
                    </c:if>
                    <c:if test="${leaveBill.state  == 1}">
                        审核中
                    </c:if>
                    <c:if test="${leaveBill.state == 2}">
                        审核完成
                    </c:if>
                </td>
                <td>
                    <c:if test="${leaveBill.state == 0}">
                        <a href="${pageContext.request.contextPath}/leaveBill/details?id=${leaveBill.id}">修改</a>
                        <a href="${pageContext.request.contextPath}/leaveBill/delete?id=${leaveBill.id}">删除</a>
                        <a href="${pageContext.request.contextPath}/workflow/startProcess?id=${leaveBill.id}">提交</a>
                    </c:if>
                    <c:if test="${leaveBill.state  == 1}">
                        <a href="${pageContext.request.contextPath}/workflow/viewHisComment?id=${leaveBill.id}">查看审核记录</a>
                    </c:if>
                    <c:if test="${leaveBill.state == 2}">
                        <a href="${pageContext.request.contextPath}/leaveBill/delete?id=${leaveBill.id}">删除</a>
                        <a href="${pageContext.request.contextPath}/workflow/viewHisComment?id=${leaveBill.id}">查看审核记录</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>


</body>
</html>
