package com.dm.crmdm_app;

/**
 * Created by Dataman on 11/8/2017.
 */

public class ContactInfoModel
{
    String name,mobile,email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }
}
