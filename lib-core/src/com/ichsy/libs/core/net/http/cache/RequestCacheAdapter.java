package com.ichsy.libs.core.net.http.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.config.CoreConfig;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.HttpRequestInterface;
import com.ichsy.libs.core.net.http.RequestListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 带缓存的HTTP请求
 *
 * @author liuyuhang
 */
public abstract class RequestCacheAdapter implements HttpRequestInterface {
    private static final String SP_FILE_NAME = "_cache_files";

    protected HttpRequestInterface mHttpHelper;
    private Context context;
    private Gson mGson;

    public RequestCacheAdapter(Context context, HttpRequestInterface httpHelper) {
        this.context = context;
        mHttpHelper = httpHelper;
        mGson = GsonHelper.build();
    }

    /**
     * 保存cache的key，根据这个key，一个请求的url也可以缓存不同的数据，例如:"url + "?productcode" + code;"
     *
     * @param url
     * @param params
     * @return
     */
    public abstract String getCacheKey(String url, Object params);

    /**
     * 保存cache
     *
     * @param cacheKey
     * @param cache
     */
    protected void saveCache(String cacheKey, Object cache) {
        if (null != context && !TextUtils.isEmpty(cacheKey)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Activity.MODE_PRIVATE);
            RequestCacheObject cacheObject = new RequestCacheObject(cacheKey, mGson.toJson(cache));
            sharedPreferences.edit().putString(cacheKey, mGson.toJson(cacheObject)).apply();
        }
    }

    /**
     * 在从file重获取缓存
     *
     * @param cacheKey
     * @return
     */
    protected String getCache(String cacheKey) {
        if (context != null && !TextUtils.isEmpty(cacheKey)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Activity.MODE_PRIVATE);
            String cache = sharedPreferences.getString(cacheKey, null);
            if (TextUtils.isEmpty(cache)) {
                return cache;
            } else {
                RequestCacheObject cacheObject = onCacheRead(context, mGson.fromJson(cache, RequestCacheObject.class));
                return cacheObject == null ? null : cacheObject.cache;
            }
        }
        return null;
    }

    public static boolean isDataFromNet(HashMap<String, Object> params) {
        boolean isSeverData;
        isSeverData = null == params || params.get("is_cache") == null || params.get("is_cache").equals("2");
        return isSeverData;

//        val cacheData = httpContext.params?.get("is_cache") == null || httpContext.params?.get("is_cache").equals("2")
    }

//    /**
//     * 清除缓存
//     *
//     * @param cacheKey
//     */
//    public static void clearCache(Context context, String cacheKey) {
//        if (!TextUtils.isEmpty(cacheKey)) {
//            SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Activity.MODE_PRIVATE);
////            RequestCacheObject cacheObject = new RequestCacheObject(cacheKey, mGson.toJson(cache));
//            sharedPreferences.edit().putString(cacheKey, "").apply();
//        }
//    }

//    /**
//     * 存储原始数据
//     *
//     * @param cacheKey
//     * @param cache
//     */
//    public void saveCacheObject(String cacheKey, Object cache) {
//
//    }

