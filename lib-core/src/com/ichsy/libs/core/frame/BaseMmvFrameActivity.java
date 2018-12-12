package com.ichsy.libs.core.frame;

import android.widget.FrameLayout;

/**
 * mmv模式的activity
 * Created by liuyuhang on 16/7/30.
 */
public abstract class BaseMmvFrameActivity<T> extends BaseFrameActivity {

    /**
     * 因为数据改变，通知的UI重绘
     *
     * @param mViewData
     * @param mContentView
     */
    protected abstract void onDataBindView(T mViewData, FrameLayout mContentView);

    /**
     * 通知重新绘制ui
     */
    protected void requestUiReloading(T data) {
        onDataBindView(data, mContentView);
    }

}
