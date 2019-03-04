package com.dm.model;

public class FailedVisit {
	private String FVId;
	private String VisId;
	private String FVDocId;
	private String AndroidId;
	private String VDate;
	private String UserID;
	private String DistId;
	private String SMId;
	private String PartyId;
	private String Remarks;
	private String AreaId;
	private String Nextvisit;
	private String ReasonID;
	private String Vtime;
	private boolean newfailedvisit;
	private String andPartyId;
	private String latitude,longitude;
	private String latlng_timestamp;
	private String web_doc_id;

	public String getWeb_doc_id() {
		return web_doc_id;
	}

	public void setWeb_doc_id(String web_doc_id) {
		this.web_doc_id = web_doc_id;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatlng_timestamp() {
		return latlng_timestamp;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLatlng_timestamp(String latlng_timestamp) {
		this.latlng_timestamp = latlng_timestamp;
	}

	public String getAndPartyId() {
			return this.andPartyId;
		}
		
		public void setAndPartyId(String andPartyId) {
			this.andPartyId = andPartyId;
		}
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public boolean isNewfailedvisit() {
		return newfailedvisit;
	}
	public void setNewfailedvisit(boolean newfailedvisit) {
		this.newfailedvisit = newfailedvisit;
	}
	public String getDistId() {
		return this.DistId;
	}

	public void setDistId(String DistId) {
		this.DistId = DistId;
	}
	public String getVtime() {
		return this.Vtime;
	}

	public void setVtime(String Vtime) {
		this.Vtime = Vtime;
	}	
	public String getAndroidId() {
		return this.AndroidId;
	}

	public void setAndroidId(String AndroidId) {
		this.AndroidId = AndroidId;
	}	
	public String getReasonID() {
		return this.ReasonID;
	}

	public void setReasonID(String ReasonID) {
		this.ReasonID = ReasonID;
	}	
	public String getNextvisit() {
		return this.Nextvisit;
	}

	public void setNextvisit(String Nextvisit) {
		this.Nextvisit = Nextvisit;
	}	
	public String getAreaId() {
		return this.AreaId;
	}

	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
	}	
	public String getRemarks() {
		return this.Remarks;
	}

	public void setRemarks(String Remarks) {
		this.Remarks = Remarks;
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
	public String getUserID() {
		return this.UserID;
	}

	public void setUserID(String UserID) {
		this.UserID = UserID;
	}	
	public String getVDate() {
		return this.VDate;
	}

	public void setVDate(String VDate) {
		this.VDate = VDate;
	}	
	public String getFVDocId() {
		return this.FVDocId;
	}

	public void setFVDocId(String FVDocId) {
		this.FVDocId = FVDocId;
	}	
	public String getVisId() {
		return this.VisId;
	}

	public void setVisId(String VisId) {
		this.VisId = VisId;
	}	
	
public String getFVId() {
	return this.FVId;
}

public void setFVId(String FVId) {
	this.FVId = FVId;
}

}
