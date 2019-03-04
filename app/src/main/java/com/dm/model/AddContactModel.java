package com.dm.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 6/13/2017.
 */
public class AddContactModel {
    String flag;
    private String firstName;
    String lastName;
    String jobTitle;
    String comapy;
    String email;
    String phone;
    String url;
    String address;
    String status;
    String taskDiscription;
    String date;
    String Lead;
    String tag;
    String owner;
    String leadStatus;
    String taskDate;
    String ID;
    String contact_id;
    String dynamicControlName;
    String dynmicSpinerValue;
    String dynamicControlType;
    //Add Contact Data
    String companyContry;
    String companyState;
    String companyZip;
    String companyPhone;
    String companyDisscussion;
    JSONObject dynamicControlData;
    JSONArray jsonPhone;
    JSONArray jsonEmail;
    JSONArray jsonURL;
    String companyCity;
    String city;
    String state;
    String zip;
    String contry;
    String background;
    String companyAddress;
    boolean active;
    String imageUrl;
    int istoday;
    String producGroup;
    String ItemName;
    String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setProducGroup(String producGroup) {
        this.producGroup = producGroup;
    }

    public String getProducGroup() {
        return producGroup;
    }

    public void setIstoday(int istoday) {
        this.istoday = istoday;
    }

    public int getIstoday() {
        return istoday;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyZip(String companyZip) {
        this.companyZip = companyZip;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyZip() {
        return companyZip;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getCompanyDisscussion() {
        return companyDisscussion;
    }

    public void setCompanyDisscussion(String companyDisscussion) {
        this.companyDisscussion = companyDisscussion;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyContry(String companyContry) {
        this.companyContry = companyContry;
    }

    public String getCompanyContry() {
        return companyContry;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String backgroung) {
        this.background = backgroung;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getContry() {
        return contry;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setJsonURL(JSONArray jsonURL) {
        this.jsonURL = jsonURL;
    }

    public JSONArray getJsonURL() {
        return jsonURL;
    }

    public void setJsonEmail(JSONArray jsonEmail) {
        this.jsonEmail = jsonEmail;
    }

    public JSONArray getJsonEmail() {
        return jsonEmail;
    }

    public void setJsonPhone(JSONArray jsonPhone) {
        this.jsonPhone = jsonPhone;
    }

    public JSONArray getJsonPhone() {
        return jsonPhone;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setDynamicControlData(JSONObject dynamicControlData) {
        this.dynamicControlData = dynamicControlData;
    }

    public JSONObject getDynamicControlData() {
        return dynamicControlData;
    }

    public String getDynamicControlName() {
        return dynamicControlName;
    }

    public void setDynamicControlName(String dynamicControlName) {
        this.dynamicControlName = dynamicControlName;
    }

    public String getDynamicControlType() {
        return dynamicControlType;
    }

    public void setDynamicControlType(String dynamicControlType) {
        this.dynamicControlType = dynamicControlType;
    }

    public String getDynmicSpinerValue() {
        return dynmicSpinerValue;
    }

    public void setDynmicSpinerValue(String dynmicSpinerValue) {
        this.dynmicSpinerValue = dynmicSpinerValue;
    }

    public String getLead() {
        return Lead;
    }

    public void setLead(String lead) {
        Lead = lead;
    }

    public String getTaskDiscription() {
        return taskDiscription;
    }

    public void setTaskDiscription(String taskDiscription) {
        this.taskDiscription = taskDiscription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getComapy() {
        return comapy;
    }

    public void setComapy(String comapy) {
        this.comapy = comapy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setContactID(String contact_id) {
        this.contact_id=contact_id;}

    public String getContact_id() {
        return contact_id;
    }
}

