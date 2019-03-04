package com.dm.model;

public class Region {

	private String region_id;
	private String region_name;
	private String country_id;
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
	public String getRegion_id() {
		return this.region_id;
	}
	
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	
	public String getRegion_name() {
		return this.region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	
	public String getCountry_id() {
		return this.country_id;
	}
	
	public void setCountry_id(String country_id) {
		this.country_id= country_id;
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
