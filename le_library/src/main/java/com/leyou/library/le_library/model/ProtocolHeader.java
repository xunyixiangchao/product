package com.leyou.library.le_library.model;

/**
 * 网络请求的协议头(header)
 * Created by liuyuhang on 16/5/31.
 */
public class ProtocolHeader {
    public String message_id;//"流消息编号 流水号，格式为时间戳+序列号：yyyyMMddHHmmssSSSSxxxxxxxxxx"
    public String time_stamp;//请求方时间戳，消息发出时系统的当前时间。格式：当前时间毫秒数
    public String transaction_type;//接口编码
    public String sign;//"签名  签名的内容为token的奇数位+messageID偶数位+timeStamp奇数位+ transaction_type 注：没有令牌的时候token为空字符串"
    public int terminal;//"IOS 安卓 网站 HTML5" "1=IOS 2=安卓 3=网站 4=HTML5"
    public String token;//登陆后的令牌

    //
    public String channel;//渠道
    public String version;//终端版本(1.0.1)
    public String imei;//手机唯一识别号

    public String ua;//手机型号
    public String network_status;//网络状态（1蜂窝、2、wifi）
    public String network_oper;//网络运营商(1、移动2联通3电信4其他)

    public String ip;//ip地址

    public String build_mode;

    ///
    public int res_code;
    public String message;

    public int city_id;
}
