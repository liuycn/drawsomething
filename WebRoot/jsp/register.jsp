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
<link rel="stylesheet" type="text/css" href="css/register.css">
<title>Insert title here</title>
</head>

<body>
	<div id="navigation">
		<img alt="drawSomething" src="picture/nav.png">
	</div>
	<div>
	<form id="register" action="user/register" method="post" onsubmit="return checkValue();">
		<div class="tableRow">
			<p>
			<input id="userName" class="input" type="text" name="userName" placeholder="名称" onChange="checkUserName()">
			</p>
		</div>
		<div class="tableRow">
			<p>
			<input id="pwd1" class="input" type="password" name="userPwd" placeholder="密码" onChange="checkUserPwd()">  
			</p>
		</div>
		<div class="tableRow">
			<p>
			<input id="pwd2" class="input" type="password"  placeholder="确认密码" onChange="checkPwd()">  
			</p>
		</div>
		<span id="promptMessage"></span>
		<div>
		    <input type="submit" class="button" value="注册">
		</div>   
	</form>
	</div>
</body>
<script type="text/javascript">
function checkValue(){
	alert("检测");
	if(checkUserName()&&checkUserPwd()&&checkPwd()){
		alert("检测true");
		return true;
	}else{
		document.getElementById("promptMessage").innerHTML="请确保各项数值合法";
		return false;
	}
}
function checkUserName(){
	var userName=document.getElementById("userName").value;
	//alert("userName.length"+userName.length);
	if(userName.length>10||userName.length<1){
		document.getElementById("promptMessage").innerHTML="名字不要太长哦(不超过10位)~";
		return false;
	}else{
		document.getElementById("promptMessage").innerHTML="";
		return true;
	}
}
function checkUserPwd(){
	var userName=document.getElementById("pwd1").value;
	if(userName.length>10||userName.length<6){
		document.getElementById("promptMessage").innerHTML="请将密码控制在6-10位";
		return false;  
	}else{
		document.getElementById("promptMessage").innerHTML="";
		return true;
	}
}
function checkPwd(){
	var pwd1=document.getElementById("pwd1").value;
	var pwd2=document.getElementById("pwd2").value;
	if(pwd1==pwd2){
		document.getElementById("promptMessage").innerHTML="";
		return true;
	}else{
		document.getElementById("promptMessage").innerHTML="请确保密码一致性";
		return false;
	}
}
</script>
</html>