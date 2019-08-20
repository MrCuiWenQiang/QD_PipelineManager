package com.zt.map.model;

import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Direction;
import com.zt.map.entity.db.system.Sys_Embedding;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_LineFC;
import com.zt.map.entity.db.system.Sys_LineType;
import com.zt.map.entity.db.system.Sys_Line_Manhole;
import com.zt.map.entity.db.system.Sys_Manhole;
import com.zt.map.entity.db.system.Sys_Material;
import com.zt.map.entity.db.system.Sys_Pressure;
import com.zt.map.entity.db.system.Sys_TGCL;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

/**
 * 系统级数据查询
 */
public class SystemQueryModel extends BaseMVPModel {
    //查询类列表
    public void queryType(final CommotListener<List<Sys_Table>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Table>>() {

            @Override
            protected List<Sys_Table> jobContent() throws Exception {
                List<Sys_Table> tables = LitPalUtils.selectWhere(Sys_Table.class);
                return tables;
            }

            @Override
            protected void jobEnd(List<Sys_Table> sysTypeTableListMap) {
                listener.result(sysTypeTableListMap);
            }
        });

    }

    /**
     * 查询特征点
     *
     * @param listener
     */
    public void queryAspect(final String code,final CommotListener<List<Sys_Features>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Features>>() {

            @Override
            protected List<Sys_Features> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));

                    List<Sys_Features> datas = LitPalUtils.selectWhere(Sys_Features.class,"fatherCode = ?",f.getFatherCode());
                    return datas;
                }
                    return null;

            }

            @Override
            protected void jobEnd(List<Sys_Features> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 查询材质
     * @param code
     * @param listener
     */
    public void queryCZ(final String code,final CommotListener<List<Sys_Line_Manhole>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Line_Manhole>>() {

            @Override
            protected List<Sys_Line_Manhole> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));

                    List<Sys_Line_Manhole> datas = LitPalUtils.selectWhere(Sys_Line_Manhole.class,"name = ?",f.getFatherCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Line_Manhole> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }
    /**
     * 查询附属物
     * @param code
     * @param listener
     */
    public void queryAppendage(final String code,final CommotListener<List<Sys_Appendages>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Appendages>>() {

            @Override
            protected List<Sys_Appendages> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));
                    List<Sys_Appendages> datas = LitPalUtils.selectWhere(Sys_Appendages.class,"fatherCode = ?",f.getFatherCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Appendages> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 查询材料
     * @param code
     * @param listener
     */
    public void queryQuality(final String code,final CommotListener<List<Sys_Material>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Material>>() {

            @Override
            protected List<Sys_Material> jobContent() throws Exception {

                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_Material> datas = LitPalUtils.selectWhere(Sys_Material.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Material> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 查询方向
     * @param code
     * @param listener
     */
    public void queryDirection(final String code,final CommotListener<List<Sys_Direction>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Direction>>() {

            @Override
            protected List<Sys_Direction> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_Direction> datas = LitPalUtils.selectWhere(Sys_Direction.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Direction> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 查询埋设方式
     * @param code
     * @param listener
     */
    public void queryEmbedding(final String code,final CommotListener<List<Sys_Embedding>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Embedding>>() {

            @Override
            protected List<Sys_Embedding> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_Embedding> datas = LitPalUtils.selectWhere(Sys_Embedding.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Embedding> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 查询管线类型
     * @param code
     * @param listener
     */
    public void queryLineType(final String code,final CommotListener<List<Sys_LineType>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_LineType>>() {

            @Override
            protected List<Sys_LineType> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_LineType> datas = LitPalUtils.selectWhere(Sys_LineType.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_LineType> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }
    /**
     * 查询管线范畴
     * @param code
     * @param listener
     */
    public void queryLinefc(final String code,final CommotListener<List<Sys_LineFC>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_LineFC>>() {

            @Override
            protected List<Sys_LineFC> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_LineFC> datas = LitPalUtils.selectWhere(Sys_LineFC.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;
            }

            @Override
            protected void jobEnd(List<Sys_LineFC> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }
    /**
     * 井盖材质
     * @param code
     * @param listener
     */
    public void queryManhole(final String code,final CommotListener<List<Sys_Manhole>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_Manhole>>() {

            @Override
            protected List<Sys_Manhole> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_Manhole> datas = LitPalUtils.selectWhere(Sys_Manhole.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_Manhole> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 使用状态
     * @param code
     * @param listener
     */
    public void queryUseStatus(final long code, final CommotListener<List<Sys_UseStatus>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Sys_UseStatus>>() {

            @Override
            protected List<Sys_UseStatus> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",String.valueOf(code));
                if (tables!=null){
                    List<Sys_UseStatus> datas = LitPalUtils.selectWhere(Sys_UseStatus.class,"fatherCode = ?",tables.getCode());
                    return datas;
                }
                return null;

            }

            @Override
            protected void jobEnd(List<Sys_UseStatus> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }


    /**
     * 查询不一定有的数据
     * @param code
     * @param listener
     */
    public void queryUncertainData(final String code,final CommotListener<Map<String,Object>> listener){
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String,Object>>() {

            @Override
            protected Map<String,Object> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",code);
                if (tables!=null){
                    List<Sys_TGCL> datas1 = LitPalUtils.selectWhere(Sys_TGCL.class,"fatherCode = ?",tables.getCode());
                    List<Sys_Pressure> datas2 = LitPalUtils.selectWhere(Sys_Pressure.class,"fatherCode = ?",tables.getCode());
                    List<Sys_Direction> datas3 = LitPalUtils.selectWhere(Sys_Direction.class,"fatherCode = ?",tables.getCode());
                    Map<String,Object> params = new HashMap<>();
                    params.put("datas1",datas1);
                    params.put("datas2",datas2);
                    params.put("datas3",datas3);
                    return params;
                }
                return null;

            }

            @Override
            protected void jobEnd(Map<String,Object> sysQualityTables) {
                listener.result(sysQualityTables);
            }
        });
    }

    /**
     * 根据小类类别查询大类
     * @param typeId
     * @param listener
     */
    public void queryFatherType(final long typeId, final CommotListener<Map<String,String>> listener){
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String,String>>() {

            @Override
            protected Map<String,String> jobContent() throws Exception {
                Map<String,String> map = new HashMap<>();
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",String.valueOf(typeId));
                if (tables!=null){
                    String code = tables.getCode();
                    map.put("child",code);
                    Sys_Type_Child child = LitPalUtils.selectsoloWhere(Sys_Type_Child.class,"value = ?",code);
                    if (child!=null){
                        map.put("father",child.getFatherCode());
                        return map;
                    }
                }
                return map;
            }

            @Override
            protected void jobEnd(Map<String,String> s) {
                listener.result(s);
            }
        });
    }

    /**
     * 查询类信息
     * @param typeId
     * @param listener
     */
    public void queryType(final long typeId, final CommotListener<String> listener){
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<String>() {

            @Override
            protected String jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class,"id = ?",String.valueOf(typeId));
                if (tables!=null){
                    String code = tables.getCode();
                   return code;
                }
                return null;
            }

            @Override
            protected void jobEnd(String s) {
                listener.result(s);
            }
        });
    }
}
