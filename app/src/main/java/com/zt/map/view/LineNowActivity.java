package com.zt.map.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zt.map.R;
import com.zt.map.contract.LineContract;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.presenter.LinePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;

/**
 * 管线编辑
 */
public class LineNowActivity extends BaseMVPAcivity<LineContract.View, LinePresenter> implements LineContract.View, View.OnClickListener {

    private static final String KEY_ID = "KEY_ID";
    private static final String PROJECT_ID = "PROJECT_ID";
    private static final String TYPE_ID = "TYPE_ID";


    public static final String KEY_STARTLATLNG_X = "KEY_STARTLATLNG_X";
    public static final String KEY_STARTLATLNG_Y = "KEY_STARTLATLNG_Y";
    public static final String KEY_ENDLATLNG_X = "KEY_ENDLATLNG_X";
    public static final String KEY_ENDLATLNG_Y = "KEY_ENDLATLNG_Y";

    private EditText tvGxlx;
    private ImageView ivLoadGxlx;
    private EditText tvQswh;
    private EditText tvZzwh;
    private EditText tvQdms;
    private EditText tvZzms;
    private EditText tvMsfs;
    private ImageView ivLoadMsfs;
    private EditText tvGjdm;
    private EditText tvGxcl;
    private ImageView ivLoadGxcl;
    private ImageView iv_load_tgcz;

    private EditText tvYxzt;
    private ImageView ivLoadYxzt;
    private ImageView ivLoadDhgd;
    private EditText tvJsnd;
    private EditText tvQsdw;
    private EditText tvSzwz;
    private ImageView ivLoadSzwz;
    private EditText tvSyzt;
    private ImageView ivLoadSyzt;
    private EditText tvTcfs;
    private ImageView ivLoadTcfs;
    private EditText tvRemarks;

    private LinearLayout llLx;
    private LinearLayout llQdyj;
    private LinearLayout llZdyj;
    private LinearLayout llDhgd;

    private ViewStub vs_dx_dx;
    private ViewStub vs_dx_ps;
    private ViewStub vs_rq;
    private ViewStub vs_dy;

    private TextView tvSave;
    private TextView tvExit;

    private EditText tv_gxfc;
    private ImageView iv_load_gxfc;

    private long projectId;
    private long typeId;

    private double start_latitude;
    private double start_longitude;
    private double end_latitude;
    private double end_longitude;

    private long start_marker;
    private long end__marker;

    private EditText tvXx;
    private ImageView ivLoadXx;
    private EditText tvTgcz;
    private EditText tvSfly;
    private ImageView ivLoadSfly;


    public static Bundle newInstance(Long projectId, long typeId) {
        Bundle args = new Bundle();
        args.putLong(PROJECT_ID, projectId);
        args.putLong(TYPE_ID, typeId);
        return args;
    }

    public static void newInstance(Activity activity, long lineId, int requestCode) {
        Bundle args = new Bundle();
        args.putLong(KEY_ID, lineId);
        Intent intent = new Intent(activity, LineNowActivity.class);
        intent.putExtras(args);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_now_line;
    }

