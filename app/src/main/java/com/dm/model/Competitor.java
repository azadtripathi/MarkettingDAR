package com.dm.model;

public class Competitor {

	private String ComptId;
	private String VisId;
	private String DocId;
	private String Android_id;
	private String VDate;
	private String UserId;
	private String PartyId;
	private String Item;
	private String Qty;
	private String Rate;
	private String SMID;
	private String remark;
	private String filePath;
    private String CreatedDate;
    private String andPartyId;
    private String brandActivity;
    private String meetActivity;
    private String roadShow;
    private String scheme;
    private String generalInfo;
    private String competitorName;
    private String discount;
    private String otherActivity;
private String isUpload;
	private String latitude,longitude;
	private String latlngTimeStamp;

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

	public String getIsUpload() {
		return this.isUpload;
	}

	public void setIsUpload(String isUpload) {
	this.isUpload = isUpload;
	}
    
    public String getOtherActivity() {
		return this.otherActivity;
	}
	
	public void setOtherActivity(String otherActivity) {
		this.otherActivity = otherActivity;
	}
    public String getDiscount() {
		return this.discount;
	}
	
	public void setDiscount(String discount) {
		this.discount = discount;
	}
    public String getBrandActivity() {
		return this.brandActivity;
	}
	
	public void setBrandActivity(String brandActivity) {
		this.brandActivity = brandActivity;
	}
    public String getMeetActivity() {
		return this.meetActivity;
	}
	
	public void setMeetActivity(String meetActivity) {
		this.meetActivity = meetActivity;
	}
    public String getRoadShow() {
		return this.roadShow;
	}
	
	public void setRoadShow(String roadShow) {
		this.roadShow = roadShow;
	}
    public String getScheme() {
		return this.scheme;
	}
	
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
    public String getGeneralInfo() {
		return this.generalInfo;
	}
	
	public void setGeneralInfo(String generalInfo) {
		this.generalInfo = generalInfo;
	}
    public String getCompetitorName() {
		return this.competitorName;
	}
	
	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
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
	
	public String getFilePath() {
		return this.filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	private boolean competitorImport;
	
	public boolean getCompetitorImport() {
		return this.competitorImport;
	}
	
	public void setCompetitorImport(boolean competitorImport) {
		this.competitorImport = competitorImport;
	}
	public String getAndroid_id() {
		return this.Android_id;
	}
	
	public void setAndroid_id(String Android_id) {
		this.Android_id = Android_id;
	}
	public String getSMID() {
		return this.SMID;
	}
	
	public void setSMID(String SMID) {
		this.SMID = SMID;
	}
	public String getRate() {
		return this.Rate;
	}
	
	public void setRate(String Rate) {
		this.Rate = Rate;
	}
	public String getQty() {
		return this.Qty;
	}
	
	public void setQty(String Qty) {
		this.Qty = Qty;
	}
	public String getItem() {
		return this.Item;
	}
	
	public void setItem(String Item) {
		this.Item = Item;
	}
	public String getPartyId() {
		return this.PartyId;
	}
	
	public void setPartyId(String PartyId) {
		this.PartyId = PartyId;
	}
	public String getUserId() {
		return this.UserId;
	}
	
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	public String getVDate() {
		return this.VDate;
	}
	
	public void setVDate(String VDate) {
		this.VDate = VDate;
	}
	public String getDocId() {
		return this.DocId;
	}
	
	public void setDocId(String DocId) {
		this.DocId = DocId;
	}
	
	public String getVisId() {
		return this.VisId;
	}
	
	public void setVisId(String VisId) {
		this.VisId = VisId;
	}
	public String getComptId() {
		return this.ComptId;
	}
	
	public void setComptId(String ComptId) {
		this.ComptId = ComptId;
	}
	
	
}
