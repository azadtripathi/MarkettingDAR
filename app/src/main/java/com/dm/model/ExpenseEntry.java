package com.dm.model;

/**
 * Created by Dataman on 1/27/2017.
 */
public class ExpenseEntry {

    private String ExpCode;
    private String ExpDetailId;
    private String ExpenseTypeId;
    private String BillNumber;
    private String BillDate;
    private String FromCity;
    private String ToCity;
    private String CityId;
    private String FromDate;
    private String ToDate;
    private String Remarks;
    private String ClaimAmount;
    private String BillAmount;
    private boolean IsSupportingAttached;
    private String TravelModeId;
    private String ExpenseGrpId;
    private boolean StayWithRelative;
    private String TimeFrom;
    private String TimeTo;
    private String stateid;
    private String fromstate;
    private String tostate;
    private String kmVisited;
    private String rate;
    private String gstNo,partyVendorName, sGSTAmt,cGSTAmt;
    boolean isGSTReq;
    String igstAmount;

    public void setIgstAmount(String igstAmount) {
        this.igstAmount = igstAmount;
    }

    public String getIgstAmount() {
        return igstAmount;
    }

    public void setGSTReq(boolean GSTReq) {
        isGSTReq = GSTReq;
    }

    public boolean isGSTReq() {
        return isGSTReq;
    }

    public String getPartyVendorName() {
        return partyVendorName;
    }

    public void setPartyVendorName(String partyVendorName) {
        this.partyVendorName = partyVendorName;
    }

    public void setcGSTAmt(String cGSTAmt) {
        this.cGSTAmt = cGSTAmt;
    }

    public String getcGSTAmt() {
        return cGSTAmt;
    }

    public void setsGSTAmt(String sGSTAmt) {
        this.sGSTAmt = sGSTAmt;
    }

    public String getsGSTAmt() {
        return sGSTAmt;
    }

    public String getGstNo() {
        return gstNo;
    }


    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setKmVisited(String kmVisited) {
        this.kmVisited = kmVisited;
    }

    public String getKmVisited() {
        return kmVisited;
    }
    public void setTostate(String tostate) {
        this.tostate = tostate;
    }

    public String getTostate() {
        return tostate;
    }
    public void setFromstate(String fromstate) {
        this.fromstate = fromstate;
    }

    public String getFromstate() {
        return fromstate;
    }
    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setExpCode(String ExpCode) {
        this.ExpCode = ExpCode;
    }

    public String getExpCode() {
        return ExpCode;
    }

    public void setExpDetailId(String ExpDetailId) {
        this.ExpDetailId = ExpDetailId;
    }

    public String getExpDetailId() {
        return ExpDetailId;
    }

    public void setExpenseTypeId(String ExpenseTypeId) {
        this.ExpenseTypeId = ExpenseTypeId;
    }

    public String getExpenseTypeId() {
        return ExpenseTypeId;
    }

    public void setBillNumber(String BillNumber) {
        this.BillNumber = BillNumber;
    }

    public String getBillNumber() {
        return BillNumber;
    }


    public void setBillDate(String BillDate) {
        this.BillDate = BillDate;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setFromCity(String FromCity) {
        this.FromCity = FromCity;
    }

    public String getFromCity() {
        return FromCity;
    }

    public void setToCity(String ToCity) {
        this.ToCity = ToCity;
    }

    public String getToCity() {
        return ToCity;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setClaimAmount(String ClaimAmount) {
        this.ClaimAmount = ClaimAmount;
    }

    public String getClaimAmount() {
        return ClaimAmount;
    }

    public void setBillAmount(String BillAmount) {
        this.BillAmount = BillAmount;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setIsSupportingAttached(boolean IsSupportingAttached) {
        this.IsSupportingAttached = IsSupportingAttached;
    }

    public boolean getIsSupportingAttached() {
        return IsSupportingAttached;
    }

    public void setTravelModeId(String TravelModeId) {
        this.TravelModeId = TravelModeId;
    }

    public String getTravelModeId() {
        return TravelModeId;
    }

    public void setExpenseGrpId(String ExpenseGrpId) {
        this.ExpenseGrpId = ExpenseGrpId;
    }

    public String getExpenseGrpId() {
        return ExpenseGrpId;
    }


    public void setStayWithRelative(boolean StayWithRelative) {
        this.StayWithRelative = StayWithRelative;
    }

    public boolean getStayWithRelative() {
        return StayWithRelative;
    }

    public void setTimeFrom(String TimeFrom) {
        this.TimeFrom = TimeFrom;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public void setTimeTo(String TimeTo) {
        this.TimeTo = TimeTo;
    }

    public String getTimeTo() {
        return TimeTo;
    }


}