    @Override
    protected void initContentView() {
        tvGxlx = findViewById(R.id.tv_gxlx);
        ivLoadGxlx = findViewById(R.id.iv_load_gxlx);
        tvQswh = findViewById(R.id.tv_qswh);
        tvZzwh = findViewById(R.id.tv_zzwh);
        tvQdms = findViewById(R.id.tv_qdms);
        tvZzms = findViewById(R.id.tv_zzms);
        tvMsfs = findViewById(R.id.tv_msfs);
        ivLoadMsfs = findViewById(R.id.iv_load_msfs);
        tvGjdm = findViewById(R.id.tv_gjdm);
        tvGxcl = findViewById(R.id.tv_gxcl);
        ivLoadGxcl = findViewById(R.id.iv_load_gxcl);
        tvYxzt = findViewById(R.id.tv_yxzt);
        ivLoadYxzt = findViewById(R.id.iv_load_yxzt);
        iv_load_tgcz = findViewById(R.id.iv_load_tgcz);
        tvJsnd = findViewById(R.id.tv_jsnd);
        tvQsdw = findViewById(R.id.tv_qsdw);
        tvSzwz = findViewById(R.id.tv_szwz);
        ivLoadSzwz = findViewById(R.id.iv_load_szwz);
        tvSyzt = findViewById(R.id.tv_syzt);
        ivLoadSyzt = findViewById(R.id.iv_load_syzt);
        tvTcfs = findViewById(R.id.tv_tcfs);
        ivLoadTcfs = findViewById(R.id.iv_load_tcfs);
        tvRemarks = findViewById(R.id.tv_remarks);
        vs_dx_dx = findViewById(R.id.vs_dx_dx);
        vs_dx_ps = findViewById(R.id.vs_dx_gd);
        vs_rq = findViewById(R.id.vs_rq);
        vs_dy = findViewById(R.id.vs_dy);

        tv_gxfc = findViewById(R.id.tv_gxfc);
        iv_load_gxfc = findViewById(R.id.iv_load_gxfc);


        tvSave = findViewById(R.id.tv_save);
        tvExit = findViewById(R.id.tv_exit);

        tvXx = findViewById(R.id.tv_xx);
        ivLoadXx = findViewById(R.id.iv_load_xx);
        tvTgcz = findViewById(R.id.tv_tgcz);
        tvSfly = findViewById(R.id.tv_sfly);
        ivLoadSfly = findViewById(R.id.iv_load_sfly);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setLeftTitle("绘制管线", R.color.white);
        setToolBarBackgroundColor(R.color.blue);

        long lineId = getIntent().getLongExtra(KEY_ID, -1);
        if (lineId > 0) {
            showLoading();
            mPresenter.queryLine(lineId);
        } else {
            projectId = getIntent().getLongExtra(PROJECT_ID, -1);
            typeId = getIntent().getLongExtra(TYPE_ID, -1);

            start_latitude = getIntent().getDoubleExtra(KEY_STARTLATLNG_X, -1);
            start_longitude = getIntent().getDoubleExtra(KEY_STARTLATLNG_Y, -1);
            end_latitude = getIntent().getDoubleExtra(KEY_ENDLATLNG_X, -1);
            end_longitude = getIntent().getDoubleExtra(KEY_ENDLATLNG_Y, -1);


            showLoading();
            mPresenter.queryTopType(projectId, typeId);
            mPresenter.queryStartAndEndMarer(projectId, typeId, start_latitude, start_longitude, end_latitude, end_longitude);
//            mPresenter.queryUncertainData(typeId);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        ivLoadMsfs.setOnClickListener(this);
        ivLoadGxlx.setOnClickListener(this);
        ivLoadSfly.setOnClickListener(this);

        tvSave.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        ivLoadSyzt.setOnClickListener(this);
        ivLoadGxcl.setOnClickListener(this);
        ivLoadXx.setOnClickListener(this);
        iv_load_gxfc.setOnClickListener(this);
        iv_load_tgcz.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_load_gxlx: {
                mPresenter.queryType(typeId);
                break;
            }
            case R.id.iv_load_gxcl: {
                mPresenter.queryCZ(typeId);
                break;
            }

            case R.id.iv_load_msfs: {
                mPresenter.queryMsfs(typeId);
                break;
            }
            case R.id.iv_load_syzt: {
                mPresenter.queryUseStatus(typeId);
                break;
            }
            case R.id.iv_load_sfly: {
                mPresenter.querySfly();
                break;
            }
            case R.id.iv_load_xx: {
                mPresenter.queryXX();
                break;
            }
            case R.id.iv_load_gxfc: {
                mPresenter.queryGXFC(typeId);
                break;
            }

            case R.id.tv_save: {
                save();
                break;
            }
            case R.id.tv_exit: {
                finish();
                break;
            }
            case R.id.iv_load_tgcz: {//套管材质
                break;
            }
        }
    }

    private Tab_Line tab_line;

