package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eloan.business.domain.PaymentSchedule;
import com.eloan.business.domain.PaymentScheduleQueryObject;


public interface PaymentScheduleMapper {
	

    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);


    int updateByPrimaryKey(PaymentSchedule record);

	int queryForCount(PaymentScheduleQueryObject qo);

	List<PaymentSchedule> query(PaymentScheduleQueryObject qo);
	
	
	List<PaymentSchedule> queryAll(PaymentScheduleQueryObject qo);
    
    
}