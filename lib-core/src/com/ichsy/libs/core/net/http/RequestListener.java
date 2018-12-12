package com.ichsy.libs.core.net.http;

import android.support.annotation.NonNull;

/**
 * 请求网络的回调
 *
 * @author LiuYuHang
 * @date 2014年11月14日
 */
public class RequestListener {

    /**
     * 请求开始的时候
     *
     * @param url
     * @author LiuYuHang
     * @date 2014年11月14日
     */
    public void onHttpRequestBegin(@NonNull String url) {
    }

    /**
     * 请求之后会回调的方法
     *
     * @param url
     * @param httpContext
     * @author LiuYuHang
     * @date 2014年11月14日
     */
    public void onHttpRequestSuccess(@NonNull String url, @NonNull HttpContext httpContext) {
    }

    /**
     * 网络请求超时的回调
     *
     * @param url
     * @param httpContext
     */
    public void onHttpRequestTimeOut(@NonNull String url, @NonNull HttpContext httpContext) {

    }

    /**
     * 请求失败回调的接口
     *
     * @param url
     * @author LiuYuHang
     * @date 2014年11月14日
     */
    public void onHttpRequestFailed(@NonNull String url, @NonNull HttpContext httpContext) {
    }

    /**
     * 请求结束之后（不管成功或者失败，都会执行本方法）
     *
     * @param url
     * @param httpContext 请求回来的对象
     * @author LiuYuHang
     * @date 2014年11月14日
     */
    public void onHttpRequestComplete(@NonNull String url, @NonNull HttpContext httpContext) {
    }

    /**
     * 调用 {@link HttpHelper#cancel(String)}之后，会回调本方法
     *
     * @param url
     * @param httpContext
     * @author LiuYuHang
     * @date 2014年11月21日
     */
    public void onHttpRequestCancel(@NonNull String url, @NonNull HttpContext httpContext) {
    }
}
