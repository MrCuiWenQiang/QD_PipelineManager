package com.zt.map.presenter;

import android.text.Editable;
import android.text.TextUtils;

import com.zt.map.constant.LoginContract;
import com.zt.map.model.SystemInitModel;

import cn.faker.repaymodel.mvp.BaseMVPModel;
import cn.faker.repaymodel.mvp.BaseMVPPresenter;


public class LoginPresenter extends BaseMVPPresenter<LoginContract.View> implements LoginContract.Presenter {
//    private LoginModel loginModel = new LoginModel();

    @Override
    public void attachView(LoginContract.View view) {
        super.attachView(view);
    }

    @Override
    public void login(Editable name, Editable pw) {
        if (TextUtils.isEmpty(name)) {
            getView().login_fail("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(pw)) {
            getView().login_fail("请输入密码");
            return;
        }

      /*  loginModel.login(name.toString(), pw.toString(), new BaseMVPModel.CommotListener<Boolean>() {
            @Override
            public void result(Boolean aBoolean) {
                if (aBoolean) {
                    if (getView() != null) {
                        getView().login_success();
                    }
                } else {
                    if (getView() != null) {
                        getView().login_fail("用户名或密码错误");
                    }
                }
            }
        });*/

    }



}
