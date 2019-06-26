package com.zt.map.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;

import com.zt.map.R;
import com.zt.map.constant.FileContract;
import com.zt.map.contract.MainContract;
import com.zt.map.entity.db.TaggingEntiiy;
import com.zt.map.entity.db.system.Sys_Color;
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
import cn.faker.repaymodel.util.DateUtils;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

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
    public void queryMarker(final long projectId, final String s) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Marker>() {

            @Override
            protected Tab_Marker jobContent() throws Exception {
                String value = s;
                if (!TextUtils.isEmpty(value)){
                    value = value.toUpperCase();
                }
                Tab_Marker tab_marker = LitPalUtils.selectsoloWhere(Tab_Marker.class, "projectId = ? AND wtdh=?", String.valueOf(projectId),value);
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
                ExcelUtil.initExcel(table_maker_title, filePath,m_fileName, table_maker_names);
                ExcelUtil.writeObjListToExcel(markers, makerPath, mContext);

                String l_fileName = table_line_title + ".xls";
                String linePath = filePath + "/" + l_fileName;
                ExcelUtil.initExcel(table_line_title, filePath,l_fileName, table_line_names);
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
                LitPalUtils.deleteData(Tab_Project.class,"id = ?",String.valueOf(id));
                LitPalUtils.deleteData(Tab_Marker.class,"projectId = ?",String.valueOf(id));
                LitPalUtils.deleteData(Tab_Line.class,"projectId = ?",String.valueOf(id));
                return true;
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                if (getView()!=null){
                    getView().delete_Project();
                }
            }
        });
    }


}
