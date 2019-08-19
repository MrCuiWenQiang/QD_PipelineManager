package com.zt.map.util;

import android.graphics.Bitmap;

import cn.faker.repaymodel.util.LogUtil;

public class BitmapUtil {
    private static int acolor=0x00000000;

    //检查图片颜色
    public static void test(Bitmap oldBitmap){
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
//        int mArrayColorLengh = width * height;
//        int[] mArrayColor = new int[mArrayColorLengh];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = newBitmap.getPixel(j, i);
                LogUtil.e("color","color = "+color);
            }
        }

    }

    //    0x00000000
    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int[] oldColor, int[] newColor) {
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
//        int mArrayColorLengh = width * height;
//        int[] mArrayColor = new int[mArrayColorLengh];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = newBitmap.getPixel(j, i);
                int count = -1;
                for (int k = 0; k < oldColor.length; k++) {
                    int oc = oldColor[k];
                    if (oc == color) {
                        count = k;
                        continue;
                    }
                }
                if (count != -1) {
                    if (count>=newColor.length){
                        count = newColor.length-1;
                    }
                    newBitmap.setPixel(j, i, newColor[count]);
                }
            }
        }
        return newBitmap;
    }
    //使用在本项目
    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int newColor) {
        Bitmap newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
//        int mArrayColorLengh = width * height;
//        int[] mArrayColor = new int[mArrayColorLengh];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = newBitmap.getPixel(j, i);
                if (color==-1){
                    newBitmap.setPixel(j,i,acolor);
                }else if (-3684409>=color&&color>=-16777216){
                    newBitmap.setPixel(j,i,newColor);
                }
            }
        }
        return newBitmap;
    }
}
