package com.eloan.business.query;

public class UserFileAuthQueryObject extends BaseAuditQueryObject {
	private Long applierId;//申请人

	public Long getApplierId() {
		return applierId;
	}

	public void setApplierId(Long applierId) {
		this.applierId = applierId;
	}
}
