package com.zt.map.entity.db.system;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

/**
 * 类型表
 */
public class Sys_Table extends LitePalSupport {
    private long id;
    private String name;//类型名称
    private String code;//种类代号


    private List<Sys_Appendages> appendages = new ArrayList<>();//附属物
    private List<Sys_Direction> direction = new ArrayList<>();//方向
    private List<Sys_Embedding> embeddings = new ArrayList<>();//埋设方式
    private List<Sys_Features> features= new ArrayList<>();//特征点
    private List<Sys_LineType> lineTypes = new ArrayList<>();//管线类型
    private List<Sys_Manhole> manholes = new ArrayList<>();//井盖材质
    private List<Sys_Material> materials = new ArrayList<>();//材料
    private List<Sys_UseStatus> useStatuses = new ArrayList<>();//使用状况
    private List<Sys_Pressure> pressures = new ArrayList<>();//压力
    private List<Sys_TGCL> tgcls = new ArrayList<>();//套管材料
    private List<Sys_LineFC> fvs = new ArrayList<>();//管线范畴
    private List<Sys_Color> colors = new ArrayList<>();
    private  List<Sys_HQSJ> hqsjs=new ArrayList<>();//获取时机

    public List<Sys_HQSJ> getHqsjs() {
        return hqsjs;
    }

    public void setHqsjs(List<Sys_HQSJ> hqsjs) {
        this.hqsjs = hqsjs;
    }

    public List<Sys_LineFC> getFvs() {
        return fvs;
    }

    public void setFvs(List<Sys_LineFC> fvs) {
        this.fvs = fvs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Sys_Color> getColors() {
        return LitPalUtils.selectWhere(Sys_Color.class,"fatherCode = ? ",code);
    }

    public List<Sys_Pressure> getPressures() {
        return pressures;
    }

    public void setPressures(List<Sys_Pressure> pressures) {
        this.pressures = pressures;
    }

    public List<Sys_TGCL> getTgcls() {
        return tgcls;
    }

    public void setTgcls(List<Sys_TGCL> tgcls) {
        this.tgcls = tgcls;
    }

    public void setColors(List<Sys_Color> colors) {
        this.colors = colors;
    }

    public List<Sys_Appendages> getAppendages() {
        return appendages;
    }

    public void setAppendages(List<Sys_Appendages> appendages) {
        this.appendages = appendages;
    }

    public List<Sys_Direction> getDirection() {
        return direction;
    }

    public void setDirection(List<Sys_Direction> direction) {
        this.direction = direction;
    }

    public List<Sys_Embedding> getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(List<Sys_Embedding> embeddings) {
        this.embeddings = embeddings;
    }

    public List<Sys_Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Sys_Features> features) {
        this.features = features;
    }

    public List<Sys_LineType> getLineTypes() {
        return lineTypes;
    }

    public void setLineTypes(List<Sys_LineType> lineTypes) {
        this.lineTypes = lineTypes;
    }

    public List<Sys_Manhole> getManholes() {
        return manholes;
    }

    public void setManholes(List<Sys_Manhole> manholes) {
        this.manholes = manholes;
    }

    public List<Sys_Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Sys_Material> materials) {
        this.materials = materials;
    }

    public List<Sys_UseStatus> getUseStatuses() {
        return useStatuses;
    }

    public void setUseStatuses(List<Sys_UseStatus> useStatuses) {
        this.useStatuses = useStatuses;
    }
}
