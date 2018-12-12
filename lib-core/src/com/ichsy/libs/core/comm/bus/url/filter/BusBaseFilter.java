package com.ichsy.libs.core.comm.bus.url.filter;

import android.content.Context;

import com.ichsy.libs.core.comm.bus.url.UrlParser;

import java.util.HashMap;

/**
 * BusFilter的基类，抽象了重要方法
 * Created by liuyuhang on 16/4/25.
 */
public abstract class BusBaseFilter {


    /**
     * 初始化并声明可处理的filter类型
     *
     * @return filterWhat[]，是个数组
     */
    public abstract String[] filterWhat();


    /**
     * 具体处理事件的方法
     *
     * @param context
     * @param params    携带参数，注意params有可能为null，需要做判断
     * @param attachMap 携带的参数
     * @return
     */
    public abstract void onAction(Context context, HashMap<String, String> params, HashMap<String, Object> attachMap);


}
