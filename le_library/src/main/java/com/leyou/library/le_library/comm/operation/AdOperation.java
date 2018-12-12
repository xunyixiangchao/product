package com.leyou.library.le_library.comm.operation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.leyou.library.le_library.comm.handler.OperationHandler;
import com.leyou.library.le_library.comm.helper.BarCodeHelper;
import com.leyou.library.le_library.comm.network.LeHttpHelper;
import com.leyou.library.le_library.config.Constant;
import com.leyou.library.le_library.config.LeConstant;
import com.leyou.library.le_library.kotlin.ImageViewExtKt;
import com.leyou.library.le_library.model.BaseRequest;
import com.leyou.library.le_library.model.O2OAdInfoVo;
import com.leyou.library.le_library.model.OrderRecommendVo;
import com.leyou.library.le_library.model.ProtocolHeader;
import com.leyou.library.le_library.model.response.AdO2oOrderResponse;
import com.leyou.library.le_library.model.response.O2OAdsListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.liuyh.com.lelibrary.R;

/**
 * 广告相关的网络请求操作
 * Created by liuyuhang on 16/7/6.
 */
public class AdOperation extends BaseRequestOperation {
    private static final String CACHE_KEY_CHANNEL_SRC = "CACHE_KEY_CHANNEL_SRC";
    private static final String CACHE_KEY_O2O_ADS = "CACHE_KEY_O2O_ADS";
    private static final String CACHE_KEY_NEW_O2O_ADS = "CACHE_KEY_NEW_O2O_ADS";
    public static final String DEFAULT_BUSINESS_ID = "0";

    private static final String AD_HOME_CONTROLLER = "ad_home_controller";

//    public static void getSplashAd(Context context, final OperationListener<LeAdResponse> handler) {
//        getErpAd(context, "305", handler);
//    }
//
//    public static void getShoppingCartAd(Context context, final OperationListener<LeAdResponse> handler) {
//        getErpAd(context, "302", handler);
//    }

//    private static void getErpAd(Context context, String adId, final OperationListener<LeAdResponse> handler) {
//        LeHttpHelper httpHelper = HttpHelperBuilder.getSilentHttpHelper(context);
//
//        httpHelper.doOldPost(Constant.API.URL_AD_GET_ERP + adId, null, LeAdResponse.class, new RequestListener() {
//            @Override
//            public void onHttpRequestComplete(String url, HttpContext httpContext) {
//                super.onHttpRequestComplete(url, httpContext);
//                LeAdResponse response = httpContext.getResponseObject();
//                if (null != response && null != response.data && !response.data.isEmpty()) {
//                    handler.onCallBack(response);
//                }
//
//            }
//        });
//    }

    public static void getO2oOrderRecommend(Context context, final OperationHandler<List<OrderRecommendVo>> handler) {
        new LeHttpHelper(context).doOldPost(Constant.API.URL_AD_O2O_ORDER, null, AdO2oOrderResponse.class, new RequestListener() {
            @Override
            public void onHttpRequestComplete(String url, HttpContext httpContext) {
                super.onHttpRequestComplete(url, httpContext);
                AdO2oOrderResponse response = null;
                List<OrderRecommendVo> data = null;
                if (httpContext.code == 0) {
                    response = httpContext.getResponseObject();
                }
                if (response != null) {
                    data = response.data;
                }

                if (data != null && data.size() > 0) {
                    handler.onCallback(data);
                } else {
                    handler.onFailed(httpContext.code, httpContext.message);
                }
            }
        });
    }

    /**
     * 保存推广渠道
     *
     * @param context
     * @param src
     */
    public static void setChannelSrc(Context context, String src) {
        if (TextUtils.isEmpty(src)) {
            return;
        }

        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        provider.putCache(CACHE_KEY_CHANNEL_SRC, src);
    }

