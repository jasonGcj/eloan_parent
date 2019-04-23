package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eloan.business.domain.RechargeOffline;
import com.eloan.business.domain.RechargeOfflineQueryObject;

public interface RechargeOfflineMapper {
	

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RechargeOffline record);

	int queryForCount(RechargeOfflineQueryObject q);

	List<RechargeOffline> query(RechargeOfflineQueryObject q);
    
    
}