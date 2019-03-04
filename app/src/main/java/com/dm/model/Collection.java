package com.dm.model;

public class Collection {
	private String CollId;
	private String CollDocId;
	private String UserId;
	private String Android_id;
	private String AreaId;
	private String PartyId;
	private String DistId;
	private String SMId;
	private String Mode;
	private String Amount;
	private String PaymentDate;
	private String Cheque_DDNo;
	private String Cheque_DD_Date;
	private String Bank;
	private String Branch;
	private String Remarks;
	private String DistName;
	private String VisitId;
	private String Vdate;
	private String CreatedDate;
	private String andPartyId;
	private String isUpload;
	private String SyncId;
	private String latitude;
	private String longitude;
	private String latlngTime_stamp;


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatlngTime_stamp() {
		return latlngTime_stamp;
	}

	public void setLatlngTime_stamp(String latlngTime_stamp) {
		this.latlngTime_stamp = latlngTime_stamp;
	}

	public String getSyncId() {
		return this.SyncId;
	}

	public void setSyncId(String SyncId) {
	this.SyncId = SyncId;
	}
	
	public String getIsUpload() {
		return this.isUpload;
	}

	public void setIsUpload(String isUpload) {
	this.isUpload = isUpload;
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
	
	private boolean colectionImport;
	
	public boolean getColectionImport() {
		return this.colectionImport;
	}

	public void setColectionImport(boolean colectionImport) {
	this.colectionImport = colectionImport;
	}
	public String getVisitId() {
		return this.VisitId;
	}

	public void setVisitId(String VisitId) {
	this.VisitId = VisitId;
	}

	public String getVdate() {
		return this.Vdate;
	}

	public void setVdate(String Vdate) {
	this.Vdate = Vdate;
	}
	
	
	public String getDistName() {
		return this.DistName;
	}

	public void setDistName(String DistName) {
	this.DistName = DistName;
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
	public String getAndroid_id() {
		return this.Android_id;
	}

	public void setAndroid_id(String Android_id) {
	this.Android_id = Android_id;

}
	
	public String getDistId() {
		return this.DistId;
	}

	public void setDistId(String DistId) {
	this.DistId = DistId;

}
	public String getRemarks() {
		return this.Remarks;
	}

	public void setRemarks(String Remarks) {
	this.Remarks = Remarks;

}
	public String getBranch() {
		return this.Branch;
	}

	public void setBranch(String Branch) {
	this.Branch = Branch;

}
	public String getBank() {
		return this.Bank;
	}

	public void setBank(String Bank) {
	this.Bank = Bank;

}
	public String getCheque_DD_Date() {
		return this.Cheque_DD_Date;
	}

	public void setCheque_DD_Date(String Cheque_DD_Date) {
	this.Cheque_DD_Date = Cheque_DD_Date;

}
	public String getCheque_DDNo() {
		return this.Cheque_DDNo;
	}

	public void setCheque_DDNo(String Cheque_DDNo) {
	this.Cheque_DDNo = Cheque_DDNo;

}
	
	public String getPaymentDate() {
		return this.PaymentDate;
	}

	public void setPaymentDate(String PaymentDate) {
	this.PaymentDate = PaymentDate;

}
	public String getAmount() {
		return this.Amount;
	}

	public void setAmount(String Amount) {
	this.Amount = Amount;

}
	public String getMode() {
		return this.Mode;
	}

	public void setMode(String Mode) {
	this.Mode = Mode;

}
	
	public String getSMId() {
		return this.SMId;
	}

	public void setSMId(String SMId) {
	this.SMId = SMId;

}
	
	public String getUserId() {
		return this.UserId;
	}

	public void setUserId(String UserId) {
	this.UserId = UserId;

}
	public String getCollDocId() {
		return this.CollDocId;
	}

	public void setCollDocId(String CollDocId) {
	this.CollDocId = CollDocId;

}
	
	public String getCollId() {
		return this.CollId;
	}

	public void setCollId(String CollId) {
		this.CollId = CollId;
	
}
	
}