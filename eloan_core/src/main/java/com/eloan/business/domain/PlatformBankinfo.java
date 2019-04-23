package com.eloan.business.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 平台的账户信息
 *
 */

public class PlatformBankinfo{

	 
	    
	
	private String bankName ; //银行名称
	private String accountName ; //开户人姓名
	private String accountNumber  ; //开户人银行账号
	private String forkName ; //开户支行
	private Long id;
	
   
	public String getJsonString(){
		Map<String, Object> json = new HashMap<>();
		json.put("id",id);
		json.put("bankName",bankName);
		json.put("accountName",accountName);
		json.put("accountNumber",accountNumber);
		json.put("forkName",forkName);
		return JSONObject.toJSONString(json);
		
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getForkName() {
		return forkName;
	}


	public void setForkName(String forkName) {
		this.forkName = forkName;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "PlatformBankinfo [bankName=" + bankName + ", accountName=" + accountName + ", accountNumber="
				+ accountNumber + ", forkName=" + forkName + ", id=" + id + "]";
	}
	
	


}
