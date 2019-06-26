package com.zt.map.util.out;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.faker.repaymodel.util.error.ErrorUtil;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author dmrfcoder
 * @date 2018/8/9
 */
public class ExcelUtil {
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public static String tableTitle(Class c){
       if (!c.isAnnotationPresent(ExcelName.class)){
            throw new RuntimeException("请指定表格标题");
       }
        ExcelName excelName = (ExcelName) c.getAnnotation(ExcelName.class);
        return excelName.TabName();
    }

    /**
     * 列名解析
     *
     * @return
     */
    public static String[] colName(Class c) {
        Field[] fields = c.getDeclaredFields();
        Map<Integer,String> valueMap = new TreeMap();
        for (Field fielditem : fields) {
            String oValue = null;
            if (!fielditem.isAnnotationPresent(ExcelCount.class)) {
                continue;
            }
            ExcelCount count = fielditem.getAnnotation(ExcelCount.class);
            if (count != null) {
                int order = count.order();
                String name = count.name();
                valueMap.put(order,name);
            }
        }
        List<String> names = new ArrayList<>();
        for (Integer name : valueMap.keySet()) {
            names.add(valueMap.get(name));
        }
        String[] ns = names.toArray(new String[names.size()]);
        return ns;
    }

    /**
     * 初始化Excel
     *
     * @param tableName 表格名称
     * @param fileName  导出excel存放的地址（目录）
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String tableName,String path, String fileName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            File file = new File(path+"/"+fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(tableName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 目前只支持String Double Integer
     *
     * @param objList
     * @param fileName
     * @param c
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    List<String> list = new ArrayList<>();
                    Map<Integer, String> orderVs = new TreeMap<>();

                    Object obj = objList.get(j);
                    Field[] fields = obj.getClass().getDeclaredFields();
                    for (Field fielditem : fields) {
                        String name = fielditem.getName();
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                        String type = fielditem.getType().toString();
                        String oValue = null;

                        Annotation[] ats = fielditem.getAnnotations();
                        // TODO: 2019/5/20 ????为何元注解是NULL
                        if (!fielditem.isAnnotationPresent(ExcelCount.class)) {
                            continue;
                        }
                        if (type.equals("class java.lang.String")) {
                            Method m = obj.getClass().getMethod("get" + name);
                            String value = (String) m.invoke(obj); // 调用getter方法获取属性值
                            oValue = value;
                        } else if (type.equals("class java.lang.Integer")||type.equals("int")) {
                            Method m = obj.getClass().getMethod("get" + name);
                            Integer value = (Integer) m.invoke(obj);
                            if (value == null) {
                                value = 0;
                            }
                            oValue = value.toString();
                        } else if (type.equals("class java.lang.Double")||type.equals("double")) {
                            Method m = obj.getClass().getMethod("get" + name);
                            Double value = (Double) m.invoke(obj);
                            if (value == null) {
                                value = 0d;
                            }
                            oValue = value.toString();
                        } else if (type.equals("class java.lang.Long")||type.equals("long")) {
                            Method m = obj.getClass().getMethod("get" + name);
                            Long value = (Long) m.invoke(obj);
                            if (value == null) {
                                value = 0l;
                            }
                            oValue = value.toString();
                        } else {
                            oValue = null;
                        }
//                        list.add(oValue);
                        int order = fielditem.getAnnotation(ExcelCount.class).order();
                        orderVs.put(order, oValue);
                    }

                    for (Integer key : orderVs.keySet()) {
                        list.add(orderVs.get(key));
                    }

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        String li = list.get(i);
                        if (TextUtils.isEmpty(li)) {
                            sheet.setColumnView(i, 8);
                        } else if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }

                writebook.write();
//                Toast.makeText(c, "导出Excel成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                ErrorUtil.showError(e);
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


    public static synchronized Object CloneAttribute(Object clone, Object beCloned) {
        Field[] fieldClone = null;
        Field[] fieldBeCloned = null;
        Map<String, Field> map = new HashMap<String, Field>();
        try {
            Class<?> classClone = clone.getClass();
            Class<?> classBecloned = beCloned.getClass();

            Annotation[] ats = classBecloned.getAnnotations();

            fieldClone = classClone.getDeclaredFields();
            fieldBeCloned = classBecloned.getDeclaredFields();

            for (int t = 0; t < fieldBeCloned.length; t++) {
                map.put(fieldBeCloned[t].getName(), fieldBeCloned[t]);
            }

            for (int i = 0; i < fieldClone.length; i++) {
                String fieldCloneName = fieldClone[i].getName();
                Field fie = map.get(fieldCloneName);
                if (fie != null) {
                    try {
                        Method method1 = classClone.getMethod(getMethodName(fieldCloneName));
                        Method method2 = classBecloned.getMethod(setMethodName(fieldCloneName), fie.getType());
                        method2.invoke(beCloned, method1.invoke(clone));
                    }catch (NoSuchMethodException e){
                        ErrorUtil.showError(e);
                        continue;
                    }

                }
            }
        } catch (Exception e) {
            ErrorUtil.showError(e);
        } finally {
            fieldClone = null;
            fieldBeCloned = null;
            map.clear();
        }
        return beCloned;
    }

    private static String getMethodName(String fieldName) {
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "get" + head + tail;
    }

    private static String setMethodName(String fieldName) {
        String head = fieldName.substring(0, 1).toUpperCase();
        String tail = fieldName.substring(1);
        return "set" + head + tail;
    }

}
