package com.dm.controller;

/**
 * Created by dataman on 1/4/2018.
 */

public class PaymentLadgerController {
    private String id;
    private String date;
    private String particuler;
    private String amount;
    private String remark;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setParticuler(String particuler) {
        this.particuler = particuler;
    }

    public String getParticuler() {
        return particuler;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
