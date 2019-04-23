package com.eloan.business.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.eloan.base.domain.Logininfo;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 线下充值单
 *
 */


public class RechargeOffline extends BaseAuditDomain{

	private PlatformBankinfo bankInfo ;   //银行信息对象
	private String 	tradeCode ; //交易号
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
	private Date tradeTime ; //交易时间
	private BigDecimal amount ;//  交易金额
	private String note ; //交易说明
	private Long id;
	private String remark;
	private Logininfo current;
	
	
	
	
	public Logininfo getCurrent() {
		return current;
	}

	public void setCurrent(Logininfo current) {
		this.current = current;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd")
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	public String getJsonString(){
		Map<String, Object> json  = new HashMap<>();
		json.put("id", id);
		json.put("username", this.getApplier().getUsername());
		json.put("tradeCode", tradeCode);
		json.put("tradeTime", tradeTime);
		json.put("amount", amount);
		json.put("note", note);
		
		return JSONObject.toJSONString(json);
	}

	public PlatformBankinfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(PlatformBankinfo bankInfo) {
		this.bankInfo = bankInfo;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	@Override
	public String toString() {
		return "RechargeOffline [bankInfo=" + bankInfo + ", tradeCode=" + tradeCode + ", tradeTime=" + tradeTime
				+ ", amount=" + amount + ", note=" + note + ", id=" + id + "]";
	}
	
	
	
	
	
}
