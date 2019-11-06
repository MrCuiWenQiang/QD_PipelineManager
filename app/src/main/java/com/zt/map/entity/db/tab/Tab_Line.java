package com.zt.map.entity.db.tab;

import android.graphics.Color;
import android.text.TextUtils;

import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.util.out.AccessTableName;
import com.zt.map.util.out.ExcelCount;
import com.zt.map.util.out.ExcelName;

import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

/**
 * 管线表
 */
@AccessTableName(name = "JS_LINE")
@ExcelName(TabName = "管线表")
public class Tab_Line extends LitePalSupport {
    private long id =-1;
    private long projectId;

    private long typeId;


    private long startMarkerId;
    private long endMarkerId;
    @ExcelCount(order = 101, name = "起点纬度")
    private double start_latitude;
    @ExcelCount(order = 102, name = "起点经度")
    private double start_longitude;
    @ExcelCount(order = 103, name = "终点纬度")
    private double end_latitude;
    @ExcelCount(order = 104, name = "终点经度")
    private double end_longitude;

    private int color = -1;
    @ExcelCount(order = 0, name = "管类")
    private String Gxlx;//管线类型
    @ExcelCount(order = 1, name = "管线类型")
    private String Gxoutlx;//管线类型输出

    // TODO: 2019/5/29 起始物号和终止物号 用markerID去查
    @ExcelCount(order = 2, name = "起点点号")
    private String Qswh;
    @ExcelCount(order = 3, name = "终点点号")
    private String Zzwh;


    @ExcelCount(order = 4, name = "起点埋深")
    private String Qdms;
    @ExcelCount(order = 5, name = "终点埋深")
    private String Zzms;
    @ExcelCount(order = 6, name = "材质")
    private String Gxcl;//材质
    @ExcelCount(order = 7, name = "埋设方式")
    private String Msfs;
    @ExcelCount(order = 8, name = "管径")
    private String Gjdm;//管径
    @ExcelCount(order = 9, name = "线型")
    private String xx;
    @ExcelCount(order = 10, name = "流向")
    private String lx;//流向
    @ExcelCount(order = 11, name = "套管材质")
    private String tgcz;
    @ExcelCount(order = 12, name = "条数")
    private String Ts;
    @ExcelCount(order = 13, name = "压力值")
    private String yl;
    /*    @ExcelCount(order = 14,name = "电压")
        private String dy;*/
    @ExcelCount(order = 15, name = "总孔数")
    private String Zks;
    @ExcelCount(order = 16, name = "已用孔数")
    private String Yyks;
    //    @ExcelCount(order = 17,name = "所在位置")
    private String Szwz;//所在位置
    //    @ExcelCount(order = 18,name = "管线范畴")
    private String gxfc;

    //    @ExcelCount(order = 19,name = "起点淤积")
    private String Qdyj;//起点淤积
    //    @ExcelCount(order = 20,name = "终点淤积")
    private String Zdyj;//终点淤积
    //    @ExcelCount(order = 21,name = "运行状态")
    private String Yxzt;//运行状态
    //    @ExcelCount(order = 22,name = "倒虹管段")
    private String Dhgd;//倒虹管段
    //    @ExcelCount(order = 23,name = "建设日期")
    private String Jsnd;//建设年代
    //    @ExcelCount(order = 24,name = "权属单位")
    private String Qsdw;//权属单位

    @ExcelCount(order = 25, name = "使用状况")
    private String Syzt;//使用状态
    //    @ExcelCount(order = 18,name = "探测方式")
    private String tcfs;//探测方式


    //    @ExcelCount(order = 26,name = "设施运行状态")
    private String ssyxzt;
    //    @ExcelCount(order = 27,name = "管线质量")
    private String gxzl;
    //    @ExcelCount(order = 28,name = "管道等级")
    private String gddj;
    //    @ExcelCount(order = 29,name = "隐患情况说明")
    private String yhqk;


    //    @ExcelCount(order = 30,name = "是否利用")
    private String sfly;


    @ExcelCount(order = 100, name = "备注")
    private String Remarks;
    private Date updateTime;
    private Date createTime;

