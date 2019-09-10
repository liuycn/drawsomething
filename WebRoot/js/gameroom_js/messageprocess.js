//messageProcess.js用于处理webSocket传输的数据

//roomId展示标记
var flagForRoomId=true;
//当前状态标记：waiting/game
var flagForFullUser=false;
//若RoomProcess中的room列表记录在线人数出现偏差，分配了websocket中已满员状态的roomId,则由websocket重新获取roomId并将新的roomId返回JSP
var roomId;
//用于在websocket状态下即时更新userInfo信息栏的score，此时HttpSession处于"失效"状态
var userPreScore;
//---------------------发送信息-------------------------
//初次建立连接，发送userName信息，得到List<user>和进入通知信息
function sendUserMessage(){
	//alert("sendUserMessage方法!!!");
	//alert("getRoomId():"+getRoomId());
	roomId=getRoomId();
	userPreScore=getScore();
	alert("userPreScore"+userPreScore);
	//发送房间信息
	var mesContentTrans={
			"roomId":roomId,
			"maxUserNumber":getMaxUserNum(),
			"user":[{
				"userId":getUserId(),
				"userName":getUserName(),
				"score":userPreScore
			}]
	};
	//alert(JSON.stringify(mesContentTrans));
	var messageTrans={
			"mesTypeTrans":"USER_MESSAGE",
			"mesContentTrans":JSON.stringify(mesContentTrans)
	};
	//alert(JSON.stringify(messageTrans));
	sendMessage(JSON.stringify(messageTrans));
}
//发送聊天内容
function sendChatMessage(){
	var messageTrans={
			"mesTypeTrans":"CHAT_MESSAGE",
			"mesContentTrans":getChatMessage()
	};
	sendMessage(JSON.stringify(messageTrans));
}
//发送绘图point
function sendDrawingMessage(mesContentTrans_Str){//drawingpage_painter.js
	//alert("2222222"+mesContentTrans_Str);
    //document.getElementById("pointTest").innerHTML+=
    //"mesContentTrans_Str->"+mesContentTrans_Str+"<br>";
	if(getMaxUserNum()!="1"){
		var messageTrans={
				"mesTypeTrans":"DRAW_MESSAGE",
				"mesContentTrans":mesContentTrans_Str
		};
		sendMessage(JSON.stringify(messageTrans));
	}else{
		//alert("maxUser=1,绘图信息不发送");
	}
}
//游戏结束后返回waitingPage页面重新请求userList
function sendAskUserList(){
	var messageTrans={
			"mesTypeTrans":"ASK_USERLIST",
			"mesContentTrans":""
	};
	sendMessage(JSON.stringify(messageTrans));
}
//发送正常退出信息
/*
function sendUserLogoutMessage(){
	var messageTrans={
			"mesTypeTrans":"LOGOUT_MESSAGE",
			"mesContentTrans":""
	};
	sendMessage(JSON.stringify(messageTrans));
}*/
//---------------------发送信息完-------------------------

