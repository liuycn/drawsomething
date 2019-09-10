package com.drawsomething.entity;

/**
 * 用户信息
 * @author L
 *
 */
public abstract class User {
	
	private int userId;
	private String userName;
	private int score;
	
	public User(){
		super();
	}
	public User(int userId,String userName,int score){
		super();
		this.setUserId(userId);
		this.setUserName(userName);
		this.setScore(score);
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String toString(){
		return "userID:"+this.getUserId()+",userName:"+this.getUserName()+",Score:"+this.getScore();
	}
}
