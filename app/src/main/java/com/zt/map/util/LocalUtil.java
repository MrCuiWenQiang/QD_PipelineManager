package com.zt.map.util;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 定位工程
 */
public class LocalUtil {
    private static LocationClient mLocationClient;
    private static Context mContext;
    private static LocationListener listener;


    private static void setting(){
        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        mLocationClient.setLocOption(option);
    }

    public static void start(Context context, final LocationListener locationListener){
        mContext = context;
        listener = locationListener;
        if (mLocationClient ==null){
            setting();
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    listener.onReceiveLocation(bdLocation);
                }
            });
        }
        mLocationClient.start();
    }

    public static void stop(){
        if (mLocationClient!=null){
            mLocationClient.stop();
        }
    }
    public interface LocationListener{
         void onReceiveLocation(BDLocation bdLocation);
    }

}
