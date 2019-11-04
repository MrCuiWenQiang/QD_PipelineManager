package com.zt.map.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zt.map.entity.db.system.Sys_Color;
import com.zt.map.entity.db.tab.Tab_Line;
import com.zt.map.entity.db.tab.Tab_Marker;

import org.litepal.annotation.Column;
import org.litepal.parser.LitePalAttr;
import org.litepal.parser.LitePalParser;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.faker.repaymodel.util.error.ErrorUtil;


/**
 * Function : sqlite 批量写入     因遇到大量数据时候 litepal插入速度慢    现用原生代替批量
 * Remarks  :
 * Created by Mr.C on 2018/9/26 0026.
 */
public class BatchDBUtil {
    private static BatchDBUtil batchDBUtil;

    public static BatchDBUtil init(Context context) {
        if (batchDBUtil == null) {
            synchronized (BatchDBUtil.class) {
                if (batchDBUtil == null) {
                    LitePalParser.parseLitePalConfiguration();
                    LitePalAttr mLitePalAttr = LitePalAttr.getInstance();
                    batchDBUtil = new BatchDBUtil(context, mLitePalAttr.getDbName(), null, mLitePalAttr.getVersion());
                }
            }
        }
        return batchDBUtil;
    }

    private Context mContext;
    private String dbName;

    public BatchDBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name+ ".db", factory, version);
        this.mContext = context;
        this.dbName = name;
    }


    public BatchDBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name+ ".db", factory, version, errorHandler);
        this.mContext = context;
        this.dbName = name;
    }
    public static boolean updateMarker(final long makerId, final double latitude, final double longitude ){
        int a=0,b=0,c = 0;
        SQLiteDatabase db = Connector.getDatabase();
        String sql_marker = " update tab_marker set latitude = "+latitude+",longitude="+longitude+"  where id = "+makerId;
        String sql_line_start = "update tab_line " +
                "       set " +
                "           start_latitude = " +latitude+
                "          , start_longitude = "+longitude+
                "       where startmarkerid ="+makerId;
        String sql_line_end = "update tab_line " +
                "       set " +
                "           end_latitude =" +latitude+
                "           ,end_longitude = " +longitude+
                "       where endmarkerid = "+makerId;
        try {
            db.beginTransaction();
              db.execSQL(sql_marker);
              db.execSQL(sql_line_start);
              db.execSQL(sql_line_end);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            ErrorUtil.showError(e);
        }finally {
            db.endTransaction();
            db.close();
        }
        return b>0||c>0;
    }
    public static List<Tab_Line> whereLines(String wq){
        SQLiteDatabase db = Connector.getDatabase();
        List<Tab_Line> lines= null;
        String sql = "select l.*,c.r,c.g,c.b from tab_line l\n" +
                "\tleft join sys_table t on t.id = l.typeid  \n" +
                "\tleft join sys_color c on c.fathercode = t.code\n" +
                "\twhere   "+wq;
        try {
            db.beginTransaction();
            Cursor cursor = db.rawQuery(sql,null);
            lines = new ArrayList<>();
            while (cursor.moveToNext()){
                Tab_Line line = new Tab_Line();
                line.setId(cursor.getLong(cursor.getColumnIndex("id")));
                line.setProjectId(cursor.getLong(cursor.getColumnIndex("projectid")));
                line.setTypeId(cursor.getLong(cursor.getColumnIndex("typeid")));
                line.setStartMarkerId(cursor.getLong(cursor.getColumnIndex("startmarkerid")));
                line.setEndMarkerId(cursor.getLong(cursor.getColumnIndex("endmarkerid")));

                line.setStart_latitude(cursor.getDouble(cursor.getColumnIndex("start_latitude")));
                line.setStart_longitude(cursor.getDouble(cursor.getColumnIndex("start_longitude")));
                line.setEnd_latitude(cursor.getDouble(cursor.getColumnIndex("end_latitude")));
                line.setEnd_longitude(cursor.getDouble(cursor.getColumnIndex("end_longitude")));



                String r = cursor.getString(cursor.getColumnIndex("r"));
                String g = cursor.getString(cursor.getColumnIndex("g"));
                String b = cursor.getString(cursor.getColumnIndex("b"));
                if (!TextUtils.isEmpty(r) && !TextUtils.isEmpty(g) && !TextUtils.isEmpty(b) ){
                    Sys_Color sys_color = new Sys_Color();
                    sys_color.setR(r);
                    sys_color.setG(g);
                    sys_color.setB(b);
                    line.setc(sys_color);
                }
                lines.add(line);
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            ErrorUtil.showError(e);
        }finally {
            db.endTransaction();
            db.close();
        }
        return lines;
    }


    public  static List<Tab_Marker> whereMarkers(String ws){
        SQLiteDatabase db = Connector.getDatabase();
        List<Tab_Marker> markers= null;
        String sql = "\tselect m.*,c.r,c.g,c.b,f.icon as ficon,a.icon as aicon from tab_marker m \n" +
                "\tleft join sys_color c on c.id = m.typeid\n" +
                "\tleft join sys_table t on t.id = m.typeid  \n" +
                "\tleft join sys_type_child  tc on tc.name =t.name  \n" +
                "\tleft join sys_features  f on f.name= m.tzd and f.fathercode = tc.fathercode\n" +
                "\tleft join sys_appendages  a on a.name= m.fsw and a.fathercode = tc.fathercode\n" +
                "\twhere "+ws;
        try{
            db.beginTransaction();
            Cursor cursor = db.rawQuery(sql,null);
            markers = new ArrayList<>();
            while (cursor.moveToNext()){
                Tab_Marker marker = new Tab_Marker();
                marker.setId(cursor.getLong(cursor.getColumnIndex("id")));
                marker.setProjectId(cursor.getLong(cursor.getColumnIndex("projectid")));
                marker.setTypeId(cursor.getLong(cursor.getColumnIndex("typeid")));

                marker.setChlatitude(cursor.getDouble(cursor.getColumnIndex("chlatitude")));
                marker.setChlongitude(cursor.getDouble(cursor.getColumnIndex("chlongitude")));
                marker.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                marker.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));

                marker.setGxlx(cursor.getString(cursor.getColumnIndex("gxlx")));
                marker.setWtdh(cursor.getString(cursor.getColumnIndex("wtdh")));
                marker.setCldh(cursor.getString(cursor.getColumnIndex("cldh")));
                marker.setTzd(cursor.getString(cursor.getColumnIndex("tzd")));
                marker.setFsw(cursor.getString(cursor.getColumnIndex("fsw")));

                String ict = cursor.getString(cursor.getColumnIndex("ficon"));
                String icf = cursor.getString(cursor.getColumnIndex("aicon"));
                if (!TextUtils.isEmpty(ict)){
                    marker.iconBase = ict;
                }else if (!TextUtils.isEmpty(icf)){
                    marker.iconBase = icf;
                }

                String r = cursor.getString(cursor.getColumnIndex("r"));
                String g = cursor.getString(cursor.getColumnIndex("g"));
                String b = cursor.getString(cursor.getColumnIndex("b"));
                if (!TextUtils.isEmpty(r) && !TextUtils.isEmpty(g) && !TextUtils.isEmpty(b) ){
                    Sys_Color sys_color = new Sys_Color();
                    sys_color.setR(r);
                    sys_color.setG(g);
                    sys_color.setB(b);
                    marker.sys_color = sys_color;
                }


                markers.add(marker);
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            ErrorUtil.showError(e);
        }finally {
            db.endTransaction();
            db.close();
        }

        return markers;
    }




}
