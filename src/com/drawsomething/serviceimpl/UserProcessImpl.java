package com.drawsomething.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drawsomething.entity.UserForGame;
import com.drawsomething.entity.UserForLogin;
import com.drawsomething.service.UserProcess;
import com.drawsomething.mapper.UserMapper;

/**
 * 用户模块业务层实现类
 * @author L
 *
 */
@Service
@Qualifier("userProcess")
public class UserProcessImpl implements UserProcess {
	
	@Autowired
	@Qualifier("userMapper")
	private UserMapper userMapper; //用户模块持久层接口
	/*
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}*/
	
	@Override
	public UserForLogin checkUserLogin(UserForLogin userCheck){		
		UserForLogin userInfo=this.userMapper.getUserInfoForLogin(userCheck.getUserId());
		System.out.println("checkUserCheck:"+userCheck);
		System.out.println("checkUserLogin:"+userInfo);
		if(userInfo==null){
			return null;
		}else if(!userInfo.getUserPwd().equals(userCheck.getUserPwd())){	
			return null;
		}else {
			return userInfo;
		}		
	}

	@Override
	public List<UserForGame> getUsersForLeaderBoard(){
		/*  也可以使用HashMap接收返回值:SELECT userName "userName",score "score" FROM ……
		 *  List<HashMap<String, Object>> users=this.userMapper.selectUsersForLeaderBoard();
		 *  List<User> newUsers=new ArrayList<User>();
		 *  for(HashMap<String, Object> hashMap:users){
		 *  newUsers.add(new UserForGame(0, (String)hashMap.get("userName"), (Integer)hashMap.get("score")));
		 *  }
		 *  return newUsers;
		 */
		List<UserForGame> users=this.userMapper.selectUsersForLeaderBoard();
		System.out.println("排行榜信息:"+users);
		return users;
	}
	
	@Override
	public void updateUserScore(int userId, int score) {
		System.out.println("--->待修改userId："+userId+",当前得分score:"+score);
		int count=this.userMapper.updateUserScore(userId, score);
		System.out.println("--->修改成功后返回值count："+count);
	}
	
	@Override
	public int register(UserForLogin user){
		user.setUserStatus(2);;
		user.setScore(0);
		System.out.println("待注册用户信息："+user);
		
		int count=this.userMapper.insertNewUser(user);
		System.out.println("注册成功后返回值count："+count+",user信息(含userId)："+user);
		
		return user.getUserId();
	}
}