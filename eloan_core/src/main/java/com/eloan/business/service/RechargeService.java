package com.eloan.business.service;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.business.domain.RechargeOffline;
import com.eloan.business.domain.RechargeOfflineQueryObject;

public interface RechargeService {

	void addMongey(RechargeOffline r,Logininfo current);

	PageResult selecRchargetList(RechargeOfflineQueryObject q);

	void checkedRed(Long id, int state, String remark,Logininfo current);

}
