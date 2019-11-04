package com.zt.map.view;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zt.map.R;
import com.zt.map.contract.MarkerContract;
import com.zt.map.entity.db.PhotoInfo;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_marker_photo;
import com.zt.map.presenter.MarkerPresenter;
import com.zt.map.util.PhotoUtil;
import com.zt.map.view.widget.AutoCaseTransformationMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;

public class MarkerNowActivity extends BaseMVPAcivity<MarkerContract.View, MarkerPresenter> implements MarkerContract.View, View.OnClickListener {

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ID = "KEY_ID";
    private static final String PROJECT_ID = "PROJECT_ID";
    private static final String TYPE_ID = "TYPE_ID";
    private static final String LINEID = "lineId";

    public static void newInstance(Activity activity, Long projectId, long typeId, double latitude, double longitude, int requestCode) {
        Bundle args = new Bundle();
        args.putDouble(KEY_LATITUDE, latitude);
        args.putDouble(KEY_LONGITUDE, longitude);
        args.putLong(PROJECT_ID, projectId);
        args.putLong(TYPE_ID, typeId);
        Intent intent = new Intent(activity, MarkerNowActivity.class);
        intent.putExtras(args);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void newInstance(Activity activity, Long projectId, long typeId, double latitude, double longitude, long lineId, int requestCode) {
        Bundle args = new Bundle();
        args.putDouble(KEY_LATITUDE, latitude);
        args.putDouble(KEY_LONGITUDE, longitude);
        args.putLong(PROJECT_ID, projectId);
        args.putLong(TYPE_ID, typeId);
        args.putLong(LINEID, lineId);
        Intent intent = new Intent(activity, MarkerNowActivity.class);
        intent.putExtras(args);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void newInstance(Activity activity, long makerId, int requestCode) {
        Bundle args = new Bundle();
        args.putLong(KEY_ID, makerId);
        Intent intent = new Intent(activity, MarkerNowActivity.class);
        intent.putExtras(args);
        activity.startActivityForResult(intent, requestCode);
    }

    private EditText tvGxlx;
    private EditText tvWtdh;
    private TextView tvTz;
    private ImageView ivLoadTzd;
    private TextView tvFsw;
    private ImageView ivLoadFsw;
    private EditText tvX;
    private EditText tvY;
    private EditText tvDmgc;
    private EditText tvPxjw;
    private EditText tv_gdfc;
    private EditText tv_pj;
    private EditText tvJlx;
    private EditText tvJzj;
    private EditText tvJbs;
    private EditText tvJds;
    private TextView tvJglx;
    private ImageView ivLoadJglx;
    private EditText tvJggg;
    private EditText tv_jggg_width;
    private TextView tvJgcz;
    private ImageView ivLoadJgcz;
    private EditText tvSzwz;
    private ImageView ivLoadSzwz;
    private EditText tv_jgzt;
    private ImageView ivLoadjgzt;
    private EditText tvSyzt;
    private ImageView ivLoadSyzt;
    private EditText tvTcfs;
    private ImageView ivLoadTcfs;
    private EditText tvRemarks;

    private EditText tv_ch_x;
    private EditText tv_ch_y;

    private TextView tvSave;
    private TextView tvExit;

    private EditText tvqsdw;
    private EditText tvJsrq;
    private TextView tvSjly;
    private TextView tv_lctype;
    private ImageView ivLoadSjly;
    private TextView tvSfly;
    private ImageView ivLoadSfly;
    private TextView tv_hqsj;
    private ImageView iv_load_hqsj;


    private long projectId;
    private long typeId;
    private long lineId = -1;

    private Tab_Marker tab;

    private List<PhotoInfo> photos = new ArrayList<>();

    private ViewStub vs_ps;
    private TextView tv_mm,tv_wx;
    private String[] lists = new String[]{"相机", "工程相册"};

    private final int PHOTOSELECTCODE = 56;
    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_now_marker;
    }


    @Override
    protected void initContentView() {
        setLeftTitle("绘制管点", R.color.white);
        setToolBarBackgroundColor(R.color.blue);

        tv_hqsj = findViewById(R.id.tv_hqsj);
        iv_load_hqsj = findViewById(R.id.iv_load_hqsj);
        tv_mm = findViewById(R.id.tv_mm);
        tv_wx = findViewById(R.id.tv_wx);
        tvGxlx = findViewById(R.id.tv_gxlx);
        tvWtdh = findViewById(R.id.tv_wtdh);
        tvTz = findViewById(R.id.tv_tz);
        ivLoadTzd = findViewById(R.id.iv_load_tzd);
        tvFsw = findViewById(R.id.tv_fsw);
        ivLoadFsw = findViewById(R.id.iv_load_fsw);
        tvX = findViewById(R.id.tv_x);
        tvY = findViewById(R.id.tv_y);
        tvDmgc = findViewById(R.id.tv_dmgc);
        tvPxjw = findViewById(R.id.tv_pxjw);
        tv_gdfc = findViewById(R.id.tv_gdfc);
        tv_pj = findViewById(R.id.tv_pj);
        tvJlx = findViewById(R.id.tv_jlx);
        tvJzj = findViewById(R.id.tv_jzj);
        tvJbs = findViewById(R.id.tv_jbs);
        tvJds = findViewById(R.id.tv_jds);
        tvJglx = findViewById(R.id.tv_jglx);
        vs_ps = findViewById(R.id.vs_ps);
        ivLoadJglx = findViewById(R.id.iv_load_jglx);
        tvJggg = findViewById(R.id.tv_jggg);
        tv_jggg_width = findViewById(R.id.tv_jggg_width);
        tvJgcz = findViewById(R.id.tv_jgcz);
        ivLoadJgcz = findViewById(R.id.iv_load_jgcz);
        tv_jgzt = findViewById(R.id.tv_jgzt);
        ivLoadjgzt = findViewById(R.id.iv_load_jgzt);
        tvSzwz = findViewById(R.id.tv_szwz);
        ivLoadSzwz = findViewById(R.id.iv_load_szwz);
        tvSyzt = findViewById(R.id.tv_syzt);
        ivLoadSyzt = findViewById(R.id.iv_load_syzt);
        tvTcfs = findViewById(R.id.tv_tcfs);
        ivLoadTcfs = findViewById(R.id.iv_load_tcfs);
        tvRemarks = findViewById(R.id.tv_remarks);

        tvSave = findViewById(R.id.tv_save);
        tvExit = findViewById(R.id.tv_exit);

        tv_ch_x = findViewById(R.id.tv_ch_x);
        tv_ch_y = findViewById(R.id.tv_ch_y);

        tvqsdw = findViewById(R.id.tv_qsdw);
        tvJsrq = findViewById(R.id.tv_jsrq);
        tvSjly = findViewById(R.id.tv_sjly);
        tv_lctype = findViewById(R.id.tv_lctype);
        ivLoadSjly = findViewById(R.id.iv_load_sjly);
        tvSfly = findViewById(R.id.tv_sfly);
        ivLoadSfly = findViewById(R.id.iv_load_sfly);


//        tvWtdh.setTransformationMethod(new AutoCaseTransformationMethod());
    }

    @Override
    public void initData(Bundle savedInstanceState) {}
    public void initData() {
        long markerId = getIntent().getLongExtra(KEY_ID, -1);
        if (markerId > 0) {
            showLoading();
            mPresenter.queryMarker(markerId);
        } else {
            projectId = getIntent().getLongExtra(PROJECT_ID, -1);

            double latitude = getIntent().getDoubleExtra(KEY_LATITUDE, 0);
            double longitude = getIntent().getDoubleExtra(KEY_LONGITUDE, 0);
            tvX.setText(String.valueOf(latitude));
            tvY.setText(String.valueOf(longitude));
            lineId = getIntent().getLongExtra(LINEID, -1);
            typeId = getIntent().getLongExtra(TYPE_ID, -1);
            showLoading();
            mPresenter.queryTopType(projectId, typeId);
            mPresenter.getName(projectId, typeId);
            mPresenter.queryIsPhoto(projectId);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        tvSave.setOnClickListener(this);
        ivLoadjgzt.setOnClickListener(this);
        ivLoadJglx.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        ivLoadTzd.setOnClickListener(this);
        ivLoadFsw.setOnClickListener(this);
        ivLoadJgcz.setOnClickListener(this);
        ivLoadSyzt.setOnClickListener(this);
        ivLoadSzwz.setOnClickListener(this);
        ivLoadTcfs.setOnClickListener(this);
        ivLoadSjly.setOnClickListener(this);
        ivLoadSfly.setOnClickListener(this);
        iv_load_hqsj.setOnClickListener(this);

    }

    private void save() {
        if (tab == null) {
            tab = new Tab_Marker();
            tab.setCreateTime(new Date());
        }

        tab.setProjectId(projectId);
        tab.setTypeId(typeId);



        tab.setSzhd(getValue(tv_szhd));
        tab.setPshmc(getValue(tv_pshmc));
        tab.setWjbh(getValue(tv_wjbh));
        tab.setQksm(getValue(tv_yhqksm));
        tab.setJgzt(getValue(tv_jgzt));

        tab.setQsdw(getValue(tvqsdw));
        tab.setJsrq(getValue(tvJsrq));
        tab.setSjly(getValue(tvSjly));
        tab.setSfly(getValue(tvSfly));

        tab.setGxlx(getValue(tvGxlx));
        tab.setHqsj(getValue(tv_hqsj));
        if (!TextUtils.isEmpty(getValue(tvWtdh))){
            tab.setWtdh(getValue(tv_lctype)+getValue(tvWtdh));
        }else {
            ToastUtility.showToast("请填写管点编号");
            return;
        }
        tab.setTzd(getValue(tvTz));
        tab.setFsw(getValue(tvFsw));
        tab.setLatitude(getLong(tvX));
        tab.setLongitude(getLong(tvY));
        tab.setDmgc(getValue(tvDmgc));
        tab.setPxjw(getValue(tvPxjw));
        tab.setFc(getValue(tv_gdfc));
        tab.setPj(getValue(tv_pj));
        tab.setJlx(getValue(tvJlx));
        tab.setJzj(getValue(tvJzj));
        tab.setJbs(getValue(tvJbs));
        tab.setJds(getValue(tvJds));
        tab.setJglx(getValue(tvJglx));
        // TODO: 2019/9/3 方形井盖填写长宽   圆形只需要半径
        String width = getValue(tv_jggg_width);
        String height = getValue(tvJggg);
        if (!TextUtils.isEmpty(width) || !TextUtils.isEmpty(height)){
            String jglx = getValue(tvJglx);
            if (TextUtils.isEmpty(jglx)){
                ToastUtility.showToast("请选择井盖类型");
                return;
            }
            if (jglx.equals("方形")){
                if (!TextUtils.isEmpty(width) && !TextUtils.isEmpty(height)){
                    tab.setJggg(height+"X"+width);
                }else {
                    ToastUtility.showToast("请填写完整井盖规格的长宽");
                    return;
                }
            }else {
                tab.setJggg(height);
            }  /*if (jglx.equals("圆形")){
                tab.setJggg(height);
            }else   if (jglx.equals("其它")){
                tab.setJggg(height);
            }else {
                tab.setJggg(height);
            }*/
        }
        tab.setJgcz(getValue(tvJgcz));
        tab.setSzwz(getValue(tvSzwz));
        tab.setSyzt(getValue(tvSyzt));
        tab.setTcfs(getValue(tvTcfs));

        tab.setChlatitude(getLong(tv_ch_x));
        tab.setChlongitude(getLong(tv_ch_y));

        tab.setRemarks(getValue(tvRemarks));

        tab.setUpdateTime(new Date());
        if (lineId == -1) {
            mPresenter.save(tab, photos);
        } else {
            mPresenter.insertMarker(tab, lineId);
        }
    }

    private double getLong(EditText et) {
        String value = getValue(et);
        if (TextUtils.isEmpty(value)) {
            return 0;
        } else {
            return Double.valueOf(value);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_load_jgzt: {
                mPresenter.queryJGZTs();
                break;
            }
            case R.id.iv_load_tzd: {
                mPresenter.query_tzd();
                break;
            }
            case R.id.iv_load_fsw: {
                mPresenter.query_fsw();
                break;
            }
            case R.id.iv_load_jgcz: {
                mPresenter.querymanhole();
                break;
            } case R.id.iv_load_tcfs: {
                mPresenter.querytcfs();
                break;
            }case R.id.iv_load_szwz: {
                mPresenter.querySZWZ();
                break;
            }
            case R.id.iv_load_jglx: {
               mPresenter.queryJGLXs();
                break;
            }
            case R.id.iv_load_syzt: {
                mPresenter.queryUseStatus();
                break;
            }
            case R.id.tv_save: {
                save();
                break;
            }
            case R.id.iv_load_sjly: {
                mPresenter.querysjly();
                break;
            }
            case R.id.iv_load_sfly: {
                mPresenter.querysfly();
                break;
            }
            case R.id.tv_exit: {
                finish();
                break;
            }
            case R.id.iv_load_hqsj: {//获取时机
                mPresenter.queryHQSJ(typeId);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void query_tzd(String[] items) {
        selectValue(tvTz, items);
    }

    @Override
    public void query_fsw(String[] items) {
        selectValue(tvFsw, items);
    }


    @Override
    public void fail(String msg) {
        ToastUtility.showToast(msg);
    }

    @Override
    public void save_success() {
//        setResult();
        setResult(200);
        finish();
    }

    @Override
    public void save_Fail(String msg) {
        ToastUtility.showToast(msg);
    }

    @Override
    public void queryMarker_success(Tab_Marker marker) {
        vMarker = tab = marker;

        projectId = marker.getProjectId();
        typeId = marker.getTypeId();
        tvGxlx.setText(marker.getGxlx());
        tv_hqsj.setText(marker.getHqsj());
        tv_jgzt.setText(marker.getJgzt());
        String w = marker.getWtdh();
        tv_lctype.setText(w.substring(0,2));
        tvWtdh.setText(w.substring(2,w.length()));
        tvTz.setText(marker.getTzd());
        tvFsw.setText(marker.getFsw());

        tvX.setText(String.valueOf(marker.getLatitude()));
        tvY.setText(String.valueOf(marker.getLongitude()));
        tvDmgc.setText(marker.getDmgc());
        tvPxjw.setText(marker.getPxjw());
        tv_gdfc.setText(marker.getFc());
        tv_pj.setText(marker.getPj());
        tv_ch_x.setText(String.valueOf(tab.getChlatitude()));
        tv_ch_y.setText(String.valueOf(tab.getChlongitude()));
        tvJlx.setText(marker.getJlx());
        tvJzj.setText(marker.getJzj());
        tvJbs.setText(marker.getJbs());
        tvJds.setText(marker.getJds());
        tvJglx.setText(marker.getJglx());
        if (!TextUtils.isEmpty(marker.getJglx())){
            if (marker.getJglx().equals("方形")){
                vis();
            }else {
                gone();
            }
        }
        if (!TextUtils.isEmpty(marker.getJglx())&&!TextUtils.isEmpty(marker.getJggg())){
            if (marker.getJglx().equals("方形")){
                String[] values = marker.getJggg().split("X");
                tvJggg.setText(values[0]);
                tv_jggg_width.setText(values[1]);
            }else {
                tvJggg.setText(marker.getJggg());
            }
        }
        tvJgcz.setText(marker.getJgcz());
        tvSzwz.setText(marker.getSzwz());
        tvSyzt.setText(marker.getSyzt());
        tvTcfs.setText(marker.getTcfs());

        tvqsdw.setText(marker.getQsdw());
        tvJsrq.setText(marker.getJsrq());
        tvSjly.setText(marker.getSjly());
        tvSfly.setText(marker.getSfly());

        tvRemarks.setText(marker.getRemarks());
        mPresenter.queryIsPhoto(projectId);

        mPresenter.queryVisible(typeId);
    }

    @Override
    public void queryMarker_fail(String msg) {
        dimiss();
        showDialog(msg, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void queryTopType(Tab_Marker marker) {
        vMarker = marker;
        mPresenter.queryVisible(typeId);
        if (marker == null) return;
        tvTz.setText(marker.getTzd());
        tv_jgzt.setText(marker.getJgzt());
        tv_hqsj.setText(marker.getHqsj());
        tvFsw.setText(marker.getFsw());
        tvDmgc.setText(marker.getDmgc());
        tvPxjw.setText(marker.getPxjw());
        tv_gdfc.setText(marker.getFc());
        tv_pj.setText(marker.getPj());
        tvJlx.setText(marker.getJlx());
        tvJzj.setText(marker.getJzj());
        tvJbs.setText(marker.getJbs());
        tvJds.setText(marker.getJds());
        tvJglx.setText(marker.getJglx());
        if (!TextUtils.isEmpty(marker.getJglx())){
            if (marker.getJglx().equals("方形")){
                vis();

            }else {
                gone();
            }
        }
        if (!TextUtils.isEmpty(marker.getJglx())&&!TextUtils.isEmpty(marker.getJggg())){
            if (marker.getJglx().equals("方形")){
                String[] values = marker.getJggg().split("X");
                tvJggg.setText(values[0]);
                tv_jggg_width.setText(values[1]);
            }else {
                tvJggg.setText(marker.getJggg());
            }
        }
//        tvJggg.setText(marker.getJggg());
        tvJgcz.setText(marker.getJgcz());
        tvSzwz.setText(marker.getSzwz());
        tvSyzt.setText(marker.getSyzt());
        tvTcfs.setText(marker.getTcfs());
        tvRemarks.setText(marker.getRemarks());

        tvqsdw.setText(marker.getQsdw());
        tvJsrq.setText(marker.getJsrq());
        tvSjly.setText(marker.getSjly());
        tvSfly.setText(marker.getSfly());
    }

    @Override
    public void getName(String name) {
//        tvWtdh.setText(name);
        tv_lctype.setText(name.substring(0,2));
        tvWtdh.setText(name.substring(2,name.length()));
    }



    @Override
    public void createPhoto() {
        setRightBtn("拍照", R.color.white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
                    int perm = checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
                    boolean permision = (perm == PackageManager.PERMISSION_GRANTED);
                    if (!permision || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //请求权限
                        ActivityCompat.requestPermissions(MarkerNowActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 63);
                 return;
                    }
                }
                    showListDialog(lists, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case 0: {
                                    mPresenter.queryProject(projectId);
                                    break;
                                }
                                case 1: {
                                    Bundle bundle = PhotoListActivity.newInstance(projectId, true);
                                    Intent intent = new Intent(getContext(), PhotoListActivity.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, PHOTOSELECTCODE);
                                    break;
                                }
                            }
                        }
                    });

            }
        });
    }

    @Override
    public void queryProjectName(String project) {
        if (TextUtils.isEmpty(project)){
            ToastUtility.showToast("未查询到项目");
            return;
        }
        PhotoUtil.openZKCamera(MarkerNowActivity.this,project);
    }


    @Override
    public void querymanhole(String[] items) {
        selectValue(tvJgcz, items);
    }

    @Override
    public void querytcfss(String[] items) {
        selectValue(tvTcfs, items);
    }

    @Override
    public void queryUseStatus(String[] items) {
        selectValue(tvSyzt, items);
    }

    @Override
    public void queryJGZTs(String[] items) {
        selectValue(tv_jgzt,items);
    }

    @Override
    public void queryJGLXs(String[] items) {
        selectValue(tvJglx,items);
    }

    @Override
    public void queryszwz(String[] items) {
        selectValue(tvSzwz,items);
    }

    @Override
    public void querysjly(String[] items) {
        selectValue(tvSjly,items);
    }

    @Override
    public void querysfly(String[] items) {
        selectValue(tvSfly,items);
    }

    private EditText tv_szhd;
    private EditText tv_pshmc;
    private EditText tv_wjbh;
    private EditText tv_yhqksm;

    private Tab_Marker vMarker;
    @Override
    public void visiblePS() {

        vs_ps.inflate();
        tv_szhd= findViewById(R.id.tv_szhd);
        tv_pshmc= findViewById(R.id.tv_gshmc);
        tv_wjbh= findViewById(R.id.tv_wjbh);
        tv_yhqksm= findViewById(R.id.tv_yhqksm);
        if (vMarker!=null){
            tv_szhd.setText(vMarker.getSzhd());
            tv_pshmc.setText(vMarker.getPshmc());
            tv_wjbh.setText(vMarker.getWjbh());
            tv_yhqksm.setText(vMarker.getQksm());
        }
    }

    @Override
    public void queryVisibleSuccess() {
        dimiss();
    }

    @Override
    public void queryhqsj(String[] hqsj) {
        selectValue(tv_hqsj,hqsj);
    }

    String[] newitems = null;
    private void selectValue(final TextView tv,  String[] items) {
        if (!TextUtils.isEmpty(tv.getText())&&items!=null){
            int maxLength = items.length+1;
          newitems = new String[maxLength];
            for (int i = 0; i < maxLength; i++) {
                if (i==0){
                    newitems[0] = "清除选项";
                }else {
                    newitems[i] = items[i-1];
                }
            }
            //增加清除选项
        }else {
            newitems = items;
        }
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.v_tablist, new ArrayList<>(Arrays.asList(newitems)));
        final QMUIListPopup mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_BOTTOM, adapter);
        mListPopup.create(tv.getWidth(), QMUIDisplayHelper.dp2px(getContext(), 200),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv.setText(newitems[position]);
                        mListPopup.dismiss();
                        if (newitems[position].equals("清除选项")){
                            tv.setText(null);
                        }else  if (tv.getId()==R.id.tv_tz){
                            tvFsw.setText(null);
                        }else if (tv.getId()==R.id.tv_fsw){
                            tvTz.setText(null);
                        }else if (tv.getId()==R.id.tv_jglx){
                            if (newitems[position].equals("方形")){
                                vis();
                            }else {
                                gone();
                            }
                        }

                    }
                });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        mListPopup.show(tv);
    }

    private void vis(){
        tv_jggg_width.setVisibility(View.VISIBLE);
        tv_wx.setVisibility(View.VISIBLE);
        tv_mm.setVisibility(View.VISIBLE);
    }
    private void gone(){
        tv_jggg_width.setVisibility(View.GONE);
        tv_wx.setVisibility(View.GONE);
        tv_mm.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_IMAGE_REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK){
                    PhotoInfo info = new PhotoInfo();
                    info.setName("");
                    info.setPath(PhotoUtil.fileImagePath.getPath());
                    photos.add(info);
                }
                    break;
            }
            case PHOTOSELECTCODE: {
                if (data==null){
                    return;
                }
                Serializable sera =  data.getSerializableExtra("data");
                if (sera != null) {
                    List<Tab_marker_photo> ps = (List<Tab_marker_photo>) sera;
                    for (Tab_marker_photo p : ps) {
                        PhotoInfo info = new PhotoInfo();
                        info.setName("");
                        info.setPath(p.getPath());
                        photos.add(info);
                    }
                }
            }
        }
    }



}