//    protected Object getCacheObject() {
//
//        return null;
//    }

    /**
     * 清除缓存
     *
     * @param cache
     * @return
     */
    public static RequestCacheObject deleteCache(Context context, RequestCacheObject cache) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Activity.MODE_PRIVATE);
        sharedPreferences.edit().remove(cache.cacheKey).apply();
        return null;
    }

    /**
     * 获取缓存之后，可以处理从数据库换取到的缓存
     *
     * @param cacheObject
     * @return
     */
    protected RequestCacheObject onCacheRead(Context context, RequestCacheObject cacheObject) {
        return cacheObject;
    }


    /**
     * 先展示缓存，等到网络数据回来，会显示网络数据，对应HttpHelper的doPost方法
     */
    @Override
    public void doPost(final String url, final Object params, Class<?> responseClass, final RequestListener listener) {
        String cache = checkCache(url, params, responseClass, listener);
        final boolean isHaveCache = !TextUtils.isEmpty(cache);

        if (verfiyHttpRequest(cache)) {// 只有验证通过才会进行http请求
            mHttpHelper.doPost(url, params, responseClass, new RequestListener() {

                @Override
                public void onHttpRequestSuccess(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestSuccess(url, httpContext);
                    }

                    saveCache(getCacheKey(url, params), httpContext.getResponseObject());
                }

                @Override
                public void onHttpRequestFailed(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestFailed(url, httpContext);
                    }
                }

                @Override
                public void onHttpRequestComplete(String url, HttpContext httpContext) {
                    if (listener != null) {
                        LogUtils.i(CoreConfig.LOG_TAG, "HttpCache" + url + ": isHaveCache：" + isHaveCache);
                        LogUtils.i(CoreConfig.LOG_TAG, "HttpCache" + url + ": httpContext.isRequestSuccess：" + httpContext.isRequestSuccess);

                        if (isHaveCache && !httpContext.isRequestSuccess) {
                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request user cache and not request, because current request failed");
                        } else {
                            httpContext.getParams().put("is_cache", "2");
                            listener.onHttpRequestComplete(url, httpContext);
                        }
                    }
                }

                @Override
                public void onHttpRequestCancel(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestCancel(url, httpContext);
                    }
                }

                @Override
                public void onHttpRequestBegin(String url) {
                    if (listener != null) {
                        listener.onHttpRequestBegin(url);
                    }
                }
            });
        }
    }

    /**
     * 先展示缓存，等到网络数据回来，会显示网络数据，对应HttpHelper的post方法
     *
     * @param url
     * @param params
     * @param responseClass
     * @param listener
     */
    @Override
    public void post(final String url, final Object params, Class<?> responseClass, final RequestListener listener) {
        String cache = checkCache(url, params, responseClass, listener);
        final boolean isHaveCache = !TextUtils.isEmpty(cache);

        if (verfiyHttpRequest(cache)) {// 只有验证通过才会进行http请求
            mHttpHelper.post(url, params, responseClass, new RequestListener() {

                @Override
                public void onHttpRequestSuccess(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestSuccess(url, httpContext);
                    }

                    saveCache(getCacheKey(url, params), httpContext.getResponseObject());
                }

                @Override
                public void onHttpRequestFailed(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestFailed(url, httpContext);
                    }
                }

                @Override
                public void onHttpRequestComplete(String url, HttpContext httpContext) {
                    if (listener != null) {
                        LogUtils.i(CoreConfig.LOG_TAG, "HttpCache" + url + ": isHaveCache：" + isHaveCache);
                        LogUtils.i(CoreConfig.LOG_TAG, "HttpCache" + url + ": httpContext.isRequestSuccess：" + httpContext.isRequestSuccess);

                        if (isHaveCache && !httpContext.isRequestSuccess) {
                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request user cache and not request, because current request failed");
                        } else {
                            httpContext.getParams().put("is_cache", "2");
                            listener.onHttpRequestComplete(url, httpContext);
                        }
                    }
                }

                @Override
                public void onHttpRequestCancel(String url, HttpContext httpContext) {
                    if (listener != null) {
                        listener.onHttpRequestCancel(url, httpContext);
                    }
                }

                @Override
                public void onHttpRequestBegin(String url) {
                    if (listener != null) {
                        listener.onHttpRequestBegin(url);
                    }
                }
            });
        }
    }

    /**
     * 检测并返回cache内容
     *
     * @param url
     * @param params
     * @param responseClass
     * @param listener
     */
    private String checkCache(String url, Object params, Class<?> responseClass, RequestListener listener) {
        String cache = getCache(getCacheKey(url, params));

        final boolean isHaveCache = !TextUtils.isEmpty(cache);

        if (isHaveCache) {
            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request user cache");
            final HttpContext httpContext = new HttpContext();
            httpContext.setContext(new WeakReference<>(context));
            httpContext.setRequestObject(params);
            // httpContext.setResponseClass(responseClass);
            // httpContext.setCache(true);

            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: cache is: " + cache);
            httpContext.setResponse(cache);
            Object cacheObject = null;
            try {
                cacheObject = GsonHelper.build().fromJson(cache, responseClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpContext.setResponseObject(cacheObject);

            if (listener != null) {
                if (cacheObject != null) {
                    httpContext.getParams().put("is_cache", "1");
                    listener.onHttpRequestSuccess(url, httpContext);
                }
                listener.onHttpRequestComplete(url, httpContext);
            }
            return cache;
        } else {
            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request not user cache");
            return null;
        }
    }

    /**
     * 校验是否要进行http请求
     */
    public boolean verfiyHttpRequest(String cache) {
        return true;
    }

    public static class RequestCacheObject {
        public String cache;// 缓存的数据
        public String cacheKey;
        public long timeStamp;// 缓存时间

        public RequestCacheObject(String cacheKey, String cache) {
            this.timeStamp = System.currentTimeMillis();
            this.cacheKey = cacheKey;
            this.cache = cache;
        }

    }
}
