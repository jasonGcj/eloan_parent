package com.eloan.base.domain;

import java.math.BigDecimal;
import java.util.Date;


import com.eloan.business.util.BidConst;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PaymentInfo {


	private Integer id;
	/**
	 * 用户userId
	 */
	private Long createuserId;
	/**
	 * 支付状态 0 待支付、1支付成功 、2支付失败
	 */
	private Integer state;
	/**
	 * 支付渠道 0-支付宝 1-易宝支付 2-网银支付
	 */
	private Integer channel;
	/**
	 * 支付报文
	 */
	private String payMessage;
	//第三方支付平台的交易编号
	private String platformOrderId;


	private Date created;
	private Date updated;


	//支付金额
	private BigDecimal amount;

	public PaymentInfo(){}

	public PaymentInfo(Long userId, Date created, BigDecimal amount){
		this.createuserId = userId;
		this.state = 0;//待支付
		this.channel = 0;//默认使用支付宝支付
		this.created = created;
		this.amount = amount;
	}


	public BigDecimal getDisplayAmount(){

		BigDecimal showAmount = this.getAmount().setScale(BidConst.DISPLAY_SCALE);
		return showAmount;
	}

	

}