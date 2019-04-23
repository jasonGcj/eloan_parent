package com.eloan.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.query.PageResult;
import com.eloan.business.domain.PlatformBankinfo;
import com.eloan.business.domain.PlatformBankinfoQueryObject;
import com.eloan.business.mapper.PlatformBankinfoMapper;
import com.eloan.business.service.PlatformBankinfoService;


@Service
public class PlatformBankinfoServiceimp implements PlatformBankinfoService {
	 
	  
	      @Autowired
	     private PlatformBankinfoMapper platformBankinfoMapper;

	      
	       
		@Override
		public PageResult selectPlat(PlatformBankinfoQueryObject q) {
			

	        //获取总数量
			 int count = platformBankinfoMapper.queryForCount(q);
			 
			   // 判断是否大于零 大于零 就 惊醒查询
			 
			  if(count > 0){
				  
				     // 查询集合
				    List<PlatformBankinfo> list = platformBankinfoMapper.query(q);
			    	     // 放进工具类
			    	return new PageResult(count, q.getPageSize(), q.getCurrentPage(), list);
			  }
			      // 返回总页数
			  return PageResult.empty(q.getPageSize());
					
		}



		@Override
		public void saveOrUpdate(PlatformBankinfo p) {
			 
			    // 判断id是否为空 进行 添加或修改
			   if(p.getId() == null){
				   
				     // 添加
				   platformBankinfoMapper.insert(p);
			   }else {
				   
				    // 修改
				   platformBankinfoMapper.updateByPrimaryKey(p);
			   }
			   
		}



		@Override
		public List<PlatformBankinfo> selectAll() {
			
			return platformBankinfoMapper.selectAll();
		}
	      
	      
	         

}
