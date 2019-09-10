package com.drawsomething.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截器，拦截路径检测session
 * @author L
 *
 */
public class LoadingJspPageInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		/*  该拦截器只拦截@Controller注解的类和方法，以及静态资源如js、html/css等，但是不拦截jsp页面
		 *  可以将jsp页面放在WEB-INF文件下，或者使用servlet的Filter接口来拦截
		 *  注：考虑springMVC的理念是不直接访问JSP页面的，而是通过controller跳转
		 */
		System.out.println("------------拦截器开始----------------");
		String preUri=request.getRequestURI();
		HttpSession session=request.getSession();
		System.out.println("拦截器的postHandle-URI:"+preUri);
		System.out.println("拦截器的postHandle-session-userId:"+session.getAttribute("userId")+",userStatus:"+session.getAttribute("userStatus"));
		
		if(preUri.contains("/gamelobby")){//访问game/gamelobby
			System.out.println("111game/gamelobby");
			if(session.getAttribute("userId")==null){
				System.out.println("session为空，添加临时信息");
				session.setAttribute("userId", this.getNewUserId());
				session.setAttribute("userName", "路人甲");//成功改变session值，但是为何controller中无法改变？
				session.setAttribute("score", 0);
				System.out.println("session："+session.getAttribute("userName"));
			}else{
				//System.out.println("session不空，直接放行");
				//session.setAttribute("userName", "路人甲");//测试，可以在有值的情况下改变session的值
			}
			return true;
		}else if(preUri.contains("/loadingAddSubjectPage")||preUri.contains("/addNewSubject")
				||preUri.contains("/loadingdedeleteSubPage")||preUri.contains("deleteGameSubject")){//后台管理部分的请求
			System.out.println("222后台请求");
			//null不是对象，不能调用.equels(),只能是o.equels(null),
			//若用户账号管理页面存在userId不为空，但userStatus为空，若直接对userStatus进行！=1会出现空指针错误，需要用短路或先判断是否为空
			if(session.getAttribute("userId")==null||session.getAttribute("userStatus")==null||((Integer)session.getAttribute("userStatus")!=1)){
				//System.out.println("session为空或userStatus不等于1，需要跳转回index.jsp");
				response.sendRedirect(request.getContextPath()+"/index.jsp");//http://localhost:8080/drawsomething/index.jsp
				//response.sendRedirect("index.jsp");//当前url+../index.jsp,即http://localhost:8080/drawsomething/game/loadingdedeleteSubPage/index.jsp        
				//response.sendRedirect("/index.jsp");//http://localhost:8080/index.jsp
				//throw new NoLoginException("未登陆异常");//@ExceptionHandler无法捕获
				return false;
			}else{
				System.out.println("session不空，不处理直接放行");
				return true;
			}
		}else{//其他用户登录页面如drawingPage，
			System.out.println("333必须要有session才可以访问");
			if(session.getAttribute("userId")==null){
				System.out.println("session为空，不准访问");
				response.sendRedirect(request.getContextPath()+"/index.jsp");
				return false;
			}else{
				//System.out.println("session不空，不处理直接放行");
				return true;
			}
		}
	}
	/**
	 * 根据当前时间+随机数生成userId
	 * @return 用户Id
	 */
	private String getNewUserId(){
		SimpleDateFormat sdf=new SimpleDateFormat("Mdd");
		Date date=new Date();
		return sdf.format(date)+String.valueOf(new Random().nextInt(100));
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mv)
			throws Exception {
		System.out.println("拦截器的postHandle");
		/*
		HttpSession session=request.getSession();
		if(session!=null){
			System.out.println("session-userName："+session.getAttribute("userName"));
		}*/
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
			throws Exception {
		System.out.println("拦截器的afterCompletion");
		System.out.println("------------拦截器结束----------------");
	}
	
	
	
	
	

}
