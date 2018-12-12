package com.ichsy.libs.core.comm.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * 处理对象的工具类
 * Created by liuyuhang on 16/8/9.
 */
public class ObjectUtils {

    /**
     * 判断对象是否为空，如果有多个对象，只要有一个对象为null，就返回false
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        if (null == object) return true;

//        for (Object obj : object) {
        if (object instanceof CharSequence) {
            CharSequence str;
            if (object instanceof String) {
                str = ((String) object).trim();
            } else {
                str = (CharSequence) object;
            }
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        } else if (object instanceof List) {
            if (((List) object).isEmpty()) {
                return true;
            }
        }
//        }
        return false;
    }

    public static
    @NonNull
    boolean isNotNull(@Nullable Object object) {
        return !isNull(object);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return !(isNull(obj1) || isNull(obj2)) && obj1.equals(obj2);
    }

    public static Object getMapObject(Map<String, ?> map, String key) {
        if (isNotNull(map)) {
            return map.get(key);
        }
        return null;
    }

    public static <T> T get(Object object, Class clazz) {
        if (isNull(object)) {
//            if (clazz instanceof Boolean) {
//            }
        }
        return (T) object;
    }

    /**
     * 获取一个对象，如果为空，会创建出来
     *
     * @param object
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getOrCreateObject(T object, Class<T> clz) {
        if (isNull(object)) {
            try {
                object = clz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static <T> T hex2Object(String hex, Class<T> classOfT) {
        if (null == hex) return null;
        return GsonHelper.build().fromJson(hex, classOfT);
    }

    public static String Object2Hex(Object obj) {
        if (null == obj) return null;
        return GsonHelper.build().toJson(obj);
    }

}
