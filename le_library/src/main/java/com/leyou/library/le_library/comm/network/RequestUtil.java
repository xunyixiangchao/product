package com.leyou.library.le_library.comm.network;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.AppUtils;
import com.ichsy.libs.core.comm.utils.DeviceUtil;
import com.ichsy.libs.core.comm.utils.MD5;
import com.ichsy.libs.core.comm.utils.NetWorkUtils;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.leyou.library.le_library.comm.helper.CitiesHelper;
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation;
import com.leyou.library.le_library.model.LocationCityInfo;
import com.leyou.library.le_library.model.ProtocolHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求的辅助类
 * Created by liuyuhang on 16/5/31.
 */
public class RequestUtil {

//    public static BaseRequest getRequestParams(Context context) {
//        return getRequestParams(context, null);
//    }

//    public static BaseRequest getRequestParams(Context context, Object body) {
//        return getRequestParams(context, body, "");
//    }

//    public static BaseRequest getRequestParams(Context context, Object body, String transaction_type) {
//        BaseRequest request = new BaseRequest();
//        request.header = createRequestHeader(context, transaction_type);
//        if (null == body) {
//            request.body = "{}";
//        } else {
//            request.body = body;
//        }
//        return request;
//    }


    public static ProtocolHeader createRequestHeader(Context context, String transaction_type) {
        ProtocolHeader header = new ProtocolHeader();

        String currentTime = System.currentTimeMillis() + "";

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        header.message_id = df.format(new Date()) + currentTime;

        header.time_stamp = currentTime;
        header.transaction_type = transaction_type.replace("/", "-");

        header.terminal = 2; //"1=IOS 2=安卓 3=网站 4=HTML5"
        header.imei = DeviceUtil.getDeviceId(context);

        header.sign = SignBuilder.createSign(header.token, header.message_id, header.time_stamp, header.transaction_type);

        header.token = TokenOperation.getToken(context);

        header.ua = DeviceUtil.getModel();
        header.version = AppUtils.getAppVersion(context);
        header.channel = DeviceUtil.getChannel(context, "leyou" + "/config/");

        String ip = DeviceUtil.getIp(context);
        if (!TextUtils.isEmpty(ip)) {
            header.ip = ip.replace(".", "-");
        }

        header.network_status = NetWorkUtils.getNetworkType(context) == NetWorkUtils.NETTYPE_WIFI ? 2 + "" : 1 + "";
//        header.network_status = NetWorkUtils.getNetWorkTypeString(context);

//        header.network_oper = DeviceUtil.getOperatorName(context);

        header.network_oper = getNetWorkOperation(context);

        LocationCityInfo currentCityFromCache = CitiesHelper.Instance.getCurrentCityFromCacheWithDefault(context);
        if (currentCityFromCache != null) {
            header.city_id = Integer.valueOf(currentCityFromCache.city_id);
        }

        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        header.build_mode = provider.getCache("apk_build_mode");

        return header;
    }

    /**
     * 获取当前网络运行商
     *
     * @param context
     * @return
     */
    private static String getNetWorkOperation(Context context) {
        String operator = DeviceUtil.getOperator(context);
        if (operator != null) {
            switch (operator) {
                case "46000":
                case "46002":
                    return "1";
//                signalTextView.setText("中国移动");
                case "46001":
                    return "2";
                case "46003":
                    return "3";
                default:
                    return "4";
            }
        } else {
            return "4";
        }
    }

    public static String getTransType(String url) {
        String transType = "default";
        if (!TextUtils.isEmpty(url) && url.length() >= 2) {
            if (url.lastIndexOf("/") + 1 == url.length()) {
                url = url.substring(0, url.length() - 1);
            }
            int startIndex = url.lastIndexOf("/") + 1;
            transType = url.substring(startIndex, url.length());
        }
        return transType;
    }

    /**
     * 验签工具类
     */
    public static class SignBuilder {
        public static String JI_SHU_KEY = "j";
        public static String OU_SHU_KEY = "o";

        /**
         * token的奇数位+messageID偶数位+timeStamp奇数位+ transactionType
         *
         * @return String 返回类型
         * @author Illidan
         * @date 2016年5月12日 下午5:37:50
         */
        public static String createSign(String token, String messageID, String timeStamp, String transactionType) {

            String tokenJ = "";// token基数
            StringBuilder returnSb = new StringBuilder();
            if (token != null && !token.equals("")) {
                Map<String, String> tokenMap = getCardinal(token);
                tokenJ = tokenMap.get(JI_SHU_KEY);
            }
            Map<String, String> messageIDMap = getCardinal(messageID);

            String messageIDO = messageIDMap.get(OU_SHU_KEY);// messageID偶数

            Map<String, String> timeStampMap = getCardinal(timeStamp);
            String timeStampJ = timeStampMap.get(JI_SHU_KEY);// timeStamp基数

            returnSb.append(tokenJ).append(messageIDO).append(timeStampJ).append(transactionType);

//        System.out.println(returnSb);

            return MD5.MD5Encode(returnSb.toString());
        }

        /**
         * 提取字符串基数位和偶数位 1位基数 2位偶数
         */
        public static Map<String, String> getCardinal(String str) {
            char[] charArray = str.toCharArray();
            StringBuffer sbJ = new StringBuffer();
            StringBuffer sbO = new StringBuffer();
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < charArray.length; i++) {
                // 基数
                if (i % 2 == 0) {
                    sbJ.append(charArray[i]);
                }
                // 偶数
                if (i % 2 == 1) {
                    sbO.append(charArray[i]);
                }
            }
            map.put(JI_SHU_KEY, sbJ.toString());
            map.put(OU_SHU_KEY, sbO.toString());

            return map;
        }
    }


}
