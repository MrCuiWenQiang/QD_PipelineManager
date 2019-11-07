package com.zt.map.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zt.map.R;
import com.zt.map.contract.InputContract;
import com.zt.map.presenter.InputPresenter;
import com.zt.map.util.FileUtils;

import java.security.PublicKey;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;

//导入
public class InputActivity extends BaseMVPAcivity<InputContract.View, InputPresenter> implements InputContract.View, View.OnClickListener {
    private static final int FILE_SELECT_CODE_MARKER = 05156;
    private static final int FILE_SELECT_CODE_LINE = 05157;

    private LinearLayout ll_marker;
    private LinearLayout ll_line;

    private ImageView input_marker_im;
    private ImageView input_line_im;

    private Button bt_input;
    private EditText et_projectname;

    private TextView input_marker_txt;
    private TextView input_line_txt;
    @Override
    protected void initContentView() {
        ll_marker = findViewById(R.id.ll_marker);
        ll_line = findViewById(R.id.ll_line);
        input_marker_txt = findViewById(R.id.input_marker_txt);
        input_line_txt = findViewById(R.id.input_line_txt);
        input_marker_im = findViewById(R.id.input_marker_im);
        input_line_im = findViewById(R.id.input_line_im);
        bt_input = findViewById(R.id.bt_input);
        et_projectname = findViewById(R.id.et_projectname);

        ll_marker.setOnClickListener(this);
        ll_line.setOnClickListener(this);
        bt_input.setOnClickListener(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setLeftTitle("项目导入", R.color.white);
        setToolBarBackgroundColor(R.color.blue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_marker: {
                showFileChooser(FILE_SELECT_CODE_MARKER);
                break;
            }
            case R.id.ll_line: {
                showFileChooser(FILE_SELECT_CODE_LINE);
                break;
            }
            case R.id.bt_input: {
                showLoading();
                mPresenter.inputProject(getValue(et_projectname),getTAG(input_marker_txt),getTAG(input_line_txt));
                break;
            }
        }
    }

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_input;
    }


    private void showFileChooser(int code) {
        if (!permissionFile()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), code);
        } catch (android.content.ActivityNotFoundException ex) {
            ToastUtility.showToast("Please install a File Manager.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE_MARKER: {
                if (resultCode == RESULT_OK) {
                    String path = getPath(data);
                    input_marker_txt.setText( path);
                    input_marker_txt.setTag(path);
                    input_marker_txt.setTextSize(12);
                    input_marker_txt.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                    input_marker_im.setImageResource(R.mipmap.excel_se);
                }
                break;
            }
            case FILE_SELECT_CODE_LINE: {
                if (resultCode == RESULT_OK) {
                    String path =getPath(data);
                    input_line_txt.setText( path);
                    input_line_txt.setTag(path);
                    input_line_txt.setTextSize(12);
                    input_line_txt.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                    input_line_im.setImageResource(R.mipmap.excel_se);
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getPath(Intent data){
        Uri uri = data.getData();
        String path = FileUtils.getssPath(this, uri);
        return path;
    }

    @Override
    public void inputProjectFail(String msg) {
        dimiss();
        showDialog(msg);
    }

    @Override
    public void inputProjectSuccess() {
        dimiss();
        showDialog("", "导入成功", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private boolean permissionFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            int perm = checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            boolean permision = (perm == PackageManager.PERMISSION_GRANTED);
            int rperm = checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
            boolean rpermision = (rperm == PackageManager.PERMISSION_GRANTED);
            if (!rpermision || !permision || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 63);
                return false;
            }
        }
        return true;
    }
}
