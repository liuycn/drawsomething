package com.drawsomething.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.drawsomething.entity.UserForGame;
import com.drawsomething.entity.UserForLogin;
import com.drawsomething.service.UserProcess;

/**
 * 处理用户登录、注册等相关请求
 * @author L
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes(value={"userId","userName","score","userStatus"})
public class UserController {
	
	@Autowired
	@Qualifier("userProcess")
	private UserProcess userProcess;//用户业务类
	
	/**
	 * 处理用户登录
	 * @param userCheck 待登录用户信息
	 * @param mm 用户信息
	 * @return 信息错误返回登录页面，否则进入游戏大厅页面
	 */
	@RequestMapping("/login")
	public String userLongin(UserForLogin userCheck,ModelMap mm) {	
		System.out.println("欲登录用户userId："+userCheck.getUserId());
		System.out.println("欲登录用户userPwd："+userCheck.getUserPwd());
		
		//UserProcess up=new UserProcessImpl();
		UserForLogin userInfo=this.userProcess.checkUserLogin(userCheck);
		System.out.println("userLongin:"+userInfo);
		if(userInfo==null){
			System.out.println("userLongin为空");
			return "redirect:/index.jsp";
		}else if(userInfo.getUserStatus()==2){
			mm.put("userId", userInfo.getUserId());
			mm.put("userName", userInfo.getUserName());
			mm.put("score", userInfo.getScore());
			return "gamelobby";
		}else if(userInfo.getUserStatus()==1){
			System.out.println("userStatus"+userInfo.getUserStatus());
			mm.put("userId", userInfo.getUserId());
			mm.put("userName", userInfo.getUserName());
			mm.put("userStatus", userInfo.getUserStatus());
			//返回管理页面
			return "infomana_jsp/addnewsubject";
			//throw new NoLoginException("未登陆异常");
		}else{
			System.out.println("userLongin()方法:???");
			return "redirect:/index.jsp";
		}
	}
	
	/**
	 * 登出
	 * @param session 
	 * @param sessionStatus
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session,SessionStatus sessionStatus){
		System.out.println("session清除");	
		session.invalidate();
		sessionStatus.setComplete();//不清空HttpSession中的值
		return "forward:/user/logout";//二次请求，因设置拦截器，该路径需要有session才可以访问，当二次访问时，session已经不存在，则会被拦截器重定向到index.jsp
		/* 0、return "redirect:/index.jsp";//会自动拼接参数
		 * 1、在mvc配置文件中设置ignoreDefaultModelOnRedirect=true不带参数(3.1版本)
		 * 2、用servlet定向页面，地址栏不会自动拼接参数，此时方法返回值为void
		 * try {
		 * response.sendRedirect(request.getContextPath()+"/index.jsp");
		 * } catch (IOException e) {
		 * e.printStackTrace();
		 * }
		 * 3、进行二次请求
		 * return "forward:/user/logout2";
		 * @RequestMapping("/logout2")
		 * public String logout2(){
		 * 	System.out.println("跳转退出");
		 * 	return "redirect:/index.jsp";
		 * }
		 */
	}
	
	/**
	 * 获取排行榜用户信息
	 * @return 排行榜用户信息
	 */
	@ResponseBody
	@RequestMapping("/leaderboard")
	public List<UserForGame> getLeaderboard(){
		/*测试
		User u1=new UserForGame(1, "name1", 1);
		User u2=new UserForGame(2, "name2", 2);
		List<User> users=new ArrayList<>();
		users.add(u1);
		users.add(u2);*/
		return this.userProcess.getUsersForLeaderBoard();
	}
	
	/**
	 * 修改session中的用户score信息
	 * @param user 用户信息
	 * @param mm
	 * @param session
	 */
	@RequestMapping("/updateUserScore")
	public void updateSessionValue(@RequestBody UserForGame user,ModelMap mm){//User是个抽象类
		System.out.println("-->Ajax传入值:"+user);
		//mm.put("score",user.getScore()+20);//gamelobby.jsp个人信息实现score+20
		mm.put("score",user.getScore());
	}
	/*
	@RequestMapping("/updateUserScore/{userPreScore}")//window.location或window.open
	public String updateSessionValue(@PathVariable("userPreScore") int score ,ModelMap mm,HttpSession session){//User是个抽象类
		System.out.println("----------进来了----------------");
		System.out.println("传入值:"+score);
		System.out.println("未修改session值:"+session.getAttribute("userId")+","+session.getAttribute("userName")+","+session.getAttribute("score"));
		mm.put("score",score+10);
		System.out.println("修改了session值:"+session.getAttribute("userId")+","+session.getAttribute("userName")+","+session.getAttribute("score"));
		return "gamelobby";
	}
	*/
	
	/**
	 * 新用户注册
	 * @param user
	 * @param mm
	 * @return
	 */
	@RequestMapping("/register")
	public String userRegister(UserForLogin user,ModelMap mm){
		System.out.println("待注册用户userName:"+user.getUserName());
		System.out.println("待注册用户UserPwd:"+user.getUserPwd());
		if(user.getUserName()==null||user.getUserPwd()==null){
			System.out.println("注册信息为空，返回注册页面");
			return "register";
		}
		else{
			//new UserProcessImpl().register(user);
			int userId=this.userProcess.register(user);
			/*模拟注册
			System.out.println("模拟注册：userID为10007");
			int userId=10007;
			模拟结束*/
			mm.put("newUserId", userId);
			return "registerSuccess";
		}
	}
}
