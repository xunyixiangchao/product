package com.leyou.library.le_library.config;

/**
 * 公共code定义
 * Created by liuyuhang on 2016/12/5.
 */

public class LeConstant {
    public static final int PAGE_SIZE = 20;
    public static final String APP_DEFINE_NAME = "leyou";

    public static class BuildType {
        public static String buildMode;

        //前期测试版
        public static final String BUILD_MODE_ALPHA = "ALPHA";
        //内部测试版
        public static final String BUILD_MODE_BETA = "BETA";
        //预生产版
        public static final String BUILD_MODE_RC = "RC";
        //百度用rc环境
        public static final String BUILD_MODE_RC_BAIDU = "RC_BAIDU";
        //生产带log版
        public static final String BUILD_MODE_RELEASE_LOG = "RELEASE_LOG";
        //生产版
        public static final String BUILD_MODE_RELEASE = "RELEASE";
    }

    public static class KEYS {
        public static final String PUSH_BAIDU = "d2VmhL06tGbQgTEtVgfo5jkw";
        public static int PUSH_KEY = 1251176880;

        public static String PUSH_XIAOMI_ID = "2882303761517253441";
        public static String PUSH_XIAOMI_KEY = "5711725322441";

        //环信key
        public static String HX_APP_KEY = "leyou-app#leyou-daogoubao-product";
        //腾讯bugly
        public static String BUGLY_KEY = "900031852";
        public static String BUG_TAG_KEY = "6438506aab391a92860ae94ab70ce3cb";

        public static final String WECHAT_APPID = "wxc21c1cf457c76368";
        //        public static String WXAPPID = "wxedcdbc0ec6a11f1d";
//        public static final String WEIXIN_APPID = "wxc21c1cf457c76368";
        public static final String WEIXIN_APPSECRET = "d7c52171502bb87d7e90a9e2c2dca16d";
        public static final String QQ_APPID = "1105430307";
        public static final String QQ_APPSECRET = "1IeawpBptVujyoHV";
        //        public static final String WEIXIN_MINI = "gh_77c7a1706f05";
        public static final String SINA_APPID = "44095716";
        public static final String SINA_APPSECRET = "80e32393c5ab5f7160aada5264a4813c";
        public static final String SINA_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        //testin 正式key
        public static final String TESTIN_KET = "TESTIN_a7f614399-d20d-4286-a5d2-0e9c424b8791";
        public static final String TD_APP_ID = "3FD3D31AE93447378852BEC3E530D11A";

        public static final String NBS_SDK_KEY = "60da6b2b4fcb4bebbd44984c42fefaf9";

        //        public static String WXAPPID = "wxedcdbc0ec6a11f1d";
    }

    public static class N_TALK {
        public static String SITE_ID = "kf_9021";
        public static String SDK_KEY = "7816491D-5AA4-4813-8651-27D4597DD911";
        /**
         * kf_9021_1501812695809 是正式组客服组， 测试组 kf_9021_1501812286066
         */
        public static String SETTING_ID = "kf_9021_1501812695809";
        /**
         * kf_9021_1501812695809 是正式组客服组， 测试组 kf_9021_1501812286066
         */
        public static String TEST_ID = "kf_9021_1501812286066";
    }

    public static class LOCATION {
        public static String LONGITUDE = "116.499639";
        public static String LATITUDE = "39.921001";
    }

    public static class CODE {
        /**
         * 请求成功
         */
        public static int CODE_OK = 0;

        public static int CODE_SERVER_NO_RESPONSE = -1;

        public static int CODE_SERVER_ERROR = -2;

        /**
         * token验证失败，需要重新登陆
         */
        public static int CODE_TOKEN_FAILED = 900001;
    }

    /**
     * url的配置文件
     */
    public static class UrlConstant {
        // 正式环境
//		public static String URL_BASE = "http://webapi.leyou.com.cn/leyou-gateway-services/";
        /**
         * PHP接口
         */
        public static String USER_URL_BASE = "https://app.leyou.com.cn/";

        // 测试环境
//        public static String URL_BASE = "http://192.168.98.215/leyou-gateway-services/";
//        public static String USER_URL_BASE = "http://182.50.112.20:8081/";

        // 预上线环境
//        public static String URL_BASE = "http://115.182.12.249:8081/leyou-gateway-services/";
//        public static String USER_URL_BASE = "http://app.leyou.com.cn/";

