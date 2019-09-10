package com.drawsomething.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，拦截直接对JSP页面的请求
 * @author L
 *
 */
public class LoadingJspFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)servletRequest;
		HttpServletResponse response=(HttpServletResponse)servletResponse;
		
		//System.out.println("Filter路径拦截-URL："+request.getRequestURL());//URL-http://localhost:8080/drawsomething/game/gamelobby
		System.out.println("Filter路径拦截-URI："+request.getRequestURI());//URI-/drawsomething/game/gamelobby
		//System.out.println("上下文："+request.getContextPath());//contextPath-/drawsomething
		
		/* 1、初次请求发送http://localhost:8080/drawsomething/被拦截,将其重定向到
		 * http://localhost:8080/drawsomething/index.jsp,此时URI为drawsomething/index.jsp
		 * 进入if语句,即filter放行
		 * 2、其他以*.jsp结尾的请求也全部定向到index.jsp
		 */
		
		if(request.getRequestURI().contains("/index.jsp")){
			System.out.println("包含index.jsp");
			filterChain.doFilter(request, response);
		}else{
			System.out.println("重定向到index.jsp");
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {	
	}

}
