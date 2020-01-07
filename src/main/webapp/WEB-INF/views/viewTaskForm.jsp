<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>任务表单</title>
</head>
<body>


<div>
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">请假申请的任务办理</div>
    <form action="${pageContext.request.contextPath}/workflow/submitTask" method="post">
        <input type="hidden" name="taskId" value="${taskId}"> <%--任务ID--%>
        <input type="hidden" name="id" value="${leaveBill.id}"> <%--请假单ID--%>
        请假天数:&nbsp;&nbsp;<input type="number" disabled name="days" value="${leaveBill.days}"><br>
        请假原因:&nbsp;&nbsp;<input type="text" disabled name="content" value="${leaveBill.content}"><br>
        备注:&nbsp;&nbsp;<textarea
            disabled
            name="remark"
            style="border:0;border-radius:5px;background-color:rgba(241,241,241,.98);width: 355px;height: 100px;padding: 10px;resize: none;"
            placeholder="备注信息">${leaveBill.remark}</textarea><br>
        批注:&nbsp;&nbsp;<textarea
            name="comment"
            style="border:0;border-radius:5px;background-color:rgba(241,241,241,.98);width: 355px;height: 100px;padding: 10px;resize: none;"
            placeholder="请输入批注信息:"></textarea><br>
        <c:forEach items="${outcomes}" var="outcome">
            <input type="submit" name="outcome" value="${outcome}">
        </c:forEach>
    </form>
</div>

<div>
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">请假申请的批注信息</div>
    <c:if test="${comments != null && comments.size() >0}">
        <table border="1" cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <th>时间</th>
                <th>批注人</th>
                <th>批注信息</th>
            </tr>
            <c:forEach items="${comments}" var="comment">
                <tr>
                    <td>
                        <fmt:formatDate value="${comment.time}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td>${comment.userId}</td>
                    <td>${comment.fullMessage}</td>
                </tr>
            </c:forEach>

        </table>
    </c:if>
    <c:if test="${comments == null || comments.size() <=0}">
        <h4>没有批注信息</h4>
    </c:if>

</div>

</body>
</html>
