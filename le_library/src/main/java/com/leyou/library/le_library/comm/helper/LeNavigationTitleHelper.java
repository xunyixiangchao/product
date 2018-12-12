package com.leyou.library.le_library.comm.helper;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * 保存标题信息的工具类
 * Created by liuyuhang on 16/7/24.
 */
public enum LeNavigationTitleHelper {

    INSTANCE;

    private HashMap<String, String> mTitleMap;

    LeNavigationTitleHelper() {
        mTitleMap = new HashMap<>();
    }

    public void putTitle(Object clazz, String title) {
        mTitleMap.put(clazz.getClass().getName(), title);
    }

    public String getTitle(Object clazz) {
        return mTitleMap.get(clazz.getClass().getName());
    }

    public String getTitleIsNone(Object clazz) {
        String title = getTitle(clazz);
        if (TextUtils.isEmpty(title)) {
            return clazz.getClass().getName();
        } else {
            return title;
        }
    }
}
