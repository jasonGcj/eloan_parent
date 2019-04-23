package com.eloan.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eloan.business.domain.Bid;
import com.eloan.business.domain.BidQueryObject;


public interface BidMapper {
	   Long	insert(Bid bid);
	   
	   List<Bid> selectBidByBidRequestId(@Param("bidRequestid")Long bidRequestid);
	   
	   Bid selectBidById(@Param("id")Long id);
	   
	   
	   void updateBidState(@Param("bidRequestId")Long bidRequestId, @Param("state") int state);
	   
	   int queryForCount(BidQueryObject q);

	   List<Bid> query(BidQueryObject q);
} 
