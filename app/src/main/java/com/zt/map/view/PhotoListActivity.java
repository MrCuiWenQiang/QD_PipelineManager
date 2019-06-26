package com.zt.map.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.zt.map.R;
import com.zt.map.contract.PhotoContract;
import com.zt.map.entity.db.tab.Tab_marker_photo;
import com.zt.map.presenter.PhotoPresenter;
import com.zt.map.view.adapter.PhotoAdapter;

import java.io.Serializable;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;

public class PhotoListActivity extends BaseMVPAcivity<PhotoContract.View, PhotoPresenter> implements PhotoContract.View {

    public static final String KEY_PROJECTID = "KEY_PROJECTID";
    public static final String KEY_SELECTSTATUS = "KEY_SELECTSTATUS";

    private RecyclerView rv_list;

    private PhotoAdapter adapter = new PhotoAdapter();

    public static Bundle newInstance(long projectId) {
        Bundle args = new Bundle();
        args.putLong(KEY_PROJECTID, projectId);
        return args;
    }

    public static Bundle newInstance(long projectId, boolean isSelect) {
        Bundle args = new Bundle();
        args.putLong(KEY_PROJECTID, projectId);
        args.putBoolean(KEY_SELECTSTATUS, isSelect);
        return args;
    }

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_photos;
    }

    @Override
    protected void initContentView() {
        setTitle("工程图片", R.color.white);
        setToolBarBackgroundColor(R.color.blue);
        rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rv_list.setAdapter(adapter);
    }

    boolean selectStatus = false;

    @Override
    public void initData(Bundle savedInstanceState) {
        long projectId = getIntent().getLongExtra(KEY_PROJECTID, -1);
        showLoading();
        mPresenter.queryPhoto(projectId);
        selectStatus = getIntent().getBooleanExtra(KEY_SELECTSTATUS, false);
        adapter.setSelectStatus(selectStatus);

        if (selectStatus){
            setRightBtn("选择完成",R.color.white, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("data", (Serializable) adapter.getSelectData());
                    setResult(120,intent);
                    finish();
                }
            });

        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void queryPhotos(List<Tab_marker_photo> datas) {
        dimiss();
        adapter.setDatas(datas);
    }
}
