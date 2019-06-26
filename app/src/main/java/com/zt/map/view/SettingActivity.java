package com.zt.map.view;

import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.zt.map.R;
import com.zt.map.contract.SettingContract;
import com.zt.map.presenter.SettingPresenter;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;

/**
 * 系统设置
 */
public class SettingActivity extends BaseMVPAcivity<SettingContract.View, SettingPresenter> implements SettingContract.View, View.OnClickListener {

    private QMUIGroupListView qmui_list;

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_setting;
    }

    @Override
    protected void initContentView() {
        setStatusBar(R.color.blue_1);
        setLeftTitle("系统设置", R.color.white);
        setToolBarBackgroundColor(R.color.blue_1);

        qmui_list = findViewById(R.id.qmui_list);
    }

    private final int ITEM_DATA_MARKER = 0;
    private final int ITEM_DATA_LINE = 1;

    @Override
    public void initData(Bundle savedInstanceState) {
        QMUICommonListItemView item_marker = qmui_list.createItemView("管点选项");
        item_marker.setDetailText("对管点选项数据修改");
        item_marker.setTag(ITEM_DATA_MARKER);

        QMUICommonListItemView item_line = qmui_list.createItemView("管线选项");
        item_line.setDetailText("对管线选项数据修改");
        item_line.setTag(ITEM_DATA_LINE);

        QMUIGroupListView.newSection(getContext())
                .setTitle("数据管理")
                .addItemView(item_marker, this)
                .addItemView(item_line, this).addTo(qmui_list);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof QMUICommonListItemView) {
            int tag = (int) v.getTag();
            switch (tag) {
                case ITEM_DATA_MARKER: {
                    toAcitvity(SettngMakerActivity.class);
                    break;
                }
                case ITEM_DATA_LINE: {
                    toAcitvity(SettingLineActivity.class);
                    break;
                }
            }
            return;
        }
        switch (v.getId()) {
        }
    }
}
