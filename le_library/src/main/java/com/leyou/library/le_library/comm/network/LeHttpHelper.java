package com.leyou.library.le_library.comm.network;

import android.content.Context;

import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.net.http.BaseHttpHelper;
import com.ichsy.libs.core.net.http.HttpQueueTask;
import com.ichsy.libs.core.net.http.OkHttpClientHelper;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.handler.DataSenderFilter;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.ichsy.libs.core.net.http.handler.TransformFilter;
import com.leyou.library.le_library.comm.network.filter.LeActionFilter;
import com.leyou.library.le_library.comm.network.filter.LeDataEncodeFilter;
import com.leyou.library.le_library.comm.network.filter.LeDataTransformFilter;
import com.leyou.library.le_library.comm.network.filter.LeRequestPackingFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 乐友的http请求
 * Created by liuyuhang on 16/5/23.
 */
public class LeHttpHelper extends BaseHttpHelper {
    private static RequestHandler testDataFilter;


    public LeHttpHelper(Context context) {
        super(context);

    }

    public LeHttpHelper(Context context, RequestOptions options) {
        super(context, options);

        if (ObjectUtils.isNotNull(options) && ObjectUtils.isNull(options.getRequestType())) {
            //如果有option但是没有设置RequestType，默认设置为get请求
            options.setRequestType(RequestOptions.Mothed.GET);
        }

        setSuccessCode(1);
    }

    public static void setTestDataFiliter(RequestHandler testDataFiliter) {
        LeHttpHelper.testDataFilter = testDataFiliter;
    }


    /**
     * 旧的请求方法，response中需要定义一个body，使用post() 代替
     *
     * @param url
     * @param params
     * @param responseClass
     * @param listener      Modifier： Modified Date： Modify：
     */
    @Deprecated
    @Override
    public void doPost(String url, Object params, Class<?> responseClass, final RequestListener listener) {
//        cancel(url);
        task = new HttpQueueTask(context, getTrueUrl(url), params, responseClass, listener);

        //设置请求队列
        List<RequestHandler> queue = new ArrayList<>();

//        if (LeAppBuildController.getBuildMode().equals(LeAppBuildController.BUILD_MODE_ALPHA)) {
//            queue.add(new TestDataFilter());
//        }

        if (null != testDataFilter) {
            queue.add(testDataFilter);
        }

//        queue.add(new LeBodyEncodeFilter());
        queue.add(new LeActionFilter());
        queue.add(new LeRequestPackingFilter());
        queue.add(new TransformFilter());
        queue.add(new LeDataEncodeFilter());
        queue.add(new DataSenderFilter());
        executeTask(queue);
    }

    /**
     * 新的post方式，不需要再response中写body对象
     *
     * @param url
     * @param params
     * @param responseClass
     * @param listener
     */
    @Override
    public void post(String url, Object params, Class<?> responseClass, final RequestListener listener) {
        task = new HttpQueueTask(context, getTrueUrl(url), params, responseClass, listener);

        //设置请求队列
        List<RequestHandler> queue = new ArrayList<>();

//        if (LeAppBuildController.getBuildMode().equals(LeAppBuildController.BUILD_MODE_ALPHA)) {
//        queue.add(new TestDataFilter());
//        }

        if (null != testDataFilter) {
            queue.add(testDataFilter);
        }

//        queue.add(new LeBodyEncodeFilter());
        queue.add(new LeActionFilter());
//        queue.add(new LeRequestPackingFilter());
        queue.add(new LeDataTransformFilter());
        queue.add(new LeDataEncodeFilter());
        queue.add(new DataSenderFilter());
        executeTask(queue);
    }

    /**
     * 上传文件
     *
     * @param url      请求地址
     * @param files    文件列表
     * @param params   参数列表
     * @param callback 回调
     */
    public static void update(String url, HashMap<String, File> files, HashMap<String, String> params, Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //拼装header files
        if (null != files && !files.isEmpty()) {
            builder.setType(MultipartBody.FORM);

            for (String key : files.keySet()) {
                File value = files.get(key);
//                "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""
//                RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                RequestBody.create(null, params.get(key))

                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"; filename=\"" + value + "\"")
                        , RequestBody.create(MediaType.parse("application/octet-stream"), value));
            }

        }

        for (String key : params.keySet()) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                    RequestBody.create(null, params.get(key)));
        }

        RequestBody requestBody = builder.build();

        OkHttpClient okHttpClient = OkHttpClientHelper.INSTANCE.getOkHttpClient();

        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