    private void save() {
        if (tab_line == null) {
            tab_line = new Tab_Line();
            tab_line.setCreateTime(new Date());
        }
        tab_line.setProjectId(projectId);
        tab_line.setTypeId(typeId);

        tab_line.setStartMarkerId(getTAGtoLong(tvQswh));
        tab_line.setEndMarkerId(getTAGtoLong(tvZzwh));

        tab_line.setStart_latitude(start_latitude);
        tab_line.setStart_longitude(start_longitude);
        tab_line.setEnd_latitude(end_latitude);
        tab_line.setEnd_longitude(end_longitude);

        tab_line.setGxlx(getValue(tvGxlx));
        tab_line.setGxoutlx(getValue(tvGxlx));
        tab_line.setGxfc(getValue(tv_gxfc));
        tab_line.setQdms(getValue(tvQdms));
        tab_line.setZzms(getValue(tvZzms));
        tab_line.setMsfs(getValue(tvMsfs));
        tab_line.setGjdm(getValue(tvGjdm));
        tab_line.setGxcl(getValue(tvGxcl));

        tab_line.setLx(getValue(tvLx));

        tab_line.setQdyj(getValue(tvQdyj));
        tab_line.setZdyj(getValue(tvZdyj));
        tab_line.setYxzt(getValue(tvYxzt));
        tab_line.setJsnd(getValue(tvJsnd));
        tab_line.setQsdw(getValue(tvQsdw));
        tab_line.setSzwz(getValue(tvSzwz));
        tab_line.setSyzt(getValue(tvSyzt));
        tab_line.setTcfs(getValue(tvTcfs));

        tab_line.setRemarks(getValue(tvRemarks));

        tab_line.setUpdateTime(new Date());


        tab_line.setLx(getValue(tvLx));
        tab_line.setYl(getValue(tv_yl));
        tab_line.setDhgd(getValue(tvDhgd));
        tab_line.setQdyj(getValue(tvQdyj));
        tab_line.setZdyj(getValue(tvZdyj));
        tab_line.setYxzt(getValue(tvYxzt));
        tab_line.setSsyxzt(getValue(ntv_yxzt));
        tab_line.setGxzl(getValue(tv_gxzl));
        tab_line.setGddj(getValue(tv_gddj));
        tab_line.setYhqk(getValue(tv_yhqk));

        tab_line.setZks(getValue(tv_zks));
        tab_line.setYyks(getValue(tv_yyks));
        tab_line.setTs(getValue(tv_ts));

        tab_line.setYl(getValue(tv_yl));
        tab_line.setDy(getValue(tv_dy));

        tab_line.setXx(getValue(tvXx));
//        tab_line.setTs(getValue(tvTs));
        tab_line.setTgcz(getValue(tvTgcz));
        tab_line.setSfly(getValue(tvSfly));


        showLoading();
        mPresenter.save(tab_line);
    }

    @Override
    public void saveSuccess() {
        dimiss();
        finish();
    }

    @Override
    public void saveFail(String msg) {
        dimiss();
        ToastUtility.showToast(msg);
    }

    @Override
    public void queryLine_Success(Tab_Line tabLine) {
        dimiss();
        this.tab_line = tabLine;

        projectId = tabLine.getProjectId();
        typeId = tabLine.getTypeId();

        start_latitude = tabLine.getStart_latitude();
        start_longitude = tabLine.getStart_longitude();
        end_latitude = tabLine.getEnd_latitude();
        end_longitude = tabLine.getEnd_longitude();


        tvQswh.setText(tabLine.getQswh());
        tvQswh.setTag(tabLine.getStartMarkerId());
        tvZzwh.setText(tabLine.getZzwh());
        tvZzwh.setTag(tabLine.getEndMarkerId());

        tvQdms.setText(tabLine.getQdms());
        tvGxlx.setText(tabLine.getGxoutlx());
        tv_gxfc.setText(tabLine.getGxfc());
        tvZzms.setText(tabLine.getZzms());
        tvMsfs.setText(tabLine.getMsfs());
        tvGjdm.setText(tabLine.getGjdm());
        tvGxcl.setText(tabLine.getGxcl());

        tvYxzt.setText(tabLine.getYxzt());

        tvJsnd.setText(tabLine.getJsnd());
        tvQsdw.setText(tabLine.getQsdw());
        tvSzwz.setText(tabLine.getSzwz());
        tvSyzt.setText(tabLine.getSyzt());
        tvTcfs.setText(tabLine.getTcfs());

        tvRemarks.setText(tabLine.getRemarks());

        tvXx.setText(tabLine.getXx());
        tvTgcz.setText(tabLine.getTgcz());
        tvSfly.setText(tabLine.getSfly());

//        mPresenter.queryUncertainData(typeId);
        mPresenter.queryShowType(typeId);
    }

