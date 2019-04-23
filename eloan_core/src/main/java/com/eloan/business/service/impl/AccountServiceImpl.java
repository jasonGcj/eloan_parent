package com.eloan.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.business.domain.Account;
import com.eloan.business.mapper.AccountMapper;
import com.eloan.business.service.IAccountService;

@Service
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public void update(Account account) {
		int ret = accountMapper.updateByPrimaryKey(account);
		if (ret <= 0) {
			throw new RuntimeException("Account对象:" + account.getId()
					+ " 乐观锁失败!");
		}
	}

	@Override
	public Account get(Long id) {
		return accountMapper.selectByPrimaryKey(id);
	}

}
 