        // 圈子
//        public static String URL_CIRCLE = "http://quanzi.leyou.com.cn/quan.php?mod=home";
//        public static final String URL_HOMEPAGE = "http://m.leyou.com.cn/mall?appVersion=";
//        public static final String URL_HOMEPAGE = "http://demom.leyou.com.cn?appVersion=";
//        public static final String URL_CATEGORY = "http://m.leyou.com.cn/mall/category";
//        public static final String URL_LEMA_GROUP = "http://quanzi.leyou.com.cn/quan.php?mod=index";

    }

    public static class API {
        /**
         * b2c服务器
         */
        public static String URL_BASE = "http://webapi.leyou.com.cn/leyou-gateway-services/";
        /**
         * B2C  O2O服务器
         */
        public static String URL_BASE_O2O = "http://o2oservice.api.leyou.com.cn/lyo2o-app-gateway/";

//        public static final String SENSOR_API_URL = "http://leyoutest.cloud.sensorsdata.cn:8006/sa?project=default&token=a20cdde44436ddb7";

//        public static String SENSOR_API_URL = "http://115.47.154.197:8106/sa?project=default";// 测试
//        public static String SENSOR_API_URL = "http://115.47.154.197:8106/sa?project=production";//生产

//        public static final String SENSOR_API_URL = "http://bi.jmleyou.com:8106/sa?project=default";// 测试
//        public static final String SENSOR_API_URL = "http://bi.jmleyou.com:8106/sa?project=production";// 生产

//        public static final String SENSOR_API_URL = "http://bi.ruiyunit.com:8106/sa?project=default";// 测试临时
//        public static final String SENSOR_API_URL = "http://bi.ruiyunit.com:8106/sa?project=production";// 生产临时


        //        o2oservice.api.leyou.com.cn
        public static final String URL_SHOPPINGCART_SYNC_SHOPPINGCART_PRODUCT = "shoppingcart/synShoppingCart/";
        public static final String URL_SHOPPINGCART_SYNC_SHOPPINGCART = "shoppingcart/synAllShoppingCart/";
        public static final String URL_SHOPPINGCART_GETALLDATA = "shoppingcart/getCartAllData/";
        public static final String URL_SHOPPINGCART_QUICK_PULL_DATA = "smgShoppingcart/getCartAllData/";
        public static final String URL_SHOPPINGCART_QUICK_SYNC_DATA = "smgShoppingcart/synShoppingCart/";
        public static final String URL_POST_SHOPPING_CARD = "shoppingcart/postShoppingCart/";
        public static final String URL_SHOPPINGCART_GET_COUNT = "shoppingcart/getCartCount/";

        public static final String URL_USER_ADD_FAVORITE_PROD = "my/addMyAttention/";
        public static final String URL_USER_ADD_FAVORITE_PRODS = "my/addMyAttentions/";
        public static final String URL_USER_REMOVE_FAVORITE_PROD = "my/removeMarkProduct/";

        public static final String RUL_SCENE_DETAIL = "/solr/product/scene_detail";
        public static final String URL_O2O_ORDER_INFO = "order/anyOrderInfo/";
        public static final String URL_O2O_ORDER_SUBMIT = "order/submitOrderInfo/";

        public static final String URL_O2O_LOCATION_PULL = "sys/getCityInfo/";//地址获取

        //投诉接口
        public static final String URL_COMPLAINTS = "my/postComplaint/";
        public static final String URL_GLOBAL_SETTING = "my/getAppInfo/";
        public static final String URL_OTO_TENANT_GET_DETAIL = "merchant/getMerchantDesc";
        public static final String URL_OTO_TENANT_GET_COMMENT_LIST = "comment/getShopCommentList";
        public static final String URL_OTO_TENANT_GET_REPORT = "merchant/putReportShop";
        public static final String URL_OTO_PRODUCT_GET_DETAIL = "product/getProductInfo";
        public static final String URL_OTO_COMMENT_SET_BETTER = "comment/putSetCommentBetter";
        public static final String URL_OTO_ORDER_GET_MONEY = "order/getOrderMoney";
        public static final String URL_OTO_ORDER_GET_PAY_INFO = "orderPay/zhifuPay";

        public static final String URL_O2O_MERCHANT_INFO = "merchant/getMerchantInfo";
        public static final String URL_O2O_MERCHANT_PRODUCT_LIST = "product/getProductList";
        // o2o首页
        public static final String URL_O2O_MAIN_STORES = "merchant/getMerchantList";
        // 订单列表
        public static final String URL_O2O_ORDER_LIST = "order/getOrderList";
        //我的预约
        public static final String URL_O2O_MY_BOOKING_LIST = "booking/getMyBookingList";
        // 订单详情页
        public static final String URL_O2O_ORDER_DETAIL = "order/getOrderInfo";

