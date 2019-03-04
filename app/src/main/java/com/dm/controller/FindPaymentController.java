package com.dm.controller;

/**
 * Created by dataman on 1/5/2018.
 */

public class FindPaymentController {
    private String Id;
    private String fromDate;
    private String toDate;
    private String Party;

    public void setParty(String party) {
        Party = party;
    }

    public String getParty() {
        return Party;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }
}
