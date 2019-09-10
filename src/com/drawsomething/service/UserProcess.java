package com.drawsomething.service;

import java.util.List;

import com.drawsomething.entity.UserForGame;
import com.drawsomething.entity.UserForLogin;

/**
 * 用户模块业务层接口
 * @author L
 *
 */
public interface UserProcess {

	/**
	 * 用户登录信息检查
	 * @param userCheck 欲登录用户信息 （含id）
	 * @return 检查结果，不存在该用户返回null，存在则返回该用户信息
	 */
	UserForLogin checkUserLogin(UserForLogin userCheck);
	
	/**
	 * 获取用户积分排行榜
	 * @return 积分排行榜
	 */
	List<UserForGame> getUsersForLeaderBoard();
	
	/**
	 * 退出gameroom页面即退出游戏时更新用户分数，DrawingServer.java调用
	 * @param userId 待修改用户id
	 * @param score 该用户当前分数
	 */
	void updateUserScore(int userId,int score);
	
	/**
	 * 新用户注册
	 * @param user 待注册用户信息
	 * @return 分配给待用户信息的userId
	 */
	int register(UserForLogin user);
}
