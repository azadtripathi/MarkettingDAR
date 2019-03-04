package com.dm.model;

public class Order1 {
	private String Ord1Id;
	private String OrdId;
	private String VisId;
	private String OrdDocId;
	private String Sno;
	private String UserId;
	private String VDate;
	private String SMId;
	private String PartyId;
	private String AreaId;
	private String ItemId,ItemName;
	private String Qty,stdPkg;
	private String FreeQty;
	private String Rate;
	private String Discount;
	private String Remarks;
	private String MeetFlag;
	private String MeetDocId;
	private String Amount;
	private String Android_Id;
	private String BeatId;
	private String OrderAndroid_Id;
	private String andPartyId;
	private String partyName;
	private boolean NewOrder;
	private String partyCode;
	private String batchNo,mfd;

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void setMfd(String mfd) {
		this.mfd = mfd;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public String getMfd() {
		return mfd;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	private String CreatedDate;
	private String unit;
	private String longitude,latitude;
	private String latlngTimeStamp;
	private String distId;

	public void setDistId(String distId) {
		this.distId = distId;
	}

	public String getDistId() {
		return distId;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatlngTimeStamp(String latlngTimeStamp) {
		this.latlngTimeStamp = latlngTimeStamp;
	}

	public String getLatlngTimeStamp() {
		return latlngTimeStamp;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	private String cases;
	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	public String getPartyName() {
		return this.partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
public String getAndPartyId() {
	return this.andPartyId;
}

public void setAndPartyId(String andPartyId) {
	this.andPartyId = andPartyId;
}
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	
	public String getOrderAndroid_Id() {
		return this.OrderAndroid_Id;
	}
	
	public void setOrderAndroid_Id(String OrderAndroid_Id) {
		this.OrderAndroid_Id = OrderAndroid_Id;
	}
	
	public String getStdPkg() {
		return this.stdPkg;
	}
	
	public void setStdPkg(String stdPkg) {
		this.stdPkg = stdPkg;
	}
	public String getBeatId() {
		return this.BeatId;
	}

	public void setBeatId(String BeatId) {
		this.BeatId = BeatId;
	}
	
	public String getAndroid_Id() {
		return this.Android_Id;
	}
	
	public void setAndroid_Id(String Android_Id) {
		this.Android_Id = Android_Id;
	}
	
	public String getAmount() {
		return this.Amount;
	}
	
	public void setAmount(String Amount) {
		this.Amount = Amount;
	}
	
	public boolean getNewOrder() {
		return this.NewOrder;
	}
	
	public void setNewOrder(boolean NewOrder) {
		this.NewOrder = NewOrder;
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
	public String getRemarks() {
		return this.Remarks;
	}
	
	public void setRemarks(String Remarks) {
		this.Remarks = Remarks;
	}
	public String getDiscount() {
		return this.Discount;
	}
	
	public void setDiscount(String Discount) {
		this.Discount = Discount;
	}
	public String getRate() {
		return this.Rate;
	}
	
	public void setRate(String Rate) {
		this.Rate = Rate;
	}
	public String getFreeQty() {
		return this.FreeQty;
	}
	
	public void setFreeQty(String FreeQty) {
		this.FreeQty = FreeQty;
	}
	public String getQty() {
		return this.Qty;
	}
	
	public void setQty(String Qty) {
		this.Qty = Qty;
	}
	public String getItemId() {
		return this.ItemId;
	}
	
	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}

	public String getItemName() {
		return this.ItemName;
	}

	public void setItemName(String ItemName) {
		this.ItemName = ItemName;
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
	public String getSno() {
		return this.Sno;
	}
	
	public void setSno(String Sno) {
		this.Sno = Sno;
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
	public String getOrd1Id() {
		return this.Ord1Id;
	}
	
	public void setOrd1Id(String Ord1Id) {
		this.Ord1Id = Ord1Id;
	}
}
