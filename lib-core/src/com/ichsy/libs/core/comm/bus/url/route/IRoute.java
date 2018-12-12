package com.ichsy.libs.core.comm.bus.url.route;

import android.content.Context;

/**
 * route中转接口，用于介入路由执行额外逻辑
 * Created by liuyuhang on 2018/1/9.
 */

public interface IRoute {

    /**
     * 路由中转
     *
     * @param context
     * @param url
     * @return 返回true表示已经消费，不执行之后逻辑
     */
    boolean onRoute(Context context, String url);
}
