package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;
//井盖状态
public class Sys_Jgzt extends LitePalSupport {
    private long id;
    private String name;

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
}
