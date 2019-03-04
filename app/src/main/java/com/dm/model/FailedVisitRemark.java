package com.dm.model;

public class FailedVisitRemark {

	
	private String webcode;
	private String name;
	private String CreatedDate;
	private String Active;
	private String sync_id;
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
	/**
	 * @return the webcode
	 */
	public String getWebcode() {
		return webcode;
	}
	/**
	 * @param webcode the webcode to set
	 */
	public void setWebcode(String webcode) {
		this.webcode = webcode;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}