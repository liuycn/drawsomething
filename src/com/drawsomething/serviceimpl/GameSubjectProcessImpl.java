package com.drawsomething.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.Page;
import com.drawsomething.mapper.GameSubjectMapper;
import com.drawsomething.service.GameSubjectProcess;

/**
 * 游戏业务模块实现类
 * @author L
 *
 */
@Service
@Qualifier("gameSubjectProcess")
public class GameSubjectProcessImpl implements GameSubjectProcess{

	@Autowired
	@Qualifier("gameSubjectMapper")
	private GameSubjectMapper gameSubjectMapper;//游戏模块持久层接口
	
	@Override
	public boolean addNewGameSubject(GameSubject gameSubject) {
		gameSubject.setSubjectStatus(1);//1表示改词可用状态
		System.out.println("插入前："+gameSubject);
		int count=this.gameSubjectMapper.insertNewGameSubject(gameSubject);
		System.out.println("addNewGameSubject,count:"+count);
		if(count==1){
			return true;
		}else{
			return false;
		}	
	}
	
	@Override
	public GameSubject getOneGameSubject(){
		GameSubject gm=null;
		while(gm==null){
			System.out.println("gameSubject为空");
			gm=this.gameSubjectMapper.selectOneGameSubjectByRandom();
			System.out.println("随机获取一条词库信息："+gm);
		}
		return gm;
	}
	
	@Override
	public Page getPageInfo(int currentPage){
		int totalCount=this.gameSubjectMapper.selectTotalGameSubCount();
		return new Page(currentPage, 10, totalCount);//每页展示10条数据
	}
	
	@Override
	public List<GameSubject> getGameSubForCurPage(Page page){
		List<GameSubject> subList=new ArrayList<>();
		if(page.getTotalCount()==0){//没有数据，不查询
		}else{
			subList=this.gameSubjectMapper.selectGameSubForCurPage(page.getStartPosition(), page.getUnitCount());
		}
		return subList;
	}
	
	@Override
	public boolean deleteGameSubject(String subIdList_str){
		String[] sIdList=subIdList_str.split(",");
		int[] subIdList=new int[sIdList.length];
		for(int i=0;i<sIdList.length;i++){
			subIdList[i]=Integer.valueOf(sIdList[i]);
		}		
		int count=this.gameSubjectMapper.updateSubIdStatus(subIdList);
		System.out.println("count:"+count);
		if(count!=0){
			return true;
		}else{
			return false;
		}
		
	}
}
