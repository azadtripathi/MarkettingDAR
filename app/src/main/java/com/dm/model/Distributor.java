package com.dm.model;

import java.io.Serializable;

public class Distributor implements Serializable {

	
	private static final long serialVersionUID = -3300751148309820502L;
	private String distributor_id;   
	private String distributor_name;
	private String address1;
	private String address2;
	private String city_id;
	private String phone;
	private String mobile;
	private String email;
	private String contact_person ;
	private String blocked_Reason ;
	private String block_Date;
	private String blocked_By;
	private String Active;
	private String AreaId;
	private String BeatId;
	private String IndId;
	private String potential;
	private String party_type_code;
	private String cst_no;
	private String vattin_no;
	private String Servicetaxreg_No;
	private String PANNo;
	private String Remark;
	private String CreditLimit;
	private String OutStanding;
	private String PendingOrder;
	private String sync_id;
	private String CreatedDate;
	private String Pin;
	private String Transport;
	private String SchemeId;
	private String ProjectId;
   private String ProjectType;
   private String openOrder;
	private String CrediDays;
	private String productname;
	private String cases;
	private String unit;
	private String androidid;
	private String area;
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAndroidID() {
		return androidid;
	}

	public void setAndroidID(String androidid) {
		this.androidid = androidid;
	}

	public String getProductname(){return  this.productname;}
	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOpenOrder() {
		return this.openOrder;
	}
	public String getCrediDays() {
		return this.CrediDays;
	}
	public void setOpenOrder(String openOrder) {
		this.openOrder = openOrder;
	}
	public void setCrediDays(String openOrder) {
		this.openOrder = CrediDays;
	}
	

   public String getProjectType() {
		return this.ProjectType;
	}
	
	public void setProjectType(String ProjectType) {
		this.ProjectType = ProjectType;
	}	
	
	public String getprojectId() {
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
	public String getTransport() {
		return this.Transport;
	}
	
	public void setTransport(String Transport) {
		this.Transport = Transport;
	}
	public String getCreatedDate() {
		return CreatedDate;
	}
	
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
	}
	public String getSync_id() {
		return sync_id;
	}
	
	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}
	
	public String getPendingOrder() {
		return PendingOrder;
	}
	
	public void setPendingOrder(String PendingOrder) {
		this.PendingOrder = PendingOrder;
	}
	
	public String getOutStanding() {
		return OutStanding;
	}
	
	public void setOutStanding(String OutStanding) {
		this.OutStanding = OutStanding;
	}
	public String getCreditLimit() {
		return CreditLimit;
	}
	
	public void setCreditLimit(String CreditLimit) {
		this.CreditLimit = CreditLimit;
	}
	public String getRemark() {
		return Remark;
	}
	
	public void setRemark(String Remark) {
		this.Remark = Remark;
	}
	public String getPANNo() {
		return PANNo;
	}
	
	public void setPANNo(String PANNo) {
		this.PANNo = PANNo;
	}
	public String getServicetaxreg_No() {
		return Servicetaxreg_No;
	}
	
	public void setServicetaxreg_No(String Servicetaxreg_No) {
		this.Servicetaxreg_No = Servicetaxreg_No;
	}
	
	public String getVattin_no() {
		return vattin_no;
	}
	
	public void setVattin_no(String vattin_no) {
		this.vattin_no = vattin_no;
	}
	public String getCst_no() {
		return cst_no;
	}
	
	public void setCst_no(String cst_no) {
		this.cst_no = cst_no;
	}
	public String getParty_type_code() {
		return party_type_code;
	}
	
	public void setParty_type_code(String party_type_code) {
		this.party_type_code = party_type_code;
	}
	public String getPotential() {
		return potential;
	}
	
	public void setPotential(String potential) {
		this.potential = potential;
	}
	
	public String getIndId() {
		return IndId;
	}
	
	public void setIndId(String IndId) {
		this.IndId = IndId;
	}
	
	public String getBeatId() {
		return BeatId;
	}
	
	public void setBeatId(String BeatId) {
		this.BeatId = BeatId;
	}
	
	public String getAreaId() {
		return AreaId;
	}
	
	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
	}
	/**
	 * @return the blocked_Reason
	 */
	public String getBlocked_Reason() {
		return blocked_Reason;
	}

	/**
	 * @param blocked_Reason the blocked_Reason to set
	 */
	public void setBlocked_Reason(String blocked_Reason) {
		this.blocked_Reason = blocked_Reason;
	}

	/**
	 * @return the block_Date
	 */
	public String getBlock_Date() {
		return block_Date;
	}

	
	public void setBlock_Date(String block_Date) {
		this.block_Date = block_Date;
	}

	public String getBlocked_By() {
		return blocked_By;
	}

	
	public void setBlocked_By(String blocked_By) {
		this.blocked_By = blocked_By;
	}
	/**
	 * @return the pin
	 */
	public String getPin() {
		return Pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		Pin = pin;
	}

	
	
	

	public String getDistributor_id() {
		return this.distributor_id;
	}
	
	public void setDistributor_id(String distributor_id) {
		this.distributor_id = distributor_id;
	}
	
	public String getDistributor_name() {
		return this.distributor_name;
	}
	public void setDistributor_name(String distributor_name) {
		this.distributor_name = distributor_name;
	}
	
	
	
	public String getPhone() {
		return this.phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getMobile() {
		return this.mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	public String getActive() {
		return Active;
	}

	
	public void setActive(String Active) {
		this.Active = Active;
	}
}
	

