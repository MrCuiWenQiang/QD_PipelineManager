package com.zt.map.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.zt.map.R;
import com.zt.map.constant.FileContract;
import com.zt.map.constant.URL;
import com.zt.map.contract.MainContract;
import com.zt.map.entity.CheckBean;
import com.zt.map.entity.db.TaggingEntiiy;
import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.system.Sys_RegisterInfo;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_Project;
import com.zt.map.model.MainModel;
import com.zt.map.model.SystemQueryModel;
import com.zt.map.util.out.AccessFileOut;
import com.zt.map.util.out.AccessUtil;
import com.zt.map.util.out.ExcelName;
import com.zt.map.util.out.ExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.net.json.JsonUtil;
import cn.faker.repaymodel.net.okhttp3.HttpHelper;
import cn.faker.repaymodel.net.okhttp3.callback.HttpResponseCallback;
import cn.faker.repaymodel.util.DateUtils;
import cn.faker.repaymodel.util.LogUtil;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;
import cn.faker.repaymodel.util.error.ErrorUtil;
import okhttp3.Call;
import okhttp3.Response;

public class MainPresenter extends BaseMVPPresenter<MainContract.View> implements MainContract.Presenter {
    private SystemQueryModel queryModel = new SystemQueryModel();
    private MainModel mainModel;

