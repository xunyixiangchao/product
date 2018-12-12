package com.ichsy.libs.core.comm.view.navigation;

import android.content.Intent;

/**
 * 导航栏监听器
 *
 * @author LiuYuHang   Date: 2015年5月14日
 *         <p/>
 *         Modifier： Modified Date： Modify： by xingchun
 *         <p/>
 *         Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.view.navigation
 * @File NavigationHandler.java
 */
public interface NavigationListener {

    /**
     * 返回
     */
    void popBack();

    /**
     * 通过总线逻辑跳转页面
     *
     * @param intent
     */
    void pushActivity(Intent intent);

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    void pushActivity(Class clz);

    /**
     * 通过总线逻辑跳转页面
     *
     * @param clz 跳转的activity类名
     */
    void pushActivity(Class clz, Intent intent);
}
  