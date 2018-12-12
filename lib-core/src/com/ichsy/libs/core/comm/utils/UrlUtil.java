package com.ichsy.libs.core.comm.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * 处理url链接的工具类
 * Created by liuyuhang on 16/7/27.
 */
public class UrlUtil {

    /**
     * 校验并调整url
     *
     * @param url
     * @return
     */
    public static String url(String url) {
        if (url.contains("://")) {
            return url;
        } else {
            return "http://" + url;
        }
    }

//    public static String getUrlFileName(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return "";
//        } else {
//            return url.substring(url.lastIndexOf("/") + 1, url.length());
//        }
//    }

    public static boolean isUrl(String url) {
        if (ObjectUtils.isNull(url)) return false;

        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String getUrl(String url) {
        if (ObjectUtils.isNotNull(url)) {
            String[] urls = url.split(" ");
            for (String s : urls) {
                if (isUrl(s)) return s;
            }
        }

        return null;
    }

    public static HashMap<String, String> getUrlQuery(String url) {
        HashMap<String, String> resultMap = new HashMap<>();
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            URI uri = null;
            try {
                uri = URI.create(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == uri) return resultMap;
//            String query = uri.getQuery(); //from=AActivity&to=BActivity
            resultMap = UrlUtil.parserQuery(uri.getQuery());
        }
        return resultMap;
    }

//    /**
//     * 判断url是否带有某参数
//     *
//     * @param url
//     * @return
//     */
//    public static boolean hasParams(String url, String key) {
//
//    }

    @Deprecated
    public static String getParams(String url, String key) {
        HashMap<String, String> params = UrlUtil.parserQuery(url);
        return params.get(key);
    }

    /**
     * 重新构建url的参数 加上&或者?
     * ##未测试
     *
     * @param url
     * @param params
     * @return
     */
    public static String getAppendString(String url, String params) {
        boolean hasParams = url.contains("?");

        return (hasParams ? "&" : "?") + params;
    }


    /**
     * 把http后面的参数处理为HashMap
     *
     * @param query
     * @return
     */
    public static HashMap<String, String> parserQuery(String query) {
        HashMap<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(query)) return map;

        LogUtils.i("urlparser", "parserQuery query:" + query);
        String[] group = query.split("&");
        if (group.length == 0) return map;

        for (String item : group) {
//            url=http://m.leyou.com.cn/gain/web_gain?id=104
            int splitIndex = item.indexOf("=");
            if (splitIndex < 0 || splitIndex + 1 > item.length()) {
                continue;
            }
            String key = item.substring(0, splitIndex);
            String value = null;
            try {
                value = URLDecoder.decode(item.substring(splitIndex + 1, item.length()), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LogUtils.i("urlparser", "current filter params: " + "key:" + key + "  value:" + value);

            map.put(TextUtils.isEmpty(key) ? "" : key, TextUtils.isEmpty(value) ? "" : value);
        }
        return map;
    }

    /**
     * 去掉http连接中的指定参数
     *
     * @param url
     * @param key
     * @return
     */
    public static String removeParams(String url, String key) {
        HashMap<String, String> params = UrlUtil.getUrlQuery(url);
        if (params == null || params.size() == 0 || !params.containsKey(key)) {
            return url;
        }

        String value = params.get(key);
        String replaceValue = key + "=" + (TextUtils.isEmpty(value) ? "" : value);
        if (params.size() == 1) {
            return url.replace("?" + replaceValue, "");
        }

        int i = url.indexOf(replaceValue);
        if (i <= 0) {
            return url;
        }

        String mark = url.substring(i - 1, i);
        if (mark.equals("?")) {
            return url.replace(replaceValue + "&", "");
        } else if (mark.equals("&")) {
            return url.replace("&" + replaceValue, "");
        } else {
            return url.replace(replaceValue, "");
        }
    }

    public static String appendParams(String url, String key, String value) {
        url = removeParams(url, key);

        if (url.endsWith("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (TextUtils.isEmpty(value)) {
            return url;
        }
        return url + (url.contains("?") ? "&" : "?") + key + "=" + value;
    }

}
