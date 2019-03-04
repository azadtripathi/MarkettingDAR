package com.dm.model;

public class Order {
	private String OrdId;
	private String VisId;
	private String OrdDocId;
	private String Android_id;
	private String UserId;
	private String VDate;
	private String SMId;
	private String PartyId;
	private String AreaId;
	private String Remarks;
	private String OrderAmount;
	private String OrderStatus;
	private String MeetFlag;
	private String webcode;
	private String MeetDocId;
	private String OrderType;
	private String andPartyId;
	private boolean isOrderImport;
	private String isUpload;
	private String CreatedDate;
	private String Qty;
	private String beatName;
	private String webDocId;

	public void setWebDocId(String webDocId) {
		this.webDocId = webDocId;
	}

	public String getWebDocId() {
		return webDocId;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	public String getBeatName() {
		return beatName;
	}

	private String longitude, latitude;
	String latlngTime;

	public String getQty() {
		return this.Qty;
	}

	public void setQty(String Qty) {
		this.Qty = Qty;
	}
	

public String getIsUpload() {
	return this.isUpload;
}

public void setIsUpload(String isUpload) {
this.isUpload = isUpload;
}
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public boolean getIsOrderImport() {
		return this.isOrderImport;
	}
	
	public void setIsOrderImport(boolean isOrderImport) {
		this.isOrderImport = isOrderImport;
	}

	public String getAndroid_id() {return this.Android_id;}
	public void setAndroid_id(String Android_id) {
		this.Android_id = Android_id;
	}

	public String getOrderType() {
		return this.OrderType;
	}
	
	public void setOrderType(String OrderType) {
		this.OrderType = OrderType;
	}
	public String getMeetDocId() {
		return this.MeetDocId;
	}
	
	public void setMeetDocId(String MeetDocId) {
		this.MeetDocId = MeetDocId;
	}
	public String getMeetFlag() {
		return this.MeetFlag;
	}
	
	public void setMeetFlag(String MeetFlag) {
		this.MeetFlag = MeetFlag;
	}
	public String getOrderStatus() {
		return this.OrderStatus;
	}
	
	public void setOrderStatus(String OrderStatus) {
		this.OrderStatus = OrderStatus;
	}
	public String getOrderAmount() {
		return this.OrderAmount;
	}

	public void setWebCode(String webcode) {
		this.webcode = webcode;
	}
	public String getWebCode() {
		return this.webcode;
	}
	public void setOrderAmount(String OrderAmount) {
		this.OrderAmount = OrderAmount;
	}
	public String getRemarks() {
		return this.Remarks;
	}
	
	public void setRemarks(String Remarks) {
		this.Remarks = Remarks;
	}
	public String getAreaId() {
		return this.AreaId;
	}
	
	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
	}
	public String getPartyId() {
		return this.PartyId;
	}
	
	public void setPartyId(String PartyId) {
		this.PartyId = PartyId;
	}
	
	public String getAndPartyId() {
		return this.andPartyId;
	}
	
	public void setAndPartyId(String andPartyId) {
		this.andPartyId = andPartyId;
	}
	public String getSMId() {
		return this.SMId;
	}
	
	public void setSMId(String SMId) {
		this.SMId = SMId;
	}
	public String getVDate() {
		return this.VDate;
	}
	
	public void setVDate(String VDate) {
		this.VDate = VDate;
	}
	public String getUserId() {
		return this.UserId;
	}
	
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	public String getOrdDocId() {
		return this.OrdDocId;
	}
	
	public void setOrdDocId(String OrdDocId) {
		this.OrdDocId = OrdDocId;
	}
	public String getVisId() {
		return this.VisId;
	}
	
	public void setVisId(String VisId) {
		this.VisId = VisId;
	}
	
	public String getOrdId() {
		return this.OrdId;
	}
	
	public void setOrdId(String OrdId) {
		this.OrdId = OrdId;
	}


	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatlngTime() {
		return latlngTime;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLatlngTime(String latlngTime) {
		this.latlngTime = latlngTime;
	}
}