//---------------------接收信息-------------------------
//处理server传回的值
function getMessageFromServer(messageTrans_str){
	//("getMessage()方法~~~");
	var messageTrans=JSON.parse(messageTrans_str);
	switch(messageTrans.mesTypeTrans){
	case "USER_LIST":
		//alert("USER_LIST,内容是"+messageTrans.mesContentTrans);//[{k:v,k:v}]
		//var mesContentTrans_str="{"+messageTrans.mesContentTrans+"}";//{k:[{k:v,k:v}]},[]作为v，需要对应的k
		getUserListFromServer(messageTrans.mesContentTrans);
		break;
	case "SYSTEM_MESSAGE"://login/logout系统消息
		getSystemMessageFromServer(messageTrans.mesContentTrans);
		break;	
	case "CHAT_MESSAGE"://userName：聊天内容
		//alert("chat message,内容是:"+messageTrans.mesContentTrans);
		getChatMessageFromServer(messageTrans.mesContentTrans);
		break;
	case "ROOM_ID":
		getRoomIdFromServer(messageTrans.mesContentTrans);
		break;
	case "GAME_SUBJECT"://满员
		getGameSubjectFromServer(messageTrans.mesContentTrans);
		//getUserNumberFromServer(messageTrans.mesContentTrans);
		break;
	case "DRAW_MESSAGE":
		getDrawingMessageFromServer(messageTrans.mesContentTrans);//drawingpage_guesser.js
		break;
	case "GAME_OVER":
		getGameOverMessageFromServer();
		break;
	case "UPDATE_SCORE"://更新得分
		getPreUserScoreFromServer(messageTrans.mesContentTrans);
		break;
	/*case "LOGOUT_MESSAGE":
		alert("user logoout,内容是:"+messageTrans.mesContentTrans);
		break;*/
	default :
		break;
	}
}
//获取当前用户列表
function getUserListFromServer(mesContentTrans_str){
	//alert("getUserList方法");
	showMessage(mesContentTrans_str,"userList");
	//默认为true即roomId正确，S端未改变roomId信息，在获取userList时同时显示userId，若roomId被改变，则先获得roomId显示并使此flag=false
	if(flagForRoomId){
		showMessage("房间号："+getRoomId(),"roomId");//未改变时roomId从本地获取即getRoomId()
	}
}
/*获取userList信息
function getUserListFromServer(mesContentTrans_str){
	//alert("getUserList方法");
	var userList=JSON.parse(mesContentTrans_str);
	var userInfo="";
	for(i in userList){
		userInfo+="登录用户信息：id:"+userList[i].userId+"name:"+userList[i].userName+"score"+userList[i].score+"<br/>";
	}
	showMessage(userInfo,"userList");
	if(flagForRoomId){
		showMessage("房间号："+getRoomId(),"roomId");
	}
}*/
//获取[系统]如login/logout等信息
function getSystemMessageFromServer(mesContentTrans_str){
	var message="<span id='systemMessage'>"+mesContentTrans_str+"</span>";
	showMessage(message,"showChatMessage");
}
//获取chat信息
function getChatMessageFromServer(mesContentTrans_str){
	//alert("getChatMessage方法："+mesContentTrans_str);
	showMessage(mesContentTrans_str,"showChatMessage");
}
//获得roomId
function getRoomIdFromServer(rId){
	roomId=rId;
	showMessage("房间号："+roomId,"roomId");//roomId被S端改变时由S端传值过来
}
//当前room满员，获取绘图/猜图者身份划分&本轮题目
function getGameSubjectFromServer(messageStr){
	flagForFullUser=true;
	//alert("messageStr:"+messageStr);
	var message=messageStr.split("+");
	//alert("selectedID:"+message[0]);
	showMessage("","clearChatMessage");//清空聊天区内容
	if(getUserId()==message[0]){
		loadPainterPage();
		showMessage("你的身份：绘图者","userStatus_painter");
		showMessage("绘图："+message[1],"gameSubject_painter");
	}else{
		loadGuesserPage();
		showMessage("你的身份：猜图者","userStatus_guesser");
		showMessage("提示："+message[2],"gameSubject_guesser");
		for(i in message){//GameSubject.otherAdditionInfo
			var mesStr="";
			if((i*1)>2){
				//alert("i="+i+",message="+message[i]);
				//showMessage("[系统消息] 提示:"+message[i],"showChatMessage");
			    mesStr="<span id='systemMessage'>[系统消息] 提示:"+message[i]+"</span>";
				showMessage(mesStr,"showChatMessage");
			}
		}
	}
	if(getMaxUserNum()!="1"){
		timerForDrawing();
	}else{
		//alert("maxUser=1,计时器不开启");
	}
}
//本轮游戏结束
function getGameOverMessageFromServer(){
	clearTimeout(timerForD);//规定时间内有人猜中/时间到
	second=20;
	flagForFullUser=false;//进入waitingPage
	alert("游戏结束，5秒后开始下一轮");
	var timerForNext=setTimeout(function(){
		//alert("gameover5秒");
		loadWaitingpage();
		chooseNextPage();//gameroom.js
	},5000);
}
//更新本地score
function getPreUserScoreFromServer(preScore){
	//alert("当前得分："+preScore);
	//updateSessionScore(preScore);
	userPreScore=preScore;
	showMessage(preScore,"userScore");
}

//展示信息
function showMessage(message,pageElement){
	switch(pageElement){//部分展示方式相同，合并之
		case "showChatMessage": //<textarea>
			document.getElementById(pageElement).innerHTML += message+ "<br/>";
			break;
		case "roomId":
			flagForRoomId=false;//后端需要保证roomId发送先于userList
			document.getElementById(pageElement).innerHTML = message;
			break;
		case "userList":
			if(flagForFullUser){//满员
				updateUserListForDrawingPage(message);
			}else{
				updateUserListForWaitingPage(message);
			}
			break;
		case "clearChatMessage":
			document.getElementById("showChatMessage").innerHTML = message;
			break;
		default://"connectMessage"、"userStatus"、"gameSubject"、"userScore"
			document.getElementById(pageElement).innerHTML = message;
			break;
	}	
}
//---------------------接收信息完-------------------------