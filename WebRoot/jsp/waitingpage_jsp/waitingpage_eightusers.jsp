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
<div id="changePageContent_eightusers">

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
		<td>
			<div class="profilePicture">
			<img id="profilePicture3" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo3">userInfo3</span>
		</td>
		<td>
			<div class="profilePicture">
			<img id="profilePicture4" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo4">userInfo4</span>
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="profilePicture">
			<img id="profilePicture5" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo5">userInfo5</span><!-- userName+score -->
			
		</td>
		<td>
			<div class="profilePicture">
			<img id="profilePicture6" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo6">userInfo6</span>
		</td>
		<td>
			<div class="profilePicture">
			<img id="profilePicture7" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo7">userInfo7</span>
		</td>
		<td>
			<div class="profilePicture">
			<img id="profilePicture8" class="propic" alt="匹配中……" src="picture/waiting/waiting.gif">
			</div>
			<span id="userInfo8">userInfo8</span>
		</td>
	</tr>
	</table>
	
	
	<div id="exitQueue">
	<button class="exitQueue" title="返回游戏大厅" onclick="gotoGameLobby()">退出队列</button>
	</div>
	<!-- 该方法需要在gameroom.js中补充 -->

</div>

</div>
 
</body>

</html>