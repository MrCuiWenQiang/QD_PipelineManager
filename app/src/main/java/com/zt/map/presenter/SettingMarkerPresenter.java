package com.zt.map.presenter;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;

import com.zt.map.contract.SettingContract;
import com.zt.map.contract.SettingMarkerContract;
import com.zt.map.entity.TreeBean;
import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_Jglx;
import com.zt.map.entity.db.system.Sys_Jgzt;
import com.zt.map.entity.db.system.Sys_Manhole;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.entity.db.system.Sys_szwz;
import com.zt.map.entity.db.system.Sys_tcfs;
import com.zt.map.model.MarkerModel;
import com.zt.map.model.SystemQueryModel;
import com.zt.map.util.Base64Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class SettingMarkerPresenter extends BaseMVPPresenter<SettingMarkerContract.View> implements SettingMarkerContract.Presenter {
    private SystemQueryModel queryModel = new SystemQueryModel();
    private MarkerModel markerModel = new MarkerModel();

    private String[] typeNames;
    private Long[] typeIds;

    @Override
    public void queryType() {
        if (typeNames != null && typeIds != null) {
            getView().queryType(typeNames, typeIds);
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
                for (Sys_Table item : sys_tables) {
                    names.add(item.getCode());
                    ids.add(item.getId());
                }
                typeNames = names.toArray(new String[names.size()]);
                typeIds = ids.toArray(new Long[ids.size()]);
                if (getView() != null) {
                    getView().queryType(typeNames, typeIds);
                }
            }
        });
    }

    @Override
    public void queryData(long typeId) {
        markerModel.queryTable(typeId, new BaseMVPModel.CommotListener<Map<String, String[]>>() {
            @Override
            public void result(Map<String, String[]> dataMap) {
                Map<String, List<String>> map = new LinkedHashMap<>();

                for (String tabName : MarkerModel.tabs) {
                    boolean isHave = dataMap.containsKey(tabName);
                    if (isHave) {
                        String[] co = dataMap.get(tabName);
                        List list = null;
                        if (co != null) {
                            list = Arrays.asList(co);
                        }
                        map.put(tabName, list);
                    }
                }
                if (getView() == null) return;
                getView().queryDb(map);
            }
        });
    }

    @Override
    public void saveOrRevise(final String typeCode, Editable nowValue, final String fatherName, String itemName, int position) {
        String value = null;
        if (nowValue == null) {
            getView().openFail("选项不能为空");
            return;
        } else {
            value = nowValue.toString();
        }
        if (TextUtils.isEmpty(value)) {
            getView().openFail("选项不能为空");
            return;
        }

        long id = 0;
        if (TextUtils.isEmpty(itemName) && position == -1) {//新增
            id = -1;
        } else {
            id = markerModel.getIds(fatherName, itemName);
        }
        final long finalId = id;
        final String finalValue = value;
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity<String>>() {

            @Override
            protected BaseMVPModel.MessageEntity<String> jobContent() throws Exception {
                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGCZS)) {
                    Sys_Manhole manhole = new Sys_Manhole();
                    manhole.setId(finalId);
                    manhole.setFatherCode(String.valueOf(typeCode));
                    manhole.setName(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGLX)) {
                    Sys_Jglx manhole = new Sys_Jglx();
                    manhole.setId(finalId);
                    manhole.setName(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGZTS)) {
                    Sys_Jgzt manhole = new Sys_Jgzt();
                    manhole.setId(finalId);
                    manhole.setName(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_STATUS)) {
                    Sys_UseStatus manhole = new Sys_UseStatus();
                    manhole.setId(finalId);
                    manhole.setFatherCode(String.valueOf(typeCode));
                    manhole.setName(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_SZWZ)) {
                    Sys_szwz manhole = new Sys_szwz();
                    manhole.setId(finalId);
                    manhole.setValue(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TCFS)) {
                    Sys_tcfs manhole = new Sys_tcfs();
                    manhole.setId(finalId);
                    manhole.setValue(finalValue);
                    if (finalId > 0) {
                        manhole.update(finalId);
                    } else {
                        manhole.save();
                    }
                }
                return BaseMVPModel.MessageEntity.success();
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity<String> stringMessageEntity) {
                if (getView() != null) {
                    getView().openSuccess();
                }
            }
        });
    }


    public void saveOrRevise(final String typeCode, Editable nowValue,
                             final String icon, String iconId, final String fatherName, String itemName, int position) {
        String value = null;
        if (nowValue == null) {
            getView().openFail("选项不能为空");
            return;
        } else {
            value = nowValue.toString();
        }
        if (TextUtils.isEmpty(value)) {
            getView().openFail("选项不能为空");
            return;
        }

        long id = 0;
        if (TextUtils.isEmpty(itemName) && position == -1) {//新增
            id = -1;
        } else {
            id = markerModel.getIds(fatherName, itemName);
        }

        final long finalId = id;
        final String finalValue = value;

        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback() {
            @Override
            protected Object jobContent() throws Exception {

                Sys_Type_Child f = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "value = ?", typeCode);

                String fcode = f.getFatherCode();
                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TZDS)) {
                    Sys_Features features = LitPalUtils.selectsoloWhere(Sys_Features.class, "fatherCode = ? And name=?", fcode,icon);

                    Sys_Features sys_features = new Sys_Features();
                    sys_features.setIcon(features.getIcon());
                    sys_features.setName(finalValue);
                    sys_features.setFatherCode(fcode);
                    if (finalId > 0) {
                        sys_features.update(finalId);
                    } else {
                        sys_features.save();
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_FSWS)) {
                    Sys_Appendages appendages = LitPalUtils.selectsoloWhere(Sys_Appendages.class, "fatherCode = ? And name =?", fcode,icon);

                    Sys_Appendages sys_features = new Sys_Appendages();
                    sys_features.setIcon(appendages.getIcon());
                    sys_features.setName(finalValue);
                    sys_features.setFatherCode(fcode);
                    if (finalId > 0) {
                        sys_features.update(finalId);
                    } else {
                        sys_features.save();
                    }
                }
                return null;
            }

            @Override
            protected void jobEnd(Object o) {
                if (getView() != null) {
                    getView().openSuccess();
                }
            }
        });

    }

    @Override
    public void delete(final String fatherName, final String itemName, int position) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity<String>>() {

            @Override
            protected BaseMVPModel.MessageEntity<String> jobContent() throws Exception {
                long id = markerModel.getIds(fatherName, itemName);
                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGCZS)) {
                    int count = LitPalUtils.deleteData(Sys_Manhole.class, "id = ?", String.valueOf(id));

                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGLX)) {
                    int count = LitPalUtils.deleteData(Sys_Jglx.class, "id = ?", String.valueOf(id));

                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_JGZTS)) {
                    int count = LitPalUtils.deleteData(Sys_Jgzt.class, "id = ?", String.valueOf(id));

                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_STATUS)) {
                    int count = LitPalUtils.deleteData(Sys_UseStatus.class, "id = ?", String.valueOf(id));
                }else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TZDS)) {
                    int count = LitPalUtils.deleteData(Sys_Features.class, "id = ?", String.valueOf(id));
                }else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_FSWS)) {
                    int count = LitPalUtils.deleteData(Sys_Appendages.class, "id = ?", String.valueOf(id));
                }else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_SZWZ)) {
                    int count = LitPalUtils.deleteData(Sys_szwz.class, "id = ?", String.valueOf(id));
                }else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TCFS)) {
                    int count = LitPalUtils.deleteData(Sys_tcfs.class, "id = ?", String.valueOf(id));
                }
                return BaseMVPModel.MessageEntity.success();
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity<String> stringMessageEntity) {
                if (getView() != null) {
                    getView().openSuccess();
                }
            }
        });

    }

    private Map<String, Long> iconIdMap;

    @Override
    public void queryIcons(final long typeId, final String fatherName, final String itemName, final int position) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity<Map<String, Bitmap>>>() {

            @Override
            protected BaseMVPModel.MessageEntity<Map<String, Bitmap>> jobContent() throws Exception {
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(typeId));
                String code = tables.getCode();
                Sys_Type_Child child = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "value = ?", code);

                Map<String, Bitmap> values = new HashMap<>();
                if (iconIdMap == null) {
                    iconIdMap = new LinkedHashMap<>();
                }
                iconIdMap.clear();

                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_FSWS)) {
                    List<Sys_Appendages> appendages = LitPalUtils.selectWhere(Sys_Appendages.class, " fatherCode = ?", child.getFatherCode());
                    for (Sys_Appendages item : appendages) {
                        Bitmap icon = Base64Util.stringtoBitmap(item.getIcon());
                        values.put(item.getName(), icon);
                        iconIdMap.put(item.getName(), item.getId());
                    }
                } else if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TZDS)) {
                    List<Sys_Features> features = LitPalUtils.selectWhere(Sys_Features.class, "  fatherCode = ?", child.getFatherCode());
                    for (Sys_Features item : features) {
                        Bitmap icon = Base64Util.stringtoBitmap(item.getIcon());
                        values.put(item.getName(), icon);
                        iconIdMap.put(item.getName(), item.getId());
                    }
                }
                BaseMVPModel.MessageEntity<Map<String, Bitmap>> d = BaseMVPModel.MessageEntity.success();
                d.setData(values);
                return d;
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity<Map<String, Bitmap>> mapCommotListener) {
                if (getView() != null) {
                    getView().queryIcons(mapCommotListener.getData(),fatherName, itemName,  position);
                }
            }
        });


    }


}
