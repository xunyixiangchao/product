package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.content.Intent;

/**
 * 模拟用户点按事件
 * Created by liuyuhang on 16/10/14.
 */

public class KeyCodeUtils {

    /**
     * 模拟点击home键
     *
     * @param context
     */
    public static void pressHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }
}
