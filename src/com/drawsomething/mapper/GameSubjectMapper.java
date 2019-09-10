package com.drawsomething.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.drawsomething.entity.GameSubject;

/**
 * 游戏主题词条数据库操作接口
 * @author L
 *
 */
public interface GameSubjectMapper {

	/**
	 * 向词库添加新的词语信息
	 * @param gameSubject 待添加词条信息
	 * @return 操作结果
	 */
	int insertNewGameSubject(GameSubject gameSubject);
	
	/**
	 * 随机取出一条可用状态的词语
	 * @return 查询到的词条信息
	 */
	GameSubject selectOneGameSubjectByRandom();
	
	/* 一次性查询可用状态下的所有词语(改为分页查询)
	 * List<GameSubject> selectAllGameSubject();
	 */
	
	/**
	 * 查询可用状态下的记录(词语)条数
	 * @return 可用的词条总数
	 */
	int selectTotalGameSubCount();
	
	/**
	 * 查询可用状态下的(当前页范围内的)词语
	 * @param startPosition 起始值
	 * @param unitCount 需查询条数
	 * @return 条件范围内的词条信息
	 */
	List<GameSubject> selectGameSubForCurPage(@Param("position")int startPosition,@Param("unitCount")int unitCount);
	
	/**
	 * 将待删除词汇的subjectStatus值改为0
	 * @param subjectId 待删除信息Id
	 * @return 操作结果
	 */
	int updateSubIdStatus(int[] subjectId);
}
