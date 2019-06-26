package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

public class Sys_Line_Manhole extends LitePalSupport {
    private long id;
    private String name;//类别名（大类）
    private String value;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
