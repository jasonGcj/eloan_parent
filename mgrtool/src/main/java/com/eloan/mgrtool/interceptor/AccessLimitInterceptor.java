package com.eloan.mgrtool.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eloan.base.util.AccessLimit;
import com.eloan.base.util.UserContext;


public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
	
	
	private Map<String, Integer> accessMap = new HashMap<>();
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if(handler instanceof HandlerMethod) {
			
			HandlerMethod hm = (HandlerMethod)handler;
			
			//获取方法
			Method method = hm.getMethod();
			//判断方法是否使用了@AccessLimit注解?			
			if(method.isAnnotationPresent(AccessLimit.class)) {
				//获取AccessLimit注解信息
				AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
				
				System.out.println("+++++++++++限流防刷的配置信息为 :needLogin="+accessLimit.needLogin()+"++++++++++++++++");
				System.out.print("seconds="+accessLimit.seconds()+"++++++++++++++++");
				System.out.print("count="+accessLimit.count()+"++++++++++++++++");
				
				boolean needLogin = accessLimit.needLogin();
				int seconds = accessLimit.seconds();
				int count = accessLimit.count();
				
				//判断用户是否登录
				boolean isLogin = request.getSession().getAttribute(UserContext.LOGIN_IN_SESSION)!=null;
				if(needLogin && !isLogin) {
					//页面跳转
					response.sendRedirect("/login.html");
					return false;
				}
				
				//用户（浏览器） 对  某个接口 的访问次数
				String key = request.getRemoteHost()+"_"+ request.getRequestURI();
				if(!accessMap.containsKey(key)) {// redis.exists(key)
					accessMap.put(key, 1);//若用redis, 则使用redis.setex(key,seconds, 1);
				}else {
					accessMap.put(key,accessMap.get(key)+1);
				}
				
				if(accessMap.get(key).intValue() > count) {
					System.out.println("=====访问频次过高！！====");
					
				}
				
			}
			
		}
		

		return true;
	}
	
	
	
}
