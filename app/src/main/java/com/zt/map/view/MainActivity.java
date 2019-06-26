package com.zt.map.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zt.map.R;
import com.zt.map.contract.MainContract;
import com.zt.map.entity.db.TaggingEntiiy;
import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.entity.db.tab.Tab_Project;
import com.zt.map.presenter.MainPresenter;
import com.zt.map.util.Base64Util;
import com.zt.map.util.LocalUtil;
import com.zt.map.util.LocalUtils;
import com.zt.map.util.MapUtil;
import com.zt.map.view.widget.AutoCaseTransformationMethod;
import com.zt.map.view.widget.CreateDialog;
import com.zt.map.view.widget.ProjectDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.faker.repaymodel.mvp.BaseMVPAcivity;
import cn.faker.repaymodel.util.ToastUtility;


public class MainActivity extends BaseMVPAcivity<MainContract.View, MainPresenter> implements
        MainContract.View,
        View.OnClickListener,
        BaiduMap.OnMapClickListener,
        BaiduMap.OnMarkerClickListener,
        BaiduMap.OnPolylineClickListener,
        BaiduMap.OnMarkerDragListener,
        BaiduMap.OnMapTouchListener {

    private final int TO_MARKER_CREATE_CODE = 112;

    private MapView bmapView;

    private BaiduMap baiduMap;
    private View[] opens;

    private TextView tv_type;
    private ImageButton ibDrawPan;
    private ImageButton ibDrawPoint;
    private ImageButton ibDrawLine;
    private ImageButton ibDrawInsert;
    private ImageButton ibDrawInfo;
    private ImageButton ibDrawDelete;

    private TextView ib_open;
    private ImageView ib_local;
    private TextView ib_bz;

    private CoordinatorLayout root_l;

    private CanvalView v_canval;

    private String[] tabNames;
    private Long[] typeIds;
    private Integer[] colors;

    private long projectId = -1;//项目id

    private final String KEY_MARKER_ID = "KEY_MARKER_ID";
    private final String KEY_LINE_ID = "KEY_LINE_ID";

    private int typeColor;
    boolean isOneLoad = true;//是否是第一次加载

    private float zoomLevel = 20;

    @Override
    protected int getLayoutContentId() {
        return R.layout.ac_main;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initContentView() {
        isShowToolView(false);

        bmapView = findViewById(R.id.bmapView);

        v_canval = findViewById(R.id.v_canval);
        tv_type = findViewById(R.id.tv_type);
        ibDrawPan = findViewById(R.id.ib_draw_pan);
        ibDrawPoint = findViewById(R.id.ib_draw_point);
        ibDrawLine = findViewById(R.id.ib_draw_line);
        ibDrawInsert = findViewById(R.id.ib_draw_insert);
        ibDrawInfo = findViewById(R.id.ib_draw_info);
        ibDrawDelete = findViewById(R.id.ib_draw_delete);
        opens = new View[]{ibDrawPan, ibDrawPoint, ibDrawLine,
                ibDrawInsert, ibDrawInfo, ibDrawDelete};
        ibDrawPan.setSelected(true);
        ib_open = findViewById(R.id.ib_open);
        ib_local = findViewById(R.id.ib_local);
        ib_bz = findViewById(R.id.ib_bz);
        root_l = findViewById(R.id.root_l);

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initListener() {
        super.initListener();

        baiduMap = bmapView.getMap();
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomBy(4));

        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);
        baiduMap.setOnPolylineClickListener(this);
        baiduMap.setOnMarkerDragListener(this);
        baiduMap.setOnMapTouchListener(this);
        MapUtil.init(baiduMap);

        tv_type.setOnClickListener(this);
        ib_open.setOnClickListener(this);
        ib_local.setOnClickListener(this);
        ibDrawPan.setOnClickListener(this);
        ibDrawPoint.setOnClickListener(this);
        ibDrawLine.setOnClickListener(this);
        ibDrawInsert.setOnClickListener(this);
        ibDrawInfo.setOnClickListener(this);
        ibDrawDelete.setOnClickListener(this);
        ib_bz.setOnClickListener(this);


        v_canval.setOnDrawListener(new CanvalView.onDrawListener() {
            @Override
            public void onDrawLineFinal(float start_x, float start_y, MotionEvent end_Event) {
                LatLng startLatLng = MapUtil.fromLocation(start_x, start_y);
                LatLng endLatLng = MapUtil.fromLocation(end_Event);

                Bundle bundle = LineNowActivity.newInstance(projectId, getTypeId());
                bundle.putDouble(LineNowActivity.KEY_STARTLATLNG_X, startLatLng.latitude);
                bundle.putDouble(LineNowActivity.KEY_STARTLATLNG_Y, startLatLng.longitude);
                bundle.putDouble(LineNowActivity.KEY_ENDLATLNG_X, endLatLng.latitude);
                bundle.putDouble(LineNowActivity.KEY_ENDLATLNG_Y, endLatLng.longitude);


                Intent intent = new Intent(getContext(), LineNowActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 520);
            }

            Overlay mText;

            @Override
            public void onMoven(float start_x, float start_y, MotionEvent moven_Event) {
                LatLng startLatLng = MapUtil.fromLocation(start_x, start_y);
                LatLng endLatLng = MapUtil.fromLocation(moven_Event.getX(), moven_Event.getY() - 70);
                double distanse = DistanceUtil.getDistance(startLatLng, endLatLng);
                double x = (startLatLng.latitude + endLatLng.latitude) / 2;
                double y = (startLatLng.longitude + endLatLng.longitude) / 2;

                String value = new DecimalFormat("0.000").format(distanse);

                if (mText != null) {
                    mText.remove();
                }
                LatLng llText = new LatLng(x, y);
                OverlayOptions mTextOptions = new TextOptions()
                        .text(value + "米")
                        .bgColor(0xAAFFFF00)
                        .fontSize(30) //字号
                        .fontColor(0xFFFF00FF)
                        .position(llText);
                mText = baiduMap.addOverlay(mTextOptions);
            }
        });
        initMapStatus();
    }


    private void initMapStatus() {
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                zoomLevel = mapStatus.zoom;

            }
        });

    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (projectId <= 0) {
            ToastUtility.showToast("请选择工程");
            return;
        }
        if (isDownMarker()) {
            double latitude = latLng.latitude;
            double longitude = latLng.longitude;
            MarkerNowActivity.newInstance(this, projectId, getTypeId(), latitude, longitude, TO_MARKER_CREATE_CODE);
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        long makerId = marker.getExtraInfo().getLong(KEY_MARKER_ID);
        if (isMarkerInfo()) {
            MarkerNowActivity.newInstance(this, makerId, 230);
        } else if (isDelete()) {
            showLoading();
            mPresenter.delete(makerId, 1);
        }
        return true;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Bundle bundle = marker.getExtraInfo();
        long makerId = bundle.getLong(KEY_MARKER_ID);
        LatLng latLng = marker.getPosition();
        showLoading();
        mPresenter.update_MakerLocal(makerId, latLng.latitude, latLng.longitude);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        long lineId = polyline.getExtraInfo().getLong(KEY_LINE_ID);
        if (isMarkerInfo()) {
            LineNowActivity.newInstance(this, lineId, 240);
        } else if (isDelete()) {
            showLoading();
            mPresenter.delete(lineId, 2);
        } else if (isInsert()) {
            List<LatLng> lats = polyline.getPoints();
            LatLng s = lats.get(0);
            LatLng e = lats.get(1);
            double m_latitude = (s.latitude + e.latitude) / 2;
            double m_longitude = (s.longitude + e.longitude) / 2;
            MarkerNowActivity.newInstance(this, projectId, getTypeId(), m_latitude, m_longitude, lineId, TO_MARKER_CREATE_CODE);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        bmapView.onDestroy();
        if (mLocationClient!=null){
            mLocationClient.stop();
            baiduMap.setMyLocationEnabled(false);
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_draw_pan: {
                selectindex(0);
                break;
            }
            case R.id.ib_draw_point: {
                selectindex(1);
                break;
            }
            case R.id.ib_draw_line: {
                selectindex(2);
                break;
            }
            case R.id.ib_draw_insert: {
                selectindex(3);
                break;
            }
            case R.id.ib_draw_info: {
                selectindex(4);
                break;
            }
            case R.id.ib_draw_delete: {
                selectindex(5);
                break;
            }
            case R.id.tv_type: {
                showTypeList();
                break;
            }
            case R.id.ib_open: {
                showBottomDialog();
                break;
            }
            case R.id.ib_local: {
                local();
                break;
            }

            case R.id.ib_bz: {
                showTaggings();
                break;
            }
        }
    }

    private final String[] taggings = new String[]{"不使用标注", "埋深", "管线材质", "管径断面"};

    /**
     * 显示标注
     */
    private void showTaggings() {
        if (projectId < 0) {
            ToastUtility.showToast("请选择工程");
            return;
        }
        showListDialog(taggings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showLoading();
                mPresenter.queryTagger(projectId, which);
            }
        });

    }

    LocationClient mLocationClient;

    /**
     * 定位
     */
    private void local() {

        if (permission()) {
            if (!LocalUtils.isLocationEnabled(getContext())) {
                showDialog("因为该功能需要使用定位，请打开手机定位开关");
                return;
            }
            baiduMap.setMyLocationEnabled(true);
            LocalUtil.start(getContext(), new LocalUtil.LocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {
                    if (location == null || baiduMap == null) {
                        return;
                    }
                    LocalUtil.stop();
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(location.getDirection()).latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    baiduMap.setMyLocationData(locData);
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(20f);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            });

        }
    }

    private boolean permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            int perm = checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            boolean permision = (perm == PackageManager.PERMISSION_GRANTED);
            if (!permision || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 63);
                return false;
            }
        }
        return true;
    }

    private boolean permissionFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            int perm = checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            boolean permision = (perm == PackageManager.PERMISSION_GRANTED);
            int rperm = checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
            boolean rpermision = (rperm == PackageManager.PERMISSION_GRANTED);
            if (!rpermision||!permision || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 63);
                return false;
            }
        }
        return true;
    }

    private void selectindex(int index) {
        boolean isSelected = opens[index].isSelected();
        for (int i = 0; i < opens.length; i++) {
            boolean value = (i == index);
            if (value) {
                if (isSelected) {
                    value = !value;
                }
            }
            opens[i].setTag(value);
            opens[i].setSelected(value);
        }
        canval();
    }


    private void canval() {
        if (ibDrawLine.getTag() == null) {
            v_canval.setVisibility(View.GONE);
        } else {
            boolean status = (boolean) ibDrawLine.getTag();
            if (projectId <= 0 && status) {
                ToastUtility.showToast("请选择工程");
                return;
            } else {
                v_canval.setVisibility(status ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * 判断是否是 画点
     *
     * @return
     */
    private boolean isDownMarker() {
        if (ibDrawPoint.getTag() == null) {
            return false;
        } else {
            return (boolean) ibDrawPoint.getTag();
        }
    }

    private boolean isMarkerInfo() {
        if (ibDrawInfo.getTag() == null) {
            return false;
        } else {
            return (boolean) ibDrawInfo.getTag();
        }
    }

    private boolean isInsert() {
        if (ibDrawInsert.getTag() == null) {
            return false;
        } else {
            return (boolean) ibDrawInsert.getTag();
        }
    }

    private boolean isDelete() {
        if (ibDrawDelete.getTag() == null) {
            return false;
        } else {
            return (boolean) ibDrawDelete.getTag();
        }
    }

    private Long getTypeId() {
        Object o = tv_type.getTag();
        if (o == null) {
            return null;
        } else {
            return (Long) o;
        }
    }


    @Override
    public void queryType(final String[] tabNames, final Long[] typeIds, final Integer[] colors) {
        this.tabNames = tabNames;
        this.typeIds = typeIds;
        this.colors = colors;

        tv_type.setText(tabNames[0]);
        tv_type.setTag(typeIds[0]);
        typeColor = colors[0];
    }

    @Override
    public void queryProjects(List<Tab_Project> tab_projects) {
        final ProjectDialog pdialog = new ProjectDialog().setTab_projects(tab_projects);
        pdialog.setOnItemListener(new ProjectDialog.OnItemListener() {
            @Override
            public void onDelete(final long id) {
                new QMUIDialog.MessageDialogBuilder(getContext()).setMessage("是否删除该项目?").addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        pdialog.dismiss();
                        showLoading();
                        mPresenter.delete_Project(id);
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).show();

            }

            @Override
            public void onClick(long id) {
                pdialog.dismiss();
                mPresenter.queryProject(id);
            }
        }).show(getSupportFragmentManager(),"gg");
/*        new QMUIDialog.CheckableDialogBuilder(getContext())
                .addItems(projects, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showLoading();
                    }
                }).setTitle("工程列表")
                .show();*/
    }

    @Override
    public void queryProjects_fail(String msg) {
        showDialog(msg);
    }

    @Override
    public void createProject_success(String msg, long projectId) {
        this.projectId = projectId;
        isOneLoad = true;
        baiduMap.clear();
        v_canval.clean();
        v_canval.setVisibility(View.GONE);
        ToastUtility.showToast(msg);
    }

    @Override
    public void createProject_fail(String msg) {
        showDialog(msg);
    }

    @Override
    public void queryProject(List<Tab_Marker> markers, List<Tab_Line> lines, long projectId) {
        baiduMap.clear();
        v_canval.clean();

        this.projectId = projectId;
        if (lines != null) {
            for (Tab_Line item : lines) {
                drawLine(item.getStart_latitude(), item.getStart_longitude(), item.getEnd_latitude(), item.getEnd_longitude(), item.getColor(), item.getId());
            }
        }
        if (markers != null && markers.size() > 0) {

            for (Tab_Marker item : markers) {
                int color = -1;
                Sys_Color c1 = item.getSys_color();
                if (c1 != null) {
                    color = Color.rgb(Integer.valueOf(c1.getR()), Integer.valueOf(c1.getG()),
                            Integer.valueOf(c1.getB()));
                }

                String base64 = item.getIconBase();
                if (!TextUtils.isEmpty(base64)) {
                    Bitmap icon = Base64Util.stringtoBitmap(base64);
                    drawMarker(item.getLatitude(), item.getLongitude(), item.getId(), color, icon, item.getWtdh());
                } else {
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.makericon);
                    drawMarker(item.getLatitude(), item.getLongitude(), item.getId(), color, bitmap.getBitmap(), item.getWtdh());
                }
            }
            if (markers.size() > 0 && isOneLoad) {
                isOneLoad = false;
                Tab_Marker mk = markers.get(markers.size() - 1);
                LatLng ll = new LatLng(mk.getLatitude(),
                        mk.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(zoomLevel);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        dimiss();
    }

    @Override
    public void delete_success(String msg, int type) {
        mPresenter.queryProject(projectId);
    }

    @Override
    public void delete_fail(String msg) {
        dimiss();
        ToastUtility.showToast(msg);
    }

    @Override
    public void update() {
        showLoading();
        mPresenter.queryProject(projectId);
    }

    @Override
    public void queryMarker(Tab_Marker data) {
        dimiss();
        if (data == null) {
            ToastUtility.showToast("未查询到该点号");
            return;
        }

        //移动到该点
        LatLng ll = new LatLng(data.getLatitude(),
                data.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoomLevel);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        ImageView button = new ImageView(getApplicationContext());
        button.setBackgroundResource(R.mipmap.message);
        InfoWindow mInfoWindow = new InfoWindow(button, ll, -30);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.hideInfoWindow();
            }
        });
        baiduMap.showInfoWindow(mInfoWindow);
    }

    private List<Overlay> taggerlist = new ArrayList<>();

    @Override
    public void taggers(List<TaggingEntiiy> taggings) {
        if (taggerlist != null && taggerlist.size() > 0) {
            for (Overlay o : taggerlist) {
                o.remove();
            }
            taggerlist.clear();
        }
        if (taggings == null || taggings.size() <= 0) {
            dimiss();
            return;
        }

        for (TaggingEntiiy item : taggings) {
            if (TextUtils.isEmpty(item.getValue())) {
                continue;
            }
            LatLng llText = new LatLng(item.getLatitude(), item.getLongitude());
            OverlayOptions mTextOptions = new TextOptions()
                    .text(item.getValue())
                    .fontSize(60) //字号
                    .fontColor(item.getColor())
                    .position(llText);
            Overlay mText = baiduMap.addOverlay(mTextOptions);
            taggerlist.add(mText);
        }
        dimiss();
    }

    @Override
    public void outExcel(String msg) {
        dimiss();
        if (msg.contains("失败")) {//懒得加失败回调 p层也未判断失败
            ToastUtility.showToast(msg);
        } else {
            Snackbar.make(root_l, msg, Snackbar.LENGTH_LONG).setAction("查看文件", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toAcitvity(OutFileListActivity.class);
                }
            }).show();
        }
    }

    @Override
    public void delete_Project() {
        dimiss();
        baiduMap.clear();
        projectId = -1;
        isOneLoad = true;
        mPresenter.queryProjects();
    }

    private void drawMarker(double latitude, double longitude, long marerId, Bitmap icon, String name) {
        drawMarker(latitude, longitude, marerId, typeColor, icon, name);
    }

    private void drawMarker(double latitude, double longitude, long marerId, int color, Bitmap icon, String name) {
        // TODO: 2019/5/31 显示名称
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_MARKER_ID, marerId);
        LatLng latLng = new LatLng(latitude, longitude);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.makericon);
        View ll = getMyMarker(icon, color, name);
        int height = ll.findViewById(R.id.iv_icon).getHeight();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(ll);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng).draggable(true).flat(true)
                .icon(bitmap).extraInfo(bundle);
        baiduMap.addOverlay(option);
    }

    private View getMyMarker(Bitmap image, int color, String name) {
        View ll = LayoutInflater.from(getContext()).inflate(R.layout.v_marker, null, false);
        TextView tv = ll.findViewById(R.id.tv_name);
        ImageView iv = ll.findViewById(R.id.iv_icon);
        tv.setText(name);
        if (color == -1) {
            color = 0xAAFF0000;
        }
        tv.setTextColor(color);
        iv.setImageBitmap(image);
        return ll;
    }

    private void drawLine(double start_latitude, double start_longitude, double end_latitude, double end_longitude, long lineId) {
        drawLine(start_latitude, start_longitude, end_latitude, end_longitude, typeColor, lineId);
    }

    private void drawLine(double start_latitude, double start_longitude, double end_latitude, double end_longitude, int color, long lineId) {
        List<LatLng> points = new ArrayList<>();
        LatLng p1 = new LatLng(start_latitude, start_longitude);
        LatLng p2 = new LatLng(end_latitude, end_longitude);
        points.add(p1);
        points.add(p2);

        Bundle opbundle = new Bundle();
        opbundle.putLong(KEY_LINE_ID, lineId);
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(5)
                .color(color)
                .points(points).extraInfo(opbundle);
        baiduMap.addOverlay(mOverlayOptions);
    }

    private void showTypeList() {
        showListDialog(tabNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String typeCode = tabNames[which];
                Long typeId = typeIds[which];
                typeColor = colors[which];
                tv_type.setText(typeCode);
                tv_type.setTag(typeId);
            }
        });
    }

    /**
     * 底部菜单
     */
    final int TAG_SELECT_CREATE = 0;
    final int TAG_SELECT_OPEN = 1;
    final int TAG_SELECT_CLONE = 2;
    final int TAG_SELECT_PHOTO = 3;
    final int TAG_SELECT_MARKER = 4;
    final int TAG_SELECT_OUT = 5;
    final int TAG_SELECT_INT = 6;
    final int TAG_SELECT_OUT_QUERY = 7;
    final int TAG_SELECT_SETTING = 8;

    private void showBottomDialog() {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getContext());


        builder.addItem(R.mipmap.create, "新建工程", TAG_SELECT_CREATE, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
        builder.addItem(R.mipmap.open, "打开工程", TAG_SELECT_OPEN, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
        builder.addItem(R.mipmap.clone, "关闭工程", TAG_SELECT_CLONE, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
        builder.addItem(R.mipmap.out, "导出工程", TAG_SELECT_OUT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);
        builder.addItem(R.mipmap.markerquery, "查找点号", TAG_SELECT_MARKER, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE);

        builder.addItem(R.mipmap.pps, "查看导出", TAG_SELECT_OUT_QUERY, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
        builder.addItem(R.mipmap.photo, "工程相册", TAG_SELECT_PHOTO, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
        builder.addItem(R.mipmap.input, "导入工程", TAG_SELECT_INT, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);
        builder.addItem(R.mipmap.setting, "系统设置", TAG_SELECT_SETTING, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE);

        builder.setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView) {
                int key = (int) itemView.getTag();
                switch (key) {
                    case TAG_SELECT_CREATE: {
                        showNewProjectDialog();
                        break;
                    }
                    case TAG_SELECT_OPEN: {
                        isOneLoad = true;
                        mPresenter.queryProjects();
                        break;
                    }
                    case TAG_SELECT_CLONE: {
                        baiduMap.clear();
                        projectId = -1;
                        isOneLoad = true;
                        break;
                    }
                    case TAG_SELECT_PHOTO: {
                        if (projectId >= 0) {
                            Bundle bundle = PhotoListActivity.newInstance(projectId);
                            Intent intent = new Intent(getContext(), PhotoListActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastUtility.showToast("请选择工程");
                        }

                        break;
                    }
                    case TAG_SELECT_MARKER: {
                        if (projectId >= 0) {
                            showQueryMarker();
                        } else {
                            ToastUtility.showToast("请选择工程");
                        }

                        break;
                    }
                    case TAG_SELECT_OUT_QUERY: {
                        toAcitvity(OutFileListActivity.class);
                        break;
                    }
                    case TAG_SELECT_OUT: {
                        if (!permissionFile()) {
                            return;
                        }
                        if (projectId >= 0) {
                            showLoading();
                            mPresenter.outExcel(projectId, getContext());

                            /*showListDialog(new String[]{"导出为Excel","导出为mdb"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (which==0){
                                        showLoading();
                                        mPresenter.outExcel(projectId,getContext());
                                        return;
                                    }else  if (which==1){
                                        showLoading();
                                        mPresenter.outAccess(projectId,getContext());
                                        return;
                                    }
                                    ToastUtility.showToast("功能正在完善");
                                }
                            });*/

                        } else {
                            ToastUtility.showToast("请选择工程");
                        }

                        break;
                    }
                    case TAG_SELECT_INT: {
                        if (projectId >= 0) {
                            // TODO: 2019/6/6 导入

                        } else {
                            ToastUtility.showToast("请选择工程");
                        }

                        break;
                    }
                    case TAG_SELECT_SETTING: {
                        toAcitvity(SettingActivity.class);
                        break;
                    }
                }
                dialog.dismiss();
            }
        }).build().show();
    }

    private void showQueryMarker() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getContext());
        builder.setTitle("点号查找")
                .setPlaceholder("请在此输入点号")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("查询", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text == null || text.length() <= 0) {
                            ToastUtility.showToast("请输入点号");
                            return;
                        }
                        dialog.dismiss();
                        showLoading();
                        mPresenter.queryMarker(projectId, text.toString());
                    }
                }).show();
        builder.getEditText().setTransformationMethod(new AutoCaseTransformationMethod());
    }

    private void showNewProjectDialog() {

        CreateDialog dialog = new CreateDialog();
        dialog.setListener(new CreateDialog.OnCreateListener() {
            @Override
            public void onCreateProject(boolean isSelect, Editable name) {
                isOneLoad = true;
                mPresenter.createProject(name, isSelect);

            }
        });
        dialog.show(getSupportFragmentManager(), "CreateDialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showLoading();
        mPresenter.queryProject(projectId);
        super.onActivityResult(requestCode, resultCode, data);
    }


}
