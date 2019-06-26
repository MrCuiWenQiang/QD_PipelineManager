package com.zt.map.view.widget;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.map.R;

import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

public class IconEditDialog extends BasicDialog {
    private TextView tvClean;
    private TextView tvNice;

    private EditText tvName;
    private TextView tvFh;
    private onIconListener listener;

    public IconEditDialog setListener(onIconListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dg_iconedit;
    }

    @Override
    public void initview(View v) {

        tvClean = v.findViewById(R.id.tv_clean);
        tvNice = v.findViewById(R.id.tv_nice);

        tvName = v.findViewById(R.id.tv_name);
        tvFh = v.findViewById(R.id.tv_fh);
    }
    public void setFh(String name){
        tvFh.setText(name);
    }
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        tvFh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(IconEditDialog.this);
            }
        });
        tvNice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onIntext(tvName.getText());
            }
        });
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public interface onIconListener {
        void onIntext(Editable name);

        void onClick(BasicDialog dialog);
    }
}
