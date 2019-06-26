package com.zt.map.contract;


import com.zt.map.entity.db.tab.Tab_marker_photo;

import java.util.List;

public class PhotoContract {
    public interface View {
        void queryPhotos(List<Tab_marker_photo> datas);

    }

    public interface Presenter {
        void queryPhoto(long project);
    }

    public interface Model {

    }
}
