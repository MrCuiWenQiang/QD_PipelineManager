package com.zt.map.view;

import android.os.Bundle;
import android.view.TextureView;

import com.zt.map.R;


import cn.faker.repaymodel.activity.BaseActivity;

/**
 * 自定义拍照
 */
public class PhotoActivity extends BaseActivity {

    private TextureView mTextureView;



    @Override
    public int getLayoutId() {
        return R.layout.ac_photo;
    }

    @Override
    public void initView() {
        mTextureView = findViewById(R.id.ttv);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }





}
