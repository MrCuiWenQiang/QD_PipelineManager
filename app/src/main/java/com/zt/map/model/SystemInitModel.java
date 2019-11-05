package com.zt.map.model;

import com.zt.map.MyApplication;
import com.zt.map.R;
import com.zt.map.entity.db.system.Sys_Appendages;
import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.system.Sys_Direction;
import com.zt.map.entity.db.system.Sys_Embedding;
import com.zt.map.entity.db.system.Sys_Features;
import com.zt.map.entity.db.system.Sys_HQSJ;
import com.zt.map.entity.db.system.Sys_Jglx;
import com.zt.map.entity.db.system.Sys_Jgzt;
import com.zt.map.entity.db.system.Sys_LineFC;
import com.zt.map.entity.db.system.Sys_LineType;
import com.zt.map.entity.db.system.Sys_Line_Manhole;
import com.zt.map.entity.db.system.Sys_Manhole;
import com.zt.map.entity.db.system.Sys_Material;
import com.zt.map.entity.db.system.Sys_Pressure;
import com.zt.map.entity.db.system.Sys_Line_Data;
import com.zt.map.entity.db.system.Sys_TGCL;
import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.system.Sys_Type;
import com.zt.map.entity.db.system.Sys_Type_Child;
import com.zt.map.entity.db.system.Sys_UseStatus;
import com.zt.map.entity.db.system.Sys_tcfs;
import com.zt.map.util.FileReadOpen;
import com.zt.map.util.Table;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class SystemInitModel extends BaseMVPModel {
    private final String[] vs_name = new String[]{"材料", "附属物", "管线类型", "井盖材质", "埋设方式", "使用状况", "方向", "特征", "颜色", "压力", "套管材质","管线范畴","获取时机"};
    //井盖状态
    String[] jgzts = new String[]{"完好", "破损", "丢失"};
    //井盖类型
    String[] jglx = new String[]{"方形", "圆形", "其它"};

    public void initType(final CommotListener<Boolean> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback() {
            @Override
            protected Object jobContent() throws Exception {


                int count = LitPalUtils.selectCount(Sys_Table.class);
                if (count > 0) {
                    // TODO: 2019/11/1 此处修复已安装版本的信息修改.
                    add();
                    return true;
                }
                InputStream inputStream = MyApplication.getContext().getResources().openRawResource(R.raw.setting);
                FileReadOpen.Data d = FileReadOpen.readText(inputStream);
                Map<String, Map<String, List<Table>>> map = d.getMap();
                Map<String, String> fatherMap = d.getFatherMap();//类型数据
                List<Sys_Table> tabs = new ArrayList<>();
                for (String key : fatherMap.keySet()) {
                    Sys_Table tab = new Sys_Table();
                    tab.setName(fatherMap.get(key));
                    tab.setCode(key);
                    tabs.add(tab);
                }
                // TODO: 2019/5/22 此处应使用反射和类赋值
                for (Sys_Table tab : tabs) {
                    Map<String, List<Table>> vmap = map.get(tab.getCode());

                    save(tab,vmap);
                }
                LitPalUtils.saveAll(tabs);

                //附属物
                InputStream fsputStream = MyApplication.getContext().getResources().openRawResource(R.raw.fushuwu);
                Map<String, Map<String, String>> fsmaps = FileReadOpen.readTZORFSW(fsputStream);
                List<Sys_Appendages> aps = new ArrayList<>();
                for (String father : fsmaps.keySet()) {
                    Map<String, String> child = fsmaps.get(father);
                    for (String key : child.keySet()) {
                        Sys_Appendages sys_appendages = new Sys_Appendages();
                        sys_appendages.setName(key);
                        sys_appendages.setFatherCode(father);
                        sys_appendages.setIcon(child.get(key));
                        aps.add(sys_appendages);
                    }
                }
                LitPalUtils.saveAll(aps);

                //特征
                InputStream tzputStream = MyApplication.getContext().getResources().openRawResource(R.raw.tezhen);
                Map<String, Map<String, String>> tzmaps = FileReadOpen.readTZORFSW(tzputStream);

                List<Sys_Features> fs = new ArrayList<>();
                for (String father : tzmaps.keySet()) {
                    Map<String, String> child = tzmaps.get(father);
                    for (String key : child.keySet()) {
                        Sys_Features sys_appendages = new Sys_Features();
                        sys_appendages.setName(key);
                        sys_appendages.setFatherCode(father);
                        sys_appendages.setIcon(child.get(key));
                        fs.add(sys_appendages);
                    }
                }
                LitPalUtils.saveAll(fs);

                //线材质
                InputStream inec = MyApplication.getContext().getResources().openRawResource(R.raw.caizi);
                Map<String, List<String>> lparams = FileReadOpen.readCaiZi(inec);

                List<Sys_Line_Manhole> mans = new ArrayList<>();
                for (String key : lparams.keySet()) {
                    List<String> ls = lparams.get(key);
                    if (ls == null) {
                        continue;
                    }
                    for (String v : ls) {
                        Sys_Line_Manhole item = new Sys_Line_Manhole();
                        item.setName(key);
                        item.setValue(v);
                        mans.add(item);
                    }
                }
                LitPalUtils.saveAll(mans);
                //管类层级
                InputStream typeStream = MyApplication.getContext().getResources().openRawResource(R.raw.type);
                Map<String, Map<String, String>> params = FileReadOpen.readType(typeStream);
                List<Sys_Type> types = new ArrayList<>();
                List<Sys_Type_Child> child_types = new ArrayList<>();

                for (String typeKey : params.keySet()) {
                    Map<String, String> childMap = params.get(typeKey);
                    Sys_Type type = new Sys_Type();
                    type.setName(typeKey);
                    types.add(type);
                    for (String c_key : childMap.keySet()) {
                        String value = childMap.get(c_key);
                        Sys_Type_Child child = new Sys_Type_Child();
                        child.setName(c_key);
                        child.setValue(value);
                        child.setFatherCode(typeKey);
                        child_types.add(child);
                    }

                }
                LitPalUtils.saveAll(types);
                LitPalUtils.saveAll(child_types);

                List<Sys_Jglx> lxs = new ArrayList<>();
                List<Sys_Jgzt> zts = new ArrayList<>();
                for (String value : jglx) {
                    Sys_Jglx jglx = new Sys_Jglx();
                    jglx.setName(value);
                    lxs.add(jglx);
                }
                for (String value : jgzts) {
                    Sys_Jgzt jgzt = new Sys_Jgzt();
                    jgzt.setName(value);
                    zts.add(jgzt);
                }
                LitPalUtils.saveAll(lxs);
                LitPalUtils.saveAll(zts);

                //线数据表
                InputStream inp = MyApplication.getContext().getResources().openRawResource(R.raw.ps_line);
                Map<String, List<String>> psparams = FileReadOpen.readCaiZi(inp);
                List<Sys_Line_Data> pss=new ArrayList<>();
                for (String name:psparams.keySet()) {
                    List<String> vs = psparams.get(name);
                    for (String value:vs) {
                        Sys_Line_Data ps = new Sys_Line_Data();
                        ps.setName(name);
                        ps.setValue(value);
                        pss.add(ps);
                    }
                }
                LitPalUtils.saveAll(pss);

                List<Sys_tcfs> tcfss = new ArrayList<>();
                Sys_tcfs tcfs1 = new Sys_tcfs();
                tcfs1.setValue("探测");
                Sys_tcfs tcfs2 = new Sys_tcfs();
                tcfs2.setValue("竣工");
                tcfss.add(tcfs1);
                tcfss.add(tcfs2);
                LitPalUtils.saveAll(tcfss);
                return true;
            }

            protected void save(Sys_Table tab, Map<String, List<Table>> vmap){
                {
                    for (String name : vmap.keySet()) {
                        List<Table> vtab = vmap.get(name);
                        if (name.equals(vs_name[0])) {
                            List<Sys_Material> materials = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Material material = new Sys_Material();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                materials.add(material);
                            }
                            tab.setMaterials(materials);
                            LitPalUtils.saveAll(materials);

                        } else if (name.equals(vs_name[1])) {
                            continue;
                        } else if (name.equals(vs_name[2])) {

                            List<Sys_LineType> lineTypes = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_LineType material = new Sys_LineType();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                lineTypes.add(material);
                            }
                            tab.setLineTypes(lineTypes);
                            LitPalUtils.saveAll(lineTypes);

                        } else if (name.equals(vs_name[3])) {

                            List<Sys_Manhole> manholes = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Manhole material = new Sys_Manhole();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                manholes.add(material);
                            }
                            tab.setManholes(manholes);
                            LitPalUtils.saveAll(manholes);

                        } else if (name.equals(vs_name[4])) {

                            List<Sys_Embedding> embeddings = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Embedding material = new Sys_Embedding();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                embeddings.add(material);
                            }
                            tab.setEmbeddings(embeddings);
                            LitPalUtils.saveAll(embeddings);

                        } else if (name.equals(vs_name[5])) {

                            List<Sys_UseStatus> useStatuses = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_UseStatus material = new Sys_UseStatus();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                useStatuses.add(material);
                            }
                            tab.setUseStatuses(useStatuses);
                            LitPalUtils.saveAll(useStatuses);

                        } else if (name.equals(vs_name[6])) {

                            List<Sys_Direction> directions = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Direction material = new Sys_Direction();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                directions.add(material);
                            }
                            tab.setDirection(directions);
                            LitPalUtils.saveAll(directions);
                        } else if (name.equals(vs_name[7])) {
                            continue;
                        } else if (name.equals(vs_name[8])) {
                            List<Sys_Color> colors = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Color material = new Sys_Color();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                if (t.getValue() != null) {
                                    String[] rgb = t.getValue().split(",");
                                    if (rgb.length == 3) {
                                        material.setR(rgb[0]);
                                        material.setG(rgb[1]);
                                        material.setB(rgb[2]);
                                    }
                                }
                                colors.add(material);
                            }
                            tab.setColors(colors);
                            LitPalUtils.saveAll(colors);
                        } else if (name.equals(vs_name[9])) {

                            List<Sys_Pressure> features = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_Pressure material = new Sys_Pressure();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                features.add(material);
                            }
                            tab.setPressures(features);
                            LitPalUtils.saveAll(features);
                        } else if (name.equals(vs_name[10])) {

                            List<Sys_TGCL> features = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_TGCL material = new Sys_TGCL();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                features.add(material);
                            }
                            tab.setTgcls(features);
                            LitPalUtils.saveAll(features);
                        }else if (name.equals(vs_name[11])) {
                            List<Sys_LineFC> features = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_LineFC material = new Sys_LineFC();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                features.add(material);
                            }
                            tab.setFvs(features);
                            LitPalUtils.saveAll(features);
                        }else if (name.equals(vs_name[12])) {
                            List<Sys_HQSJ> features = new ArrayList<>();
                            for (Table t : vtab) {
                                Sys_HQSJ material = new Sys_HQSJ();
                                material.setFatherCode(tab.getCode());
                                material.setName(t.getName());
                                features.add(material);
                            }
                            tab.setHqsjs(features);
                            LitPalUtils.saveAll(features);
                        }
                    }
                }
            }

            private void add() {
                Sys_Line_Manhole sys_line_manhole = LitPalUtils.selectsoloWhere(Sys_Line_Manhole.class,"value = '钢PE  PVC'");
                if (sys_line_manhole!=null){
                    sys_line_manhole.delete();
                    List<Sys_Line_Manhole> sms = new ArrayList<>();
                    Sys_Line_Manhole sm1 = new Sys_Line_Manhole();
                    sm1.setName("JS");
                    sm1.setValue("钢");
                    Sys_Line_Manhole sm2 = new Sys_Line_Manhole();
                    sm2.setName("JS");
                    sm2.setValue("PE");
                    Sys_Line_Manhole sm3 = new Sys_Line_Manhole();
                    sm3.setName("JS");
                    sm3.setValue("PVC");
                    sms.add(sm1);
                    sms.add(sm2);
                    sms.add(sm3);
                    LitPalUtils.saveAll(sms);
                }
                int txcount = LitPalUtils.selectCount(Sys_Type_Child.class,"value = 'TV'");
                if (txcount<=0){
                    try {
                        InputStream inputStream = MyApplication.getContext().getResources().openRawResource(R.raw.setting);
                        FileReadOpen.Data d = null;
                        d = FileReadOpen.readText(inputStream);
                        Map<String, Map<String, List<Table>>> map = d.getMap();
                        Map<String, String> fatherMap = d.getFatherMap();//类型数据
                        Sys_Table tab = null;
                        for (String key : fatherMap.keySet()) {
                            if (key.equals("TV")){
                                tab = new Sys_Table();
                                tab.setName(fatherMap.get(key));
                                tab.setCode(key);
                            }
                        }
                        if (tab!=null){
                            Map<String, List<Table>> vmap = map.get(tab.getCode());
                            save(tab,vmap);
                            tab.save();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Sys_Type_Child child = new Sys_Type_Child();
                    child.setName("电视");
                    child.setValue("TV");
                    child.setFatherCode("DX");
                    child.save();


                }

                int llcount = LitPalUtils.selectCount(Sys_Features.class,"name = '非普查区'");
                if (llcount<=0){
                    Sys_Features s1 = new Sys_Features();
                    s1.setName("非普查区");
                    s1.setFatherCode("JS");

                    Sys_Features s2 = new Sys_Features();
                    s2.setName("非普查区");
                    s2.setFatherCode("RQ");

                    Sys_Features s3 = new Sys_Features();
                    s3.setName("非普查区");
                    s3.setFatherCode("RL");

                    Sys_Features s4 = new Sys_Features();
                    s4.setName("非普查区");
                    s4.setFatherCode("GY");

                    Sys_Features s5 = new Sys_Features();
                    s5.setName("非普查区");
                    s5.setFatherCode("DL");

                    Sys_Features s6 = new Sys_Features();
                    s6.setName("非普查区");
                    s6.setFatherCode("DX");

                    Sys_Features s7 = new Sys_Features();
                    s7.setName("非普查区");
                    s7.setFatherCode("PS");

                    Sys_Features s8 = new Sys_Features();
                    s8.setName("非普查区");
                    s8.setFatherCode("ZH");

                    List<Sys_Features> ss = new ArrayList<>();
                    ss.add(s1);
                    ss.add(s2);
                    ss.add(s3);
                    ss.add(s4);
                    ss.add(s5);
                    ss.add(s6);
                    ss.add(s7);
                    ss.add(s8);
                    LitPalUtils.saveAll(ss);
                }
            }

            @Override
            protected void jobEnd(Object o) {
                listener.result((Boolean) o);
            }
        });


    }

}
