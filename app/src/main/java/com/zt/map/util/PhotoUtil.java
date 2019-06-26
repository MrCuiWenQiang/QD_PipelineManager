package com.zt.map.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PhotoUtil {
    public static final int TAKE_IMAGE_REQUEST_CODE = 19001;
    private static String IMG_TYPE = "image/jpeg";
    public  static File fileImagePath;

    public static void openZKCamera(Activity context,String projectName) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String imageName = "immqy_%s.jpg";
            String filename = String.format(imageName, dateFormat.format(new Date()));
            File mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Pipeline/"+projectName);
            if (!mImageStoreDir.exists()) {
                mImageStoreDir.mkdirs();
            }
            fileImagePath = new File(mImageStoreDir, filename);
            String mImagePath = fileImagePath.getAbsolutePath();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
                context.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mImagePath);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, IMG_TYPE);
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                context.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
            }
        } else {
            Toast.makeText(context, "没有检测到拍照设备", Toast.LENGTH_SHORT).show();
        }
    }
}
