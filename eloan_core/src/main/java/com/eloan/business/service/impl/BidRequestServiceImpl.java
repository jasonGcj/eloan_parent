package com.eloan.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.business.domain.Account;
import com.eloan.business.domain.AccountFlow;
import com.eloan.business.domain.Bid;
import com.eloan.business.domain.BidQueryObject;
import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestAuditHistory;
import com.eloan.business.domain.BidRequestQueryObject;
import com.eloan.business.domain.Userinfo;
import com.eloan.business.mapper.BidMapper;
import com.eloan.business.mapper.BidRequestAuditHistoryMapper;
import com.eloan.business.mapper.BidRequestMapper;
import com.eloan.business.service.AccuctFlowService;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.IBidRequestService;
import com.eloan.business.service.IUserService;
import com.eloan.business.service.PaymentscheduleService;
import com.eloan.business.util.BidConst;
import com.eloan.business.util.BitStatesUtils;
import com.eloan.business.util.CalculatetUtil;
import com.eloan.business.util.EntryValue;

import sun.net.www.content.text.plain;
@Service
@Transactional
public class BidRequestServiceImpl implements IBidRequestService {

    @Autowired
    private BidRequestMapper bidRequestMapper;
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private IAccountService iAccountService;
	
	@Autowired
    private  BidMapper bidMapper;
	
	@Autowired
    private  AccuctFlowService accuctFlowService;
	
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	
	@Autowired
	private PaymentscheduleService paymentscheduleService;
	
	@Override
	public void update(BidRequest bidRequest) {
          int ret = this.bidRequestMapper.updateByPrimaryKey(bidRequest);
          if(ret<=0) {
        	  throw new RuntimeException("借款乐观锁错误:"+bidRequest.getId());
          }
	}

	@Override
	public boolean canBorrow(Logininfo logininfo) {
		Userinfo userinfo = this.iUserService.get(logininfo.getId());
		Account account = this.iAccountService.get(logininfo.getId());
		//FIXME 只要满足四个条件 并且  可用征信额度>0  , 就能借款
		return  //!userinfo.getHasBidRequest()&&
			    userinfo.getBaseInfo()
			    &&userinfo.getRealAuth()
			    &&userinfo.getVedioAuth()
			    &&userinfo.getAuthScore()>=30
			    &&account.getRemainBorrowLimit().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>0;
	
	}

	@Override
	public List<BidRequest> indexList(int size) {
		// 创建查询对象 
	    BidRequestQueryObject b = new BidRequestQueryObject();
	      // 设置 查询条件  招标中 还款中 已还清
	    b.setBidRequestStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
	     // 设置显示页数
	    b.setPageSize(size);
	    // 设置当前页数
	    b.setCurrentPage(1);
	     // 设置 排序
	    b.setOrderBy("bidRequestState");
	    b.setOrderType("ASC");
	    
	return bidRequestMapper.query(b);
	}

	@Override
	public PageResult selectBidRequestList(BidRequestQueryObject q) {

		
		   // 通过 条件 查询 有没有 
		    int i = bidRequestMapper.queryForCount(q);
		    
		    if(i > 0){
		    	
		    	  // 通过条件查询 集合
		    	List<BidRequest> query = bidRequestMapper.query(q);
		    	  
		    	   // 调取工具类返回
		    	return new PageResult(i, q.getPageSize(), q.getCurrentPage(), query);
		    }
		
		return PageResult.empty(q.getPageSize());
	}

	@Override
	public BidRequest getbidRequest(Long id) {
		return bidRequestMapper.selectBidRequestById(id);
	}

