package com.dm.model;

public class Beat {

	private String beat_id;
	private String beat_name;
	private String area_id;
	private String sync_id;
	private String CreatedDate;
	private String Active;
	private String Description;

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
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
	public String getBeat_id() {
		return this.beat_id;
	}
	
	public void setBeat_id(String beat_id) {
		this.beat_id = beat_id;
	}
	
	public String getBeat_name() {
		return this.beat_name;
	}
	public void setBeat_name(String beat_name) {
		this.beat_name = beat_name;
	}
	
	public String getArea_id() {
		return this.area_id;
	}
	
	public void setArea_id(String area_id) {
		this.area_id = area_id;
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


