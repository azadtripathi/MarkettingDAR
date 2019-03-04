package com.dm.model;

public class History {

	private String code;
	private String webcode;
	private String item_id;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
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
	 * @return the item_id
	 */
	public String getItem_id() {
		return item_id;
	}
	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	
}