<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>    

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/registerSuccess.css">
<title>Insert title here</title>
</head>

<body>
	<div id="navigation">
		<img alt="drawSomething" src="picture/nav.png">
	</div>
	<div id="registerSuccess">
		<h1>注册成功</h1>
		<h2>你的账号为：${requestScope.newUserId}</h2>
		<h2>点击<a href="index.jsp">这里</a>登录</h2>
	</div>
</body>
</html>