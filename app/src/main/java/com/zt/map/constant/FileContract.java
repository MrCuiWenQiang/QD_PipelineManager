package com.zt.map.constant;

import android.os.Environment;
import android.text.TextUtils;

import com.zt.map.MyApplication;

import cn.faker.repaymodel.util.FileUtility;

public class FileContract {
    private static final String defPath = "/excel";
    private static  String excelPath;
    /**
     * excel导出文件夹
     * @return
     */
    public static String getOutFilePath(){
        if (TextUtils.isEmpty(excelPath)){
            excelPath = FileUtility.getFileDir(MyApplication.getContext()) + defPath;
//            excelPath = MyApplication.getContext().getFilesDir().getAbsolutePath() + defPath;
        }

        return excelPath;
    }
}
