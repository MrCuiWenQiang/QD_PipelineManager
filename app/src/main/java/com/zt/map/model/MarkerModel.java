package com.zt.map.model;

import com.zt.map.contract.MarkerContract;
import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_Jglx;
import com.zt.map.entity.db.system.Sys_Jgzt;
import com.zt.map.entity.db.system.Sys_Line_Data;
import com.zt.map.entity.db.system.Sys_Manhole;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.entity.db.system.Sys_szwz;
import com.zt.map.entity.db.system.Sys_tcfs;
import com.zt.map.entity.db.tab.Tab_Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class MarkerModel extends BaseMVPModel implements MarkerContract.Model {
    @Override
    public void queryMarker(final long id, final CommotListener<Tab_Marker> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Marker>() {

            @Override
            protected Tab_Marker jobContent() throws Exception {
                return LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ? ", String.valueOf(id));
            }

            @Override
            protected void jobEnd(Tab_Marker marker) {
                listener.result(marker);
            }
        });
    }

    public final static String TABLE_MARKER_TYPE_STATUS = "使用状态";
    public final static String TABLE_MARKER_TYPE_JGCZS = "井盖材质";
    public final static String TABLE_MARKER_TYPE_TZDS = "特征点";
    public final static String TABLE_MARKER_TYPE_FSWS = "附属物";
    public final static String TABLE_MARKER_TYPE_JGZTS = "井盖状态";
    public final static String TABLE_MARKER_TYPE_JGLX = "井盖类型";
    public final static String TABLE_MARKER_TYPE_SZWZ = "所在位置";
    public final static String TABLE_MARKER_TYPE_TCFS = "探测方式";
    public final static String TABLE_MARKER_TYPE_SJLY = "数据来源";
    public final static String TABLE_MARKER_TYPE_SFLY = "是否利用";

    public final static String TYPE_MARKER_FATHER_PS = "PS";

    public final static String[] tabs = new String[]{TABLE_MARKER_TYPE_STATUS, TABLE_MARKER_TYPE_JGCZS, TABLE_MARKER_TYPE_TZDS,
            TABLE_MARKER_TYPE_FSWS, TABLE_MARKER_TYPE_JGZTS, TABLE_MARKER_TYPE_JGLX,TABLE_MARKER_TYPE_SZWZ,TABLE_MARKER_TYPE_TCFS
    ,TABLE_MARKER_TYPE_SJLY,TABLE_MARKER_TYPE_SFLY};

    Map<String, Map<String, Long>> ids;//用于存放数据对应id 通过get方法获取选择项的id

    public long getIds(String f, String c) {
        long id = 0;
        for (String f_n : ids.keySet()) {
            if (f_n.equals(f)) {
                Map<String, Long> cv = ids.get(f_n);
                for (String dc : cv.keySet()) {
                    if (dc.equals(c)) {
                        id = cv.get(dc);
                        break;
                    }
                }
                break;
            }
        }


        return id;
    }

    // TODO: 2019/6/18 未考虑一点：对排水 大类 小类的数据控制问题
    @Override
    public void queryTable(final long typeId, final CommotListener<Map<String, String[]>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, String[]>>() {
            @Override
            protected Map<String, String[]> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(typeId));
                Sys_Type_Child father = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));

                Map<String, String[]> params = new LinkedHashMap<>();
                ids = new LinkedHashMap<>();

                //加载公共数据选项
                //使用状态
                List<Sys_UseStatus> userStatus = LitPalUtils.selectWhere(Sys_UseStatus.class, "fatherCode = ?", tables.getCode());
                List<String> datas = new ArrayList<>();
                Map<String, Long> userStatuIds = new LinkedHashMap<>();
                for (Sys_UseStatus item : userStatus) {
                    datas.add(item.getName());
                    userStatuIds.put(item.getName(), item.getId());
                }
                String[] status = datas.toArray(new String[datas.size()]);

                //井盖材质
                List<Sys_Manhole> manholes = LitPalUtils.selectWhere(Sys_Manhole.class, "fatherCode = ?", tables.getCode());
                datas.clear();
                Map<String, Long> manholeIds = new LinkedHashMap<>();
                for (Sys_Manhole item : manholes) {
                    datas.add(item.getName());
                    manholeIds.put(item.getName(), item.getId());
                }
                String[] jgczs = datas.toArray(new String[datas.size()]);

                //特征点
                List<Sys_Features> features = LitPalUtils.selectWhere(Sys_Features.class, "fatherCode = ?", father.getFatherCode());
                datas.clear();
                Map<String, Long> featureIDs = new LinkedHashMap<>();
                for (Sys_Features item : features) {
                    datas.add(item.getName());
                    featureIDs.put(item.getName(), item.getId());
                }
                String[] tzds = datas.toArray(new String[datas.size()]);

                //附属物
                List<Sys_Appendages> appendages = LitPalUtils.selectWhere(Sys_Appendages.class, "fatherCode = ?", father.getFatherCode());
                datas.clear();
                Map<String, Long> appendageIDs = new LinkedHashMap<>();
                for (Sys_Appendages item : appendages) {
                    datas.add(item.getName());
                    appendageIDs.put(item.getName(), item.getId());
                }
                String[] fsws = datas.toArray(new String[datas.size()]);
                //井盖类型
                List<Sys_Jglx> jgs = LitPalUtils.selectWhere(Sys_Jglx.class);
                datas.clear();
                Map<String, Long> jgIDs = new LinkedHashMap<>();
                for (Sys_Jglx item : jgs) {
                    datas.add(item.getName());
                    jgIDs.put(item.getName(), item.getId());
                }
                String[] jglx = datas.toArray(new String[datas.size()]);
                //井盖状态
                List<Sys_Jgzt> jgzts = LitPalUtils.selectWhere(Sys_Jgzt.class);
                datas.clear();
                Map<String, Long> jgztIDs = new LinkedHashMap<>();
                for (Sys_Jgzt item : jgzts) {
                    datas.add(item.getName());
                    jgztIDs.put(item.getName(), item.getId());
                }
                String[] jgzt = datas.toArray(new String[datas.size()]);
                //所在位置
                List<Sys_szwz> szwzs = LitPalUtils.selectWhere(Sys_szwz.class);
                datas.clear();
                Map<String, Long> szwzIDs = new LinkedHashMap<>();
                for (Sys_szwz item : szwzs) {
                    datas.add(item.getValue());
                    szwzIDs.put(item.getValue(), item.getId());
                }
                String[] szwz = datas.toArray(new String[datas.size()]);

                //所在位置
                List<Sys_tcfs> tcfss = LitPalUtils.selectWhere(Sys_tcfs.class);
                datas.clear();
                Map<String, Long> tcfsIDs = new LinkedHashMap<>();
                for (Sys_tcfs item : tcfss) {
                    datas.add(item.getValue());
                    tcfsIDs.put(item.getValue(), item.getId());
                }
                String[] tcfs = datas.toArray(new String[datas.size()]);


                //数据来源
                List<Sys_Line_Data> sjly = LitPalUtils.selectWhere(Sys_Line_Data.class,"name = ?","数据来源");
                datas.clear();
                Map<String, Long> sjlyIDs = new LinkedHashMap<>();
                for (Sys_Line_Data item : sjly) {
                    datas.add(item.getValue());
                    sjlyIDs.put(item.getValue(), item.getId());
                }
                String[] sjlys = datas.toArray(new String[datas.size()]);

                //是否利用
                List<Sys_Line_Data> sfly = LitPalUtils.selectWhere(Sys_Line_Data.class,"name = ?","是否利用");
                datas.clear();
                Map<String, Long> sflyIDs = new LinkedHashMap<>();
                for (Sys_Line_Data item : sfly) {
                    datas.add(item.getValue());
                    sflyIDs.put(item.getValue(), item.getId());
                }
                String[] sflys = datas.toArray(new String[datas.size()]);

                params.put(TABLE_MARKER_TYPE_STATUS, status);
                params.put(TABLE_MARKER_TYPE_JGCZS, jgczs);
                params.put(TABLE_MARKER_TYPE_TZDS, tzds);
                params.put(TABLE_MARKER_TYPE_FSWS, fsws);
                params.put(TABLE_MARKER_TYPE_JGZTS, jgzt);
                params.put(TABLE_MARKER_TYPE_JGLX, jglx);
                params.put(TABLE_MARKER_TYPE_SZWZ, szwz);
                params.put(TABLE_MARKER_TYPE_TCFS, tcfs);
                params.put(TABLE_MARKER_TYPE_SJLY, sjlys);
                params.put(TABLE_MARKER_TYPE_SFLY, sflys);

                ids.put(TABLE_MARKER_TYPE_STATUS, userStatuIds);
                ids.put(TABLE_MARKER_TYPE_JGCZS, manholeIds);
                ids.put(TABLE_MARKER_TYPE_TZDS, featureIDs);
                ids.put(TABLE_MARKER_TYPE_FSWS, appendageIDs);
                ids.put(TABLE_MARKER_TYPE_JGZTS, jgztIDs);
                ids.put(TABLE_MARKER_TYPE_JGLX, jgIDs);
                ids.put(TABLE_MARKER_TYPE_SZWZ, szwzIDs);
                ids.put(TABLE_MARKER_TYPE_TCFS, tcfsIDs);
                ids.put(TABLE_MARKER_TYPE_SJLY, sjlyIDs);
                ids.put(TABLE_MARKER_TYPE_SFLY, sflyIDs);

                String c_code = tables.getCode();
                String f_code = father.getFatherCode();
                if (f_code.equals("PS")) {
                    params.put(TYPE_MARKER_FATHER_PS, null);
                } else if (f_code.equals("DX")) {
                } else if (f_code.equals("RQ")) {
                } else if (f_code.equals("BM")) {
                }

                if (c_code.equals("GD")) {
//                        getView().visiblegd(dy);
                }
                return params;
            }

            @Override
            protected void jobEnd(Map<String, String[]> code) {
                listener.result(code);
            }
        });
    }
}
