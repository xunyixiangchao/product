package com.leyou.library.le_library.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 配置接口参数
 * Created by liuyuhang on 16/8/25.
 */
public class LeSetting {
    public String h5_url = "http://m.leyou.com.cn/mall?appVersion=";
    //        public String version;
//        public boolean must_update = false;
    public String tel = "400-666-9888";
    public String about_url = "http://www.leyou.com.cn/mob/secureAndPresent";
    public String characters = "买母婴，来乐友！\\r\\n全国500家门店，品质保真！";
    public String high_light_word = "500家门店";
    public String invoices[] = new String[]{
            "母婴食品",
            "奶粉",
            "纸防尿用品",
            "喂养",
            "洗护安全电器",
            "文体用品",
            "车床椅",
            "服纺"};
    //        public String update_content;
    public String after_service_url = "http://www.leyou.com.cn/special/leyou-sh/index2.php";
    public String after_common_sercice_url = "http://www.leyou.com.cn/special/leyou-sh/index3.php";
    public String after_sea_service_url = "http://www.leyou.com.cn/special/leyou-sh/index4.php";
    public HashMap<String, String> ship_desc;// 快递运费说明
    public List<FlashPostageVo> flash_shipping_reminder;// 闪送运费说明

    public String invoice_note;
    public List<String> kf_talk_name_map = new ArrayList<>();

    public InviteInfo inviteInfo;
    public String o2o_cms_content_url = "";

    public String product_share_url = "http://m.leyou.com.cn/product/single/";//普通商品分享链接
    public String haitao_share_url = "http://m.leyou.com.cn/product/ht_single/";//海淘商品分享链接
    public String guimi_share_url = "http://www.leyou.com.cn/app/download/lyappdownload.php";//邀请闺蜜的分享链接
    public String reg_info_url = "http://www.leyou.com.cn/special/leyou-sh/index.php";//注册协议

    public String my_zoo = "http://quanzi.leyou.com.cn/quan.php?mod=home";//我的圈子
    public String mother_zoo = "http://quanzi.leyou.com.cn/quan.php?mod=index";//乐妈圈
    //       public String download_image = "http://image.leyou.com.cn/images_db/";//下载图片的url
    public String download_le_image = "http://image.leyou.com.cn/images_db/";//新的图片根地址
    public String points_mall = "http://m.leyou.com.cn/benefits?page=integral";//积分商城
    public String coupons_core = "http://m.leyou.com.cn/benefits?page=coupons";//优惠券去领券

    public String vip_info_url = "http://app.leyou.com.cn/activity/vipCheck";

    public HashMap<String, String> bus_mapping;
    public List<ActiveVo> index_actives; // 首页摇金币

    public KfSetting kfSetting;
    public boolean return_switch = true;

    public List<QrShopVo> shop_info_list = new ArrayList<>();

    public HotSearch[] hot_search;
    //秒杀分享
    public ShareVo seckill_share_info;

    public HomePageActivityInfo home_page_activity_info = new HomePageActivityInfo();

    /**
     * 微信小程序的key
     */
    public String wx_program_username = "gh_77c7a1706f05";
    /**
     * 微信小程序的路径
     */
    public String wx_program_propath = "pages/details/index?sku=";

    public String daguan_send = "http://47.92.10.168:50013/data/leyou";

    public static class KfSetting {
        public HashMap<String, String> kf_talk_android_map;
    }

    /**
     * 乐友热词对象（大家都在搜）
     */
    public static class HotSearch {
        /**
         * 展示内容 大家都在搜：清明必备清单
         */
        public String search_display;
        /**
         * 1:文字搜索 2：链接搜索
         */
        public int search_type;
        /**
         * 搜索的关键词或者链接，如果是链接，增加http://
         */
        public String search_content;

    }

    public static class InviteInfo {
        public String title;
        public String content;
        public String url;
        public String ico;
    }

    public static class HomePageActivityInfo {
        /**
         * 活动标题
         */
        public String activity_title;

        /**
         * 活动链接
         */
        public String activity_url;

        /**
         * 活动类型 1：原生 2：h5
         */
        public int activity_type = 1;
    }

    /**
     * 商品详情最下部图片
     */
    public String product_details_url;

}
