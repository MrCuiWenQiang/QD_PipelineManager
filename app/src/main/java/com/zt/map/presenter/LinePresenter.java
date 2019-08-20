package com.zt.map.presenter;

import android.text.TextUtils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zt.map.constant.TypeConstant;
import com.zt.map.contract.LineContract;
import com.zt.map.entity.db.system.Sys_Direction;
import com.zt.map.entity.db.system.Sys_Embedding;
import com.zt.map.entity.db.system.Sys_LineFC;
import com.zt.map.entity.db.system.Sys_LineType;
import com.zt.map.entity.db.system.Sys_Line_Data;
import com.zt.map.entity.db.system.Sys_Line_Manhole;
import com.zt.map.entity.db.system.Sys_Pressure;
import com.zt.map.entity.db.system.Sys_TGCL;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.model.SystemQueryModel;
import com.zt.map.util.MapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class LinePresenter extends BaseMVPPresenter<LineContract.View> implements LineContract.Presenter {

    private String[] remarks = new String[]{"图边点", "出图点"};
    private String[] sfly ;
    private String[] xx ;

    private SystemQueryModel queryModel = new SystemQueryModel();
    private String[] lineTypes;
    private String[] msfss;


//    private String[] tgcls;
//    private String[] pressures;
//    private String[] directions;


    @Override
    public void save(final Tab_Line tab) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback() {


            @Override
            protected Object jobContent() throws Exception {
                return tab.save();
            }

            @Override
            protected void jobEnd(Object o) {
                if (getView() != null) {
                    boolean status = (boolean) o;
                    if (status) {
                        getView().saveSuccess();
                    } else {
                        getView().saveFail("保存失败");
                    }
                }
            }
        });
    }

    @Override
    public void queryLine(final long id) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Line>() {

            @Override
            protected Tab_Line jobContent() throws Exception {
                Tab_Line tabLine = LitPalUtils.selectsoloWhere(Tab_Line.class, "id = ? ", String.valueOf(id));
                if (tabLine != null) {
                    tabLine.getQswh();
                    tabLine.getZzwh();
                }
                return tabLine;
            }

            @Override
            protected void jobEnd(Tab_Line tabLine) {
                if (getView() == null) {
                    return;
                }

                if (tabLine != null) {
                    tabLine.getQswh();
                    tabLine.getZzwh();
                    getView().queryLine_Success(tabLine);
                } else {
                    getView().saveFail("未查询到该管线信息");
                }
            }
        });
    }
    private String[] gxfc;
    @Override
    public void queryGXFC(long id) {
        if (gxfc != null) {
            getView().queryGXFC(gxfc);
            return;
        }
            queryModel.queryLinefc(String.valueOf(id), new BaseMVPModel.CommotListener<List<Sys_LineFC>>() {
                @Override
                public void result(List<Sys_LineFC> sys_lineFCS) {
                    if (getView() == null) {
                        return;
                    }
                    if (sys_lineFCS == null || sys_lineFCS.size() <= 0) {
                        getView().fail("未获取到选择数据");
                    } else {
                        List<String> datas = new ArrayList<>();
                        for (Sys_LineFC item : sys_lineFCS) {
                            datas.add(item.getName());
                        }
                        gxfc = datas.toArray(new String[datas.size()]);
                        getView().queryGXFC(gxfc);
                    }
                }
            });
    }

    private String[] czs;

    public void queryCZ(final long typeid) {
        if (czs != null) {
            getView().queryCZ(czs);
            return;
        }
        queryModel.queryCZ(String.valueOf(typeid), new BaseMVPModel.CommotListener<List<Sys_Line_Manhole>>() {
            @Override
            public void result(List<Sys_Line_Manhole> sys_line_manholes) {
                if (getView() == null) {
                    return;
                }
                if (sys_line_manholes == null || sys_line_manholes.size() <= 0) {
                    getView().fail("未获取到选择数据");
                } else {
                    List<String> datas = new ArrayList<>();
                    for (Sys_Line_Manhole item : sys_line_manholes) {
                        datas.add(item.getValue());
                    }
                    czs = datas.toArray(new String[datas.size()]);
                    getView().queryCZ(czs);

                }
            }
        });
    }

    @Override
    public void queryType(final long typeid) {
        if (lineTypes != null) {
            getView().query_LineType(lineTypes);
            return;
        }
        queryModel.queryLineType(String.valueOf(typeid), new BaseMVPModel.CommotListener<List<Sys_LineType>>() {
            @Override
            public void result(List<Sys_LineType> sys_lineTypes) {

                if (getView() == null) {
                    return;
                }
                if (sys_lineTypes == null || sys_lineTypes.size() <= 0) {
                    getView().fail("未获取到选择数据");
                } else {
                    List<String> datas = new ArrayList<>();
                    for (Sys_LineType item : sys_lineTypes) {
                        datas.add(item.getName());
                    }
                    lineTypes = datas.toArray(new String[datas.size()]);
                    getView().query_LineType(lineTypes);

                }

            }
        });
    }

    @Override
    public void queryXX() {
        getView().queryXX(xx);
    }

    @Override
    public void queryMsfs(long typeid) {
        if (msfss != null) {
            getView().query_msfs(msfss);
            return;
        }
        queryModel.queryEmbedding(String.valueOf(typeid), new BaseMVPModel.CommotListener<List<Sys_Embedding>>() {
            @Override
            public void result(List<Sys_Embedding> sys_embeddings) {
                if (getView() == null) {
                    return;
                }
                if (sys_embeddings == null || sys_embeddings.size() <= 0) {
                    getView().fail("未获取到选择数据");
                } else {
                    List<String> datas = new ArrayList<>();
                    for (Sys_Embedding item : sys_embeddings) {
                        datas.add(item.getName());
                    }
                    msfss = datas.toArray(new String[datas.size()]);
                    getView().query_msfs(msfss);

                }
            }
        });

    }

    @Override
    public void query_remarks(long typeId) {
        getView().query_remarks(remarks);
    }

    @Override
    public void querySfly() {
        getView().querySfly(sfly);
    }

    public void queryUncertainData(final long typeId) {
        queryModel.queryUncertainData(String.valueOf(typeId), new BaseMVPModel.CommotListener<Map<String, Object>>() {
            @Override
            public void result(Map<String, Object> map) {
                if (getView() == null) {
                    return;
                }
                String[] tgcls = null;
                String[] pressures = null;
                String[] directions = null;
                if (map != null) {
                    List<Sys_TGCL> tgclsl = null;
                    List<Sys_Pressure> pressuresl = null;
                    List<Sys_Direction> directionsl = null;
                    Object o1 = map.get("datas1");
                    Object o2 = map.get("datas2");
                    Object o3 = map.get("datas3");
                    if (o1 != null) {
                        tgclsl = (List<Sys_TGCL>) o1;
                        List<String> datas = new ArrayList<>();
                        for (Sys_TGCL item : tgclsl) {
                            datas.add(item.getName());
                        }
                        tgcls = datas.toArray(new String[datas.size()]);
                    }
                    if (o2 != null) {
                        pressuresl = (List<Sys_Pressure>) o2;
                        List<String> datas = new ArrayList<>();
                        for (Sys_Pressure item : pressuresl) {
                            datas.add(item.getName());
                        }
                        pressures = datas.toArray(new String[datas.size()]);
                    }
                    if (o3 != null) {
                        directionsl = (List<Sys_Direction>) o3;
                        List<String> datas = new ArrayList<>();
                        for (Sys_Direction item : directionsl) {
                            datas.add(item.getName());
                        }
                        directions = datas.toArray(new String[datas.size()]);
                    }

                    getView().queryUncertainData(tgcls, pressures, directions);
                }
            }
        });
    }

    @Override
    public void queryTopType(final long project, final long type) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Line>() {

            @Override
            protected Tab_Line jobContent() throws Exception {
                List<Tab_Line> tab_line = LitPalUtils.selectorderWhere("updateTime DESC", Tab_Line.class, "projectId=? AND typeId = ?", String.valueOf(project), String.valueOf(type));
                if (tab_line != null && tab_line.size() > 0) {
                    return tab_line.get(0);
                }
                return null;
            }

            @Override
            protected void jobEnd(Tab_Line tabLine) {
                if (getView() != null) {
                    getView().queryTopType(tabLine);
                }
            }
        });
    }

    String[] status;

    public void queryUseStatus(final long typeId) {
        if (status != null) {
            getView().queryUseStatus(status);
            return;
        }

        queryModel.queryUseStatus(typeId, new BaseMVPModel.CommotListener<List<Sys_UseStatus>>() {
            @Override
            public void result(List<Sys_UseStatus> sys_useStatuses) {
                if (getView() == null) {
                    return;
                }
                if (sys_useStatuses == null || sys_useStatuses.size() <= 0) {
                    getView().fail("未获取到选择数据");
                } else {
                    List<String> datas = new ArrayList<>();
                    for (Sys_UseStatus item : sys_useStatuses) {
                        datas.add(item.getName());
                    }
                    status = datas.toArray(new String[datas.size()]);
                    getView().queryUseStatus(status);

                }
            }
        });
    }


    @Override
    public void queryStartAndEndMarer(final long projectId, final long typeId, final double start_latitude, final double start_longitude, final double end_latitude, final double end_longitude) {

        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, Object>>() {

            @Override
            protected Map<String, Object> jobContent() throws Exception {
                List<Tab_Marker> makers = LitPalUtils.selectWhere(Tab_Marker.class,
                        "projectId=? AND typeId=?", String.valueOf(projectId), String.valueOf(typeId));
                if (makers == null || makers.size() <= 0) {
                    return null;
                }

                LatLng start_latlng = new LatLng(start_latitude, start_longitude);
                LatLng end_latlng = new LatLng(end_latitude, end_longitude);

                Map<Long, Double> start_dimap = new LinkedHashMap<>();
                Map<Long, Double> end_dimap = new LinkedHashMap<>();
                for (Tab_Marker item : makers) {
                    LatLng item_latlng = new LatLng(item.getLatitude(), item.getLongitude());
                    double start_dis = DistanceUtil.getDistance(start_latlng, item_latlng);
                    double end_dis = DistanceUtil.getDistance(end_latlng, item_latlng);
                    start_dimap.put(item.getId(), start_dis);
                    end_dimap.put(item.getId(), end_dis);
                }
                start_dimap = sortMapByValue(start_dimap);
                end_dimap = sortMapByValue(end_dimap);

                long s_id = getFirstOrNull(start_dimap);
                long e_id = getFirstOrNull(end_dimap);

                double s_d = start_dimap.get(s_id);
                double e_d = end_dimap.get(e_id);

                Tab_Marker s_maker = null;
                Tab_Marker e_maker = null;
                int errorValue = 10;
                BaiduMap baiduMap = MapUtil.getmBaiduMap();
                if (baiduMap != null) {
                    MapStatus status = baiduMap.getMapStatus();
                    float now_zoom = status.zoom;
                    if (now_zoom >= 20) {
                        errorValue = 5;
                    } else if (now_zoom > 18) {
                        errorValue = 10;
                    } else if (now_zoom >= 10 && 18 >= now_zoom) {
                        errorValue = 50;
                    } else if (now_zoom < 10) {
                        errorValue = 200;
                    }
                }

                //距离误差在20m
                if (s_d < errorValue) {
                    s_maker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(s_id));
                }
                if (e_d < errorValue) {
                    e_maker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(e_id));
                }
                if (s_id == e_id) {//避免短线同名
                    e_maker = null;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("s", s_maker);
                params.put("e", e_maker);
                return params;
            }

            @Override
            protected void jobEnd(Map<String, Object> map) {
                if (getView() != null) {
                    if (map == null) {
                        getView().queryStartAndEndMarer(null, null);
                        return;
                    }
                    Tab_Marker s_maker = null;
                    Tab_Marker e_maker = null;
                    Object so = map.get("s");
                    Object eo = map.get("e");
                    if (so != null) {
                        s_maker = (Tab_Marker) so;
                    }
                    if (eo != null) {
                        e_maker = (Tab_Marker) eo;
                    }
                    getView().queryStartAndEndMarer(s_maker, e_maker);

                }
            }
        });

    }


    @Override
    public void queryShowType(final long type) {
//        getView().showPS(lx);

        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, Object>>() {
            @Override
            protected Map<String, Object> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(type));
                Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));
                List<Sys_Line_Data> l = LitPalUtils.selectWhere(Sys_Line_Data.class);
                Map<String, Object> dataMaps = new HashMap<>();

                Map<String, String> params = new HashMap<>();
                params.put("c", tables.getCode());
                params.put("f", f.getFatherCode());
                dataMaps.put("f", params);

                Map<String, List<String>> datam = new HashMap<>();
                for (Sys_Line_Data d : l) {
                    String key_name = d.getName();
                    if (!datam.containsKey(key_name)) {
                        datam.put(key_name, new ArrayList());
                    }
                    List<String> ls = datam.get(key_name);
                    ls.add(d.getValue());
                }
                dataMaps.put("m", datam);
                return dataMaps;
            }

            @Override
            protected void jobEnd(Map<String, Object> datam) {
                Map<String, String> code = (Map<String, String>) datam.get("f");
                Map<String, List<String>> dm = (Map<String, List<String>>) datam.get("m");

                sfly = toList(dm.get("是否利用"));
                xx = toList(dm.get("线型"));

                String c = code.get("c");
                String f = code.get("f");
                if (getView() != null) {
                    // TODO: 2019/6/24 数据填充
                    if (f.equals("PS")) {
                        String[] lx = toList(dm.get("流向"));
                        String[] dh = toList(dm.get("导虹管段"));
                        String[] ssyxzt = toList(dm.get("设施运行状态"));
                        String[] gxzl = toList(dm.get("管线质量"));
                        String[] gddj = toList(dm.get("管道等级"));
                        getView().visiblePS(lx, dh,ssyxzt,gxzl,gddj);
                    } else if (f.equals("DX")) {
                        getView().visibleDX();
                    } else if (f.equals("RQ")) {
                        String[] yl = toList(dm.get("压力"));
                        getView().visibleRQ(yl);
                    } else if (f.equals("BM")) {
//                        getView().visibleBM();
                    }
                    if (c.equals("GD")) {
                        String[] dy = toList(dm.get("电压"));
                        getView().visiblegd(dy);
                    }
                }
            }
        });
    }




    public static Map<Long, Double> sortMapByValue(Map<Long, Double> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Long, Double> sortedMap = new LinkedHashMap<Long, Double>();
        List<Map.Entry<Long, Double>> entryList = new ArrayList<Map.Entry<Long, Double>>(
                oriMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Long, Double>>() {
            @Override
            public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        Iterator<Map.Entry<Long, Double>> iter = entryList.iterator();
        Map.Entry<Long, Double> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    private <K, V> K getFirstOrNull(Map<K, V> map) {
        if (map == null || map.size() <= 0) return null;
        K obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

}
