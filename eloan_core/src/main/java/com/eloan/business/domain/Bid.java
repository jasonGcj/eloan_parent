package com.eloan.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.eloan.base.domain.BaseDomain;
import com.eloan.base.domain.Logininfo;
import com.eloan.business.util.BidConst;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.ToString;
@Alias("Bid")
@ToString
public class Bid extends BaseDomain{
     private BigDecimal actualRate=BidConst.ZERO;
     private BigDecimal availableAmount=BidConst.ZERO;
     
     private Long bidRequestId;
     private String bidRequestTitle;
     private Logininfo bidUser;
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
     private Date bidTime;
     
     private int bidRequestState;

	public BigDecimal getActualRate() {
		return actualRate;
	}

	public void setActualRate(BigDecimal actualRate) {
		this.actualRate = actualRate;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public Long getBidRequestId() {
		return bidRequestId;
	}

	public void setBidRequestId(Long bidRequestId) {
		this.bidRequestId = bidRequestId;
	}

	public String getBidRequestTitle() {
		return bidRequestTitle;
	}

	public void setBidRequestTitle(String bidRequestTitle) {
		this.bidRequestTitle = bidRequestTitle;
	}

	public Logininfo getBidUser() {
		return bidUser;
	}

	public void setBidUser(Logininfo bidUser) {
		this.bidUser = bidUser;
	}

	public Date getBidTime() {
		return bidTime;
	}

	public void setBidTime(Date bidTime) {
		this.bidTime = bidTime;
	}

	public int getBidRequestState() {
		return bidRequestState;
	}

	public void setBidRequestState(int bidRequestState) {
		this.bidRequestState = bidRequestState;
	}
     
     
}
