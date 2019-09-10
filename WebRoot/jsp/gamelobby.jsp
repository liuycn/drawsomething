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
<link rel="stylesheet" type="text/css" href="css/gamelobby.css">
<title>Insert title here</title>
</head>
<body>
	<!-- 房间左侧 -->
	<div id="mainContent">
	
	<div id="navigation">
	<img alt="drawSomething" src="picture/nav.png">
	</div>

    <div id="chooseGame">
    	<table class="chooseGameTable">
    	<tr class="chooseGameTable">
	    	<td class="chooseGameTable">
		    	<div class="gameEntrance">
		    	<a class="selectUserNum" href="game/gameroom/1" title="1人游戏">自娱自乐</a>
		    	</div>
	    	</td>
	    	<td class="chooseGameTable">
		    	<div class="gameEntrance">
		    	<a class="selectUserNum" href="game/gameroom/2" title="2人游戏">伯牙子期</a>
		    	</div>
	    	</td>
	    	<td class="chooseGameTable">
		    	<div class="gameEntrance">
		    	<a class="selectUserNum" href="game/gameroom/3" title="3人游戏">三足鼎立</a>
		    	</div>
	    	</td>
    	</tr>
    	<tr class="chooseGameTable">
	    	<td class="chooseGameTable">
	    		<div class="gameEntrance">    	
		    	<a class="selectUserNum" href="game/gameroom/5" title="5人游戏">群策群力</a>
		    	</div>
	    	</td>
	    	<td class="chooseGameTable">
	    		<div class="gameEntrance">
		    	<a class="selectUserNum" href="game/gameroom/8" title="8人游戏">集思广益</a>
		    	</div>
	    	</td>
	    	<td class="chooseGameTable">
	    		<div class="gameEntrance">
		    	<a class="selectUserNum" href="game/gameroom/12" title="12人游戏">万众一心</a>
		    	</div>
	    	</td>
    	</tr>
    	</table>

    </div>
    
    </div>
    <!-- 房间左侧结束 -->
    <!-- 房间右侧 -->
    <div id="secondaryContent">
    	<div id="logoutDiv">
    		<a id="logout" href="user/logout">退出</a>
    	</div>
	    <div id="preUserInfo">
	    	
		    <img id="userProfilePicture" alt="用户头像" src="picture/profilePic.png">
		    <p id="preUserInfoContent">
		              个人信息<br/>
		    <!-- request:${requestScope.userId};session:${sessionScope.userId} -->
		              账号：${sessionScope.userId}<br/>
		              昵称：${sessionScope.userName}<br>
		              当前得分：${sessionScope.score}
		    </p>
		</div> 

	    <div id="leaderBoard" >
	    <!-- 
	    	<button onclick="getLeaderboard()">获取排行榜</button>
	    
	    	<h2>排行榜</h2>
		    <table class="leaderBoardTable">
		    <tr>
		    <th>排名</th>
		    <th>用户</th>
		    <th>总分</th>
		    </tr>
		    <tr>
		    <td>1</td>
		    <td>用户1</td>
		    <td>积分1</td>
		    </tr>
		    </table>
		-->
	    </div>

	</div>
    <!-- 房间右侧结束 -->
</body>

<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript">
	window.onload=function(){
		getLeaderboard();
	};
	//获取排行榜内容
	function getLeaderboard(){
		//alert("getLeaderBoard");
		$.ajax({
			type:"POST",
			url:"user/leaderboard",
			dataType: "json",
			success:function(result){//JSON数组
				showLeaderboard(result);
			}
		});
	}
	function showLeaderboard(userInfo){
	    var showmessage="<h3 id='leaderBoardTitle'>排行榜</h3><table class='leaderBoardTable'>"+
	    	"<tr><th>排名</th><th>用户</th><th>总分</th></tr>";
		for(i in userInfo){
			//alert("i="+i+"账号："+userInfo[i].userId+",名字："+userInfo[i].userName+",得分："+userInfo[i].score);
			showmessage+="<tr><td>"+(i*1+1)+"</td>"+
				"<td>"+userInfo[i].userName+"</td>"+
				"<td>"+userInfo[i].score+"</td></tr>";
		}
	    showmessage+="</table>";
	    document.getElementById("leaderBoard").innerHTML=showmessage;
	}
</script>
</html>