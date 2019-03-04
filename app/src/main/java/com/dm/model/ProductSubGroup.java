package com.dm.model;

public class ProductSubGroup {
	private String subgroup_id;
	private String subgroup_name;
	private String group_id;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public String getSubgroup_id() {
		return subgroup_id;
	}
	public void setSubgroup_id(String subgroup_id) {
		this.subgroup_id = subgroup_id;
	}
	public String getSubgroup_name() {
		return subgroup_name;
	}
	public void setSubgroup_name(String subgroup_name) {
		this.subgroup_name = subgroup_name;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	
}
