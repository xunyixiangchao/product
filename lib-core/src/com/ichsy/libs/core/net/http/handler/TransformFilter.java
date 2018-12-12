package com.ichsy.libs.core.net.http.handler;

import android.os.Handler;

import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.HttpHelper;
import com.ichsy.libs.core.net.http.RequestListener;

/**
 * 类型转换的过滤器
 *
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net.http.handler
 * @File HttpTransformFilter.java
 */
public class TransformFilter implements RequestHandler {
    /*
     * (non-Javadoc)
     *
     * @see
     * com.ichsy.libs.core.net.http.handler.RequestHandler#onRequest(java.lang
     * .String, com.ichsy.libs.core.net.http.HttpContext,
     * com.ichsy.libs.core.net.http.RequestListener)
     */
    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        if (null == httpContext.getRequestObject()) {
            httpContext.setRequest("");
        } else {
            httpContext.setRequest(GsonHelper.build().toJson(httpContext.getRequestObject()));
        }
//		httpContext.setRequest(JsonHelper.obj2Json(httpContext.getRequestObject()));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.ichsy.libs.core.net.http.handler.RequestHandler#onResponse(java.lang
     * .String, com.ichsy.libs.core.net.http.HttpContext,
     * com.ichsy.libs.core.net.http.RequestListener)
     */
    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        Object baseResponse = null;
        try {
            baseResponse = GsonHelper.build().fromJson(httpContext.getResponse(), httpContext.getResponseClass());
            httpContext.isRequestSuccess = true;
//			baseResponse = (Object) JsonHelper.json2Obj(httpContext.getResponse(), httpContext.getResponseClass());
        } catch (Exception e) {
            e.printStackTrace();
            httpContext.isRequestSuccess = false;

            LogUtils.e(HttpHelper.TAG, "HttpUtil: TransformFilter Error------");
            LogUtils.e(HttpHelper.TAG, "{JSON}  " + httpContext.getResponse() + "\n");
//            LogUtils.e(HttpHelper.TAG, ExceptionUtil.getException(e));
            LogUtils.e(HttpHelper.TAG, e.getMessage());
        }
        httpContext.setResponseObject(baseResponse);
    }

}
