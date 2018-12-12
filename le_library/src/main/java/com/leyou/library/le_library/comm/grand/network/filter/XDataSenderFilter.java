package com.leyou.library.le_library.comm.grand.network.filter;

import android.os.Handler;

import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.HttpHelper;
import com.ichsy.libs.core.net.http.OkHttpClientHelper;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.handler.RequestHandler;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * X 表单
 * <p>
 * Created by ss on 2018/7/10.
 */
public class XDataSenderFilter implements RequestHandler {

    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {

        RequestOptions options = httpContext.getOptions();
        try {
            Object object = httpContext.getRequestObject();
            if (object == null) {
                httpContext.setRequest("");
            } else {
                httpContext.setRequest(obj2String(object));
            }
//            RequestBody formBody = FormBody.create(
//                    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), httpContext.getRequest());

            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (httpContext.getRequestObject() != null) {
                Object requestObject = httpContext.getRequestObject();
                Field[] fields = requestObject.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.get(requestObject) != null && !"".equals(field.get(requestObject).toString())) {
                        formBodyBuilder.add(field.getName(), field.get(requestObject).toString());
                    }
                }
            }
            Request request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();

            OkHttpClient okHttpClient;
            if (0 != options.getTimeout()) {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(httpContext.getOptions().getTimeout(), TimeUnit.SECONDS)
                        .writeTimeout(httpContext.getOptions().getTimeout(), TimeUnit.SECONDS)
                        .readTimeout(httpContext.getOptions().getTimeout(), TimeUnit.SECONDS)
                        .build();
            } else {
                okHttpClient = OkHttpClientHelper.INSTANCE.getOkHttpClient();
            }

            Response response = okHttpClient.newCall(request).execute();
            httpContext.httpCode = response.code();
            if (response.isSuccessful()) {
                httpContext.setResponse(response.body().string());
            } else {
                String serviceResponse;
                if (response.body() != null) {
                    serviceResponse = response.body().string();
                } else {
                    serviceResponse = response.toString();
                }
                httpContext.setResponse(serviceResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {
        try {
            Object response = GsonHelper.build().fromJson(httpContext.getResponse(), httpContext.getResponseClass());
            httpContext.setResponseObject(response);
            httpContext.isRequestSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(HttpHelper.TAG, "HttpUtil: " + getClass().getCanonicalName() + " Error");
            LogUtils.e(HttpHelper.TAG, "Source JSON：---->>> " + httpContext.getResponse() + " <<<---\n");
//            LogUtils.e(HttpHelper.TAG, ExceptionUtil.getException(e));
            LogUtils.e(HttpHelper.TAG, e.getMessage());
        }
    }

    private String obj2String(Object obj) {
        StringBuilder builder = new StringBuilder();
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                field.setAccessible(true);
                if (field.get(obj) != null && !"".equals(field.get(obj).toString())) {
                    builder.append(field.getName()).append("=").append(field.get(obj).toString());
                    builder.append("&");
                }
            }
            int length = builder.length();
            if (length > 0) {
                builder.deleteCharAt(builder.lastIndexOf("&"));
            }
        } catch (IllegalAccessException e) {
            LogUtils.e(HttpHelper.TAG, e.getMessage());
        }
        return builder.toString();
    }
}
