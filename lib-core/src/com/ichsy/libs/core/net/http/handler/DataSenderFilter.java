package com.ichsy.libs.core.net.http.handler;

import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.OkHttpClientHelper;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求发送的过滤器
 *
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net.http.handler
 * @File HttpDataSenderFilter.java
 */
public class DataSenderFilter implements RequestHandler {
    // private static final String TAG = DataSenderFilter.class.getSimpleName();
//    private static final int TIME_OUT = 10;
//
//    private static OkHttpClient okHttpClient;
//
//    public static OkHttpClient getOkHttpClient() {
//        synchronized (DataSenderFilter.class) {
//            if (okHttpClient == null) {
////                okHttpClient = new OkHttpClient();
//                okHttpClient = new OkHttpClient.Builder()
//                        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
//                        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
//                        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
//                        .build();
//            }
//            return okHttpClient;
//        }
//    }


    @Override
    public void onRequest(final String url, final HttpContext httpContext, final RequestListener listener, final Handler handler) {
//        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
//
//            @Override
//            protected void handleFailureMessage(Throwable arg0, String arg1) {
//                super.handleFailureMessage(arg0, arg1);
//                httpContext.setResponse(arg1);
//                LogUtils.i(HttpHelper.TAG, "response data:" + arg1);
//                LogWatcher.getInstance().putRequestInfo("[response: " + url + "]\n" + arg1);
//            }
//
//            @Override
//            protected void handleSuccessMessage(int arg0, String responseJson) {
//                super.handleSuccessMessage(arg0, responseJson);
//                httpContext.setResponse(responseJson);
//            }
//
//        };
//        SyncHttpClient httpClient = HttpClientHelper.getSyncHttpClient();

        RequestOptions options = httpContext.getOptions();
//        if (url.toLowerCase(Locale.CHINA).startsWith("https")) {
//            String cer = "";
//            String cerPassWord = "";
//            if (options != null) {
//                cer = options.getHttpsCer();
//                cerPassWord = options.getHttpsCerPassWord();
//            }
//            SSLSocketFactory socketFactory = HttpClientHelper.getSocketFactory(httpContext.getContext(), cer, cerPassWord);
//            if (socketFactory != null) {
//                httpClient.setSSLSocketFactory(socketFactory);
//            }
//        }
//
//        //设置header
//        if (null != options && null != options.getHeader() && !options.getHeader().isEmpty()) {
//            Iterator<Entry<String, String>> i = options.getHeader().entrySet().iterator();
//            while (i.hasNext()) {
//                Object o = i.next();
//                String key = o.toString();
//                String value = options.getHeader().get(key);
//                httpClient.addHeader(key, value);
//            }
//        }
//
//        if (options != null && options.getTimeout() != 0) {
//            httpClient.setTimeout(options.getTimeout());
//        } else {
//            httpClient.setTimeout(TIME_OUT);
//        }

        // String request =
        // JsonParseUtils.obj2Json(httpContext.getRequestObject());
        // String request = jsonFormatter(request);
        // LogUtils.i(HttpHelper.TAG, "request data:" + request);
        // LogWatcher.getInstance().putRequestInfo("[request: " + url + "]\n" +
        // jsonFormatter(request));

//        RequestParams params = new RequestParams();
//        Map<String, Object> requestMap = getMapForJson(httpContext.getRequest());
//        if (requestMap != null) {
//            for (String key : requestMap.keySet()) {
//                params.put(key, requestMap.get(key).toString());
//            }
//        }


//        if (options != null && options.getRequestType().toLowerCase(Locale.CHINA).equals("get")) {
//            httpClient.get(httpContext.getContext(), url + httpContext.getRequest(), responseHandler);
//        } else {
//            httpClient.post(httpContext.getContext(), url, params, responseHandler);
//        }


//        OkHttpClient okHttp = getOkHttpClient();

//        Map<String, String> header = httpContext.getOptions().getHeader();
//        if (null != header && !header.isEmpty()) {
//            MultipartBody.Builder builder = new MultipartBody.Builder();
//            builder.setType(MultipartBody.FORM);
//
//            for (String key : header.keySet()) {
//                String value = header.get(key);
////                "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""
////                RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
////                RequestBody.create(null, params.get(key))
//
//                builder.addPart(Headers.of("Content-Disposition", key), RequestBody.create(null, v));
//            }
//
//            builder.build();
//        }


        try {
            String getUrl = url + (null == httpContext.getRequest() ? "" : httpContext.getRequest());
            Request request;
            if (!TextUtils.isEmpty(options.getRequestType()) && options.getRequestType().toLowerCase(Locale.CHINA).equals("get")) {
                LogUtils.i("HttpHelperDebug", "request url is[get]:" + getUrl);
                request = new Request.Builder().url(getUrl).get().build();
            } else {
                LogUtils.i("HttpHelperDebug", "request url is[post]:" + url);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), httpContext.getRequest());

                FormBody formBody = new FormBody.Builder()
                        .add("json", httpContext.getRequest())
                        .build();
                request = new Request.Builder().url(url).post(formBody).build();
                LogUtils.i("HttpHelperDebug", "request is:" + request.toString());
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
//                httpContext.code = response.code();
//                httpContext.message = response.message();

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
        httpContext.responseTimeStamp = System.currentTimeMillis();

        //
        // String response = null;
        // try {
        // LogWatcher.getInstance().putRequestInfo("1111");
        // response = post(url, httpContext.getRequest());
        // LogWatcher.getInstance().putRequestInfo("2222");
        // } catch (IOException e) {
        // LogWatcher.getInstance().putRequestInfo("3333" + e.getMessage());
        // e.printStackTrace();
        // } finally {
        // httpContext.setResponse(response);
        // LogUtils.i(HttpHelper.TAG, "response data:" + response);
        // LogWatcher.getInstance().putRequestInfo("[response: " + url + "]\n" +
        // response);
        // }
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                // valueMap.put(key, getMapForJson(value.toString()));
                if (jsonObject.optJSONObject(key) != null) {
                    valueMap.put(key, getMapForJson(value.toString()));
                } else {
                    valueMap.put(key, value);
                }
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String jsonFormatter(String uglyJSONString) {
        String prettyJsonStr2 = uglyJSONString;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(uglyJSONString);
            prettyJsonStr2 = gson.toJson(je);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prettyJsonStr2;
    }


    @Override
    public void onResponse(String url, HttpContext httpContext, RequestListener listener, Handler handler) {

    }

}
