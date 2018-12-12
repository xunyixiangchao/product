package com.ichsy.libs.core.comm.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.bus.url.filter.IntentFilter;
import com.ichsy.libs.core.frame.BaseFrameActivity;

import java.util.HashMap;

/**
 * 跳转管理类
 * Created by liuyuhang on 16/6/23.
 */
public class NavigationUtil {
    public static final String INTENT_BUNDLE_ACTIVITY_REQUEST_CODE = "INTENT_BUNDLE_ACTIVITY_REQUEST_CODE";

    /**
     * 跳转到某个页面
     *
     * @param context
     * @param intent
     */
    public static void navigationTo(Context context, Intent intent, HashMap<String, View> transitions) {
        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE);

        HashMap<String, Object> attachMap = new HashMap<>();
        attachMap.put(IntentFilter.PARAMS_ATTACH_INTENT_KEY, intent);
        attachMap.put(IntentFilter.PARAMS_BUNDLE_TRANSITION, transitions);
        UrlParser.getInstance().parser(context, uri, attachMap);
    }

    public static void navigationTo(Context context, Intent intent) {
        navigationTo(context, intent, null);
    }

    /**
     * 跳转到某个页面
     */
    public static void navigationTo(Context context, Class clz) {
//        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE, clz.getName());
//        UrlParser.getInstance().parser(context, uri);

        Intent intent = new Intent(context, clz);
        navigationTo(context, intent);
    }

    /**
     * 跳转到某个页面
     */
    public static void navigationTo(Context context, Class clz, Intent intent) {
//        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE, clz.getName());
//        UrlParser.getInstance().parser(context, uri, intent);

        intent.setClass(context, clz);
        navigationTo(context, intent);
    }

    public static void navigationToForResult(Context context, Intent intent, int requestCode) {
        String uri = UrlParser.getInstance().buildUri(UrlParser.SCHEME_RULE_NATIVE);
        intent.putExtra(INTENT_BUNDLE_ACTIVITY_REQUEST_CODE, requestCode);
        UrlParser.getInstance().parser(context, uri, intent);
    }

    public static void navigationToForResult(Context context, Class clz, int requestCode) {
        Intent intent = new Intent(context, clz);
        navigationToForResult(context, intent, requestCode);
    }

    public static void navigationToForResult(Context context, Class clz, Intent intent, int requestCode) {
        intent.setClass(context, clz);
        navigationToForResult(context, intent, requestCode);
    }

    /**
     * 跳转到某页面，然后在这个页面关闭的时候，打开第二个页面
     *
     * @param context
     * @param intent
     * @param secondIntent
     */
    public static void navigationNext(Context context, Intent intent, Intent secondIntent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BaseFrameActivity.BUNDLE_INTENT_SECOND_INTENT, secondIntent);
        intent.putExtras(bundle);
        NavigationUtil.navigationTo(context, intent);
    }

//    /**
//     * 跳转到某页面，然后在这个页面关闭的时候，执行事件
//     *
//     * @param context
//     * @param intent
//     * @param actionEvent
//     */
//    public static void navigationNextAndRunAction(Context context, Intent intent, String actionEvent) {
//        Bundle bundle = new Bundle();
//        bundle.putString(BaseFrameActivity.BUNDLE_INTENT_SECOND_ACTION_EVENT, actionEvent);
////        bundle.putParcelable(BaseFrameActivity.BUNDLE_INTENT_SECOND_ACTION_EVENT, actionEvent);
//        intent.putExtras(bundle);
//        NavigationUtil.navigationTo(context, intent);
//    }

    /**
     * 跳转到某页面，然后在这个页面关闭的时候，打开第二个页面
     *
     * @param context
     * @param intent
     * @param secondScheme
     */
    public static void navigationNext(Context context, Intent intent, String secondScheme) {
        intent.putExtra(BaseFrameActivity.BUNDLE_INTENT_SECOND_SCHEME_URL, secondScheme);
        NavigationUtil.navigationTo(context, intent);
    }
}
