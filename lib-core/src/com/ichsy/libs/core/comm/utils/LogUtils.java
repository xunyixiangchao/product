package com.ichsy.libs.core.comm.utils;

import android.text.TextUtils;
import android.util.Log;

import com.ichsy.libs.core.comm.logwatch.LogWatcher;

/**
 * log工具类
 *
 * @author xingchun
 * @data 2015-3-31
 * @description 进入正式版本，LOG_LEVEL=0，那么log都不会输出
 */
public class LogUtils {
    public static int VERBOSE = 5;
    public static int DEBUG = 4;
    public static int INFO = 3;
    public static int WARN = 2;
    public static int ERROR = 1;

    //    public static int LOG_LEVEL = 6;
    private static int logLevel = 6;

    public static void setLogLevel(int level) {
        logLevel = level;
    }

    public static void v(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (logLevel > VERBOSE) {
            Log.v(tag, msg);
            LogWatcher.getInstance().putMessage("log", "v\n" + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (logLevel > DEBUG) {
            Log.d(tag, msg);
            LogWatcher.getInstance().putMessage("log", "d\n" + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (logLevel > INFO) {
            Log.i(tag, msg);
            LogWatcher.getInstance().putMessage("log", "i\n" + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (logLevel > WARN) {
            Log.w(tag, msg);
            LogWatcher.getInstance().putMessage("log", "w\n" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (logLevel > ERROR) {
            Log.e(tag, msg);
            LogWatcher.getInstance().putMessage("log", "e\n" + msg);
        }
    }

}
