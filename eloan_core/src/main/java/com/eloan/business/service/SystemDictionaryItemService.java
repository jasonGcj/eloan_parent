package com.eloan.business.service;

import java.util.List;

import com.eloan.base.domain.SystemDictionary;
import com.eloan.base.domain.SystemDictionaryItem;
import com.eloan.base.query.PageResult;
import com.eloan.base.query.SystemDictionaryQueryObject;



public interface SystemDictionaryItemService {

	PageResult querItemDic(SystemDictionaryQueryObject q);

	List<SystemDictionary> systemDictionaryGroups();

	void addSystemDictionary(SystemDictionaryItem sdi);

	void updateSystemDictionary(SystemDictionaryItem sdi);

	void delSystemDictionary(Long id);

}
