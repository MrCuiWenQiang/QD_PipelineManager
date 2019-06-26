package com.zt.map.entity.db.tab;

import org.litepal.crud.LitePalSupport;

public class TabUser  extends LitePalSupport {
    private long id;
    private String admin;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
