package com.zt.map.presenter;

import android.text.Editable;
import android.text.TextUtils;

import com.zt.map.contract.SettingLineContract;
import com.zt.map.entity.db.system.Sys_Embedding;
import com.zt.map.entity.db.system.Sys_Line_Data;
import com.zt.map.entity.db.system.Sys_Line_Manhole;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.model.SystemQueryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;


public class SettingLinePresenter extends BaseMVPPresenter<SettingLineContract.View> implements SettingLineContract.Presenter {

    private SystemQueryModel queryModel = new SystemQueryModel();

    private String[] typeNames;
    private Long[] typeIds;

    public final static String TABLE_LINE_TYPE_STATUS = "材质";
    public final static String TABLE_LINE_TYPE_MSFS = "埋设方式";
    public final static String TABLE_LINE_TYPE_SSZT = "使用状态";
    public final static String TABLE_LINE_TYPE_LX = "流向";
    public final static String TABLE_LINE_TYPE_DHGD = "导虹管段";
    public final static String TABLE_LINE_TYPE_YXZT = "设施运行状态";
    public final static String TABLE_LINE_TYPE_GXZL = "管线质量";
    public final static String TABLE_LINE_TYPE_GDDJ = "管道等级";
    public final static String TABLE_LINE_TYPE_YL = "压力";
    public final static String TABLE_LINE_TYPE_DY = "电压";
    public final static String TABLE_LINE_TYPE_XX = "线型";
    public final static String TABLE_LINE_TYPE_SFLY = "是否利用";

    Map<String, Map<String, Long>> idds = new HashMap<>();//用于存放数据对应id 通过get方法获取选择项的id

