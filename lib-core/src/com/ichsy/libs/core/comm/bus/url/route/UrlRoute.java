package com.ichsy.libs.core.comm.bus.url.route;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dalvik.system.DexFile;

/**
 * Url的路由解析器，用来替代之前的UrlParser
 *
 * @author liuyuhang
 * @date 2018/1/9
 */
public class UrlRoute {
    private static UrlRoute instance;

    private List<IRoute> mRouteList;
    private HashMap<String, String> mRouteMap;

    public static UrlRoute getInstance() {
        if (null == instance) {
            instance = new UrlRoute();
        }
        return instance;
    }

    /**
     * 初始化路由表
     *
     * @param context
     */
    public void initRouteMap(final Context context) {
        mRouteMap = new HashMap<>();

        ThreadPoolUtil.getInstance().fetchData(new Runnable() {
            @Override
            public void run() {
                try {
                    //通过资源路径获得DexFile
                    DexFile e = new DexFile(context.getPackageResourcePath());
                    Enumeration entries = e.entries();
                    //遍历所有元素
                    while (entries.hasMoreElements()) {
                        String entryName = (String) entries.nextElement();
                        //匹配Activity包名与类名
                        if (entryName.toLowerCase(Locale.CHINA).contains("activity")) {
                            //通过反射获得Activity类
                            Class entryClass = Class.forName(entryName);
                            if (entryClass.isAnnotationPresent(Router.class)) {
                                Router router = (Router) entryClass.getAnnotation(Router.class);
                                String routeName = router.value();
                                if (TextUtils.isEmpty(mRouteMap.get(routeName))) {
                                    mRouteMap.put(routeName, entryName);
                                    LogUtils.i("route", String.format("route路由表注册成功：key [%s] class: %s", routeName, entryClass.getName()));
                                } else {
                                    LogUtils.e("route", String.format("route路由表重复请修改route名，[%s] class: %s", routeName, entryClass.getName()));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.i("route", String.format("route路由表注册异常：%s", e.getMessage()));
                }

            }
        });

    }

    /**
     * 是否有自定义的route路由
     */
    public boolean hasCustomRouteList() {
        return !(null == mRouteList || mRouteList.isEmpty());
    }

    public String getClassName(String name) {
        if (null == mRouteMap) {
            return null;
        }
        return mRouteMap.get(name);
    }

    /**
     * 添加url路由
     *
     * @param route 路由接口
     */
    public static void addRoute(IRoute route) {
        if (!getInstance().hasCustomRouteList()) {
            UrlRoute.instance.mRouteList = new ArrayList<>();
        }
        UrlRoute.instance.mRouteList.add(route);
    }

//    public static boolean route(Context context, String url) {
//        if (getInstance().hasCustomRouteList()) {
//            for (IRoute iRoute : getInstance().mRouteList) {
//                boolean cost = iRoute.onRoute(context, url);
//                if (cost) {
//                    return true;
//                }
//            }
//        }
//        return UrlParser.getInstance().parser(context, url);
//    }
}
