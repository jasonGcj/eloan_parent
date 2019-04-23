package com.eloan.base.mapper;

import com.eloan.base.domain.PaymentInfo;

public interface PaymentInfoMapper {
   
    int insert(PaymentInfo record);

    //查询最近的一条支付信息
	PaymentInfo selectLatest();

	PaymentInfo selectById(int id);

}