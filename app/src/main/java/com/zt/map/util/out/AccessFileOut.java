package com.zt.map.util.out;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.WorkerThread;

import com.zt.map.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AccessFileOut {

    @WorkerThread
    public static String copyAccess(Context mContext,int id, String fileName) {
        InputStream inputStream = mContext.getResources().openRawResource(id);
        String filePath = Environment.getExternalStorageDirectory() + "/excel";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return readInputStream(filePath + "/" + fileName, inputStream);
    }

    /**
     * 读取输入流中的数据写入输出流
     *
     * @param storagePath 目标文件路径
     * @param inputStream 输入流
     */
    public static String readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        try {
            if (!file.exists()) {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

}
