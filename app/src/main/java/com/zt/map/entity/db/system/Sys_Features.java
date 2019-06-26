package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

/**
 * 特征点
 */
public class Sys_Features extends LitePalSupport {
    private long id;
    private String fatherCode;//父代号
    private String name;
    private String icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFatherCode() {
        return fatherCode;
    }

    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