    @Override
    public void attachView(SettingLineContract.View view) {
        super.attachView(view);
        queryType();
    }

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
    public void queryData(final long type) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Map<String, List<String>>>() {
            @Override
            protected Map<String, List<String>> jobContent() throws Exception {
                if (idds != null) {
                    idds.clear();
                }
                Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "id = ?", String.valueOf(type));
                Sys_Type_Child df = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));
                List<Sys_Line_Data> l = LitPalUtils.selectWhere(Sys_Line_Data.class);
                //cz
                List<Sys_Line_Manhole> manholes = LitPalUtils.selectWhere(Sys_Line_Manhole.class, "name = ?", df.getFatherCode());
                //埋设方式
                List<Sys_Embedding> embeddings = LitPalUtils.selectWhere(Sys_Embedding.class, "fatherCode = ?", tables.getCode());
                //使用状态
                List<Sys_UseStatus> statuses = LitPalUtils.selectWhere(Sys_UseStatus.class, "fatherCode = ?", tables.getCode());
                Map<String, List<String>> dm = new HashMap<>();

                for (Sys_Line_Data d : l) {
                    String key_name = d.getName();
                    if (!dm.containsKey(key_name)) {
                        dm.put(key_name, new ArrayList());
                    }
                    List<String> ls = dm.get(key_name);
                    ls.add(d.getValue());

                    if (!idds.containsKey(key_name)) {
                        idds.put(key_name, new HashMap<String, Long>());
                    }
                    Map<String, Long> sda = idds.get(key_name);
                    sda.put(d.getValue(), d.getId());
                }

                String c = tables.getCode();
                String f = df.getFatherCode();

                Map<String, List<String>> dataMap = new HashMap<>();
                Map<String, Long> manholeMap = new HashMap<>();
                List<String> mas = new ArrayList<>();
                for (Sys_Line_Manhole item : manholes) {
                    manholeMap.put(item.getValue(), item.getId());
                    mas.add(item.getValue());
                }
                Map<String, Long> embeddingMap = new HashMap<>();
                List<String> ems = new ArrayList<>();
                for (Sys_Embedding item : embeddings) {
                    embeddingMap.put(item.getName(), item.getId());
                    ems.add(item.getName());
                }
                Map<String, Long> useStatusMap = new HashMap<>();
                List<String> uss = new ArrayList<>();
                for (Sys_UseStatus item : statuses) {
                    useStatusMap.put(item.getName(), item.getId());
                    uss.add(item.getName());
                }
                dataMap.put(TABLE_LINE_TYPE_STATUS, mas);
                dataMap.put(TABLE_LINE_TYPE_MSFS, ems);
                dataMap.put(TABLE_LINE_TYPE_SSZT, uss);

                idds.put(TABLE_LINE_TYPE_STATUS, manholeMap);
                idds.put(TABLE_LINE_TYPE_MSFS, embeddingMap);
                idds.put(TABLE_LINE_TYPE_SSZT, useStatusMap);

                dataMap.put(TABLE_LINE_TYPE_SFLY, dm.get("是否利用"));
                dataMap.put(TABLE_LINE_TYPE_XX, dm.get("线型"));

                // TODO: 2019/6/24 管线最麻烦的 同样的逻辑要修改两份  一是填数据 二是此处
                if (f.equals("PS")) {
                    dataMap.put(TABLE_LINE_TYPE_LX, dm.get("流向"));
                    dataMap.put(TABLE_LINE_TYPE_DHGD, dm.get("导虹管段"));
                    dataMap.put(TABLE_LINE_TYPE_YXZT, dm.get("设施运行状态"));
                    dataMap.put(TABLE_LINE_TYPE_GXZL, dm.get("管线质量"));
                    dataMap.put(TABLE_LINE_TYPE_GDDJ, dm.get("管道等级"));
                } else if (f.equals("DX")) {
//                    getView().visibleDX();
                } else if (f.equals("RQ")) {
                    dataMap.put(TABLE_LINE_TYPE_YL, dm.get("压力"));
                } else if (f.equals("BM")) {
//                        getView().visibleBM();
                }
                if (c.equals("GD")) {
                    dataMap.put(TABLE_LINE_TYPE_DY, dm.get("电压"));
                }
                return dataMap;
            }

            @Override
            protected void jobEnd(Map<String, List<String>> datam) {
                if (getView() != null) {
                    // TODO: 2019/6/24 数据填充
                    getView().queryDb(datam);
                }
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
            id = getIds(fatherName, itemName);
        }

        final long finalId = id;
        final String finalValue = value;
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<BaseMVPModel.MessageEntity<String>>() {

            @Override
            protected BaseMVPModel.MessageEntity<String> jobContent() throws Exception {

                if (fatherName.equals(TABLE_LINE_TYPE_STATUS)) {
                    Sys_Table tables = LitPalUtils.selectsoloWhere(Sys_Table.class, "code = ?", typeCode);
                    Sys_Type_Child father = LitPalUtils.selectsoloWhere(Sys_Type_Child.class, "name = ?", String.valueOf(tables.getName()));

                    Sys_Line_Manhole m = new Sys_Line_Manhole();
                    m.setName(father.getFatherCode());
                    m.setValue(finalValue);
                    if (finalId > 0) {
                        m.update(finalId);
                    } else {
                        m.save();
                    }
                } else if (fatherName.equals(TABLE_LINE_TYPE_MSFS)) {
                    Sys_Embedding e = new Sys_Embedding();
                    e.setId(finalId);
                    e.setFatherCode(String.valueOf(typeCode));
                    e.setName(finalValue);
                    if (finalId > 0) {
                        e.update(finalId);
                    } else {
                        e.save();
                    }
                } else if (fatherName.equals(TABLE_LINE_TYPE_SSZT)) {
                    Sys_UseStatus s = new Sys_UseStatus();
                    s.setId(finalId);
                    s.setFatherCode(String.valueOf(typeCode));
                    s.setName(finalValue);
                    if (finalId > 0) {
                        s.update(finalId);
                    } else {
                        s.save();
                    }
                } else {
                    Sys_Line_Data d = new Sys_Line_Data();
                    d.setName(fatherName);
                    d.setValue(finalValue);
                    if (finalId > 0) {
                        d.update(finalId);
                    } else {
                        d.save();
                    }
                }

                return null;
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity<String> stringMessageEntity) {
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
                long id = getIds(fatherName, itemName);
                if (fatherName.equals(TABLE_LINE_TYPE_STATUS)) {
                    int count = LitPalUtils.deleteData(Sys_Line_Manhole.class, "id = ?", String.valueOf(id));

                } else if (fatherName.equals(TABLE_LINE_TYPE_MSFS)) {
                    int count = LitPalUtils.deleteData(Sys_Embedding.class, "id = ?", String.valueOf(id));
                } else if (fatherName.equals(TABLE_LINE_TYPE_SSZT)) {
                    int count = LitPalUtils.deleteData(Sys_UseStatus.class, "id = ?", String.valueOf(id));
                } else {
                    int count = LitPalUtils.deleteData(Sys_Line_Data.class, "id = ?", String.valueOf(id));
                }
                return null;
            }

            @Override
            protected void jobEnd(BaseMVPModel.MessageEntity<String> stringMessageEntity) {
                if (getView() != null) {
                    getView().openSuccess();
                }
            }
        });
    }

    public long getIds(String f, String c) {
        long id = 0;
        for (String f_n : idds.keySet()) {
            if (f_n.equals(f)) {
                Map<String, Long> cv = idds.get(f_n);
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
}
