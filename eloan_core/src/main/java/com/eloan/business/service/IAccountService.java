package com.eloan.business.service;

import com.eloan.business.domain.Account;

public interface IAccountService {

	void update(Account account);

	Account get(Long id);
}
