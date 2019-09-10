//用于gameroom.jsp页面<div id="mainShowContent"></div>刷新载入新页面
var linkNode=document.createElement("link");
linkNode.setAttribute("rel","stylesheet");  
linkNode.setAttribute("type","text/css"); 

//根据后端传回session.maxUserNum加载不同等待页面
function loadWaitingpage(){
	var maxUserNum=getMaxUserNum();
	//alert(typeof maxUserNum);//String
	switch(maxUserNum*1){
	case 1:
		loadPainterPage();
		break;
	case 2:
		chooseWaitingpage("two");
		break;
	case 3:
		chooseWaitingpage("three");
		break;
	case 5:
		chooseWaitingpage("five");
		break;
	case 8:
		chooseWaitingpage("eight");
		break;
	case 12:
		chooseWaitingpage("twelve");
		break;
	default:
		chooseWaitingpage("two");
		break;
	}
}
//waitingPage加载
function chooseWaitingpage(userNumber){
	//alert(userNumber);
	//拼接url/元素名
	var hrefStr="css/waitingpage_css/waitingpage_"+userNumber+"users.css";
	var elementIdStr="changePageContent_"+userNumber+"users";
	                  
	linkNode.setAttribute("href",hrefStr); 
	document.head.appendChild(linkNode);
	document.getElementById("mainShowContent").innerHTML=document.getElementById(elementIdStr).innerHTML;
}
//更新waitingPage当前用户列表
function updateUserListForWaitingPage(mesContentTrans_str){
	var userList=JSON.parse(mesContentTrans_str);
	var userInfo="";
	for(i in userList){
		//i=0,i+1=01,i*1+1=1;
		userInfo=/*"id"+userList[i].userId+"name"+*/userList[i].userName+" 得分"+userList[i].score+"<br/>";	
		document.getElementById("userInfo"+(i*1+1)).innerHTML=userInfo;
		//document.getElementById("userInfo1").innerHTML=userInfo;
		changeProfilePicture("profilePicture"+(i*1+1),true);//有user加入
	}
	var maxUserNum=getMaxUserNum()*1;//String->number
	i=i*1+1;//alert(typeof i);//String
	while(i<maxUserNum){
		i++;
		//alert("i="+i);
		document.getElementById("userInfo"+i).innerHTML="匹配中……";
		changeProfilePicture("profilePicture"+i,false);//有user退出
	}
}
//更换waitingPage的profilePicture
function changeProfilePicture(proPicStr,flag){
	//alert("changePic:"+proPicStr);
	var imgEle=document.getElementById(proPicStr);
	if(flag){
		//alert("true");
		var index=Math.floor(Math.random()*6)+1;
		//alert("index="+index);
		imgEle.src="picture/waiting/coming"+index+".gif";
	}else{
		imgEle.src="picture/waiting/waiting.gif";
	}
}
//加载绘图页面
function loadPainterPage(){
	var hrefStr="css/drawingpage_css/drawingpage_painter.css";
	linkNode.setAttribute("href",hrefStr); 
	document.head.appendChild(linkNode);
	document.getElementById("mainShowContent").innerHTML=
		document.getElementById("changePageContent_painter").innerHTML;
	drawingPageForPainterOnload();//绘图页面初始化
}
//加载猜图页面
function loadGuesserPage(){
	var hrefStr="css/drawingpage_css/drawingpage_guesser.css";
	linkNode.setAttribute("href",hrefStr); 
	document.head.appendChild(linkNode);
	document.getElementById("mainShowContent").innerHTML=
		document.getElementById("changePageContent_guesser").innerHTML;
}
var second=20;
var timerForD;
//绘图计时器
function timerForDrawing(){
	second=second-1;
	if(second>=0){
		document.getElementById("timer").innerHTML=second;
		timerForD=setTimeout("timerForDrawing()",1000);
		//timerForD=setTimeout(timerForDrawing(),1000);
	}
	else{
		//clearTimeout(timerForD);
		//second=20;
		alert("时间到,没有人猜出正确答案");
		getGameOverMessageFromServer();//messageprocess.js
	}
}
var timerForC;
//刷新列表
function chooseNextPage(){
	timerForC=setTimeout(function(){
		//alert("timerForC5秒了");
		//5秒后自动加入当前房间
		sendAskUserList();//messageprocess.js
	},5000);
}
//更新drawingPage当前用户列表
function updateUserListForDrawingPage(mesContentTrans_str){
	var userList=JSON.parse(mesContentTrans_str);
	/*
	<ul>
	<li>
	<img id="userpropic_userListPai" alt="用户头像" src="picture/profilePic.png">
	<br>
	<span id="userName_userListPai">userName</span>
	<span id="userScore_userListPai">score</span>
	</li>
	<br>
	</ul>
	*/
	var userInfo="<ul>";
	for(i in userList){
		userInfo+=
		"<li><img id='userpropic_userListPai' alt='用户头像' src='picture/profilePic.png'>"+
		"<br><span id='userName_userListPai'>"+userList[i].userName+
		"</span> <span id='userScore_userListPai'>得分"+userList[i].score+
		"</span></li><br>";
	}
	userInfo+="</ul>";
	document.getElementById("userList").innerHTML=userInfo;
}

//-----------------------waiting/gamepage.jsp所需js-----------------------
//退出队列/游戏,返回gameLobby
function gotoGameLobby(){
	closeWebSocket();
	alert("用户当前分数"+userPreScore);
	alert("userPreScore"+userPreScore);
	updateSessionScore(userPreScore);

	window.location="game/gamelobby";//IE不支持,会从当前目录开始跳转
	//window.location="jsp/gamelobby.jsp";//IE不支持
	//window.open("user/updateUserScore/"+userPreScore);//可以改变session，但是重开了新的页面（新的页面窗口）
	//window.location="user/updateUserScore/"+userPreScore;//更新session且不重开页面
	//window.location="http://localhost:8080/drawsomething/jsp/gamelobby.jsp";
		
}
//更新HttpSession中的score
function updateSessionScore(preScore){
	var preUserInfo={
			"score":preScore
	};
	$.ajax({
		type:"POST",
		url:"user/updateUserScore",
		data:JSON.stringify(preUserInfo),
		contentType:"application/json;charset=utf-8",
		/*success:function(result){
			if(result=="test"){
				alert("ajax成功");
			}
		},
		error:function(){
			alert("ajax失败");
		}*/
	});
}
//-----------------------waiting/gamepage.jsp所需js结束---------------------