package com.ichsy.libs.core.comm.update;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 检测更新的操作类，处理检测更新中用户的操作
 * Created by liuyuhang on 16/4/29.
 */
public abstract class UpdateOperator {

    /**
     * 执行检测更新的网络请求，返回网络请求之后的数据（注意：这个函数在子线程中）
     *
     * @return
     */
    public abstract String onUpdateRequest();


    /**
     * 解析服务端返回的网络请求数据，返回检测更新的实体类
     *
     * @param updateJson
     * @return
     */
    public abstract UpdateVo parserUpdateJson(String updateJson);

    public String onUpdateRequestHeader() {
        return null;
    }

    public String onUpdateRequestParams() {
        return null;
    }

    public String doRequest(String url, String header, String body) {
        String result = null;

        Response response = null;
        try {
            String finalUrl;
            if (TextUtils.isEmpty(body)) {
                finalUrl = url;
            } else {
                finalUrl = url + body;
            }
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(4, TimeUnit.SECONDS)
                    .writeTimeout(4, TimeUnit.SECONDS)
                    .readTimeout(4, TimeUnit.SECONDS);
//                    .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

            Request request = new Request.Builder().url(finalUrl).get().build();
//            OkHttpClient okHttpClient = new OkHttpClient();
            OkHttpClient okHttpClient = builder.build();

            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
                result = response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                response.close();
            }
        }
        return result;
    }
}
