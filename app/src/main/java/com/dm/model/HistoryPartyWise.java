package com.dm.model;

public class HistoryPartyWise {
private String doc_id;
private String date;
private String type;
private String CreatedDate;
	private String lock;
	public String getLock() {
		return lock;
	}
	/**
	 * @param doc_id the doc_id to set
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}

public String getCreatedDate() {
	return this.CreatedDate;
}

public void setCreatedDate(String CreatedDate) {
this.CreatedDate = CreatedDate;
}
/**
 * @return the doc_id
 */
public String getDoc_id() {
	return doc_id;
}
/**
 * @param doc_id the doc_id to set
 */
public void setDoc_id(String doc_id) {
	this.doc_id = doc_id;
}
/**
 * @return the date
 */
public String getDate() {
	return date;
}
/**
 * @param date the date to set
 */
public void setDate(String date) {
	this.date = date;
}
/**
 * @return the type
 */
public String getType() {
	return type;
}
/**
 * @param type the type to set
 */
public void setType(String type) {
	this.type = type;
}
	
}