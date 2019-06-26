package com.zt.map.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.faker.repaymodel.util.LogUtil;

/**
 * 2019年5月22日 09:42:47 文本解析器
 */
public class FileReadOpen {


    public static Data readText(InputStream file) throws IOException {
        if (file == null) {
            throw new RuntimeException("配置表不存在!");
        }

        Map<String, Map<String, List<Table>>> map = new LinkedHashMap();
        Map<String, String> fatherMap = new LinkedHashMap();

        Data data = new Data();
        data.setFatherMap(fatherMap);
        data.setMap(map);

        InputStreamReader insr = new InputStreamReader(file, "utf-8");
        BufferedReader bReader = new BufferedReader(insr);

        boolean isTypes = false;//判断是否是管线种类数据
        String childTabName = null;
        String childTab = null;

        String str;
        while ((str = bReader.readLine()) != null) {
            if (str == null) {
                continue;
            }
            str = str.trim();
            if (str.startsWith("[")) {
                int end = str.indexOf("]");
                if (end <= 0) {
                    throw new RuntimeException("类型名称不规范!");
                }
                String table = str.substring(1, end); //类型名称
//                isTypes = table.equals("管线种类");
                isTypes = table.contains("_");
                if (isTypes) { //如果是管线种类 不再做进一步名称解析
                    int c_index = table.indexOf("_");
                    childTab = table.substring(0, c_index);
                    childTabName = table.substring(c_index + 1, table.length());
                }

            } else {
                int index = str.indexOf("=");
                String key = str.substring(0, index);
                String value = str.substring(index + 1, str.length());
                if (!isTypes) {
//                    Table tab = new Table(key, value);
                    map.put(key, new LinkedHashMap<String, List<Table>>());
                    fatherMap.put(key, value);
                } else {
                    if (childTab == null) {
                        throw new RuntimeException("配置表不合法！ 请按照格式填写");
                    }
                    LogUtil.e("-------TAB NAME-------", childTab);
                    if (!map.containsKey(childTab)) {
                        continue;
                    }
                    Map<String, List<Table>> f_m = map.get(childTab);
                    List<Table> ds = f_m.get(childTabName);
                    if (ds == null) {
                        ds = new LinkedList<>();
                    }
                    ds.add(new Table(key, value));
                    f_m.put(childTabName, ds);
                }
            }
        }
        return data;
    }


    public static Map<String, String> readIcon(InputStream file) throws IOException {
        if (file == null) {
            throw new RuntimeException("配置表不存在!");
        }
        Map<String, String> params = new LinkedHashMap<>();
        InputStreamReader insr = new InputStreamReader(file, "utf-8");
        BufferedReader bReader = new BufferedReader(insr);
        String str;
        while ((str = bReader.readLine()) != null) {
            str = str.trim();
            int index = str.indexOf("=");
            String key = str.substring(0, index);
            String value = str.substring(index + 1, str.length());
            params.put(key, value);
        }
        return params;
    }

    public static Map<String, Map<String, String>> readTZORFSW(InputStream file) throws IOException {
        if (file == null) {
            throw new RuntimeException("配置表不存在!");
        }

        Map<String, Map<String, String>> fatherMap = new LinkedHashMap<>();

        InputStreamReader insr = new InputStreamReader(file, "utf-8");
        BufferedReader bReader = new BufferedReader(insr);
        String str;
        String tag = null;
        Map<String, String> childMap = new LinkedHashMap<>();
        while ((str = bReader.readLine()) != null) {

            str = str.trim();
            int index = str.indexOf("=");
            String key = str.substring(0, index);
            String father = str.substring(index + 1, str.length());

            int i_index = father.indexOf("=");
            String child = father.substring(0, i_index);
            String base = father.substring(i_index + 1, father.length());


            if (key.equals(tag)) {
                childMap.put(child, base);
            } else {
                tag = key;
                childMap = new LinkedHashMap<>();
                childMap.put(child, base);
                fatherMap.put(tag, childMap);
            }
        }
        return fatherMap;
    }

    public static Map<String, List<String>> readCaiZi(InputStream file) throws IOException {
        if (file == null) {
            throw new RuntimeException("配置表不存在!");
        }
        InputStreamReader inputStreamReader = new InputStreamReader(file);
        BufferedReader bReader = new BufferedReader(inputStreamReader);

        Map<String, List<String>> maps = new HashMap<>();
        List<String> params = null;
        String str = null;
        while ((str = bReader.readLine()) != null) {
            if (str.startsWith("[")) {
                int end = str.indexOf("]");
                if (end <= 0) {
                    throw new RuntimeException("类型名称不规范!");
                }
                String table = str.substring(1, end); //类型名称
                params = new ArrayList<>();
                maps.put(table, params);
            } else {
                params.add(str);
            }
        }
        return maps;
    }

    public static Map<String, Map<String, String>> readType(InputStream file) throws IOException {
        if (file == null) {
            throw new RuntimeException("配置表不存在!");
        }
        InputStreamReader inst = new InputStreamReader(file, "utf-8");
        BufferedReader bReader = new BufferedReader(inst);
        String str;
        boolean isType = false;//是否是读取大类阶段

        Map<String, Map<String, String>> maps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        while ((str = bReader.readLine()) != null) {
            if (str.startsWith("[")) {
                int end = str.indexOf("]");
                if (end <= 0) {
                    throw new RuntimeException("类型名称不规范!");
                }
                String table = str.substring(1, end); //类型名称

                if (table.equals("管线大类")) {
                    isType = true;
                } else {
                    isType = false;
                    if (!maps.containsKey(table)) {
                        throw new RuntimeException("配置表缺失");
                    } else {
                        params = maps.get(table);
                    }
                }
            } else if (isType) {
                String text = str.trim();
                maps.put(text, new HashMap<String, String>());
            } else {
                int index = str.indexOf("=");
                String key = str.substring(0, index);
                String value = str.substring(index + 1, str.length());
                params.put(key, value);
            }
        }
        return maps;
    }

    public static class Data {
        private Map<String, Map<String, List<Table>>> map;
        private Map<String, String> fatherMap;//管线种类

        public Map<String, Map<String, List<Table>>> getMap() {
            return map;
        }

        public void setMap(Map<String, Map<String, List<Table>>> map) {
            this.map = map;
        }

        public Map<String, String> getFatherMap() {
            return fatherMap;
        }

        public void setFatherMap(Map<String, String> fatherMap) {
            this.fatherMap = fatherMap;
        }
    }

}
