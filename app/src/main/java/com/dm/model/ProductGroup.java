package com.dm.model;

public class ProductGroup {
private String group_id;
private String group_name;
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
public String getGroup_id() {
	return group_id;
}
public void setGroup_id(String group_id) {
	this.group_id = group_id;
}
public String getGroup_name() {
	return group_name;
}
public void setGroup_name(String group_name) {
	this.group_name = group_name;
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
