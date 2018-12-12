package com.leyou.library.le_library.comm.helper;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.ichsy.libs.core.comm.utils.LogUtils;
import com.tencent.bugly.crashreport.BuglyLog;

/**
 * 监控崩溃bug的工具框架
 * Created by liuyuhang on 16/8/19.
 */
public class AppWatcher {
    private static final String TAG = "watcher";
    private static AppWatcher instance;

    public static AppWatcher getInstance() {
        if (null == instance) {
            instance = new AppWatcher();
        }
        return instance;
    }

    public void tag(String tag) {
        BuglyLog.i(TAG, tag);
        LogUtils.i(TAG, tag);
    }


    public void tagClass(String tag, Object clz) {
        String append = clz.getClass().getSimpleName() + " (" + LeNavigationTitleHelper.INSTANCE.getTitleIsNone(clz) + ")";
        if (clz instanceof Activity) {
            append = append + " - Activity";
        } else if (clz instanceof Fragment) {
            append = append + " - Fragment";
        }
        BuglyLog.i(TAG, tag + " " + append);
        LogUtils.i(TAG, tag + " " + append);
    }
}
