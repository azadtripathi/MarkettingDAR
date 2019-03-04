package com.dm.model;

/**
 * Created by Dataman on 11/4/2017.
 */

public class MilesStonesData
{
    String milestone,rate,qty,amount;

    public String getAmount() {
        return amount;
    }

    public String getMilestone() {
        return milestone;
    }

    public String getQty() {
        return qty;
    }

    public String getRate() {
        return rate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
