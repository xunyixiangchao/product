package com.leyou.library.le_library.comm.impl;

import android.support.annotation.NonNull;

import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.leyou.library.le_library.config.LeConstant;

/**
 * 根据乐友请求协议自动处理了请求
 * <p>
 * 简化了判断code逻辑
 *
 * @author liuyuhang
 * @date 2018/1/24
 */
public abstract class LeRequestListener extends RequestListener {

    /**
     * 网络请求成功
     *
     * @param url
     * @param httpContext
     */
    public abstract void onRequestSuccess(@NonNull String url, @NonNull HttpContext httpContext);

    /**
     * 网络请求失败
     *
     * @param url
     * @param httpContext
     */
    public abstract void onRequestFailed(@NonNull String url, @NonNull HttpContext httpContext);

    @Override
    public void onHttpRequestComplete(@NonNull String url, @NonNull HttpContext httpContext) {
        super.onHttpRequestComplete(url, httpContext);

        if (LeConstant.CODE.CODE_OK == httpContext.code) {
            onRequestSuccess(url, httpContext);
        } else {
            onRequestFailed(url, httpContext);
        }
    }
}
