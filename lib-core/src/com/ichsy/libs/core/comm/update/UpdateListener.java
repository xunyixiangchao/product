package com.ichsy.libs.core.comm.update;

/**
 * 检测更新的回调
 * Created by liuyuhang on 16/4/29.
 */
public interface UpdateListener {

    /**
     * 检测更新业务准备期，还未进行检测更新逻辑处理
     */
    void onUpdatePre();

    /**
     * 检测更新业务执行完毕，本次不需要更新或者用户点击下次更新
     */
    void onUpdateSkip();

    /**
     * 检测更新业务执行完毕，用户点击立即更新并且apk文件已经下载文笔
     *
     * @param path 下载好的apk路径（SD卡绝对路径）
     */
    void onUpdateCompleteAndInstall(String path);
}
