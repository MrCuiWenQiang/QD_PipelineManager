package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

/**
 * 大类表
 */
public class Sys_Type_Child extends LitePalSupport {
    private long id;
    private String name;
    private String value;
    private String fatherCode;
    private String fatherName;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFatherCode() {
        return fatherCode;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }
}
