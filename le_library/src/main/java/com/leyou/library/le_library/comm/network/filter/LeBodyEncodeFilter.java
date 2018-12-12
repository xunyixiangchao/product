package com.leyou.library.le_library.comm.network.filter;

import android.os.Handler;

import com.ichsy.libs.core.comm.utils.Base64Util;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.leyou.library.le_library.model.BaseRequest;
import com.leyou.library.le_library.model.ProtocolHeader;

/**
 * 乐友专用的http请求
 * Created by liuyuhang on 16/6/1.
 */
@Deprecated
public class LeBodyEncodeFilter implements RequestHandler {

    /**
     * 因为加密需要，需要把body转换为string然后做加密，最终发送给服务器的就是这个对象
     */
    public class RequestEncodeData {
        public ProtocolHeader header;
        public String body;
    }

    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {

        if (httpContext.getRequestObject() instanceof BaseRequest) {
            BaseRequest request = ((BaseRequest) httpContext.getRequestObject());
            RequestEncodeData encodeData = new RequestEncodeData();
            encodeData.header = request.header;

            LogUtils.i("HttpHelper", "body en:" + GsonHelper.build().toJson(request.body));
            encodeData.body = Base64Util.encode(GsonHelper.build().toJson(request.body).getBytes());

            httpContext.setRequestObject(encodeData);
//            httpContext.setRequest(GsonHelper.build().toJson(decodeData));
        }
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
