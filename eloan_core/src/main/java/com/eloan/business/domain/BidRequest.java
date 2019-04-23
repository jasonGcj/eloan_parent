package com.eloan.business.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.JSONObject;
import com.eloan.base.domain.Logininfo;
import com.eloan.business.util.BidConst;
import com.fasterxml.jackson.annotation.JsonFormat;
@Alias("BidRequest")
public class BidRequest{
	
	private Long id;
	private int version;// 版本号
	private int returnType=BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL;//还款方式
	private int bidRequestType=BidConst.BIDREQUEST_TYPE_NORMAL;//标类型
	private int bidRequestState=BidConst.BIDREQUEST_STATE_PUBLISH_PENDING;//这个标状态
	private BigDecimal bidRequestAmount=BidConst.ZERO;//借款金额
	private BigDecimal currentRate=BidConst.ZERO;//借款利率
	private BigDecimal minBidAmount=BidConst.SMALLEST_BID_AMOUNT;//最小投标
	private int monthes2Return=1;//借款期限
	private int bidCount=0;//已有投标数
	private BigDecimal totalRewardAmount=BidConst.ZERO;//总报酬金额
	private BigDecimal currentSum=BidConst.ZERO;//当前已经借到多少钱
	private String title="";//标名
	private String description="";//借款描述
	private String note="";//风控评审意见
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
	private Date disableDate=new Date();//招标到期时间
	private int disableDays=0;//标的有效天数
	private Logininfo createUser;//发标人
	private List<Bid> bids=new ArrayList<>();//这个借款已经有的标
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
	private Date applyDate;//申请时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
	private Date publishDate;//发布时间
	
	private Logininfo current;
	
	
	public Logininfo getCurrent() {
		return current;
	}
	public void setCurrent(Logininfo current) {
		this.current = current;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getReturnType() {
		return returnType;
	}
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	
	public int getBidRequestType() {
		return bidRequestType;
	}
	public void setBidRequestType(int bidRequestType) {
		this.bidRequestType = bidRequestType;
	}
	public int getBidRequestState() {
		return bidRequestState;
	}
	public void setBidRequestState(int bidRequestState) {
		this.bidRequestState = bidRequestState;
	}
	public BigDecimal getBidRequestAmount() {
		return bidRequestAmount;
	}
	public void setBidRequestAmount(BigDecimal bidRequestAmount) {
		this.bidRequestAmount = bidRequestAmount;
	}
	public BigDecimal getCurrentRate() {
		return currentRate;
	}
	public void setCurrentRate(BigDecimal currentRate) {
		this.currentRate = currentRate;
	}
	public BigDecimal getMinBidAmount() {
		return minBidAmount;
	}
	public void setMinBidAmount(BigDecimal minBidAmount) {
		this.minBidAmount = minBidAmount;
	}
	public int getMonthes2Return() {
		return monthes2Return;
	}
	public void setMonthes2Return(int monthes2Return) {
		this.monthes2Return = monthes2Return;
	}
	public int getBidCount() {
		return bidCount;
	}
	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}
	public BigDecimal getTotalRewardAmount() {
		return totalRewardAmount;
	}
	public void setTotalRewardAmount(BigDecimal totalRewardAmount) {
		this.totalRewardAmount = totalRewardAmount;
	}
	public BigDecimal getCurrentSum() {
		return currentSum;
	}
	public void setCurrentSum(BigDecimal currentSum) {
		this.currentSum = currentSum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}
	public int getDisableDays() {
		return disableDays;
	}
	public void setDisableDays(int disableDays) {
		this.disableDays = disableDays;
	}
	public Logininfo getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Logininfo createUser) {
		this.createUser = createUser;
	}
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	//获取到进度条
		public BigDecimal getPersent(){
			return this.currentSum.divide(this.bidRequestAmount, BidConst.DISPLAY_SCALE, RoundingMode.HALF_UP);
			
		}
		public String getBidRequestStateDisplay() {
			switch (this.bidRequestState) {
			case BidConst.BIDREQUEST_STATE_PUBLISH_PENDING:
				return "待发布";
			case BidConst.BIDREQUEST_STATE_BIDDING:
				return "招标中";
			case BidConst.BIDREQUEST_STATE_UNDO:
				return "已撤销";
			case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:
				return "流标";
			case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:
				return "满标一审";
			case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:
				return "满标二审";
			case BidConst.BIDREQUEST_STATE_REJECTED:
				return "满标审核被拒";
			case BidConst.BIDREQUEST_STATE_PAYING_BACK:
				return "还款中";
			case BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK:
				return "完成";
			case BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE:
				return "逾期";
			case BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE:
				return "发标拒绝";
			default:
				return "";
			}
		}
		public String getReturnTypeDisplay() {
			switch (returnType) {
				case BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL:
					return "等额本息";
	
				case BidConst.RETURN_TYPE_MONTH_INTERST:
					return "按月到期";
					
				case BidConst.RETURN_TYPE_MONTH_PRINCIPAL:
					return "等额本金";
					
				default:
					return "";
			}
		}

		
		public String getJsonString() {
			Map<String, Object> json = new HashMap<>();
			json.put("id", id);
			json.put("username", this.createUser.getUsername());
			json.put("title", title);
			json.put("bidRequestAmount", bidRequestAmount);
			json.put("currentRate", currentRate);
			json.put("monthes2Return", monthes2Return);
			json.put("returnType", getReturnTypeDisplay());
			json.put("totalRewardAmount", totalRewardAmount);
			return JSONObject.toJSONString(json);
		}
		
		
		//获取剩余还未投满的金额 (+:add  -:subtract * :multiply  / :divide)
		public BigDecimal  getRemainAmount(){
			return this.bidRequestAmount.subtract(this.currentSum);
		}
}
