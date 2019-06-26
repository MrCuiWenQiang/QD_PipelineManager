package com.zt.map.view;

import android.os.Bundle;

import com.zt.map.R;
import com.zt.map.contract.StartContract;
import com.zt.map.presenter.StartPresenter;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.CountDownUtil;
import cn.faker.repaymodel.util.ToastUtility;

public class StartActivity extends BaseMVPAcivity<StartContract.View,StartPresenter> implements StartContract.View {
    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_start;
    }

    @Override
    protected void initContentView() {
        isShowToolView(false);
        setStatusBar(R.color.white);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.initData();
    }

    @Override
    public void initData_success() {
        CountDownUtil.countTextView(3, 1, new CountDownUtil.TimerListener() {
            @Override
            public void onCreate() {

            }

            @Override
            public void onStart(int newtime, int difference) {

            }

            @Override
            public void onEnd() {
                toMain();
            }
        });
    }

    @Override
    public void initData_fail(String msg) {
        ToastUtility.showToast("初始化数据失败");
        finish();
    }

    private void toMain(){
        CountDownUtil.stop();
        toAcitvity(MainActivity.class);
        finish();
    }
}