	@Override
	public void borrowApply(BidRequest b,Logininfo current) {
	     // 获取账户剩余金额  
			Account account = iAccountService.get(current.getId());		
			     
			boolean applyFlag = this.canApplyBit(current.getId()) 
					   // 借款金额大于系统最小借款金额 并且系小于剩余金额
				     && b.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0
					 && b.getBidRequestAmount().compareTo(account.getBorrowLimitAmount())<=0
					    // 贷款利息大于5 小于20
					 && b.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0
					 && b.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<= 0
					    //借款金额大于系统最小投标金额
					 && b.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0;
					 
					 
			    // 判断 满足申请条件 基本信息
			   if(applyFlag){
				   
				    // 创建 BidRequest 进行赋值
				   BidRequest bidRequest = new BidRequest();
				    // 借款总金额
				   bidRequest.setBidRequestAmount(b.getBidRequestAmount());
				     // 借款年华利率
				   bidRequest.setCurrentRate(b.getCurrentRate());
				     //借款描述
				   bidRequest.setDescription(b.getDescription());
				     // 价款天数
				   bidRequest.setDisableDays(b.getDisableDays());
				     // 借款最小金额
				   bidRequest.setMinBidAmount(b.getMinBidAmount());
				     // 借款类型
				   bidRequest.setReturnType(b.getReturnType());
				      // 还款月数
				   bidRequest.setMonthes2Return(b.getMonthes2Return());
				     // 借款标题
				   bidRequest.setTitle(b.getTitle());
				     
				    // 借款时间
				   bidRequest.setApplyDate(new Date());
				   
				   bidRequest.setCreateUser(current);
				    // 借款状态
				   bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
				    
				     // 总得利息 金额  调取工具类计算总利息 
				   bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));
				   
