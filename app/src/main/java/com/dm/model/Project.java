package com.dm.model;

public class Project {

	private String Projectid;
	private String Projectname;
	private String Active;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	
	public String getProjectname() {
		return this.Projectname;
	}

	
	public void setProjectname(String Projectname) {
		this.Projectname = Projectname;
	}
	public String getProjectid() {
		return this.Projectid;
	}

	
	public void setProjectid(String Projectid) {
		this.Projectid = Projectid;
	}
	public String getActive() {
		return this.Active;
	}

	
	public void setActive(String Active) {
		this.Active = Active;
	}
	
}
