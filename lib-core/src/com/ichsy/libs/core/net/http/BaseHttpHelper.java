package com.ichsy.libs.core.net.http;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ichsy.libs.core.net.http.handler.DataSenderFilter;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.ichsy.libs.core.net.http.handler.TransformFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求的base类
 * Created by liuyuhang on 16/7/29.
 */
public abstract class BaseHttpHelper implements HttpRequestInterface {

    protected HttpQueueTask task;
    protected RequestOptions options;

    private String mBaseUrl;
    protected Context context;
    private int successCode;

    public BaseHttpHelper(Context context) {
        this.context = context;

        options = new RequestOptions();
        options.setRequestType(RequestOptions.Mothed.GET);
    }

    public BaseHttpHelper(Context context, RequestOptions options) {
        this.context = context;
        this.options = options;

    }

    /**
     * 设置BaseUrl，拼接在url前面
     *
     * @param url
     */
    public void setBaseUrl(String url) {
        mBaseUrl = url;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public void doOldPost(String url, Object params, Class<?> responseClass, RequestListener listener) {
        cancel(getTrueUrl(url));
        task = new HttpQueueTask(context, getTrueUrl(url), params, responseClass, listener);

        //设置请求队列
        List<RequestHandler> queue = new ArrayList<>();
        queue.add(new TransformFilter());
        queue.add(new DataSenderFilter());
        executeTask(queue);
    }

    protected void executeTask(List<RequestHandler> queue) {
        task.setRequestOptions(options);
        task.setRequestQueue(queue);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 取消之前的请求
     */
    public void cancel(String url) {
        if (null != task && task.getStatus() != AsyncTask.Status.FINISHED) {
            if (url.equals(task.url)) {
                task.cancel(true);
            }
        }
    }

    protected String getTrueUrl(String url) {
        if (TextUtils.isEmpty(mBaseUrl)) {
            return url;
        } else {
            return mBaseUrl + url;
        }
    }

}
