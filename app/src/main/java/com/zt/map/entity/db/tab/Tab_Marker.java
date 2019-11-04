package com.zt.map.entity.db.tab;

import android.text.TextUtils;

import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.util.out.AccessTableName;
import com.zt.map.util.out.ExcelCount;
import com.zt.map.util.out.ExcelName;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

/**
 * 管点表
 */
@AccessTableName(name = "JS_POINT")
@ExcelName(TabName = "管点表")
public class Tab_Marker extends LitePalSupport {
    private long id =-1;
    private long projectId;
    private long typeId;//类型id

    @ExcelCount(order = 0, name = "管类")
    private String gxlx;//管线类型
    @ExcelCount(order = 1, name = "物探点号")
    private String wtdh;//管点编号
    @ExcelCount(order = 2, name = "特征")
    private String tzd;//特征点
    @ExcelCount(order = 3, name = "附属物")
    private String fsw;//附属物
    @ExcelCount(order = 4, name = "井底深")
    private String jds;//井底深
    @ExcelCount(order = 5, name = "数据来源")
    private String Sjly;
    @ExcelCount(order = 6, name = "获取时机")
    private String hqsj;//获取时机
    @ExcelCount(order = 7, name = "井盖类型")
    private String jglx;//井盖类型
    @ExcelCount(order = 8, name = "井盖规格")
    private String jggg;//井盖规格
    @ExcelCount(order = 9, name = "偏心井点号")
    private String pxjw;//偏心井点号
    @ExcelCount(order = 10, name = "偏距")
    private String pj;//偏距
    @ExcelCount(order = 11, name = "备注")
    private String remarks;//备注
//    @ExcelCount(order = 12, name = "测绘横坐标")
    private double chlatitude;//x
//    @ExcelCount(order = 13, name = "测绘纵坐标")
    private double chlongitude;//y


    @ExcelCount(order = 101, name = "纬度")
    private double latitude;//x
    @ExcelCount(order = 102, name = "经度")
    private double longitude;//y

//    @ExcelCount(order = 11, name = "地面高程")
    private String dmgc;//高程



    @ExcelCount(order = 12, name = "井盖材质")
    private String jgcz;//井盖材质
//    @ExcelCount(order = 16, name = "权属单位")
    private String qsdw;
//    @ExcelCount(order = 17, name = "建设日期")
    private String Jsrq;


//    @ExcelCount(order = 20, name = "所在位置")
    private String szwz;//所在位置
//    @ExcelCount(order = 21, name = "管点范畴")
    private String fc;



//    @ExcelCount(order = 11, name = "井类型")
    private String jlx;//井类型
//    @ExcelCount(order = 12, name = "井直径")
    private String jzj;//井直径
//    @ExcelCount(order = 13, name = "井脖深")
    private String jbs;//井脖深

//    @ExcelCount(order = 18, name = "井盖状态")
    private String jgzt;

//    @ExcelCount(order = 20, name = "使用状态")
    private String syzt;//使用状态
//    @ExcelCount(order = 21, name = "探测方式")
    private String tcfs;//探测方式

//    @ExcelCount(order = 22, name = "所在河道")
    private String szhd;
//    @ExcelCount(order = 23, name = "排水户名称")
    private String pshmc;
//    @ExcelCount(order = 22, name = "影像资料文件编号")
    private String wjbh;
//    @ExcelCount(order = 25, name = "隐患情况说明")
    private String qksm;


//    @ExcelCount(order = 23, name = "是否利用")
    private String Sfly;
    @ExcelCount(order = 24, name = "测量点号")
    private String cldh;


    public String getJgzt() {
        return jgzt;
    }

    public void setJgzt(String jgzt) {
        this.jgzt = jgzt;
        if (TextUtils.isEmpty(jgzt)){
            setToDefault("jgzt");
        }
    }

    private Date updateTime;
    private Date createTime;
    public String iconBase;
    public Sys_Color sys_color;

    public String getHqsj() {
        return hqsj;
    }

    public void setHqsj(String hqsj) {
        this.hqsj = hqsj;
        if (TextUtils.isEmpty(hqsj)){
            setToDefault("hqsj");
        }
    }

    public Sys_Color getSys_color() {
        if (sys_color != null) {
            return sys_color;
        }
//        sys_color = LitPalUtils.selectsoloWhere(Sys_Color.class, "id = ?", String.valueOf(typeId));
        return sys_color;
    }

