package com.zt.map.entity;



//APP校验信息
public class CheckBean {
    private int type;//0正常  1停用 2审核中 3审核不通过 4未提交信息
    private String msg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
