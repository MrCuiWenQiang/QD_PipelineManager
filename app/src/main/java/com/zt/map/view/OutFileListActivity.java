package com.zt.map.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.zt.map.R;
import com.zt.map.constant.OutFileContract;
import com.zt.map.entity.FileBean;
import com.zt.map.presenter.OutFilePresenter;
import com.zt.map.view.adapter.FileAdapter;

import java.io.File;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.SpaceItemDecoration;
import cn.faker.repaymodel.util.ToastUtility;
import cn.faker.repaymodel.util.error.ErrorUtil;

/**
 * 输出文件查看列表
 */
public class OutFileListActivity extends BaseMVPAcivity<OutFileContract.View, OutFilePresenter> implements OutFileContract.View {
    private RecyclerView rv_list;
    private FileAdapter adapter = new FileAdapter();

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_outfiles;
    }

    @Override
    protected void initContentView() {
        setStatusBar(R.color.blue_1);
        setTitle("文件列表", R.color.white);
        setToolBarBackgroundColor(R.color.blue_1);

        rv_list = findViewById(R.id.rv_list);

        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.addItemDecoration(new SpaceItemDecoration(5));
        rv_list.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    FileBean fileBean = adapter.getFiles(i);
                    openFile(fileBean.getPath());
                }catch (Exception e){
                    ErrorUtil.showError(e);
                    ToastUtility.showToast("手机没有可打开该文件的应用程序");
                }

            }
        });
    }
    private void openFile(String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(path);
        if (file == null || !file.exists()) {
            ToastUtility.showToast("文件为空或者不存在");
            return ;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri= FileProvider.getUriForFile(getContext(),"com.zt.map",file);
        }else {
            uri= Uri.fromFile(file);
        }
        intent.setDataAndType(uri,"application/vnd.ms-excel");
        startActivity(intent);
    }


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void queryFiles_success(List<FileBean> files) {
        adapter.setFiles(files);
    }

    @Override
    public void queryFiles(String msg) {
        ToastUtility.showToast(msg);
    }
}
