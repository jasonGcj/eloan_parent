package com.eloan.base.service;

import com.eloan.base.domain.PaymentInfo;

public interface PayService {
	
	
	PaymentInfo findPayInfoByPayToken(String payToken);
	

}
