package com.ichsy.libs.core.comm.bus;

import android.content.Context;

import java.io.Serializable;

/**
 * 总线业务逻辑
 * Created by liuyuhang on 16/4/22.
 */
public class BusManager {
    private static BusManager instance;

    private BusQueueService mService;
    private BusClassQueueService mClassService;

    /**
     * 使用getDefault()代替
     */
    @Deprecated
    public synchronized static BusManager getInstance() {
//        synchronized (BusManager.class) {
        if (null == instance) {
            instance = new BusManager();
        }
//        }
        return instance;
    }

    public synchronized static BusManager getDefault() {
//        synchronized (BusManager.class) {
        if (null == instance) {
            instance = new BusManager();
        }
//        }
        return instance;
    }


    private BusManager() {
        mService = new BusQueueService();

        mClassService = new BusClassQueueService();
    }

    /**
     * 注册总线事件，在类销毁的时候，需要调用unRegister()方法反注册事件，防止可能发生的oom
     *
     * @param key      event事件key
     * @param observer
     */
    public void register(String key, BusEventObserver observer) {
        unRegister(key, observer);

        mService.addQueue(key, new BusEventObject(observer, true));
    }

    @Deprecated
    public void registerClass(String key, Class<? extends BusEventObserver> clazz) {
        mClassService.addClass(key, clazz);
    }

    @Deprecated
    public void unRegisterClass(String key, Class<? extends BusEventObserver> clazz) {
        mClassService.remove(key, clazz);
    }

    @Deprecated
    public void postClassEvent(String key, Serializable object) {
        mClassService.distributeEvent(key, object);
    }

    /**
     * 批量注册事件
     *
     * @param observer
     * @param keys
     */
    public void register(BusEventObserver observer, String... keys) {
        if (null != keys) {
            for (String key : keys) {
                register(key, observer);
            }
        }
    }

    /**
     * 注册总线事件，会在主线程中收到event事件，在类销毁的时候，需要调用unRegister()方法反注册事件，防止可能发生的oom
     *
     * @param key      event事件key
     * @param observer
     */
    public void registerOnMainThread(String key, BusEventObserver observer) {
        unRegister(key, observer);
        mService.addQueue(key, new BusEventObject(observer, true));
    }

    /**
     * 注册总线事件，会在子线程中收到event事件，在类销毁的时候，需要调用unRegister()方法反注册事件，防止可能发生的oom
     *
     * @param key      event事件key
     * @param observer
     */
    public void registerOnThread(String key, BusEventObserver observer) {
        unRegister(key, observer);
        mService.addQueue(key, new BusEventObject(observer, false));
    }

    /**
     * 注册之后的事件在使用完或者不再使用的时候，需要进行反注册，防止可能发生的oom
     *
     * @param key
     */
    public void unRegister(String key, BusEventObserver observer) {
        mService.removeQueue(key, observer);
    }

    /**
     * 监听activity关闭，会自定反注册部分event事件
     *
     * @param clazz
     */
    public void onActivityDestroy(Object clazz) {

        if (clazz instanceof BusEventObserver) {
            mService.removeQueueByClassName(clazz);
        }

    }

//    /**
//     * 反注册该key所有的事件
//     *
//     * @param key
//     */
//    public void unRegisterKey(String key) {
//        if (mMessageQueue == null) return;
//        mMessageQueue.remove(key);
//    }

    /**
     * 分发事件，所有注册该事件的类都可以收到此事件和所传参数（message）
     *
     * @param key
     * @param message
     */
    public void postEvent(String key, Object message) {
        postEvent(null, key, message);
    }


    /**
     * 分发事件，所有注册该事件的类都可以收到此事件和所传参数（message）
     *
     * @param receiveContext 限制接收到总线小心的context
     * @param key
     * @param message
     */
    public void postEvent(Context receiveContext, String key, Object message) {
        mService.distributeEvent(receiveContext, key, message);
    }

}
