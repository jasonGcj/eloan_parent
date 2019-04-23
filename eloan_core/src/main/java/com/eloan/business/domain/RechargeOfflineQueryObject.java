package com.eloan.business.domain;

import org.springframework.util.StringUtils;


public class RechargeOfflineQueryObject extends AuditQueryObject {

	private Long applierId ; //申请人id  只能查看自己当前的充值记录
	
	private Long bankInfoId = -1L ; //  银行账户信息对应的id
	private String tradeCode ; //交易流水账号
	
	
	public String getTradeCode(){
		return StringUtils.hasLength(tradeCode) ? tradeCode : null ;
	}


	public Long getApplierId() {
		return applierId;
	}


	public void setApplierId(Long applierId) {
		this.applierId = applierId;
	}


	public Long getBankInfoId() {
		return bankInfoId;
	}


	public void setBankInfoId(Long bankInfoId) {
		this.bankInfoId = bankInfoId;
	}


	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	
	
}
