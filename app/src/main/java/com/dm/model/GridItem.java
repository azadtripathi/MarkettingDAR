package com.dm.model;

/**
 * Created by dataman on 5/30/2017.
 */

public class GridItem {
    private String id;
    private String image;
    private String title;
    private String flag;
    private String downloadUrl;

    public GridItem() {
        super();
    }

    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getflag() {
        return flag;
    }
    public void setflag(String flag) {
        this.flag = flag;
    }

    public String getdownloadUrl() {
        return downloadUrl;
    }
    public void setdownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
