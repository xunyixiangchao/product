package com.leyou.library.le_library.comm.grand.modle.request;

/**
 * x平台管理
 * <p>
 * Created by ss on 2018/7/31.
 */
public class XOtmRequest {
    public String userID;// (必填)	varchar	用户ID，用户唯一识别码，支持数字或字符串，如果未登录上传设备号
    public String obj_id;// (必填)	varchar	SKU
    public String dimensional_code;// (必填)	varchar	该用户行为的维度代码，在X智能后台中取得
    public String word;//	varchar	商品名
    public String category;// 	text	六级分类名称
    public String cateid;//	varchar	分类ID，格式：2级分类_4级分类_6级分类xx_xx_xx，从左到右分别为主、子分类关系
    public String mac_addr;//	varchar	设备号，用户当前MAC地址，用于识别唯一用户
    public String log_ip;//	varchar	当前行为所处的IP地址，用途同上。格式：0.0.0.0
}
