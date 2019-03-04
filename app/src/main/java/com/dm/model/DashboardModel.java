package com.dm.model;

public class DashboardModel {
	private String dashboardItemName;
	private String dashboardItemdescription;
	private int imageResourceId;private String partyName,areaId,city,contactPerson,partyId;
	private String partyAddress;
	private String partyVisit;
	private String productName;
	private double productPrice;private double qty1;
	private double qty2;
	private String nameProductList;
	private double priceProductList;
	private double mpProductList;
	private double dpProductList;
	private String dateOnHistroy;
	private String operationOnHistory;
	private String dateOnOrderMgmt;
	private String operationOnOrderMgmt;
	private int rowOfOrderMgmt;
	private String Remarks;
	private String orderModel;
	private String demoModel;
	private String failedVisitModel,distfailedVisitModel,CompetitorModel,CollectionModel,DiscussionModel;
	private String partyNameOnOrderMgmtRow;
	private String beatNameViewOnOrderMgmtRow;
	private String partyNameOnOrderMgmtDemoRow;
	private String beatNameViewOnOrderMgmtDemoRow;
	private String partyNameOnOrderMgmtFailedVisitRow;
	private String beatNameViewOnOrderMgmtFailedVisitRow;
	private String productNameOnOrderMgmt;
	private double priceProductOnOrderMgmt;
	private double qtyProductOnOrderMgmt;
	private String productNameOnDemoOrderMgmt;
	private String priceProductOnDemoOrderMgmt;
	private String qtyProductOnDemoOrderMgmt;
	private String remarkFailedVisitOnOrderMgmt;
	private double totalOrderValueOnOrderProductList;
	private String samqty;
	private String productNameOnDemoRetailer;
	private double priceProductOnDemoRetailer;
	private double mpProductOnDemoRetailer;
	private double dpProductOnDemoRetailer;
	private double qtyOnDemoRetailer, sampleQtyOnDemoRetailer,rpProductList,priceProduct,qtyInCatron;
	DashboardModel dashboardModel;
	private String itemId;
	private String quantityOfProduct;
	private String qtyInSample;
	private String dsrLockDate,Rate;
	private String potential,productNameOnPruchaseOrder;
	private double priceViewOnPruchaseOrder;
	private double quantityOnPruchaseOrder;
	private String quantity;
	private String date, dateOfDate,ditributorName,distId;
	private String cartonqtyProduct, type,doc_id,nextVisitDateOnOrderMgmt;
	private String webcode,remark,transId,Picture,lock,nextVisitDate,nextVisitTime, itemName,itemPrice,itemQty,sampleQty,remarkOnDemo,unitOnPruchaseOrder;
	private String partyNameOnReportAbove,address,industryType,monbileNo,unitOrProduct;
	private String monthfirst,monthsecond,monththird,monthfirstvalue,monthsecondvalue,monththirdvalue,partyname;
	private String amount,color,ProdClass,ProdSegment,ProdGrp,mode,bank,branch,chqdate,chqno,DropDownId;
	private String DistCollModel,androidId,name,userId,vdate,noOfDays,fromDate,toDate,reason,status,otherActivity,discount,stdPack,path;
	private String SyncId,startTime,endTime,claimAmount,billNo,active;
	private String id,docid,empname,tfdate,ttdate,astatus,apamt,Advammount,fromtime,totime,stateid;
	private String cases,distName,partyCreatorName;
	String android1Id,OrderAndroidId;
	private String Area,complainNature;
	public String GSTNo;
	private String pkging,total;
	String salesRetUnit,salesRetCases,salesBachNo,salesMfd,saledAndroidId,salesAndroidId1;
	String partyVendorName, cgstAmount, sgstAmount, isGSTRegistered,IGSTAmount;



	public void setSgstAmount(String sgstAmount) {
		this.sgstAmount = sgstAmount;
	}

	public void setIsGSTRegistered(String isGSTRegistered) {
		this.isGSTRegistered = isGSTRegistered;
	}
	public void setCgstAmount(String cgstAmount) {
		this.cgstAmount = cgstAmount;
	}

	public void setPartyVendorName(String partyVendorName) {
		this.partyVendorName = partyVendorName;
	}

	public String getIGSTAmount() {
		return IGSTAmount;
	}

	public void setIGSTAmount(String IGSTAmount) {
		this.IGSTAmount = IGSTAmount;
	}

	public String getPartyVendorName() {
		return partyVendorName;
	}

	public String getCgstAmount() {
		return cgstAmount;
	}

	public String getSgstAmount() {
		return sgstAmount;
	}

	public String getIsGSTRegistered() {
		return isGSTRegistered;
	}


	public void setSalesRetCases(String salesRetCases) {
		this.salesRetCases = salesRetCases;
	}

	public String getSalesRetCases() {
		return salesRetCases;
	}

	public void setSalesRetUnit(String salesRetUnit) {
		this.salesRetUnit = salesRetUnit;
	}

	public String getSalesRetUnit() {
		return salesRetUnit;
	}

	public void setSalesBachNo(String salesBachNo) {
		this.salesBachNo = salesBachNo;
	}

	public String getSalesBachNo() {
		return salesBachNo;
	}

	public String getSalesMfd() {
		return salesMfd;
	}

	public String getSaledAndroidId() {
		return saledAndroidId;
	}

	public String getSalesAndroidId1() {
		return salesAndroidId1;
	}

	public void setSaledAndroidId(String saledAndroidId) {
		this.saledAndroidId = saledAndroidId;
	}

	public void setSalesMfd(String salesMfd) {
		this.salesMfd = salesMfd;
	}

	public void setSalesAndroidId1(String salesAndroidId1) {
		this.salesAndroidId1 = salesAndroidId1;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal() {
		return total;
	}

	public void setPkging(String pkging) {
		this.pkging = pkging;
	}

	public String getPkging() {
		return pkging;
	}

	public String getGSTNo() {
		return GSTNo;
	}

	public void setGSTNo(String GSTNo) {
		this.GSTNo = GSTNo;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public void setPartyCreatorName(String partyCreatorName) {
		this.partyCreatorName = partyCreatorName;
	}

	public String getPartyCreatorName() {
		return partyCreatorName;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getID() {
		return this.id;
	}
	public void setID(String id) {
		this.id = id;
	}

	public String getDocid() {
		return this.docid;
	}
	public void setDocid(String did) {
		this.docid = did;
	}

	public String getcomplainNature() {
		return this.complainNature;
	}
	public void setcomplainNature(String complainNature) {this.complainNature = complainNature;}

	public String getEmpname() {
		return this.empname;
	}
	public void setEmpname(String name) {
		this.empname = name;
	}

	public String getTfdate() {
		return this.tfdate;
	}
	public void setTfdate(String tfd) {
		this.tfdate = tfd;
	}

	public String getTtdate() {
		return this.ttdate;
	}
	public void setTtdate(String ttd) {
		this.ttdate = ttd;
	}
	public void setAstatus(String astatus) {
		this.astatus = astatus;
	}
	public String getAstatus() {
		return this.astatus;
	}
	public void setAdvamount(String apamm) {
		this.Advammount = apamm;
	}
	public String getAdvamount() {
		return this.Advammount;
	}
	public void setApamount(String amm) {
		this.apamt = amm;
	}
	public String getApamount() {
		return this.apamt;
	}

	public void setFromtime(String ft) {
		this.fromtime =ft;
	}
	public String getFromtime() {
		return this.fromtime;
	}

	public void setTotime(String tt) {
		this.totime = tt;
	}
	public String getTotime() {
		return this.totime;
	}


	String brandActivity, meetActivity,roadshowActivity,scheme,genralInfo;

	public void setBrandActivity(String brandActivity) {
		this.brandActivity = brandActivity;
	}

	public String getBrandActivity() {
		return brandActivity;
	}

	public void setMeetActivity(String meetActivity) {
		this.meetActivity = meetActivity;
	}

	public String getMeetActivity() {
		return meetActivity;
	}

	public void setRoadshowActivity(String roadshowActivity) {
		this.roadshowActivity = roadshowActivity;
	}

	public String getRoadshowActivity() {
		return roadshowActivity;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getGenralInfo() {
		return genralInfo;
	}

	public void setGenralInfo(String genralInfo) {
		this.genralInfo = genralInfo;
	}

	public DashboardModel()
	{}
	public DashboardModel filleredBeatWiseMonthlySecondryList(String monthfirstvalue, String monthsecondvalue, String monththirdvalue, String partyname)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setMonthfirstvalue(monthfirstvalue);
		dashboardModel.setMonthsecondvalue(monthsecondvalue);
		dashboardModel.setMonththirdvalue(monththirdvalue);
		dashboardModel.setPartyname(partyname);
		return dashboardModel;
	}


	public DashboardModel TourPlanDateWiseModel(String tDate, String city, String dist, String purpose, String id, String smId, String vdate, String remark, String transId, String isUpload)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setTtdate(tDate);
		dashboardModel.setCity(city);
		dashboardModel.setDitributorName(dist);
		dashboardModel.setType(purpose);
		dashboardModel.setID(id);
		dashboardModel.setEmpname(smId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setRemark(remark);
		dashboardModel.setTransId(transId);
		dashboardModel.setActive(isUpload);
		return dashboardModel;

	}

	public DashboardModel distItemStockModel(String productName, double priceProduct, double qtyInCatron, String itemId, String quantityOfProduct, String unitOrProduct, String amount , String color, String cases, String syncId, String stockUnit, String androidId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductName(productName);
		dashboardModel.setPriceProduct(priceProduct);
		dashboardModel.setQtyInCatron(qtyInCatron);
		dashboardModel.setItemId(itemId);
		dashboardModel.setQuantityOfProduct(quantityOfProduct);
		dashboardModel.setUnitOrProduct(unitOrProduct);
		dashboardModel.setAmount(amount);
		dashboardModel.setColor(color);
		dashboardModel.setCases(cases);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setUnitOnPruchaseOrder(stockUnit);
		dashboardModel.setAndroidId(androidId);
		return dashboardModel;
	}

	public DashboardModel FindTourPlanDateWiseModel(String vdate, String doc_id, String name, String smId, String status, String Appremark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(vdate);
		dashboardModel.setDoc_id(doc_id);
		dashboardModel.setName(name);
		dashboardModel.setStatus(status);
		dashboardModel.setID(smId);
		dashboardModel.setRemark(Appremark);
		return dashboardModel;
	}
	
	public DashboardModel filleredCollectionList(String partyName, String amt)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyName(partyName);
		dashboardModel.setAmount(amt);
		return dashboardModel;
	}

	public DashboardModel beatMasterListFind(String id, String bname, String rem, String area, String syncId, String active)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(id);
		dashboardModel.setBeatNameViewOnOrderMgmtRow(bname);
		dashboardModel.setAreaId(area);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setActive(active);
		dashboardModel.setRemark(rem);
		return dashboardModel;
	}
	public DashboardModel expenseGrpListFind(String id, String name, String rem, String fromDate, String toDate)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(id);
		dashboardModel.setName(name);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setRemark(rem);
		return dashboardModel;
	}

