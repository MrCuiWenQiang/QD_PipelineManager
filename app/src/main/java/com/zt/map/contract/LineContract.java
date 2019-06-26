package com.zt.map.contract;

import com.zt.map.entity.db.system.Sys_Direction;
import com.zt.map.entity.db.system.Sys_Pressure;
import com.zt.map.entity.db.system.Sys_TGCL;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;

import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPModel;

public class LineContract {
    public interface View {

        void saveSuccess();
        void saveFail(String msg);

        void queryLine_Success(Tab_Line tabLine);
        void queryLine_Fail(String msg);

        void query_remarks(String[] items);
        void query_LineType(String[] items);
        void query_msfs(String[] items);

        void queryTopType(Tab_Line tabLine);

        void queryUncertainData(String[] tgcls, String[] pressures, String[] directions);



        void visiblePS( String[] lx,  String[] dhgd,  String[] ssyxzt,  String[] gxzl,  String[] gddj);

        void visibleDX();
        void visibleRQ(String[] yl);

        void visiblegd(String[] dy);

        void queryUseStatus(String[] items);
        void queryCZ(String[] items);
        void querySfly(String[] items);
        void queryXX(String[] items);

        void fail(String msg);

        void queryStartAndEndMarer(Tab_Marker sm,Tab_Marker em);
    }

    public interface Presenter {

        void save(Tab_Line tab);

        void queryLine(long id);

        void queryType(long typeid);
        void queryXX();

        void queryMsfs(long typeid);

        void query_remarks(long typeId);
        void querySfly();
//        void queryUncertainData(long typeId);

        void queryTopType(long project,long type);//默认加载上一条数据

        void queryStartAndEndMarer(long projectId,long typeId,double start_latitude,double start_longitude,double end_latitude,double end_longitude); //根据传来的坐标点确定是哪条管点

        /**
         * 查询大类
         * @param type
         */
        void queryShowType(long type);
    }

    public interface Model {
//        void queryLine(long id,BaseMVPModel.CommotListener<Tab_Line> listener);

    }
}
