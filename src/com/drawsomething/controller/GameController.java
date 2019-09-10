package com.drawsomething.controller;

import java.util.List;

//import javax.servlet.http.HttpServletRequest;关闭websocket方法传入参数

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drawsomething.entity.GameSubject;
import com.drawsomething.entity.Page;
import com.drawsomething.entity.Room;
import com.drawsomething.service.GameSubjectProcess;
import com.drawsomething.service.RoomProcess;

/**
 * 处理游戏相关请求
 * @author L
 *
 */
@Controller
@RequestMapping("/game")
public class GameController {
	
	@Autowired
	@Qualifier("gameSubjectProcess")
	private GameSubjectProcess gameSubjectProcess;//游戏主题业务类
	
	@Autowired
	@Qualifier("roomProcess")
	private RoomProcess roomProcess;//游戏房间业务类
	
	/**
	 * 通过选择的房间上限人数返回分配房间信息
	 * @param maxUserNum 待分配room上线人数
	 * @param mm 分配房间信息
	 * @return gameroom.jsp
	 */
	@RequestMapping("/gameroom/{maxUserNum}")
	public String chooseGameRoom(@PathVariable("maxUserNum")int maxUserNum,ModelMap mm){
		Room room=this.roomProcess.getRoomForUser(maxUserNum);
		mm.put("roomId", room.getRoomId());
		mm.put("maxUserNum", room.getMaxUserNumber());
		return "gameroom";
	}
	
	/**
	 * 向词库添加新的词语信息
	 * @param gameSubject 待添加词语信息
	 * @return 添加结果
	 */
	@ResponseBody
	@RequestMapping("/addNewSubject")
	public String addNewSubject(@RequestBody GameSubject gameSubject){//前台传的是JSON，在参数声明中用String分别接收或用对应类接收
		if(gameSubject==null){
			System.out.println("gameSubject==null");
			return "error";
		}
		System.out.println("conroller-addNewSubject"+gameSubject);
		boolean flag=this.gameSubjectProcess.addNewGameSubject(gameSubject);
		if(flag){
			return "success";
		}else{
			return "error";
		}	
	}
	
	/**
	 * 返回所有可用状态下的当前页范围的记录并等待删除
	 * @param currentPage 待查询页数
	 * @param mm 待查询页面信息以及该页面内所有词条信息
	 * @return 待查询页面
	 */
	@RequestMapping("/loadingdedeleteSubPage/{curPage}")
	public String showGameSubForCurPage(@PathVariable("curPage")int currentPage,ModelMap mm) {
		Page page=this.gameSubjectProcess.getPageInfo(currentPage);
		List<GameSubject> subList=this.gameSubjectProcess.getGameSubForCurPage(page);
		System.out.println("page:"+page);
		mm.put("page", page);
		mm.put("subjectList", subList);
		return "infomana_jsp/deletesubject";
	}
	
	/**
	 * 删除选中id的题目
	 * @param subIdList_str 待删除词条对应的id
	 * @return 删除结果
	 */
	@ResponseBody
	@RequestMapping("deleteGameSubject")
	public String deleteGameSubject(@RequestBody String subIdList_str){
		System.out.println("selectedId:"+subIdList_str);
		if(this.gameSubjectProcess.deleteGameSubject(subIdList_str)){
			return "success";
		}else{
			return "error";
		}	
	}	
}
