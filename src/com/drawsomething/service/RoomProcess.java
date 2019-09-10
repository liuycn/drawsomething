package com.drawsomething.service;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.Room;

/**
 * 房间模块接口
 * @author L
 *
 */
public interface RoomProcess {

	/**
	 * 获取分配的房间信息
	 * @param maxUserNum 设定房间人数上限
	 * @return 分配到的房间信息
	 */
	Room getRoomForUser(int maxUserNum);
	
	/**
	 * 实有用户人数+1/-1，更改信息列表中房间的当前人数
	 * @param room 待更改人数房间
	 */
	void changeRoomList(Room room);
	
	/**
	 * room满员检查，若满员则将room信息从列表中删除
	 * @param room 待检查房间
	 */
	void checkFullUserNumber(Room room);
	
	/*检查满员的方式：
	 * 1、websocket中对应roomid的session数量（二次开始时session没有退出，可能每个端点都得到满员结果导致数据重复发送）
	 * 2、检查gamesubject中是否存在该roomId信息，若存在表示该room已满员并进入游戏状态（但需要先确定满员并将roomId加入该列表）
	 * 3、检查room列表中该roomId是否存在（每次+1/-1后，都会通过checkFullUserNumber()方法将满员的room信息从列表清除）
	 */
	/**
	 * 通过roomId检查列表中的该room是否满员，同时用于WebSocket中游戏二次开始之前
	 * @param maxUserNum 上限人数
	 * @param roomId 待检查房间对应id
	 * @return 是否满员
	 */
	boolean checkRoomStatusByMaxNum(int maxUserNum,String roomId);
	
	/**
	 * 从DB随机随机获取一条游戏主题，并将对应roomId、该主题和绘图者Id一起加入gamesubjec列表
	 * @param roomId 等待游戏主题词的roomId
	 * @param userId 指定的绘图者Id
	 * @return 获取到的主题词汇相关信息
	 */
	GameSubject selectGameSubject(String roomId,int userId);
	
	/**
	 * 游戏结束，移除该条游戏主题信息（解除对应room的游戏状态）
	 * @param roomId 待操作的roomId
	 */
	void removeGameSubject(String roomId);
	
	/**
	 * 通过gameSubject检查room状态，若在游戏中则返回绘图者Id
	 * @param roomId 待检查的roomId
	 * @return 若房间人未满（处于等待状态）则返回-1，若在游戏状态则返回绘图者Id
	 */
	int checkRoomStatusByRoomId(String roomId);
	
	/**
	 * 检查用户的答案是否正确
	 * @param roomId 待检查roomId
	 * @param message 待检查信息内容
	 * @return 传入信息内容与当前轮次待猜词汇内容匹配为true，不匹配为false
	 */
	boolean checkUserAnswer(String roomId,String message);
}
