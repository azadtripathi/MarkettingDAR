package com.dm.model;

public class ProductClass {
	private String class_id;
	private String class_name;
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
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
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
