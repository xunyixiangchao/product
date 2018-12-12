package com.ichsy.libs.core.comm.helper;

import android.os.Environment;
import android.text.TextUtils;

/**
 * 项目中唯一的sd控制类
 * Created by liuyuhang on 2017/7/13.
 */

public class SDHelper {
    private static SDHelper helper;

    public static SDHelper getInstance() {
        if (null == helper) {
            helper = new SDHelper();
        }
        return helper;
    }

    private String mBasePath;

    /**
     * @param appPath 设置基础路径，所有路径都是以此路径为基础
     */
    public void init(String appPath) {
        this.mBasePath = appPath;
    }

    public String getOrCreatePath(String path) {
        return getAppPath() + "/" + path;
    }

    public String getAppPath() {
        if (TextUtils.isEmpty(mBasePath)) {
            return getSDPath() + "/";
        } else {
            return mBasePath;
        }
    }

    public String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