    public String getFc() {
        return fc;
    }

    public void setFc(String fc) {
        this.fc = fc;
        if (TextUtils.isEmpty(fc)){
            setToDefault("fc");
        }
    }

    public String getIconBase() {
        if (iconBase != null) {
            return iconBase;
        }
        if (!TextUtils.isEmpty(fsw)&&fsw.equals("检修井")){
            iconBase = "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";
            return iconBase;
        }
/*        if (TextUtils.isEmpty(tzd) && TextUtils.isEmpty(fsw)) {
            iconBase = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAoHBwkHBgoJCAkLCwoMDxkQDw4ODx4WFxIZJCAmJSMgIyIoLTkwKCo2KyIjMkQyNjs9QEBAJjBGS0U+Sjk/QD3/2wBDAQsLCw8NDx0QEB09KSMpPT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT3/wAARCAAJAAkDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDckk0qTQdQv9S1KaLxHFJKOLhlmhmDERpHFnkfdwMEMDk5zWn/AGl4z/6B8f8A3wP8ak1L/kpWn/8AXMfyauzoA//Z";
            return iconBase;
        }
        if (!TextUtils.isEmpty(fsw)&&fsw.equals("检修井")){
            iconBase = "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";
            return iconBase;
        }


        Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(typeId));
        Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));
        if (!TextUtils.isEmpty(tzd)) {
            Sys_Features features = LitPalUtils.selectsoloWhere(Sys_Features.class, "name = ? AND fatherCode = ?", tzd, f.getFatherCode());
            if (features != null) {
                iconBase = features.getIcon();
            }
        } else if (!TextUtils.isEmpty(fsw)) {
            Sys_Appendages appendages = LitPalUtils.selectsoloWhere(Sys_Appendages.class, "name = ? AND fatherCode = ?", fsw, f.getFatherCode());
            if (appendages != null) {
                iconBase = appendages.getIcon();
            }
        }*/
        if (iconBase == null) {
            iconBase = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAoHBwkHBgoJCAkLCwoMDxkQDw4ODx4WFxIZJCAmJSMgIyIoLTkwKCo2KyIjMkQyNjs9QEBAJjBGS0U+Sjk/QD3/2wBDAQsLCw8NDx0QEB09KSMpPT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT3/wAARCAAJAAkDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDckk0qTQdQv9S1KaLxHFJKOLhlmhmDERpHFnkfdwMEMDk5zWn/AGl4z/6B8f8A3wP8ak1L/kpWn/8AXMfyauzoA//Z";
        }
        return iconBase;
    }
    public void setColor(Sys_Color sys_color){
        this.sys_color = sys_color;
    }
    public String getGxlx() {


        Sys_Table table = LitPalUtils.selectsoloWhere(Sys_Table.class, "id=?", String.valueOf(typeId));
        if(table==null) return null;

//        Sys_Type_Child type = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "value=?", table.getCode());
        if (table != null) {
            return table.getCode();
        } else {
            return null;
        }
    }
    public double getChlatitude() {
        return chlatitude;
    }

    public void setChlatitude(double chlatitude) {
        this.chlatitude = chlatitude;
    }

    public double getChlongitude() {
        return chlongitude;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
        if (TextUtils.isEmpty(pj)){
            setToDefault("pj");
        }
    }

    public void setChlongitude(double chlongitude) {
        this.chlongitude = chlongitude;
    }

    public String getSzhd() {
        return szhd;
    }

    public void setSzhd(String szhd) {
        this.szhd = szhd;
        if (TextUtils.isEmpty(szhd)){
            setToDefault("szhd");
        }
    }
    public String getCldh() {
        return cldh;
    }

    public void setCldh(String cldh) {
        this.cldh = cldh;
        if (TextUtils.isEmpty(cldh)){
            setToDefault("cldh");
        }
    }
    public String getPshmc() {
        return pshmc;
    }

    public void setPshmc(String pshmc) {
        this.pshmc = pshmc;
        if (TextUtils.isEmpty(pshmc)){
            setToDefault("pshmc");
        }
    }

    public String getWjbh() {
        return wjbh;
    }

    public void setWjbh(String wjbh) {
        this.wjbh = wjbh;
        if (TextUtils.isEmpty(wjbh)){
            setToDefault("wjbh");
        }
    }

    public String getQksm() {
        return qksm;
    }

