package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eloan.business.domain.PaymentSchedule;
import com.eloan.business.domain.PaymentScheduleDetail;
import com.eloan.business.domain.PaymentScheduleQueryObject;
public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    List<PaymentScheduleDetail> selectByPaymentSchedule(Long id);

    int updateByPrimaryKey(PaymentScheduleDetail record);

	int queryForCount(PaymentScheduleQueryObject qo);

	List<PaymentScheduleDetail> query(PaymentScheduleQueryObject qo);
    
}