    /**
     * 获取src
     *
     * @param context
     * @return
     */
    @Nullable
    public static String getChannelSrc(Context context) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        return provider.getCache(CACHE_KEY_CHANNEL_SRC);
    }

    /**
     * 获取O2O的广告
     */
    public static void requestO2oAds(final Context context) {
        RequestOptions options = new RequestOptions();
        options.toastDisplay(false);
        LeHttpHelper httpHelper = new LeHttpHelper(context, options);
        httpHelper.post(LeConstant.API.URL_BASE +
                Constant.API.URL_AD_O2O_ALL, new BaseRequest(), O2OAdsListResponse.class, new RequestListener() {
            @Override
            public void onHttpRequestComplete(@NonNull String url, @NonNull final HttpContext httpContext) {
                super.onHttpRequestComplete(url, httpContext);

                O2OAdsListResponse response = httpContext.getResponseObject();
                ProtocolHeader header = (ProtocolHeader) httpContext.getParams().get("le_header");

                Context refContext = httpContext.getContext().get();
                if (null != header && null != refContext) {
                    BarCodeHelper.Companion.updateSeverTime(refContext, header.time_stamp);
                }

                if (Constant.CODE.CODE_OK == httpContext.code && null != refContext) {
                    saveO2oAdsCache(refContext, response);
                    if (response.ad_list != null && !response.ad_list.isEmpty()) {
                        Collections.sort(response.ad_list, new Comparator<O2OAdInfoVo>() {
                            @Override
                            public int compare(O2OAdInfoVo t0, O2OAdInfoVo t1) {
                                return Integer.parseInt(t0.type) - Integer.parseInt(t1.type);
                            }
                        });
                        saveO2oAdsCache(refContext, transTypeAdList(response.ad_list));
                    }
                }

            }
        });
    }


    public static class TransTypeAdVo {
        public String type;
        private List<O2OAdInfoVo> typeAdList = new ArrayList<>();
    }

    private static class TransBusinessAdVo {
        private String businessId;
        List<O2OAdInfoVo> businessAdList = new ArrayList<>();
    }

    public static class TransVo {
        public String type;
        private List<TransBusinessAdVo> transBusinessAdVoList;
    }


    private static List<TransVo> transTypeAdList(List<O2OAdInfoVo> list) {
        List<TransTypeAdVo> typeAdList = new ArrayList<>();
        TransTypeAdVo tansTypeAdVo = null;
        // 先根据type进行组合
        for (O2OAdInfoVo o2OAdInfoVo : list) {
            if (tansTypeAdVo == null ||
                    !o2OAdInfoVo.type.equals(typeAdList.get(typeAdList.size() - 1).type)) {
                tansTypeAdVo = new TransTypeAdVo();
                tansTypeAdVo.type = o2OAdInfoVo.type;
                tansTypeAdVo.typeAdList.add(o2OAdInfoVo);
                typeAdList.add(tansTypeAdVo);
            } else {
                typeAdList.get(typeAdList.size() - 1).typeAdList.add(o2OAdInfoVo);
            }

        }

        // 在根据business_id进行组合
        List<TransVo> transVoList = new ArrayList<>();
        for (TransTypeAdVo transTypeAdVo : typeAdList) {
            Collections.sort(transTypeAdVo.typeAdList, new Comparator<O2OAdInfoVo>() {
                @Override
                public int compare(O2OAdInfoVo t0, O2OAdInfoVo t1) {
                    return Integer.parseInt(t0.business_id) - Integer.parseInt(t1.business_id);
                }
            });
            List<O2OAdInfoVo> mBusinessList = transTypeAdVo.typeAdList;
            List<TransBusinessAdVo> businessList = new ArrayList<>();
            TransBusinessAdVo transBusinessAdVo = null;

            for (O2OAdInfoVo mBusinessAdVo : mBusinessList) {
                if (transBusinessAdVo == null || !mBusinessAdVo.business_id.equals(businessList.get(
                        businessList.size() - 1).businessId)) {
                    transBusinessAdVo = new TransBusinessAdVo();
                    transBusinessAdVo.businessId = mBusinessAdVo.business_id;
                    transBusinessAdVo.businessAdList.add(mBusinessAdVo);
                    businessList.add(transBusinessAdVo);
                } else {
                    businessList.get(businessList.size() - 1).businessAdList.add(mBusinessAdVo);
                }
            }
            TransVo transVo = new TransVo();
            transVo.type = transTypeAdVo.type;
            transVo.transBusinessAdVoList = businessList;
            transVoList.add(transVo);
        }
        return transVoList;
    }


    /**
     * 保存服务端返回的ads
     *
     * @param context
     * @param response
     */
    private static void saveO2oAdsCache(Context context, O2OAdsListResponse response) {
        if (null != response && ObjectUtils.isNotNull(response.ad_list)) {
            BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
            HashMap<String, HashMap<String, O2OAdInfoVo>> adsMap = new HashMap<>();
            for (O2OAdInfoVo o2OAdInfoVo : response.ad_list) {
                HashMap<String, O2OAdInfoVo> map = adsMap.get(o2OAdInfoVo.type);
                if (null == map) {
                    map = new HashMap<>();
                }

                if (null == map.get(o2OAdInfoVo.business_id)) {
                    map.put(o2OAdInfoVo.business_id, o2OAdInfoVo);
                }

                adsMap.put(o2OAdInfoVo.type, map);
//                if(o2OAdInfoVo.msg_type.equals(o2OAdInfoVo.O2O_AD_TYPE_1)){// 如果是首页广告 记录当前时间
//                    AdHomeTimeModel model = readController(context);
//                    if(model == null){
//                        model = new AdHomeTimeModel();
//                    }
//                    model.requestTime = System.currentTimeMillis();
//                    writeController(context, model);
//                }
            }
            provider.putCache(CACHE_KEY_O2O_ADS, adsMap);
        }
    }


    private static void saveO2oAdsCache(Context context, List<TransVo> transVoList) {
        if (ObjectUtils.isNotNull(transVoList)) {
            BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);

            HashMap<String, HashMap<String, List<O2OAdInfoVo>>> typeMap = new HashMap<>();
            for (TransVo transVo : transVoList) {
                HashMap<String, List<O2OAdInfoVo>> businessMap = new HashMap<>();
                for (TransBusinessAdVo transBusinessAdVo : transVo.transBusinessAdVoList) {
                    businessMap.put(transBusinessAdVo.businessId, transBusinessAdVo.businessAdList);
                    typeMap.put(transBusinessAdVo.businessId, businessMap);
                }
                typeMap.put(transVo.type, businessMap);
            }
            provider.putCache(CACHE_KEY_NEW_O2O_ADS, typeMap);
        }
    }

    public static HashMap<String, List<O2OAdInfoVo>> getO2oAdListCache(Context context, String adType) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
        HashMap<String, HashMap<String, List<O2OAdInfoVo>>> adsMap = provider.getCache(CACHE_KEY_NEW_O2O_ADS, new TypeToken<Map<String, HashMap<String, List<O2OAdInfoVo>>>>() {
        }.getType());
        HashMap<String, List<O2OAdInfoVo>> o2oAdMap = null;
        if (null != adsMap) {
            o2oAdMap = adsMap.get(adType);
        }

        if (null == o2oAdMap) {
            o2oAdMap = new HashMap<>();
        }
        return o2oAdMap;
    }


    /**
     * 根据广告业务id获取广告对象
     *
     * @return
     */
    public static HashMap<String, O2OAdInfoVo> getO2oAdsCache(Context context, String adType) {
        BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);

        HashMap<String, HashMap<String, O2OAdInfoVo>> adsMap = provider.getCache(CACHE_KEY_O2O_ADS, new TypeToken<Map<String, HashMap<String, O2OAdInfoVo>>>() {
        }.getType());

        HashMap<String, O2OAdInfoVo> o2oAdInfoMap = null;
        if (null != adsMap) {
            o2oAdInfoMap = adsMap.get(adType);
        }

        if (null == o2oAdInfoMap) {
            o2oAdInfoMap = new HashMap<>();
        }

        return o2oAdInfoMap;
    }

    /**
     * 加载制定businessId广告到ImageView，如果没有广告，隐藏ImageView
     *
     * @param context
     * @param adType
     * @param adView  return 返回加载成功或者失败
     */
