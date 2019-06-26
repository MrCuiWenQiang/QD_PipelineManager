package com.zt.map.contract;

import android.graphics.Bitmap;
import android.text.Editable;

import com.zt.map.entity.TreeBean;

import java.util.List;
import java.util.Map;

public class SettingMarkerContract {
    public interface View {
        void queryType(String[] tabNames, Long[] typeIds);

        void queryDb(Map<String, List<String>> dataMap);

        void openSuccess();
        void openFail(String msg);

        void queryIcons(Map<String, Bitmap> bitmap,String f,String itemName, final int position);
    }

    public interface Presenter {
        void queryType(); //查询类别

        void queryData(long typeId);//根据类别查询相应数据

        /**
         * 保存或修改 由itemName id决定是否保存

         */
        void saveOrRevise(String typeCode, Editable nowValue, String fatherName, String itemName, int position);

        void delete(String fatherName, String itemName, int position);

        void queryIcons(long typeId,String fatherName,String itemName, final int position);//查询某类下的特征点附属物图片
    }

    public interface Model {
    }
}
