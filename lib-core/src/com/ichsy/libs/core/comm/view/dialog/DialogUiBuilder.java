package com.ichsy.libs.core.comm.view.dialog;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 页面的builder
 * Created by liuyuhang on 16/5/12.
 */
public interface DialogUiBuilder {

    /**
     * 页面初始化
     *
     * @return
     */
    View onViewCreate(@NonNull LayoutInflater inflater);

    /**
     * 页面内容绘制
     */
    void onViewDraw(@NonNull View view, String message);


}
