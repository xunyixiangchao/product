/*
 * ToastUtils.java [V 1.0.0]
 * classes : com.ichsy.libs.view.toast.ToastUtils
 * 时培飞 Create at 2015-5-28 下午3:14:40
 */
package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * com.ichsy.libs.view.toast.ToastUtils
 *
 * @author 时培飞 Create at 2015-5-28 下午3:14:40
 */
public class ToastUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    /***
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * @param msg 消息内容
     *            Create at 2015-5-28 下午3:15:26
     * @author 时培飞
     */
    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /***
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @param msg 消息内容
     * @author 时培飞
     * Create at 2015-5-28 下午3:16:02
     */
    public static void showMessageLong(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /***
     * Toast发送消息，默认Toast.LENGTH_SHORT
     *
     * 发送整形数据 Create at 2015-5-28 下午3:16:48
     * @author 时培飞
     */
    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /***
     * Toast发送消息，默认Toast.LENGTH_LONG
     *
     * @author 时培飞
     * Create at 2015-5-28 下午3:17:10
     */
    public static void showMessageLong(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /***
     * Toast发送消息
     *
     * @param msg 发送内容
     * @param len 显示时长
     *            Create at 2015-5-28 下午3:17:31
     * @author 时培飞
     */
    public static void showMessage(final Context act, final int msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(act, msg, len);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    /***
     * Toast发送消息
     *
     * @param msg 发送内容
     * @param len 显示时长
     *            Create at 2015-5-28 下午3:17:31
     * @author 时培飞
     */
    public static void showMessage(final Context act, final String msg, final int len) {
        if (!TextUtils.isEmpty(msg) && null != act) {

//            ThreadPoolUtil.runOnMainThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(act, msg, len).show();
//                }
//            });
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(act, msg, len);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        }
//        new Thread(new Runnable() {
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (synObj) {
//                            if (toast != null) {
//                                toast.cancel();
//                                toast.setText(msg);
//                                toast.setDuration(len);
//                            } else {
//                                toast = Toast.makeText(act, msg, len);
//                            }
//                            toast.setGravity(Gravity.BOTTOM, 0, 0);
//                            toast.show();
//                        }
//                    }
//                });
//            }
//        }).start();
    }

    /**
     * 关闭当前Toast
     *
     * @author 时培飞
     * Create at 2015-5-28 下午3:18:31
     */
    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
