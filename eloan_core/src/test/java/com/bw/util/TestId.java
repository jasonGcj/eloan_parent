package com.bw.util;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eloan.base.domain.IpLog;
import com.eloan.base.domain.PaymentInfo;
import com.eloan.base.mapper.IpLogMapper;
import com.eloan.base.mapper.PaymentInfoMapper;

public class TestId {
	
	public static void main(String[] args) {
		
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		/*IpLogMapper ipLogMapper = applicationContext.getBean(IpLogMapper.class);
		
		IpLog ipLog = new IpLog();
		ipLog.setIp("0.0.0.0");
		ipLog.setLoginInfoId(6L);
		ipLog.setLoginState(0);
		ipLog.setLoginTime(new Date());
		ipLog.setLoginType(0);
		ipLog.setUsername("zhangsan");
		
		ipLogMapper.insert(ipLog);
		
		System.out.println("刚刚插入的ipLog的主键为:"+ipLog.getId());*/
		PaymentInfoMapper paymentInfoMapper = applicationContext.getBean(PaymentInfoMapper.class);
		
		PaymentInfo payInfo = new PaymentInfo();
		payInfo.setCreateuserId(666L);
		payInfo.setState(1);
		paymentInfoMapper.insert(payInfo);
		
		System.out.println("刚刚插入的PaymentInfo的主键为:"+payInfo.getId());
		
		
	}

}
