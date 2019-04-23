package com.eloan.business.mapper;

import java.util.List;

import com.eloan.business.domain.Userinfo;

public interface UserinfoMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Userinfo record);

	Userinfo selectByPrimaryKey(Long id);

	List<Userinfo> selectAll();

	int updateByPrimaryKey(Userinfo record);
}