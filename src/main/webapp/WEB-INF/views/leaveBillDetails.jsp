<%--
  Created by IntelliJ IDEA.
  User: lilei
  Date: 2019-10-25
  Time: 17:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>请假详情页</title>

</head>
<body>
<div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">新增/修改请假单申请</div>
<form action="${pageContext.request.contextPath}/leaveBill/save" method="post">
    请假天数:&nbsp;&nbsp;<input type="number" name="days" value="${echoLeaveBill.days}"><br>
    请假原因:&nbsp;&nbsp;<input type="text" name="content" value="${echoLeaveBill.content}"><br>
    备注:&nbsp;&nbsp;<textarea
        name="remark"
        style="border:0;border-radius:5px;background-color:rgba(241,241,241,.98);width: 355px;height: 100px;padding: 10px;resize: none;"
        placeholder="备注信息">${echoLeaveBill.remark}</textarea><br>
    <input type="hidden" value="${sessionScope.user.id}" name="employee.id">
    <input type="hidden" value="${echoLeaveBill.id}" name="id">
    <input type="submit" value="提交" style="width: 100px ;height: 200px">
</form>
</body>
</html>
