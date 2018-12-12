package com.ichsy.libs.core.comm.bus.url;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.bus.url.filter.BusBaseFilter;
import com.ichsy.libs.core.comm.bus.url.filter.IntentFilter;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.UrlUtil;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

/**
 * URL格式解析工具类
 * <p/>
 * 总线格式:leyou://native?native=B
 * <p/>
 *
 * @author liuyuhang
 * @date 16/4/22
 */
public class UrlParser {
//    public static final String AES_KEY_WORD_1 = "lEyOu";
//    public static final String AES_KEY_WORD_2 = "85861200";
//    public static final String AES_KEY_WORD = AES_KEY_WORD_1 + AES_KEY_WORD_2;

//    private IUrlParserCallback urlParserCallback;

    private static final String SCHEME_START = "leyou";

    public static final String SCHEME_RULE_NATIVE = "native";
    public static final String SCHEME_RULE_WEB = "web";
    public static final String SCHEME_RULE_ACTION = "action";

    public static final String URL_PATH_PARAMS_KEY = "_url_path";
//    public static final String URL_PATH_PARAMS_TIME = "_url_time";


    private static UrlParser INSTANCE;

    private HashMap<String, BusBaseFilter> filters;
    private HashMap<String, String> specialMap;

    public static UrlParser getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new UrlParser();
        }
        return INSTANCE;
    }

    private UrlParser() {
        //初始化过滤器的过滤类型
        filters = new HashMap<>();
    }

    /**
     * 给url总线添加可处理的逻辑
     */
    public void registerFilter(BusBaseFilter filter) {
        for (String s : filter.filterWhat()) {
            filters.put(s, filter);
        }
        LogUtils.v("filter", "给url添加处理类型:" + Arrays.toString(filter.filterWhat()));
    }

    /**
     * 批量添加事件
     *
     * @param filter
     */
    public void registerFilters(BusBaseFilter... filter) {
        for (BusBaseFilter busBaseFilter : filter) {
            registerFilter(busBaseFilter);
        }
    }

    /**
     * 添加特殊的url集合，转换成规范的url
     *
     * @param key   特殊的url
     * @param value 转换后的url
     */
    public void putSpacialMap(String key, String value) {
        if (null == specialMap) {
            specialMap = new HashMap<>();
        }
        specialMap.put(key, value);
    }

    /**
     * 获取规范的url前缀
     *
     * @param schemeType
     * @return leyou://native?
     * <br>leyou://web?
     * <br>leyou://action?
     * <br>
     */
    public String buildUri(String schemeType) {
        return buildUri(schemeType, null);
    }

    /**
     * 获取规范的url前缀
     *
     * @param schemeType
     * @param param      目标参数，SCHEME_RULE_NATIVE 为要跳转的activity类名<br>
     *                   SCHEME_RULE_WEB 为url地址<br>
     *                   SCHEME_RULE_ACTION 为具体事件
     * @return leyou://intent?<br>leyou://web?<br>leyou://action?<br>
     */
    public String buildUri(String schemeType, String param) {
        return "leyou://" + schemeType + (TextUtils.isEmpty(param) ? "" : ("?" + schemeType + "=" + param));
    }

