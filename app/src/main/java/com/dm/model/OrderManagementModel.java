package com.dm.model;

/**
 * Created by dataman on 18-Apr-17.
 */
public class OrderManagementModel {

    public String visit_date;
    public String srep_code;
    public String citynames;
    public String orderbyemail;
    public String orderbyphone;
    public String remark;
    public String dsrtype;
    public String dfv;
    public String Area;
    public String Beat;

    public void setArea(String Area) {
        this.Area = Area;
    }
    public String getArea() {
        return Area;
    }

    public void setBeat(String Beat) {
        this.Beat = Beat;
    }
    public String getBeat() {
        return Beat;
    }

    public void setVisitDate(String visit_date) {
        this.visit_date = visit_date;
    }
    public String getVisitDate() {
        return visit_date;
    }


    public void setSrepCode(String srep_code) {
        this.srep_code = srep_code;
    }
    public String getSrepCode() {
        return this.srep_code;
    }

    public String getCityName() {
        return this.citynames;
    }
    public void setCityName(String citynames) {
        this.citynames = citynames;
    }

    public void setEmail(String orderbyemail) {
        this.orderbyemail = orderbyemail;
    }
    public String getEmail() {
        return this.orderbyemail;
    }

    public void setPhone(String orderbyphone) {
        this.orderbyphone = orderbyphone;
    }
    public String getPhone() {
        return this.orderbyphone;
    }

    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDsrType(String dsrtype) {
        this.dsrtype = dsrtype;
    }
    public String getDsrType() {
        return this.dsrtype;
    }

    public String getDistFailedVisit() {
        return this.dfv;
    }
    public void setDistFailedVisit(String dfv) {
        this.dsrtype = dfv;
    }
}