				   bidRequest.setCurrentSum(BidConst.ZERO);
				    // 添加
				   bidRequestMapper.insert(bidRequest);
				   // 给申请者设置状态码
				   Userinfo userinfo = iUserService.get(current.getId());
				   userinfo.addState(BitStatesUtils.OP_HAS_BIDREQUEST);
				   // 修改
				   iUserService.update(userinfo);
			   }
		
	}

	private boolean canApplyBit(Long id) {
		   //得到用户信息
				Userinfo userinfo = iUserService.get(id);
				
				 System.err.println(!userinfo.getHasBidRequest()+"hahahhaahahah ");
				 System.err.println(userinfo.getBaseInfo()+"基本信息");
				 System.err.println(userinfo.getVedioAuth()+"视频认证");
				 System.err.println(userinfo.getRealAuth()+"实名认证");
				 System.err.println(userinfo.getAuthScore()+"分数");
				    // 判断认证的相关的信息
					  return userinfo != null && userinfo.getBaseInfo() && userinfo.getVedioAuth() && userinfo.getRealAuth() && userinfo.getAuthScore() >= BidConst.CREDIT_BORROW_SCORE;// && !userinfo.getHasBidRequest();
	}

	@Override
	public void addBach(Long bidRequestId, BigDecimal amount,Logininfo current) {

        // 获取借款标对象
       BidRequest b = bidRequestMapper.selectBidRequestById(bidRequestId);
        // 获取 用户及该用户的账户信息
       Long id = current.getId();
       Account account = iAccountService.get(id);
       
       Date date = new Date();
         
         // 判断 是不是为招标中
       if(b != null && BidConst.BIDREQUEST_STATE_BIDDING == b.getBidRequestState()
      		 
      		  // 判断是不是当前用户
      		 && !b.getCreateUser().getId().equals(current.getId())
      		  // 判断当前用户账户金额是否大于等于投标金额
      		 && account.getUsableAmount().compareTo(amount) >= 0
      		  // 投标金额大于小最小投标金额
      		 && amount.compareTo(b.getMinBidAmount()) >= 0
      		   // 投标金额小于等于剩余金额
      		 && amount.compareTo(b.getBidRequestAmount()) <= 0
      		 ){
      	 
      	    // 创建投标对象
      	  Bid bid = new Bid();
      	     
      	   //设置年华利率
      	   bid.setActualRate(b.getCurrentRate());
      	     // 设置投标金额
      	   bid.setAvailableAmount(amount);
      	     //设置借款id 
      	   bid.setBidRequestId(b.getId());
      	     // 设置投标主题
      	   bid.setBidRequestTitle(b.getTitle());
      	     // 设置投标时间
      	   bid.setBidTime(date);
      	     // 设置投标人id 
      	   bid.setBidUser(current);
      	    // 添加
      	   bidMapper.insert(bid);
      	     
      	    // 修改账户相关信息
      	     //减去此次相关金额
      	   account.setUsableAmount(account.getUsableAmount().subtract(amount));
      	     // 冻结金额增加
      	   account.setFreezedAmount(account.getFreezedAmount().add(amount));
      	     
      	      // 修改
      	    iAccountService.update(account);
      	       
      	     // 生成投标流水
      	    accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED, amount, account);
      	  
      	      
      	      // 修改借款相关信息
      	        // 设置当前总得投资金额
      	    b.setCurrentSum(b.getCurrentSum().add(amount));
      	    b.setBidCount(b.getBidCount()+1);
      	        // 判断是否投满
      	      if(b.getBidRequestAmount().equals(b.getCurrentSum())){
      	    	  
      	    	      // 修改标状态 标为满一审 
      	    	    b.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
      	      }
      	      
      	       // 修改
      	   this.update(b);
       }
		
	}
	@Override
	public void addBidRequest(String remark, int state, Long id,Logininfo current) {
		 
		      // 获取 审核 对象
		BidRequest b = bidRequestMapper.selectBidRequestById(id);
		
		     // 判断 是否为审核状态
		if(b != null && b.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING){
			
			 // 创建 历史审核 对象
			BidRequestAuditHistory bid = new BidRequestAuditHistory();
			
			 // 设置借款人
			bid.setApplier(b.getCreateUser());
			  // 设置申请时间
			bid.setApplyTime(b.getApplyDate());
			  // 设置申请类型  为 发标申请阶段
			bid.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
			  // 设置审核者
			bid.setAuditor(current);
			  // 设置审核时间
			bid.setAuditTime(new Date());
			  // 设置备注
			bid.setRemark(remark);
			 // 设置审核状态
			bid.setState(state);
			// 设置发标的id
			bid.setBidRequestId(b.getId());
			  // 添加发标历史
			bidRequestAuditHistoryMapper.insert(bid);			
			  // 如果 审核通过
			if(state == BidRequestAuditHistory.STATE_PASS){
				  
				 // 状态值 为 招标中
				b.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
				  // 设置风控资料 备注
				b.setNote(remark);
				 // 设置招标截止时间
				b.setDisableDate(DateUtils.addDays(new Date(), b.getDisableDays()));
			}else {				
				// 审核失败
				// 设置审核状态码
				b.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
				// 获取申请者信息
				Userinfo userinfo = iUserService.get(b.getCreateUser().getId());
				 // 删除状态码
				userinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST);				 
				 // 修改
				iUserService.update(userinfo);
			}
			this.update(b);
		}
		
	}

	
	@Override
	public void bidUpdateAndOne(Long id, int state, String remark,Logininfo current) {
	      // 获取 审核 对象
		BidRequest b = bidRequestMapper.selectBidRequestById(id);
		
		
		//插入 满标一审 记录
		BidRequestAuditHistory bid = new BidRequestAuditHistory();
		 // 设置借款人
		bid.setApplier(b.getCreateUser());
		  // 设置申请时间
		bid.setApplyTime(b.getApplyDate());
		  // 设置申请类型  为满标一审
		bid.setAuditType(BidRequestAuditHistory.FULL_AUDIT1);
		  // 设置审核者
		bid.setAuditor(current);
		  // 设置审核时间
		bid.setAuditTime(new Date());
		  // 设置备注
		bid.setRemark(remark);
		 // 设置审核状态
		bid.setState(state);
		// 设置发标的id
		bid.setBidRequestId(b.getId());
		  // 添加发标历史
		bidRequestAuditHistoryMapper.insert(bid);
		
		//更新借款标的状态 
		if(state == BidRequestAuditHistory.STATE_PASS) {
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
		}else if(state == BidRequestAuditHistory.STATE_REJECT){
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
		}
		
		bidRequestMapper.updateByPrimaryKey(b);
			
		
	}

	@Override
	public void bidUpdateAndTwo(Long id, int state, String remark,Logininfo current) {
		// 获取 审核 对象
		BidRequest b = bidRequestMapper.selectBidRequestById(id);
		
		
		//插入 满标2审 记录
		BidRequestAuditHistory bid = new BidRequestAuditHistory();
		 // 设置借款人
		bid.setApplier(b.getCreateUser());
		  // 设置申请时间
		bid.setApplyTime(b.getApplyDate());
		  // 设置申请类型   为满标二审
		bid.setAuditType(BidRequestAuditHistory.FULL_AUDIT2);
		  // 设置审核者
		bid.setAuditor(current);
		  // 设置审核时间
		bid.setAuditTime(new Date());
		  // 设置备注
		bid.setRemark(remark);
		 // 设置审核状态
		bid.setState(state);
		// 设置发标的id
		bid.setBidRequestId(b.getId());

		//更新借款标的状态
		if(state == BidRequestAuditHistory.STATE_PASS) {
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_3);
		}else if(state == BidRequestAuditHistory.STATE_REJECT){
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
		}


		bidRequestMapper.updateByPrimaryKey(b);
	}

	@Override
	public void bidUpdateAndThree(Long id, int state, String remark, Logininfo current) {
		// 获取 审核 对象
		BidRequest b = bidRequestMapper.selectBidRequestById(id);


		//插入 满标2审 记录
		BidRequestAuditHistory bid = new BidRequestAuditHistory();
		// 设置借款人
		bid.setApplier(b.getCreateUser());
		// 设置申请时间
		bid.setApplyTime(b.getApplyDate());
		// 设置申请类型   为满标三审
		bid.setAuditType(BidRequestAuditHistory.FULL_AUDIT3);
		// 设置审核者
		bid.setAuditor(current);
		// 设置审核时间
		bid.setAuditTime(new Date());
		// 设置备注
		bid.setRemark(remark);
		// 设置审核状态
		bid.setState(state);
		// 设置发标的id
		bid.setBidRequestId(b.getId());

		//更新借款标的状态
		if(state == BidRequestAuditHistory.STATE_PASS) {
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);//还款中

		}else if(state == BidRequestAuditHistory.STATE_REJECT){
			b.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
		}

		//放款
		paymentscheduleService.callbackMoney(b,bid);
	}


	//分页查询投资列表
	@Override
	public PageResult queryBids(BidQueryObject q) {

	   // 通过 条件 查询 有没有 
	    int i = bidMapper.queryForCount(q);
	    
	    if(i > 0){
	    	
	    	  // 通过条件查询 集合
	    	List<Bid> query = bidMapper.query(q);
	    	  
	    	   // 调取工具类返回
	    	return new PageResult(i, q.getPageSize(), q.getCurrentPage(), query);
	    }
	
		return PageResult.empty(q.getPageSize());
	}
	

	@Override
	public List<EntryValue<Integer, String>> listBidRequestStates() {
		List<EntryValue<Integer, String>> ret = new ArrayList<>();
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING,"待发布"));
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_BIDDING,"招标中"));//招标中=发标前审核通过
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_UNDO,"已撤销"));//已撤销（借款方主动发起的）
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE,"流标"));//流标（招标期限内没有满标）
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1,"满标1审"));//满标1审（一旦在招标期限内满标，则自动进入待满标一审）
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2,"满标2审"));//满标2审（一旦满标一审通过，则自动进入待满标二审）
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_REJECTED,"满标审核被拒绝"));//一审或者二审不通过
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_PAYING_BACK,"还款中"));//通过二审（会放款）,还款中
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK,"已还清"));//已还清
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE,"逾期"));//逾期
		ret.add(new EntryValue<Integer, String>(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE,"初审拒接状态"));//初审拒绝
		return ret;
	}
	
	

}
