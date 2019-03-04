package com.dm.model;

public class TransLeaveRequest {

	private String Android_id;
	private String id;
	private String leaveDocId;
	private String userId;
	private String smId;
	private String vdate;
	private String noOfDay;
	private String fromDate;
	private String toDate;
	private String reason;
	private String appStatus;
	private String appBy;
	private String appRemark;
	private String appBySmid;
	private String leaveFlag,upload;
	private boolean newTransLeave;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public boolean getNewTransLeave() {
		return this.newTransLeave;
	}

	public void setNewTransLeave(boolean newTransLeave) {
	this.newTransLeave = newTransLeave;

}
	public String getLeaveFlag() {
		return this.leaveFlag;
	}

	public void setLeaveFlag(String leaveFlag) {
	this.leaveFlag = leaveFlag;
}
	
	public String getUpload() {
		return this.upload;
	}

	public void setUpload(String upload) {
	this.upload = upload;
}
	
	public String getAppBySmid() {
		return this.appBySmid;
	}

	public void setAppBySmid(String appBySmid) {
	this.appBySmid = appBySmid;
}
	public String getAppRemark() {
		return this.appRemark;
	}

	public void setAppRemark(String appRemark) {
	this.appRemark = appRemark;
}
	
	public String getAppBy() {
		return this.appBy;
	}

	public void setAppBy(String appBy) {
	this.appBy = appBy;
}
	public String getAppStatus() {
		return this.appStatus;
	}

	public void setAppStatus(String appStatus) {
	this.appStatus = appStatus;
}
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
	this.reason = reason;
}
	public String getToDate() {
		return this.toDate;
	}

	public void setToDate(String toDate) {
	this.toDate = toDate;
}
	public String getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(String fromDate) {
	this.fromDate = fromDate;
}
	public String getNoOfDay() {
		return this.noOfDay;
	}

	public void setNoOfDay(String noOfDay) {
	this.noOfDay = noOfDay;
}
	public String getAndroid_id() {
		return this.Android_id;
	}

	public void setAndroid_id(String Android_id) {
	this.Android_id = Android_id;
}
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
	this.id = id;
}
	public String getLeaveDocId() {
		return this.leaveDocId;
	}

	public void setLeaveDocId(String leaveDocId) {
	this.leaveDocId = leaveDocId;
}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
	this.userId = userId;
}
	public String getSmid() {
		return this.smId;
	}

	public void setSmId(String smId) {
	this.smId = smId;
}
	public String getVdate() {
		return this.vdate;
	}

	public void setVdate(String vdate) {
	this.vdate = vdate;
}
		
	
}
