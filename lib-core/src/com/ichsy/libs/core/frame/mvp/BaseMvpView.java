package com.ichsy.libs.core.frame.mvp;

import android.app.Activity;

/**
 * mvp的view基类
 * Created by liuyuhang on 16/10/12.
 */

public interface BaseMvpView<M> {

    /**
     * 初始化页面
     *
     * @param dataModel
     */
    void initView(Activity activity, M dataModel);

    /**
     * 数据发生改变
     *
     * @param dataModel
     */
    void onViewUpdate(M dataModel);

}
