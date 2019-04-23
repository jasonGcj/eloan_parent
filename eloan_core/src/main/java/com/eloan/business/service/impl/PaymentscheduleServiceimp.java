package com.eloan.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.base.util.DateUtil;
import com.eloan.business.domain.Account;
import com.eloan.business.domain.Bid;
import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestAuditHistory;
import com.eloan.business.domain.PaymentSchedule;
import com.eloan.business.domain.PaymentScheduleDetail;
import com.eloan.business.domain.PaymentScheduleQueryObject;
import com.eloan.business.mapper.BidMapper;
import com.eloan.business.mapper.BidRequestAuditHistoryMapper;
import com.eloan.business.mapper.BidRequestMapper;
import com.eloan.business.mapper.PaymentScheduleDetailMapper;
import com.eloan.business.mapper.PaymentScheduleMapper;
import com.eloan.business.service.AccuctFlowService;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.PaymentscheduleService;
import com.eloan.business.util.BidConst;
import com.eloan.business.util.CalculatetUtil;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class PaymentscheduleServiceimp implements PaymentscheduleService {
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	@Autowired
	private  PaymentScheduleMapper paymentScheduleMapper;
	  
    @Autowired
	private IAccountService accountService ;
    
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper ;
    
    @Autowired
    private BidRequestMapper bidRequestMapper ;
    
    @Autowired
    private BidMapper bidMapper ;
	    
    @Autowired
   	private AccuctFlowService accuctFlowService;     

	@Override
	public PageResult selectReturnList(PaymentScheduleQueryObject q) {
		
		   // 查询个数
		int  count = this.paymentScheduleMapper.queryForCount(q);
		if (count > 0) {
			   // 查询列表
			List<PaymentSchedule> list = paymentScheduleMapper.query(q);
			 return new PageResult(count, q.getPageSize(), q.getCurrentPage(), list);
		}
		return PageResult.empty(q.getPageSize());
		
	}

	@Override
	public PageResult selectshouList(PaymentScheduleQueryObject q) {
		System.out.println("查询收款的个数:..................");
		Long logininfoId = q.getLogininfoId();
		// 查询个数
		//int  count = this.paymentScheduleDetailMapper.queryForCount(q);
		int  count = 6;
		if (count > 0) {
			// 查询列表
			List<PaymentScheduleDetail> list = paymentScheduleDetailMapper.selectByPaymentSchedule(logininfoId);
			//return new PageResult(count, 10, 10, list);
			return new PageResult(20, 10, 1, list);
		}
		return PageResult.empty(q.getPageSize());
	}


	/**
		 * 放款流程：
		 * 	生成还款计划和回款计划
		 *  变更借款用户和投资用户的账户
		 *  生成放款的流水账单
		 */
	@Transactional
	public void callbackMoney(BidRequest bidRequest,BidRequestAuditHistory bidRequestAuditHistory) {
		
		// 添加发标历史
		bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
		//更新借款标状态
		bidRequestMapper.updateByPrimaryKey(bidRequest);
			
		Date date = new Date();
		int monthes2Return = bidRequest.getMonthes2Return();
		
		//生成还款计划
		PaymentSchedule psTemplate = new PaymentSchedule();
		psTemplate.setBidRequestId(bidRequest.getId());// 对应借款 
		psTemplate.setBidRequestTitle(bidRequest.getTitle());//借款名称
		psTemplate.setBorrowUser(bidRequest.getCreateUser()); // 还款人
		psTemplate.setState(BidConst.PAYMENT_STATE_NORMAL);// 本期还款状态（默认正常待还）
		psTemplate.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);// 借款类型
		psTemplate.setReturnType(bidRequest.getReturnType());// 还款方式，等同借款(BidRequest)中的还款方式
		
		//获取该借款标的所有投标列表
		List<Bid> bids = bidRequest.getBids();
		log.info("获取该借款标的所有投标列表,结果为:{}",bids);
		
		int bidCount = bidRequest.getBidCount();
		for (int i = 0; i < monthes2Return; i++) {
			int monthIndex = i+1;// 第几期 (即第几个月)
			
			PaymentSchedule ps = new PaymentSchedule();
			//ps.setBidRequestTitle("我的标题");//借款名称
			BeanUtils.copyProperties(psTemplate, ps);
			//待处理的字段
			Date deadLine = DateUtil.addMonth(date, monthIndex);
			
			
			ps.setDeadLine(deadLine);	// 本期还款截止期限
			// 计算每期还款
			BigDecimal monthToReturnMoney = CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType()
							,bidRequest.getBidRequestAmount()
							,bidRequest.getCurrentRate()
							,monthIndex
							,bidRequest.getMonthes2Return());	
			//计算每期利息
			BigDecimal monthlyInterest = CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType()
					,bidRequest.getBidRequestAmount()
					,bidRequest.getCurrentRate()
					,monthIndex
					,bidRequest.getMonthes2Return());
				
			BigDecimal monthlyPrincipal = monthToReturnMoney.subtract(monthlyInterest);
			// 本期还款总金额，利息 +本金
			ps.setTotalAmount(monthToReturnMoney);
			// 本期还款本金
			ps.setPrincipal(monthlyPrincipal);
			// 本期还款总利息
			ps.setInterest(monthlyInterest);
		 	// 第几期 (即第几个月)
			ps.setMonthIndex(monthIndex);
			
			//保存还款计划   useGeneratedKeys="true" keyProperty="id"
			paymentScheduleMapper.insert(ps);
			
			//还款计划的id
			Long paymentScheduleId = ps.getId();
			//生成回款计划
			PaymentScheduleDetail psDetailTemplate = new PaymentScheduleDetail();
			psDetailTemplate.setMonthIndex(monthIndex);// 第几期（即第几个月）
			psDetailTemplate.setDeadline(deadLine);;// 本期还款截止时间
			psDetailTemplate.setBidRequestId(bidRequest.getId()); // 所属哪个借款
			psDetailTemplate.setReturnType(bidRequest.getReturnType());// 还款方式
			psDetailTemplate.setPaymentScheduleId(paymentScheduleId);// 所属还款计划
			psDetailTemplate.setFromLogininfo(bidRequest.getCreateUser());// 还款人(即发标人)
			
			for (int j = 0; j < bidCount; j++) {
				Bid bid = bids.get(j);
				PaymentScheduleDetail psDetail = new PaymentScheduleDetail();
				BeanUtils.copyProperties(psDetailTemplate, psDetail);
				//待处理字段
				// 该投标人总共投标金额,便于还款/垫付查询
				BigDecimal acturalBidAmount = bid.getAvailableAmount();
				psDetail.setBidAmount(acturalBidAmount);
				// 对应的投标ID
				psDetail.setBidId(bid.getId());
				// 所占比例
				BigDecimal proportion = acturalBidAmount.divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP);
				// 本期回款总金额(=本金+利息)
				BigDecimal monthCollectionMoney = monthToReturnMoney.multiply(proportion);
				psDetail.setTotalAmount(monthCollectionMoney);
				
				// 本期应回款本金
				BigDecimal monthCollectionPrincipal = monthlyPrincipal.multiply(proportion);
				psDetail.setPrincipal(monthCollectionPrincipal);
				// 本期应回款利息
				BigDecimal monthCollectionInterest = monthlyInterest.multiply(proportion);
				psDetail.setInterest(monthCollectionInterest);
				// 收款人(即投标人)
				psDetail.setToLogininfoId(bid.getBidUser().getId());
				//保存回款计划明细
				paymentScheduleDetailMapper.insert(psDetail);

			}
						
		}
		
		//借款人成功借款：可用余额增加，增加待还本息字段
		Account borrowAccount = this.accountService.get(bidRequest.getCreateUser().getId());
		borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
		borrowAccount.setUnReturnAmount(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount()));	
		
		accountService.update(borrowAccount);
				
		accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL, bidRequest.getBidRequestAmount(), borrowAccount);
		
		//投标人投标成功：冻结金额减少，增加待收本金 和 待收利息 字段
		for (int j = 0; j < bidCount; j++) {
			Bid bid = bids.get(j);
			Account bidAccount = this.accountService.get(bid.getBidUser().getId());
			
			//冻结金额减少投标额度
			bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
			//计算一次投标实际获得的利息=投标金额/借款金额 *总利息
			BigDecimal unReceiveInterest = CalculatetUtil.calBidInterest( bidRequest.getBidRequestAmount(), monthes2Return,bidRequest.getCurrentRate(),bidRequest.getReturnType(), bid.getAvailableAmount() );
			bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().add(bid.getAvailableAmount()));
			bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(unReceiveInterest));
			
			accountService.update(bidAccount);
			
			accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL, bid.getAvailableAmount(), bidAccount);

		}	
		
		
		
	}
	  

                  
		/**
		 * 还款流程
		 *  更新还款计划和回款计划
		 *  变更借款用户和投资用户的账户
		 *  生成相关的还款、回款、支付利息管理费等流水账单
		 *  	如果当前还的是最后一期,则更新借款标的状态为已完结
		 */
		@Transactional
		public void returnMoney(Long id,Logininfo current) {
			Date date = new Date();
			//得到还款对象  判断状态
			PaymentSchedule ps = this.paymentScheduleMapper.selectByPrimaryKey(id);
			//处于待还款  ,  并且当前是为自己的借款还款 
			if (ps.getState()==BidConst.PAYMENT_STATE_NORMAL
					&& ps.getBorrowUser().getId().equals(current.getId())) {
				//借款人账户余额大于还款金额 
				Account returnAccount = this.accountService.get(ps.getBorrowUser().getId());
				if (returnAccount.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0) {
			//执行还款
				//对于还款对象,修改状态
					ps.setState(BidConst.PAYMENT_STATE_DONE);
					ps.setPayDate(new Date());
					this.paymentScheduleMapper.updateByPrimaryKey(ps);
				//对于借款人
					//可用金额减少生成还款流水
					BigDecimal usableAmount = returnAccount.getUsableAmount();
					BigDecimal totalAmount = ps.getTotalAmount();
					BigDecimal subtract = usableAmount.subtract(totalAmount);
					returnAccount.setUsableAmount(subtract);
					
					//待还金额减少 剩余信用额度增加
					returnAccount.setUnReturnAmount(returnAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));
					returnAccount.setRemainBorrowLimit(returnAccount.getRemainBorrowLimit().add(ps.getPrincipal()));
				//对于投资人
					//遍历还款明细对象
					Map<Long, Account> updates = new HashMap<>();
					for (PaymentScheduleDetail  psd : ps.getPaymentScheduleDetails()) {
						Long bidAccountId = psd.getToLogininfoId();
						Account bidAccount = updates.get(bidAccountId);
						if (bidAccount == null ) {
							bidAccount=this.accountService.get(bidAccountId);
							 updates.put(bidAccountId, bidAccount);
						}
						
					//得到投资人对象,增加账户的可用余额
						BigDecimal usableAmount2 = bidAccount.getUsableAmount();
						BigDecimal totalAmount2 = psd.getTotalAmount();
						BigDecimal add = usableAmount2.add(totalAmount2);
						bidAccount.setUsableAmount(add);
					//减少待收本金和待收利息
						bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
						bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(psd.getInterest()));
					//支付利息管理 费
						BigDecimal interestChargeFee = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
						bidAccount.setUsableAmount(bidAccount.getUsableAmount().subtract(interestChargeFee));
						
					//修改每一期回款
						psd.setPayDate(date);
						this.paymentScheduleDetailMapper.updateByPrimaryKey(psd) ;
						
						//记录账单流水:回款   支付利息管理费
						accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY, psd.getTotalAmount(), bidAccount);
						accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE, interestChargeFee, bidAccount);
					}
					for (Account  account : updates.values()) {
						accountService.update(account);
					}
				//对于借款,如果当前还的是最后一期,借款状态变成已完成状态 ,修改投标信息
					PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
					qo.setBidRequestId(ps.getBidRequestId());
					qo.setPageSize(-1) ;
					List<PaymentSchedule>  psss = this.paymentScheduleMapper.query(qo);
					
					//遍历查询是否还有没有还款的还款计划 
					boolean unReturn = false ;
					for (PaymentSchedule temp : psss) {
						if (temp.getState() == BidConst.PAYMENT_STATE_NORMAL
								|| temp.getState() == BidConst.PAYMENT_STATE_OVERDUE) {
							unReturn = true ;
							break ;
						}
					}
					
					if ( !unReturn) { //说明是最后一期
						BidRequest bidRequest =bidRequestMapper.selectBidRequestById(ps.getBidRequestId());
						bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
						bidRequestMapper.updateByPrimaryKey(bidRequest);
						bidMapper.updateBidState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK) ;
					}
					
					
					accountService.update(returnAccount);

					//记录账单流水：还款
					accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY, ps.getTotalAmount(), returnAccount);
				}
			}
		}



		@Override
		public List<PaymentSchedule> selectAllReturnList(PaymentScheduleQueryObject q) {
			return paymentScheduleMapper.queryAll(q);
		}

}
