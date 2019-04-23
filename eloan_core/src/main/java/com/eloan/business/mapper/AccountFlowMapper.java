package com.eloan.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.eloan.business.domain.AccountFlow;
public interface AccountFlowMapper {

    int insert(AccountFlow record);

}