	public DashboardModel expenseEntryListFind(String id, String exp, String city, String billNo, String billDate, String billAmount,
											   String claimAmt,String supportAttach,String gstNo,String partyVendorName,String sgstAmount,
											   String cgstAmount,String isGSTRegistered,String IGSTAmount)
	{

		dashboardModel=new DashboardModel();
		dashboardModel.setGSTNo(gstNo);
		dashboardModel.setDoc_id(id);
		dashboardModel.setName(exp);
		dashboardModel.setCity(city);
		dashboardModel.setBillNo(billNo);
		dashboardModel.setDate(billDate);
		dashboardModel.setAmount(billAmount);
		dashboardModel.setClaimAmount(claimAmt);
		dashboardModel.setOtherActivity(supportAttach);
		dashboardModel.setIsGSTRegistered(isGSTRegistered);
		dashboardModel.setPartyVendorName(partyVendorName);
		dashboardModel.setCgstAmount(cgstAmount);
		dashboardModel.setSgstAmount(sgstAmount);
		dashboardModel.setIGSTAmount(IGSTAmount);

		return dashboardModel;
	}

	public DashboardModel expenseEntryListFind(String id, String exp, String city, String billNo, String billDate, String billAmount, String claimAmt, String supportAttach,String gstNo)
	{

		dashboardModel=new DashboardModel();
		dashboardModel.setGSTNo(gstNo);
		dashboardModel.setDoc_id(id);
		dashboardModel.setName(exp);
		dashboardModel.setCity(city);
		dashboardModel.setBillNo(billNo);
		dashboardModel.setDate(billDate);
		dashboardModel.setAmount(billAmount);
		dashboardModel.setClaimAmount(claimAmt);
		dashboardModel.setOtherActivity(supportAttach);
		return dashboardModel;
	}
	public DashboardModel filleredRetailerAboveLists(String partyNameOnReportAbove, String address, String industryType, String monbileNo, String potential)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyNameOnReportAbove(partyNameOnReportAbove);
		dashboardModel.setAddress(address);
		dashboardModel.setIndustryType(industryType);
		dashboardModel.setMonbileNo(monbileNo);
		dashboardModel.setPotential(potential);
		return dashboardModel;
	}
	public DashboardModel filleredRetailerAboveList(String partyNameOnReportAbove, String address, String industryType, String monbileNo)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyNameOnReportAbove(partyNameOnReportAbove);
		dashboardModel.setAddress(address);
		dashboardModel.setIndustryType(industryType);
		dashboardModel.setMonbileNo(monbileNo);
		return dashboardModel;
	}
	public DashboardModel purchaseOrderListOnList(String dateOfDate, String ditributorName)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDateOfDate(dateOfDate);
		dashboardModel.setDitributorName(ditributorName);
		return dashboardModel;
	}
	public DashboardModel partyCollectionListPartyWiseModel(String amount, String mode, String chqdate, String chqno, String remark, String branch, String bank)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setRemark(remark);
		dashboardModel.setAmount(amount);
		dashboardModel.setMode(mode);
		dashboardModel.setChqdate(chqdate);
		dashboardModel.setChqno(chqno);
		dashboardModel.setBranch(branch);
		dashboardModel.setBank(bank);
		return dashboardModel;
	}
	public DashboardModel partyCollectionListPartyWiseModel(String vdate, String amount, String mode, String chqdate, String chqno, String remark, String branch, String bank, String partyName, String type)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(vdate);
		dashboardModel.setRemark(remark);
		dashboardModel.setAmount(amount);
		dashboardModel.setMode(mode);
		dashboardModel.setChqdate(chqdate);
		dashboardModel.setChqno(chqno);
		dashboardModel.setBranch(branch);
		dashboardModel.setBank(bank);
		dashboardModel.setPartyName(partyName);
		dashboardModel.setType(type);
		return dashboardModel;
	}
	
	public DashboardModel discussionDistributerWiseModel(String remark, String distName)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setRemark(remark);
		dashboardModel.setPartyname(distName);
		return dashboardModel;
	}
	
	public DashboardModel failedVisitListPartyWiseModel(String remark, String nextVisitDate, String docid, String reason, String androidId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setRemark(remark);
		dashboardModel.setNextVisitDate(nextVisitDate);
		dashboardModel.setDoc_id(docid);
		dashboardModel.setReason(reason);
		dashboardModel.setAndroidId(androidId);
		return dashboardModel;
	}
	
	
	
	public DashboardModel competitorListPartyWiseModel(String item, String qty, String rate)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setItemName(item);
		dashboardModel.setQuantity(qty);
		dashboardModel.setRate(rate);
		return dashboardModel;
	}
	
	public DashboardModel findPartyListPartyWiseModel(String partyId, String androidId, String partyName, String areaId, String beatId, String partyType, String city, String mobile, String contactPerson,
													  String industryType, String distName, String active, String creatorName)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyName(partyName);
		dashboardModel.setAreaId(areaId);
		dashboardModel.setBeatNameViewOnOrderMgmtDemoRow(beatId);
		dashboardModel.setType(partyType);
		dashboardModel.setCity(city);
		dashboardModel.setMonbileNo(mobile);
		dashboardModel.setContactPerson(contactPerson);
		dashboardModel.setPartyId(partyId);
		dashboardModel.setItemId(androidId);
		dashboardModel.setIndustryType(industryType);
		dashboardModel.setActive(active);
		dashboardModel.setDistName(distName);
		dashboardModel.setPartyCreatorName(creatorName);

		return dashboardModel;
	}


	public DashboardModel findLeaveRequestListModel(String leaveDocId, String androidId, String userId, String vdate, String noOfDays, String fromDate, String toDate, String reason, String status, String remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);

		dashboardModel.setRemark(remark);
		return dashboardModel;
	}


	public DashboardModel ditailDistributorStock(String dname, String pname, String cases, String unit, String androidID)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDistName(dname);
		dashboardModel.setProductName(pname);
		dashboardModel.setCases(cases);
		dashboardModel.setUnitOrProduct(unit);
		dashboardModel.setAndroidId(androidID);
		return dashboardModel;
	}
	public DashboardModel listDistributorStock(String dname, String area, String cases, String unit, String distId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDistName(dname);
		dashboardModel.setArea(area);
		dashboardModel.setCases(cases);
		dashboardModel.setUnitOrProduct(unit);
		dashboardModel.setDistId(distId);
		return dashboardModel;
	}
	public DashboardModel findLeaveRequestListModel(String leaveDocId, String androidId, String userId, String vdate, String noOfDays, String fromDate, String toDate, String salesman, String reason, String remark, String status, String LVRQId, String lvrid, String smid, String DropDownId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setSamqty(salesman);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		dashboardModel.setLock(LVRQId);
		dashboardModel.setDocid(lvrid);
		dashboardModel.setSyncId(smid);

		dashboardModel.setDropDownId(DropDownId);
		return dashboardModel;
	}


	//write by sandeep singh 07-03-17
	//strat
	public DashboardModel advanceLeaveRequestListModel(String id, String docid, String empname, String ftd, String ttd, String stat, String amt, String apamt, String ftime, String ttime)
	{
		System.out.println("DataInDAHBOARD"+id+"-"+docid+"-"+empname+""+ftd+""+ttd+""+status+""+amt+""+apamt);
		dashboardModel=new DashboardModel();
		dashboardModel.setID(id);
		dashboardModel.setDocid(docid);
		dashboardModel.setEmpname(empname);
		dashboardModel.setTfdate(ftd);
		dashboardModel.setTtdate(ttd);
		dashboardModel.setAstatus(stat);
		dashboardModel.setAdvamount(amt);
		dashboardModel.setApamount(apamt);
		dashboardModel.setFromtime(ftime);
		dashboardModel.setTotime(ttime);
		return dashboardModel;
	}
	public DashboardModel advanceLeaveRequestdataModel(String ftd, String tdt, String amt, String rmk, String RescStatus, String ftime, String ttime, String City, String State)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setTfdate(ftd);
		dashboardModel.setTtdate(tdt);
		dashboardModel.setAdvamount(amt);
		dashboardModel.setAstatus(RescStatus);
		dashboardModel.setFromtime(ftime);
		dashboardModel.setTotime(ttime);
		dashboardModel.setRemark(rmk);
		dashboardModel.setCity(City);
		dashboardModel.setStateID(State);

		return dashboardModel;
	}
	//end
	public DashboardModel aprovalLeaveRequestListModel(String leaveDocId, String androidId, String userId, String vdate, String noOfDays, String fromDate, String toDate, String salesman, String reason, String remark, String status, String LVRQId, String lvrid, String smid, String DropDownId)
	{
		/*dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		return dashboardModel;*/

		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setSamqty(salesman);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		dashboardModel.setLock(LVRQId);
		dashboardModel.setDocid(lvrid);
		dashboardModel.setSyncId(smid);

		dashboardModel.setDropDownId(DropDownId);
		return dashboardModel;

	}
	//public DashboardModel aprovalLeaveRequestListModel(String leaveDocId,String androidId,String userId,String vdate,String noOfDays,String fromDate,String toDate,String reason,String status,String remark,String DropDownId)
	public DashboardModel aprovalLeaveRequestListModel(String leaveDocId, String androidId, String userId, String vdate, String noOfDays, String fromDate, String toDate, String reason, String status, String remark)
	{
		/*dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		dashboardModel.setDropDownId(DropDownId);
		return dashboardModel;*/

		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(leaveDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setUserId(userId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setNoOfDays(noOfDays);
		dashboardModel.setFromDate(fromDate);
		dashboardModel.setToDate(toDate);
		dashboardModel.setReason(reason);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		return dashboardModel;
	}
	public DashboardModel findtourPlan(String item)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(item);
		return dashboardModel;
	}
	public DashboardModel findcityPlan(String id, String name)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setAreaId(id);
		dashboardModel.setName(name);
		return dashboardModel;
	}
	public DashboardModel findBeatPlanListModel(String beatPlanDocId, String androidId, String startdate, String smid, String status)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(beatPlanDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setFromDate(startdate);
		dashboardModel.setUserId(smid);
		dashboardModel.setStatus(status);
		return dashboardModel;
	}

	/****************************************** Ashutosh  ********************************************/
	public DashboardModel findComplaintEntryModel(String date, String SMName, String DocId, String Complainnature,
												  String ItemName,String Distributor,String imgUrl)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDate(date);
		dashboardModel.setName(SMName);
		dashboardModel.setDocid(DocId);
		dashboardModel.setcomplainNature(Complainnature);
		dashboardModel.setItemName(ItemName);
		dashboardModel.setDitributorName(Distributor);
		dashboardModel.setPath(imgUrl);
		return dashboardModel;
	}
	public DashboardModel findBeatPlanListModel(String beatPlanDocId, String androidId, String startdate, String smid, String status, String remark, String App_remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(beatPlanDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setFromDate(startdate);
		dashboardModel.setUserId(smid);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		dashboardModel.setAstatus(App_remark);
		return dashboardModel;
	}
	public DashboardModel findBeatPlanAprovalModel(String beatPlanDocId, String androidId, String startdate, String smid, String status)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(beatPlanDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setFromDate(startdate);
		dashboardModel.setUserId(smid);
		dashboardModel.setStatus(status);
		return dashboardModel;
	}

	/****************************************** Ashutosh  ********************************************/

	/****************************************** Ashutosh  ********************************************/
	public DashboardModel findBeatPlanAprovalModel(String beatPlanDocId, String androidId, String startdate, String smid, String status, String remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDoc_id(beatPlanDocId);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setFromDate(startdate);
		dashboardModel.setUserId(smid);
		dashboardModel.setStatus(status);
		dashboardModel.setRemark(remark);
		return dashboardModel;
	}
	/****************************************** Ashutosh  ********************************************/
	
	public DashboardModel competitorListAndroidIdPartyWiseModel(String androidId, String item, String qty, String rate, String remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setItemId(androidId);
		dashboardModel.setItemName(item);
		dashboardModel.setQuantity(qty);
		dashboardModel.setRate(rate);
		dashboardModel.setRemark(remark);
		return dashboardModel;
	}
	public DashboardModel competitorListPartyWiseModel(String partyId, String name, String androidId, String docId, String vDate, String otherActivity, String discount, String item, String qty, String rate, String path, String remark, String type,
													   String brandActivity, String meetActivity, String roadShow, String scheme, String genralInfo)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyName(partyId);
		dashboardModel.setName(name);
		dashboardModel.setItemName(item);
		dashboardModel.setQuantity(qty);
		dashboardModel.setRate(rate);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setDoc_id(docId);
		dashboardModel.setVdate(vDate);
		dashboardModel.setOtherActivity(otherActivity);
		dashboardModel.setDiscount(discount);
		dashboardModel.setPicture(path);
		dashboardModel.setRemark(remark);
		dashboardModel.setType(type);
		dashboardModel.setBrandActivity(brandActivity);
		dashboardModel.setMeetActivity(meetActivity);
		dashboardModel.setRoadshowActivity(roadShow);
		dashboardModel.setScheme(scheme);
		dashboardModel.setGenralInfo(genralInfo);
		return dashboardModel;
		
	}
	
	public DashboardModel demoProductListPartyWiseModel(String prodClass, String prodSegment, String prodGrp, String Remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProdClass(prodClass);
		dashboardModel.setProdSegment(prodSegment);
		dashboardModel.setProdGrp(prodGrp);
		dashboardModel.setRemark(Remark);
		return dashboardModel;
	}
	public DashboardModel productListPartyWiseModel(String amount, String remark, String doc_id, String androidId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setAmount(amount);
		dashboardModel.setRemark(remark);
		dashboardModel.setDoc_id(doc_id);
		dashboardModel.setAndroidId(androidId);
		return dashboardModel;
	}
	
	public DashboardModel(String dateOnHistroy,String operationOnHistory)
	{
		this.dateOnHistroy=dateOnHistroy;
		this.operationOnHistory=operationOnHistory;
	}
	public DashboardModel HistoryPartyWiseModel(String date, String type, String doc_id)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setDate(date);
		dashboardModel.setType(type);
		dashboardModel.setDoc_id(doc_id);
		return dashboardModel;
	}
	public DashboardModel(String nameProductList,double priceProductList,double mpProductList,double dpProductList,double rpProductList,int whichRow,String cartonqtyProduct) {
		this.nameProductList=nameProductList;
		this.priceProductList=priceProductList;
		this.mpProductList=mpProductList;
		this.dpProductList=dpProductList;
		this.setRpProductList(rpProductList);
		this.setCartonqtyProduct(cartonqtyProduct);
	}
	public DashboardModel OrderDemoModel(String productName, double priceProduct, String qtyInSample, String itemId, String quantity)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductName(productName);
		dashboardModel.setPriceProduct(priceProduct);
		dashboardModel.setQtyInSample(qtyInSample);
		dashboardModel.setItemId(itemId);
		dashboardModel.setQuantity(quantity);
		return dashboardModel;
	}

	public void setAndroid1Id(String android1Id) {
		this.android1Id = android1Id;
	}

	public String getAndroid1Id() {
		return android1Id;
	}

	public String getOrderAndroidId() {
		return OrderAndroidId;
	}

	public void setOrderAndroidId(String orderAndroidId) {
		OrderAndroidId = orderAndroidId;
	}


	public DashboardModel OrderBookModel(String productName, double priceProduct, double qtyInCatron, String itemId, String quantityOfProduct, String unitOrProduct, String amount , String color, String cases,
										 String syncId, String AndroidId, String ord1AndroidId,String batch,String mfd)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductName(productName);
		dashboardModel.setPriceProduct(priceProduct);
		dashboardModel.setQtyInCatron(qtyInCatron);
		dashboardModel.setItemId(itemId);
		dashboardModel.setQuantityOfProduct(quantityOfProduct);
		dashboardModel.setUnitOrProduct(unitOrProduct);
		dashboardModel.setAmount(amount);
		dashboardModel.setColor(color);
		dashboardModel.setCases(cases);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setAndroid1Id(ord1AndroidId);
		dashboardModel.setOrderAndroidId(AndroidId);
		dashboardModel.setSalesMfd(mfd);
		dashboardModel.setSalesBachNo(batch);

		return dashboardModel;
	}
	public DashboardModel OrderBookModel(String productName, double priceProduct, double qtyInCatron, String itemId, String quantityOfProduct, String unitOrProduct, String amount , String color, String cases, String syncId, String AndroidId, String ord1AndroidId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductName(productName);
		dashboardModel.setPriceProduct(priceProduct);
		dashboardModel.setQtyInCatron(qtyInCatron);
		dashboardModel.setItemId(itemId);
		dashboardModel.setQuantityOfProduct(quantityOfProduct);
		dashboardModel.setUnitOrProduct(unitOrProduct);
		dashboardModel.setAmount(amount);
		dashboardModel.setColor(color);
		dashboardModel.setCases(cases);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setAndroid1Id(ord1AndroidId);
		dashboardModel.setOrderAndroidId(AndroidId);

		return dashboardModel;
	}
	public DashboardModel demoProductRetailerModel(String productNameOnDemoRetailer, double priceProductOnDemoRetailer, double qtyOnDemoRetailer, double sampleQtyOnDemoRetailer)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnDemoRetailer(productNameOnDemoRetailer);
		dashboardModel.setPriceProductOnDemoRetailer(priceProductOnDemoRetailer);
		dashboardModel.setQtyOnDemoRetailer(qtyOnDemoRetailer);
		dashboardModel.setSampleQtyOnDemoRetailer(sampleQtyOnDemoRetailer);
		return dashboardModel;
	}
	public DashboardModel purchaseOrderModel(String productNameOnPruchaseOrder, String unitOnPruchaseOrder, double quantityOnPruchaseOrder, String rate,String pkg,String total)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnPruchaseOrder(productNameOnPruchaseOrder);
		dashboardModel.setUnitOnPruchaseOrder(unitOnPruchaseOrder);
		dashboardModel.setQuantityOnPruchaseOrder(quantityOnPruchaseOrder);
		dashboardModel.setRate(rate);
		dashboardModel.setPkging(pkg);
		dashboardModel.setTotal(total);
		return dashboardModel;
	}


