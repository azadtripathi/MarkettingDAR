package com.dm.model;

public class PurchaseOrder {

	private String doc_id;
	private String webcode;
	private String distributor_code;
	private String distributor_name;
	private String group_code;
	private String subGroup_code;
	private String qty;
	private String date;
	private String item_name;
	private String item_code;
	private String quantityIncarton;
	private String unit;
	private int doc_id_sno;
	private double total;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}

	public String getWebcode() {
		return webcode;
	}
	/**
	 * @param webcode the webcode to set
	 */
	public void setWebcode(String webcode) {
		this.webcode = webcode;
	}
	/**
	 * @return the distributor_code
	 */
	public String getDistributor_code() {
		return distributor_code;
	}
	/**
	 * @param distributor_code the distributor_code to set
	 */
	public void setDistributor_code(String distributor_code) {
		this.distributor_code = distributor_code;
	}
	/**
	 * @return the group_code
	 */
	public String getGroup_code() {
		return group_code;
	}
	/**
	 * @param group_code the group_code to set
	 */
	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}
	/**
	 * @return the subGroup_code
	 */
	public String getSubGroup_code() {
		return subGroup_code;
	}
	/**
	 * @param subGroup_code the subGroup_code to set
	 */
	public void setSubGroup_code(String subGroup_code) {
		this.subGroup_code = subGroup_code;
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
	 * @return the item_id
	 */
	public String getItem_name() {
		return item_name;
	}
	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	/**
	 * @return the item_code
	 */
	public String getItem_code() {
		return item_code;
	}
	/**
	 * @param item_code the item_code to set
	 */
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	/**
	 * @return the quantityIncarton
	 */
	public String getQuantityIncarton() {
		return quantityIncarton;
	}
	/**
	 * @param quantityIncarton the quantityIncarton to set
	 */
	public void setQuantityIncarton(String quantityIncarton) {
		this.quantityIncarton = quantityIncarton;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the doc_id_sno
	 */
	public int getDoc_id_sno() {
		return doc_id_sno;
	}
	/**
	 * @param doc_id_sno the doc_id_sno to set
	 */
	public void setDoc_id_sno(int doc_id_sno) {
		this.doc_id_sno = doc_id_sno;
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
	 * @return the distributor_name
	 */
	public String getDistributor_name() {
		return distributor_name;
	}
	/**
	 * @param distributor_name the distributor_name to set
	 */
	public void setDistributor_name(String distributor_name) {
		this.distributor_name = distributor_name;
	}
	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	

	
}