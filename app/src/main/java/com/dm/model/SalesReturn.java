package com.dm.model;

/**
 * Created by Dataman on 8/25/2017.
 */

public class SalesReturn
{

    String salesDocId,
            Smid,
            userId,
            vDate,
            partyId,
            AreaId,
            longitude,
            latitude,
            lat_long_dt,
            visit_no,
            androidId,
            androidId1,
            andPartyCode,
            salesRetNo,
            cases,
            unit,
            bachNo,
            mfdDate;


    public void setSalesDocId(String salesDocId)
    {
        this.salesDocId = salesDocId;
    }

    public String getSalesDocId() {
        return salesDocId;
    }

    public void setSalesRetNo(String salesRetNo) {
        this.salesRetNo = salesRetNo;
    }

    public String getSalesRetNo() {
        return salesRetNo;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId1(String androidId1) {
        this.androidId1 = androidId1;
    }

    public String getAndroidId1() {
        return androidId1;
    }


    public void setVisit_no(String visit_no) {
        this.visit_no = visit_no;
    }

    public String getVisit_no() {
        return visit_no;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public void setLat_long_dt(String lat_long_dt) {
        this.lat_long_dt = lat_long_dt;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public void setSmid(String smid) {
        Smid = smid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return AreaId;
    }

    public String getLat_long_dt() {
        return lat_long_dt;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getSmid() {
        return Smid;
    }

    public String getUserId() {
        return userId;
    }

    public String getvDate() {
        return vDate;
    }

    public void setAndPartyCode(String andPartyCode) {
        this.andPartyCode = andPartyCode;
    }

    public String getAndPartyCode() {
        return andPartyCode;
    }
}
