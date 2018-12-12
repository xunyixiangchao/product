package com.ichsy.libs.core.comm.bus.url;

import android.app.Activity;

import java.util.HashMap;

/**
 * intent跳转的回调
 * Created by liuyuhang on 2018/8/1.
 */

public interface IIntentFiliter {

    void onActivityIntent(Activity intentClass, HashMap<String , Object> attachMap);
}
