package com.eloan.base.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
	
	boolean needLogin();//是否需要登录
	
	int seconds();//指定的秒数
	
	int count();//指定的次数
	

}
