package com.zt.map.entity.db.tab;

import org.litepal.crud.LitePalSupport;

import java.util.Date;


public class Tab_Project extends LitePalSupport {
    private long id;
    private String name;
    private String measureName;//测量前置名称
    private boolean isCreatePhoto = false;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isCreatePhoto() {
        return isCreatePhoto;
    }

    public void setCreatePhoto(boolean createPhoto) {
        isCreatePhoto = createPhoto;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }
}