    @Override
    public void queryLine_Fail(String msg) {
        ToastUtility.showToast(msg);
    }

    @Override
    public void query_remarks(String[] items) {
        selectValue(tvRemarks, items);
    }

    @Override
    public void query_LineType(String[] items) {
        selectValue(tvGxlx, items);
    }

    @Override
    public void query_msfs(String[] items) {
        selectValue(tvMsfs, items);
    }


    private Tab_Line topLine;

    @Override
    public void queryTopType(Tab_Line tabLine) {
        dimiss();
        mPresenter.queryShowType(typeId);

        topLine = tabLine;

        if (tabLine == null) return;

        tvGxlx.setText(tabLine.getGxoutlx());
        //上一条线的终点埋深默认为下一条线的起点埋深
        tvQdms.setText(tabLine.getZzms());
//        tvZzms.setText(tabLine.getZzms());
        tvMsfs.setText(tabLine.getMsfs());
        tvGjdm.setText(tabLine.getGjdm());
        tvGxcl.setText(tabLine.getGxcl());

        tvYxzt.setText(tabLine.getYxzt());
        tvJsnd.setText(tabLine.getJsnd());
        tvQsdw.setText(tabLine.getQsdw());
        tvSzwz.setText(tabLine.getSzwz());
        tvSyzt.setText(tabLine.getSyzt());
        tvTcfs.setText(tabLine.getTcfs());

        tvXx.setText(tabLine.getXx());
        tvTgcz.setText(tabLine.getTgcz());
        tvSfly.setText(tabLine.getSfly());

    }

    @Override
    public void queryUncertainData(String[] tgcls, String[] pressures, String[] directions) {
    }

    private EditText tvLx;
    private ImageView ivLoadLx;
    private EditText tvQdyj;
    private EditText tvZdyj;
    private EditText tv_yhqk;
    private EditText tvDhgd;

    private EditText ntv_yxzt;
    private ImageView niv_load_yxzt;
    private EditText tv_gxzl;
    private ImageView iv_load_gxzl;
    private EditText tv_gddj;
    private ImageView iv_load_gddj;

