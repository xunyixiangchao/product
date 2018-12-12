package com.ichsy.libs.core.comm.bus.url.route;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ichsy.libs.core.comm.utils.GsonHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由拼接工具
 *
 * @author liuyuhang
 * @date 2018/1/12
 */
public class RouterBuilder {

    private static String scheme;
    private String actionName;
    private String actionParams;
    private HashMap<String, String> params;

    public static void setDefaultScheme(String scheme) {
        RouterBuilder.scheme = scheme;
    }


    /**
     * 从页面获取route的bundle
     *
     * @param activity
     * @return
     */
    @Nullable
    public static HashMap<String, Object> getRouteBundle(Activity activity) {
        if (null == activity) {
            return null;
        }
        String bundleString = activity.getIntent().getStringExtra("route_bundle");
        if (TextUtils.isEmpty(bundleString)) {
            return null;
        }
        return GsonHelper.build().fromJson(bundleString, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public RouterBuilder setActionName(String actionName, String actionParams) {
        this.actionName = actionName;
        this.actionParams = actionParams;
        return this;
    }

    public RouterBuilder addParams(String key, String value) {
        if (null == params) {
            params = new HashMap<>();
        }

        try {
            params.put(key, URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            params.put(key, value);
        }
        return this;
    }

    public RouterBuilder addParams(HashMap<String, Object> map) {
        if (null == params) {
            params = new HashMap<>();
        }

        String params = GsonHelper.build().toJson(map, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        addParams("route_bundle", params);
        return this;
    }


    public String build() {
        StringBuilder baseUrl = new StringBuilder(String.format("%s://%s?%s=%s", scheme, actionName, actionName, actionParams));
        if (null != params && !params.isEmpty()) {
            StringBuilder paramsStr = new StringBuilder();
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                paramsStr.append(String.format("&%s=%s", stringStringEntry.getKey(), stringStringEntry.getValue()));
            }
//            baseUrl.append("&");
            baseUrl.append(paramsStr.toString());
        }

        return baseUrl.toString();

//        return "leyou://" + schemeType + (TextUtils.isEmpty(param) ? "" : ("?" + schemeType + "=" + param))
    }

}
