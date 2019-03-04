package com.dm.model;

import java.io.Serializable;

public class Porder1 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String POrdId;
	private String PODocId;
	private String POrd1Id;
	private String Androidcode;
	private String Pord1Androidcode;
	private int Sno;
	private String Vdate;
	private String DistId;
	private String UserId;
	private String ItemId;
	private String Qty;
	private String Disc;
	private String Remarks;
	private String Rate,Packaging;

	private double total;
	private String item;
private String CreatedDate;

	public void setPackaging(String packaging) {
		Packaging = packaging;
	}

	public String getPackaging() {
		return Packaging;
	}

	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public String getItem() {
		return this.item;
	}
	
	public void setitem(String item) {
		this.item = item;
	}
	public String getPord1Androidcode() {
		return this.Pord1Androidcode;
	}
	
	public void setPord1Androidcode(String Pord1Androidcode) {
		this.Pord1Androidcode = Pord1Androidcode;
	}
	public String getAndroidcode() {
		return this.Androidcode;
	}
	
	public void setAndroidcode(String Androidcode) {
		this.Androidcode = Androidcode;
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
	
	public String getRate() {
		return this.Rate;
	}
	
	public void setRate(String Rate) {
		this.Rate = Rate;
	}
	public String getRemarks() {
		return this.Remarks;
	}
	
	public void setRemarks(String Remarks) {
		this.Remarks = Remarks;
	}
	public String getDisc()
	{
		if(this.Disc == null){
			this.Disc="0";
		}
		return this.Disc;
	}
	
	public void setDisc(String Disc) {
		this.Disc = Disc;
	}
	public String getQty() {
		return this.Qty;
	}
	
	public void setQty(String Qty) {
		this.Qty = Qty;
	}
	public String getItemId() {
		return this.ItemId;
	}
	
	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}
	public String getUserId() {
		return this.UserId;
	}
	
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}
	public int getSno() {
		return this.Sno;
	}
	
	public void setSno(int Sno) {
		this.Sno = Sno;
	}
	public String getPOrd1Id() {
		return this.POrd1Id;
	}
	
	public void setPOrd1Id(String POrd1Id) {
		this.POrd1Id = POrd1Id;
	}
	
	
	public String getVdate() {
		return this.Vdate;
	}
	
	public void setVdate(String Vdate) {
		this.Vdate = Vdate;
	}
	public String getPODocId() {
		return this.PODocId;
	}
	
	public void setPODocId(String PODocId) {
		this.PODocId = PODocId;
	}
	public String getPOrdId() {
		return this.POrdId;
	}
	
	public void setPOrdId(String POrdId) {
		this.POrdId = POrdId;
	}
	
	
	
	public String getDistId() {
		return this.DistId;
	}
	
	public void setDistId(String DistId) {
		this.DistId = DistId;
	}
	
}
