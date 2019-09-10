package com.drawsomething.service;

import java.util.List;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.Page;

/**
 * 游戏业务模块接口
 * @author L
 *
 */
public interface GameSubjectProcess {

	/**
	 * 后台管理：增加新的游戏词汇
	 * @param gameSubject 待添加词汇信息
	 * @return 添加结果
	 */
	boolean addNewGameSubject(GameSubject gameSubject);
	
	/**
	 * 游戏状态：从数据库随机获取一条词汇信息用于当前轮次游戏主题
	 * @return 词条信息
	 */
	GameSubject getOneGameSubject();
	
	/**
	 * 后台管理：用于删除词汇页面的分页查询，获取所有可删除状态的词汇总条数用于确定分页页数
	 * @param currentPage 当前页数
	 * @return 页面信息
	 */
	Page getPageInfo(int currentPage);
	
	/**
	 * 后台管理：用于删除词汇页面的分页查询，获取当前页面的待展示信息
	 * @param startPosition
	 * @param unitCount 
	 * @return 当前页面待展示词条信息
	 */
	List<GameSubject> getGameSubForCurPage(Page page);
	
	/**
	 * 后台管理：批量删除选中的subjectId
	 * @param subIdList_str
	 * @return 操作结果
	 */
	boolean deleteGameSubject(String subIdList_str);
}
