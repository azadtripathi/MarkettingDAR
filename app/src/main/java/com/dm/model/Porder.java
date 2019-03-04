package com.dm.model;

import java.io.Serializable;

public class Porder implements Serializable
{

	private static final long serialVersionUID = 7694776476249470490L;
	private String POrdId;
	private String PODocId;
	private String Vdate;
	private String UserId;
	private String SMId;
	private String DistId;
	private String Remarks;
	private String Transporter;
	private String DispName;
	private String DispAdd1;
	private String DispAdd2;
	private String DispCity;
	private String DispPin;
	private String DispState;
	private String DispCountry;
	private String DispPhone;
	private String DispMobile;
	private String DispEmail;
	private String Androidcode;
	private String SchemeId;
	private String ProjectId;
	private String ProjectType;
	private String OrderValue;
	private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	
	public String getOrderValue() {
		if(this.OrderValue==null)
		{
			OrderValue="0.00";
		}
		return this.OrderValue;
	}
	
	public void setOrderValue(String OrderValue) {
		this.OrderValue = OrderValue;
	}	
	public String getProjectType() {
		return this.ProjectType;
	}
	
	public void setProjectType(String ProjectType) {
		this.ProjectType = ProjectType;
	}	
	public String getProjectId() {
		return this.ProjectId;
	}
	
	public void setProjectId(String ProjectId) {
		this.ProjectId = ProjectId;
	}	
	public String getSchemeId() {
		return this.SchemeId;
	}
	
	public void setSchemeId(String SchemeId) {
		this.SchemeId = SchemeId;
	}	
	public String getDispEmail() {
		return this.DispEmail;
	}
	
	public void setDispEmail(String DispEmail) {
		this.DispEmail = DispEmail;
	}	
	public String getDispMobile() {
		return this.DispMobile;
	}
	
	public void setDispMobile(String DispMobile) {
		this.DispMobile = DispMobile;
	}	
	
	public String getDispPhone() {
		return this.DispPhone;
	}
	
	public void setDispPhone(String DispPhone) {
		this.DispPhone = DispPhone;
	}	
	public String getDispCountry() {
		return this.DispCountry;
	}
	
	public void setDispCountry(String DispCountry) {
		this.DispCountry = DispCountry;
	}	
	
	public String getDispState() {
		return this.DispState;
	}
	
	public void setDispState(String DispState) {
		this.DispState = DispState;
	}	
	
	public String getDispPin() {
		return this.DispPin;
	}
	
	public void setDispPin(String DispPin) {
		this.DispPin = DispPin;
	}	
	public String getDispCity() {
		return this.DispCity;
	}
	
	public void setDispCity(String DispCity) {
		this.DispCity = DispCity;
	}	
	public String getDispAdd2() {
		return this.DispAdd2;
	}
	
	public void setDispAdd2(String DispAdd2) {
		this.DispAdd2 = DispAdd2;
	}	
	public String getDispAdd1() {
		return this.DispAdd1;
	}
	
	public void setDispAdd1(String DispAdd1) {
		this.DispAdd1 = DispAdd1;
	}	
	public String getDispName() {
		return this.DispName;
	}
	
	public void setDispName(String DispName) {
		this.DispName = DispName;
	}	
	public String getTransporter() {
		return this.Transporter;
	}
	
	public void setTransporter(String Transporter) {
		this.Transporter = Transporter;
	}	
	public String getRemarks() {
		return this.Remarks;
	}
	
	public void setRemarks(String Remarks) {
		this.Remarks = Remarks;
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
	
	public String getVdate() {
		return this.Vdate;
	}
	
	public void setVdate(String Vdate) {
		this.Vdate = Vdate;
	}
	public String getPODocId() {
		return this.PODocId;
	}
	
	public void setPODocId(String PODocId) {
		this.PODocId = PODocId;
	}
	public String getPOrdId() {
		return this.POrdId;
	}
	
	public void setPOrdId(String POrdId) {
		this.POrdId = POrdId;
	}
	
	public String getAndroidcode() {
		return this.Androidcode;
	}
	
	public void setAndroidcode(String Androidcode) {
		this.Androidcode = Androidcode;
	}
	
	public String getDistId() {
		return this.DistId;
	}
	
	public void setDistId(String DistId) {
		this.DistId = DistId;
	}
}