// 30 august sales return ...
	public DashboardModel salesReturnModel(String productName, String cases, String unit, String mfd,
										   String batchNo,String androidId,String androidId1)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnPruchaseOrder(productName);
		dashboardModel.setSalesRetUnit(unit);
		dashboardModel.setSalesRetCases(cases);
		dashboardModel.setSalesBachNo(batchNo);
		dashboardModel.setSalesMfd(mfd);
		dashboardModel.setSaledAndroidId(androidId);
		dashboardModel.setSalesAndroidId1(androidId1);
		return dashboardModel;
	}


	
	public DashboardModel OrderItemModel(String productNameOnPruchaseOrder, String unitOnOrder, String rate)
	{
		
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnPruchaseOrder(productNameOnPruchaseOrder);
		dashboardModel.setUnitOnPruchaseOrder(unitOnOrder);
		dashboardModel.setRate(rate);
		return dashboardModel;
	}
	public DashboardModel failedVisitremarkMgmtModel(String remarkFailedVisitOnOrderMgmt, String nextVisitDate)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setRemarkFailedVisitOnOrderMgmt(remarkFailedVisitOnOrderMgmt);
		dashboardModel.setNextVisitDateOnOrderMgmt(nextVisitDate);
		return dashboardModel;
	}
	public DashboardModel orderDemoProductMgmtModel(String productNameOnDemoOrderMgmt, String priceProductOnDemoOrderMgmt, String qtyProductOnDemoOrderMgmt, String samqty, String remarkOnDemo)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnDemoOrderMgmt(productNameOnDemoOrderMgmt);
		dashboardModel.setPriceProductOnDemoOrderMgmt(priceProductOnDemoOrderMgmt);
		dashboardModel.setQtyProductOnDemoOrderMgmt(qtyProductOnDemoOrderMgmt);
		dashboardModel.setSamqty(samqty);
		dashboardModel.setRemark(remarkOnDemo);
		return dashboardModel;
	}
	public DashboardModel orderProductMgmtModel(String productNameOnOrderMgmt, double priceProductOnOrderMgmt, double qtyProductOnOrderMgmt, double totalOrderValueOnOrderProductList)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setProductNameOnOrderMgmt(productNameOnOrderMgmt);
		dashboardModel.setPriceProductOnOrderMgmt(priceProductOnOrderMgmt);
		dashboardModel.setQtyProductOnOrderMgmt(qtyProductOnOrderMgmt);
		dashboardModel.setTotalOrderValueOnOrderProductList(totalOrderValueOnOrderProductList);
		return dashboardModel;
	}
	public DashboardModel orderMgmtModel(String orderModel, String demoModel, String failedVisitModel, String CompetitorModel, String dateOnOrderMgmt, String dsrLockDate)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setOrderModel(orderModel);
		dashboardModel.setDemoModel(demoModel);
		dashboardModel.setFailedVisitModel(failedVisitModel);
		dashboardModel.setCompetitorModel(CompetitorModel);
		dashboardModel.setDateOnOrderMgmt(dateOnOrderMgmt);
		dashboardModel.setDsrLockDate(dsrLockDate);
		return dashboardModel;
	}
	
	public DashboardModel orderMgmtModel(String orderModel, String demoModel, String failedVisitModel, String CompetitorModel, String CollectionModel, String DiscussionModel, String dateOnOrderMgmt, String dsrLockDate)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setOrderModel(orderModel);
		dashboardModel.setDemoModel(demoModel);
		dashboardModel.setFailedVisitModel(failedVisitModel);
		dashboardModel.setCompetitorModel(CompetitorModel);
		dashboardModel.setCollectionModel(CollectionModel);
		dashboardModel.setDiscussionModel(DiscussionModel);
		dashboardModel.setDateOnOrderMgmt(dateOnOrderMgmt);
		dashboardModel.setDsrLockDate(dsrLockDate);
		return dashboardModel;
	}
	public DashboardModel orderMgmtModel(String orderModel, String demoModel, String failedVisitModel, String CompetitorModel, String CollectionModel, String DiscussionModel, String DistCollModel, String dateOnOrderMgmt, String dsrLockDate, String distFailedVisit)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setOrderModel(orderModel);
		dashboardModel.setDemoModel(demoModel);
		dashboardModel.setFailedVisitModel(failedVisitModel);
		dashboardModel.setCompetitorModel(CompetitorModel);
		dashboardModel.setCollectionModel(CollectionModel);
		dashboardModel.setDiscussionModel(DiscussionModel);
		dashboardModel.setDistCollModel(DistCollModel);
		dashboardModel.setDateOnOrderMgmt(dateOnOrderMgmt);
		dashboardModel.setDsrLockDate(dsrLockDate);
		dashboardModel.setDistFailedVisitModel(distFailedVisit);
		return dashboardModel;
	}

	public DashboardModel orderPartyListOnDateWiseList(String partyNameOnOrderMgmtRow, String areaNameViewOnOrderMgmtRow, String amount, String remark, String androidId, String docId, String vdate, String qty)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setBeatNameViewOnOrderMgmtRow(areaNameViewOnOrderMgmtRow);
		dashboardModel.setPartyNameOnOrderMgmtRow(partyNameOnOrderMgmtRow);
		dashboardModel.setAmount(amount);
		dashboardModel.setRemark(remark);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setDoc_id(docId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setQuantity(qty);

		return dashboardModel;
	}


	public DashboardModel orderPartyListOnDateWiseList(String partyNameOnOrderMgmtRow, String areaNameViewOnOrderMgmtRow, String amount, String remark, String androidId, String docId, String vdate, String qty, String beatName)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setBeatNameViewOnOrderMgmtRow(areaNameViewOnOrderMgmtRow);
		dashboardModel.setPartyNameOnOrderMgmtRow(partyNameOnOrderMgmtRow);
		dashboardModel.setAmount(amount);
		dashboardModel.setRemark(remark);
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setDoc_id(docId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setQuantity(qty);
		dashboardModel.setBeatNameViewOnOrderMgmtDemoRow(beatName);
		return dashboardModel;
	}
	
	public DashboardModel dsrListOnDateWise(String vDate, String vDocId, String andId, String sTime, String eTime, String remark, String lock, String status)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(vDate);
		dashboardModel.setDoc_id(vDocId);
		dashboardModel.setAndroidId(andId);
		dashboardModel.setFromDate(sTime);
		dashboardModel.setToDate(eTime);
		dashboardModel.setRemark(remark);
		dashboardModel.setLock(lock);	
		dashboardModel.setStatus(status);	
		return dashboardModel;
	}
	
	public DashboardModel collectionListPartyWise(String pDate, String DocId, String andId, String dname, String amount, String mode, String chequeNo, String chequeDate, String distId, String syncId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(pDate);
		dashboardModel.setDoc_id(DocId);
		dashboardModel.setAndroidId(andId);
		dashboardModel.setDitributorName(dname);
		dashboardModel.setDistId(distId);
		dashboardModel.setAmount(amount);
		dashboardModel.setMode(mode);
		dashboardModel.setChqno(chequeNo);	
		dashboardModel.setChqdate(chequeDate);	
		dashboardModel.setSyncId(syncId);	
		
		return dashboardModel;
	}
	public DashboardModel discussionListDistWise(String androidId, String vDocId, String startTime, String endTime, String nextVisitDate, String nextVisitTime, String remark, String distId, String distName, String syncId, String vdate, String s, String type, String File, String docId)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setDoc_id(vDocId);
		dashboardModel.setStartTime(startTime);
		dashboardModel.setEndTime(endTime);
		dashboardModel.setNextVisitDate(nextVisitDate);
		dashboardModel.setNextVisitTime(nextVisitTime);
		dashboardModel.setRemark(remark);
		dashboardModel.setDistId(distId);
		dashboardModel.setDitributorName(distName);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setItemQty(s);
		dashboardModel.setType(type);
		dashboardModel.setPicture(File);
		;
		return dashboardModel;
	}
	public DashboardModel discussionListDistWise(String androidId, String vDocId, String startTime, String endTime, String nextVisitDate, String nextVisitTime, String remark, String distId, String distName, String syncId, String vdate, String s, String type, String File)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setAndroidId(androidId);
		dashboardModel.setDoc_id(vDocId);
		dashboardModel.setStartTime(startTime);
		dashboardModel.setEndTime(endTime);
		dashboardModel.setNextVisitDate(nextVisitDate);
		dashboardModel.setNextVisitTime(nextVisitTime);
		dashboardModel.setRemark(remark);
		dashboardModel.setDistId(distId);
		dashboardModel.setDitributorName(distName);
		dashboardModel.setSyncId(syncId);
		dashboardModel.setVdate(vdate);
		dashboardModel.setItemQty(s);
		dashboardModel.setType(type);
		dashboardModel.setPicture(File);
		return dashboardModel;
	}

	public DashboardModel partyListModel(String vDate, String order, String demo, String failVisit, String collection, String competitor, String party, String add, String mb, String contactPerson)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(vDate);
		dashboardModel.setOrderModel(order);
		dashboardModel.setDemoModel(demo);
		dashboardModel.setFailedVisitModel(failVisit);
		dashboardModel.setCollectionModel(collection);
		dashboardModel.setCompetitorModel(competitor);
		dashboardModel.setPartyName(party);
		dashboardModel.setPartyAddress(add);
		dashboardModel.setContactPerson(contactPerson);
		dashboardModel.setMonbileNo(mb);


		return dashboardModel;
	}

	public DashboardModel demoListFind(String partyId, String vDate, String vDocId, String andId, String pClassId, String pSegmentId, String pmaterialGroup, String remark, String File, String type)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setPartyName(partyId);
		dashboardModel.setVdate(vDate);
		dashboardModel.setDoc_id(vDocId);
		dashboardModel.setAndroidId(andId);
		dashboardModel.setProdClass(pClassId);
		dashboardModel.setProdSegment(pSegmentId);
		dashboardModel.setProdGrp(pmaterialGroup);
		dashboardModel.setRemark(remark);
		dashboardModel.setPicture(File);
		//dashboardModel.setPartyName(party);
		dashboardModel.setType(type);

		return dashboardModel;
	}
	
	public DashboardModel
	OrderListFind(String vDate,String vDocId,String andId,String amount,String remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setVdate(vDate);
		dashboardModel.setDoc_id(vDocId);
		dashboardModel.setAndroidId(andId);
		dashboardModel.setAmount(amount);
		dashboardModel.setRemark(remark);
		return dashboardModel;
	}
	
	public DashboardModel orderDemoPartyListOnDateWiseList(String partyNameOnOrderMgmtDemoRow, String beatNameViewOnOrderMgmtDemoRow, String prodClass, String prodSegment, String prodGrp, String Remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setBeatNameViewOnOrderMgmtDemoRow(beatNameViewOnOrderMgmtDemoRow);
		dashboardModel.setPartyNameOnOrderMgmtDemoRow(partyNameOnOrderMgmtDemoRow);
		dashboardModel.setProdClass(prodClass);
		dashboardModel.setProdSegment(prodSegment);
		dashboardModel.setProdGrp(prodGrp);
		dashboardModel.setRemark(Remark);
		
		return dashboardModel;
	}
	public DashboardModel orderFailedVisitPartyListOnDateWiseList(String partyNameOnOrderMgmtFailedVisitRow, String beatNameViewOnOrderMgmtFailedVisitRow, String nextVisitDate, String nextVisitTime, String remark)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setBeatNameViewOnOrderMgmtFailedVisitRow(beatNameViewOnOrderMgmtFailedVisitRow);
		dashboardModel.setPartyNameOnOrderMgmtFailedVisitRow(partyNameOnOrderMgmtFailedVisitRow);
		dashboardModel.setRemark(remark);
		dashboardModel.setNextVisitDate(nextVisitDate);
		dashboardModel.setNextVisitTime(nextVisitTime);
		return dashboardModel;
	}
	public DashboardModel orderFailedVisitPartyListOnDateWiseList(String distNameOnOrderMgmtFailedVisitRow, String partyNameOnOrderMgmtFailedVisitRow, String beatNameViewOnOrderMgmtFailedVisitRow, String nextVisitDate, String nextVisitTime, String remark, String reason)
	{
		dashboardModel=new DashboardModel();
		dashboardModel.setBeatNameViewOnOrderMgmtFailedVisitRow(beatNameViewOnOrderMgmtFailedVisitRow);
		dashboardModel.setPartyNameOnOrderMgmtFailedVisitRow(partyNameOnOrderMgmtFailedVisitRow);
		dashboardModel.setDitributorName(distNameOnOrderMgmtFailedVisitRow);
		dashboardModel.setRemark(remark);
		dashboardModel.setNextVisitDate(nextVisitDate);
		dashboardModel.setNextVisitTime(nextVisitTime);
		dashboardModel.setReason(reason);
		return dashboardModel;
	}
	public DashboardModel(String Remarks)
	{
	this.Remarks=Remarks;
	}
	
	public DashboardModel(String dateOnOrderMgmt,String operationOnOrderMgmt,int rowOfOrderMgmt)
	{
		this.dateOnOrderMgmt=dateOnOrderMgmt;
		this.operationOnOrderMgmt=operationOnOrderMgmt;
		this.rowOfOrderMgmt=rowOfOrderMgmt;
	}
	public DashboardModel(String dashboardItemName,int imageResourceId)
	{
		this.dashboardItemName=dashboardItemName;
		this.imageResourceId=imageResourceId;
	}
	public DashboardModel(String partyName,String partyAddress,String partyVisit)
	{
		this.partyName=partyName;
		this.partyAddress=partyAddress;
		this.partyVisit=partyVisit;
	}
	public DashboardModel(String productName,double productPrice,double qty1,double qty2)
	{
		this.productName=productName;
		this.productPrice=productPrice;
		this.qty1=qty1;
		this.qty2=qty2;
	}
	
	public String getDashboardItemName() {
		return dashboardItemName;
	}

	public void setDashboardItemName(String dashboardItemName) {
		this.dashboardItemName = dashboardItemName;
	}

	public String getDashboardItemdescription() {
		return dashboardItemdescription;
	}

	public void setDashboardItemdescription(String dashboardItemdescription) {
		this.dashboardItemdescription = dashboardItemdescription;
	}

	public int getImageResourceId() {
		return imageResourceId;
	}

	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaId() {
		return this.areaId; 
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return this.city; 
	}
	public void setStateID(String stateid) {
		this.stateid = stateid;
	}
	public String getStateID() {
		return this.stateid;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactPerson() {
		return this.contactPerson; 
	}

	public String getClaimAmount() {return claimAmount;}

	public void setClaimAmount(String claimAmount) {this.claimAmount = claimAmount;}

	public String getBillNo() {return billNo;}

	public void setBillNo(String billNo) {this.billNo = billNo;}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getPartyId() {
		return this.partyId; 
	}
	public String getPartyAddress() {
		return partyAddress;
	}
	public void setPartyAddress(String partyAddress) {
		this.partyAddress = partyAddress;
	}
	public String getPartyVisit() {
		return partyVisit;
	}
	public void setPartyVisit(String partyVisit) {
		this.partyVisit = partyVisit;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public double getQty1() {
		return qty1;
	}
	public void setQty1(double qty1) {
		this.qty1 = qty1;
	}
	public double getQty2() {
		return qty2;
	}
	public void setQty2(double qty2) {
		this.qty2 = qty2;
	}
	/**
	 * @return the nameProductList
	 */
	public String getNameProductList() {
		return nameProductList;
	}
	/**
	 * @param nameProductList the nameProductList to set
	 */
	public void setNameProductList(String nameProductList) {
		this.nameProductList = nameProductList;
	}
	/**
	 * @return the priceProductList
	 */
	public double getPriceProductList() {
		return priceProductList;
	}
	/**
	 * @param priceProductList the priceProductList to set
	 */
	public void setPriceProductList(double priceProductList) {
		this.priceProductList = priceProductList;
	}
	/**
	 * @return the mpProductList
	 */
	public double getMpProductList() {
		return mpProductList;
	}
	/**
	 * @param mpProductList the mpProductList to set
	 */
	public void setMpProductList(double mpProductList) {
		this.mpProductList = mpProductList;
	}
	/**
	 * @return the dpProductList
	 */
	public double getDpProductList() {
		return dpProductList;
	}
	/**
	 * @param dpProductList the dpProductList to set
	 */
	public void setDpProductList(double dpProductList) {
		this.dpProductList = dpProductList;
	}
	/**
	 * @return the operationOnHistory
	 */
	public String getOperationOnHistory() {
		return operationOnHistory;
	}
	/**
	 * @param operationOnHistory the operationOnHistory to set
	 */
	public void setOperationOnHistory(String operationOnHistory) {
		this.operationOnHistory = operationOnHistory;
	}
	/**
	 * @return the dataOnHistroy
	 */
	public String getDateOnHistroy() {
		return dateOnHistroy;
	}
	/**
	 * @param dataOnHistroy the dataOnHistroy to set
	 */
	public void setDataOnHistroy(String dataOnHistroy) {
		this.dateOnHistroy = dataOnHistroy;
	}
	/**
	 * @return the dateOnOrderMgmt
	 */
	public String getDateOnOrderMgmt() {
		return dateOnOrderMgmt;
	}
	/**
	 * @param dateOnOrderMgmt the dateOnOrderMgmt to set
	 */
	public void setDateOnOrderMgmt(String dateOnOrderMgmt) {
		this.dateOnOrderMgmt = dateOnOrderMgmt;
	}
	/**
	 * @return the operationOnOrderMgmt
	 */
	public String getOperationOnOrderMgmt() {
		return operationOnOrderMgmt;
	}
	/**
	 * @param operationOnOrderMgmt the operationOnOrderMgmt to set
	 */
	public void setOperationOnOrderMgmt(String operationOnOrderMgmt) {
		this.operationOnOrderMgmt = operationOnOrderMgmt;
	}
	/**
	 * @return the rowOfOrderMgmt
	 */
	public int getRowOfOrderMgmt() {
		return rowOfOrderMgmt;
	}
	/**
	 * @param rowOfOrderMgmt the rowOfOrderMgmt to set
	 */
	public void setRowOfOrderMgmt(int rowOfOrderMgmt) {
		this.rowOfOrderMgmt = rowOfOrderMgmt;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return Remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	/**
	 * @return the orderModel
	 */
	public String getOrderModel() {
		return orderModel;
	}
	/**
	 * @param orderModel the orderModel to set
	 */
	public void setOrderModel(String orderModel) {
		this.orderModel = orderModel;
	}
	/**
	 * @return the demoModel
	 */
	public String getDemoModel() {
		return demoModel;
	}
	/**
	 * @param demoModel the demoModel to set
	 */
	public void setDemoModel(String demoModel) {
		this.demoModel = demoModel;
	}
	/**
	 * @return the failedVisitModel
	 */
	public String getDistfailedVisitModel() {
		return distfailedVisitModel;
	}
	/**
	 * @param distfailedVisitModel the failedVisitModel to set
	 */
	public void setDistFailedVisitModel(String distfailedVisitModel) {
		this.distfailedVisitModel = distfailedVisitModel;
	}

	public String getFailedVisitModel() {
		return failedVisitModel;
	}
	/**
	 * @param failedVisitModel the failedVisitModel to set
	 */
	public void setFailedVisitModel(String failedVisitModel) {
		this.failedVisitModel = failedVisitModel;
	}


	public String getCompetitorModel() {
		return CompetitorModel;
	}

	public void setCollectionModel(String CollectionModel) {
		this.CollectionModel = CollectionModel;
	}
	public String getCollectionModel() {
		return CollectionModel;
	}
	public void setDistCollModel(String DistCollModel) {
		this.DistCollModel = DistCollModel;
	}
	public String getDistCollModel() {
		return DistCollModel;
	}
	public void setDiscussionModel(String DiscussionModel) {
		this.DiscussionModel = DiscussionModel;
	}
	public String getDiscussionModel() {
		return DiscussionModel;
	}

	public void setCompetitorModel(String CompetitorModel) {
		this.CompetitorModel = CompetitorModel;
	}
	/**
	 * @return the partyNameOnOrderMgmtRow
	 */
	public String getPartyNameOnOrderMgmtRow() {
		return partyNameOnOrderMgmtRow;
	}
	/**
	 * @param partyNameOnOrderMgmtRow the partyNameOnOrderMgmtRow to set
	 */
	public void setPartyNameOnOrderMgmtRow(String partyNameOnOrderMgmtRow) {
		this.partyNameOnOrderMgmtRow = partyNameOnOrderMgmtRow;
	}
	/*
	  @return the mobileNoViewOnOrderMgmtRow
	 */
	public String getBeatNameViewOnOrderMgmtRow() {
		return beatNameViewOnOrderMgmtRow;
	}
	/*
	  @param mobileNoViewOnOrderMgmtRow the mobileNoViewOnOrderMgmtRow to set
	 */
	public void setBeatNameViewOnOrderMgmtRow(String beatNameViewOnOrderMgmtRow) {
		this.beatNameViewOnOrderMgmtRow = beatNameViewOnOrderMgmtRow;
	}
	/**
	 * @return the partyNameOnOrderMgmtDemoRow
	 */
	public String getPartyNameOnOrderMgmtDemoRow() {
		return partyNameOnOrderMgmtDemoRow;
	}
	/**
	 * @param partyNameOnOrderMgmtDemoRow the partyNameOnOrderMgmtDemoRow to set
	 */
	public void setPartyNameOnOrderMgmtDemoRow(
			String partyNameOnOrderMgmtDemoRow) {
		this.partyNameOnOrderMgmtDemoRow = partyNameOnOrderMgmtDemoRow;
	}
	/**
	 * @return the beatNameViewOnOrderMgmtDemoRow
	 */
	public String getBeatNameViewOnOrderMgmtDemoRow() {
		return beatNameViewOnOrderMgmtDemoRow;
	}
	/**
	 * @param beatNameViewOnOrderMgmtDemoRow the beatNameViewOnOrderMgmtDemoRow to set
	 */
	public void setBeatNameViewOnOrderMgmtDemoRow(
			String beatNameViewOnOrderMgmtDemoRow) {
		this.beatNameViewOnOrderMgmtDemoRow = beatNameViewOnOrderMgmtDemoRow;
	}
	/**
	 * @return the partyNameOnOrderMgmtFailedVisitRow
	 */
	public String getPartyNameOnOrderMgmtFailedVisitRow() {
		return partyNameOnOrderMgmtFailedVisitRow;
	}
	/**
	 * @param partyNameOnOrderMgmtFailedVisitRow the partyNameOnOrderMgmtFailedVisitRow to set
	 */
	public void setPartyNameOnOrderMgmtFailedVisitRow(
			String partyNameOnOrderMgmtFailedVisitRow) {
		this.partyNameOnOrderMgmtFailedVisitRow = partyNameOnOrderMgmtFailedVisitRow;
	}
	/**
	 * @return the beatNameViewOnOrderMgmtFailedVisitRow
	 */
	public String getBeatNameViewOnOrderMgmtFailedVisitRow() {
		return beatNameViewOnOrderMgmtFailedVisitRow;
	}
	/**
	 * @param beatNameViewOnOrderMgmtFailedVisitRow the beatNameViewOnOrderMgmtFailedVisitRow to set
	 */
	public void setBeatNameViewOnOrderMgmtFailedVisitRow(
			String beatNameViewOnOrderMgmtFailedVisitRow) {
		this.beatNameViewOnOrderMgmtFailedVisitRow = beatNameViewOnOrderMgmtFailedVisitRow;
	}
	/**
	 * @return the productNameOnOrderMgmt
	 */
	public String getProductNameOnOrderMgmt() {
		return productNameOnOrderMgmt;
	}
	/**
	 * @param productNameOnOrderMgmt the productNameOnOrderMgmt to set
	 */
	public void setProductNameOnOrderMgmt(String productNameOnOrderMgmt) {
		this.productNameOnOrderMgmt = productNameOnOrderMgmt;
	}
	/**
	 * @return the priceProductOnOrderMgmt
	 */
	public double getPriceProductOnOrderMgmt() {
		return priceProductOnOrderMgmt;
	}
	/**
	 * @param priceProductOnOrderMgmt the priceProductOnOrderMgmt to set
	 */
	public void setPriceProductOnOrderMgmt(double priceProductOnOrderMgmt) {
		this.priceProductOnOrderMgmt = priceProductOnOrderMgmt;
	}
	/**
	 * @return the qtyProductOnOrderMgmt
	 */
	public double getQtyProductOnOrderMgmt() {
		return qtyProductOnOrderMgmt;
	}
	/**
	 * @param qtyProductOnOrderMgmt the qtyProductOnOrderMgmt to set
	 */
	public void setQtyProductOnOrderMgmt(double qtyProductOnOrderMgmt) {
		this.qtyProductOnOrderMgmt = qtyProductOnOrderMgmt;
	}
	/**
	 * @return the productNameOnDemoOrderMgmt
	 */
	public String getProductNameOnDemoOrderMgmt() {
		return productNameOnDemoOrderMgmt;
	}
	/**
	 * @param productNameOnDemoOrderMgmt the productNameOnDemoOrderMgmt to set
	 */
	public void setProductNameOnDemoOrderMgmt(String productNameOnDemoOrderMgmt) {
		this.productNameOnDemoOrderMgmt = productNameOnDemoOrderMgmt;
	}
	/**
	 * @return the priceProductOnDemoOrderMgmt
	 */
	public String getPriceProductOnDemoOrderMgmt() {
		return priceProductOnDemoOrderMgmt;
	}
	/**
	 * @param priceProductOnDemoOrderMgmt the priceProductOnDemoOrderMgmt to set
	 */
	public void setPriceProductOnDemoOrderMgmt(
			String priceProductOnDemoOrderMgmt) {
		this.priceProductOnDemoOrderMgmt = priceProductOnDemoOrderMgmt;
	}
	/**
	 * @return the qtyProductOnDemoOrderMgmt
	 */
	public String getQtyProductOnDemoOrderMgmt() {
		return qtyProductOnDemoOrderMgmt;
	}
	/**
	 * @param qtyProductOnDemoOrderMgmt the qtyProductOnDemoOrderMgmt to set
	 */
	public void setQtyProductOnDemoOrderMgmt(String qtyProductOnDemoOrderMgmt) {
		this.qtyProductOnDemoOrderMgmt = qtyProductOnDemoOrderMgmt;
	}
	/**
	 * @return the remarkFailedVisitOnOrderMgmt
	 */
	public String getRemarkFailedVisitOnOrderMgmt() {
		return remarkFailedVisitOnOrderMgmt;
	}
	/**
	 * @param remarkFailedVisitOnOrderMgmt the remarkFailedVisitOnOrderMgmt to set
	 */
	public void setRemarkFailedVisitOnOrderMgmt(
			String remarkFailedVisitOnOrderMgmt) {
		this.remarkFailedVisitOnOrderMgmt = remarkFailedVisitOnOrderMgmt;
	}
	/**
	 * @return the productNameOnDemoRetailer
	 */
	public String getProductNameOnDemoRetailer() {
		return productNameOnDemoRetailer;
	}
	/**
	 * @param productNameOnDemoRetailer the productNameOnDemoRetailer to set
	 */
	public void setProductNameOnDemoRetailer(String productNameOnDemoRetailer) {
		this.productNameOnDemoRetailer = productNameOnDemoRetailer;
	}
	/**
	 * @return the priceProductOnDemoRetailer
	 */
	public double getPriceProductOnDemoRetailer() {
		return priceProductOnDemoRetailer;
	}
	/**
	 * @param priceProductOnDemoRetailer the priceProductOnDemoRetailer to set
	 */
	public void setPriceProductOnDemoRetailer(double priceProductOnDemoRetailer) {
		this.priceProductOnDemoRetailer = priceProductOnDemoRetailer;
	}
	/**
	 * @return the mpProductOnDemoRetailer
	 */
	public double getMpProductOnDemoRetailer() {
		return mpProductOnDemoRetailer;
	}
	/**
	 * @param mpProductOnDemoRetailer the mpProductOnDemoRetailer to set
	 */
	public void setMpProductOnDemoRetailer(double mpProductOnDemoRetailer) {
		this.mpProductOnDemoRetailer = mpProductOnDemoRetailer;
	}
	/**
	 * @return the dpProductOnDemoRetailer
	 */
	public double getDpProductOnDemoRetailer() {
		return dpProductOnDemoRetailer;
	}
	/**
	 * @param dpProductOnDemoRetailer the dpProductOnDemoRetailer to set
	 */
	public void setDpProductOnDemoRetailer(double dpProductOnDemoRetailer) {
		this.dpProductOnDemoRetailer = dpProductOnDemoRetailer;
	}
	/**
	 * @return the productNameOnPruchaseOrder
	 */
	public String getProductNameOnPruchaseOrder() {
		return productNameOnPruchaseOrder;
	}
	/**
	 * @param productNameOnPruchaseOrder the productNameOnPruchaseOrder to set
	 */
	public void setProductNameOnPruchaseOrder(String productNameOnPruchaseOrder) {
		this.productNameOnPruchaseOrder = productNameOnPruchaseOrder;
	}
	/**
	 * @return the priceViewOnPruchaseOrder
	 */
	public double getPriceViewOnPruchaseOrder() {
		return priceViewOnPruchaseOrder;
	}
	/**
	 * @param priceViewOnPruchaseOrder the priceViewOnPruchaseOrder to set
	 */
	public void setPriceViewOnPruchaseOrder(double priceViewOnPruchaseOrder) {
		this.priceViewOnPruchaseOrder = priceViewOnPruchaseOrder;
	}
	/**
	 * @return the quantityOnPruchaseOrder
	 */
	public double getQuantityOnPruchaseOrder() {
		return quantityOnPruchaseOrder;
	}
	/**
	 * @param quantityOnPruchaseOrder the quantityOnPruchaseOrder to set
	 */
	public void setQuantityOnPruchaseOrder(double quantityOnPruchaseOrder) {
		this.quantityOnPruchaseOrder = quantityOnPruchaseOrder;
	}
	/**
	 * @return the totalOrderValueOnOrderProductList
	 */
	public double getTotalOrderValueOnOrderProductList() {
		return totalOrderValueOnOrderProductList;
	}
	/**
	 * @param totalOrderValueOnOrderProductList the totalOrderValueOnOrderProductList to set
	 */
	public void setTotalOrderValueOnOrderProductList(
			double totalOrderValueOnOrderProductList) {
		this.totalOrderValueOnOrderProductList = totalOrderValueOnOrderProductList;
	}
	/**
	 * @return the qtyType
	 */
	public String getSamqty() {
		return samqty;
	}
	/*
	  @param qtyType the qtyType to set
	 */
	public void setSamqty(String samqty) {
		this.samqty = samqty;
	}
	/**
	 * @return the qtyOnDemoRetailer
	 */
	public double getQtyOnDemoRetailer() {
		return qtyOnDemoRetailer;
	}
	/**
	 * @param qtyOnDemoRetailer the qtyOnDemoRetailer to set
	 */
	public void setQtyOnDemoRetailer(double qtyOnDemoRetailer) {
		this.qtyOnDemoRetailer = qtyOnDemoRetailer;
	}
	/**
	 * @return the sampleQtyOnDemoRetailer
	 */
	public double getSampleQtyOnDemoRetailer() {
		return sampleQtyOnDemoRetailer;
	}
	/**
	 * @param sampleQtyOnDemoRetailer the sampleQtyOnDemoRetailer to set
	 */
	public void setSampleQtyOnDemoRetailer(double sampleQtyOnDemoRetailer) {
		this.sampleQtyOnDemoRetailer = sampleQtyOnDemoRetailer;
	}
	/**
	 * @return the rpProductList
	 */
	public double getRpProductList() {
		return rpProductList;
	}
	/**
	 * @param rpProductList the rpProductList to set
	 */
	public void setRpProductList(double rpProductList) {
		this.rpProductList = rpProductList;
	}
	/**
	 * @return the priceProduct
	 */
	public double getPriceProduct() {
		return priceProduct;
	}
	/**
	 * @param priceProduct the priceProduct to set
	 */
	public void setPriceProduct(double priceProduct) {
		this.priceProduct = priceProduct;
	}
	/**
	 * @return the qtyInCatron
	 */
	public double getQtyInCatron() {
		return qtyInCatron;
	}
	/**
	 * @param qtyInCatron the qtyInCatron to set
	 */
	public void setQtyInCatron(double qtyInCatron) {
		this.qtyInCatron = qtyInCatron;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the quantityOfProduct
	 */
	public String getQuantityOfProduct() {
		return quantityOfProduct;
	}
	/**
	 * @param quantityOfProduct the quantityOfProduct to set
	 */
	public void setQuantityOfProduct(String quantityOfProduct) {
		this.quantityOfProduct = quantityOfProduct;
	}
	/**
	 * @return the qtyInSample
	 */
	public String getQtyInSample() {
		return qtyInSample;
	}
	/**
	 * @param qtyInSample the qtyInSample to set
	 */
	public void setQtyInSample(String qtyInSample) {
		this.qtyInSample = qtyInSample;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
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
	 * @return the itemName
	 */
	
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getAndroidId() {
		return androidId;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	/*
	  @param itemName the itemName to set
	 */
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getUserId() {
		return userId;
	}
	public String getOtherActivity() {
		return this.otherActivity;
	}
	
	public void setOtherActivity(String otherActivity) {
		this.otherActivity = otherActivity;
	}
	
	public String getDiscount() {
		return this.discount;
	}
	
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	/*
	  @param itemName the itemName to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVdate() {
		return vdate;
	}

	/*
	  @param itemName the itemName to set
	 */
	public void setVdate(String vdate) {
		this.vdate = vdate;
	}
	public String getFromDate() {
		return fromDate;
	}


	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getToDate() {
		return toDate;
	}


	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getNoOfDays() {
		return noOfDays;
	}

	/*
	  @param itemName the itemName to set
	 */
	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}
	
	public String getReason() {
		return reason;
	}

	/*
	  @param itemName the itemName to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getStatus() {
		return status;
	}

	/*
	  @param itemName the itemName to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	/**
	 * @return the itemPrice
	 */
	public String getItemPrice() {
		return itemPrice;
	}

	/**
	 * @param itemPrice the itemPrice to set
	 */
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	/**
	 * @return the itemQty
	 */
	public String getItemQty() {
		return itemQty;
	}

	/**
	 * @param itemQty the itemQty to set
	 */
	public void setItemQty(String itemQty) {
		this.itemQty = itemQty;
	}
	/**
	 * @return the sampleQty
	 */
	public String getSampleQty() {
		return sampleQty;
	}
	/**
	 * @param sampleQty the sampleQty to set
	 */
	public void setSampleQty(String sampleQty) {
		this.sampleQty = sampleQty;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getPicture() {
		return Picture;
	}
	public void setPicture(String Picture) {
		this.Picture = Picture;
	}

	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getDropDownId() {
		return DropDownId;
	}
	/**
	 * @param dropDownId the color to set
	 */
	public void setDropDownId(String dropDownId) {
		DropDownId=dropDownId;
	}
	/**
	 * @return the nextVisitDate
	 */
	public String getNextVisitDate() {
		return nextVisitDate;
	}

	public void setNextVisitTime(String nextVisitTime) {
		this.nextVisitTime = nextVisitTime;
	}
	public String getNextVisitTime() {
		return nextVisitTime;
	}
	/**
	 * @param nextVisitDate the nextVisitDate to set
	 */
	public void setNextVisitDate(String nextVisitDate) {
		this.nextVisitDate = nextVisitDate;
	}
	/**
	 * @return the remarkOnDemo
	 */
	public String getRemarkOnDemo() {
		return remarkOnDemo;
	}
	/**
	 * @param remarkOnDemo the remarkOnDemo to set
	 */
	public void setRemarkOnDemo(String remarkOnDemo) {
		this.remarkOnDemo = remarkOnDemo;
	}
	/**
	 * @return the nextVisitDateOnOrderMgmt
	 */
	public String getNextVisitDateOnOrderMgmt() {
		return nextVisitDateOnOrderMgmt;
	}
	/**
	 * @param nextVisitDateOnOrderMgmt the nextVisitDateOnOrderMgmt to set
	 */
	public void setNextVisitDateOnOrderMgmt(String nextVisitDateOnOrderMgmt) {
		this.nextVisitDateOnOrderMgmt = nextVisitDateOnOrderMgmt;
	}
	/**
	 * @return the cartonqtyProduct
	 */
	public String getCartonqtyProduct() {
		return cartonqtyProduct;
	}
	/**
	 * @param cartonqtyProduct the cartonqtyProduct to set
	 */
	public void setCartonqtyProduct(String cartonqtyProduct) {
		this.cartonqtyProduct = cartonqtyProduct;
	}
	/**
	 * @return the unitOnPruchaseOrder
	 */
	public String getUnitOnPruchaseOrder() {
		return unitOnPruchaseOrder;
	}
	/**
	 * @param unitOnPruchaseOrder the unitOnPruchaseOrder to set
	 */
	public void setUnitOnPruchaseOrder(String unitOnPruchaseOrder) {
		this.unitOnPruchaseOrder = unitOnPruchaseOrder;
	}
	/**
	 * @return the dateOfDate
	 */
	public String getDateOfDate() {
		return dateOfDate;
	}
	/**
	 * @param dateOfDate the dateOfDate to set
	 */
	public void setDateOfDate(String dateOfDate) {
		this.dateOfDate = dateOfDate;
	}
	/**
	 * @return the ditributorName
	 */
	public String getDitributorName() {
		return ditributorName;
	}
	/**
	 * @param ditributorName the ditributorName to set
	 */
	public void setDitributorName(String ditributorName) {
		this.ditributorName = ditributorName;
	}
	
	public String getDistId() {
		return distId;
	}

	public void setDistId(String distId) {
		this.distId = distId;
	}
	/**
	 * @return the dsrLockDate
	 */
	public String getDsrLockDate() {
		return dsrLockDate;
	}
	/**
	 * @param dsrLockDate the dsrLockDate to set
	 */
	public void setDsrLockDate(String dsrLockDate) {
		this.dsrLockDate = dsrLockDate;
	}
	/**
	 * @return the partyNameOnReportAbove
	 */
	public String getPartyNameOnReportAbove() {
		return partyNameOnReportAbove;
	}
	/**
	 * @param partyNameOnReportAbove the partyNameOnReportAbove to set
	 */
	public void setPartyNameOnReportAbove(String partyNameOnReportAbove) {
		this.partyNameOnReportAbove = partyNameOnReportAbove;
	}
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
	 * @return the monbileNo
	 */
	public String getMonbileNo() {
		return monbileNo;
	}
	/**
	 * @param monbileNo the monbileNo to set
	 */
	public void setMonbileNo(String monbileNo) {
		this.monbileNo = monbileNo;
	}
	/**
	 * @return the monthfirst
	 */
	public String getMonthfirst() {
		return monthfirst;
	}
	/**
	 * @param monthfirst the monthfirst to set
	 */
	public void setMonthfirst(String monthfirst) {
		this.monthfirst = monthfirst;
	}
	/**
	 * @return the monthsecond
	 */
	public String getMonthsecond() {
		return monthsecond;
	}
	/**
	 * @param monthsecond the monthsecond to set
	 */
	public void setMonthsecond(String monthsecond) {
		this.monthsecond = monthsecond;
	}
	/**
	 * @return the monththird
	 */
	public String getMonththird() {
		return monththird;
	}
	/**
	 * @param monththird the monththird to set
	 */
	public void setMonththird(String monththird) {
		this.monththird = monththird;
	}
	/**
	 * @return the monthfirstvalue
	 */
	public String getMonthfirstvalue() {
		return monthfirstvalue;
	}
	/**
	 * @param monthfirstvalue the monthfirstvalue to set
	 */
	public void setMonthfirstvalue(String monthfirstvalue) {
		this.monthfirstvalue = monthfirstvalue;
	}
	/**
	 * @return the monthsecondvalue
	 */
	public String getMonthsecondvalue() {
		return monthsecondvalue;
	}
	/**
	 * @param monthsecondvalue the monthsecondvalue to set
	 */
	public void setMonthsecondvalue(String monthsecondvalue) {
		this.monthsecondvalue = monthsecondvalue;
	}
	/**
	 * @return the monththirdvalue
	 */
	public String getMonththirdvalue() {
		return monththirdvalue;
	}
	/**
	 * @param monththirdvalue the monththirdvalue to set
	 */
	public void setMonththirdvalue(String monththirdvalue) {
		this.monththirdvalue = monththirdvalue;
	}
	/**
	 * @return the partyname
	 */
	public String getPartyname() {
		return partyname;
	}
	/**
	 * @param partyname the partyname to set
	 */
	public void setPartyname(String partyname) {
		this.partyname = partyname;
	}
	/**
	 * @return the unitOrProduct
	 */
	public String getUnitOrProduct() {
		return unitOrProduct;
	}
	/**
	 * @param unitOrProduct the unitOrProduct to set
	 */
	public void setUnitOrProduct(String unitOrProduct) {
		this.unitOrProduct = unitOrProduct;
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
	
	public String getMode() {
		return mode;
	}
	/*
	  @param amount the amount to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getBank() {
		return bank;
	}
	/*
	  @param amount the amount to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}	
	public String getBranch() {
		return branch;
	}
	/*
	  @param amount the amount to set
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}	
	public String getChqdate() {
		return chqdate;
	}
	/*
	  @param amount the amount to set
	 */
	public void setChqdate(String chqdate) {
		this.chqdate = chqdate;
	}
	
	public String getChqno() {
		return chqno;
	}
	

	
	public String getSyncId() {
		return this.SyncId;
	}

	public void setSyncId(String SyncId) {
	this.SyncId = SyncId;
	}

	public void setActive(String active) {
		this.active = active;
	}
	public String getActive() {
		return this.active;
	}

	/*
	  @param amount the amount to set
	 */
	public void setChqno(String chqno) {
		this.chqno = chqno;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getCases() {
		return cases;
	}
	
	/*
	 @return the color
	 */
	public String getColor() {
		return color;
	}
	/*
	  @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the rate
	 */
	public String getRate() {
		return Rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		Rate = rate;
	}
	public String getPotential() {
		return potential;
	}
	public void setPotential(String potential) {
		this.potential = potential;
	}
	public String getProdClass() {
		return ProdClass;
	}
	public void setProdClass(String ProdClass) {
		this.ProdClass = ProdClass;
	}
	public String getProdSegment() {
		return ProdSegment;
	}
	public void setProdSegment(String ProdSegment) {
		this.ProdSegment = ProdSegment;
	}
	
	public String getProdGrp() {
		return ProdGrp;
	}
	public void setProdGrp(String ProdGrp) {
		this.ProdGrp = ProdGrp;
	}
	
	
}
