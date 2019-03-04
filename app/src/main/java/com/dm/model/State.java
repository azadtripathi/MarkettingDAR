package com.dm.model;

public class State {
	private String state_id;
	private String state_name;
	private String state_region_id;
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
	public String getState_id() {
		return this.state_id;
	}
	
	public String getState_name() {
		return this.state_name;
	}
	
	public void setState_id(String state_id) {
		this.state_id = state_id;
	}
	
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getState_region_id() {
		return state_region_id;
	}

	public void setState_region_id(String state_region_id) {
		this.state_region_id = state_region_id;
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
