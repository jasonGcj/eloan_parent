package com.eloan.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.business.domain.Account;
import com.eloan.business.domain.AccountFlow;
import com.eloan.business.domain.RechargeOffline;
import com.eloan.business.domain.RechargeOfflineQueryObject;
import com.eloan.business.mapper.RechargeOfflineMapper;
import com.eloan.business.service.AccuctFlowService;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.RechargeService;
import com.eloan.business.util.BidConst;



@Service
public class RechargeServicImpl implements RechargeService {
	  
	  @Autowired
	  private RechargeOfflineMapper rechargeOfflineMapper;
	  
	  
	  @Autowired
	  private IAccountService accoutService;
	  
	  
	  @Autowired
	  private AccuctFlowService accuctFlowService;
	  

	@Override
	public void addMongey(RechargeOffline r,Logininfo current) {
		  // 设置 属性
		   // 申请者id 
		r.setApplier(current);
		  // 申请时间
		r.setApplyTime(new Date());
		  //设置充值状态
		r.setState(RechargeOffline.STATE_APPLY);
		  
		rechargeOfflineMapper.insert(r);
	}

	@Override
	public PageResult selecRchargetList(RechargeOfflineQueryObject q) {
		
		  // 查询 有没有 
		 int count = rechargeOfflineMapper.queryForCount(q);
		  
		    if(count > 0){
		    	
		    	    // 有 就 查询集合
		    	  List<RechargeOffline> list = rechargeOfflineMapper.query(q);
		    	     
		    	     // 调用工具类 返回
		    	  return new PageResult(count, q.getPageSize(), q.getCurrentPage(), list);
		    }
		
		return PageResult.empty(q.getPageSize());
		
	}

	@Override
	public void checkedRed(Long id, int state, String remark,Logininfo current) {
		   
		  // 查询充值对象  设置属性
		RechargeOffline r = rechargeOfflineMapper.selectByPrimaryKey(id);
		   // 判断 是否是申请状态
		  if(r != null && r.getState() == RechargeOffline.STATE_APPLY){
			   
			  Date date = new Date();
			     // 设置审核者
			  r.setAuditor(current);
			     // 设置审核时间
			  r.setAuditTime(date);
			     // 设置审核步骤
			  r.setRemark(remark);
			     // 设置状态
			  r.setState(state);
			  
			   // 判断是否通过审核
			       // 通过
			   if(state == RechargeOffline.STATE_PASS){
				      // 获取 用户账户信息
				   Account account = accoutService.get(r.getApplier().getId());
				      //设置可用余额
				   account.setUsableAmount(account.getUsableAmount().add(r.getAmount()));
				   accoutService.update(account);
				       
				      // 生成流水  成功充值
				   accuctFlowService.addFlow(date, BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE, r.getAmount(), account);
			   }
			   rechargeOfflineMapper.updateByPrimaryKey(r);
			    
		  }
		  
		      
	}
	   

}
