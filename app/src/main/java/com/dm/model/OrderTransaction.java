package com.dm.model;

public class OrderTransaction {

	private String doc_id;
	private int doc_id_sno;
	private String web_doc_id;
	private int web_doc_id_sno;
	private String date;
	private String time;
	private String party_code;
	private String party_name;
	private String beat_name;
	private String srep_code;
	private String area_code;
	private String amount;
	private String  beat_code;
	private String  route_code;
	private String  distCollection;
	private String  qty;
	private String  rate ;
	private String remark;
	private String  product_code ,potentail;
	private String  product_name ;
	private boolean  newOrder ;
	private String address,mobileNo,industryType;
	private String timestamp;
	private String firstmonth,secondmonth,thirdmonth;
	private int firstMonthValue,secondMonthValue,thirdMonthValue;
	
private String CreatedDate;
	
	public String getCreatedDate() {
		return this.CreatedDate;
	}

	public void setCreatedDate(String CreatedDate) {
	this.CreatedDate = CreatedDate;
	}
	public String getDistCollection() {
		return this.distCollection;
	}
	/**
	 * @param address the address to set
	 */
	public void setDistCollection(String distCollection) {
		this.distCollection = distCollection;
	}
	
	public int getFirstMonthValue() {
		return firstMonthValue;
	}
	public void setFirstMonthValue(int firstMonthValue) {
		this.firstMonthValue = firstMonthValue;
	}
	public int getSecondMonthValue() {
		return secondMonthValue;
	}
	public void setSecondMonthValue(int secondMonthValue) {
		this.secondMonthValue = secondMonthValue;
	}
	public int getThirdMonthValue() {
		return thirdMonthValue;
	}
	public void setThirdMonthValue(int thirdMonthValue) {
		this.thirdMonthValue = thirdMonthValue;
	}
	private String  sample ;
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	/**
	 * @param mobileNo the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	/**
	 * @return the industryType
	 */
	public String getIndustryType() {
		return industryType;
	}
	/**
	 * @param industryType the industryType to set
	 */
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	/**
	 * @return the demo
	 */
	public String getDemo() {
		return demo;
	}
	/**
	 * @param demo the demo to set
	 */
	public void setDemo(String demo) {
		this.demo = demo;
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	private String demo;
	private String order;
	private String failed;
	private String distfailed;
	private String compt;
	private String collection;
	private String discussion;
	
	public String getDiscussion() {
		return this.discussion;
	}
	/**
	 * @param order the order to set
	 */
	public void setDiscussion(String discussion) {
		this.discussion = discussion;
	}
	
	
	public String getCollection() {
		return this.collection;
	}
	/**
	 * @param order the order to set
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getCompt() {
		return compt;
	}
	
	/**
	 * @param order the order to set
	 */
	public void setCompt(String compt) {
		this.compt = compt;
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
	 * @return the web_doc_id
	 */
	public String getWeb_doc_id() {
		return web_doc_id;
	}
	/**
	 * @param web_doc_id the web_doc_id to set
	 */
	public void setWeb_doc_id(String web_doc_id) {
		this.web_doc_id = web_doc_id;
	}
	/**
	 * @return the web_doc_id_sno
	 */
	public int getWeb_doc_id_sno() {
		return web_doc_id_sno;
	}
	/**
	 * @param web_doc_id_sno the web_doc_id_sno to set
	 */
	public void setWeb_doc_id_sno(int web_doc_id_sno) {
		this.web_doc_id_sno = web_doc_id_sno;
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
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * @return the party_code
	 */
	public String getParty_code() {
		return party_code;
	}
	/**
	 * @param party_code the party_code to set
	 */
	public void setParty_code(String party_code) {
		this.party_code = party_code;
	}
	/**
	 * @return the srep_code
	 */
	public String getSrep_code() {
		return srep_code;
	}
	/**
	 * @param srep_code the srep_code to set
	 */
	public void setSrep_code(String srep_code) {
		this.srep_code = srep_code;
	}
	/**
	 * @return the area_code
	 */
	public String getArea_code() {
		return area_code;
	}
	/**
	 * @param area_code the area_code to set
	 */
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	/**
	 * @return the beat_code
	 */
	public String getBeat_code() {
		return beat_code;
	}
	/**
	 * @param beat_code the beat_code to set
	 */
	public void setBeat_code(String beat_code) {
		this.beat_code = beat_code;
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
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the product_code
	 */
	public String getProduct_code() {
		return product_code;
	}
	/**
	 * @param product_code the product_code to set
	 */
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	/**
	 * @return the product_name
	 */
	public String getProduct_name() {
		return product_name;
	}
	/**
	 * @param product_name the product_name to set
	 */
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	/**
	 * @return the route_code
	 */
	public String getRoute_code() {
		return route_code;
	}
	/**
	 * @param route_code the route_code to set
	 */
	public void setRoute_code(String route_code) {
		this.route_code = route_code;
	}
	/**
	 * @return the party_name
	 */
	public String getParty_name() {
		return party_name;
	}
	/**
	 * @param party_name the party_name to set
	 */
	public void setParty_name(String party_name) {
		this.party_name = party_name;
	}
	/**
	 * @return the beat_name
	 */
	public String getBeat_name() {
		return beat_name;
	}
	/**
	 * @param beat_name the beat_name to set
	 */
	public void setBeat_name(String beat_name) {
		this.beat_name = beat_name;
	}
	/**
	 * @return the failed
	 */
	public String getFailed() {
		return failed;
	}
	/**
	 * @param failed the failed to set
	 */
	public void setFailed(String failed) {
		this.failed = failed;
	}

	public String getDistFailed() {
		return distfailed;
	}
	/**
	 * @param failed the failed to set
	 */
	public void setDistfailed(String distfailed) {
		this.distfailed = distfailed;
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return the newOrder
	 */
	public boolean isNewOrder() {
		return newOrder;
	}
	/**
	 * @param newOrder the newOrder to set
	 */
	public void setNewOrder(boolean newOrder) {
		this.newOrder = newOrder;
	}
	/**
	 * @return the timestamp
	 */
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the firstMonthValue
	 */
	
	/**
	 * @return the firstmonth
	 */
	public String getFirstmonth() {
		return firstmonth;
	}
	/**
	 * @param firstmonth the firstmonth to set
	 */
	public void setFirstmonth(String firstmonth) {
		this.firstmonth = firstmonth;
	}
	/**
	 * @return the secondmonth
	 */
	public String getSecondmonth() {
		return secondmonth;
	}
	/**
	 * @param secondmonth the secondmonth to set
	 */
	public void setSecondmonth(String secondmonth) {
		this.secondmonth = secondmonth;
	}
	/**
	 * @return the thirdmonth
	 */
	public String getThirdmonth() {
		return thirdmonth;
	}
	/**
	 * @param thirdmonth the thirdmonth to set
	 */
	public void setThirdmonth(String thirdmonth) {
		this.thirdmonth = thirdmonth;
	}
	/**
	 * @return the sample
	 */
	public String getSample() {
		return sample;
	}
	/**
	 * @param sample the sample to set
	 */
	public void setSample(String sample) {
		this.sample = sample;
	}
	public String getPotentail() {
		return potentail;
	}
	public void setPotentail(String potentail) {
		this.potentail = potentail;
	}
	
}