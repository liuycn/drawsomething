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
<div id="changePageContent_painter">
<div id="drawingPage_painter">
	<!-- ---------------------用户列表-------------------------- -->
	<div id="userList">
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
	<!-- ---------------------绘图-------------------------- -->
	<div id="drawing_painter"> 
	
		<div id="gameInfo_painter"><!-- 主题信息 -->	
	    <!--  
	    <span class="gameInfo" id="gameSubject_painter">绘图：手机</span>
	    <span class="gameInfo" id="userStatus_painter">你的身份：绘图者</span>
	    -->
	    <span class="gameInfo" id="gameSubject_painter"></span>
	    <span class="gameInfo" id="userStatus_painter"></span>
		</div>
	
		<div id="drawingdiv_painter"><!-- 绘图界面开始 -->
		  <canvas id="drawingCanvas_painter" width="700" height="420">
		        换个浏览器试试吧……
		  </canvas>
		</div>
		<div id="timer">
		</div>
	    <div id="exitQueueDiv_painter">
    	<button id="exitQueue_painter" title="返回游戏大厅" onclick="gotoGameLobby()">退出</button>
    	</div>
    	
		<div id="Toolbar">

		  <div class="patternChoose">
			  <canvas id="red" class="colorChoose" title="红色" onclick="setColor(0)"> 
			  	红色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="orange" class="colorChoose" title="橙色" onclick="setColor(1)">
			            橙色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="yellow" class="colorChoose" title="黄色" onclick="setColor(2)">
			  	黄色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="green" class="colorChoose" title="绿色" onclick="setColor(3)">
			  	绿色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="teal" class="colorChoose" title="青色" onclick="setColor(4)">
			  	青色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="blue" class="colorChoose" title="蓝色" onclick="setColor(5)">
			  	蓝色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="purple" class="colorChoose" title="紫色" onclick="setColor(6)">
			  	紫色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="gray" class="colorChoose" title="灰色" onclick="setColor(7)">
			  	灰色	
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="black" class="colorChoose" title="黑色" onclick="setColor(8)">
			  	黑色
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="white" class="colorChoose" title="白色" onclick="setColor(9)"><!-- onclick="getPoint() "-->
			  	白色
			  </canvas>  
		  </div>
				<!--  
				<img id="red" width="20" height="20" alt="红色" src="" onclick="setColor('#FF0000')"/>
				-->
		  <div class="patternChoose">
			  <canvas id="thin_pen" class="thicknessChoose" title="笔触：细" width="25" height="25" onclick="setThickness(2)">
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="medium_pen" class="thicknessChoose" title="笔触：中" width="25" height="25" onclick="setThickness(6)">
			  </canvas>
		  </div>
		  <div class="patternChoose">
			  <canvas id="wide_pen" class="thicknessChoose" title="笔触：粗" width="25" height="25" onclick="setThickness(10)">
			  </canvas>
		  </div>
			  <!--  
			  <img alt="细" src="" onclick="setThickness(2)">
			  -->  
		  <div class="patternChoose">
			  <img alt="清屏" src="picture/clearCanvas.png" class="colorChoose" title="清屏" onclick="clearCanvas()">
		  </div>
			  <!-- <button onclick="clearCanvas()">清屏</button> -->
		  
		  <!--  获取drawpoint测试
		  <div>
		  <div id="pointTest" style="border:1px solid black">pointTest</div>
		  </div>
		  -->
		  
		</div> <!-- 绘图界面结束 -->
		
	</div>
	<!-- ---------------------绘图结束--------------------------- -->
  
  
</div>
</div>
</body>

<script type="text/javascript">

</script>
</html>