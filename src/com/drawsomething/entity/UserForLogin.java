package com.drawsomething.entity;

/**
 * 用户登录类，携带密码&身份信息(用户或管理员)
 * @author L
 *
 */
public class UserForLogin extends User{
	
	private String userPwd;
	//权限区分，用户or管理
	private int userStatus;
	

	public UserForLogin(){
		super();
	}
	public UserForLogin(int userId,String userName,int score,String userPwd,int status){
		super(userId, userName, score);
		this.setUserPwd(userPwd);
		this.setUserStatus(status);;
	}
	
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	
	@Override
	public String toString(){
		return super.toString()+",userPwd:"+this.getUserPwd()+",status:"+this.getUserStatus();
	}
}
