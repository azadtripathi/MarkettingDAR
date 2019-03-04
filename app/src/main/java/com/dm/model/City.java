package com.dm.model;

public class City {

	private String city_id;
	private String city_name;
	private String district_id;
	private String sync_id;
	private String CreatedDate;
	private String Active;
	private String Amount;

	public String getAmount() {
		return Amount;
	}
	public void setAmount(String Amount) {
		this.Amount = Amount;
	}
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
	public String getCity_id() {
		return this.city_id;
	}
	
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	public String getCity_name() {
		return this.city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
	public String getDistrict_id() {
		return this.district_id;
	}
	
	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
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
