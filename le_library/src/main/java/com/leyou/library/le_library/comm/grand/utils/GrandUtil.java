package com.leyou.library.le_library.comm.grand.utils;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.DeviceUtil;
import com.ichsy.libs.core.comm.utils.UrlUtil;
import com.leyou.library.le_library.comm.grand.constant.GrandConstant;
import com.leyou.library.le_library.comm.grand.helper.GrandRequestHelper;
import com.leyou.library.le_library.comm.grand.modle.GrandFieldVo;
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation;
import com.leyou.library.le_library.config.LeSettingInfo;

import java.util.List;

/**
 * Created by ss on 2018/7/10.
 */
public class GrandUtil {

    private static void request(Context context, int appId, String url, Object... field) {
        new GrandRequestHelper.Builder()
                .setAppId(appId)
                .setTableName("user_action")
                .setCmd("add")
                .addField(field)
                .create()
                .request(context, url);
    }

    private static void request1(Context context, Object... field) {
        String url = LeSettingInfo.get().setting.daguan_send;
        if(TextUtils.isEmpty(url)){
            return;
        }
        request(context, GrandConstant.GRAND_APP_ID, url, field);
    }

    private static void request2(Context context, Object... field) {
        request(context, GrandConstant.GRAND_APP_ID_2, GrandConstant.URL_GRAND_USER_ACTION_2, field);
    }

    private static void request(Context context, Object... field) {
        request1(context, field);
        request2(context, field);
    }

    public static void requestSimple(Context context, String actionType, String itemId) {
        GrandFieldVo field = new GrandFieldVo();
        field.action_type = actionType;
        field.itemid = itemId;
        field = packData(context, field);
        request(context, field);
    }

    public static void requestRecommend(Context context, String itemId, String sceneType, String requestId) {
        GrandFieldVo field = new GrandFieldVo();
        field.action_type = "rec_click";
        field.itemid = itemId;
        field.scene_type = sceneType;
        field.rec_requestid = requestId;
        field = packData(context, field);
        request1(context, field);
    }

    public static void requestProduct(Context context, String itemId, String sceneType, String requestId) {
        GrandFieldVo field = new GrandFieldVo();
        field.action_type = "view";
        field.itemid = itemId;
        field.scene_type = sceneType;
        field.rec_requestid = requestId;
        field = packData(context, field);
        request(context, field);
    }

    public static void requestSearch(Context context, String keyword, String sceneType) {
        GrandFieldVo field = new GrandFieldVo();
        field.action_type = "search";
        field.keyword = keyword;
        field.scene_type = sceneType;
        field = packData(context, field);
        request(context, field);
    }

    public static void requestSearchClick(Context context, String itemId, String keyword, String sceneType, String requestId) {
        GrandFieldVo field = new GrandFieldVo();
        field.action_type = "search_click";
        field.itemid = itemId;
        field.keyword = keyword;
        field.scene_type = sceneType;
        field.rec_requestid = requestId;
        field = packData(context, field);
        request(context, field);
    }

    public static void requestMultiple(Context context, String actionType, List<GrandFieldVo> fieldList) {
        if (fieldList == null) {
            return;
        }
        for (GrandFieldVo field : fieldList) {
            field.action_type = actionType;
            packData(context, field);
        }
        request(context, fieldList.toArray());
    }

    public static void requestSearchMultiple(Context context, String actionType, String sceneType,
                                             List<GrandFieldVo> fieldList) {
        if (fieldList == null) {
            return;
        }
        for (GrandFieldVo field : fieldList) {
            field.action_type = actionType;
            field.scene_type = sceneType;
            packData(context, field);
        }
        request2(context, fieldList.toArray());
    }


    private static GrandFieldVo packData(Context context, GrandFieldVo field) {
        if (field == null) {
            field = new GrandFieldVo();
        }
        field.userid = TokenOperation.getUserId(context);
        field.imei = DeviceUtil.getDeviceId(context);
        field.timestamp = System.currentTimeMillis() / 1000;
        return field;
    }

    public static String dealUrl(String url, String sceneType) {
        url = UrlUtil.appendParams(url, GrandConstant.INTENT_PRODUCT_GRAND_SCENE, sceneType);
        return url;
    }

}
