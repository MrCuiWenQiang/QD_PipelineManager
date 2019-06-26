package com.zt.map.view;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private EditText tvTz;
    private ImageView ivLoadTzd;
    private EditText tvFsw;
    private ImageView ivLoadFsw;
    private EditText tvX;
    private EditText tvY;
    private EditText tvDmgc;
    private EditText tvPxjw;
    private EditText tvJlx;
    private EditText tvJzj;
    private EditText tvJbs;
    private EditText tvJds;
    private EditText tvJglx;
    private ImageView ivLoadJglx;
    private EditText tvJggg;
    private EditText tvJgcz;
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
    private EditText tvSjly;
    private ImageView ivLoadSjly;
    private EditText tvSfly;
    private ImageView ivLoadSfly;


    private long projectId;
    private long typeId;
    private long lineId = -1;

    private Tab_Marker tab;

    private List<PhotoInfo> photos = new ArrayList<>();

    private ViewStub vs_ps;

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
        tvJlx = findViewById(R.id.tv_jlx);
        tvJzj = findViewById(R.id.tv_jzj);
        tvJbs = findViewById(R.id.tv_jbs);
        tvJds = findViewById(R.id.tv_jds);
        tvJglx = findViewById(R.id.tv_jglx);
        vs_ps = findViewById(R.id.vs_ps);
        ivLoadJglx = findViewById(R.id.iv_load_jglx);
        tvJggg = findViewById(R.id.tv_jggg);
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
        ivLoadSjly = findViewById(R.id.iv_load_sjly);
        tvSfly = findViewById(R.id.tv_sfly);
        ivLoadSfly = findViewById(R.id.iv_load_sfly);


        tvWtdh.setTransformationMethod(new AutoCaseTransformationMethod());
    }

    @Override
    public void initData(Bundle savedInstanceState) {
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
        tab.setWtdh(getValue(tvWtdh));
        tab.setTzd(getValue(tvTz));
        tab.setFsw(getValue(tvFsw));
        tab.setLatitude(getLong(tvX));
        tab.setLongitude(getLong(tvY));
        tab.setDmgc(getValue(tvDmgc));
        tab.setPxjw(getValue(tvPxjw));
        tab.setJlx(getValue(tvJlx));
        tab.setJzj(getValue(tvJzj));
        tab.setJbs(getValue(tvJbs));
        tab.setJds(getValue(tvJds));
        tab.setJglx(getValue(tvJglx));
        tab.setJggg(getValue(tvJggg));
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
        tv_jgzt.setText(marker.getJgzt());
        tvWtdh.setText(marker.getWtdh());
        tvTz.setText(marker.getTzd());
        tvFsw.setText(marker.getFsw());
        tvX.setText(String.valueOf(marker.getLatitude()));
        tvY.setText(String.valueOf(marker.getLongitude()));
        tvDmgc.setText(marker.getDmgc());
        tvPxjw.setText(marker.getPxjw());
        tv_ch_x.setText(String.valueOf(tab.getChlatitude()));
        tv_ch_y.setText(String.valueOf(tab.getChlongitude()));
        tvJlx.setText(marker.getJlx());
        tvJzj.setText(marker.getJzj());
        tvJbs.setText(marker.getJbs());
        tvJds.setText(marker.getJds());
        tvJglx.setText(marker.getJglx());
        tvJggg.setText(marker.getJggg());
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
        tvFsw.setText(marker.getFsw());
        tvDmgc.setText(marker.getDmgc());
        tvPxjw.setText(marker.getPxjw());
        tvJlx.setText(marker.getJlx());
        tvJzj.setText(marker.getJzj());
        tvJbs.setText(marker.getJbs());
        tvJds.setText(marker.getJds());
        tvJglx.setText(marker.getJglx());
        tvJggg.setText(marker.getJggg());
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
        tvWtdh.setText(name);
    }



    @Override
    public void createPhoto() {
        setRightBtn("拍照", R.color.white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(MarkerNowActivity.this, new String[]{
                                    Manifest.permission.CAMERA},
                            0);
                } else {
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

    private void selectValue(final TextView tv, final String[] items) {
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
