package com.dm.model;

public class Segment {

	private String segmentId;
	private String segment_name;
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
	public String getSegment_id() {
		return segmentId;
	}
	public void setSegment_id(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getSegment_name() {
		return segment_name;
	}
	public void setSegment_name(String segment_name) {
		this.segment_name = segment_name;
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
