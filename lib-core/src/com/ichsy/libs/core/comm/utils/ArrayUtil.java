package com.ichsy.libs.core.comm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 处理array的工具类
 * Created by liuyuhang on 16/4/26.
 */
public class ArrayUtil {
    public interface MapHandler<K, V> {
        void onNext(K key, V value);
    }

    public interface ListHandler<T> {
        void onNext(int position, T item);
    }

    /**
     * 检查list,判断position是否越界
     *
     * @param categoryList
     * @param position
     * @return true:安全 false:不安全
     */
    public static boolean checkSafe(List categoryList, int position) {
        return !((ObjectUtils.isNull(categoryList)) || (position >= categoryList.size()) || position < 0);
    }

    public static <T> T getArrayObject(List<T> list, int position) {
        if (null == list || position >= list.size()) {
            return null;
        } else {
            return list.get(position);
        }
    }

    /**
     * 迭代列表
     *
     * @param list
     */
    public static <T> void iteratorList(List<T> list, ListHandler<T> handler) {
        if (null == list || list.isEmpty()) return;

        for (int i = 0; i < list.size(); i++) {
            handler.onNext(i, list.get(i));
        }

    }

//    public static <T> T get(List list, int position) {
//        return null;
//    }

    /**
     * 获取map中的key和value
     *
     * @param map
     * @param handler
     * @param <K>
     * @param <V>
     */
    public static <K, V> void arrayMap(HashMap<K, V> map, MapHandler<K, V> handler) {
//        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            K key = (K) entry.getKey();
            V value = (V) entry.getValue();
            handler.onNext(key, value);
        }
    }

    public static <T> T getLastItem(List<T> data) {
        if (data == null || data.isEmpty()) return null;
        return data.get(data.size() - 1);
    }

    /**
     * 数组转行成list，会做为空判断，如果为null，为了解决空指针，会new一个新的list
     *
     * @param a
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T[] a) {
        if (null == a) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(a);
        }
    }

    /**
     * 获取list的size，会对null进行判断
     *
     * @param list
     * @return
     */
    public static int size(List list) {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    /**
     * 判断两个list数据是否相同
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }


}
