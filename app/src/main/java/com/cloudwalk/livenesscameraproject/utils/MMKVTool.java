package com.cloudwalk.livenesscameraproject.utils;

import com.tencent.mmkv.MMKV;

/**
 * MMKV 工具
 */
public class MMKVTool {
    private static MMKV mmkv= MMKV.defaultMMKV();

    /**
     * String数据存取
     * @param key
     * @param value
     * @return
     */
    public static boolean saveString(String key,String value){
        return mmkv.encode(key,value);
    }
    public static String loadString(String key,String defaultValue){
        return mmkv.decodeString(key,defaultValue);
    }

    /**
     * int数据存取
     * @param key
     * @param value
     * @return
     */
    public static boolean saveInt(String key,int value){
        return mmkv.encode(key,value);
    }
    public static int loadInt(String key,int defaultValue){
        return mmkv.decodeInt(key,defaultValue);
    }

    /**
     * double数据存取
     * @param key
     * @param value
     * @return
     */
    public static boolean saveDouble(String key,double value){
        return mmkv.encode(key,value);
    }
    public static double loadDouble(String key,double defaultValue){
        return mmkv.decodeDouble(key,defaultValue);
    }

    public static boolean saveBoolean(String key, boolean value) {
        return mmkv.encode(key, value);
    }

    public static boolean loadBoolean(String key, boolean defaultValue) {
        return mmkv.decodeBool(key, defaultValue);
    }

    public static boolean saveFloat(String key, float value) {
        return mmkv.encode(key, value);
    }

    public static float loadFloat(String key, float defaultValue) {
        return mmkv.decodeFloat(key, defaultValue);
    }
}
