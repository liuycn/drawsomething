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
<title>Information management</title>
</head>
<body>

	<!-- 页眉 -->
	<div id="header">
		<div id="preUserInfo">
		你好,${sessionScope.userId}${sessionScope.userName}
		<a href="user/logout">退出</a>
    	</div>
		<h1>你画我猜词库管理</h1>
	</div>
	<!-- 页眉结束 -->

	<!-- 导航 -->
    <div id="navigation">
	    <h2>
	    <a href="game/loadingAddSubjectPage" title="插入新的词语">添加新词</a><br>
	    </h2>
	    <h2>
	    <a href="game/loadingdedeleteSubPage/1" title="删除已有词语">删除词语</a>
	    </h2>
    </div>  
	<!-- 导航结束 -->
	
</body>
</html>