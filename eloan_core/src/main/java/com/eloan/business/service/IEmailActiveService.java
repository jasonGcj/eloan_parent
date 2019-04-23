package com.eloan.business.service;

import com.eloan.base.domain.Logininfo;

public interface IEmailActiveService {

	/**
	 * 发送激活邮件
	 * @param email
	 */
	void sendActiveEmail(String email,Logininfo current);

	/**
	 * 绑定邮箱验证
	 * @param code
	 * @return
	 */
	void bindEmail(String code);
}
