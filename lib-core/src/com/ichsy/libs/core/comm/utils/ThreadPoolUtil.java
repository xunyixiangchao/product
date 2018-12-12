package com.ichsy.libs.core.comm.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LiuYuHang Date: 2015年5月29日
 *         <p/>
 *         Modifier： Modified Date： Modify：
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.libs.core.net.http.handler
 * @File HttpThreadController.java
 */
public class ThreadPoolUtil {
    private static final int THREAD_COUNT = 15;

    private static ThreadPoolUtil instance = null;

    private ExecutorService mService = null;

    //工作线程，次要优先级
    private ExecutorService mWordThreadService = null;

    //后台线程，低优先级
    private ExecutorService mBackGroundThreadService = null;

    private ThreadPoolUtil() {
        mService = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public static ThreadPoolUtil getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolUtil.class) {
                if (instance == null) {
                    instance = new ThreadPoolUtil();
                }
            }

        }
        return instance;
    }

    public void fetchData(Runnable request) {
        mService.submit(request);
    }

    /**
     * 检测当前是否在主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 检测并在主线程中执行 runnable
     *
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable) {
        if (ThreadPoolUtil.isMainThread()) {
            runnable.run();
        } else {
            getMainHandle().post(runnable);
        }
    }

    /**
     * 获取当前app的主handler
     *
     * @return
     */
    public static Handler getMainHandle() {
        return new Handler(Looper.getMainLooper());
    }
}
