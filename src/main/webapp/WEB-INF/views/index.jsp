<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <style type="text/css">
        a {
            text-decoration: none;
            color: black;
        }
    </style>
</head>
<body>
<div style=" width: 600px;margin: 100px auto 20px auto ">
    <div style="text-align: right">当前登录人:${sessionScope.user.name} <a
            href="${pageContext.request.contextPath}/logout"><span style="color: red">退出登录</span></a></div>
    <div>
        <button><a href="${pageContext.request.contextPath}/workflow/deploy" target="deployIframe">部署管理</a></button>
        <button><a href="${pageContext.request.contextPath}/leaveBill/home" target="deployIframe">请假管理</a></button>
        <button><a href="${pageContext.request.contextPath}/workflow/listTask" target="deployIframe">任务管理</a></button>
    </div>
</div>
<div style="text-align: center">
    <iframe name="deployIframe" width=100% height=100% src="${pageContext.request.contextPath}/main" scrolling="auto " frameborder="0 "></iframe>
</div>
</body>
</html>