    public String getQswh() {
        if (!TextUtils.isEmpty(Qswh)) {
            return Qswh;
        }
        Tab_Marker marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(startMarkerId));
        if (marker == null) {
            return null;
        } else {
            Qswh = marker.getWtdh();
            return Qswh;
        }
    }

    public String getGxfc() {
        return gxfc;
    }

    public String getGxoutlx() {
        return Gxoutlx;
    }

    public void setGxoutlx(String gxoutlx) {
        Gxoutlx = gxoutlx;
        if (TextUtils.isEmpty(Gxoutlx)){
            setToDefault("Gxoutlx");
        }
    }

    public void setGxfc(String gxfc) {
        this.gxfc = gxfc;
        if (TextUtils.isEmpty(gxfc)){
            setToDefault("gxfc");
        }
    }

    public void setQswh(String qswh) {
        Qswh = qswh;
        if (TextUtils.isEmpty(Qswh)){
            setToDefault("Qswh");
        }
    }

    public void setZzwh(String zzwh) {
        Zzwh = zzwh;
        if (TextUtils.isEmpty(Zzwh)){
            setToDefault("Zzwh");
        }
    }

    public String getZks() {
        return Zks;
    }

    public void setZks(String zks) {
        Zks = zks;
        if (TextUtils.isEmpty(Zks)){
            setToDefault("Zks");
        }
    }

    public String getYyks() {
        return Yyks;
    }

    public void setYyks(String yyks) {
        Yyks = yyks;
        if (TextUtils.isEmpty(Yyks)){
            setToDefault("Yyks");
        }
    }

    public String getTs() {
        return Ts;
    }

    public void setTs(String ts) {
        Ts = ts;
        if (TextUtils.isEmpty(Ts)){
            setToDefault("Ts");
        }
    }


    public String getSsyxzt() {
        return ssyxzt;
    }

    public void setSsyxzt(String ssyxzt) {
        this.ssyxzt = ssyxzt;
        if (TextUtils.isEmpty(ssyxzt)){
            setToDefault("ssyxzt");
        }
    }

    public String getGxzl() {
        return gxzl;
    }

    public void setGxzl(String gxzl) {
        this.gxzl = gxzl;
        if (TextUtils.isEmpty(gxzl)){
            setToDefault("gxzl");
        }
    }

    public String getGddj() {
        return gddj;
    }

    public void setGddj(String gddj) {
        this.gddj = gddj;
        if (TextUtils.isEmpty(gddj)){
            setToDefault("gddj");
        }
    }

    public String getYhqk() {
        return yhqk;
    }

    public void setYhqk(String yhqk) {
        this.yhqk = yhqk;
        if (TextUtils.isEmpty(yhqk)){
            setToDefault("yhqk");
        }
    }

    public String getYl() {
        return yl;
    }

    public void setYl(String yl) {
        this.yl = yl;
        if (TextUtils.isEmpty(yl)){
            setToDefault("yl");
        }
    }
