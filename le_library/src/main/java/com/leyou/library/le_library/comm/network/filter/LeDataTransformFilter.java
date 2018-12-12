package com.leyou.library.le_library.comm.network.filter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.HttpHelper;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.leyou.library.le_library.comm.network.RequestUtil;
import com.leyou.library.le_library.config.LeConstant;
import com.leyou.library.le_library.model.BaseRequest;
import com.leyou.library.le_library.model.BaseResponse;
import com.leyou.library.le_library.model.ProtocolHeader;

import java.util.HashMap;

/**
 * 乐友专属的body转换类， response为Header+Body
 * Created by liuyuhang on 2016/11/23.
 */

public class LeDataTransformFilter implements RequestHandler {
    /**
     * 发送请求要处理的业务
     *
     * @param url         请求的url
     * @param httpContext 请求的上下文，保存了请求的所有参数
     * @param listener    Modifier： Modified Date： Modify：
     * @param handler
     */
    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        BaseRequest baseRequest = new BaseRequest();

        Context context = httpContext.getContext().get();
        if (null != context) {
            baseRequest.header = RequestUtil.createRequestHeader(context, RequestUtil.getTransType(url));
        }

        baseRequest.body = httpContext.getRequestObject();
//        if (null != httpContext.getOptions() && httpContext.getOptions().getRequestType().equals("post")) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("json", body);
//            httpContext.setRequestObject(map);
//        } else {
        httpContext.setRequestObject(baseRequest);
//        }

        if (null == httpContext.getRequestObject()) {
            httpContext.setRequest("");
        } else {
            httpContext.setRequest(GsonHelper.build().toJson(httpContext.getRequestObject()));
        }
//        }
    }

    /**
     * 接收到请求的时候要处理的业务
     *
     * @param url         请求的url
     * @param httpContext 请求的上下文，保存了请求的所有参数
     * @param listener    回调
     * @param handler     在主线程中回调
     */
    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        try {
            HashMap<String, Object> sourceResponse = GsonHelper.build().fromJson(httpContext.getResponse(), new TypeToken<HashMap<String, Object>>() {
            }.getType());

            ProtocolHeader header = GsonHelper.build().fromJson(GsonHelper.build().toJson(sourceResponse.get("header")), ProtocolHeader.class);
            httpContext.code = header.res_code;
            httpContext.message = header.message;

            HashMap<String, Object> params = httpContext.getParams();
            params.put("le_header", header);

            Object response = GsonHelper.build().fromJson(GsonHelper.build().toJson(sourceResponse.get("body")), httpContext.getResponseClass());
            httpContext.setResponseObject(response);

            httpContext.isRequestSuccess = true;
//			baseResponse = (Object) JsonHelper.json2Obj(httpContext.getResponse(), httpContext.getResponseClass());
        } catch (Exception e) {
            e.printStackTrace();

            BaseResponse response;
//            try {
//                response = (BaseResponse) httpContext.getResponseClass().newInstance();
//            } catch (Exception e1) {
//                e1.printStackTrace();
            response = new BaseResponse();
//            }

            response.header = new ProtocolHeader();
            if (TextUtils.isEmpty(httpContext.getResponse())) {
                httpContext.code = LeConstant.CODE.CODE_SERVER_NO_RESPONSE;
                response.header.res_code = LeConstant.CODE.CODE_SERVER_NO_RESPONSE;

                response.header.message = "网络请求失败";
                httpContext.message = "网络请求失败";
            } else {
                response.header.res_code = LeConstant.CODE.CODE_SERVER_ERROR;
                httpContext.code = LeConstant.CODE.CODE_SERVER_ERROR;

                response.header.message = "服务器请求错误";
                httpContext.message = "服务器请求错误";
            }

            httpContext.isRequestSuccess = false;

            LogUtils.e(HttpHelper.TAG, "HttpUtil: " + getClass().getCanonicalName() + " Error");
            LogUtils.e(HttpHelper.TAG, "Source JSON：---->>> " + httpContext.getResponse() + " <<<---\n");
//            LogUtils.e(HttpHelper.TAG, ExceptionUtil.getException(e));
            LogUtils.e(HttpHelper.TAG, e.getMessage());
        }
    }
}
