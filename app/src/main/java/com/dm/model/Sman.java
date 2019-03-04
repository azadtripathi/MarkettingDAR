package com.dm.model;

public class Sman {

	private String ConPerId;
	private String User_Name;
	private String level;
	private String UnderId;
	private String RoleId;
	private String DisplayName;
	private String SrType;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public String getSrType() {
		return this.SrType;
	}
	public void setSrType(String SrType) {
		this.SrType = SrType;
	}
	
	public String getUnderId() {
		return this.UnderId;
	}
	public void setUnderId(String UnderId) {
		this.UnderId = UnderId;
	}
	
	public String getRoleId() {
		return this.RoleId;
	}
	public void setRoleId(String RoleId) {
		this.RoleId = RoleId;
	}
	/**
	 * @return the conPerId
	 */
	public String getConPerId() {
		return ConPerId;
	}
	/**
	 * @param conPerId the conPerId to set
	 */
	public void setConPerId(String conPerId) {
		ConPerId = conPerId;
	}
	/**
	 * @return the user_Name
	 */
	public String getUser_Name() {
		return User_Name;
	}
	/**
	 * @param user_Name the user_Name to set
	 */
	public void setUser_Name(String user_Name) {
		User_Name = user_Name;
	}
	/**
	 * @return the rank
	 */
	public String getLevel() {
		return this.level;
	}
	/**
	 * @param rank the rank to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * @return the reporting
	 */
	public String getDisplayName() {
		return this.DisplayName;
	}
	/**
	 * @param reporting the reporting to set
	 */
	public void setDisplayName(String DisplayName) {
		this.DisplayName = DisplayName;
	}
	 
}