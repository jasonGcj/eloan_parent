package com.eloan.business.service;

import java.math.BigDecimal;
import java.util.List;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.business.domain.BidQueryObject;
import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestQueryObject;
import com.eloan.business.util.EntryValue;

public interface IBidRequestService {
  void update(BidRequest bidRequest);
  boolean canBorrow(Logininfo logininfo);
  List<BidRequest> indexList(int size);
  PageResult selectBidRequestList(BidRequestQueryObject q);
  BidRequest getbidRequest(Long id);
  void borrowApply(BidRequest b,Logininfo current);
  void addBach(Long bidRequestId, BigDecimal amount,Logininfo current);
  void addBidRequest(String remark, int state, Long id,Logininfo current);
  void bidUpdateAndOne(Long id,int state,String remark,Logininfo current);//满标一审
  void bidUpdateAndTwo(Long id,int state,String remark,Logininfo current);//满标二审
  void bidUpdateAndThree(Long id,int state,String remark,Logininfo current);//满标三审

  //查询投资列表
  PageResult queryBids(BidQueryObject q);
  //返回状态列表
  public List<EntryValue<Integer,String>> listBidRequestStates();
}


