package com.ichsy.libs.core.frame.interfaces;

/**
 * Created by liuyuhang on 2016/12/29.
 */

public interface ActivityStatusListener {

    boolean isActivityFinish();

    void onActivityError(String message);
}
