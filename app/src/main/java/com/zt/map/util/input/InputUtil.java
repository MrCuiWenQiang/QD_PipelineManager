package com.zt.map.util.input;

import android.text.TextUtils;

import com.zt.map.entity.db.system.Sys_Table;
import com.zt.map.entity.db.tab.Tab_Marker;
import com.zt.map.util.out.ExcelCount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.faker.repaymodel.util.LogUtil;
import cn.faker.repaymodel.util.db.litpal.LitPalUtils;
import cn.faker.repaymodel.util.error.ErrorUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class InputUtil {
    /**
     * 导入管点表
     */
    public static void inputMarker(String filePath, Long projectId) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.isFile()) {
            return;
        }
        if (!filePath.contains("xls")) {
            return;
        }
        List<Tab_Marker> markers = new ArrayList<>();

        // TODO: 2019/11/5 首先获取class类导出注解属性 再去决定导入
        Map<String, String> rowMap = new HashMap<>();
        Map<String, Class<?>> rowTypeMap = new HashMap<>();
        Map<String, Integer> rowIndexMap = new HashMap<>();
        //set方法
        Field[] fields = Tab_Marker.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelCount.class)) {
                continue;
            }
            String name = field.getAnnotation(ExcelCount.class).name();
            rowMap.put(name, field.getName());
            rowTypeMap.put(name, field.getType());
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            Workbook rwb = Workbook.getWorkbook(fis);
            Sheet[] sheet = rwb.getSheets();
            int length = sheet.length;
            if (length <= 0) {
                return;
            }
            for (int i = 0; i < length; i++) {//读sheet
                Sheet rs = rwb.getSheet(i);
                for (int j = 0; j < rs.getRows(); j++) {//读行
                    Cell[] cells = rs.getRow(j);
                    if (j == 0) {
                        for (int k = 0; k < cells.length; k++) {//读列
                            String value = cells[k].getContents();
                            for (String name : rowMap.keySet()) {
                                if (name.equals(value)) {
                                    rowIndexMap.put(name, k);
                                }
                            }
                        }
                    } else {
                        Tab_Marker marker = settingMKValue(rowTypeMap, rowMap, rowIndexMap, cells, projectId);
                        markers.add(marker);
                    }
                }
            }
            fis.close();
            LitPalUtils.saveAll(markers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ErrorUtil.showError(e);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorUtil.showError(e);
        } catch (BiffException e) {
            e.printStackTrace();
            ErrorUtil.showError(e);
        }
    }

    private static Tab_Marker settingMKValue(Map<String, Class<?>> rowTypeMap, Map<String, String> rowMap, Map<String, Integer> rowIndexMap, Cell[] cells, Long projectId) {
        Tab_Marker marker = new Tab_Marker();
        for (String name : rowIndexMap.keySet()) {
            int index = rowIndexMap.get(name); //列索引值
            String mothedName = rowMap.get(name);//方法名
            String value = cells[index].getContents();
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            Class<?> type = rowTypeMap.get(name);
            Method m = null;
            try {
                m = marker.getClass().getMethod("set" + captureName(mothedName), type);
                m.invoke(marker, getClassTypeValue(type, value));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                ErrorUtil.showError(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                ErrorUtil.showError(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                ErrorUtil.showError(e);
            } finally {
                LogUtil.e("1", mothedName);
            }
        }
        if (!TextUtils.isEmpty(marker.getGxlxValue())) {
            Sys_Table table = LitPalUtils.selectsoloWhere(Sys_Table.class, " code = ?",marker.getGxlxValue());
            if (table!=null){
                marker.setTypeId(table.getId());
            }
        }

/*        for (String name : rowIndexMap.keySet()) {
            int index = rowIndexMap.get(name);
            String value = cells[index].getContents();
            if (name.equals("管类") ) {
                if (table != null) {
                    marker.setTypeId(table.getId());
                }
                break;
            }
        }*/
        marker.setProjectId(projectId);
        marker.setCreateTime(new Date());
        return marker;
    }

    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }


    private static Object getClassTypeValue(Class<?> typeClass, Object value) {
        if (typeClass == int.class || value instanceof Integer) {
            if (null == value) {
                return 0;
            }
            return Integer.valueOf(String.valueOf(value));
        } else if (typeClass == short.class) {
            if (null == value) {
                return 0;
            }
            return Short.valueOf(String.valueOf(value));
        } else if (typeClass == byte.class) {
            if (null == value) {
                return 0;
            }
            return Byte.parseByte(String.valueOf(value));
        } else if (typeClass == double.class) {
            if (null == value) {
                return 0;
            }
            return Double.valueOf(String.valueOf(value));
        } else if (typeClass == long.class) {
            if (null == value) {
                return 0;
            }
            return Long.valueOf(String.valueOf(value));
        } else if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value;
        } else if (typeClass == boolean.class) {
            if (null == value) {
                return true;
            }
            return Boolean.valueOf(String.valueOf(value));
        } else if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        } else {
            return typeClass.cast(value);
        }
    }


}
