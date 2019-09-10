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
<title>Insert title here</title>
</head>

<body>
<div id="changePageContent_twousers">

<div id="userList_waitingPage">
	
	<table>
	<tr>

	<td>
		<div class="profilePicture">
		<img id="profilePicture1" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
		</div>
		<span id="userInfo1">userInfo1</span><!-- userName+score -->
		
	</td>
	<td>
		<div class="profilePicture">
		<img id="profilePicture2" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
		</div>
		<span id="userInfo2">userInfo2</span>
	</td>
	</tr>
    <!--   
	<tr class="userTextInfo">
		<td>
		
		</td>
		<td>
		
		</td>
	</tr>
	 --> 
	</table>
	
	<div id="exitQueue">
	<button class="exitQueue" title="返回游戏大厅" onclick="gotoGameLobby()">退出队列</button>
	</div>
	<!-- 该方法需要在gameroom.js中补充 -->

</div>

</div>
 
</body>

</html>