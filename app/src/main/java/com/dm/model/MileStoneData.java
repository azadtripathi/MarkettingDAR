package com.dm.model;

/**
 * Created by Dataman on 11/5/2017.
 */

public class MileStoneData
{

    String milstone,rate,qty,amount,commper,commamount;
    String fileName,filePath;
    String imageIcon;

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void setCommamount(String commamount) {
        this.commamount = commamount;
    }

    public void setCommper(String commper) {
        this.commper = commper;
    }

    public String getCommamount() {
        return commamount;
    }

    public String getCommper() {

        return commper;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMilstone(String milstone) {
        this.milstone = milstone;
    }

    public String getRate() {
        return rate;
    }

    public String getQty() {
        return qty;
    }

    public String getAmount() {
        return amount;
    }

    public String getMilstone() {
        return milstone;
    }
}
