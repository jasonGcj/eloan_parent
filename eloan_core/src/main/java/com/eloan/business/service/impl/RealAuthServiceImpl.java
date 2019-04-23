package com.eloan.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.query.PageResult;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.Realauth;
import com.eloan.business.domain.Userinfo;
import com.eloan.business.mapper.RealauthMapper;
import com.eloan.business.query.RealAuthQueryObject;
import com.eloan.business.service.IRealAuthService;
import com.eloan.business.service.IUserService;
import com.eloan.business.util.BitStatesUtils;

@Service
public class RealAuthServiceImpl implements IRealAuthService {
	@Autowired
	private RealauthMapper realAuthMapper;

	@Autowired
	private IUserService userService;

	@Override
	public Realauth get(Long id) {
		return this.realAuthMapper.selectByPrimaryKey(id);
	}

	@Override
	public void apply(Realauth realAuth) {
		realAuth.setApplyTime(new Date());
		realAuth.setApplier(UserContext.getCurrent());
		realAuth.setState(Realauth.STATE_APPLY);
		realAuthMapper.insert(realAuth);

		Userinfo current = this.userService.get(UserContext.getCurrent()
				.getId());
		current.setRealauthId(realAuth.getId());
		this.userService.update(current);
	}

	@Override
	public PageResult query(RealAuthQueryObject qo) {
		int count = this.realAuthMapper.queryForCount(qo);
		if (count > 0) {
			List<Realauth> list = this.realAuthMapper.query(qo);
			return new PageResult(count, qo.getPageSize(), qo.getCurrentPage(),
					list);
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void audit(Long id, String remark, int state) {
		//思路:
		Realauth realAuth = this.realAuthMapper.selectByPrimaryKey(id);
		if (realAuth != null && realAuth.getState() == Realauth.STATE_APPLY) {
			realAuth.setAuditor(UserContext.getCurrent());
			realAuth.setAuditTime(new Date());
			realAuth.setState(state);
			Userinfo userinfo = this.userService.get(realAuth.getApplier()
					.getId());
			if (state == Realauth.STATE_REJECT) {
				userinfo.setRealauthId(null);
			} else if (state == Realauth.STATE_PASS && !userinfo.getRealAuth()) {
				userinfo.setRealName(realAuth.getRealname());
				userinfo.setIdNumber(realAuth.getIdNumber());
				userinfo.addState(BitStatesUtils.OP_REAL_AUTH);
			}
			this.userService.update(userinfo);
			this.realAuthMapper.updateByPrimaryKey(realAuth);
		}
	}

}
