package com.zt.map.contract;

import android.text.Editable;

import java.util.List;
import java.util.Map;

public class SettingLineContract {
    public interface View {
        void queryType(String[] tabNames, Long[] typeIds);
        void queryDb(Map<String, List<String>> dataMap);

        void openSuccess();
        void openFail(String msg);
    }

    public interface Presenter {
        void queryType(); //查询类别

         void queryData(long typeId);

        /**
         * 保存或修改 由itemName id决定是否保存

         */
        void saveOrRevise(String typeCode, Editable nowValue, String fatherName, String itemName, int position);

        void delete(String fatherName, String itemName, int position);
    }

    public interface Model {
    }
}
