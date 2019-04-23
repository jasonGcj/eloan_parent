package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestQueryObject;
public interface BidRequestMapper {
	int updateByPrimaryKey(BidRequest bidRequest);
	   
	Long insert(BidRequest bidRequest); 
    
	BidRequest selectBidRequestById(@Param("id")Long id);

	int queryForCount(BidRequestQueryObject q);

	List<BidRequest> query(BidRequestQueryObject q);
}
