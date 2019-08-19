package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

import java.util.Date;
//APP注册信息
public class Sys_RegisterInfo extends LitePalSupport {
    private int type;//注册类型 1注册码注册  2验证信息注册
    private String androidUUID;//android的唯一标识

    private String coltd;//公司名称
    private String linman;//联系人名称
    private String tel;//联系电话
    private String email;//联系邮箱
    private String remarks;//备注信息

    private String code;//注册码
    private Boolean isRegister;//是否注册成功
    private Date createTimer;//注册时间
    private String info;//当注册到期后 接口返回信息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAndroidUUID() {
        return androidUUID;
    }

    public void setAndroidUUID(String androidUUID) {
        this.androidUUID = androidUUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getRegister() {
        return isRegister;
    }

    public void setRegister(Boolean register) {
        isRegister = register;
    }

    public Date getCreateTimer() {
        return createTimer;
    }

    public void setCreateTimer(Date createTimer) {
        this.createTimer = createTimer;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getColtd() {
        return coltd;
    }

    public void setColtd(String coltd) {
        this.coltd = coltd;
    }

    public String getLinman() {
        return linman;
    }

    public void setLinman(String linman) {
        this.linman = linman;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
