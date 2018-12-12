package com.leyou.library.le_library.comm.network;

import android.content.Context;

import com.ichsy.libs.core.net.http.HttpRequestInterface;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.cache.SimpleCacheAdapter;

/**
 * httphelper构建类
 * Created by liuyuhang on 2018/6/21.
 */

public class HttpHelperBuilder {

    public static LeHttpHelper getDefaultHttpHelper(Context context) {
        return new LeHttpHelper(context);
    }

    /**
     * 获取一个不会弹出提示的httpHelper
     *
     * @param context
     * @return
     */
    public static LeHttpHelper getSilentHttpHelper(Context context) {
        RequestOptions options = new RequestOptions();
        options.toastDisplay(false);
        return new LeHttpHelper(context, options);
    }

    /**
     * 带缓存的http请求
     *
     * @param context
     * @return
     */
    public static HttpRequestInterface getDefaultCacheHttpHelper(Context context) {
        LeHttpHelper httpHelper = getDefaultHttpHelper(context);
        return new SimpleCacheAdapter(context, httpHelper);
    }
}
