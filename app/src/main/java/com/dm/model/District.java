package com.dm.model;

public class District {

	private String district_id;
	private String district_name;
	private String state_id;
	private String sync_id;
	private String CreatedDate;
	private String Active;
	
	public String getCreatedDate() {
		return CreatedDate;
	}
	
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
	}
	public String getActive() {
		return Active;
	}

	
	public void setActive(String Active) {
		this.Active = Active;
	}

	public String getDistrict_id() {
		return this.district_id;
	}
	
	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}
	
	public String getDistrict_name() {
		return this.district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	
	public String getState_id() {
		return this.state_id;
	}
	
	public void setState_id(String state_id) {
		this.state_id= state_id;
	}

	
	/**
	 * @return the sync_id
	 */
	public String getSync_id() {
		return sync_id;
	}

	/**
	 * @param sync_id the sync_id to set
	 */
	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}
	
	
}
