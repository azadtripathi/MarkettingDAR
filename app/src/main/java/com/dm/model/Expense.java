package com.dm.model;

/**
 * Created by Dataman on 1/24/2017.
 */
public class Expense {

    private String id;
    private String name;
    private String fromDate;
    private String toDate;
    private String remark;
    private String Expcd;

    public String getExpcd() {
        return Expcd;
    }

    public void setExpcd(String Expcd) {
        this.Expcd = Expcd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
