//drawingclientendpoint.js用于建立S端和B端的连接，用WebSocket实现B端和S端的双向数据传输，此为B端的端点

var websocket = null;

//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
	alert("webSocket开始建立连接");
	websocket = new WebSocket("ws://localhost:8080/drawsomething/message");
}else{
	alert("Not support websocket");
}
      
//连接发生错误的回调方法
websocket.onerror = function(){
	//showMessage("连接异常","connectMessage");
};
       
//连接成功建立的回调方法
websocket.onopen = function(event){
	//alert("onOpen:event="+event);
	//showMessage("已建立连接","connectMessage");
	sendUserMessage();
};
       
//接收到消息的回调方法
websocket.onmessage = function(){
	//alert("event:"+event);
	//alert("event.data:"+event.data);
	getMessageFromServer(event.data);
};
   
//连接关闭的回调方法
websocket.onclose = function(){
	alert("onclose");
	//showMessage("连接已关闭","connectMessage");
	//window.location="jsp/gamelobby.jsp";
	
};
       
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
	websocket.close();
};

//"退出"button，测试专用
function closeWebSocket(){
	//sendAskUserScore();
	alert("closeWebSocket()");
	clearTimeout(timerForD);//绘图计时器
	clearTimeout(timerForC);//页面跳转计时器
	alert("计时器关闭");
	websocket.close();
}            
//发送消息
function sendMessage(message){
	websocket.send(message);
}
