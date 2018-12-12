package com.leyou.library.le_library.config;

/**
 * abtest常量
 * Created by zhaoye on 2017/11/14.
 */

public class TestABConstant {
    /**
     * 购物车
     */
    //实验 1
    public static final String SHOPPINGCART_HASTWOLINETITLE = "hasTwoLineTitle"; //商品名次是否显示两行
    public static final String SHOPPINGCART_CARTCLEARBTN = "cartClearBtn"; // 去结算按钮点击
    public static final String SHOPPINGCART_CARTSTAYTIME = "cartStayTime"; //购物车停留时间
    //实验 2
    public static final String SHOPPINGCART_HASADBANNER = "hasAdBanner"; //购物车是否显示banner
    public static final String SHOPPINGCART_CARTCLEARBTN_BANNER = "cartClearBtn_2"; // 去结算按钮点击
    public static final String SHOPPINGCART_CARTSTAYTIME_BANNER = "cartStayTime_2"; //购物车停留时间
    public static final String SHOPPINGCART_CARTBANNERCLICK = "cartBannerClick"; //购物车banner点击

    //是否支持拆分购物车
    public static final String SHOPPINGCART_AB_SPLITE = "splitSettlement";

    /**
     * 购物车去结算按钮点击
      */
    public static final String SHOPPINGCART_COLLECTION_CLICK_SUBMIT = "purchaseNow_btn";
    /**
     * 弹层_乐友自营去结算按钮点击
     */
    public static final String SHOPPINGCART_COLLECTION_CLICK_SUBMIT_SELF = "purchaseNow_btn1";
    /**
     * 弹层_乐海淘去结算按钮点击
     */
    public static final String SHOPPINGCART_COLLECTION_CLICK_SUBMIT_SEA = "purchaseNow_btn2";
    /**
     * 弹层关闭按钮点击
     */
    public static final String SHOPPINGCART_COLLECTION_CLICK_SUBMIT_CLOSE = "purchaseNow_close";
    /**
     * 由购物车页进入提交订单页
     */
    public static final String SHOPPINGCART_COLLECTION_CLICK_SUBMIT_PUSH_ORDER = "EnterSubmitOrderPage";

    /**
     * 提交订单
     */
    public static final String PLACE_ORDER_PURCHASE_BTN = "purchase_btn";// 提交订单按钮显示文字 1:立即下单 2：去付款
    public static final String PLACE_ORDER_PURCHASE_BTN_NUM = "purchase_btn_num"; //提交订单 立即下单点击次数


    /**
     * 注册
     */
    //实验 1
    public static final String REGISTER_HASOPTIONALTIPS = "hasOptionalTips";//注册推荐是否有选填字样
    public static final String REGISTER_SIGNINSUBMITBTNCLICK = "SigninSubmitBtnClick";//注册第二页按钮点击
    public static final String REGISTER_SIGNINSECONDSTAYNUM = "SigninSecondStayNum";//注册第二页停留时间

    /**
     * 订单详情
     */
    //实验 1
    public static final String PRODUCT_ORDER_HASCONTACTICON = "hasContactIcon";//订单详情页联系客服按钮位置
    public static final String PRODUCT_ORDER_SERVICECONTACT_NUM = "serviceContact_num";//订单详情页联系客服按钮点击次数

    /**
     * 乐友收银台
     */
    public static final String CASHIER_HAS_PRICE = "has_price";// 是否显示收银台头部价格 true：显示 false：不显示
    public static final String CASHIER_PURCHASE_NOW_NUM = "purchase_now_num"; //收银台 立即支付点击次数


    /**
     * 个人中心
     */
    public static final String USER_CENTER_LOGIN_TEST = "der_login";

    public static final String USER_CENTER_LOGIN_GET_CODE = "getCode";
    public static final String USER_CENTER_LOGIN_BACK = "loginBackBtn";
    public static final String USER_CENTER_LOGIN_BUTTON = "loginBtn";
    public static final String USER_CENTER_LOGIN_REG_CLICK = "mineLogin";

    /**
     * 达观推荐
     */
    public static final String PRODUCT_REMIND = "rec_from";
    /**
     * 搜索ab
     */
    public static final String SEARCH_SOURCE = "searchSource2";

    /**
     * 首页商品
     */
    public static final String HOME_PAGE_GOODS="shouye_from";
    public static final String DEFAULT_FLAG = "leyou";
    /**
     * 秒杀商品
     */
    public static final String SECKILL_GOODS="sec_kill_from";
    public static final String SECK_DEFAULT="leyou";

}
