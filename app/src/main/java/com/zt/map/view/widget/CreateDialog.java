package com.zt.map.view.widget;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.map.R;

import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

public class CreateDialog extends BasicDialog implements View.OnClickListener {

    private TextView tv_create;
    private TextView tv_clean;
    private CheckBox cb_create;
    private EditText et_name;

    private OnCreateListener listener;

    @Override
    public int getLayoutId() {
        return R.layout.dg_create;
    }

    @Override
    public void initview(View v) {
        tv_create = v.findViewById(R.id.tv_create);
        tv_clean = v.findViewById(R.id.tv_clean);
        cb_create = v.findViewById(R.id.cb_create);
        et_name = v.findViewById(R.id.et_name);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1,-2 );
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_create.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create: {
                listener.onCreateProject(cb_create.isChecked(),et_name.getText());

            }
            case R.id.tv_clean: {
                dismiss();
            }
        }
    }



    public void setListener(OnCreateListener listener) {
        this.listener = listener;
    }

    public interface OnCreateListener{
        void onCreateProject(boolean isSelect,Editable name);
    }
}
