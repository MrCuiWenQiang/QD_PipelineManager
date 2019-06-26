package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;
//所在位置
public class Sys_szwz  extends LitePalSupport {
    private long id;
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
