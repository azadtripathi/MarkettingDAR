
package com.dm.model;

public class Visit {
	private String VisId;
	private String VisitDocId;
	private String AndroidDocId;
	private String UserId;
	private String NextwithUserId;
	private String Vdate;
	private String NextVisitDate;
	private String Remark;
	private String SMId;
	private String AreaId;
	private String CityId;
	private String DistId;
	private String nCityId;
	private String frTime1;
	private String frTime2;
	private String toTime1;
	private String toTime2;
	private String WithUserId;
	private String ModeOfTransport;
	private String VehicleUsed;
	private String Industry;
	private String DsrLock;
	private String VisitNo;
	private String Isupload;
	private String cityIds;
	private String cityName;
	private boolean visitImport;
	private String filePath;
    private String CreatedDate;
    private String OrderByPhone;
    private String OrderByEmail;
    private String Stock;
	private String Attandanse;
	private String OtherExp;
	private String OtherExpRem;
	private String VisitName;
	private String toAreaId;
	private String chargeable;

	private String latitude,longitude;
	String  latlngTimeStamp;
	private String Area;
	private String Beat;
	String imagePathOnLaunch,imagePathOnLock;

	public void setImagePathOnLaunch(String imagePathOnLaunch)
	{
		this.imagePathOnLaunch = imagePathOnLaunch;
	}

	public String getImagePathOnLaunch() {
		return imagePathOnLaunch;
	}

	public void setImagePathOnLock(String imagePathOnLock) {
		this.imagePathOnLock = imagePathOnLock;
	}

	public String getImagePathOnLock() {
		return imagePathOnLock;
	}
	public void setArea(String Area) {
		this.Area = Area;
	}

	public String getArea() {
		return this.Area;
	}

	public void setBeat(String Beat) {
		this.chargeable = Beat;
	}

	public String getBeat() {
		return this.Beat;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatlngTimeStamp(String latlngTimeStamp) {
		this.latlngTimeStamp = latlngTimeStamp;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLatlngTimeStamp() {
		return latlngTimeStamp;
	}

	public void setChargeable(String chargeable) {
		this.chargeable = chargeable;
	}

	public String getChargeable() {
		return this.chargeable;
	}

	public void setToAreaId(String toAreaId) {
		this.toAreaId = toAreaId;
	}

	public String getToAreaId() {
		return this.toAreaId;
	}

	public void setVisitName(String VisitName) {
		this.VisitName = VisitName;
	}

	public String getVisitName() {
		return this.VisitName;
	}
	public void setOtherExpRem(String OtherExpRem) {
		this.OtherExpRem = OtherExpRem;
	}

	public String getOtherExpRem() {
		return this.OtherExpRem;
	}
	public void setOtherExp(String OtherExp) {
		this.OtherExp = OtherExp;
	}

	public String getOtherExp() {
		return this.OtherExp;
	}
	public void setAttandanse(String Attandanse) {
		this.Attandanse = Attandanse;
	}

	public String getAttandanse() {
		return this.Attandanse;
	}

    public void setStock(String Stock) {
    	this.Stock = Stock;
    	}
    	
    	public String getStock() {return this.Stock;}
    
	public void setOrderByEmail(String OrderByEmail) {
	this.OrderByEmail = OrderByEmail;
	}
	
	public String getOrderByEmail() {
		return this.OrderByEmail;
	}

	public void setOrderByPhone(String OrderByPhone) {
	this.OrderByPhone = OrderByPhone;
	}
	 public String getOrderByPhone() {
			return this.OrderByPhone;
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
	public boolean getVisitImport() {
		return this.visitImport;
	}
	
	public void setVisitImport(boolean visitImport) {
		this.visitImport = visitImport;
	}
	public String getCityIds() {
		return this.cityIds;
	}
	
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}
	
	public String getCityName() {
		return this.cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getIsupload() {
		return this.Isupload;
	}
	
	public void setIsupload(String Isupload) {
		this.Isupload = Isupload;
	}
	public String getVisitNo() {
		return this.VisitNo;
	}
	
	public void setVisitNo(String VisitNo) {
		this.VisitNo = VisitNo;
	}
	public String getAreaId() {
		return this.AreaId;
	}
	
	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
	}
	public String getUserId() {
		return this.UserId;
	}
	
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	public String getDsrLock() {
		return this.DsrLock;
	}
	
	public void setDsrLock(String DsrLock) {
		this.DsrLock = DsrLock;
	}
	public String getIndustry() {
		return this.Industry;
	}
	
	public void setIndustry(String Industry) {
		this.Industry = Industry;
	}
	public String getVehicleUsed() {
		return this.VehicleUsed;
	}
	
	public void setVehicleUsed(String VehicleUsed) {
		this.VehicleUsed = VehicleUsed;
	}
	public String getModeOfTransport() {
		return this.ModeOfTransport;
	}
	
	public void setModeOfTransport(String ModeOfTransport) {
		this.ModeOfTransport = ModeOfTransport;
	}
	
	public String getWithUserId() {
		return this.WithUserId;
	}
	
	public void setWithUserId(String WithUserId) {
		this.WithUserId = WithUserId;
	}
	
	public String getToTime2() {
		return this.toTime2;
	}
	
	public void setToTime2(String toTime2) {
		this.toTime2 = toTime2;
	}
	public String getToTime1() {
		return this.toTime1;
	}
	
	public void setToTime1(String toTime1) {
		this.toTime1 = toTime1;
	}
	public String getfrTime2() {
		return this.frTime2;
	}
	
	public void setfrTime2(String frTime2) {
		this.frTime2 = frTime2;
	}
	public String getfrTime1() {
		return this.frTime1;
	}
	
	public void setfrTime1(String frTime1) {
		this.frTime1 = frTime1;
	}
	public String getNCityId() {
		return this.nCityId;
	}
	
	public void setNCityId(String nCityId) {
		this.nCityId = nCityId;
	}
	public String getDistId() {
		return this.DistId;
	}
	
	public void setDistId(String DistId) {
		this.DistId = DistId;
	}
	public String getCityId() {
		return this.CityId;
	}
	
	public void setCityId(String CityId) {
		this.CityId = CityId;
	}
	public String getSMId() {
		return this.SMId;
	}
	
	public void setSMId(String SMId) {
		this.SMId = SMId;
	}
	
	public String getRemark() {
		return this.Remark;
	}
	
	public void setRemark(String Remark) {
		this.Remark = Remark;
	}
	public String getNextVisitDate() {
		return this.NextVisitDate;
	}
	
	public void setNextVisitDate(String NextVisitDate) {
		this.NextVisitDate = NextVisitDate;
	}
	public String getVdate() {
		return this.Vdate;
	}
	
	public void setVdate(String Vdate) {
		this.Vdate = Vdate;
	}
	public void setNextwithUserId(String NextwithUserId) {
		this.NextwithUserId = NextwithUserId;
	}
	public String getNextwithUserId() {
		return this.NextwithUserId;
	}
	
	
	public String getVisitDocId() {
		return this.VisitDocId;
	}
	
	public void setVisitDocId(String VisitDocId) {
		this.VisitDocId = VisitDocId;
	}
	public String getAndroidDocId() {
		return this.AndroidDocId;
	}
	
	public void setAndroidDocId(String AndroidDocId) {
		this.AndroidDocId = AndroidDocId;
	}
	
	public String getVisId() {
		return this.VisId;
	}
	
	public void setVisId(String VisId) {
		this.VisId = VisId;
	}


}
