package com.drawsomething.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.drawsomething.entity.UserForGame;
import com.drawsomething.entity.UserForLogin;

/**
 * 用户相关操作接口
 * @author L
 *
 */
public interface UserMapper {
	
	/**
	 * 获取待登录用户信息
	 * @param userId 待登录用户Id
	 * @return 待登录用户信息
	 */
	UserForLogin getUserInfoForLogin(int userId);
	
	/**
	 * 更新用户得分
	 * @param userId 用户Id
	 * @param score 待更新score
	 * @return 操作结果
	 */
	int updateUserScore(@Param("userId")int userId,@Param("score")int score);
	
	/**
	 * 获取积分前十用户用户排行榜
	 * @return 用户信息
	 */
	List<UserForGame> selectUsersForLeaderBoard();
	
	/**
	 * 注册新用户
	 * @param user 待注册用户信息
	 * @return 操作结果
	 */
	int insertNewUser(UserForLogin user);
}
