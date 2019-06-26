package com.zt.map.model;

import com.zt.map.contract.MainContract;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_Project;

import java.util.Date;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class MainModel extends BaseMVPModel implements MainContract.Model {
    @Override
    public void query_projects(final CommotListener<List<Tab_Project>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Tab_Project>>() {

            @Override
            protected List<Tab_Project> jobContent() throws Exception {
                List<Tab_Project> list =   LitPalUtils.selectWheres("createTime DESC",Tab_Project.class);

                return list;
            }

            @Override
            protected void jobEnd(List<Tab_Project> tab_projects) {
                listener.result(tab_projects);
            }
        });
    }

    @Override
    public void createProject(final String name, final boolean isPgoto, final CommotListener<MessageEntity<Long>> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<MessageEntity<Long>>() {

            @Override
            protected MessageEntity jobContent() throws Exception {
                MessageEntity<Long> msg = new MessageEntity();
                int count = LitPalUtils.selectCount(Tab_Project.class,"name = ?",name);
                if (count>0){
                    msg.setStatus(false);
                    msg.setMessage("已有同名工程");
                    return msg;
                }
                Tab_Project tab = new Tab_Project();
                tab.setCreatePhoto(isPgoto);
                tab.setName(name);
                tab.setCreateTime(new Date());
                boolean status = tab.save();
                if (status){
                    msg.setData(tab.getId());
                    msg.setStatus(true);
                    msg.setMessage("创建成功");
                }else {
                    msg.setStatus(false);
                    msg.setMessage("创建失败");
                }
                return msg;
            }

            @Override
            protected void jobEnd(MessageEntity messageEntity) {
                listener.result(messageEntity);
            }
        });
    }

    @Override
    public void update_MakerLocal(final long makerId, final double latitude, final double longitude, final CommotListener<Boolean> listener) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<Boolean>() {

            @Override
            protected Boolean jobContent() throws Exception {
                boolean status = false;
                Tab_Marker tb_marker = LitPalUtils.selectsoloWhere(Tab_Marker.class,"id = ?",String.valueOf(makerId));
                if (tb_marker!=null){
                    tb_marker.setLatitude(latitude);
                    tb_marker.setLongitude(longitude);
                    status = tb_marker.save();

                    List<Tab_Line> lines = LitPalUtils.selectWhere(Tab_Line.class,"startMarkerId = ? OR endMarkerId = ?"
                            ,String.valueOf(makerId),String.valueOf(makerId));
                    if (lines!=null){
                        for (Tab_Line item:lines) {
                            if (item.getStartMarkerId()==makerId){
                                item.setStart_latitude(latitude);
                                item.setStart_longitude(longitude);
                            }else  if (item.getEndMarkerId()==makerId){
                                item.setEnd_latitude(latitude);
                                item.setEnd_longitude(longitude);
                            }
                        }
                        LitPalUtils.saveAll(lines);
                    }

                }
                return status;
            }

            @Override
            protected void jobEnd(Boolean aBoolean) {
                listener.result(aBoolean);
            }
        });
    }
}