//    public static O2OAdInfoVo loadAdIntoImageView(final Context context, String adType, ImageView adView) {
//        return loadAdIntoImageView(context, adType, adView, null);0
//    }
    public static O2OAdInfoVo loadAdIntoImageView(final Context context, String adType, ImageView adView) {
        final HashMap<String, O2OAdInfoVo> o2oAdsCache = AdOperation.getO2oAdsCache(context, adType);

        if (null == o2oAdsCache || null == o2oAdsCache.get(DEFAULT_BUSINESS_ID) ||
                ObjectUtils.isNull(o2oAdsCache.get(DEFAULT_BUSINESS_ID).url)) {
            ViewUtil.setViewVisibility(View.GONE, adView);
            return null;
        } else {
            ViewUtil.setViewVisibility(View.VISIBLE, adView);
//            setImageViewHW(context, adView, o2oAdsCache.get(DEFAULT_BUSINESS_ID));
            ImageViewExtKt.loadFromUrl(adView, context, R.drawable.seat_adv1026x288, o2oAdsCache.get(DEFAULT_BUSINESS_ID));
//            ImageHelper.with(context).load(o2oAdsCache.get(DEFAULT_BUSINESS_ID).url, R.drawable.seat_adv1026x288).centerCrop(true).into(adView);

            adView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UrlParser.getInstance().parser(context
                            , "leyou://native?native=com.capelabs.leyou.ui.activity.WebViewActivity&url="
                                    + Uri.encode(o2oAdsCache.get(DEFAULT_BUSINESS_ID).link));

                }
            });

            return o2oAdsCache.get(DEFAULT_BUSINESS_ID);
        }

    }


