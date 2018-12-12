package com.ichsy.libs.core.net.http;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.logwatch.LogWatcher;
import com.ichsy.libs.core.comm.utils.GsonHelper;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.frame.BaseFrameActivity;
import com.ichsy.libs.core.net.http.handler.DataSenderFilter;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.ichsy.libs.core.net.http.handler.TransformFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行http队列请求的异步task
 * Created by liuyuhang on 16/5/23.
 */
public class HttpQueueTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = "HttpHelper_AsyncTask";

    public String url;

    private List<RequestHandler> taskQueue; //任务队列
    private RequestListener listener;
    private HttpContext requestContext; //请求的上下文
    private RequestOptions requestOptions; //请求的附带参数

    public HttpQueueTask(Context context, String url, Object params, Class<?> responseClass, RequestListener listener) {
        this.url = url;
        this.listener = listener;

        requestContext = new HttpContext();
        requestContext.setContext(new WeakReference<>(context));
        requestContext.setRequestObject(params);
        requestContext.setResponseClass(responseClass);
    }

    /**
     * 设置请求的options
     *
     * @param options
     */
    public void setRequestOptions(RequestOptions options) {
        this.requestOptions = options;
    }

    /**
     * 设置请求的任务队列
     *
     * @param taskQueue
     */
    public void setRequestQueue(List<RequestHandler> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (null == taskQueue) {
            taskQueue = new ArrayList<>();
            taskQueue.add(new TransformFilter());
            taskQueue.add(new DataSenderFilter());
        }

        if (null == requestOptions) {
            requestOptions = new RequestOptions();
        }
        requestContext.setOptions(requestOptions);

        if (null != listener) {
            listener.onHttpRequestBegin(url);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        if (TextUtils.isEmpty(url)) return "";

        long requestTime = System.currentTimeMillis();
        for (int i = 0; i < taskQueue.size(); i++) {
            if (isCancelled()) {
                break;
            }
            RequestHandler requestHandler = taskQueue.get(i);
//            LogUtils.i(TAG, "HttpUtil: request executed:" + requestHandler.getClass().getSimpleName());
            requestHandler.onRequest(url, requestContext, listener, null);
        }
        for (int i = taskQueue.size() - 1; i >= 0; i--) {
            if (isCancelled()) {
                break;
            }
            RequestHandler responseHandler = taskQueue.get(i);
//            LogUtils.i(TAG, "HttpUtil: response executed:" + responseHandler.getClass().getSimpleName());
            responseHandler.onResponse(url, requestContext, listener, null);
        }
        requestContext.setRequestTime(System.currentTimeMillis() - requestTime);
        return null;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
//        if (null == requestContext.getContext()) {
//            LogUtils.i(HttpHelper.TAG, "response was cancelled because the context is null\n response data:[cost:" + requestContext.getRequestTime() + "ms] " + requestContext.getResponse());
//        }

        BaseFrameActivity requestActivity = null;
        if (ObjectUtils.isNotNull(requestContext.getContext()) && requestContext.getContext().get() instanceof BaseFrameActivity) {
            requestActivity = (BaseFrameActivity) requestContext.getContext().get();
        }

        if (TextUtils.isEmpty(url)) {
            return;
        } else if (requestActivity != null && requestOptions.isCancelIfActivityFinish() && requestActivity.isActivityFinish()) {
            LogUtils.i(HttpHelper.TAG, "request cancel: because activity is finished");
            return;
        }

        if (null != listener) {
            if (requestContext.isRequestSuccess) {
                listener.onHttpRequestSuccess(url, requestContext);
            } else {
                listener.onHttpRequestFailed(url, requestContext);
            }
            listener.onHttpRequestComplete(url, requestContext);
        }
        LogUtils.i(HttpHelper.TAG, "request url:" + url);
        LogUtils.i(HttpHelper.TAG, "request data:" + requestContext.getRequest());
        LogUtils.i(HttpHelper.TAG, "response data:[cost:" + requestContext.getRequestTime() + "ms] " + requestContext.getResponse());
        LogWatcher.getInstance().putRequestInfo("[request: " + url + "]\n\n" + GsonHelper.formatter(requestContext.getRequest()));
        LogWatcher.getInstance().putRequestInfo(
                "[response: " + url + "]\n[cost:" + requestContext.getRequestTime() + "ms ]\n\n" + GsonHelper.formatter(requestContext.getResponse()));

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        /**
         * 执行取消的事件
         */
        if (null != listener) {
            listener.onHttpRequestCancel(url, requestContext);
        }

        LogUtils.i(HttpHelper.TAG, "request url:" + url);
        LogUtils.i(HttpHelper.TAG, "request data:" + requestContext.getRequest());
        LogUtils.i(HttpHelper.TAG, "response data (Canceled!) :[cost:" + requestContext.getRequestTime() + "ms ]" + requestContext.getResponse());

        LogWatcher.getInstance().putRequestInfo("[request: " + url + "]\n\n" + GsonHelper.formatter(requestContext.getRequest()));
        LogWatcher.getInstance().putRequestInfo(
                "[response (Canceled!) : " + url + "]\n[cost:" + requestContext.getRequestTime() + "ms ]\n\n" + GsonHelper.formatter(requestContext.getResponse()));
    }

}
