package com.drawsomething.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.Room;
import com.drawsomething.service.GameSubjectProcess;
import com.drawsomething.service.RoomProcess;

/**
 * 房间模块实现类
 * @author L
 *
 */
@Service
@Qualifier("roomProcess")
public class RoomProcessImpl implements RoomProcess{
	
	//存储所有房间信息，<房间max人数,该max值对应的所有房间信息>
	private static ConcurrentHashMap<Integer, List<Room>> allRoom=new ConcurrentHashMap<>();
	//某个max值对应的所有房间信息，<房间信息>
	//private List<Room> allRoomInfo=new ArrayList<>();
	private List<Room> allRoomInfo;
	
	//存储所有room对应的当前轮次游戏词和指定的绘图者Id，<roomId, GameSubject+userId>
	private static ConcurrentHashMap<String,String> allGameSubject=new ConcurrentHashMap<>();

	@Autowired
	@Qualifier("gameSubjectProcess")
	private GameSubjectProcess gameSubjectProcess;//游戏主题业务类
	
	/**
	 * 设定allRoomInfo信息
	 * @param allRoomInfo
	 */
	public void setAllRoomInfo(List<Room> allRoomInfo) {
		this.allRoomInfo = allRoomInfo;
	}
	/**
	 * 获取allRoomInfo信息
	 * @param maxUserNum 待获取的room信息对应的max人数值 
	 * @return 该max人数值对应的所有room信息
	 */
	public List<Room> getAllRoomInfo(int maxUserNum) {
		if(allRoom.containsKey(maxUserNum)){
			System.out.println(maxUserNum+",存在");
			this.allRoomInfo=allRoom.get(maxUserNum);
		}else{
			System.out.println(maxUserNum+",不存在");
			this.allRoomInfo=new ArrayList<>();
			//this.allRoomInfo.clear();
			//this.allRoomInfo=null;
		}
		return allRoomInfo;
	}

	@Override
	public Room getRoomForUser(int maxUserNum) {
		//防止通过路径更改不存在的房间上限人数
		if(maxUserNum!=1&&maxUserNum!=2&&maxUserNum!=3&&maxUserNum!=5&&maxUserNum!=8&&maxUserNum!=12){
			maxUserNum=2;
		}
		String roomId=checkAvailableRoom(maxUserNum);
		System.out.println("分配到的roomId:"+roomId);
		Room newRoom=new Room(roomId, maxUserNum, 1);
		//System.out.println("getRoom方法："+newRoom);
		changeRoomList(newRoom);
		return newRoom;
	}
	/**
	 * 获取可使用roomId
	 * @param maxUserNum 指定待获取room的max人数
	 * @return 分配的roomId
	 */
	public String checkAvailableRoom(int maxUserNum) {
		if(allRoom.containsKey(maxUserNum)&&!allRoom.get(maxUserNum).isEmpty()){
			Room room=allRoom.get(maxUserNum).get(0);//获取List<room>中最靠前的Room
			System.out.println("存在可分配room"+room);
			return room.getRoomId();
		}else{
			System.out.println("创建新的roomId");
			return produceNewRoomId();
		}
	}
	/**
	 * 生成新的roomId
	 * @return 生成的新的roomId
	 */
	private String produceNewRoomId(){
		SimpleDateFormat sdf=new SimpleDateFormat("ddHHmmss");
		Date date=new Date();
		//System.out.println("produce方法：");
		return sdf.format(date)+String.valueOf(new Random().nextInt(100));
	}

	@Override
	public void changeRoomList(Room room) {//room.getPreUserNumber()=1/-1
		if(!allGameSubject.containsKey(room.getRoomId())){
			boolean flag=true;
			int maxUserNum=room.getMaxUserNumber();
			this.allRoomInfo=this.getAllRoomInfo(maxUserNum);
			System.out.println("增加");
			for(Room r:this.allRoomInfo){
				if(r.getRoomId().equals(room.getRoomId())){
					flag=false;//该room信息已经在列表中(room中不是第一次+1/-1),直接修改
					r.setPreUserNumber(r.getPreUserNumber()+room.getPreUserNumber());
					System.out.println("增加中："+r);
					this.checkFullUserNumber(r);
					break;
				}
			}
			if(flag){//room信息不存在的情况下,先将该room信息添加至room列表，再进行+1/-1操作
				flag=false;
				//System.out.println("flag=false："+room.getPreUserNumber());
				this.allRoomInfo.add(room);
				allRoom.put(maxUserNum, this.allRoomInfo);
				this.checkFullUserNumber(room);
			}
			if(allRoom.containsKey(maxUserNum)){
				for(Room r:allRoom.get(maxUserNum)){
					System.out.println("【检测】changeRoomList增加完成："+r);
				}
				System.out.println("当前max="+maxUserNum+"人数，有房间数量"+allRoom.get(maxUserNum).size());
				
			}else{
				System.out.println("【检测】changeRoomList："+maxUserNum+"不存在");
			}
			/*
			 * 区别于用=null（.clear的底层实现即=null）防止内存泄漏，此处this.allRoomInfo=null会影响到类静态变量allRoom
			 * this.allRoomInfo属于引用数据类型，通过getter()获取的到是指向其值的地址即指针，在此处对其进行.clear()操作，
			 * 必然影响原位置即allRoom中的值，造成每次room信息都在添加后被清空，下一次则必然分配到新roomId。下同
			 * PS：可以通过debug模式观察此句代码操作前后的this.allRoomInfo和allRoom的值的变化
			 */
			//this.allRoomInfo.clear();
			
		}else{
			System.out.println("游戏状态，不予处理");
		}
	}
	
