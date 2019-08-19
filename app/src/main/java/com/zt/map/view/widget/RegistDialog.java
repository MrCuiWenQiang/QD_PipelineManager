package com.zt.map.view.widget;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.map.R;
import com.zt.map.entity.db.system.Sys_RegisterInfo;

import cn.faker.repaymodel.util.UUIDUtil;
import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

/**
 * 发送注册信息
 */
public class RegistDialog extends BasicDialog {

    private EditText tv_code;

    private EditText tv_coltd;
    private EditText tv_linkman;
    private EditText tv_tel;
    private EditText tv_email;
    private EditText tv_remarks;


    private TextView tv_clean;
    private TextView tv_nice;

    private View[] infoViews;
    private View[] codeViews;

    private onRegisListener regisListener;

    private int type = 2;

    public RegistDialog setRegisListener(onRegisListener regisListener) {
        this.regisListener = regisListener;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dg_regist;
    }

    @Override
    public void initview(View v) {
        tv_code = v.findViewById(R.id.tv_code);

        tv_coltd = v.findViewById(R.id.tv_coltd);
        tv_linkman = v.findViewById(R.id.tv_linkman);
        tv_tel = v.findViewById(R.id.tv_tel);
        tv_email = v.findViewById(R.id.tv_email);
        tv_remarks = v.findViewById(R.id.tv_remarks);

        tv_clean = v.findViewById(R.id.tv_clean);
        tv_nice = v.findViewById(R.id.tv_nice);

        infoViews = new View[]{tv_coltd, tv_linkman, tv_tel
                , tv_email, tv_remarks};
        codeViews = new View[]{tv_code};
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    private void showInfo() {
        type =2;
        tv_clean.setText("验证码注册");
        for (View childView:infoViews) {
            ViewGroup vg = (ViewGroup) childView.getParent();
            vg.setVisibility(View.VISIBLE);
        }
        for (View childView:codeViews) {
            ViewGroup vg = (ViewGroup) childView.getParent();
            vg.setVisibility(View.GONE);
        }
    }

    private void showCode() {
        type =1;
        tv_clean.setText("审核信息注册");
        for (View childView:codeViews) {
            ViewGroup vg = (ViewGroup) childView.getParent();
            vg.setVisibility(View.VISIBLE);
        }
        for (View childView:infoViews) {
            ViewGroup vg = (ViewGroup) childView.getParent();
            vg.setVisibility(View.GONE);
        }
    }
    private boolean isCode(){//是否是验证码模式
        View viewGroup = (View) tv_code.getParent();
        return viewGroup.getVisibility()==View.VISIBLE;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCode()){
                    showInfo();
                }else {
                    showCode();
                }
            }
        });
        tv_nice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    void addData() {
        Sys_RegisterInfo info = new Sys_RegisterInfo();
        info.setColtd(getValue(tv_coltd));
        info.setLinman(getValue(tv_linkman));
        info.setTel(getValue(tv_tel));
        info.setEmail(getValue(tv_email));
        info.setRemarks(getValue(tv_remarks));
        info.setType(type);
        info.setCode(getValue(tv_code));
        info.setAndroidUUID(UUIDUtil.getUUID());
        regisListener.onRegistInfo(info);
    }

    protected String getValue(EditText text) {
        if (text == null) return null;
        if (TextUtils.isEmpty(text.getText())) {
            return null;
        } else {
            return text.getText().toString();
        }
    }

    public interface onRegisListener {
        void onRegistInfo(Sys_RegisterInfo info);
    }
}
