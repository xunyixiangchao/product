package com.ichsy.libs.core.comm.bus.url;

import android.content.Context;

/**
 * url解析器的回调
 *
 * @author liuyuhang
 * @date 2018/1/12
 */
public interface IUrlParserCallback {

    /**
     * url解析之前，可以对url进行转换处理
     * @param context
     * @return
     */
    String boforeUrlParser(Context context);

    void onParserFailed(Context context, String url);
}
