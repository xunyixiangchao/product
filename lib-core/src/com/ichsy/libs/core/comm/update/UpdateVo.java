package com.ichsy.libs.core.comm.update;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * 检测更新的实体类
 * Created by liuyuhang on 16/4/29.
 */
public class UpdateVo implements Serializable {

    public enum UpdateStatus {
        UPDATE,//需要更新
        FORCE,//强制更新
        FREE,//免流量更新
        NONE,//不需要更新
        SILENCE,//静默更新
    }

    /**
     * 更新版本号
     */
    public String version;

    /**
     * 更新标题
     */
    public String title;

    /**
     * 更新描述
     */
    public String description;

    /**
     * 文件的大小
     */
    public long size;

    /**
     * 更新地址
     */
    public String url;

    public
    @DrawableRes
    int iconResId;

    /**
     * 更新状态 - 也就是是否需要更新
     */
    public UpdateStatus updateStatus;

    public UpdateVo() {
        this.updateStatus = UpdateStatus.NONE;
    }

    public UpdateVo(String version, String title, String description, String url, long size, UpdateStatus status, @DrawableRes int iconResId) {
        this.version = version;
        this.title = title;
        this.description = description;
        this.url = url;
        this.size = size;
        this.updateStatus = status;
        this.iconResId = iconResId;
    }

}
