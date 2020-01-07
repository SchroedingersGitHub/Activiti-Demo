<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>流程图</title>
</head>
<body>

<%--获取到规则的流程图--%>
<img style="position: absolute;top: 0;left: 0;" src="${pageContext.request.contextPath}
/workflow/viewImage?deploymentId=${deploymentId}&imageName=${ImageName}"/>


<%--根据当前活动的坐标,动态绘制DIV--%>
<div style="position: absolute;border: 1px solid red;  top: ${map.y}px;left: ${map.x}px;width: ${map.width}px; height: ${map.height}px"></div>

</body>
</html>
