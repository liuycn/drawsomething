package com.drawsomething.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.MessageTrans;
import com.drawsomething.entity.Room;
import com.drawsomething.entity.User;
import com.drawsomething.entity.UserForGame;
import com.drawsomething.service.RoomProcess;
import com.drawsomething.service.UserProcess;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用WebSocket实现B端和S端的双向数据传输，该类为S端的端点/处理类
 * @author L
 *
 */
//tomcat自带jar包tomcat-websocket.jar/websocket-api.jar的支持
@ServerEndpoint(value="/message",
	decoders={MessageDecoder.class},
	encoders={MessageEncoder.class},
	configurator=SpringConfigurator.class//通过spring-websocket-4.3.1.RELEASE.jar提供的该类配置spring与websocket
)
public class DrawingServer {
	
	@Autowired
	@Qualifier("userProcess")
	private UserProcess userProcess;//通过注解方式引入bean
	
	@Autowired
	@Qualifier("roomProcess")
	private RoomProcess roomProcess;//RoomProcess rp=new RoomProcessImpl();
	
	//测试监测数据，用于统计当前连接人数
	private static int onlineCount = 0;

	//session.getOpenSessions()只返回当前session（版本tomcat8.0.32），详见:https://my.oschina.net/u/2012496/blog/817944
	/*
	Set<Session> ss=this.session.getOpenSessions();
	System.out.println("onMessage:getOpenSessions的size:"+session.getOpenSessions().size());
	for(Session s:ss){
		System.out.println("当前session："+s);
		System.out.println("当前session的name："+s.getUserProperties().get("USERNAME_KEY"));
	}*/
	
	//存储所有连接状态下的session
	private List<Session> allSession=new LinkedList<Session>();
	//<roomId,allSession>，存储某个roomId对应的所有user的连接session
	private static ConcurrentHashMap<String, List<Session>> allRoomId=new ConcurrentHashMap<String, List<Session>>();
	
	//Session用于存放当前连接端的应用状态/信息
	private Session session;
	//EndpointConfig用于存放WebSocket端点所有连接端的应用状态/信息
	private ServerEndpointConfig endpointConfig;
	
	//更改分配到的roomId（在WebSocket中该roomId连接的session已达到上限，需要重新分配roomId）
	/*!PS:
	 * 理论来说RoomProcess分配应该保证其可靠性，但此版本中roomId是通过B端传送给本类
	 */
	private boolean flagForChangeRoomId=false;
	
	@OnOpen
    public void onOpen(EndpointConfig config,Session session){
		this.endpointConfig=(ServerEndpointConfig)config;
		this.session = session;
		//allSession.add(this);//加入DrawingServer实例
		//allSession.add(this.session);
		addOnlineCount();//在线数加1
		System.out.println("WebSocket有新连接加入！当前在线人数为"+getOnlineCount());
	}
	
