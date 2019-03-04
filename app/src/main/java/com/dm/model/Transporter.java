package com.dm.model;

public class Transporter {

	private String Transport_id;
	private String Transport_name;
	private String sync_id;
	private String CreatedDate;
	
	public String getTransport_id() {
		return this.Transport_id;
	}
	
	public void setTransport_id(String Transport_id) {
		this.Transport_id = Transport_id;
	}
	public String getTransport_name() {
		return this.Transport_name;
	}
	
	public void setTransport_name(String Transport_name) {
		this.Transport_name = Transport_name;
	}
	
	public String getCreatedDate() {
		return CreatedDate;
	}
	
	public void setCreatedDate(String CreatedDate) {
		this.CreatedDate = CreatedDate;
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
