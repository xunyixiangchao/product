package com.leyou.library.le_library.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.ichsy.libs.core.net.http.cache.SimpleCacheAdapter;
import com.leyou.library.le_library.comm.network.LeHttpHelper;
import com.leyou.library.le_library.model.LeSetting;
import com.leyou.library.le_library.model.response.SystemSettingResponse;

/**
 * 全局配置信息
 * Created by liuyuhang on 16/6/8.
 */
public enum LeSettingInfo {
    INSTANCE;

    public LeSetting setting = new LeSetting();

    /**
     * 本次更新配置文件是否成功，是否失败，需要根据是否成功确定要不要重新更新
     */
    private static boolean isServerInfoUpdateSuccess = true;

    public static LeSettingInfo get() {
        return INSTANCE;
    }

//    public String getHotWordHint() {
//        if (setting.hot_search == null) {
//            return "搜索乐友商品";
//        } else {
//            //随机值
//            return setting.hot_search[new Random().nextInt(setting.hot_search.length)].search_display;
//        }
//    }

    public void init(final Context context) {
        RequestOptions options = new RequestOptions();
        options.toastDisplay(false);
        LeHttpHelper httpHelper = new LeHttpHelper(context, options);

        SimpleCacheAdapter cacheAdapter = new SimpleCacheAdapter(context, httpHelper);
        cacheAdapter.doPost(LeConstant.API.URL_BASE + LeConstant.API.URL_GLOBAL_SETTING, null, SystemSettingResponse.class, new RequestListener() {
                    @Override
                    public void onHttpRequestComplete(@NonNull String url, @NonNull HttpContext httpContext) {
                        super.onHttpRequestComplete(url, httpContext);

                        SystemSettingResponse response = httpContext.getResponseObject();
                        if (response.header.res_code == 0) {
                            isServerInfoUpdateSuccess = true;
                            setting = response.body;
                        } else {
                            isServerInfoUpdateSuccess = false;
                        }
                    }
                }
        );


    }

//    public SystemSettingResponse.Setting initSettingInfo() {
//        SystemSettingResponse.Setting setting = new SystemSettingResponse.Setting();
//
//        setting.ship_desc = new HashMap<>();
//        setting.ship_desc.put("北京", "满79包邮");
//        setting.ship_desc.put("天津", "满79包邮");
//        setting.ship_desc.put("其他", "满99包邮");
//
//        setting.inviteInfo = new SystemSettingResponse.Setting.InviteInfo();
//        setting.inviteInfo.title = "亲爱哒，乐友发福利啦，注册就送15元券，快来看看";
//        setting.inviteInfo.content = "新客有礼，下载乐友APP，不光送券还送积分，不要错过哦";
//        setting.inviteInfo.title = "http://www.leyou.com.cn/app/download/lyappdownload.php?from=singlemessage&isappinstalled=1";
//
//        return setting;
//    }

}
