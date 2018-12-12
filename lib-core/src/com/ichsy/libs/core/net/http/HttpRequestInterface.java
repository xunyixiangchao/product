package com.ichsy.libs.core.net.http;


public interface HttpRequestInterface {

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     * @param listener Modifier： Modified Date： Modify：
     */
    void doPost(String url, Object params, Class<?> responseClass, RequestListener listener);

    void post(String url, Object params, Class<?> responseClass, RequestListener listener);
}
