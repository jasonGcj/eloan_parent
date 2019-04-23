package com.eloan.business.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.eloan.base.domain.SystemDictionaryItem;

/**
 * 风控材料
 */
@Getter
@Setter
public class Userfile extends BaseAuditDomain {

	private String file;
	private SystemDictionaryItem fileType;
	private int score;
	
	public String getJsonString() {
		Map<String, Object> m = new HashMap<>();
		m.put("id", getId());
		m.put("username", this.getApplier().getUsername());
		m.put("fileType", this.getFileType().getTitle());
		m.put("file", this.getFile());
		m.put("score", this.getScore());
		m.put("remark", this.getRemark());
		m.put("state", this.getState());
		return JSONObject.toJSONString(m);
	}
}
