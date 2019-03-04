package com.dm.model;

/**
 * Created by Dataman on 11/20/2017.
 */

public class AttachmentData
{
    String fileId, fileName,fileUrl,imageIcon;

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