//    /**
//     * 同步解析url
//     *
//     * @param context
//     * @param url
//     * @return
//     */
//    public synchronized boolean syncParser(Context context, String url) {
//        return parser(context, url);
//    }

    /**
     * 解析总线逻辑
     *
     * @param context
     * @param url     处理当前scheme url，并返回是否处理成功<br>
     *                <p/>
     *                url分成3部分，第一部分是固定的<leyou://>，第二部分是跳转逻辑，表示跳转到页面(intent)还是网页(web)还是事件(action)，后边会跟上参数<br>
     *                eg. leyou://intent?from=AActivity&to=BActivity
     * @return 返回是否解析成功，如果解析失败，可能为一个普通url
     */
    public boolean parser(Context context, String url) {
        return parser(context, url, (Intent) null);
    }

    /**
     * 解析总线逻辑
     *
     * @param context
     * @param url     处理当前scheme url，并返回是否处理成功<br>
     *                <p/>
     *                url分成3部分，第一部分是固定的<leyou://>，第二部分是跳转逻辑，表示跳转到页面(intent)还是网页(web)还是事件(action)，后边会跟上参数<br>
     *                eg. leyou://native?from=AActivity&to=BActivity
     * @param intent  附带的intent参数
     * @return 返回是否解析成功，如果解析失败，可能为一个普通url
     */
    public boolean parser(Context context, String url, Intent intent) {
        HashMap<String, Object> attachMap = new HashMap<>();
        attachMap.put(IntentFilter.PARAMS_ATTACH_INTENT_KEY, intent);
        return parser(context, url, attachMap);
    }

    public boolean parser(Context context, String url, HashMap<String, Object> attachMap) {
        if (null == filters || TextUtils.isEmpty(url)) {
//            if(urlParserCallback)
            return false;
        }

        if (null != specialMap && !specialMap.isEmpty()) {
            String value = specialMap.get(url);
            if (!TextUtils.isEmpty(value)) {
                url = value;
                LogUtils.i("lyh", "url replace: " + url);
            }
        }

        URI uri;
        try {
            uri = URI.create(url);
        } catch (Exception e) {
            LogUtils.i("urlparser", "url parser 失败，原因：" + e.getMessage());
            e.printStackTrace();
            return false;
        }

        String host = uri.getHost();
        String path = uri.getPath();

        String query = null;
        int queryIndex = url.indexOf("?");
        if (queryIndex >= 0) {
            query = url.substring(queryIndex + 1, url.length());
        }

        LogUtils.i("urlparser", "uri:" + uri.toString());
        LogUtils.i("urlparser", "query:" + query);
        LogUtils.i("urlparser", "current filter host is:" + host);
        LogUtils.i("urlparser", "current filter path is:" + path);

        BusBaseFilter filter;
        HashMap<String, String> queryMap = UrlUtil.parserQuery(query);
        LogUtils.i("urlparser", "queryMap size: " + queryMap.size());

        //首先判断是商品还是其他
        String keyWord = "forapp/";
        if (url.contains(keyWord)) {
            //其他
            query = url.substring(url.lastIndexOf(keyWord) + keyWord.length(), url.length());
            queryMap = UrlUtil.parserQuery(query);
            LogUtils.i("urlparser", "le query:" + query);

            filter = filters.get(host);
        } else {
            //商品
            if (!TextUtils.isEmpty(path)) {
//                queryMap = new HashMap<>();
                queryMap.put(URL_PATH_PARAMS_KEY, path.substring(path.lastIndexOf("/") + 1, path.length()));

                int pathLastIndex = path.lastIndexOf("/");
                if (pathLastIndex != 0 && pathLastIndex != -1) {
                    path = path.substring(0, pathLastIndex);
                }
            }
            LogUtils.i("urlparser", "filters.get(host + path): " + host + path);
            filter = filters.get(host + path);

//            if (filter instanceof IntentFilter) {
//                ((IntentFilter)filter).setUrlParserFilter(urlParserFilter);
//            }

        }

        if (null == filter) {
            return false;
        } else {
            filter.onAction(context, queryMap, attachMap);
            return true;
        }
    }

//    private List<UrlParserFilter> intentFilters;
//
//    public void addIntentFilter(UrlParserFilter filter, Class<?> hookClass) {
//        if (null == intentFilters) {
//            intentFilters = new ArrayList<>();
//        }
//        intentFilters.add(filter);
//    }
//
//    public interface UrlParserFilter {
//        /**
//         * fillter拦截时候的处理
//         * @param context
//         * @param attachMap
//         */
//        void onIntentFilter(Context context, HashMap<String, Object> attachMap);
//    }

//    public void setUrlParserCallback(IUrlParserCallback urlParserCallback) {
//        this.urlParserCallback = urlParserCallback;
//    }

}
