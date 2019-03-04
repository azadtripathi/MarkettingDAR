package com.dm.model;

public class Scheme {

	private String Schemeid;
	private String Schemename;
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
	public String getSchemeid() {
		return this.Schemeid;
	}
	
	public void setSchemeid(String Schemeid) {
		this.Schemeid = Schemeid;
	}
	
	public String getSchemename() {
		return this.Schemename;
	}
	public void setSchemename(String Schemename) {
		this.Schemename = Schemename;
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
