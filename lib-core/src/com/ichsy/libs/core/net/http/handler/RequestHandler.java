package com.ichsy.libs.core.net.http.handler;

import android.os.Handler;

import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;

/**
 * @author LiuYuHang Date: 2015年5月29日
 *         <p>
 *         Modifier： Modified Date： Modify：
 *         <p>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net.http.handler
 * @File HttpFliter.java
 */
public interface RequestHandler {

    /**
     * 发送请求要处理的业务
     *
     * @param url         请求的url
     * @param httpContext 请求的上下文，保存了请求的所有参数
     * @param listener    Modifier： Modified Date： Modify：
     */
    void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler);

    /**
     * 接收到请求的时候要处理的业务
     *
     * @param url         请求的url
     * @param httpContext 请求的上下文，保存了请求的所有参数
     * @param listener    回调
     * @param handler     在主线程中回调
     *                    Modifier： Modified Date： Modify：
     */
    void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler);
}
