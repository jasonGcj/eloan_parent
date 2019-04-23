package com.eloan.business.service;

import java.util.List;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestAuditHistory;
import com.eloan.business.domain.PaymentSchedule;
import com.eloan.business.domain.PaymentScheduleQueryObject;

public interface PaymentscheduleService {

	public PageResult selectReturnList(PaymentScheduleQueryObject q);
	public PageResult selectshouList(PaymentScheduleQueryObject q);

	
	public List<PaymentSchedule> selectAllReturnList(PaymentScheduleQueryObject q);

	public void returnMoney(Long id,Logininfo current);

	public void callbackMoney(BidRequest bidRequest,BidRequestAuditHistory bidRequestAuditHistory);
}
