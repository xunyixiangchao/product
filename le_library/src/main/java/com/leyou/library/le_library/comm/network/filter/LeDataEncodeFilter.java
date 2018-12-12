package com.leyou.library.le_library.comm.network.filter;

import android.os.Handler;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.logwatch.LogWatcher;
import com.ichsy.libs.core.comm.utils.Base64Util;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.handler.RequestHandler;

/**
 * 对请求参数做加密解密
 * Created by liuyuhang on 16/6/1.
 */
public class LeDataEncodeFilter implements RequestHandler {


    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        String sourceData = httpContext.getRequest();
        LogUtils.i("HttpHelper", "request before encode: " + sourceData);
        LogWatcher.getInstance().putRequestInfo("[request before encode]\n" + sourceData);
        if (!TextUtils.isEmpty(sourceData)) {
//            if (null != httpContext.getOptions() && httpContext.getOptions().getRequestType().equals("post")) {
//                httpContext.setRequest("json=" + Base64Util.nativeEnCode(sourceData));
//            } else {
                httpContext.setRequest(Base64Util.nativeEnCode(sourceData));
//            }
        }

//        if (httpContext.getRequestObject() instanceof BaseRequest) {
//            BaseRequest request = ((BaseRequest) httpContext.getRequestObject());
//            RequestEncodeData encodeData = new RequestEncodeData();
//            encodeData.header = request.header;
//
//            LogUtils.i("HttpHelper", "body en:" + GsonHelper.build().toJson(request.body));
//            encodeData.body = Base64Util.encode(GsonHelper.build().toJson(request.body).getBytes());
//
//            httpContext.setRequestObject(encodeData);
////            httpContext.setRequest(GsonHelper.build().toJson(decodeData));
//        }
//        else {
//            httpContext.setRequest(GsonHelper.build().toJson(httpContext.getRequestObject()));
//        }
    }

    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
//        Object baseResponse = null;
//        try {
//            baseResponse = GsonHelper.build().fromJson(httpContext.getResponse(), httpContext.getResponseClass());
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.e(HttpHelper.TAG, "HttpUtil: TransformFilter Error------>>>>>>" + ExceptionUtil.getException(e));
//        }
//        httpContext.setResponseObject(baseResponse);
    }
}
