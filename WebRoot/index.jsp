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
<link rel="stylesheet" type="text/css" href="css/index.css">
<title>DrawSomething_你画我猜</title>
</head>

<body>
	<div id="title">
		<img alt="欢迎……" src="picture/title.png">
	</div>

	<form id="login" action="user/login" method="post">
		<div class="tableRow">
			<p>
			<input class="input" type="text" name="userId" placeholder="账号" required>
			</p>
		</div>
		<div class="tableRow">
			<p>
			<input class="input" type="password" name="userPwd" placeholder="密码" required>  
			</p>
		</div>

		<div class="buttonDiv">
		    <input type="submit" class="button" value="登录">
	    	<button type="button" class="button" onclick="register()">注册</button>	
		</div>   
	</form>
</body>

<script type="text/javascript">
function register(){
	window.location="loadingRegisterPage";
}
</script>
</html>