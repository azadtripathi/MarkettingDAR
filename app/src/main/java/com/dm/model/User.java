package com.dm.model;

public class User{
	
	private String conper_id;
	private String user_name;
	private String password;
	private String level;
	private String PDA_Id;
	private int DSRAllowDays;
	private String Email;
	private String RoleId;
	private String DeptId;
	private String Active;
	private String DesigId;
	private String UserId;
	private String CreatedDate;
	private String RollType;

	public String getRollType() {
		return this.RollType;
	}

	public void setRollType(String RollType) {
		this.RollType = RollType;
	}

	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	
	 
		public String getUserId() {
			return this.UserId;
		}
		public void setUserId(String UserId) {
			this.UserId = UserId;
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
	 * @return the pDA_Id
	 */
	public String getPDA_Id() {
		return this.PDA_Id;
	}
	/**
	 * @param pDA_Id the pDA_Id to set
	 */
	public void setPDA_Id(String PDA_Id) {
		this.PDA_Id = PDA_Id;
	}
	/**
	 * @return the dSRAllowDays
	 */
	public int getDSRAllowDays() {
		return this.DSRAllowDays;
	}
	/**
	 * @param dSRAllowDays the dSRAllowDays to set
	 */
	public void setDSRAllowDays(int DSRAllowDays) {
		this.DSRAllowDays = DSRAllowDays;
	}
	/**
	 * @return the displayName
	 */
	public String getEmail() {
		return this.Email;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setEmail(String Email) {
		this.Email = Email;
	}
	/**
	 * @return the activeYN
	 */
	public String getActive() {
		return this.Active;
	}
	/**
	 * @param activeYN the activeYN to set
	 */
	public void setActive(String Active) {
		this.Active = Active;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoleId() {
		return this.RoleId;
	}
	public void setRoleId(String RoleId) {
		this.RoleId = RoleId;
	}
	
	public String getDeptId() {
		return this.DeptId;
	}
	public void setDeptId(String DeptId) {
		this.DeptId = DeptId;
	}
	public String getConper_id() {
		return conper_id;
	}
	public void setConper_id(String conper_id) {
		this.conper_id = conper_id;
	}
	public String getDesigId() {
		return this.DesigId;
	}
	public void setDesigId(String DesigId) {
		this.DesigId = DesigId;
	}
	
	
}