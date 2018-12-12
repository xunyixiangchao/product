package com.leyou.library.le_library.frame;

import android.support.annotation.NonNull;
import android.view.View;

import com.ichsy.libs.core.frame.BaseDialogFrameActivity;
import com.ichsy.libs.core.frame.BaseFrameActivity;

import org.jetbrains.annotations.NotNull;

/**
 * ui管理类
 * Created by liuyuhang on 2018/7/26.
 */

public interface BaseUiManager<T> {

    /**
     * 初始化ui布局
     *
     * @return
     */
    int initLayoutUi();

    void onInitUi(BaseFrameActivity activity, @NonNull View rootView);

    /**
     * 当数据发生改变的时候，会调用此方法
     *
     * @param data 返回数据
     */
    void onUpdateUi(@NonNull BaseDialogFrameActivity activity, @NotNull View ui, @NotNull T data);

    /**
     * 如果当前数据为空的业务处理
     */
    void onDataEmpty();

    /**
     * 当网络请求失败的业务处理
     */
    void onNetError();

    /**
     * 当数据请求错误的时候的业务处理
     *
     * @param code
     * @param message
     */
    void onDataError(int code, String message);


}