        // 退款
        public static final String URL_O2O_ORDER_REFUND = "order/putOrderRefund";

        public static final String URL_O2O_ORDER_COMMENT = "comment/putEvaluate";

        public static final String URL_O2O_PRODUCT_GET_INFO = "product/getProductImageDesc";

        public static final String URL_O2O_GET_OOS_CONFIG = "sys/getOssConfig";
        //特色展示详情
        public static final String URL_O2O_GET_SPECIAL_INFO = "product/getSpecialProductInfo/";
        //特色展示列表
        public static final String URL_O2O_GET_SPECIAL_LIST = "product/getSpecialProductList/";

        //亲子乐园首页
        public static final String URL_EDUCATION_JAVA_INFO = "rebateIndex/index/";
        public static final String URL_EDUCATION_PHP_INFO = "eduApi/index";

        //用户提现首页
        public static final String URL_POST_WITHDRAWAL_INFO = "transfer/selectUserWithdrawInfo/";
        //用户提交认证信息
        public static final String URL_POST_USER_AUTH_INFO = "transfer/submitUserAuthenticaInfo/";
        //用户绑定卡信息
        public static final String URL_POST_BINDING_USER_CARD = "transfer/bundingUserCardNumber/";
        //用户提现记录
        public static final String URL_POST_WITHDRAWAL_RECORD = "transfer/selectWithdrawHistoyByUserId/";
        //提现
        public static final String URL_POST_WITHDRAWAL = "transfer/alipayTransferServer/";
        //获取昵称和头像
        public static final String URL_GET_USER_EXTEND_INFO = "user/getUserExtendInfo/";
        //修改昵称和头像
        public static final String URL_GET_UPDATE_USER_INFO = "user/updateUserShowInfo/";
        // 领取小红花
        public static final String URL_EDUCATION_FETCH_AWARD = "eduApi/getMoney";
        // 获取签到信息
        public static final String URL_CALENDAR_GET_INFO = "user/getSignNew";
        // 签到
        public static final String URL_CALENDAR_SIGN_IN = "user/signNew";
        //补签
        public static final String URL_CALENDAR_RETROACTIVE = "user/signRepair";
        // 获取优惠券
        public static final String URL_CALENDAR_GET_COUPONS = "user/getCoupons";
        // 获取签到礼物
        public static final String URL_CALENDAR_GET_GIFT = "user/getCouponsNew";

        public static final String URL_PRE_SELL_ORDER = "preSaleOrder/settlementOrder";
        public static final String URL_SUBMIT_PRE_SELL_ORDER = "preSaleOrder/preSaleSubmitOrder";

        //达观推荐的商品列表
        public static final String URL_PRODUCT_RECOMMEND = "daGuanRecomment/daGuanLikeRecomment/";

        public static class ShoppingCart {
            public static final String URL_MERGE_SHOPPING_CART = "shoppingcart/mergeShoppingCart/";
        }

        public static class MessageCenter {
            public static final String URL_MESSAGE_GET = "user/getMessage/";
            public static final String URL_MESSAGE_CENTER_LAST_MESSAGE = "user/getMessageMain/";

            public static final String URL_MESSAGE_STATUS_GET = "user/getUserMsgStatus/";
            public static final String URL_MESSAGE_STATUS_SET = "user/setUserMsgStatus/";
        }

        public static class Order {
            public static final String URL_ORDER_CANCEL = "order/cancelorder/";
        }

        public static class Vip {
            public static final String URL_VIP_BUY = "expressOrder/expressSubmitOrder/";
        }

        public static class Coupon {
            /**
             * 激活优惠券
             */
            public static final String URL_COUPON_GET = "coupons/putCouponsByCodeNum/";
            /**
             * 领取优惠券
             */
            public static final String URL_COUPON_GET_FREE = "coupons/getFreeCoupons/";
            /**
             * 领取优惠券
             */
            public static final String URL_COUPON_GET_BY_POINT = "point/putCouponsByPoint/";
        }

        public static class SaleAfter {
            public static final String URL_SALE_AFTER_LIST = "refund/queryRefundApplicationList";
            public static final String URL_SALE_AFTER_INFO = "refund/queryRefundApplicationInfo";
            public static final String URL_SALE_AFTER_SUBMIT = "refund/addRefundApplicationInfo";
        }
    }

    public static class Course {
        public static final String URL_DETAIL = "eduCourse/getEduCourseInfo/";
        public static final String URL_SHARE_URL = "eduApi/shareUrl/";

    }
}

