package com.ichsy.libs.core.net.http;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * @param
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net
 * @File HttpContext.java
 */
public class HttpContext {
    //    private final Object lock1 = new Object();
//    private final Object lock2 = new Object();
    private long requestTime;
    private WeakReference<Context> context;
    /**
     * 请求的数据string
     */
    private String request;
    private Object requestObject;
    private Class<?> responseClass;
    public int httpCode;
    /**
     * 返回的数据string
     */
    public int code;
    public String message;
    public boolean isRequestSuccess = false;//标志本次请求是否成功，因为直接判断返回的responseObject有可能不准确，所以单独用一个新的变量去记录
    //    public String message;
    private String response;
    private Object responseObject;

//	private boolean isCache;
//	private Object tag;

    private RequestOptions options;

    private HashMap<String, Object> params = new HashMap<>();

    public long responseTimeStamp = 0;

    /**
     * 网络超时时间
     */
//	private int timeOut;

    /**
     * 每次网络请求的唯一标示 用于缓存key
     */
    // private String uuid;
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(Object requestObject) {
        this.requestObject = requestObject;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public WeakReference<Context> getContext() {
        return context;
    }

    public void setContext(WeakReference<Context> context) {
        this.context = context;
    }

    public Class<?> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<?> responseClass) {
        this.responseClass = responseClass;
    }


    public <T> T getResponseObject() {
        return (T) responseObject;
    }

    public RequestOptions getOptions() {
        return options;
    }

    public void setOptions(RequestOptions options) {
        this.options = options;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }

    public HashMap<String, Object> getParams() {
        if(null == params) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
