package com.leyou.library.le_library.comm.grand.network.filter;

import android.os.Handler;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.OkHttpClientHelper;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.handler.RequestHandler;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 达观application/json; charset=utf-8
 * <p>
 * Created by ss on 2018/7/10.
 */
public class SimpleDataSenderFilter implements RequestHandler {

    private String TAG = SimpleDataSenderFilter.class.getSimpleName();

    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {

        RequestOptions options = httpContext.getOptions();
        try {
            if (null == httpContext.getRequestObject()) {
                httpContext.setRequest("");
            } else {
                httpContext.setRequest(GsonHelper.build().toJson(httpContext.getRequestObject()));
            }
            String getUrl = url + (null == httpContext.getRequest() ? "" : httpContext.getRequest());
            Request request;
            if (!TextUtils.isEmpty(options.getRequestType()) && options.getRequestType().toLowerCase(Locale.CHINA).equals("get")) {
                LogUtils.i(TAG, "request url is[get]:" + getUrl);
                request = new Request.Builder().url(getUrl).get().build();
            } else {
                LogUtils.i(TAG, "request url is[post]:" + url);

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), httpContext.getRequest());
                request = new Request.Builder().url(url).post(requestBody).build();
                LogUtils.i(TAG, "request is:" + request.toString());
            }

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
        } catch (IOException e) {
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
            LogUtils.e(TAG, "HttpUtil: " + getClass().getCanonicalName() + " Error");
            LogUtils.e(TAG, "Source JSON：---->>> " + httpContext.getResponse() + " <<<---\n");
//            LogUtils.e(TAG, ExceptionUtil.getException(e));
            LogUtils.e(TAG, e.getMessage());
        }
    }
}
