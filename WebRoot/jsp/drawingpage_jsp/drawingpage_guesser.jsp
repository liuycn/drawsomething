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
<div id="changePageContent_guesser">

<div id="drawingPage_guesser">
	<!-- ---------------------用户列表-------------------------- -->
	<div id="userList"><!-- 与paint页面id相同 -->
		<!-- 
		<ul>
		<li>
		<img id="userpropic_userListPai" alt="用户头像" src="picture/profilePic.png">
		<br>
		<span id="userName_userListPai">userName</span>
		<span id="userScore_userListPai">score</span>
		</li>
		<br>
		</ul>
		 -->
	</div>
	<!-- ---------------------用户列表结束-------------------------- -->
	 <!-- ---------------------展示--------------------------- -->
	<div id="drawing_guesser"> 
	
		<div id="gameInfo_guesser"><!-- 主题信息 -->
	    <span class="gameInfo" id="gameSubject_guesser">一种动物</span>
	    <span class="gameInfo" id="userStatus_guesser">userStatus</span>
		</div>
	
		<div id="drawingdiv_guesser"><!-- 展示界面开始 -->
		  <canvas id="drawingCanvas_guesser" width="710" height="440">
		        换个浏览器试试吧……
		  </canvas>
		</div>
		
		<div id="timer">
		</div>
	    <div id="exitQueueDiv_guesser">
    	<button id="exitQueue_guesser" title="返回游戏大厅" onclick="gotoGameLobby()">退出</button>
    	</div>
    	
	    <!--  获取drawpoint测试 
	    <div>
	    <div id="pointTest" style="border:1px solid black">pointTest</div>
	    </div>
	    -->
	    
	    <!-- 展示界面完 -->
		
	</div>
	<!-- ---------------------展示界面完--------------------------- -->
  
  
</div>
</div>
</body>

<script type="text/javascript">

</script>
</html>