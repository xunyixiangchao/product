package com.ichsy.libs.core.comm.bus;

import android.support.annotation.Nullable;

/**
 * Created by liuyuhang on 16/4/22.
 */
public interface BusEventObserver {

    /**
     * 当收到event事件，会执行此函数
     *
     * @param event   event事件
     * @param message 事件所带的参数
     */
    void onBusEvent(String event, @Nullable Object message);
}
