package com.ichsy.libs.core.comm.hotfix;

/**
 * Created by liuyuhang on 16/6/28.
 */
public class BaseHotfixVo {

    public String[] version;// 需要修复的版本号
    public String[] channel;// 需要修复的渠道号
    public String[] model;// 需要修复的手机型号
    public String[] network;// 需要修复的网络环境
    public String[] uid;// 需要匹配的uid

    public String dversioncode;// 设备的系统版本
}
