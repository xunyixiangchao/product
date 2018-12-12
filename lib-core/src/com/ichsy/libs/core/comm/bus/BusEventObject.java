package com.ichsy.libs.core.comm.bus;

/**
 * 注册在消息总线的event事件
 * Created by liuyuhang on 16/5/12.
 */
public class BusEventObject {
    public boolean onMainThread;
    public BusEventObserver observer;
    public String classNameTarget;//记录是哪个class的

    public BusEventObject(BusEventObserver observer, boolean onMainThread) {
        this.observer = observer;
        this.onMainThread = onMainThread;

        this.classNameTarget = observer.getClass().getName();
    }

}
