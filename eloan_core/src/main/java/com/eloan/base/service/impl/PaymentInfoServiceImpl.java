package com.eloan.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.domain.PaymentInfo;
import com.eloan.base.mapper.PaymentInfoMapper;
import com.eloan.base.service.PaymentInfoService;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

	@Autowired
	private PaymentInfoMapper paymentInfoMapper;
	@Override
	public int insert(PaymentInfo record) {
		
		return paymentInfoMapper.insert(record);
	}
	
	
	@Override
	public PaymentInfo selectLatest() {
		return paymentInfoMapper.selectLatest();
	}


	@Override
	public PaymentInfo selectById(int id) {
		return paymentInfoMapper.selectById(id);
	}

}
