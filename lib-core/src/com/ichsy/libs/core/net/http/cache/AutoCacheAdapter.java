package com.ichsy.libs.core.net.http.cache;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.config.CoreConfig;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.HttpRequestInterface;
import com.ichsy.libs.core.net.http.RequestListener;

/**
 * 如果获取网络数据失败，会展示缓存数据
 * Created by liuyuhang on 16/6/30.
 */
public class AutoCacheAdapter extends RequestCacheAdapter {

    public AutoCacheAdapter(Context context, HttpRequestInterface httpHelper) {
        super(context, httpHelper);
    }

    @Override
    public String getCacheKey(String url, Object params) {
        return url;
    }

    @Override
    public void doPost(String url, final Object params, Class<?> responseClass, final RequestListener listener) {
//        super.doPost(url, params, responseClass, listener);
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
                    if (httpContext.isRequestSuccess) {
                        listener.onHttpRequestComplete(url, httpContext);
                    } else {
                        String cache = getCache(getCacheKey(url, params));
                        final boolean isHaveCache = !TextUtils.isEmpty(cache);
                        if (isHaveCache) {
                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request user cache");
//                            final HttpContext httpContext = new HttpContext();
//                            httpContext.setContext(context);
//                            httpContext.setRequestObject(params);
                            // httpContext.setResponseClass(responseClass);
                            // httpContext.setCache(true);

                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: cache is: " + cache);
                            httpContext.setResponse(cache);
                            Object cacheObject = null;
                            try {
                                cacheObject = GsonHelper.build().fromJson(cache, httpContext.getResponseClass());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            httpContext.setResponseObject(cacheObject);

                            if (cacheObject != null) {
                                httpContext.code = 0;
                                listener.onHttpRequestSuccess(url, httpContext);
                            }
                            listener.onHttpRequestComplete(url, httpContext);

                            // new Handler(Looper.getMainLooper()).post(new Runnable() {
                            // @Override
                            // public void run() {
                            // // 如果response已经被处理，直接返回，然后再做网络请求
                            // // return;
                            // if (listener != null) {
                            // listener.onHttpRequestSuccess(url, httpContext);
                            // listener.onHttpRequestComplete(url, httpContext);
                            // }
                            // }
                            // });
                            // mHttpHelper.doPost(url, params, responseClass, listener);
                        }
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

    @Override
    public void post(String url, final Object params, Class<?> responseClass, final RequestListener listener) {
//        super.doPost(url, params, responseClass, listener);
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
                    if (httpContext.isRequestSuccess) {
                        listener.onHttpRequestComplete(url, httpContext);
                    } else {
                        String cache = getCache(getCacheKey(url, params));
                        final boolean isHaveCache = !TextUtils.isEmpty(cache);
                        if (isHaveCache) {
                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: request user cache");

                            LogUtils.i(CoreConfig.LOG_TAG, "HttpUtil: cache is: " + cache);
                            httpContext.setResponse(cache);
                            Object cacheObject = null;
                            try {
                                cacheObject = GsonHelper.build().fromJson(cache, httpContext.getResponseClass());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            httpContext.setResponseObject(cacheObject);

                            if (cacheObject != null) {
                                httpContext.code = 0;
                                listener.onHttpRequestSuccess(url, httpContext);
                            }
                        }
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
