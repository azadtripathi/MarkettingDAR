package com.dm.model;

/**
 * Created by Dataman on 3/17/2017.
 */
public class TourPlan {
    private String  Code;
    private String  TourPlanId;
    private String DocId;
    private String VDate;
    private String UserID;
    private String AppStatus;
    private String AppBy;
    private String AppRemark;
    private String SMId;
    private String AppBySMId;
    private String TourPlanHId;
    private String Remarks;
    private String MCityID;
    private String MCityName;
    private String MPurposeId;
    private String MPurposeName;
    private String MDistId;
    private String MDistName;
    private String isUpload;
    private String FromDate;
    private String ToDate;
    private String CreatedDate;

    public String getCreatedDate() {
        return this.CreatedDate;
    }
    public String getToDate() {
        return this.ToDate;
    }
    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }
    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    public String getFromDate() {
        return this.FromDate;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getTourPlanId() {
        return this.TourPlanId;
    }

    public void setTourPlanId(String TourPlanId) {
        this.TourPlanId = TourPlanId;
    }

    public String getDocId() {
        return this.DocId;
    }

    public void setDocId(String DocId) {
        this.DocId = DocId;
    }

    public String getVDate() {
        return this.VDate;
    }

    public void setVDate(String VDate) {
        this.VDate = VDate;
    }

    public String getUserID() {
        return this.UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getAppStatus() {
        return this.AppStatus;
    }

    public void setAppStatus(String AppStatus) {
        this.AppStatus = AppStatus;
    }


    public String getAppBy() {
        return this.AppBy;
    }

    public void setAppBy(String AppBy) {
        this.AppBy = AppBy;
    }

    public String getAppRemark() {
        return this.AppRemark;
    }

    public void setAppRemark(String AppRemark) {
        this.AppRemark = AppRemark;
    }

    public String getSMId() {
        return this.SMId;
    }

    public void setSMId(String SMId) {
        this.SMId = SMId;
    }

    public String getAppBySMId() {
        return this.AppBySMId;
    }

    public void setAppBySMId(String AppBySMId) {
        this.AppBySMId = AppBySMId;
    }

    public String getTourPlanHId() {
        return this.TourPlanHId;
    }

    public void setTourPlanHId(String TourPlanHId) {
        this.TourPlanHId = TourPlanHId;
    }

    public String getRemarks() {
        return this.Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getMCityID() {
        return this.MCityID;
    }

    public void setMCityID(String MCityID) {
        this.MCityID = MCityID;
    }

    public String getMCityName() {
        return this.MCityName;
    }

    public void setMCityName(String MCityName) {
        this.MCityName = MCityName;
    }

    public String getMPurposeId() {
        return this.MPurposeId;
    }

    public void setMPurposeId(String MPurposeId) {
        this.MPurposeId = MPurposeId;
    }

    public String getMPurposeName() {
        return this.MPurposeName;
    }

    public void setMPurposeName(String MPurposeName) {
        this.MPurposeName = MPurposeName;
    }

    public String getMDistId() {
        return this.MDistId;
    }

    public void setMDistId(String MDistId) {
        this.MDistId = MDistId;
    }
    public String getMDistName() {
        return this.MDistName;
    }

    public void setMDistNamed(String MDistName) {
        this.MDistName = MDistName;
    }

    public String getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }


}
