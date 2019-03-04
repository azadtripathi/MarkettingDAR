package com.dm.model;

public class Item {

	private String item_id;
	private String item_name;
	private String unit;
	private String DP;
	private String MRP;
	private String ProductGroup_Id;
	private String RP;
	private String stdpack;
	private String orginalRP;
	private String sync_id;
	private String itemcode;
	private String display_Name;
	private String itemtype;
	private String segmentId;
	private String classid;
	private String pricegroup,color;
	private String CreatedDate,price;
	private String Active,qty,amount;
	private int carton_no;
	private String cases;
	private String stockUnit;
	private String androidId,ord1AndroidId,mfd,batchNo;

	private  String partyId, visitDate,party_android_id,remark;

	public void setMfd(String mfd)
	{
		this.mfd = mfd;
	}

	public String getMfd()
	{
		return mfd;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setParty_android_id(String party_android_id) {
		this.party_android_id = party_android_id;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParty_android_id() {
		return party_android_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public void setOrd1AndroidId(String ord1AndroidId) {
		this.ord1AndroidId = ord1AndroidId;
	}

	public String getAndroidId() {
		return androidId;
	}

	public String getOrd1AndroidId() {
		return ord1AndroidId;
	}

	public void setStockUnit(String stockUnit) {
		this.stockUnit = stockUnit;
	}

	public String getStockUnit() {
		return stockUnit;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getCases() {
		return cases;
	}

	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	public int getCarton_no() {
		return carton_no;
	}
	public void setCarton_no(int carton_no) {
		this.carton_no = carton_no;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
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
	/**
	 * @return the rP
	 */
	public String getRP() {
		return RP;
	}
	/**
	 * @param rP the rP to set
	 */
	public void setRP(String rP) {
		RP = rP;
	}
	/**
	 * @param item_name the item_name to set
	 */
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public  String getItem_id() {
		return this.item_id;
		
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
		
	}
	
	public String getItem_name() {
		return this.item_name;
	}
	
	public void setItem_Name(String item_name) {
		this.item_name = item_name;
	}
	
	public String getUnit() {
		return this.unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getSync_id() {
		return this.sync_id;
		
	}
	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}
	
	public String getClassid() {
		return this.classid;
	}
	public void setClassid(String classid) {
		this.classid = classid;
	}
	
	 public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	 public String getItemtype() {
			return itemtype;
		}
	
	
	public String getSegmentid() {
		return segmentId;
	}
	public void setSegmentid(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getPricegroup() {
		return pricegroup;
	}
	public void setPricegroup(String pricegroup) {
		this.pricegroup = pricegroup;
	}
	public String getDP() {
		return DP;
	}
	public void setDP(String dP) {
		DP = dP;
	}
	public String getMRP() {
		return MRP;
	}
	public void setMRP(String mRP) {
		MRP = mRP;
	}
	public String getDisplay_Name() {
		return display_Name;
	}
	public void setDisplay_Name(String display_Name) {
		this.display_Name = display_Name;
	}
	
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getStdpack() {
		return stdpack;
	}
	public void setStdpack(String stdpack) {
		this.stdpack = stdpack;
	}
	public String getOrginalRP() {
		return orginalRP;
	}
	public void setOrginalRP(String orginalRP) {
		this.orginalRP = orginalRP;
	}
}
