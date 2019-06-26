package com.zt.map.presenter;

import com.zt.map.contract.MarkerContract;
import com.zt.map.entity.db.PhotoInfo;
import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_LineType;
import com.zt.map.entity.db.system.Sys_Manhole;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_Marker_Count;
import com.zt.map.entity.db.tab.Tab_Project;
import com.zt.map.entity.db.tab.Tab_marker_photo;
import com.zt.map.model.MarkerModel;
import com.zt.map.model.SystemQueryModel;
import com.zt.map.util.out.ExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class MarkerPresenter extends BaseMVPPresenter<MarkerContract.View> implements MarkerContract.Presenter {

    private SystemQueryModel queryModel = new SystemQueryModel();
    private MarkerModel markerModel = new MarkerModel();

    private String[] tzds;
    private String[] fsws;

    @Override
    public void save(final Tab_Marker tab, final List<PhotoInfo> resultList) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity>() {

            @Override
            protected BaseMVPModel.MessageEntity jobContent() throws Exception {
                if (tab.getId() <= 0) {
                    boolean isHave = LitPalUtils.selectCount(Tab_Marker.class,
                            "projectId = ? AND wtdh=?", String.valueOf(tab.getProjectId()), tab.getWtdh()) > 0;
                    if (isHave) {
                        return BaseMVPModel.MessageEntity.fail("点号重复,请重新填写");
                    }
                    Tab_Marker_Count count = LitPalUtils.selectsoloWhere(Tab_Marker_Count.class, "projectId = ? AND typeId=?",
                            String.valueOf(tab.getProjectId()), String.valueOf(tab.getTypeId()));
                    if (count == null) {
                        count = new Tab_Marker_Count();
                    }
                    count.setCount(count.getCount() + 1);
                    count.setProjectId(tab.getProjectId());
                    count.setTypeId(tab.getTypeId());
                    count.save();
                }

                boolean status = tab.save();
                long makerId = tab.getId();
                long projectId = tab.getProjectId();
                int count = LitPalUtils.selectCount(Tab_marker_photo.class, "markerId=? AND projectId=?", String.valueOf(makerId),
                        String.valueOf(projectId));

                if (resultList != null && status) {
                    List<Tab_marker_photo> pos = new ArrayList<>();
                    int i = 1;
                    for (PhotoInfo info : resultList) {
                        Tab_marker_photo p = new Tab_marker_photo();
                        p.setMarkerId(makerId);
                        p.setProjectId(projectId);
                        p.setName(tab.getWtdh() + "_" + (count + i));
                        p.setPath(info.getPath());
                        pos.add(p);
                        i++;
                    }
                    LitPalUtils.saveAll(pos);
                }

                return BaseMVPModel.MessageEntity.success();
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity msg) {
                if (getView() == null) {
                    return;
                }
                if (msg.isStatus()) {
                    getView().save_success();
                } else {
                    getView().save_Fail(msg.getMessage());
                }
            }
        });
    }

    @Override
    public void queryMarker(long id) {
        if (markerModel == null) {
            markerModel = new MarkerModel();
        }
        markerModel.queryMarker(id, new BaseMVPModel.CommotListener<Tab_Marker>() {
            @Override
            public void result(Tab_Marker marker) {
                if (getView() == null) {
                    return;
                }
                if (marker != null) {
                    getView().queryMarker_success(marker);
                } else {
                    getView().queryMarker_fail("未查询到该管点");
                }
            }
        });
    }

    @Override
    public void queryTopType(final long project, final long typeId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Tab_Marker>() {

            @Override
            protected Tab_Marker jobContent() throws Exception {
                List<Tab_Marker> tab_line = LitPalUtils.selectorderWhere("updateTime DESC", Tab_Marker.class, "projectId=? AND typeId = ?", String.valueOf(project), String.valueOf(typeId));
                if (tab_line != null && tab_line.size() > 0) {
                    return tab_line.get(0);
                }
                return null;
            }

            @Override
            protected void jobEnd(Tab_Marker tabLine) {
                if (getView() != null) {
                    getView().queryTopType(tabLine);
                }
            }
        });
    }

    @Override
    public void getName(final long project, final long typeId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<String>() {

            @Override
            protected String jobContent() throws Exception {
                Sys_Table table = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(typeId));
                StringBuilder name = new StringBuilder();
                name.append(table.getCode());
           /*     int count = LitPalUtils.selectCount(Tab_Marker.class, "projectId=? AND typeId=?"
                        , String.valueOf(project), String.valueOf(typeId));*/


                Tab_Marker_Count count = LitPalUtils.selectsoloWhere(Tab_Marker_Count.class, "projectId = ? AND typeId=?", String.valueOf(project), String.valueOf(typeId));

                if (count != null) {
                    name.append(count.getCount() + 1);
                } else {
                    name.append(1);
                }
                return name.toString();
            }

            @Override
            protected void jobEnd(String s) {
                if (getView() != null) {
                    getView().getName(s);
                }
            }
        });
    }

    @Override
    public void insertMarker(final Tab_Marker tab, final long lineId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity>() {

            @Override
            protected BaseMVPModel.MessageEntity jobContent() throws Exception {
                //冗余代码
                boolean isHave = LitPalUtils.selectCount(Tab_Marker.class,
                        "projectId = ? AND wtdh=?", String.valueOf(tab.getProjectId()), tab.getWtdh()) > 0;
                if (isHave) {
                    return BaseMVPModel.MessageEntity.fail("点号重复,请重新填写");
                }
                Tab_Marker_Count count = LitPalUtils.selectsoloWhere(Tab_Marker_Count.class, "projectId = ? AND typeId=?",
                        String.valueOf(tab.getProjectId()), String.valueOf(tab.getTypeId()));
                if (count == null) {
                    count = new Tab_Marker_Count();
                }
                count.setCount(count.getCount() + 1);
                count.setProjectId(tab.getProjectId());
                count.setTypeId(tab.getTypeId());
                count.save();

                boolean status = false;
                status = tab.save();
                long new_MarkerId = tab.getId();

                Tab_Line line = LitPalUtils.selectsoloWhere(Tab_Line.class, "id = ?", String.valueOf(lineId));
                Tab_Line endline = new Tab_Line();
                ExcelUtil.CloneAttribute(line, endline);

                endline.setId(0);
                endline.setStartMarkerId(new_MarkerId);
                endline.setStart_latitude(tab.getLatitude());
                endline.setStart_longitude(tab.getLongitude());
                endline.setQswh(tab.getWtdh());

                endline.setEndMarkerId(line.getEndMarkerId());
                endline.setEnd_latitude(line.getEnd_latitude());
                endline.setEnd_longitude(line.getEnd_longitude());
                endline.setZzwh(line.getZzwh());
//                endline.setProjectId(line.getProjectId());//???未传递projectid
                status = endline.save();


                line.setEndMarkerId(new_MarkerId);
                line.setEnd_latitude(tab.getLatitude());
                line.setEnd_longitude(tab.getLongitude());
                line.setZzwh(tab.getWtdh());

                status = line.save();


                return BaseMVPModel.MessageEntity.success();
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity msg) {
                if (getView() == null) {
                    return;
                }
                if (msg.isStatus()) {
                    getView().save_success();
                } else {
                    getView().save_Fail(msg.getMessage());
                }
            }
        });
    }

    @Override
    public void queryIsPhoto(final long projectId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Boolean>() {

            @Override
            protected Boolean jobContent() throws Exception {
                Tab_Project project = LitPalUtils.selectsoloWhere(Tab_Project.class, "id = ?", String.valueOf(projectId));
                if (project != null) {
                    return project.isCreatePhoto();
                } else {
                    return false;
                }
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                if (getView() != null && aBoolean) {
                    getView().createPhoto();
                }
            }
        });
    }

    public void queryProject(final long projectId) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<String>() {

            @Override
            protected String jobContent() throws Exception {
                Tab_Project project = LitPalUtils.selectsoloWhere(Tab_Project.class, "id=?", String.valueOf(projectId));
                return project == null ? "未知" : project.getName();
            }

            @Override
            protected void jobEnd(String s) {
                getView().queryProjectName(s);
            }
        });
    }

    String[] status;

    //使用状态
    @Override
    public void queryUseStatus() {
        if (status != null && status.length > 0) {
            getView().queryUseStatus(status);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    String[] manholes;
    String[] tcfss;

    //井盖材质
    @Override
    public void querymanhole() {
        if (manholes != null && manholes.length > 0) {
            getView().querymanhole(manholes);
            return;
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    public void querytcfs() {
        if (tcfss != null && tcfss.length > 0) {
            getView().querytcfss(tcfss);
            return;
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    @Override
    public void query_tzd() {
        if (tzds != null && tzds.length > 0) {
            getView().query_tzd(tzds);
            return;
        } else {
            getView().fail("未获取到选择数据");
        }

    }
    private String[] sjly;
    private String[] sfly;


    @Override
    public void query_fsw() {
        if (fsws != null && fsws.length > 0) {
            getView().query_fsw(fsws);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    String[] jgzts;

    @Override
    public void queryJGZTs() {
        if (jgzts != null && jgzts.length > 0) {
            getView().queryJGZTs(jgzts);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    String[] jglx;
    String[] szwz;

    @Override
    public void queryJGLXs() {
        if (jglx != null && jglx.length > 0) {
            getView().queryJGLXs(jglx);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    @Override
    public void querysjly() {
        if (sjly != null && sjly.length > 0) {
            getView().querysjly(sjly);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    @Override
    public void querysfly() {
        if (sfly != null && sfly.length > 0) {
            getView().querysfly(sfly);
        } else {
            getView().fail("未获取到选择数据");
        }
    }

    public void querySZWZ() {
        if (szwz != null && szwz.length > 0) {
            getView().queryszwz(szwz);
        } else {
            getView().fail("未获取到选择数据");
        }
    }
    @Override
    public void queryVisible(final long typeId) {
        markerModel.queryTable(typeId, new BaseMVPModel.CommotListener<Map<String, String[]>>() {
            @Override
            public void result(Map<String, String[]> dataMap) {
                status = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_STATUS);
                manholes = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_JGCZS);
                tcfss = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_TCFS);
                tzds = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_TZDS);
                fsws = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_FSWS);
                jgzts = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_JGZTS);
                jglx = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_JGLX);
                szwz = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_SZWZ);
                sjly = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_SJLY);
                sfly = dataMap.get(MarkerModel.TABLE_MARKER_TYPE_SFLY);
                if (getView()==null) return;
                if (dataMap.containsKey(MarkerModel.TYPE_MARKER_FATHER_PS)) {
                    getView().visiblePS();
                }
                getView().queryVisibleSuccess();
            }
        });
    }


}
