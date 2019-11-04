package com.zt.map.contract;

import com.zt.map.entity.db.PhotoInfo;
import com.zt.map.entity.db.tab.Tab_Marker;

import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPModel;

public class MarkerContract {
    public interface View {
        void query_tzd(String[] items);

        void query_fsw(String[] items);

        void fail(String msg);

        void save_success();

        void save_Fail(String msg);

        void queryMarker_success(Tab_Marker marker);

        void queryMarker_fail(String mag);

        void queryTopType(Tab_Marker marker);

        void getName(String name);

        void createPhoto();

        void queryProjectName(String project);

        void querymanhole(String[] items);
        void querytcfss(String[] items);

        void queryUseStatus(String[] items);

        void queryJGZTs(String[] items);

        void queryJGLXs(String[] items);
        void queryszwz(String[] items);

        void querysjly(String[] items);
        void querysfly(String[] items);

        void visiblePS();
        void queryVisibleSuccess();

        void queryhqsj(String[] hqsj);
    }

    public interface Presenter {
        void save(Tab_Marker tab, List<PhotoInfo> resultList);

        void queryMarker(long id);

        void getName(long project, long typeId);

        void insertMarker(Tab_Marker tab, long lineId);//插入点

        void queryIsPhoto(long projectId);

        void querySZWZ();

        void queryTopType(long project, long typeId);

        void queryVisible(long typeId);

        void query_tzd();

        void query_fsw();

        void queryUseStatus();

        void querymanhole();

        void queryJGZTs();

        void queryJGLXs();

        void querysjly();
        void querysfly();

    }

    public interface Model {
        void queryMarker(long id, BaseMVPModel.CommotListener<Tab_Marker> listener);

        void queryTable(long typeId, BaseMVPModel.CommotListener<Map<String, String[]>> listener);
    }
}