    private String[] typeNames;
    private Long[] typeIds;
    private Integer[] rgbs;

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        queryType();
        isregister();
    }
    @Override
    public void updateMaker(final Tab_Marker marker) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Boolean>() {

            @Override
            protected Boolean jobContent() throws Exception {
                int c = marker.update(marker.getId());
                return c>0;
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                queryMeasureProject(marker.getProjectId());
            }
        });
    }
    @Override
    public void queryType() {
        if (typeNames != null && typeIds != null) {
            getView().queryType(typeNames, typeIds, rgbs);
            return;
        }

        queryModel.queryType(new BaseMVPModel.CommotListener<List<Sys_Table>>() {
            @Override
            public void result(List<Sys_Table> sys_tables) {
                if (sys_tables == null && sys_tables.size() <= 0) {
                    return;
                }
                List<String> names = new ArrayList<>();
                List<Long> ids = new ArrayList<>();
                List<Integer> cs = new ArrayList<>();
                for (Sys_Table item : sys_tables) {
                    names.add(item.getCode());
                    ids.add(item.getId());
                    List<Sys_Color> colors = item.getColors();
                    if (colors != null && colors.size() > 0) {
                        Sys_Color c1 = colors.get(0);

                        int color = Color.rgb(Integer.valueOf(c1.getR()), Integer.valueOf(c1.getG()),
                                Integer.valueOf(c1.getB()));
                        cs.add(color);
                    } else {
                        cs.add(0xAAFF0000);
                    }
                }
                typeNames = names.toArray(new String[names.size()]);
                typeIds = ids.toArray(new Long[ids.size()]);
                rgbs = cs.toArray(new Integer[cs.size()]);
                if (getView() != null) {
                    getView().queryType(typeNames, typeIds, rgbs);
                }
            }
        });
    }

    @Override
    public void queryProjects() {
        if (mainModel == null) {
            mainModel = new MainModel();
        }
        mainModel.query_projects(new BaseMVPModel.CommotListener<List<Tab_Project>>() {
            @Override
            public void result(List<Tab_Project> tab_projects) {
                if (getView() == null) {
                    return;
                }
                if (tab_projects == null || tab_projects.size() < 0) {
                    getView().queryProjects_fail("暂时没有工程");
                    return;
                }
                getView().queryProjects(tab_projects);
            }
        });
    }

    @Override
    public void createProject(Editable name, boolean isPhoto) {
        if (mainModel == null) {
            mainModel = new MainModel();
        }
        if (TextUtils.isEmpty(name)) {
            getView().createProject_fail("请输入工程名称");
            return;
        }
        mainModel.createProject(name.toString(), isPhoto, new BaseMVPModel.CommotListener<BaseMVPModel.MessageEntity<Long>>() {
            @Override
            public void result(BaseMVPModel.MessageEntity<Long> messageEntity) {
                if (getView() == null) {
                    return;
                }
                if (messageEntity.isStatus()) {
                    getView().createProject_success(messageEntity.getMessage(), messageEntity.getData());
                } else {
                    getView().createProject_fail(messageEntity.getMessage());
                }
            }
        });
    }

    @Override
    public void queryProject(final long projectId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, Object>>() {

            @Override
            protected Map<String, Object> jobContent() {
                Map<String, Object> map = new HashMap<>();
                List<Tab_Marker> markers = LitPalUtils.selectWhere(Tab_Marker.class, "projectId = ? order by updateTime", String.valueOf(projectId));
                List<Tab_Line> lines = LitPalUtils.selectWhere(Tab_Line.class, "projectId = ?", String.valueOf(projectId));
                for (Tab_Line item : lines) {
                    item.getColor();
                }
                for (Tab_Marker item :
                        markers) {
                    item.getIconBase();
                    item.getSys_color();
                }
                map.put("Tab_Marker", markers);
                map.put("Tab_Line", lines);
                return map;
            }

            @Override
            protected void jobEnd(Map<String, Object> map) {
                if (getView() == null) {
                    return;
                }
                List<Tab_Marker> markers = null;
                List<Tab_Line> lines = null;
                Object o_m = map.get("Tab_Marker");
                Object o_l = map.get("Tab_Line");
                if (o_m != null) {
                    markers = (List<Tab_Marker>) o_m;
                }
                if (o_l != null) {
                    lines = (List<Tab_Line>) o_l;
                }
                getView().queryProject(markers, lines, projectId);
            }
        });
    }
    @Override
    public void queryMeasureProject(final long projectId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Tab_Marker>>() {

            @Override
            protected List<Tab_Marker>  jobContent() {
                List<Tab_Marker> markers = LitPalUtils.selectWhere(Tab_Marker.class, "projectId = ? order by updateTime", String.valueOf(projectId));
                for (Tab_Marker item :
                        markers) {
                    item.getIconBase();
                    if (TextUtils.isEmpty(item.getCldh())){
                        item.getSys_color();
                    }else {
                        Sys_Color sys_color = new Sys_Color();
                        sys_color.setR("128");
                        sys_color.setG("128");
                        sys_color.setB("128");
                        item.setColor(sys_color);
                    }
                }
                return markers;
            }

            @Override
            protected void jobEnd(List<Tab_Marker> datas) {
                if (getView()!=null){
                    getView().queryMeasure(datas);
                }
            }
        });
    }
    @Override
    public void delete(final long id, final int type) {
        if (id < 0) {
            getView().delete_fail("请选择正确点线");
            return;
        }
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Boolean>() {

            @Override
            protected Boolean jobContent() throws Exception {
                boolean status = false;
                if (type == 1) {
                    Tab_Marker marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(id));
                    if (marker != null) {
                        status = marker.delete() > 0;
                    }
                } else if (type == 2) {
                    Tab_Line line = LitPalUtils.selectsoloWhere(Tab_Line.class, "id = ?", String.valueOf(id));
                    if (line != null) {
                        status = line.delete() > 0;
                    }
                }
                return status;
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                if (getView() == null) {
                    return;
                }
                if (aBoolean) {
                    getView().delete_success("删除成功", type);
                } else {
                    getView().delete_fail("删除失败");
                }
            }
        });


    }

    @Override
    public void update_MakerLocal(long makerId, double latitude, double longitude) {
        mainModel.update_MakerLocal(makerId, latitude, longitude, new BaseMVPModel.CommotListener<Boolean>() {
            @Override
            public void result(Boolean aBoolean) {
                if (getView() != null) {
                    getView().update();
                }
            }
        });
    }

    private double value = 0.00009;

    @Override
    public void queryTagger(final long projectId, final int which) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<TaggingEntiiy>>() {

            @Override
            protected List<TaggingEntiiy> jobContent() throws Exception {
                if (which == 0) {
                    return null;
                }
                List<Tab_Line> tab_line = LitPalUtils.selectWhere(Tab_Line.class, "projectId =?", String.valueOf(projectId));
                if (tab_line != null) {
                    List<TaggingEntiiy> taggings = new ArrayList<>();
                    for (Tab_Line item : tab_line) {
                        item.getColor();
                        TaggingEntiiy startEntiiy = new TaggingEntiiy();

                        switch (which) {
                            case 1: {
                                TaggingEntiiy endEntity = new TaggingEntiiy();
                                endEntity.setLatitude(item.getEnd_latitude());
                                endEntity.setLongitude(item.getEnd_longitude() - value);

                                startEntiiy.setValue(item.getQdms());
                                endEntity.setValue(item.getZzms());
                                startEntiiy.setLatitude(item.getStart_latitude());
                                startEntiiy.setLongitude(item.getStart_longitude() + value);
                                endEntity.setColor(item.getColor());

                                taggings.add(endEntity);
                                break;
                            }
                            case 2: {
                                startEntiiy.setValue(item.getGxcl());
                                break;
                            }
                            case 3: {
                                startEntiiy.setValue(item.getGjdm());
                                break;
                            }
                        }

                        if (which != 1) {
                            double x = (item.getStart_longitude() + item.getEnd_longitude()) / 2;
                            double y = (item.getStart_latitude() + item.getEnd_latitude()) / 2;
                            startEntiiy.setLatitude(y);
                            startEntiiy.setLongitude(x);
                        }

                        startEntiiy.setColor(item.getColor());
                        taggings.add(startEntiiy);
                    }
                    return taggings;
                }
                return null;
            }

            @Override
            protected void jobEnd(List<TaggingEntiiy> taggingEntiiys) {
                getView().taggers(taggingEntiiys);
            }
        });
    }
    @Override
    public void queryMarker(final long id) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Marker>() {

            @Override
            protected Tab_Marker jobContent() throws Exception {
                Tab_Marker tab_marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "id = ?", String.valueOf(id));
                return tab_marker;
            }

            @Override
            protected void jobEnd(Tab_Marker marker) {
                getView().queryMarker(marker);
            }
        });
    }
    @Override
    public void queryMarker(final long projectId, final String s) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Marker>() {

            @Override
            protected Tab_Marker jobContent() throws Exception {
                String value = s;
                if (!TextUtils.isEmpty(value)) {
                    value = value.toUpperCase();
                }
                Tab_Marker tab_marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "projectId = ? AND wtdh=?", String.valueOf(projectId), value);
                return tab_marker;
            }

            @Override
            protected void jobEnd(Tab_Marker marker) {
                getView().queryMarker(marker);
            }
        });


    }


    private String filePath = FileContract.getOutFilePath();

    @Override
    public void outExcel(final Long projectId, final Context mContext) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Object>() {

            @Override
            protected Object jobContent() throws Exception {
                Tab_Project project = LitPalUtils.selectsoloWhere(Tab_Project.class, "id = ?", String.valueOf(projectId));
                List<Tab_Marker> markers = LitPalUtils.selectWhere(Tab_Marker.class, "projectId=?", String.valueOf(projectId));
                List<Tab_Line> lines = LitPalUtils.selectWhere(Tab_Line.class, "projectId=?", String.valueOf(projectId));
                if ((markers == null || markers.size() <= 0)
                        && (lines == null || lines.size() <= 0)) {
                    return "导出失败:该工程没有管点数据和管线数据";
                }

                String table_maker_title = ExcelUtil.tableTitle(Tab_Marker.class);
                String table_line_title = ExcelUtil.tableTitle(Tab_Line.class);

                table_maker_title = project.getName() + table_maker_title + DateUtils.getCurrentDateTime();
                table_line_title = project.getName() + table_line_title + DateUtils.getCurrentDateTime();

                String[] table_maker_names = ExcelUtil.colName(Tab_Marker.class);
                String[] table_line_names = ExcelUtil.colName(Tab_Line.class);
                String m_fileName = table_maker_title + ".xls";
                String makerPath = filePath + "/" + m_fileName;
                ExcelUtil.initExcel(table_maker_title, filePath, m_fileName, table_maker_names);
                ExcelUtil.writeObjListToExcel(markers, makerPath, mContext);

                String l_fileName = table_line_title + ".xls";
                String linePath = filePath + "/" + l_fileName;
                ExcelUtil.initExcel(table_line_title, filePath, l_fileName, table_line_names);
                ExcelUtil.writeObjListToExcel(lines, linePath, mContext);

                return "文件已导出 ,点击查看文件";
            }

            @Override
            protected void jobEnd(Object data) {
//                listener.result(tab_projects);
                if (getView() != null && data != null) {
                    getView().outExcel((String) data);
                }
            }
        });
    }

    @Deprecated
    @Override
    public void outAccess(final Long projectId, final Context mContext) {

    }

    @Override
    public void delete_Project(final long id) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Boolean>() {

            @Override
            protected Boolean jobContent() throws Exception {
                LitPalUtils.deleteData(Tab_Project.class, "id = ?", String.valueOf(id));
                LitPalUtils.deleteData(Tab_Marker.class, "projectId = ?", String.valueOf(id));
                LitPalUtils.deleteData(Tab_Line.class, "projectId = ?", String.valueOf(id));
                return true;
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                if (getView() != null) {
                    getView().delete_Project();
                }
            }
        });
    }

    @Override
    public void isregister() {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, Object>>() {

            @Override
            protected Map<String, Object> jobContent() throws Exception {
                Map<String, Object> params = new HashMap<>();
                boolean isApplen = false;
                Sys_RegisterInfo info = LitPalUtils.selectsoloWhere(Sys_RegisterInfo.class);
                isApplen = (info != null);
                if (isApplen) {
                    if (!info.getRegister()) {
                        //添加验证
                        Call call = HttpHelper.getPostCall(URL.CHECK, info);
                        try {
                            Response response = call.execute();


                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            JSONObject jsonObject = JSONObject.parseObject(json);

                            CheckBean d = JsonUtil.convertJsonToObject(jsonObject.get("data").toString(), CheckBean.class);
                            isApplen = (d.getType() == 0);
                            if (!isApplen) {
                                params.put("msg", d.getMsg());
                                params.put("type", d.getType());
                            }
                        }else {
                            params.put("type", 404);//超时
                            params.put("msg", "账户校验超时,请保持网络通畅");
                        }
                        }catch (Exception e){
                            ErrorUtil.showError(e);
                            isApplen = false;
                            params.put("type", 404);//超时
                            params.put("msg", "账户校验超时,请保持网络通畅");
                        }
                    }
                }
                params.put("status", isApplen);
                return params;
            }

            @Override
            protected void jobEnd(Map<String, Object> aBoolean) {
                if (getView() == null) return;
                boolean status = (boolean) aBoolean.get("status");
                if (status) {
                    getView().registerSuccess();
                } else {
                    String msg = null;
                    int type = 0;
                    if (aBoolean.containsKey("type")){
                        type= (int) aBoolean.get("type");
                    }
                    if (type != 4) {
                        msg = (String) aBoolean.get("msg");
                    }
                    getView().shoWregister(msg);
                }
            }
        });
    }

    @Override
    public void toregister(final Sys_RegisterInfo sys_registerInfo) {
        HttpHelper.post(URL.APPLY, sys_registerInfo, new HttpResponseCallback() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onMessage(String msg) {
                LitPalUtils.deleteData(Sys_RegisterInfo.class);
                sys_registerInfo.save();
                if (getView() == null) return;

                getView().finsh(msg);
            }

            @Override
            public void onFailed(int status, String message) {
                if (getView() == null) return;
                getView().fail(message);
            }
        });
    }


}
