package com.dm.model;

/**
 * Created by Dataman on 6/22/2017.
 */
public class WriteLogData
{
    String method, smId,createdDate,params,response;

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public String getSmId() {
        return smId;
    }
}
