<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
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
<link rel="stylesheet" type="text/css" href="css/gameroom.css">
<link rel="stylesheet" type="text/css" href="css/waitingpage_css/waitingpage_all.css">
<title>DrawSomething_你画我猜</title>
</head>
<body>
	<!-- 房间左侧 -->
	<div id="mainContent">
	
		<div id="roomId">roomId</div>
		
	    <div id="mainShowContent">
			<!-- waitingPage(核心即userList)/drawingPage(canvas+页面左侧userList)提出来，需要的时候填充 -->
	    </div>
    
    </div>
    <!-- 房间左侧结束 -->
    <!-- 房间右侧 -->
    <div id="secondaryContent">
  
	    <div id="preUserInfo">
	    <img id="userProfilePicture" alt="用户头像" src="picture/profilePic.png">
	    <p id="preUserInfoContent">
	              个人信息<br>
		 账号:${sessionScope.userId}<br/>
	              昵称:${sessionScope.userName}<br>
	             当前得分:<span id="userScore">${sessionScope.score}</span>
	    </p>
		</div> 
	    
	    <!-- 测试专用 ,显示当前连接状态
	    <div id="connectMessage">
			connectMessage
	    </div>
	    -->
	    
	    <div id="showChatMessage" >
	    </div>
	    
		<div id="sendMessage">
			<input id="chatMessage" type="text" required>
			
			<button onclick="sendChatMessage()">发送</button> 
			<!-- 测试专用 
		    <button onclick="closeWebSocket()">退出</button>
		  	-->  
		</div>
		
	</div>
    <!-- 房间右侧结束 -->
    
    <!-- 刷新页面引入 -->
    <div id="changePage">
	    <jsp:include page="waitingpage_jsp/waitingpage_twousers.jsp" flush="true"/>
	    <jsp:include page="waitingpage_jsp/waitingpage_threeusers.jsp" flush="true"/>
	    <jsp:include page="waitingpage_jsp/waitingpage_fiveusers.jsp" flush="true"/>
	    <jsp:include page="waitingpage_jsp/waitingpage_eightusers.jsp" flush="true"/>
	    <jsp:include page="waitingpage_jsp/waitingpage_twelveusers.jsp" flush="true"/>
	    
	    <jsp:include page="drawingpage_jsp/drawingpage_painter.jsp" flush="true"/>
	    <jsp:include page="drawingpage_jsp/drawingpage_guesser.jsp" flush="true"/>
    </div>
	<!-- 刷新页面引入结束 -->
	
</body>
<script type="text/javascript" src="js/drawingpage_js/drawingpage_painter.js"></script>
<script type="text/javascript" src="js/drawingpage_js/drawingpage_guesser.js"></script>

<script type="text/javascript" src="js/gameroom_js/gameroom.js"></script>
<script type="text/javascript" src="js/gameroom_js/drawingclientendpoint.js"></script>
<script type="text/javascript" src="js/gameroom_js/messageprocess.js"></script>

<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript">
	window.onload=function(){
		alert("window.onload加载页面");
		loadWaitingpage();
	};
	function getRoomId(){
		return "${requestScope.roomId}";
	}
	function getMaxUserNum(){
		return "${requestScope.maxUserNum}";
	}
	function getUserId(){
		//alert("session中的id值是："+"${sessionScope.userId}");
		return "${sessionScope.userId}";
	}
	function getUserName(){
		return "${sessionScope.userName}";
	}
	function getScore(){
		return "${sessionScope.score}";
	}
	function getChatMessage(){
		return document.getElementById("chatMessage").value;
	}

	/*在页面不跳转下更改页面内容
	<button onclick="changePage()">change</button>
	<div id="test" style="border:1px solid #000000;">
		test!!
	</div>
	function changePage(){
		//法一：引入一个jsp，只能在jsp中有效，在js中引入整个jsp报错，只能引入jsp页面中的html代码
		//<!--jsp:include page="drawingpage_painter_test.jsp" flush="true"/-->
		
		//法二：通过jquery的.load()方法引入一个jsp，该jsp中包含js,则该js会被主线程会被弃用(该js中的方法无法获取)
		//即若js中有一个test()方法，主jsp页面无法获取该test()方法,若该jsp不含js则可用此方法
		//$("#test").load("drawingpage_painter_test.jsp");
		
		//法三(火狐可行)：通过jquery的.load()方法引入jsp，同时将js从中剥离出来，直接在主jsp中对二者进行引用
		//然后调用js中的方法对加载中的jsp进行修饰，但谷歌浏览器中无法获取.load()载入的jsp页面的html元素如<div>(火狐可以获取，谷歌无法获取)
		//<script …… src="js/drawingpage_painter_test.js">
		//$("#test").load("drawingpage_painter_test.jsp");
		
		//法四(火狐，谷歌皆可行)：将需要的html放在一个隐藏的id=drawing_part的div，将该div放入一个jsp，在当前jsp中引入该jsp
		//主jsp的js中取出隐藏域的HTML代码，赋值给主jsp页面的id=test的div，
		//$("#test").html(document.getElementById("drawing_part").innerHTML);
		//注1：可以不使用jquery，使用js原生代码赋值即可
		//document.getElementById("test").innerHTML=document.getElementById("drawing_part").innerHTML;
		//注2：可以将所有js打包然后通过<script …… src="js/test.js">引入即可
		//<!--jsp:include page="drawingpage_painter_test.jsp" flush="true"/-->
		//注3：关于隐藏域：visibility:hidden;display:none;二者皆可，但display不占用空间
		//<div style="display:none;/visibility:hidden;">
		//注4：关于js的引入，可以在js中引入js(主线程中同步的 XMLHttpRequest已不推荐使用)
		//var sc=document.createElement("script");
		//sc.src="js/drawingpage_painter.js";
		//$("body").append(sc);//使js生效
		
		alert("changePage()");
		document.getElementById("test").innerHTML=document.getElementById("drawing_part").innerHTML;	//如果是字符串，需要""	
		drawingPageOnload();
	}*/
</script>
</html>