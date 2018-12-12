package com.ichsy.libs.core.comm.helper;

import android.os.Handler;

/**
 * 倒计时工具类
 *
 * @author liuyuhang
 * @date 2018/7/10
 */
public class TimerHelper {

    /**
     * 用来做倒计时的handler
     */
    private Handler timerHandler;
    /**
     * 倒计时的间隔，单位毫秒
     */
    private long interval = 500L;

    /**
     * 结束时间
     */
    private long endTime = 0L;

    /**
     * 当前时间
     */
    private long currentTime = 0L;

    private TimerListener mTimerListener;

    public interface TimerListener {

        /**
         * 每次倒计时都会触发
         *
         * @param currentTime 当前时间
         */
        void onTimer(long currentTime);

        /**
         * 倒计时结束
         */
        void onTrigger();
    }

    public TimerHelper() {
        timerHandler = new Handler();
        currentTime = 0;
    }

    public void setTimerListener(TimerListener listener) {
        mTimerListener = listener;
    }

    private Runnable timerRunnable = new Runnable() {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            currentTime += interval;

            if (null != mTimerListener) {
                mTimerListener.onTimer(currentTime);

                if (currentTime >= endTime) {
                    mTimerListener.onTrigger();
                } else {
                    timerHandler.postDelayed(timerRunnable, interval);
                }
            }

        }
    };

    /**
     * 开始倒计时
     *
     * @param endTime 单位秒
     */
    public void start(int endTime) {
        start(endTime, interval);
    }

    public void start(int endTime, long interval) {
        this.endTime = endTime * 1000;
        this.interval = interval;
        stop();
        timerHandler.post(timerRunnable);
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
    }
}
