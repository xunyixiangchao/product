package com.leyou.library.le_library.comm.network.filter;

import android.content.Context;
import android.os.Handler;

import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;
import com.ichsy.libs.core.comm.utils.ToastUtils;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.handler.RequestHandler;
import com.leyou.library.le_library.comm.helper.CrashHelper;
import com.leyou.library.le_library.config.LeConstant;

/**
 * 乐友相关的action事件蓝阶层
 * Created by liuyuhang on 16/6/14.
 */
public class LeActionFilter implements RequestHandler {
    //code：900001 token失效

    @Override
    public void onRequest(String url, HttpContext httpContext, RequestListener listener, Handler handler) {

    }

    @Override
    public void onResponse(String url, final HttpContext httpContext, RequestListener listener, Handler handler) {


        if (LeConstant.CODE.CODE_OK != httpContext.code) {
            if (httpContext.getOptions().isToastDisplay()) {
                Runnable toastShowRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Context context = httpContext.getContext().get();

                        if (null != context) {
//                            if (context instanceof BaseFrameActivity) {
//                            if (((BaseFrameActivity) context).isActivityFinish()) {
//                                return;
//                            }
//                            }
                            ToastUtils.showMessage(context.getApplicationContext(), httpContext.message);
                        }
                    }
                };

                ThreadPoolUtil.runOnMainThread(toastShowRunnable);
            }

            /**
             * 如果token失效，弹出登陆窗口
             */
            if (LeConstant.CODE.CODE_TOKEN_FAILED == httpContext.code) {
                String loginActivityClassName = "com.capelabs.leyou.ui.activity.user.LoginActivity";

                String loginActivityUrl = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE, loginActivityClassName);
                UrlParser.getInstance().parser(httpContext.getContext().get(), loginActivityUrl);

                String logoutUrl = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_ACTION, "logout");
                UrlParser.getInstance().parser(httpContext.getContext().get(), logoutUrl);
            }
        }

        CrashHelper.Companion.instance().putRequest(new CrashHelper.CrashRequest(url, httpContext.getRequest(), httpContext.getResponse(), httpContext.getRequestTime()));

//        if (response.header.res_code != 0 && httpContext.getOptions().isToastDisplay()) {
//            ToastUtils.showMessage(httpContext.getContext().getApplicationContext(), response.header.message);
//        }

//        try {
//            ClassReflexUtils.testReflect(response);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
//            e.printStackTrace();
//        }
    }
}
