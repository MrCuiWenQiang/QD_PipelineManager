package com.zt.map.view;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zt.map.R;
import com.zt.map.contract.SettingMarkerContract;
import com.zt.map.model.MarkerModel;
import com.zt.map.presenter.SettingMarkerPresenter;
import com.zt.map.view.adapter.SettingMarkerAdapter;
import com.zt.map.view.widget.IconEditDialog;
import com.zt.map.view.widget.IconListDialog;

import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;
import cn.faker.repaymodel.widget.view.dialog.BasicDialog;

/**
 * 管点数据修改
 */
public class SettngMakerActivity extends BaseMVPAcivity<SettingMarkerContract.View, SettingMarkerPresenter>
        implements SettingMarkerContract.View, View.OnClickListener {

    private RecyclerView rv_list;
    private SettingMarkerAdapter adapter = new SettingMarkerAdapter();

    private TextView tv_marker_type;

    private String[] tabNames;
    private Long[] typeIds;
    private String typeCode;
    private long typeId;

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_setting_marker;
    }

    @Override
    protected void initContentView() {
        setStatusBar(R.color.blue_1);
        setTitle("管点设置", R.color.white);
        setToolBarBackgroundColor(R.color.blue_1);

        tv_marker_type = findViewById(R.id.tv_marker_type);

        rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        showLoading();
        mPresenter.queryType();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_marker_type.setOnClickListener(this);

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
                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TZDS) || fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_FSWS)) {
                    mPresenter.queryIcons(typeId, fatherName, itemName, position);
                } else {
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
            }

            @Override
            public void onSave(final String fatherName) {
                if (fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_TZDS) || fatherName.equals(MarkerModel.TABLE_MARKER_TYPE_FSWS)) {
                    mPresenter.queryIcons(typeId, fatherName, null, -1);
                } else {
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
            }
        });
    }

    @Override
    public void queryType(String[] tabNames, Long[] typeIds) {
        this.tabNames = tabNames;
        this.typeIds = typeIds;
        tv_marker_type.setText(tabNames[0]);
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

    String iconname=null;

    @Override
    public void queryIcons(final Map<String, Bitmap> bitmap, final String f, final String itemName, final int position) {
        iconname = null;
        new IconEditDialog().setListener(new IconEditDialog.onIconListener() {
            @Override
            public void onIntext(Editable name) {
                mPresenter.saveOrRevise(typeCode, name,iconname,iconname,f,itemName,position);
            }

            @Override
            public void onClick(final BasicDialog dialog) {
                new IconListDialog().setBitmap(bitmap).setOnItemClickListener(new IconListDialog.onIconDialogItem() {
                    @Override
                    public void onClick(String name) {
                        ((IconEditDialog) dialog).setFh(name);
                        iconname= name;
                    }
                }).show(getSupportFragmentManager(), "far");
            }
        }).show(getSupportFragmentManager(), "iconn");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_marker_type: {
                showListDialog(tabNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String typeCode = tabNames[which];
                        typeId = typeIds[which];
                        tv_marker_type.setText(typeCode);
                        SettngMakerActivity.this.typeCode = typeCode;
                        showLoading();
                        mPresenter.queryData(typeId);
                    }
                });
            }
        }
    }
}
