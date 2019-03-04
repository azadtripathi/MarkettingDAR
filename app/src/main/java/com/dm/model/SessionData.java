package com.dm.model;

public class SessionData {

	private String item_id;
	private String qty;
	private String rate;
	private String cases, mfd, batchNo;
	private String unit;
	String androidId,android1Id;

	public void setMfd(String mfd)
	{
		this.mfd = mfd;
	}

	public String getMfd()
	{
		return mfd;
	}

	public void setBatchNo(String batchNo)
	{
		this.batchNo = batchNo;
	}

	public String getBatchNo()
	{
		return batchNo;
	}

	public void setAndroid1Id(String android1Id) {
		this.android1Id = android1Id;
	}

	public String getAndroid1Id() {
		return android1Id;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public String getAndroidId() {
		return androidId;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	public String getItem_id() {
		return item_id;
	}
	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}
	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

}