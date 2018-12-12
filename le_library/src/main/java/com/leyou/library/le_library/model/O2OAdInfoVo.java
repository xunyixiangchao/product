package com.leyou.library.le_library.model;

/**
 * o2o的广告对象
 * Created by liuyuhang on 2017/5/10.
 */

public class O2OAdInfoVo extends ImageVo {
    public static final String O2O_AD_TYPE_1 = "10011001";//首页插屏广告
    public static final String O2O_AD_TYPE_2 = (Integer.valueOf(O2O_AD_TYPE_1) + 1) + "";//签到页广告
    public static final String O2O_AD_TYPE_3 = (Integer.valueOf(O2O_AD_TYPE_2) + 1) + "";//我的乐友登陆和未登录广告
    public static final String O2O_AD_TYPE_4 = (Integer.valueOf(O2O_AD_TYPE_3) + 1) + "";//我的订单广告
    public static final String O2O_AD_TYPE_5 = (Integer.valueOf(O2O_AD_TYPE_4) + 1) + "";//分类导航顶部广告和底部广告,里面根据type又返回对应不同的广告
    public static final String O2O_AD_TYPE_6 = (Integer.valueOf(O2O_AD_TYPE_5) + 1) + "";//占个坑
    public static final String O2O_AD_TYPE_7 = (Integer.valueOf(O2O_AD_TYPE_6) + 1) + "";//未注册用户广告提醒

    /**
     * 注册落地页图片
     */
    public static final String O2O_AD_TYPE_8 = (Integer.valueOf(O2O_AD_TYPE_7) + 1) + "";

    /**
     * 订单支付完成广告
     */
    public static final String O2O_AD_TYPE_9 = (Integer.valueOf(O2O_AD_TYPE_8) + 1) + "";
    /**
     * 扫码购订单支付完成广告
     */
    public static final String O2O_AD_TYPE_10 = (Integer.valueOf(O2O_AD_TYPE_9) + 1) + "";

    /**
     * 签到成功弹窗广告
     */
    public static final String O2O_AD_TYPE_11 = (Integer.valueOf(O2O_AD_TYPE_10) + 1) + "";
    /**
     * 签到页新客任务入口
     */
    public static final String O2O_AD_TYPE_12 = (Integer.valueOf(O2O_AD_TYPE_11) + 1) + "";
    /**
     * 购物车 广告页
     */
    public static final String O2O_AD_TYPE_13 = (Integer.valueOf(O2O_AD_TYPE_12) + 1) + "";
    /**
     * 启动页广告
     */
    public static final String O2O_AD_TYPE_14 = (Integer.valueOf(O2O_AD_TYPE_13) + 1) + "";


//    public static final String O2O_AD_BUSINESS_ID_6 = (Integer.valueOf(O2O_AD_BUSINESS_ID_5) + 1) + "";//品牌导航底部广告,里面根据type又返回对应不同的广告
//    public static final String O2O_AD_BUSINESS_ID_7 = O2O_AD_BUSINESS_ID_6 + 1;
//    public static final String O2O_AD_BUSINESS_ID_8 = O2O_AD_BUSINESS_ID_7 + 1;

    public String type;//类型
    public String business_id;//业务ID

}
