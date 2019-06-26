package com.zt.map.constant;

import android.text.Editable;


import cn.faker.repaymodel.mvp.BaseMVPModel;

public class LoginContract {
    public interface View{
        void login_success();
        void login_fail(String msg);
        void settingUserInfo(String user, String passWord);
    }

    public interface Presenter{
        void login(Editable name, Editable pw);
    }

    public interface Model{
        void login(String name, String pw, BaseMVPModel.CommotListener<Boolean> listener);
    }
}
