package com.eloan.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.business.domain.Account;
import com.eloan.business.domain.AccountFlow;
import com.eloan.business.mapper.AccountFlowMapper;
import com.eloan.business.service.AccuctFlowService;
import com.eloan.business.util.BidConst;
import com.eloan.business.util.EntryValue;



@Service
public class AccuctFlowServiceimp implements AccuctFlowService {
	  
	 @Autowired
	private AccountFlowMapper accountFlowMapper;

	
	 @Override
	public void addFlow(Date vdate,int accountActionType, BigDecimal amount, Account account) {
		AccountFlow accountFlow = new AccountFlow();
		accountFlow.setAmount(amount);
		//设置 账户id 
		accountFlow.setAccountId(account.getId());
		//设置业务时间
		accountFlow.setVdate(vdate);
		//资金流水类别
		accountFlow.setAccountActionType(accountActionType);
	    //设置动账注明
		accountFlow.setNote(listAccountActionTypes().get(accountActionType).getValue()+":"+amount.doubleValue()+"元");
		//动账后的账户可用金额
		accountFlow.setUseableAmount(account.getUsableAmount());
		//动账后的账户冻结金额
		accountFlow.setFreezedAmount(account.getFreezedAmount());
		
		accountFlowMapper.insert(accountFlow);
	}
	
	 @Override
	 public List<EntryValue<Integer, String>> listAccountActionTypes() {
		List<EntryValue<Integer, String>> ret = new ArrayList<>();
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"充值成功"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW,"提现成功"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,"成功借款"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,"成功投标"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY,"成功还款"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY,"成功回款"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_CHARGE,"支付平台管理费"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,"支付利息管理费"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE,"支付提现手续费"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE,"支付充值手续费"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标冻结资金"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,"取消投标解冻资金"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED,"提现冻结资金"));
		ret.add(new EntryValue<Integer, String>(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED,"取消提现解冻资金"));
		return ret;
	}
		
}
