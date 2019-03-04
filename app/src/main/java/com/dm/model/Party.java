package com.dm.model;

import java.io.Serializable;

public class Party implements Serializable {
	private String DistId;   
	private String party_id;   
	private String party_name;
	private String AndroidId;
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
	private boolean isnewparty=false;
	private String code;
	private String OpenOrder;
	private String CreditDays;
	private String AreaId;
	private String UserId;
	private String dob;
	private String doa;

	String partyCreatorName;
	private String timeStamp;
	private String distributorName;
	String patryLat,partyLng,partyLatLngTimeStamp;


	public String getPartyLatLngTimeStamp() {
		return partyLatLngTimeStamp;
	}

	public void setPartyLatLngTimeStamp(String partyLatLngTimeStamp) {
		this.partyLatLngTimeStamp = partyLatLngTimeStamp;
	}

	public String getPartyLng() {
		return partyLng;
	}

	public String getPatryLat() {
		return patryLat;
	}

	public void setPartyLng(String partyLng) {
		this.partyLng = partyLng;
	}

	public void setPatryLat(String patryLat) {
		this.patryLat = patryLat;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistributorName() {
		return distributorName;
	}

	private String IsBlocked;


	public void setPartyCreatorName(String partyCreatorName) {
		this.partyCreatorName = partyCreatorName;
	}

	public String getPartyCreatorName() {
		return partyCreatorName;
	}

	public String gettimeStamp() {
		return this.timeStamp;
	}

	public void settimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}


	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getIsBlocked() {
		return this.IsBlocked;
	}

	public void setIsBlocked(String dob) {
		this.dob = IsBlocked;
	}

	public String getDoa() {
		return this.doa;
	}
	
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getUserId() {
		return this.UserId;
	}
	
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	public String getDistId() {
		return this.DistId;
	}
	
	public void setDistId(String DistId) {
		this.DistId = DistId;
	}
	public String getOpenOrder() {
		return this.OpenOrder;
	}
	
	public void setOpenOrder(String OpenOrder) {
		this.OpenOrder = OpenOrder;
	}
	public String getCreditDays() {
		return this.CreditDays;
	}
	
	public void setCreditDays(String CreditDays) {
		this.CreditDays = CreditDays;
	}
	public String getAndroidId() {
		return this.AndroidId;
	}
	
	public void setAndroidId(String AndroidId) {
		this.AndroidId = AndroidId;
	}
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
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
	public String getAreaId() {
		return AreaId;
	}

	
	public void setAreaId(String AreaId) {
		this.AreaId = AreaId;
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

	
	
	

	public String getParty_id() {
		return this.party_id;
	}
	
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
	
	public String getParty_name() {
		return this.party_name;
	}
	public void setParty_name(String party_name) {
		this.party_name = party_name;
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
	/**
	 * @return the isnewparty
	 */
	public boolean isIsnewparty() {
		return isnewparty;
	}
	/**
	 * @param isnewparty the isnewparty to set
	 */
	public void setIsnewparty(boolean isnewparty) {
		this.isnewparty = isnewparty;
	}

	
	
	
	
}
