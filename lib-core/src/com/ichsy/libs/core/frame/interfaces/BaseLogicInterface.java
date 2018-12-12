package com.ichsy.libs.core.frame.interfaces;

import android.view.View;

/**
 * 处理网络业务的接口
 * Created by liuyuhang on 2016/12/9.
 */

public interface BaseLogicInterface<T> {

    /**
     * 初始化view
     *
     * @param rootView
     */
    void onInitView(View rootView);

    /**
     * 网络请求的函数写在这里
     */
    T onRequestData();

    /**
     * 请求完数据之后，刷新UI
     *
     * @param data
     */
    void onUpdateUi(T data);
}
