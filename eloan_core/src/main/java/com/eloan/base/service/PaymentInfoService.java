package com.eloan.base.service;

import com.eloan.base.domain.PaymentInfo;

public interface PaymentInfoService {
   
    int insert(PaymentInfo record);
    
    PaymentInfo selectLatest();//模拟
    
    
    PaymentInfo selectById(int id);//模拟

}