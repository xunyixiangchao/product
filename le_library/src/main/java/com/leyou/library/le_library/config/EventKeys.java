package com.leyou.library.le_library.config;

/**
 * Created by liuyuhang on 16/4/25.
 */
public class EventKeys {
    public static final String EVENT_PUSH_KEY = "event_push_receiver";
    public static final String EVENT_ACTIVITY_LIFE_DELEGATE = "EVENT_ACTIVITY_LIFE_DELEGATE";//activity生命周期处理逻辑

    public static final String EVENT_LOGIN_ON_LOG = "event_login_receiver";//获取登陆广告通知
    public static final String EVENT_LOGIN_OUT_LOG = "event_logout_receiver";//获取登陆广告通知

    public static final String EVENT_CHANGED_HOMEPAGE_POSITION = "event_changed_homepage_position";

    public static final String EVENT_USERINFO_CHANGED_EMAIL = "EVENT_CHANGED_USER_EMAIL";

    public static final String EVENT_CHANGE_SEARCH_KEYWORD = "event_change_search_keyword";

    public static final String EVENT_KEY_SALE_AFTER_SUCCESS = "EVENT_KEY_SALE_AFTER_SUCCESS";


//    public static final String EVENT_PRODUCT_SEARCH_COMMIT = "event_search_check_keyword";

    public static final String EVENT_SUBMIT_ORDER_RECEIVE_TIME = "event_submit_order_receive_time";
    public static final String EVENT_SUBMIT_ORDER_GIFTCARD_INFO = "event_submit_order_giftcard_info";
    public static final String EVENT_SUBMIT_ORDER_INVOICE = "event_submit_order_invoice_info";


    public static final String EVENT_SHOPPING_CART_COUNT_CHANGED = "EVENT_SHOPPING_CART_COUNT_CHANGED";
    public static final String EVENT_ADD_LATEST_ADDRESS = "EVENT_ADD_LATEST_ADDRESS";
    public static final String EVENT_CHOOSE_SHIP_METHOD = "EVENT_CHOOSE_SHIP_METHOD";
    public static final String EVENT_CHOOSE_AREA_DIALOG = "EVENT_CHOOSE_AREA_DIALOG";
    public static final String EVENT_CHOOSE_AREA_RESULT = "EVENT_CHOOSE_AREA_RESULT";
    public static final String SUBMIT_ORDER_ADDRESS = "SUBMIT_ORDER_ADDRESS";
    public static final String EVENT_PAY_SUCCESS_CLOSE_PRE_ACTIVITY = "EVENT_PAY_SUCCESS_CLOSE_PRE_ACTIVITY";
    public static final String EVENT_CHOOSE_SHOP_AREA = "EVENT_CHOOSE_SHOP_AREA";

    public static final String EVENT_PRODUCT_CHOOSE_STANDARD = "EVENT_PRODUCT_CHOOSE_STANDARD";
    public static final String EVENT_ORDER_SEKECT_COUPON = "EVENT_ORDER_SEKECT_COUPON";
    public static final String EVENT_ORDER_SEKECT_FREE_COUPON = "EVENT_ORDER_SEKECT_FREE_COUPON";
    public static final String EVENT_ORDER_SELECT_SHOP = "EVENT_ORDER_SELECT_SHOP";

    // 搜索
    public static final String EVENT_NAME_CATEGORY = "EVENT_NAME_CATEGORY";

    public static final String EVENT_NAME_CATEGORY_CONDITION = "EVENT_NAME_CATEGORY_CONDITION";

    public static final String EVENT_SAVE_SCREEN = "EVENT_SAVE_SCREEN";

    public static final String EVENT_NAME_CATEGORY_CHANGE = "EVENT_NAME_CATEGORY_CHANGE";

    public static final String EVENT_CLASSIFICATION = "EVENT_CLASSIFICATION";

    /**
     * 微信支付成功的回调
     */
    public static final String EVENT_ON_WX_PAY_SUCCESS = "EVENT_ON_WX_PAY_SUCCESS";

    /***
     * 品类
     */
    public static final String EVENT_CHOOSE_CATEGORY_NAME = "EVENT_CHOOSE_CATEGORY_NAME";
    /***
     * 品牌
     */
    public static final String EVENT_CHOOSE_BRAND_NAME = "EVENT_CHOOSE_BRAND_NAME";

    public static final String EVENT_SHOP_FROM_SEARCH = "EVENT_SHOP_FROM_SEARCH";
    public static final String EVENT_SHOP_FROM_FILTER = "EVENT_SHOP_FROM_FILTER";
    public static final String EVENT_FILTER_SELECT_CONFIRM = "EVENT_FILTER_SELECT_CONFIRM";
    public static final String EVENT_DIALOG_DISMISS = "EVENT_DIALOG_DISMISS";

    public static final String EVENT_PUSH_MESSAGE_KEY = "EVENT_PUSH_MESSAGE_KEY";//接受到推送消息
    public static final String EVENT_UPDATE_IDCARD_LAYOUT = "EVENT_UPDATE_IDCARD_LAYOUT";
    public static final String EVENT_IGNORE_INVALID_ORDER = "EVENT_IGNORE_INVALID_ORDER";
    public static final String EVENT_O2O_EVALUATION_SUCCESS = "EVENT_O2O_EVALUATION_SUCCESS";

    public static final String EVENT_EVALUATION_SUCCESS = "EVENT_EVALUATION_SUCCESS";
    public static final String EVENT_EVALUATION_CONFIRM_SUCCESS = "EVENT_EVALUATION_CONFIRM_SUCCESS";

    /***
     * 首页导航栏名称（99click用
     */
    public static final String EVEN_MAIN_TOPIC_NAME = "EVEN_MAIN_TOPIC_NAME";

    public static final String EVENT_CHANGE_LOGIN = "EVENT_CHANGE_LOGIN";

    public static final String EVENT_HOMEPAGE_HOT_SEARCH_CHAGNED = "EVENT_HOMEPAGE_HOT_SEARCH_CHAGNED";

    /**
     * 优惠券
     */
    public static final String EVENT_COUPONS_COUNT = "EVENT_COUPONS_COUNT";
    public static final String EVENT_CHOICE_COUPONS = "EVENT_CHOICE_COUPONS";
    public static final String EVENT_EXCHANGE_COUPONS = "EVENT_EXCHANGE_COUPONS";

    public static final String PRODUCT_BANNER_VIDEO_PAUSE = "PRODUCT_BANNER_VIDEO_PAUSE";
    public static final String PRODUCT_ACTIVITY_VIDEO_PAUSE = "PRODUCT_ACTIVITY_VIDEO_PAUSE";
    public static final String PRODUCT_BANNER_VIDEO_DESTROY = "PRODUCT_BANNER_VIDEO_DESTROY";
    public static final String EVENT_NEAR_STORE_HEIGHT = "EVENT_NEAR_STORE_HEIGHT";
    public static final String EVENT_NEAR_STORE_STAFF_NO_MORE_PAGE = "EVENT_NEAR_STORE_STAFF_NO_MORE_PAGE";
    public static final String EVENT_NEAR_STORE_FAILED = "EVENT_NEAR_STORE_FAILED";
    public static final String EVENT_NEAR_STORE_OFTEN_STORE = "EVENT_NEAR_STORE_OFTEN_STORE";
    public static final String EVENT_BALANCE_UPDATE = "EVENT_BALANCE_UPDATE";

    public static final String EVENT_REQUEST_REFRESH = "EVENT_REQUEST_REFRESH";

}
