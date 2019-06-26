package com.zt.map.contract;

import android.content.Context;
import android.text.Editable;

import com.zt.map.entity.db.TaggingEntiiy;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_Project;

import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPModel;

public class MainContract {
    public interface View {
        void queryType(String[] tabNames, Long[] typeIds,Integer[] colors);

        void queryProjects(List<Tab_Project> tab_projects);
        void queryProjects_fail(String msg);

        void createProject_success(String msg,long projectId);
        void createProject_fail(String msg);

        void queryProject(List<Tab_Marker> markers, List<Tab_Line> lines,long projectId);
        void delete_success(String msg,int type);
        void delete_fail(String msg);
        void update();
        void queryMarker(Tab_Marker data);

        void taggers(List<TaggingEntiiy> taggings);

        void outExcel(String msg);
        void delete_Project();
    }

    public interface Presenter {
        void queryType();
        void queryProjects();
        void createProject(Editable name,boolean isPhoto);
        void queryProject(long projectId);

        void delete(long id, int type);// 1 删除点 2删除线
        void update_MakerLocal(long makerId, double latitude, double longitude);

        void queryTagger(long projectId,int which);

        void queryMarker(long projectId, String text);
         void outExcel(Long projectId, final Context mContext);
         void outAccess(Long projectId, final Context mContext);

        void delete_Project(long id);
    }

    public interface Model {
        void query_projects(BaseMVPModel.CommotListener<List<Tab_Project>> listener);
        void createProject(String name,boolean isPgoto,BaseMVPModel.CommotListener<BaseMVPModel.MessageEntity<Long>> listener);

        void update_MakerLocal(final long makerId, final double latitude, final double longitude, final BaseMVPModel.CommotListener<Boolean> listener);
    }
}
