package com.leyou.library.le_library.comm.bus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 拼装bus的String串
 * Created by liuyuhang on 2017/5/22.
 */

public class BusStringFactory {

    public static String buildWebViewUrl(String url) {
//        leyou://action?action=loginAndOpen&open=com.capelabs.leyou.ui.activity.order.O2oCashierActivity&intent_o2o_order_id_bundle=" + orderId
        try {
            return "leyou://native?native=com.capelabs.leyou.ui.activity.WebViewActivity&url=" + URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String buildActivityUrl(Class clazz) {
        return "leyou://native?native=" + clazz.getName();
    }
}
