package com.eloan.business.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.eloan.base.query.QueryObject;
import com.eloan.base.util.DateUtil;

// 借款查询 
public class BidRequestQueryObject extends QueryObject{
	 
	  // 借款状态
	private int bidRequestState = -1;
	private int[] bidRequestStates; // 要查询的多个值
	private String orderBy; // 按照那个列表排序
	private String orderType; // 按照什么类型排序
	private Long creatorId;//借款人的id
	private Date beginDate ;
	private Date endDate ;
 	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setBeginDate(Date beginDate){
		this.beginDate = beginDate ;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setEndDate(Date endDate){
		this.endDate = endDate ;
	}
	
	//获取到endDate时间的最后一秒
	public Date getEndDate(){
		return endDate == null ? null : DateUtil.endOfDay(endDate);
	}
	
	public Date getBeginDate() {
		return beginDate;
	}
	
	  

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public int getBidRequestState() {
		return bidRequestState;
	}

	public void setBidRequestState(int bidRequestState) {
		this.bidRequestState = bidRequestState;
	}

	public int[] getBidRequestStates() {
		return bidRequestStates;
	}

	public void setBidRequestStates(int[] bidRequestStates) {
		this.bidRequestStates = bidRequestStates;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
	   

}
