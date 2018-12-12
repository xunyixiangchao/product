package com.leyou.library.le_library.comm.helper;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation;

/**
 * Created by lis on 2017/6/19.
 */

public class BabyHelper {
    private static String BABY_ICON = "BABY_ICON";
    private static String BABY_DRESS_STATUS = "BABY_DRESS_STATUS";

    public static boolean isBoy(String sex) {
        return !"f".equals(sex);
    }

    /**
     * 设置宝宝头像
     *
     * @param context
     * @param img
     */
    public static void setBabyImg(Context context, String img) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context, TokenOperation.getUserId(context));
        provider.putCache(BABY_ICON, img);
    }

    /**
     * 获取宝宝头像
     *
     * @param context
     * @return
     */
    public static String getBabyImg(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context, TokenOperation.getUserId(context));
        return TextUtils.isEmpty(provider.getCache(BABY_ICON)) ? "" : provider.getCache(BABY_ICON);
    }

    /**
     * 设置宝宝妆扮开关
     *
     * @param context
     * @param isOpen
     */
    public static void setBabyDressStatus(Context context, boolean isOpen) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context, TokenOperation.getUserId(context));
        provider.putCache(BABY_DRESS_STATUS, isOpen);
    }

    /**
     * 获取宝宝妆扮开关状态
     *
     * @param context
     * @return
     */
    public static boolean getBabyDressStatus(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context, TokenOperation.getUserId(context));
        return provider.getCache(BABY_DRESS_STATUS, Boolean.class) == null ? false
                : provider.getCache(BABY_DRESS_STATUS, Boolean.class);
    }
}
