package com.zt.map.util.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.SyncStateContract;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * 微信 文件分享
 */
public class WXShareUtil {

   // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wxa24eb43a8e35114b";

    // IWXAPI 是第三方app和微信通信的openApi接口
    private static IWXAPI api;

    public static void regToWx(Context context) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    public static void ShareFileToWeiXin(String title, String filePath, String fileName) {
        WXFileObject fileObj = new WXFileObject();
        fileObj.fileData = inputStreamToByte(filePath);//文件路径
        fileObj.filePath = fileName;
        //使用媒体消息分享
        WXMediaMessage msg = new WXMediaMessage(fileObj);
        msg.title = title;
        //发送请求
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //创建唯一标识 
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 将输入的流转换为byte数组
     *
     * @return byte数组
     */
    public static byte[] inputStreamToByte(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
