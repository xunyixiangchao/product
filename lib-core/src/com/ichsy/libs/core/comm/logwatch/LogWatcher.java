package com.ichsy.libs.core.comm.logwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.ichsy.libs.core.comm.logwatch.db.DAO;

/**
 * log日志观察期
 *
 * @author liuyuhang
 */
public class LogWatcher {
    private MyWindowManager windowManager;
    private DAO dao;
    private Context context;

    private static LogWatcher instance;

    public static LogWatcher getInstance() {
        if (instance == null)
            instance = new LogWatcher();
        return instance;
    }

    public void init(Context context) {
        init(context, getApplicationName(context));
    }

    public void init(Context context, String title) {
        this.context = context;
        if (windowManager == null) {
            windowManager = MyWindowManager.getInstance(context);
        }

        if (!windowManager.isShow()) {

            //权限判断
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(context.getApplicationContext())) {
                    //启动Activity让用户授权
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);

                    if (!(context instanceof Activity)) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                } else {
                    //执行6.0以上绘制代码
                    windowManager.show(title);// 如果需要修改悬浮框名称，则直接添加参数
                }
            } else {
                //执行6.0以下绘制代码
                windowManager.show(title);// 如果需要修改悬浮框名称，则直接添加参数
            }
            // finish();
        }
    }

    public void show() {
        if (null != windowManager) {
            windowManager.show();
        }
    }

    public void dismiss() {
        if (null != windowManager) {
            windowManager.dismissFloat();
        }
    }

    public String getApplicationName(Context mContext) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = mContext.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public void putRequestInfo(String request) {
        putMessage("请求", request);
    }

    public void putMessage(String message) {
        putMessage("消息", message);
    }

    /**
     * 放入一组message
     *
     * @param message
     */
    public void putMessage(String... message) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < message.length; i++) {
            sb.append(message[i] + "\n");
        }
        putMessage(sb.toString());
    }

    /**
     * 添加带有target标签的消息
     *
     * @param tag
     * @param message
     */
    public void putMessage(String tag, String message) {
        if (context == null)
            return;

        if (dao == null) {
            dao = new DAO(context);
        }
        dao.insert(tag, message);// 第一个参数类型名称，第二个参数是详细信息
    }

}
