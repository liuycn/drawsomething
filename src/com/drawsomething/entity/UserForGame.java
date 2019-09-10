package com.drawsomething.entity;

/**
 * 用于携带房间信息
 * @author L
 *
 */
public class UserForGame extends User{
	
	public UserForGame(){
		super();
	}
	public UserForGame(int userId,String userName,int score){
		super(userId,userName,score);	
	}
	
	@Override
	public String toString(){
		return "UserForGame类:"+super.toString();
	}
	
}
