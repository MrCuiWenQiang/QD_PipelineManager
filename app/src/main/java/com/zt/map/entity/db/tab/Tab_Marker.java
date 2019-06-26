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
    private long id;
    private long projectId;
    private long typeId;//类型id

    @ExcelCount(order = 1, name = "管线类型")
    private String gxlx;//管线类型
    @ExcelCount(order = 2, name = "管点编号")
    private String wtdh;//管点编号
    @ExcelCount(order = 3, name = "特征点")
    private String tzd;//特征点
    @ExcelCount(order = 4, name = "附属物")
    private String fsw;//附属物
    @ExcelCount(order = 5, name = "横坐标")
    private double latitude;//x
    @ExcelCount(order = 6, name = "纵坐标")
    private double longitude;//y
    @ExcelCount(order = 7, name = "测绘横坐标")
    private double chlatitude;//x
    @ExcelCount(order = 8, name = "测绘纵坐标")
    private double chlongitude;//y

    @ExcelCount(order = 9, name = "地面高程")
    private String dmgc;//高程
//    @ExcelCount(order = 10, name = "偏心井点号")
    private String pxjw;//偏心井点号
//    @ExcelCount(order = 11, name = "井类型")
    private String jlx;//井类型
//    @ExcelCount(order = 12, name = "井直径")
    private String jzj;//井直径
//    @ExcelCount(order = 13, name = "井脖深")
    private String jbs;//井脖深
    @ExcelCount(order = 14, name = "井底深")
    private String jds;//井底深
    @ExcelCount(order = 15, name = "井盖类型")
    private String jglx;//井盖类型
    @ExcelCount(order = 16, name = "井盖规格")
    private String jggg;//井盖规格
    @ExcelCount(order = 17, name = "井盖材质")
    private String jgcz;//井盖材质
//    @ExcelCount(order = 18, name = "井盖状态")
    private String jgzt;
    @ExcelCount(order = 19, name = "所在位置")
    private String szwz;//所在位置
//    @ExcelCount(order = 20, name = "使用状态")
    private String syzt;//使用状态
//    @ExcelCount(order = 21, name = "探测方式")
    private String tcfs;//探测方式

//    @ExcelCount(order = 22, name = "所在河道")
    private String szhd;
//    @ExcelCount(order = 23, name = "排水户名称")
    private String pshmc;
    @ExcelCount(order = 24, name = "影像资料文件编号")
    private String wjbh;