//    /**
//     * 设置广告宽高并显示
//     */
//    private static void setImageViewHW(final Context context, final ImageView imageView, final ImageVo adInfo) {
//        ImageViewExtKt.loadFromUrl(imageView, context, R.drawable.seat_adv1026x288, adInfo);
//        if (imageView != null) {
//            imageView.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (o2OAdInfoVo.width != 0 && o2OAdInfoVo.high != 0) {
//                        float viewWidth = imageView.getMeasuredWidth();
//                        int imgWidth = o2OAdInfoVo.width;
//                        int imgHeight = o2OAdInfoVo.high;
//                        float changedHeight = imgHeight * (viewWidth / imgWidth);
//                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//                        layoutParams.width = (int) viewWidth;
//                        layoutParams.height = (int) changedHeight;
//                        imageView.setLayoutParams(layoutParams);
//                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    }
//                    if (transform != null) {
//                        Glide.with(context).load(o2OAdInfoVo.url)
//                                .placeholder(R.drawable.seat_adv1026x288)
//                                .error(R.drawable.seat_adv1026x288)
//                                .transform(transform)
//                                .into(imageView);
//                    } else {
//                        Glide.with(context).load(o2OAdInfoVo.url)
//                                .placeholder(R.drawable.seat_adv1026x288)
//                                .error(R.drawable.seat_adv1026x288)
//                                .into(imageView);
//                    }
//
//                }
//            });
//        }
//    }
//
//    public static void writeController(Context context, AdHomeTimeModel model) {
//        BaseProvider provider = new SharedPreferencesProvider();
//        provider.getProvider(context).putCache(AD_HOME_CONTROLLER, model);
//    }
//
//    public static AdHomeTimeModel readController(Context context) {
//        BaseProvider provider = new SharedPreferencesProvider();
//        return provider.getProvider(context).getCache(AD_HOME_CONTROLLER, AdHomeTimeModel.class);
//    }
}
