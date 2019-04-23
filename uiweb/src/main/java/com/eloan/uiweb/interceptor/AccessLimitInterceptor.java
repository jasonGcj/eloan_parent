package com.eloan.uiweb.interceptor;

import com.eloan.business.service.SystemDictionaryUtil;
import com.eloan.business.util.AccessLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

	//用户对某个接口的请求次数
	private Map<String,Integer> accessMap = new HashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		//1.获取方法的注解信息
		//instanceof 判断对象A是否被对象B所包含
		if(handler instanceof HandlerMethod){
			HandlerMethod hm = (HandlerMethod) handler;
			Method m = hm.getMethod();
			//判断对象上有没有使用注解
			String key = request.getRemoteHost()+"_"+request.getRequestURI().toString();

			if(!accessMap.containsKey(key)){
				accessMap.put(key,1);
			}else{
				accessMap.put(key,accessMap.get(key)+1);
			}
			if(accessMap.get(key).intValue()>3){
				System.out.println("请求过于频繁.............当前请求次数为:"+accessMap.get(key).intValue());
				return false;
			}

		}


		return super.preHandle(request, response, handler);
	}
}