//    @ExcelCount(order = 25, name = "隐患情况说明")
    private String qksm;

    @ExcelCount(order = 25, name = "权属单位")
    private String qsdw;
    @ExcelCount(order = 26, name = "建设日期")
    private String Jsrq;
    @ExcelCount(order = 27, name = "数据来源")
    private String Sjly;
    @ExcelCount(order = 28, name = "是否利用")
    private String Sfly;

    @ExcelCount(order = 30, name = "备注")
    private String remarks;//备注

    public String getJgzt() {
        return jgzt;
    }

    public void setJgzt(String jgzt) {
        this.jgzt = jgzt;
    }

    private Date updateTime;
    private Date createTime;
    private String iconBase;
    private Sys_Color sys_color;

    public Sys_Color getSys_color() {
        if (sys_color != null) {
            return sys_color;
        }
        sys_color = LitPalUtils.selectsoloWhere(Sys_Color.class, "id = ?", String.valueOf(typeId));
        return sys_color;
    }


    public String getIconBase() {
        if (iconBase != null) {
            return iconBase;
        }
        if (TextUtils.isEmpty(tzd) && TextUtils.isEmpty(fsw)) {
            iconBase = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAoHBwkHBgoJCAkLCwoMDxkQDw4ODx4WFxIZJCAmJSMgIyIoLTkwKCo2KyIjMkQyNjs9QEBAJjBGS0U+Sjk/QD3/2wBDAQsLCw8NDx0QEB09KSMpPT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT3/wAARCAAJAAkDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDckk0qTQdQv9S1KaLxHFJKOLhlmhmDERpHFnkfdwMEMDk5zWn/AGl4z/6B8f8A3wP8ak1L/kpWn/8AXMfyauzoA//Z";
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
        }
        if (iconBase == null) {
            iconBase = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAoHBwkHBgoJCAkLCwoMDxkQDw4ODx4WFxIZJCAmJSMgIyIoLTkwKCo2KyIjMkQyNjs9QEBAJjBGS0U+Sjk/QD3/2wBDAQsLCw8NDx0QEB09KSMpPT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT09PT3/wAARCAAJAAkDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDckk0qTQdQv9S1KaLxHFJKOLhlmhmDERpHFnkfdwMEMDk5zWn/AGl4z/6B8f8A3wP8ak1L/kpWn/8AXMfyauzoA//Z";
        }
        return iconBase;
    }

    public String getGxlx() {
        Sys_Type type = LitPalUtils.selectsoloWhere(Sys_Type.class, "id=?", String.valueOf(typeId));
        if (type != null) {
            return type.getName();
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

    public void setChlongitude(double chlongitude) {
        this.chlongitude = chlongitude;
    }

    public String getSzhd() {
        return szhd;
    }

    public void setSzhd(String szhd) {
        this.szhd = szhd;
    }

    public String getPshmc() {
        return pshmc;
    }

    public void setPshmc(String pshmc) {
        this.pshmc = pshmc;
    }

    public String getWjbh() {
        return wjbh;
    }

    public void setWjbh(String wjbh) {
        this.wjbh = wjbh;
    }

    public String getQksm() {
        return qksm;
    }

    public void setQksm(String qksm) {
        this.qksm = qksm;
    }
    public void setGxlx(String gxlx) {
        this.gxlx = gxlx;
    }

    public String getJlx() {
        return jlx;
    }

    public void setJlx(String jlx) {
        this.jlx = jlx;
    }

    public String getJzj() {
        return jzj;
    }

    public void setJzj(String jzj) {
        this.jzj = jzj;
    }

    public String getJbs() {
        return jbs;
    }

    public void setJbs(String jbs) {
        this.jbs = jbs;
    }

    public String getJds() {
        return jds;
    }

    public void setJds(String jds) {
        this.jds = jds;
    }

    public String getJglx() {
        return jglx;
    }

    public void setJglx(String jglx) {
        this.jglx = jglx;
    }

    public String getJggg() {
        return jggg;
    }

    public void setJggg(String jggg) {
        this.jggg = jggg;
    }

    public String getJgcz() {
        return jgcz;
    }

    public void setJgcz(String jgcz) {
        this.jgcz = jgcz;
    }

    public String getSzwz() {
        return szwz;
    }

    public void setSzwz(String szwz) {
        this.szwz = szwz;
    }

    public String getSyzt() {
        return syzt;
    }

    public void setSyzt(String syzt) {
        this.syzt = syzt;
    }

    public String getTcfs() {
        return tcfs;
    }

    public void setTcfs(String tcfs) {
        this.tcfs = tcfs;
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
    }

    public String getTzd() {
        return tzd;
    }

    public void setTzd(String tzd) {
        this.tzd = tzd;
    }

    public String getFsw() {
        return fsw;
    }

    public void setFsw(String fsw) {
        this.fsw = fsw;
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
    }

    public String getPxjw() {
        return pxjw;
    }

    public void setPxjw(String pxjw) {
        this.pxjw = pxjw;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    }

    public String getJsrq() {
        return Jsrq;
    }

    public void setJsrq(String jsrq) {
        Jsrq = jsrq;
    }

    public String getSjly() {
        return Sjly;
    }

    public void setSjly(String sjly) {
        Sjly = sjly;
    }

    public String getSfly() {
        return Sfly;
    }

    public void setSfly(String sfly) {
        Sfly = sfly;
    }
}
