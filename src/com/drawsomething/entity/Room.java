package com.drawsomething.entity;

import java.util.List;

/**
 * 房间信息类
 * @author L
 *
 */
public class Room {
	//房间Id
	private String roomId;
	//规定最大人数
	private int maxUserNumber;
	//当前实有人数
	private int preUserNumber;
	//用于前端传递新用户信息
	private List<UserForGame> user;
	

	public Room(){
		super();
	}
	public Room(String roomId,int maxUserNum,int preUserNum){//创建新的room使用
		super();
		this.setRoomId(roomId);
		this.setMaxUserNumber(maxUserNum);
		this.setPreUserNumber(preUserNum);
	}
	public Room(String roomId,int maxUserNum,int preUserNum,List<UserForGame> user){
		super();
		this.setRoomId(roomId);
		this.setMaxUserNumber(maxUserNum);
		this.setPreUserNumber(preUserNum);
		this.setUser(user);
	}
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public int getMaxUserNumber() {
		return maxUserNumber;
	}
	public void setMaxUserNumber(int maxUserNumber) {
		this.maxUserNumber = maxUserNumber;
	}
	public int getPreUserNumber() {
		return preUserNumber;
	}
	public void setPreUserNumber(int preUserNumber) {
		this.preUserNumber = preUserNumber;
	}
	public List<UserForGame> getUser() {
		return user;
	}
	public void setUser(List<UserForGame> user) {
		this.user = user;
	}
	
	@Override
	public String toString(){
		return "Room类：roomId:"+this.getRoomId()+",maxUserNumber:"+this.getMaxUserNumber()+",preUserNumber:"+this.getPreUserNumber()+",User:"+this.getUser();
	}

	
}
