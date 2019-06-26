package com.zt.map.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.map.R;
import com.zt.map.constant.LoginContract;
import com.zt.map.presenter.LoginPresenter;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;

public class LoginActivity extends BaseMVPAcivity<LoginContract.View, LoginPresenter> implements LoginContract.View, View.OnClickListener {
    private EditText ed_name;
    private EditText ed_password;
    private TextView bt_tologin;
    private TextView tv_regisit;


    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_new_login;
    }

    @Override
    protected void initContentView() {
        isShowToolView(false);
        setStatusBar(R.color.blue_1);
        ed_name = findViewById(R.id.ed_name);
        ed_password = findViewById(R.id.ed_password);
        bt_tologin = findViewById(R.id.bt_tologin);
        tv_regisit = findViewById(R.id.tv_regisit);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        bt_tologin.setOnClickListener(this);
        tv_regisit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_tologin: {
                showLoading();
                mPresenter.login(ed_name.getText(), ed_password.getText());
                break;
            }
         /*   case R.id.tv_regisit: {
                toAcitvity(RegisteActivity.class);
                break;
            }*/
        }
    }

    @Override
    public void login_success() {
        dimiss();
        finish();
        toAcitvity(MainActivity.class);
    }

    @Override
    public void login_fail(String msg) {
        dimiss();
        ToastUtility.showToast(msg);
    }

    @Override
    public void settingUserInfo(String user, String passWord) {
        if (TextUtils.isEmpty(ed_name.getText())&&TextUtils.isEmpty(ed_password.getText())){
            ed_name.setText(user);
            ed_password.setText(passWord);
        }
    }
}
