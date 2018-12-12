package com.leyou.library.le_library.comm.network.filter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.leyou.library.le_library.comm.network.RequestUtil;
import com.leyou.library.le_library.model.BaseResponse;
import com.leyou.library.le_library.model.ProtocolHeader;

/**
 * 为了减少每个请求对象都要继承baseRequest，包装方法写在这里
 * <p/>
 * Created by liuyuhang on 16/6/6.
 */
public class LeRequestPackingFilter implements RequestHandler {
    public static final int CODE_ERROR_NET_ERROR = -1;
    public static final int CODE_ERROR_SERVER_ERROR = -2;


    public static class LeRequest {
        public ProtocolHeader header;
        public Object body;
    }

    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        LeRequest baseRequest = new LeRequest();

        Context context = httpContext.getContext().get();
        if (null != context) {
            baseRequest.header = RequestUtil.createRequestHeader(context.getApplicationContext(), RequestUtil.getTransType(url));
        }

        baseRequest.body = httpContext.getRequestObject();
//        if (null != httpContext.getOptions() && httpContext.getOptions().getRequestType().equals("post")) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("json", body);
//            httpContext.setRequestObject(map);
//        } else {
        httpContext.setRequestObject(baseRequest);

    }

    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        BaseResponse response = httpContext.getResponseObject();
        if (null == response || null == response.header) {
            try {
                response = (BaseResponse) httpContext.getResponseClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                response = new BaseResponse();
            }

            response.header = new ProtocolHeader();

            String errorMessage;
            if (TextUtils.isEmpty(httpContext.getResponse())) {
                httpContext.code = CODE_ERROR_NET_ERROR;
                response.header.res_code = CODE_ERROR_NET_ERROR;
                errorMessage = "网络请求失败";
                response.header.message = errorMessage;
                httpContext.message = errorMessage;
            } else {
                response.header.res_code = CODE_ERROR_SERVER_ERROR;
                httpContext.code = CODE_ERROR_SERVER_ERROR;
                errorMessage = "服务器请求错误";
                response.header.message = errorMessage;
                httpContext.message = errorMessage;
            }

            httpContext.isRequestSuccess = false;
            httpContext.setResponseObject(response);
        } else {
            httpContext.code = response.header.res_code;
            httpContext.message = response.header.message;
        }
//        else {
//            BaseResponse response = httpContext.getResponseObject();
//            httpContext.code = response.header.res_code;
//            httpContext.message = response.header.message;
//        }
    }
}
