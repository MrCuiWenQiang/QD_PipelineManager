package com.zt.map.entity.db.tab;



import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 管点---照片表
 */
public class Tab_marker_photo extends LitePalSupport implements Serializable {
    private long id;
    private long projectId;
    private long markerId;
    private String name;//名称 = 物探点号+序号
    private String path;//图片路径

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(long markerId) {
        this.markerId = markerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