	@Override
	public void checkFullUserNumber(Room room){
		this.allRoomInfo=this.getAllRoomInfo(room.getMaxUserNumber());
		/* 存在一种情况：RoomProcess将初始roomId分配完成，满员后将room从List移出，但B端有人退出，webSocket端将-1值传来，
		 * 此时List中不存在该roomId，则将该new Room(roomId, this.getRoomMaxUserNumber(), -1)存入List
		 * 即-1意味缺少1人，若继续退出，则为-2，直到List为其安排新的人员使其preUserNumber==0，此时则该room满员，移出List
		 */
		//room.getPreUserNumber()为当前实际值,存在+1/-1两种情况
		if(room.getMaxUserNumber()<=Math.abs(room.getPreUserNumber())||room.getPreUserNumber()==0){
			System.out.println("满员，service删除");
			for(Room r:this.allRoomInfo){ 
				if(r.getRoomId().equals(room.getRoomId())){
					this.allRoomInfo.remove(r);//通过roomId从列表删除该room信息
					break;
				}
			}
			System.out.println("删除后：");
			for(Room r:allRoom.get(room.getMaxUserNumber())){
				System.out.println("【检测】allRoom.get(maxUserNum)："+r);
			}/*
			if(this.allRoomInfo.isEmpty()){
				allRoom.remove(maxUserNum);
				System.out.println("【检测】移除"+maxUserNum+"对应房间");
			}*/
		}
		//同上
		//this.allRoomInfo.clear();
	}
	
	@Override
	public boolean checkRoomStatusByMaxNum(int maxUserNum,String roomId){//通过room列表检查room是否已满员
		if(allRoom.containsKey(maxUserNum)){
			System.out.println("该人数："+maxUserNum+"对应房间数量为："+allRoom.get(maxUserNum).size());
			for(Room r:allRoom.get(maxUserNum)){
				System.out.println("第二轮游戏检查,for循环中……");
				if(r.getRoomId().equals(roomId)){
					System.out.println("第二轮游戏检查中，房间"+r.getRoomId()+"未满员，当前人数："+r.getPreUserNumber());
					return false;
				}
			}
			System.out.println("第二轮游戏检查中，该房间已满员");
			return true;
		}else{
			System.out.println("第二轮游戏检查中，当前人数对应房间不存在");
			return true;
		}
		
	}
	
	@Override
	public GameSubject selectGameSubject(String roomId,int userId){
		/*
		GameSubject gameSubject1=new GameSubject("电脑","常用工具","2个字+提示1+提示2",1);
		List<GameSubject> gss=new ArrayList<>();
		gss.add(gameSubject1);
		int index=new Random().nextInt(1);
		System.out.println("index:"+index);
		GameSubject gs=gss.get(index);*/
		
		//GameSubject gs=new GameSubjectProcessImpl().getOneGameSubject();
		GameSubject gs=this.gameSubjectProcess.getOneGameSubject();
		System.out.println("获取到词语信息："+gs);
		allGameSubject.put(roomId, gs.getWordInfo()+"+"+userId);
		
		for(Entry<String,String> entry:allGameSubject.entrySet()){
			System.out.println("【检测】当前答案存储：房间号："+entry.getKey()+",主题+绘图者ID:"+entry.getValue());
		}
		return gs;
	}
	
	@Override
	public void removeGameSubject(String roomId){
		if(allGameSubject.containsKey(roomId)){
			allGameSubject.remove(roomId);
			System.out.println("removeGameSubject-if:当前roomId："+roomId+"被移除");
		}else{
			System.out.println("removeGameSubject-else:当前roomId："+roomId+"已被移除");
		}
		
		for(Entry<String,String> entry:allGameSubject.entrySet()){
			System.out.println("【检测】当前答案存储：房间号："+entry.getKey()+",主题+绘图者ID:"+entry.getValue());
		}
	}
	
	@Override
	public int checkRoomStatusByRoomId(String roomId){//room处于waiting/game状态
		if(allGameSubject.containsKey(roomId)){
			String info=allGameSubject.get(roomId);
			System.out.println("info:"+info);
			String[] infos=info.split("[+]");
			return Integer.parseInt(infos[1]);//返回绘图者ID
		}else{
			return -1;
		}
	}
	
	@Override
	public boolean checkUserAnswer(String roomId,String message){//检测用户答案是否正确
		if(allGameSubject.containsKey(roomId)){
			String info=allGameSubject.get(roomId);
			String[] infos=info.split("\\+");
			String wordInfo=infos[0];
			System.out.println("checkUserAnswer检测：wordInfo="+wordInfo+",message:"+message);
			return message.contains(wordInfo);
		}else{
			return false;
		}
	}

}
