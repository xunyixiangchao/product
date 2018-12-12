package com.leyou.library.le_library.comm.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.leyou.library.le_library.config.LeConstant;

/**
 * 全局工具类，用来获取项目的全局参数
 *
 * @author liuyuhang
 * @date 16/4/29
 */
public class GlobalUtil {
    private static final String CACHE_SEVER_DIFF_TIME = "CACHE_SEVER_DIFF_TIME";

    /**
     * 本地和服务器时间
     */
    public static long differentTime = -1L;

    /**
     * 未读消息数量
     */
    private static String NOTIFY_MESSAGE_COUNT_KEY = "notify_message_count";

    /**
     * 获取下载文件的
     *
     * @return
     */
    public static String getDownloaderPath() {
        return Environment.getExternalStorageDirectory() + "/" + LeConstant.APP_DEFINE_NAME + "/";
    }

    /**
     * 获取服务器当前时间
     *
     * @param context
     * @return
     */
    public static long getServiceTime(Context context) {
        return System.currentTimeMillis() + getTimeDiff(context);
    }

    /**
     * 保存和服务器的时间差异
     */
    public static void saveTimeDiff(Context context, long diffTime) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        provider.putCache(CACHE_SEVER_DIFF_TIME, diffTime);
    }

    /**
     * 获取和服务器的时间差异
     */
    public static long getTimeDiff(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);

        Long cache = provider.getCache(CACHE_SEVER_DIFF_TIME, Long.class);
        if (cache == null) {
            cache = -1L;
        }
        return cache;
//            return provider.getCache(CACHE_SEVER_DIFF_TIME, Long.class) ?:-1L
    }

    /**
     * 设置未读消息数量
     *
     * @param count
     */
    public static void setMessageCount(Context context, int count) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        provider.putCache(NOTIFY_MESSAGE_COUNT_KEY, count + "");
    }

    /**
     * 获取未读消息数量
     *
     * @return
     */
    public static int getMessageCount(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        String countString = provider.getCache(NOTIFY_MESSAGE_COUNT_KEY);
        if (TextUtils.isEmpty(countString) || "".equals(countString)) {
            return 0;
        }
        return Integer.parseInt(countString);
    }

    /**
     * 添加未读消息数量（增量）
     *
     * @param count
     */
    public static void addMessageCount(Context context, int count) {
        setMessageCount(context, getMessageCount(context) + count);
    }


    /**
     * 是否需要跳过闪屏广告
     *
     * @param context
     */
    public static boolean isShipSplashAd(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);

        Long lastShipTime = provider.getCache("splash_ship_time", Long.class);

        if (null == lastShipTime) {
            return false;
        } else {
            //如果距离上次跳过时间不超过24小时，本次跳过广告
            long diff = System.currentTimeMillis() - lastShipTime;
            return diff <= 60 * 60 * 24 * 100;
        }
    }

    /**
     * 设置跳过闪屏广告
     *
     * @param context
     */
    public static void setShipSplashAd(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        provider.putCache("splash_ship_time", System.currentTimeMillis());
    }
}
