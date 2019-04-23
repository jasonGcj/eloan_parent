package com.eloan.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.eloan.business.domain.Account;
import com.eloan.business.util.EntryValue;

public interface AccuctFlowService {

	public void addFlow(Date vdate,int accountActionType, BigDecimal amount, Account account);
	
	
	public List<EntryValue<Integer, String>> listAccountActionTypes();
}
