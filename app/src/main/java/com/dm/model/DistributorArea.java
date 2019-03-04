package com.dm.model;

public class DistributorArea {
	private String distributor_id;
	private String area_id;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public String getDistributor_id() {
		return distributor_id;
	}
	public void setDistributor_id(String distributor_id) {
		this.distributor_id = distributor_id;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

}
