package com.zt.map.util.out;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import cn.faker.repaymodel.util.error.ErrorUtil;

public class AccessUtil {
/*

    private static final String TAG_TABLE_NAME = "#{tableName}";
    private static final String TAG_TABLE_VALUE = "#{value}";
    private static final String TEMPLATESQL = "insert into JS_LINE(" + TAG_TABLE_NAME + ") values (" + TAG_TABLE_VALUE + ")";

    *//**
     * 自动对列名 表名解析
     *
     * @param objList
     * @param <T>
     *//*
    public static <T> void insert(List<T> objList,String path) {
        if (objList == null || objList.size() <= 0) {
            return;
        }
        for (int i = 0; i < objList.size(); i++) {
            Object obj = objList.get(i);
            Class c = obj.getClass();
            Field[] fields = c.getDeclaredFields();
            String tableName = ((AccessTableName) c.getAnnotation(AccessTableName.class)).name();

            Map<String, String> rowMap = new HashMap<>();
            for (Field item_field : fields) {
                if (!item_field.isAnnotationPresent(ExcelCount.class)) {
                    continue;
                }
                String name = item_field.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                try {
                    Method m = c.getMethod("get" + name);
                    Object value = m.invoke(obj);
                    if (value == null) {
                        value = "null";
                    }

                    ExcelCount excelCount = item_field.getAnnotation(ExcelCount.class);

                    String rowName = excelCount.name();
                    rowMap.put(rowName, value.toString());

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    ErrorUtil.showError(e);
                    continue;
                } catch (IllegalAccessException e) {
                    ErrorUtil.showError(e);
                    e.printStackTrace();
                    continue;
                } catch (InvocationTargetException e) {
                    ErrorUtil.showError(e);
                    e.printStackTrace();
                    continue;
                }
            }
            List<String> colName = new ArrayList<>();
            List<String> colValue = new ArrayList<>();
            if (rowMap.size() > 0) {
                for (String key : rowMap.keySet()) {
                    colName.add(key);
                    colValue.add(rowMap.get(key));
                }
            } else {
                continue;
            }
            StringBuilder sql_row_name = new StringBuilder();
            for (String item : colName) {
                sql_row_name.append(item);
                sql_row_name.append(",");
            }
            sql_row_name.deleteCharAt(sql_row_name.length() - 1);

            StringBuilder sql_row_value = new StringBuilder();
            for (String value : colValue) {
                sql_row_value.append(value);
                sql_row_value.append(",");
            }
            sql_row_value.deleteCharAt(sql_row_value.length() - 1);

            StringBuilder sql = new StringBuilder(TEMPLATESQL);
            int name_index = sql.indexOf(TAG_TABLE_NAME);
            int value_index = sql.indexOf(TAG_TABLE_VALUE);
            sql.replace(name_index, name_index + TAG_TABLE_NAME.length(), sql_row_name.toString());
            sql.replace(value_index, value_index + TAG_TABLE_VALUE.length(), sql_row_value.toString());
            String sql_finish = sql.toString();
            init(path);
        }
    }

    *//**
     * 列名解析
     *
     * @return
     *//*
    public static String[] colName(Class c) {
        Field[] fields = c.getDeclaredFields();
        Map<Integer, String> valueMap = new TreeMap();
        for (Field fielditem : fields) {
            String oValue = null;
            if (!fielditem.isAnnotationPresent(ExcelCount.class)) {
                continue;
            }
            ExcelCount count = fielditem.getAnnotation(ExcelCount.class);
            if (count != null) {
                int order = count.order();
                String name = count.name();
                valueMap.put(order, name);
            }
        }
        List<String> names = new ArrayList<>();
        for (Integer name : valueMap.keySet()) {
            names.add(valueMap.get(name));
        }
        String[] ns = names.toArray(new String[names.size()]);
        return ns;
    }


    private static Statement stmt;

    public final static Statement init(String path) {
        if (stmt != null) {
            return stmt;
        }
        try {
            DriverManager.registerDriver(new UcanaccessDriver());
//            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://"+path);
            stmt = con.createStatement();
        }  catch (SQLException e) {
            ErrorUtil.showError(e);
            e.printStackTrace();
        }catch (Exception e){
            ErrorUtil.showError(e);
        }finally {
            return stmt;
        }
    }*/
}
