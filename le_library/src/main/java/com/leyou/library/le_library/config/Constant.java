package com.leyou.library.le_library.config;

import com.leyou.library.le_library.config.LeConstant;

/**
 * app全局常量
 * Created by liuyuhang on 16/4/25.
 */
public class Constant extends LeConstant {
    public static final String PASSWORD_REGEX = "(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\\w~!@#_$%*-=+)(^><?.\\^&*?]+$";

    //    public static final String DB_NAME = "note";
    public static final String REGISTER_PHONE = "registerPhone";
    public static final String VERIFICATION_SMS = "verificationSms";
    public static final String VERIFICATION_SMS_TOKEN = "verificationSmsToken";
    public static final String FIND_TOKEN = "findToken";
    public static final String FIND_MOBILE = "findMobile";
    public static final String FIND_EMAIL = "findeMail";
    //    public static final String SAVE_PRICE = "savePrice";
//    public static final String SAVE_CATEGORY = "saveCategory";
//    public static final String SAVE_BRAND = "saveBrand";
    public static final String SALESDESCEND = "salesDescend";
    public static final String PRICEDESCEND = "priceDescend";
    public static final String NEWDESCEND = "newDescend";
    public static final String COMPREHENSIVEDESCEND = "comprehensiveDescend";
    public static String AGE_BRACKET = "";

    /**
     * 地址默认值
     */
    public static class Location {
        public static int DEFAULT_AREA_ID = 110100;
        public static String DEFAULT_CITY_NAME = "北京市";
    }

    public static class Interface {

        public static final String URL_GET_SHAKE_LOTTERY = "user/shakeLottery/";
        public static final String URL_CAPTURE_SCAN_QR_CODE = "system/scanQrCode/";
        public static final String URL_SIGN_GET_MESSAGE = "user/getSignInfo/";
        public static final String URL_UPDATE_CALENDAR_RECEIVE_STATUS = "user/updateReceiveSignMsgStatus/";
        public static final String URL_SYSTEM_AR_SOURCE = "system/getArSource/";
        public static final String URL_AR_UPLOAD_SOURCE = "activity/uploadImg";
        public static final String URL_USER_STAFFER_SET_RELATION = "user/setUserStaffRelation/";
        public static final String URL_USER_STAFFER_GET_RELATION = "user/getUserStaffRelation/";
        public static final String URL_FETCH_GUIDER_RECOMMEND_LIST = "product/getProductStaffs/";
        public static final String URL_FETCH_GUIDER_RECOMMEND_LABEL = "product/getProductLabels/";
        public static final String URL_IM_GUIDE_MEMBER_LIST = "im/getGroupMembers/";
        public static final String URL_IM_FOLLOW_GUIDER = "im/followStaff/";
        public static final String URL_IM_INFO_INIT = "im/imInitialize/";
        public static final String URL_IM_REMOVE_GUIDER = "im/removeFriend/";
        public static final String URL_IM_SEND_RECEIVED_MESSAGE = "im/sendImMsg/";

    }

    public class API {
        public static final String URL_PUT_VOICE_VALIDATE_CODE = "user/putVoiceValidateCode/";
        public static final String URL_CHECK_USER_ACTION = "user/checkUserAction/";
        public static final String URL_O2O_GET_RED_PACKET = "redpacket/getOrderRedpacket";
        public static final String URL_SEND_RED_ENVELOPS = "/orderPaySuccess/sendRedenvelopes/";
        public static final String URL_ORDER_SHARE_INFO = "orderPaySuccess/orderShareInfo/";
        public static final String URL_ORDER_SMGORDER = "smgorder/getOrderPayInfo/"; //扫码购 检验码
        public static final String URL_ORDER_INVOICE_URL = "invoice/loadInvoice/";
        public static final String URL_PRODUCT_HOTKEYS = "product/searchKeyword/";
        public static final String URL_PRODUCT_AUTO_WORD = "solr/product/getSuggest/";
        public static final String URL_USER_POINT_DATA = "point/getPointDetails/";
        public static final String URL_CATEGORY_GETALLDATA = "solr/product/categoryFacet/";
        public static final String URL_CATEGORY_AGE = "solr/product/age_detail/";