/*
    public String getDy() {
        return dy;
    }

    public void setDy(String dy) {
        this.dy = dy;
    }*/

    public String getZzwh() {
        if (!TextUtils.isEmpty(Zzwh)) {
            return Zzwh;
        }
        Tab_Marker marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(endMarkerId));
        if (marker == null) {
            return null;
        } else {
            Zzwh = marker.getWtdh();
            return Zzwh;
        }
    }

    public String getXx() {
        return xx;
    }

    public void setXx(String xx) {
        this.xx = xx;
        if (TextUtils.isEmpty(xx)){
            setToDefault("xx");
        }
    }

    public String getTgcz() {
        return tgcz;
    }

    public void setTgcz(String tgcz) {
        this.tgcz = tgcz;
        if (TextUtils.isEmpty(tgcz)){
            setToDefault("tgcz");
        }
    }

    public String getSfly() {
        return sfly;
    }

    public void setSfly(String sfly) {
        this.sfly = sfly;
        if (TextUtils.isEmpty(sfly)){
            setToDefault("sfly");
        }
    }

    public long getTypeId() {
        return typeId;
    }

    protected Object c1;

    public void setc(Sys_Color c1){
        this.c1 = c1;
    }

    public int getColor() {
        if (color > 0) {
            return color;
        }
        if (c1 != null) {
            Sys_Color c2 = (Sys_Color) c1;
            color = Color.rgb(Integer.valueOf(c2.getR()), Integer.valueOf(c2.getG()),
                    Integer.valueOf(c2.getB()));
        } else {
            color = 0xAAFF0000;
        }
        return color;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
        if (TextUtils.isEmpty(lx)){
            setToDefault("lx");
        }
    }


    public void setTypeId(long typeId) {
        this.typeId = typeId;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }


    public long getStartMarkerId() {
        return startMarkerId;
    }

    public void setStartMarkerId(long startMarkerId) {
        this.startMarkerId = startMarkerId;
    }

    public long getEndMarkerId() {
        return endMarkerId;
    }

    public void setEndMarkerId(long endMarkerId) {
        this.endMarkerId = endMarkerId;
    }

    public double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public double getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public double getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(double end_longitude) {
        this.end_longitude = end_longitude;
    }


    public String getQdms() {
        return Qdms;
    }

    public void setQdms(String qdms) {
        Qdms = qdms;
        if (TextUtils.isEmpty(qdms)){
            setToDefault("Qdms");
        }
    }

    public String getZzms() {
        return Zzms;
    }

    public void setZzms(String zzms) {
        Zzms = zzms;
        if (TextUtils.isEmpty(zzms)){
            setToDefault("Zzms");
        }
    }

    public String getMsfs() {
        return Msfs;
    }

    public void setMsfs(String msfs) {
        Msfs = msfs;
        if (TextUtils.isEmpty(msfs)){
            setToDefault("Msfs");
        }
    }

    public String getGxcl() {
        return Gxcl;
    }

    public void setGxcl(String gxcl) {
        Gxcl = gxcl;
        if (TextUtils.isEmpty(gxcl)){
            setToDefault("Gxcl");
        }
    }

    public String getGjdm() {
        return Gjdm;
    }

    public void setGjdm(String gjdm) {
        Gjdm = gjdm;
        if (TextUtils.isEmpty(gjdm)){
            setToDefault("Gjdm");
        }
    }

    public String getQsdw() {
        return Qsdw;
    }

    public void setQsdw(String qsdw) {
        Qsdw = qsdw;
        if (TextUtils.isEmpty(qsdw)){
            setToDefault("Qsdw");
        }
    }


    public String getJsnd() {
        return Jsnd;
    }

    public void setJsnd(String jsnd) {
        Jsnd = jsnd;
        if (TextUtils.isEmpty(jsnd)){
            setToDefault("Jsnd");
        }
    }


    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
        if (TextUtils.isEmpty(remarks)){
            setToDefault("Remarks");
        }
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGxlxValue(){
        return Gxlx;
    }
    public String getGxlx() {


        Sys_Table table = LitPalUtils.selectsoloWhere(Sys_Table.class, "id=?", String.valueOf(typeId));
        if (table == null) return null;

//        Sys_Type_Child type = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "value=?", table.getCode());
        if (table != null) {
            return table.getCode();
        } else {
            return null;
        }
    }

    public void setGxlx(String gxlx) {
        Gxlx = gxlx;
        if (TextUtils.isEmpty(gxlx)){
            setToDefault("Gxlx");
        }
    }

    public String getQdyj() {
        return Qdyj;
    }

    public void setQdyj(String qdyj) {
        Qdyj = qdyj;
        if (TextUtils.isEmpty(qdyj)){
            setToDefault("Qdyj");
        }
    }

    public String getZdyj() {
        return Zdyj;
    }

    public void setZdyj(String zdyj) {
        Zdyj = zdyj;
        if (TextUtils.isEmpty(zdyj)){
            setToDefault("Zdyj");
        }
    }

    public String getYxzt() {
        return Yxzt;
    }

    public void setYxzt(String yxzt) {
        Yxzt = yxzt;
        if (TextUtils.isEmpty(yxzt)){
            setToDefault("Yxzt");
        }
    }

    public String getDhgd() {
        return Dhgd;
    }

    public void setDhgd(String dhgd) {
        Dhgd = dhgd;
        if (TextUtils.isEmpty(dhgd)){
            setToDefault("Dhgd");
        }
    }

    public String getSzwz() {
        return Szwz;
    }

    public void setSzwz(String szwz) {
        Szwz = szwz;
        if (TextUtils.isEmpty(szwz)){
            setToDefault("Szwz");
        }
    }

    public String getSyzt() {
        return Syzt;
    }

    public void setSyzt(String syzt) {
        Syzt = syzt;
        if (TextUtils.isEmpty(syzt)){
            setToDefault("Syzt");
        }
    }

    public String getTcfs() {
        return tcfs;
    }

    public void setTcfs(String tcfs) {
        this.tcfs = tcfs;
        if (TextUtils.isEmpty(tcfs)){
            setToDefault("tcfs");
        }
    }
}