	@OnMessage
	public void onMessage(MessageTrans message) {	
		if(message!=null){
			System.out.println("来自客户端的消息:" + message);
			String mesType=message.getMesTypeTrans();//传来信息内容分类
			
			switch (mesType) {
			case "USER_MESSAGE":
				this.processNewUser(message);
				break;
			case "CHAT_MESSAGE":
				this.sendChatMessage(message.getMesContentTrans());
				break;
			case "DRAW_MESSAGE":
				this.sendMessageExceptMe("DRAW_MESSAGE", message.getMesContentTrans());
				break;
			case "ASK_USERLIST":
				this.processAskUserList();
				break;
			/*case "LOGOUT_MESSAGE":
				this.processUserLogout();
				break;*/
			default:
				break;
			}
		}else{//message==null
			System.out.println("来自客户端的消息:怎么会是个NULL？？？");
		}
	}
	
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("onError,WebSocket发生错误");
		error.printStackTrace();
	}
	
	@OnClose
	 public void onClose(){
		if(this.session.getUserProperties().get("USERID_KEY")!=null){
			this.sendLogoutMessage();
			this.removeUser();
		}
	 }
	
	/**
	 * 广播用户退出消息
	 */
	private void sendLogoutMessage(){
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		/*
		System.out.println("当前session-roomid："+roomId);
		System.out.println("当前session-userid："+(Integer)this.session.getUserProperties().get("USERID_KEY"));
		List<User> users=this.getUserList();
		for(User s:users){
			System.out.println("【user】："+s);
		}*/
		//依据该roomId是否在gameSubject表中决定是否处理，因而要先处理是否-1，再移除gameSubject中的roomId
		//RoomProcess rp=new RoomProcessImpl();
		//直接使用IOC中的bean
		this.roomProcess.changeRoomList(new Room(roomId, this.getRoomMaxUserNumber(), -1));
		
		int userId=(Integer)this.session.getUserProperties().get("USERID_KEY");
		int score=(Integer)this.session.getUserProperties().get("SCORE_KEY");
		
		//UserProcess up=new UserProcessImpl();//并非IOC中的bean，详见被调用方法处
		//up.updateUserScore(userId, score);
		this.userProcess.updateUserScore(userId, score);//当前得分
		
		allRoomId.get(roomId).remove(this.session);//从roomId对应的List中移除该session
		if(allRoomId.get(roomId).isEmpty()){
			allRoomId.remove(roomId); //该房间被移出列表,用于onClose
			System.out.println("【登出】检测："+roomId+"空了，已移出列表");
			//所有人退出了当前该房间，将roomId+题目从列表移除
			this.roomProcess.removeGameSubject(roomId);
		}
		
		for(String key:allRoomId.keySet()){
			for(Session s:allRoomId.get(key)){
				System.out.println("【登出】检测：roomId："+key+",session:"+s.getUserProperties().get("USERNAME_KEY"));
			}
		}
		String message="[系统消息]:用户["+this.session.getUserProperties().get("USERNAME_KEY")+"]退出房间";
		this.sendMessage("SYSTEM_MESSAGE", message);
	}
	/**
	* 移除session信息
	*/
	private void removeUser(){
		try {
			this.updateUserList();//this.endpointConfig.getUserProperties()
			this.broadcastUserListUpdate();
			this.session.getUserProperties().remove("USERID_KEY");
			this.session.getUserProperties().remove("USERNAME_KEY");
			this.session.getUserProperties().remove("SCORE_KEY");
			this.session.getUserProperties().remove("ROOMID_KEY");
			subOnlineCount();//在线数减1 
			System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
			//this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "userLogout"));
			this.session.close();
			System.out.println("close~~~~~~~~~");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("removeUser异常，e:"+e.getMessage());
		}
	}	
	/*//onMessage==用户退出
	private void processUserLogout(){
		allSession.remove(this.session);
		String message=this.session.getUserProperties().get("USERNAME_KEY")+":退出房间";
		this.sendMessage_2("LOGOUT_MESSAGE", message);
		this.removeUser();
	}*/
	
	private int roomMaxUserNumber;
	/**
	 * 获取room的人数max值
	 * @return max值
	 */
	public int getRoomMaxUserNumber() {
		return roomMaxUserNumber;
	}
	/**
	 * 设置room的人数max值
	 * @param roomMaxUserNumber max值
	 */
	public void setRoomMaxUserNumber(int roomMaxUserNumber) {
		this.roomMaxUserNumber = roomMaxUserNumber;
	}
	
	/**
	 * onMessage==新用户登录，在session中注册相关信息
	 * @param message
	 */
	private void processNewUser(MessageTrans message){
		//将message中的mesContentTrans二次解码，此时其内容是一个Room.class，包含了一个User信息
		Object o=new JsonProcess().jsonDecode(message.getMesContentTrans(),message.getMesTypeTrans());
		if(o!=null&&o instanceof Room){
			Room room=(Room)o;
			System.out.println("解码后获得新用户信息："+room);
	
			Room newRoom=this.checkRoomId(room);//通过session数量查看该room是否已经满员，已满员则更换roomId
			this.setRoomMaxUserNumber(newRoom.getMaxUserNumber());//记录该端点指向的room的人数max值
			
			newRoom.setUser(room.getUser());
			//room=null;
			System.out.println("解码后获得新->新用户信息："+newRoom);
			this.registerNewUser(newRoom);//在session中注册相关信息
			this.sendRoomId();//从session取值，B端的roomId展示逻辑需要S端逻辑保持roomId先于userList发送
			this.broadcastUserListUpdate();//向waitingPage.jsp发送userList信息
			this.sendUserLoginMessage();//群发
			
			int maxUserNum=this.getRoomMaxUserNumber();
			//通过room等待列表查看newRoom是否已满员，满员时等待列表无该roomId信息
			if(this.roomProcess.checkRoomStatusByMaxNum(maxUserNum, newRoom.getRoomId())){
				this.sendGameSubject();
			}
			
		}else{
			System.out.println("processNewUser中解析出错啦……");
		}
	}
	
	/**
	 * 检查该roomId，确保用户携带的room未满员
	 * @param room 待检查room
	 * @return 该room未满员则返回当前room，否则分配新的roomId
	 */
	private Room checkRoomId(Room room){
		while(!checkRoomIdLegality(room.getRoomId(),room.getMaxUserNumber())){//检查
			this.flagForChangeRoomId=true;//roomId被改变
			System.out.println("while循环中：房间已满!!!!!");
			//this.session.close();
			room=changeRoomId(room.getMaxUserNumber());
		}
		return room;
	}
	/**
	 * 检查该房间是否满员
	 * @param roomId 待检查房间的Id
	 * @param maxUserNum 该房间理论max人数
	 * @return 未满员为true
	 */
	private boolean checkRoomIdLegality(String roomId,int maxUserNum){
		if(allRoomId.containsKey(roomId)){
			if(allRoomId.get(roomId).size()<maxUserNum){//当前.size+1=maxUserMum
				System.out.println("【登出】检测：当前房间："+roomId+"存在人数为："+allRoomId.get(roomId).size());
				return true;
			}else{
				System.out.println("【登出】检测：当前房间："+roomId+"满员，存在人数为："+allRoomId.get(roomId).size());
				//告诉room业务模块的列表该roomId已满员
				this.roomProcess.checkFullUserNumber(new Room(roomId, maxUserNum, maxUserNum));
				return false;
			}
		}else{
			System.out.println("【登出】检测：当前房间："+roomId+"不存在");
			return true;
		}
	}
	/**
	 * 改变用户携带的roomId，重新为其分配roomId
	 * @param maxUserNum 指定待分配room的人数max值
	 * @return 被分配的新room
	 */
	private Room changeRoomId(int maxUserNum){
		Room room=this.roomProcess.getRoomForUser(maxUserNum);
		return room;
	}
	/**
	 * 若flagForChangeRoomId改变，将roomId改变的信息返回B端
	 */
	private void sendRoomId(){
		if(this.flagForChangeRoomId){//roomId发生了改变
			this.flagForChangeRoomId=false;
			this.sendMessageOnlyMe("ROOM_ID", (String)this.session.getUserProperties().get("ROOMID_KEY"));
		}
	}

	/**
	 * 在session中注册当前用户信息
	 * @param room 待注册的信息
	 */
	private void registerNewUser(Room room){
		System.out.println("registerNewUser()方法,roomId:"+room.getRoomId());
		String roomId=room.getRoomId();
		System.out.println("registerNewUser:"+room.getUser().get(0).getUserId());
		System.out.println("registerNewUser:"+room.getUser().get(0).getUserName());
		this.session.getUserProperties().put("USERID_KEY",room.getUser().get(0).getUserId());
		this.session.getUserProperties().put("USERNAME_KEY",room.getUser().get(0).getUserName());
		this.session.getUserProperties().put("SCORE_KEY",room.getUser().get(0).getScore());
		this.session.getUserProperties().put("ROOMID_KEY", roomId);
		
		if(allRoomId.containsKey(roomId)){
			this.allSession=allRoomId.get(roomId);
		}else{
			this.allSession.clear();
		}
		this.allSession.add(this.session);
		allRoomId.put(roomId, this.allSession);
		
		//值监测
		//System.out.println("allRoomId.size:"+allRoomId.size());
		for(String key:allRoomId.keySet()){
			for(Session s:allRoomId.get(key)){
				System.out.println("【登录】检测：roomId："+key+",session:"+s.getUserProperties().get("USERNAME_KEY"));
			}
		}
		
		this.updateUserList();
	}
	/**
	 * 更新用户列表，增加端点endpointConfig用户列表中的新的用户session或移除空room
	 */
	private void updateUserList(){
		System.out.println("updateUserList()方法");
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		List<User> userList=new ArrayList<>();
		if(allRoomId.containsKey(roomId)){
			for(Session s:allRoomId.get(roomId)){
				int id=(Integer)s.getUserProperties().get("USERID_KEY");
				String name=(String)s.getUserProperties().get("USERNAME_KEY");
				int score=(Integer)s.getUserProperties().get("SCORE_KEY");
				userList.add(new UserForGame(id,name,score));
				//System.out.println("updateUserList:userList.size:"+new UserForGame(id,name,score));
			}
			String key="ALLUSERINFO_KEY_"+roomId;
			this.endpointConfig.getUserProperties().put(key,userList);	
		}else{//room为空，已被allRoomId移除，返回空的ListUser
			System.out.println("endpointConfig空，remove：endpointConfig");
			this.endpointConfig.getUserProperties().remove("ALLUSERINFO_KEY_"+roomId);
		}
	}
	
	/**
	 * 广播用户列表更改事件，即有用户连接或退出
	 */
	private void broadcastUserListUpdate(){
		System.out.println("broadcastUserListUpdate()方法");
		List<User> userList=this.getUserList();
		//System.out.println("getUserList:"+userList);
		String userListStr="";
		if(userList.isEmpty()){
		}else{
			userListStr=new JsonProcess().jsonEncode(userList);
		}
		this.sendMessage("USER_LIST", userListStr);
	}
	/**
	 * 获取当前登录/连接状态的所有用户
	 * @return 相关用户列表
	 */
	private List<User> getUserList(){
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		String key="ALLUSERINFO_KEY_"+roomId;
		List<User> userList=new ArrayList<User>();
		if(this.endpointConfig.getUserProperties().containsKey(key)){
			Object object=this.endpointConfig.getUserProperties().get(key);
			/*//直接强制转换会报异常，可通过@SuppressWarnings("unchecked")忽略
			  userList=(List<User>)object;
			 */
			String userListStr=new JsonProcess().jsonEncode(object);//通过JSON:Object->String->List<User>
			System.out.println("userListStr:"+userListStr);
			ObjectMapper mapper=new ObjectMapper();
			try {
				userList=mapper.readValue(userListStr,new TypeReference<ArrayList<UserForGame>>(){});
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("DrawingServer类中JSON转码异常："+e.getMessage());
			}
		}else{
			System.out.println("this.endpointConfig空，当前userList为："+userList);
		}
		//return (userList.isEmpty())?new ArrayList<User>():userList;
		return userList;
	}
	
	/**
	 * 发送用户登录通知
	 */
	private void sendUserLoginMessage(){
		String message="[系统消息]:用户["+this.session.getUserProperties().get("USERNAME_KEY")+"]进入房间";
		this.sendMessage("SYSTEM_MESSAGE", message);
	}
	
	/**
	 * 当前人数==房间上限人数时，即满员后发送gameSubject
	 */
	private void sendGameSubject(){
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		List<Session> sessions=allRoomId.get(roomId);
		int preUserNum=sessions.size();
		System.out.println("房间："+roomId+"当前人数为："+preUserNum+"上限人数："+this.getRoomMaxUserNumber());
		if(preUserNum==this.getRoomMaxUserNumber()){
			/*roomStatus.add(roomId);
			for(String s:roomStatus){
				System.out.println("满员的roomId:"+s);
			}*/
			int index=new Random().nextInt(preUserNum);//生成随机数不包含preUserNum
			int selectedUserId=(Integer)sessions.get(index).getUserProperties().get("USERID_KEY");
			System.out.println("满员,被选中的UserId:"+selectedUserId);
			
			//RoomProcess rp=new RoomProcessImpl();
			//GameSubject gs=rp.selectGameSubject(roomId,selectedUserId);
			GameSubject gs=this.roomProcess.selectGameSubject(roomId,selectedUserId);
			
			String messageStr=selectedUserId+"+"+gs.getWordInfo()+"+"+gs.getAdditionInfo()+"+"+gs.getOtherAdditionInfo();
			this.sendMessage("GAME_SUBJECT", messageStr);
			this.broadcastUserListUpdate();//向drawingPage.jsp发送userList信息
		}else{//room等待列表偏差，人数未满
			System.out.println("等待列表出现偏差，更新中");
			int maxUserNum=this.getRoomMaxUserNumber();
			if(preUserNum<maxUserNum){
				for(int i=0;i<preUserNum;i++){//满员后等待列表已将该roomId移除
					System.out.println("更新->i="+i);
					this.roomProcess.changeRoomList(new Room(roomId,maxUserNum,1));//若room未满员，将其将入等待列表
				}
			}
		}
	}
	
	/**
	 * onMessage==发送用户聊天内容
	 * @param chatMessage 接收到的信息内容
	 */
	/*!PS:
	 * 此种检测方式下，每次收到消息都要去进行判断room状态和绘图者Id，room最好是打标记，绘图者Id在web页面抹掉信息输入框  
	 * 来自系统端的信息最好从DrawingServer.java摘出去，因该类主要用于数据传输而非判断&处理业务
	 */
	private void sendChatMessage(String chatMessage){
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		String userName=(String)this.session.getUserProperties().get("USERNAME_KEY");
		int painterId=this.roomProcess.checkRoomStatusByRoomId(roomId);
		System.out.println("chatMessage检查：painterId="+painterId);
		if(painterId==-1){//waitingRoom
			String message=userName+":"+chatMessage;
			this.sendMessage("CHAT_MESSAGE", message);
		}else{//gamePage
			int userId=(Integer)this.session.getUserProperties().get("USERID_KEY");
			if(painterId==userId){//当前消息来自绘图者
				String message="[系统消息]:游戏中，绘图者无法发送信息";
				this.sendMessageOnlyMe("SYSTEM_MESSAGE", message);
			}else{//当前消息来自猜图者
				String message=userName+":"+chatMessage;
				this.sendMessage("CHAT_MESSAGE", message);
				//检测用户信息是否是正确答案
				if(this.roomProcess.checkUserAnswer(roomId, chatMessage)){//答案正确
					message="[系统消息]:用户["+userName+"]已给出正确答案";
					this.sendMessage("SYSTEM_MESSAGE", message);
					this.sendMessage("GAME_OVER", "");//向B端发送gameover信号
					this.endGame(roomId, painterId, userId);
				}else{}//答案不正确
			}
		}
	}
	/**
	 * 存在用户给出正确答案，游戏结束，后续清算工作
	 * @param roomId 该用户所在房间Id
	 * @param painterId 绘图者Id
	 * @param guesserId 猜中者Id
	 */
	private void endGame(String roomId,int painterId,int guesserId){
		//将其存储在session中，remove时一并存储
		for(Session s:allRoomId.get(roomId)){
			int userId=(Integer)s.getUserProperties().get("USERID_KEY");
			if(userId==guesserId){
				int score=(Integer)s.getUserProperties().get("SCORE_KEY")+1;//默认+1
				s.getUserProperties().put("SCORE_KEY",score);			
				System.out.println("----->更新得分信息："+userId+","+score);
				this.sendMessageOnlyMe("UPDATE_SCORE",String.valueOf(score));
			}else if(userId==painterId){
				int score=(Integer)s.getUserProperties().get("SCORE_KEY")+1;//默认+1
				s.getUserProperties().put("SCORE_KEY",score);
				System.out.println("----->更新得分信息："+userId+","+score);
				this.sendMessageToSpecifiedUser("UPDATE_SCORE", String.valueOf(score), userId);
			}
		}
		/*
		RoomProcess rp=new RoomProcessImpl();
		rp.removeGameSubject(roomId);//若2人退出1人-1.后续1人ask+1，则为0（满员）
		
		1：GAME_OVER后，通过session检测是否满员，若满员则不做任何处理，不满员将其加入room等待列表，
		然后romeveGameSubject，若有人退出-1，有人进入+1，时间到后（可通过前端请求或后端计时）向
		前端发送UserList，此时若满员则sendGameSubject
		2：或直接进入等待时间段，此时退出不-1,时间到后开启removeGameSubject，所有在线的用户重新发送
		AskUserList，发送的用户+1，当满员后（若满员则最后一位发送AskUserList，不满员状态下新用
		户加入发送processNewUser）开始sendGameSubject
		
		注：由于每个@ServerEndpoint存储的UseList不同，需要先updateUserList
		*/
	}
	
	/**
	 * onMessage==请求用户列表，返回当前userList给请求页面
	 */
	private void processAskUserList(){
		System.out.println("processAskUserList()方法");
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		int userId=(Integer)this.session.getUserProperties().get("USERID_KEY");
		int maxUserNum=this.getRoomMaxUserNumber();
		//RoomProcess rp=new RoomProcessImpl();
		this.roomProcess.removeGameSubject(roomId);//gameSubject存在则即不-1也不+1，
		
		//重新加入等待列表，满员后该roomId会从等待列表删除，给所有发出ask的页面+1，后续若退出则-1
		this.roomProcess.changeRoomList(new Room(roomId,maxUserNum,1));
		System.out.println("processAskUserList()方法：user+1");

		/*
		//用于测试endpointConfig中的数据，每个WebSocket的端点中保存各自的数据，
		//其值根据加入session的顺序而定，并不自动更新，即每个endpointConfig保持独立
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		List<User> userListTest=getUserList();
		System.out.println("二轮游戏中测试获取endpointConfig,getUserList:"+userListTest);

		this.updateUserList();
		List<User> userList=getUserList();
		System.out.println("二轮游戏中测试endpointConfig,getUserList:"+userList);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		*/
	
		this.updateUserList();
		List<User> userList=getUserList();
		
		String userListStr="";
		if(userList.isEmpty()){
		}else{
			userListStr=new JsonProcess().jsonEncode(userList);
		}
		this.sendMessageOnlyMe("USER_LIST", userListStr);
		System.out.println("重新请求:userID:"+userId+"maxUserNum:"+maxUserNum);
		System.out.println("重新请求:userID:"+userId+"roomId:"+roomId);
		//this.sendGameSubject();session实质上并没有移除，若满员则每个客户端都将引起一次
		if(this.roomProcess.checkRoomStatusByMaxNum(maxUserNum, roomId)){	
			System.out.println("processAskUserList()方法，已满员");
			this.sendGameSubject();
		}else{
			System.out.println("processAskUserList()方法，目前未满员");
		}
	}
	
	/**
	 * 向B端(所有用户包括自己)发送信息(群发)
	 * @param messageType 待发送信息类型
	 * @param message 待发送信息内容
	 */
	private void sendMessage(String messageType,String message){
		MessageTrans mt=new MessageTrans(messageType, message);
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		if(allRoomId.containsKey(roomId)){
			for(Session s:allRoomId.get(roomId)){
				try {
					System.out.println("发送信息中，当前session是："+s.getUserProperties().get("USERNAME_KEY"));
					s.getBasicRemote().sendObject(mt);
				} catch (IOException|EncodeException e) {
					e.printStackTrace();
					System.out.println("sendMessage出错啦……"+e.getMessage());
				}
			}
		}
	}
	/**
	 * 向B端(除了自己的其他用户)发送信息(群发)
	 * @param messageType 待发送信息类型
	 * @param message 待发送信息内容
	 */
	private void sendMessageExceptMe(String messageType,String message){
		MessageTrans mt=new MessageTrans(messageType, message);
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		int userId=(Integer)this.session.getUserProperties().get("USERID_KEY");
		if(allRoomId.containsKey(roomId)){
			for(Session s:allRoomId.get(roomId)){
				if(!((Integer)s.getUserProperties().get("USERID_KEY")==userId)){
					try {
						System.out.println("发送信息中，当前session是："+s.getUserProperties().get("USERNAME_KEY"));
						s.getBasicRemote().sendObject(mt);
					} catch (IOException|EncodeException e) {
						e.printStackTrace();
						System.out.println("sendMessage出错啦……"+e.getMessage());
					}
				}
			}
		}
	}
	/**
	 * 向B端(仅给自己)发送信息(单发)，另，Map<K,Map<k,v>>即可实现私信
	 * @param messageType 要发送的信息类型
	 * @param message 要发送的信息内容
	 */
	private void sendMessageOnlyMe(String messageType,String message){
		MessageTrans mt=new MessageTrans(messageType, message);
		try {
			System.out.println("给自己发送信息：");
			this.session.getBasicRemote().sendObject(mt);
		} catch (IOException|EncodeException e) {
			e.printStackTrace();
			System.out.println("sendMessageForOne出错啦……"+e.getMessage());
		}
	}
	/**
	 * 向B端(指定的用户)发送信息(单发)
	 * @param messageType 待发送信息类型
	 * @param message 待发送信息具体内容
	 * @param userId 待发送用户Id
	 */
	private void sendMessageToSpecifiedUser(String messageType,String message,int userId){
		MessageTrans mt=new MessageTrans(messageType, message);
		String roomId=(String)this.session.getUserProperties().get("ROOMID_KEY");
		if(allRoomId.containsKey(roomId)){
			for(Session s:allRoomId.get(roomId)){
				if((Integer)s.getUserProperties().get("USERID_KEY")==userId){
					try {
						System.out.println("向指定用户发送信息中，指定session是："+s.getUserProperties().get("USERNAME_KEY"));
						s.getBasicRemote().sendObject(mt);
					} catch (IOException|EncodeException e) {
						e.printStackTrace();
						System.out.println("sendMessage出错啦……"+e.getMessage());
					}
					break;
				}
			}
		}
	}
	
	private static synchronized int getOnlineCount() {
		return onlineCount;
	}
	private static synchronized void addOnlineCount() {
		DrawingServer.onlineCount++;
    }
	private static synchronized void subOnlineCount() {
        DrawingServer.onlineCount--;
    }
}
