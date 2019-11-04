package com.zt.map;


import android.os.Build;
import android.os.StrictMode;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.Bugly;
import com.zt.map.util.db.BatchDBUtil;
import com.zt.map.util.share.WXShareUtil;

import org.litepal.LitePal;

import cn.faker.repaymodel.BasicApplication;
import cn.faker.repaymodel.util.LocImageUtility;
import cn.faker.repaymodel.util.LogUtil;
import cn.faker.repaymodel.util.ToastUtility;

public class MyApplication extends BasicApplication {

    public static String userId = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        setting();
        initBaiduMap();
        initPhoto();
        initShare();
    }

    private void initShare() {
        WXShareUtil.regToWx(getContext());
    }

    private void setting() {
        LogUtil.isShow = true;
        ToastUtility.setToast(getApplicationContext());
        LitePal.initialize(this);
        LogUtil.isShow = true;
        LocImageUtility.setImageUtility(this);
//        HttpHelper.setOnFailedAll(new OnFileClass());
        Bugly.init(getApplicationContext(), "d0a0192266", false);
        BatchDBUtil.init(getContext());
    }

    private void initBaiduMap() {
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    private void initPhoto() {

    }
}
