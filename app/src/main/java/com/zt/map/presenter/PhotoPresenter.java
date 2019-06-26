package com.zt.map.presenter;


import com.zt.map.contract.PhotoContract;
import com.zt.map.entity.db.tab.Tab_marker_photo;

import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPPresenter;
import cn.faker.repaymodel.util.db.DBThreadHelper;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;

public class PhotoPresenter extends BaseMVPPresenter<PhotoContract.View> implements PhotoContract.Presenter {


    @Override
    public void queryPhoto(final long project) {
        DBThreadHelper.startThreadInPool(new DBThreadHelper.ThreadCallback<List<Tab_marker_photo>>() {

            @Override
            protected List<Tab_marker_photo> jobContent() throws Exception {
                List<Tab_marker_photo> ps = LitPalUtils.selectWhere(Tab_marker_photo.class, "projectId=?", String.valueOf(project));
                return ps;
            }

            @Override
            protected void jobEnd(List<Tab_marker_photo> datas) {
                if (getView()!=null){
                    getView().queryPhotos(datas);
                }
            }
        });
    }
}
