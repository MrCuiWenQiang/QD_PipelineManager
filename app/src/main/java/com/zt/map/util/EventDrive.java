package com.zt.map.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Function : 为了解决增删改查事件更新问题 故创建事件模式
 * Remarks  :
 * Created by Mr.C on 2019/10/28 0028.
 */
public class EventDrive {
    private static final int MARKER = 1;
    private static final int LINE = 2;

    public static Map<Integer,List<Long>> addmap = new HashMap<>();
    public static Map<Integer,List> deletemap = new HashMap<>();
    public static  Map<Integer,Long> infomap = new HashMap<>();


    public static boolean isAddMarker(){
        return addmap.containsKey(MARKER);
    }

    public static boolean isAddLine(){
        return addmap.containsKey(LINE);
    }
    public static List<Long> getaddMarker(){
        return addmap.get(MARKER);
    }

    public static List<Long> getAddLine(){
        return addmap.get(LINE);
    }

    public static void addMarker(List<Long> ids){
        addmap.put(MARKER,ids);
    }

    public static void addLine(List<Long> ids){
        addmap.put(LINE,ids);
    }

    public static void clean(){
        addmap.clear();
    }


}