    public void setQksm(String qksm) {
        this.qksm = qksm;
        if (TextUtils.isEmpty(qksm)){
            setToDefault("qksm");
        }
    }
    public void setGxlx(String gxlx) {
        this.gxlx = gxlx;
        if (TextUtils.isEmpty(gxlx)){
            setToDefault("gxlx");
        }
    }

    public String getJlx() {
        return jlx;
    }

    public void setJlx(String jlx) {
        this.jlx = jlx;
        if (TextUtils.isEmpty(jlx)){
            setToDefault("jlx");
        }
    }

    public String getJzj() {
        return jzj;
    }

    public void setJzj(String jzj) {
        this.jzj = jzj;
        if (TextUtils.isEmpty(jzj)){
            setToDefault("jzj");
        }
    }

    public String getJbs() {
        return jbs;
    }

    public void setJbs(String jbs) {
        this.jbs = jbs;
        if (TextUtils.isEmpty(jbs)){
            setToDefault("jbs");
        }
    }

    public String getJds() {
        return jds;
    }

    public void setJds(String jds) {
        this.jds = jds;
        if (TextUtils.isEmpty(jds)){
            setToDefault("jds");
        }
    }

    public String getJglx() {
        return jglx;
    }

    public void setJglx(String jglx) {
        this.jglx = jglx;
        if (TextUtils.isEmpty(jglx)){
            setToDefault("jglx");
        }
    }

    public String getJggg() {
        return jggg;
    }

    public void setJggg(String jggg) {
        this.jggg = jggg;
        if (TextUtils.isEmpty(jggg)){
            setToDefault("jggg");
        }
    }

    public String getJgcz() {
        return jgcz;
    }

    public void setJgcz(String jgcz) {
        this.jgcz = jgcz;
        if (TextUtils.isEmpty(jgcz)){
            setToDefault("jgcz");
        }
    }

    public String getSzwz() {
        return szwz;
    }

    public void setSzwz(String szwz) {
        this.szwz = szwz;
        if (TextUtils.isEmpty(szwz)){
            setToDefault("szwz");
        }
    }

    public String getSyzt() {
        return syzt;
    }

    public void setSyzt(String syzt) {
        this.syzt = syzt;
        if (TextUtils.isEmpty(syzt)){
            setToDefault("syzt");
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

    public long getTypeId() {
        return typeId;
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


    public String getWtdh() {
        return wtdh;
    }

    public void setWtdh(String wtdh) {
        this.wtdh = wtdh;
        if (TextUtils.isEmpty(wtdh)){
            setToDefault("wtdh");
        }
    }

    public String getTzd() {
        return tzd;
    }

    public void setTzd(String tzd) {
        this.tzd = tzd;
        if (TextUtils.isEmpty(tzd)){
            setToDefault("tzd");
        }
    }

    public String getFsw() {
        return fsw;
    }

    public void setFsw(String fsw) {
        this.fsw = fsw;
        if (TextUtils.isEmpty(fsw)){
            setToDefault("fsw");
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDmgc() {
        return dmgc;
    }

    public void setDmgc(String dmgc) {
        this.dmgc = dmgc;
        if (TextUtils.isEmpty(dmgc)){
            setToDefault("dmgc");
        }
    }

    public String getPxjw() {
        return pxjw;
    }

    public void setPxjw(String pxjw) {
        this.pxjw = pxjw;
        if (TextUtils.isEmpty(pxjw)){
            setToDefault("pxjw");
        }
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        if (TextUtils.isEmpty(remarks)){
            setToDefault("remarks");
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


    public String getQsdw() {
        return qsdw;
    }

    public void setQsdw(String qsdw) {
        this.qsdw = qsdw;
        if (TextUtils.isEmpty(qsdw)){
            setToDefault("qsdw");
        }
    }

    public String getJsrq() {
        return Jsrq;
    }

    public void setJsrq(String jsrq) {
        Jsrq = jsrq;
        if (TextUtils.isEmpty(jsrq)){
            setToDefault("Jsrq");
        }
    }

    public String getSjly() {
        return Sjly;
    }

    public void setSjly(String sjly) {
        Sjly = sjly;
        if (TextUtils.isEmpty(sjly)){
            setToDefault("Sjly");
        }
    }

    public String getSfly() {
        return Sfly;
    }

    public void setSfly(String sfly) {
        Sfly = sfly;
        if (TextUtils.isEmpty(sfly)){
            setToDefault("Sfly");
        }
    }
}
