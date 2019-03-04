package com.dm.model;

public class Area {

	private String area_id;
	private String area_name;
	private String city_id;
	private String type;
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

	public String getArea_id() {
		return this.area_id;
	}
	
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	
	public String getArea_name() {
		return this.area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	
	public String getCity_id() {
		return this.city_id;
	}
	
	public void setCity_id(String city_id) {
		this.city_id= city_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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