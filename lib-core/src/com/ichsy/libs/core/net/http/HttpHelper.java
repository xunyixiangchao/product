package com.ichsy.libs.core.net.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.logwatch.LogWatcher;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;
import com.ichsy.libs.core.net.http.handler.DataSenderFilter;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.ichsy.libs.core.net.http.handler.TransformFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Http请求的工具类
 *
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net
 * @File HttpUtil.java
 */
public class HttpHelper implements HttpRequestInterface {
    public static final String TAG = "HttpHelper";

    private List<RequestHandler> mRequestTaskList;
    private Context mContext;

    private RequestOptions mOptions;

    private String mCanceledUrl;

    /***/
    private static RequestHandler testDataFilter;

    public HttpHelper(Context context) {
        mContext = context;
        mRequestTaskList = new ArrayList<>();
        onTaskListFiliter(mRequestTaskList);
        mRequestTaskList.add(new TransformFilter());
        mRequestTaskList.add(new DataSenderFilter());
    }

    // public void doPost(String url, RequestParams params, Object target,
    // Class<?> responseClass, RequestListener listener) {
    // doPost(url, params, target, responseClass, mRequestTaskList, listener);
    // }

    @Override
    public void doPost(String url, Object params, Class<?> responseClass, RequestListener listener) {
        doPost(url, params, responseClass, mRequestTaskList, listener);
    }

    /**
     * @param url
     * @param params
     * @param responseClass
     * @param taskList
     * @param listener
     */
    public void doPost(final String url, Object params, Class<?> responseClass, final List<RequestHandler> taskList, final RequestListener listener) {
        LogUtils.i(HttpHelper.TAG, "request url:" + url);
        LogWatcher.getInstance().putRequestInfo("[request: " + url + "]");

        if (url.equals(mCanceledUrl)) {
            mCanceledUrl = "";
        }
        final Handler handler = new Handler(Looper.getMainLooper());

        final HttpContext httpContext = getHttpContext();
        httpContext.setContext(new WeakReference<>(mContext));
        httpContext.setRequestObject(params);
        httpContext.setOptions(getOptions());
        // httpContext.setTag(requestTarget);
        // if (mTimeOut != 0) {
        // httpContext.setTimeOut(mTimeOut);
        // }
        httpContext.setResponseClass(responseClass);
        if (listener != null) {
            listener.onHttpRequestBegin(url);
        }

        Runnable task = new Runnable() {

            @Override
            public void run() {
                long requestTime = System.currentTimeMillis();
//				try {
                for (int i = 0; i < taskList.size(); i++) {
                    RequestHandler requstHandler = taskList.get(i);
                    LogUtils.i(TAG, "HttpUtil: request executed:" + requstHandler.getClass().getSimpleName());
                    requstHandler.onRequest(url, httpContext, listener, handler);
                }
                for (int i = taskList.size() - 1; i >= 0; i--) {
                    RequestHandler requstHandler = taskList.get(i);
                    LogUtils.i(TAG, "HttpUtil: response executed:" + requstHandler.getClass().getSimpleName());
                    requstHandler.onResponse(url, httpContext, listener, handler);
                }
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
                httpContext.setRequestTime(System.currentTimeMillis() - requestTime);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mCanceledUrl) && mCanceledUrl.equals(url)) {// 本次请求已经被取消
                            if (listener != null) {
                                listener.onHttpRequestCancel(url, httpContext);
                            }
                            LogUtils.i(HttpHelper.TAG, "request data:" + httpContext.getRequest());
                            LogUtils.i(HttpHelper.TAG, "response data (Canceled!) :[cost:" + httpContext.getRequestTime() + "ms ]" + httpContext.getResponse());

                            LogWatcher.getInstance().putRequestInfo("[request: " + url + "]\n\n" + GsonHelper.formatter(httpContext.getRequest()));
                            LogWatcher.getInstance().putRequestInfo(
                                    "[response (Canceled!) : " + url + "]\n[cost:" + httpContext.getRequestTime() + "ms ]\n\n" + GsonHelper.formatter(httpContext.getResponse()));
                        } else {
                            if (listener != null) {
                                if (httpContext.getResponseObject() != null) {
                                    listener.onHttpRequestSuccess(url, httpContext);
                                } else {
                                    listener.onHttpRequestFailed(url, httpContext);
                                }
                                listener.onHttpRequestComplete(url, httpContext);
                            }
                            LogUtils.i(HttpHelper.TAG, "request data:" + httpContext.getRequest());
                            LogUtils.i(HttpHelper.TAG, "response data:[cost:" + httpContext.getRequestTime() + "ms ]" + httpContext.getResponse());
                            LogWatcher.getInstance().putRequestInfo("[request: " + url + "]\n\n" + GsonHelper.formatter(httpContext.getRequest()));
                            LogWatcher.getInstance().putRequestInfo(
                                    "[response: " + url + "]\n[cost:" + httpContext.getRequestTime() + "ms ]\n\n" + GsonHelper.formatter(httpContext.getResponse()));
                        }

                    }
                });
            }
//			}
        };
        ThreadPoolUtil.getInstance().fetchData(task);
    }

    @Override
    @Deprecated
    /**
     * 不要使用本方法！
     */
    public void post(String url, Object params, Class<?> responseClass, RequestListener listener) {

    }

    /**
     * 取消某个http请求
     *
     * @param url
     */
    public void cancel(String url) {
        mCanceledUrl = url;
    }

    /**
     * 可以在http请求的队列中添加任意新的队列任务
     *
     * @param mRequestTaskList Modifier： Modified Date： Modify：
     */
    public void onTaskListFiliter(List<RequestHandler> mRequestTaskList) {
        if (testDataFilter != null)
            mRequestTaskList.add(testDataFilter);
    }

    public HttpContext getHttpContext() {
        return new HttpContext();
    }

    public Context getContext() {
        return mContext;
    }

    public static void enableTestData(RequestHandler testDataFilter) {
        HttpHelper.testDataFilter = testDataFilter;
    }

    public RequestOptions getOptions() {
        return mOptions;
    }

    public void setOptions(RequestOptions options) {
        this.mOptions = options;
    }

}
