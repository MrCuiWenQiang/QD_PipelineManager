package com.zt.map.view.widget;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zt.map.R;

import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

public class MeasureNameDialog extends BasicDialog {
    private TextView tvClean;
    private TextView tvNice;
    private String cl;

    private EditText tvFh;
    private onIconListener listener;

    public MeasureNameDialog setListener(onIconListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dg_markermeasure;
    }

    @Override
    public void initview(View v) {

        tvClean = v.findViewById(R.id.tv_clean);
        tvNice = v.findViewById(R.id.tv_nice);

        tvFh = v.findViewById(R.id.tv_fh);

        tvFh.setText(cl);
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        tvNice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onIntext(tvFh.getText());
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

    }
}
