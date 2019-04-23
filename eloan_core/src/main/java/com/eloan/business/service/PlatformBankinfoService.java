package com.eloan.business.service;

import java.util.List;

import com.eloan.base.query.PageResult;
import com.eloan.business.domain.PlatformBankinfo;
import com.eloan.business.domain.PlatformBankinfoQueryObject;


public interface PlatformBankinfoService {

	PageResult selectPlat(PlatformBankinfoQueryObject q);

	void saveOrUpdate(PlatformBankinfo p);

	List<PlatformBankinfo> selectAll();
    
}
