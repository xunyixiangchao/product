package com.leyou.library.le_library.model;

import java.net.URLEncoder;

/**
 * 分享对象
 * Created by ss on 2017/2/28.
 */
public class ShareVo {
    public String share_title;
    public String share_image_url;
    public String share_content;
    public String share_link;

    /**
     * 是否可分享
     */
    public boolean enable;

    public String toUrl() {
        String action = "";
        try {
            action = "leyou://action"
                    + "?action=openShare"
                    + "&img=" + URLEncoder.encode(share_image_url, "utf-8")
                    + "&title=" + URLEncoder.encode(share_title, "utf-8")
                    + "&content=" + URLEncoder.encode(share_content, "utf-8")
                    + "&params=" + URLEncoder.encode(share_link, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }


}
