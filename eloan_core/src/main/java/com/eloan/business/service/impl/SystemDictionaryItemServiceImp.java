package com.eloan.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloan.base.domain.SystemDictionary;
import com.eloan.base.domain.SystemDictionaryItem;
import com.eloan.base.mapper.SystemDictionaryItemMapper;
import com.eloan.base.mapper.SystemDictionaryMapper;
import com.eloan.base.query.PageResult;
import com.eloan.base.query.SystemDictionaryQueryObject;
import com.eloan.business.service.SystemDictionaryItemService;



@Service
public class SystemDictionaryItemServiceImp implements SystemDictionaryItemService {
	  
	  @Autowired
	 private SystemDictionaryItemMapper systemDictionaryItemMapper;
	  
	  @Autowired
	  private SystemDictionaryMapper systemDictionaryMapper;

	@Override
	public PageResult querItemDic(SystemDictionaryQueryObject q) {
		
		 int count = systemDictionaryItemMapper.queryForCount(q);
		  
		    if(count > 0){
		    	
		    	  List<SystemDictionaryItem> list = systemDictionaryItemMapper.query(q);
		    	  
		    	  return new PageResult(count, q.getPageSize(), q.getCurrentPage(), list);
		    }
		
		return PageResult.empty(q.getPageSize());
		
	}

	@Override
	public List<SystemDictionary> systemDictionaryGroups() {
		
		  
		
		return systemDictionaryMapper.selectAll();
	}

	@Override
	public void addSystemDictionary(SystemDictionaryItem sdi) {
		
		systemDictionaryItemMapper.insert(sdi);
	}

	@Override
	public void updateSystemDictionary(SystemDictionaryItem sdi) {
		systemDictionaryItemMapper.updateByPrimaryKey(sdi);
	}

	@Override
	public void delSystemDictionary(Long id) {
		  
		systemDictionaryItemMapper.deleteByPrimaryKey(id);
	}

}
