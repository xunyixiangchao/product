package com.ichsy.libs.core.frame.mvp;

import android.content.Context;

/**
 * mvp的model数据处理基类，包括了网络请求之类
 * Created by liuyuhang on 16/10/12.
 */
public class BaseMvpModel<T> {
    private T data;
    private Context context;

    public void update(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Context getContext() {
        return context;
    }

    public Context getModelContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
