package com.dm.model;

/**
 * Created by dataman on 28-Mar-17.
 */
public class NotificationData {
        private String notifyId;
        String msg;
        String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotId() {
            return this.notifyId;
        }
        public void setNotId(String notifyId) {
            this.notifyId = notifyId;
        }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
