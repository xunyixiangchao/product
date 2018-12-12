package com.leyou.library.le_library.comm.grand.network;

import android.content.Context;

import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.net.http.BaseHttpHelper;
import com.ichsy.libs.core.net.http.HttpQueueTask;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.leyou.library.le_library.comm.grand.network.filter.SimpleDataSenderFilter;
import com.leyou.library.le_library.comm.grand.network.filter.XDataSenderFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 达观数据分析
 * <p>
 * Created by ss on 2018/7/10.
 */
public class SimpleHttpHelper extends BaseHttpHelper {

    public SimpleHttpHelper(Context context) {
        super(context);
    }

    public SimpleHttpHelper(Context context, RequestOptions options) {
        super(context, options);

        if (ObjectUtils.isNotNull(options) && ObjectUtils.isNull(options.getRequestType())) {
            //如果有option但是没有设置RequestType，默认设置为get请求
            options.setRequestType(RequestOptions.Mothed.GET);
        }

        setSuccessCode(1);
    }

    @Override
    public void doPost(String url, Object params, Class<?> responseClass, RequestListener listener) {
        task = new HttpQueueTask(context, getTrueUrl(url), params, responseClass, listener);

        //设置请求队列
        List<RequestHandler> queue = new ArrayList<>();

        queue.add(new XDataSenderFilter());
        executeTask(queue);
    }

    @Override
    public void post(String url, Object params, Class<?> responseClass, RequestListener listener) {
        task = new HttpQueueTask(context, getTrueUrl(url), params, responseClass, listener);

        //设置请求队列
        List<RequestHandler> queue = new ArrayList<>();

        queue.add(new SimpleDataSenderFilter());
        executeTask(queue);
    }

}