    @Override
    public void visiblePS(final String[] lx, final String[] dhgd, final String[] ssyxzt, final String[] gxzl, final String[] gddj) {
        vs_dx_ps.inflate();

        llLx = findViewById(R.id.ll_lx);
        llDhgd = findViewById(R.id.ll_dhgd);
        tvLx = findViewById(R.id.tv_lx);
        ivLoadLx = findViewById(R.id.iv_load_lx);
        ivLoadDhgd = findViewById(R.id.iv_load_dhgd);
        llQdyj = findViewById(R.id.ll_qdyj);
        tvQdyj = findViewById(R.id.tv_qdyj);
        llZdyj = findViewById(R.id.ll_zdyj);
        tvZdyj = findViewById(R.id.tv_zdyj);
        tvDhgd = findViewById(R.id.tv_dhgd);
        tv_yhqk = findViewById(R.id.tv_yhqk);
        ntv_yxzt = findViewById(R.id.ntv_yxzt);
        tv_gxzl = findViewById(R.id.tv_gxzl);
        tv_gddj = findViewById(R.id.tv_gddj);
        niv_load_yxzt = findViewById(R.id.niv_load_yxzt);
        iv_load_gxzl = findViewById(R.id.iv_load_gxzl);
        iv_load_gddj = findViewById(R.id.iv_load_gddj);

        ivLoadLx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tvLx, lx);
            }
        });
        ivLoadDhgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tvDhgd, dhgd);
            }
        });
        // TODO: 2019/6/18 修改数据源

        niv_load_yxzt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(ntv_yxzt, ssyxzt);
            }
        });
        iv_load_gxzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tv_gxzl, gxzl);
            }
        });
        iv_load_gddj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tv_gddj, gddj);
            }
        });
        Tab_Line data = topLine == null ? tab_line : topLine;
        if (data != null) {
            tvLx.setText(data.getLx());
            tvDhgd.setText(data.getDhgd());
            tvQdyj.setText(data.getQdyj());
            tvZdyj.setText(data.getZdyj());
            ntv_yxzt.setText(data.getYxzt());
            tv_gxzl.setText(data.getGxzl());
            tv_gddj.setText(data.getGddj());
            tv_yhqk.setText(data.getYhqk());
        }
    }

    private EditText tv_zks;
    private EditText tv_yyks;
    private EditText tv_ts;

    @Override
    public void visibleDX() {
        vs_dx_dx.inflate();

        tv_zks = findViewById(R.id.tv_zks);
        tv_yyks = findViewById(R.id.tv_yyks);
        tv_ts = findViewById(R.id.tv_ts);


        Tab_Line data = topLine == null ? tab_line : topLine;
        if (data != null) {
            tv_zks.setText(data.getZks());
            tv_yyks.setText(data.getYyks());
            tv_ts.setText(data.getTs());
        }
    }

    private EditText tv_yl;
    private ImageView iv_load_yl;

    @Override
    public void visibleRQ(final String[] yl) {
        vs_rq.inflate();
        tv_yl = findViewById(R.id.tv_yl);
        iv_load_yl = findViewById(R.id.iv_load_yl);
        iv_load_yl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tv_yl, yl);
            }
        });
        Tab_Line data = topLine == null ? tab_line : topLine;
        if (data != null) {
            tv_yl.setText(data.getYl());
        }
    }

    private EditText tv_dy;

    private ImageView iv_load_dy;

    @Override
    public void visiblegd(final String[] dy) {
        vs_dy.inflate();
        tv_zks = findViewById(R.id.tv_zks);
        tv_yyks = findViewById(R.id.tv_yyks);
        tv_ts = findViewById(R.id.tv_ts);
        tv_dy = findViewById(R.id.tv_dy);
        iv_load_dy = findViewById(R.id.iv_load_dy);
        iv_load_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectValue(tv_dy, dy);
            }
        });
        Tab_Line data = topLine == null ? tab_line : topLine;
        if (data != null) {
            tv_dy.setText(data.getDy());
            tv_zks.setText(data.getZks());
            tv_yyks.setText(data.getYyks());
            tv_ts.setText(data.getTs());
        }
    }

    @Override
    public void queryUseStatus(String[] items) {
        selectValue(tvSyzt, items);
    }

    @Override
    public void queryCZ(String[] items) {
        selectValue(tvGxcl, items);
    }

    @Override
    public void querySfly(String[] items) {
        selectValue(tvSfly, items);
    }

    @Override
    public void queryXX(String[] items) {
        selectValue(tvXx, items);
    }

    @Override
    public void queryGXFC(String[] items) {
        selectValue(tv_gxfc, items);
    }

    @Override
    public void fail(String msg) {
        ToastUtility.showToast(msg);
    }

    @Override
    public void queryStartAndEndMarer(Tab_Marker sm, Tab_Marker em) {
        dimiss();
        if (sm != null) {
            start_marker = sm.getId();
            tvQswh.setText(sm.getWtdh());
            tvQswh.setTag(start_marker);
            start_latitude = sm.getLatitude();
            start_longitude = sm.getLongitude();
        }
        if (em != null) {
            end__marker = em.getId();
            tvZzwh.setText(em.getWtdh());
            tvZzwh.setTag(end__marker);

            end_latitude = em.getLatitude();
            end_longitude = em.getLongitude();
        }
    }


    private void selectValue(final TextView tv, final String[] items) {
        if (items == null || items.length <= 0) {
            ToastUtility.showToast("暂无分类");
            return;
        }
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.v_tablist, new ArrayList<>(Arrays.asList(items)));
        final QMUIListPopup mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_BOTTOM, adapter);
        mListPopup.create(tv.getWidth(), QMUIDisplayHelper.dp2px(getContext(), 200),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv.setText(items[position]);
                        mListPopup.dismiss();
                    }
                });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.show(tv);
    }
}
