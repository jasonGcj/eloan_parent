package com.eloan.business.domain;

/**
 * 还款计划查询对象
 *
 */

public class PaymentScheduleQueryObject extends AuditQueryObject {

	private Long logininfoId   = -1L ;
	private  Long bidRequestId ;
	public Long getLogininfoId() {
		return logininfoId;
	}
	public void setLogininfoId(Long logininfoId) {
		this.logininfoId = logininfoId;
	}
	public Long getBidRequestId() {
		return bidRequestId;
	}
	public void setBidRequestId(Long bidRequestId) {
		this.bidRequestId = bidRequestId;
	}
	@Override
	public String toString() {
		return "PaymentScheduleQueryObject [logininfoId=" + logininfoId + ", bidRequestId=" + bidRequestId + "]";
	}
	 
	
	
	 
	
}
