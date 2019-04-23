package com.eloan.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.domain.PaymentInfo;
import com.eloan.base.mapper.PaymentInfoMapper;
import com.eloan.base.service.PayService;

@Service
public class PayServiceImpl implements PayService {


	@Autowired
	private PaymentInfoMapper paymentInfoMapper;
	
	@Override
	public PaymentInfo findPayInfoByPayToken(String payToken) {
		
		//1.从redis中找到payToken对应的支付信息id
		
		//2.根据支付信息id从数据库中查询支付信息

		return null;
	}

	
	
}
