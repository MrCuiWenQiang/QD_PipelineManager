package com.zt.map.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zt.map.R;
import com.zt.map.contract.SettingLineContract;
import com.zt.map.presenter.SettingLinePresenter;
import com.zt.map.view.adapter.SettingMarkerAdapter;

import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;

/**
 * 管线设置页面
 */
public class SettingLineActivity extends BaseMVPAcivity<SettingLineContract.View, SettingLinePresenter>
        implements SettingLineContract.View{

    private TextView tvLineType;
    private RecyclerView rvList;

    private SettingMarkerAdapter adapter = new SettingMarkerAdapter();


    private String[] tabNames;
    private Long[] typeIds;
    private String typeCode;
    private long typeId;

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_setting_line;
    }

    @Override
    protected void initContentView() {
        setStatusBar(R.color.blue_1);
        setTitle("管线设置", R.color.white);
        setToolBarBackgroundColor(R.color.blue_1);

        tvLineType = findViewById(R.id.tv_line_type);
        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        tvLineType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog(tabNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String typeCode = tabNames[which];
                        typeId = typeIds[which];
                        tvLineType.setText(typeCode);
                        SettingLineActivity.this.typeCode = typeCode;
                        showLoading();
                        mPresenter.queryData(typeId);
                    }
                });
            }
        });

        adapter.setOnItemSettingListener(new SettingMarkerAdapter.OnItemSettingListener() {
            @Override
            public void onDelete(final String fatherName, final String itemName, final int position) {
                new QMUIDialog.MessageDialogBuilder(getContext()).setMessage("是否删除" + itemName + "选择项?").addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.delete(fatherName, itemName, position);
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).show();
            }

            @Override
            public void onRevise(final String fatherName, final String itemName, final int position) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
                builder.setTitle("修改选项");
                builder.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.addAction("修改", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.saveOrRevise(typeCode, builder.getEditText().getText(), fatherName, itemName, position);
                    }
                });
                builder.addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                builder.getEditText().setText(itemName);
            }

            @Override
            public void onSave(final String fatherName) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
                builder.setTitle("添加选项");
                builder.setPlaceholder("在此输入新选项");
                builder.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.addAction("添加", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.saveOrRevise(typeCode, builder.getEditText().getText(), fatherName, null, -1);
                    }
                });
                builder.addAction(0, "取消", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void queryType(String[] tabNames, Long[] typeIds) {
        this.tabNames = tabNames;
        this.typeIds = typeIds;
        tvLineType.setText(tabNames[0]);
        mPresenter.queryData(typeIds[0]);
        typeCode = tabNames[0];
        typeId = typeIds[0];
    }

    @Override
    public void queryDb(Map<String, List<String>> dataMap) {
        dimiss();
        adapter.setDataMap(dataMap);
    }

    @Override
    public void openSuccess() {
        showLoading();
        mPresenter.queryData(typeId);
    }

    @Override
    public void openFail(String msg) {
        dimiss();
        ToastUtility.showToast(msg);
    }
}