        public static final String URL_SUBMIT_GIFTCARD = "gift/getGiftCart/";
        public static final String URL_KEYWORD_FILTER = "solr/product/productQuery/";
        public static final String URL_GET_ADDRESS = "user/getAddresses/";
        public static final String URL_DELETE_ADDRESS = "user/deleteAddress/";
        public static final String URL_GET_WX_PAY_REQ = "pay/getWxPayReq/";
        public static final String URL_ADD_ADDRESS = "user/postAddress/";
        public static final String URL_DEFAULT_ADDRESS = "user/updateDefaultAddress/";

        public static final String URL_AD_O2O_ALL = "content/getAdvertisement/";
        public static final String URL_HOMEPAGE_GETINDEX = "content/getIndex/";
        /**
         * 获取宝宝信息数据
         */
        public static final String URL_HOMEPAGE_GET_BABYINFO = "/content/getBabyInfo/";
        public static final String URL_HOMEPAGE_ACTIVITY_INDEX_DATA = "content/getT1Index/";
//        public static final String URL_HOMEPAGE_GETINDEX = "content/getIndexForView/";//测试首页地址

        public static final String URL_HOMEPAGE_MAY_LIKE = "content/getLikeInfo/";
        public static final String URL_HOMEPAGE_FUNCTIONAL = "content/getFunctional/";
        public static final String URL_HOMEPAGE_BRAND_RECOMMEND = "content/getBrandRecommend/";
        //秒杀
        public static final String URL_HOMEPAGE_GET_SECKILL_INFO = "content/getSeckillInfo/";
        // 秒杀
        public static final String URL_POST_SECKILL_INFO = "skillProduct/getSkillProduct/";
        //秒杀提醒
        public static final String URL_POST_SECKILL_REMIND = "skillProduct/getSkillProductRemind/";
        //常购清单
        public static final String URL_ALWAYS_BUY_LIST = "content/getAlwaysBuyList/";
        //门店详情
        public static final String URL_NEAR_STORE_DETAIL = "staff/getStoreStaffs/";
        public static final String URL_NEAR_STORE_ACTIVITY = "user/getMessageMainSideShop/";
        public static final String URL_ORDER_EVALUATE_SUBMIT = "product/addComments/";
        public static final String URL_AD_GET_ERP = "https://service.leyou.com.cn/index.php?a=flashshopping&m=getShanGouPidApp&bid=";
        public static final String URL_AD_O2O_ORDER = "https://service.leyou.com.cn/index.php?a=flashshopping&m=getDataByBidForJson&bid=212";
        public static final String URL_SIGN_IN = "user/signIn/";
        public static final String URL_SIGN_INFO = "user/signInfo/";

        public static final String URL_USER_INFO_INDEX = "my/getUserInfoIndex/";
        public static final String URL_CHECK_IDCARD = "haitaoOrder/checkIdcard/";
        public static final String URL_ORDERINFO_REFRESH_SEA = "haitaoOrder/anyHaitaoInfo/";
        public static final String URL_SUBMIT_ORDER_SEA = "haitaoOrder/submitHaiTaoOrder/";

        /**
         * 删除订单接口
         */

        public static final String URL_ORDER_DELETE = "order/delMyOrderBySerialNum/";
        public static final String URL_ORDERINFO_REFRESH = "order/anyOrderInfo/";
        public static final String URL_SUBMIT_ORDER = "order/anySubmitOrder/";

//        public static final String URL_ORDER_DELETE = "neworder/delMyOrderBySerialNum/";
//        public static final String URL_ORDERINFO_REFRESH = "neworder/anyOrderInfo/";
//        public static final String URL_SUBMIT_ORDER = "neworder/anySubmitOrder/";

        public static final String URL_SCAN_CODE_ORDERINFO_REFRESH = "smgorder/anyOrderInfo/";
        public static final String URL_SCAN_CODE_SUBMIT_ORDER = "smgorder/anySubmitOrder/";

