package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

/**
 * 管线颜色
 */
public class Sys_Color extends LitePalSupport {
    private long id;
    private String fatherCode;//父代号
    private String name;
    private String R;
    private String G;
    private String B;//颜色值

    public String getR() {
        return R;
    }

    public void setR(String r) {
        R = r;
    }

    public String getG() {
        return G;
    }

    public void setG(String g) {
        G = g;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

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
}
