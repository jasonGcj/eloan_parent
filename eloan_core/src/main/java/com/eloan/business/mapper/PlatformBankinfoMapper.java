package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eloan.business.domain.PlatformBankinfo;
import com.eloan.business.domain.PlatformBankinfoQueryObject;


public interface PlatformBankinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformBankinfo record);

    PlatformBankinfo selectByPrimaryKey(Long id);

    List<PlatformBankinfo> selectAll();

    int updateByPrimaryKey(PlatformBankinfo record);
    

     int queryForCount(PlatformBankinfoQueryObject qo);

	List<PlatformBankinfo> query(PlatformBankinfoQueryObject qo);
    
}