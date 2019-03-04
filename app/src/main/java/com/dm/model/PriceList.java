package com.dm.model;

public class PriceList {

	private String item_id;
	private String list_date;
	private String mrp;
	private String dp;
	private String rp;
	private int pricelist_id;
	private String ProductGroup_Id;
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}

	/**
	 * @return the productGroup_Id
	 */
	public String getProductGroup_Id() {
		return ProductGroup_Id;
	}

	/**
	 * @param productGroup_Id the productGroup_Id to set
	 */
	public void setProductGroup_Id(String productGroup_Id) {
		ProductGroup_Id = productGroup_Id;
	}

	
	
	public String getItem_id() {
		return this.item_id;
	}
	
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	
	public String getList_date() {
		return this.list_date;
	}
	public void setList_date(String list_date) {
		this.list_date = list_date;
	}
	
	public String getMrp() {
		return this.mrp;
	}
	
	public void setMrp(String mrp) {
		this.mrp = mrp;
	}
	
	public String getDp() {
		return this.dp;
	}
	
	public void setDp(String dp) {
		this.dp = dp;
	}

	public String getRp() {
		return rp;
	}

	public void setRp(String rp) {
		this.rp = rp;
	}

	public int getPricelist_id() {
		return pricelist_id;
	}

	public void setPricelist_id(int pricelist_id) {
		this.pricelist_id = pricelist_id;
	}
}