        public static final String URL_PRODUCT_GET_STATIC = "lyproduct/getProductStaticInfo/";
        public static final String URL_PRODUCT_GET_DYNAMIC = "lyproduct/getProductDynamicInfo/";
        public static final String URL_PRODUCT_GET_EXTRA = "lyproduct/getProductImageInfo/";

        public static final String URL_PRODUCT_GET_PRODUCT = "product/getProduct/";
        public static final String URL_PRODUCT_GET_REVIEWS = "product/getReviews/";
        public static final String URL_PRODUCT_GET_RECOMMEND = "product/getBIRecommend/";
        public static final String URL_PRODUCT_FETCH_RECOMMEND = "hotlist/daGuanHotlistRecomment/";
        public static final String URL_PRODUCT_GET_RELATED_INFO = "product/getRelatedSkuInfo/";
        public static final String URL_PRODUCT_SET_ARRIVAL_REMINDING = "product/setUpReminder/"; //商品详情页 设置到货提醒


        public static final String URL_GET_ORDERS_BY_STORE = "myorder/getMySerialOrdersByStore/";
        public static final String URL_ORDER_GET_ORDERS = "myorder/getMySerialOrdersByApp/";
        public static final String URL_ORDER_SUBMIT_GET_SHOPS = "oto/areaShopStock/";
        public static final String URL_GET_NEAR_STORE_BANNER = "content/getNearbyShopImg/";
        public static final String URL_USER_GET_NEAR_SHOPS = "user/getLocationShops/";
        public static final String URL_ORDER_SUBMIT_CHOOSE_CITY_SHOPS = "user/getAdderssShops/";
        public static final String URL_USER_GET_FAVORITE_PROD = "my/getProductMarkList/";
        public static final String URL_ALL_ADDRESS = "user/getLevelAddress/";
        public static final String URL_USER_GET_ZITI_LIST = "user/getLevelAddressTwo/";
        public static final String URL_GET_ZHIPAY_INFO = "orderPay/zhifuPay/";
        public static final String URL_GET_ORDER_DETAIL = "myorder/getMyOrderInfo/";
        public static final String URL_GET_SERIAL_DETAIL = "myorder/getMySerialOrderInfo/";
        public static final String URL_PRODUCT_GET_ORDER_RECOMMEND = "product/getOrderRecommend/";
        public static final String URL_ORDER_GET_PAY_AMOUNT = "orderPay/getOrderMoney/";
        public static final String URL_ORDER_POST_PAY_SUCCESS = "verification/orderPayVerificationStatus/"; //支付成功通知后台

        public static final String URL_POST_NEAR_SHOP_SETTING_DEFAULT = "user/updateOftenShop/"; //设置默认门店

        public static final String URL_SYSTEM_USER_CHANNEL_SRC = "sys/getUserSrc";//获取推广渠道
        public static final String URL_SYSTEM_USER_CHANNEL_SRC_SAVE = "sys/putUserSrcDb";//保存推广渠道

        //发票相关
        public static final String URL_INVOICE_MOTIFY = "userInvoiceBase/modifyUserInvoice/";//新建编辑发票信息
        public static final String URL_INVOICE_DELETE = "userInvoiceBase/deleteInvoiceBase/";//删除发票信息
        public static final String URL_INVOICE_PULL_LIST = "userInvoiceBase/listUserInvoice/";//删除发票信息

        public static final String URL_PRODUCT_EVALUATE_LIST = "product/getReviews/"; //商品评论列表
        public static final String URL_EVALUATE_SUBMIT = "product/addComments/"; //商品评论列表

        public static final String URL_CONFIRM_RECEIPT = "refund/confirmRecept/";

        public static final String URL_SCAN_FETCH_SHOP_LIST = "oto/getSmgShopInfo/";

        public static final String URL_POST_COUPON_RECEIVE = "coupons/getFreeCoupons/"; //领券中心领取优惠券
        public static final String URL_POST_COUPON_RECEIVE_LIST = "coupons/getFreeCouponsList/"; //领券中心优惠劵列表


        /**
         * 预售单订单详情
         */
        public static final String URL_GET_PRESELL_ORDER_DETAIL = "myorder/getMyPreOrderInfo/";

    }
}
