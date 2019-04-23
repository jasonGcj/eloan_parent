package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eloan.business.domain.Realauth;
import com.eloan.business.domain.Userfile;
import com.eloan.business.query.RealAuthQueryObject;
import com.eloan.business.query.UserFileAuthQueryObject;

public interface UserfileMapper {
	
    int insert(Userfile record);

    Userfile selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Userfile record);

	List<Userfile> listUnSetTypeFiles(@Param("applierId")Long id,@Param("unselected")boolean unselected);


	//用户风控材料审核页面的分页查询
	int queryForCount(UserFileAuthQueryObject qo);

	List<Userfile> query(UserFileAuthQueryObject qo);
}