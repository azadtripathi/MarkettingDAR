package com.dm.model;

import java.io.Serializable;

public class DemoTransaction implements Serializable{

	private String DemoId;
	private String VisId;
	private String DemoDocId;
	private String Android_id;
	private String UserId;
	private String VDate;
	private String SMId;
	private String PartyId;
	private String Remarks;
	private String AreaId;
	private String CompleteAppDetail;
	private String AvailablityShop;
	private String IsPartyConverted;
	private String NewAppArea;
	private String TechAdvantage;
	private String TechSuggestion;
	private String NewApp;
	private String OrderType;
	private String ProductClassId;
	private String ProductSegmentId;
	private String ProductMatGrp;
	private String ItemId;
	private String filePath;
	private String andPartyId;
	private String CreatedDate;
	private String isUpload;
	private String latitude, longitude;
	String latlngTimeStamp;
	String webDocId;


	public void setWebDocId(String webDocId) {
		this.webDocId = webDocId;
	}

	public String getWebDocId() {
		return webDocId;
	}

	public void setLatlngTimeStamp(String latlngTimeStamp) {
		this.latlngTimeStamp = latlngTimeStamp;
	}

	public String getLatlngTimeStamp() {
		return latlngTimeStamp;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
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
	
	private boolean   newDemo;
	
	public String getFilePath() {
		return this.filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * @return the newDemo
	 */
	public boolean isNewDemo() {
		return newDemo;
	}
	/**
	 * @param newDemo the newDemo to set
	 */
	public void setNewDemo(boolean newDemo) {
		this.newDemo = newDemo;
	}
	public String getAndroid_id() {
		return this.Android_id;
	}
	public void setAndroid_id(String Android_id) {
		this.Android_id = Android_id;
	}
	
	public String getItemId() {
		return this.ItemId;
	}
	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}
	public String getProductMatGrp() {
		return this.ProductMatGrp;
	}
	public void setProductMatGrp(String ProductMatGrp) {
		this.ProductMatGrp = ProductMatGrp;
	}
	public String getProductSegmentId() {
		return this.ProductSegmentId;
	}
	public void setProductSegmentId(String ProductSegmentId) {
		this.ProductSegmentId = ProductSegmentId;
	}
	public String getProductClassId() {
		return this.ProductClassId;
	}
	public void setProductClassId(String ProductClassId) {
		this.ProductClassId = ProductClassId;
	}
	public String getOrderType() {
		return this.OrderType;
	}
	public void setOrderType(String OrderType) {
		this.OrderType = OrderType;
	}
	public String getNewApp() {
		return this.NewApp;
	}
	public void setNewApp(String NewApp) {
		this.NewApp = NewApp;
	}
	public String getTechSuggestion() {
		return this.TechSuggestion;
	}
	public void setTechSuggestion(String TechSuggestion) {
		this.TechSuggestion = TechSuggestion;
	}
	public String getTechAdvantage() {
		return this.TechAdvantage;
	}
	public void setTechAdvantage(String TechAdvantage) {
		this.TechAdvantage = TechAdvantage;
	}
	public String getNewAppArea() {
		return this.NewAppArea;
	}
	public void setNewAppArea(String NewAppArea) {
		this.NewAppArea = NewAppArea;
	}
	public String getIsPartyConverted() {
		return this.IsPartyConverted;
	}
	public void setIsPartyConverted(String IsPartyConverted) {
		this.IsPartyConverted = IsPartyConverted;
	}
	public String getAvailablityShop() {
		return this.AvailablityShop;
	}
	public void setAvailablityShop(String AvailablityShop) {
		this.AvailablityShop = AvailablityShop;
	}
	public String getCompleteAppDetail() {
		return this.CompleteAppDetail;
	}
	public void setCompleteAppDetail(String CompleteAppDetail) {
		this.CompleteAppDetail = CompleteAppDetail;
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
	public String getDemoDocId() {
		return this.DemoDocId;
	}
	public void setDemoDocId(String DemoDocId) {
		this.DemoDocId = DemoDocId;
	}
	public String getVisId() {
		return this.VisId;
	}
	public void setVisId(String VisId) {
		this.VisId = VisId;
	}
	
	public String getDemoId() {
		return this.DemoId;
	}
	public void setDemoId(String DemoId) {
		this.DemoId = DemoId;
	}
	
	
}