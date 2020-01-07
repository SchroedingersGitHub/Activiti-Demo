<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>部署管理</title>

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
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">部署信息管理列表</div>
    <table border="1" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <th>ID</th>
            <th>流程名称</th>
            <th>发布时间</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${requestScope.deployments}" var="deployment">
            <tr>
                <td>${deployment.id}</td>
                <td>${deployment.name}</td>
                <td>
                    <fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td><a href="${pageContext.request.contextPath}/workflow/delDeployment?deploymentId=${deployment.id}">删除</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
<hr>
<div>
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">流程定义信息列表</div>
    <table border="1" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <th>ID</th>
            <th>名称</th>
            <th>流程定义的Key</th>
            <th>流程定义的版本</th>
            <th>流程定义的规则文件名称</th>
            <th>流程定义的规则图片名称</th>
            <th>部署ID</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${requestScope.processDefinitions}" var="processDefinition">
            <tr>
                <td>${processDefinition.id}</td>
                <td>${processDefinition.name}</td>
                <td>${processDefinition.key}</td>
                <td>${processDefinition.version}</td>
                <td>${processDefinition.resourceName}</td>
                <td>${processDefinition.diagramResourceName}</td>
                <td>${processDefinition.deploymentId}</td>
                <td>
                    <a target="_blank"
                       href="${pageContext.request.contextPath}
                       /workflow/viewImage?deploymentId=${processDefinition.deploymentId}&imageName=${processDefinition.diagramResourceName}">
                        查看流程图</a>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>
<hr>
<div>
    <div style="background:#000; color: aqua;padding:10px 10px 10px 10px; margin-bottom: 10px">部署流程定义</div>
    <form action="${pageContext.request.contextPath}/workflow/newDeploy" method="post" enctype="multipart/form-data">
        流程名称：<input type="text" name="name"><br>
        流程文件：<input type="file" name="file"><br>
        <input type="submit" value="上传流程" style="width: 100px; height: 50px">
    </form>
</div>
<hr>


</body>
</html>
