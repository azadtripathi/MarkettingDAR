package com.dm.model;

public class Industry {
	
	private String industry_id;
	private String industry_name;
	private String code;
	private String sync_id;
	private String CreatedDate;
	private String Active;
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
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
	}
	public String getCreatedDate() {
		return CreatedDate;
	}
	public String getActive() {
		return Active;
	}

	
	public void setActive(String Active) {
		this.Active = Active;
	}
	public String getIndustry_id() {
		return this.industry_id;
	}
	
	public String getIndustry_name() {
		return this.industry_name;
	}
	
	public void setIndustry_id(String industry_id) {
		this.industry_id = industry_id;
	}
	
	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
	}

	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
