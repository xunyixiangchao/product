package com.ichsy.libs.core.dao;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * SharedPreferences的DAO存储过程
 * Created by liuyuhang on 16/5/4.
 */
public class SharedPreferencesProvider extends BaseProvider {
    private final String SP_FILE_NAME = "cache_user_data_";

    @Override
    protected boolean add(String key, String value) {
        SharedPreferences.Editor editor = getSP(getUid()).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    @Override
    protected boolean delete(String key) {
        SharedPreferences.Editor editor = getSP(getUid()).edit();
        editor.putString(key, "");
        return editor.commit();
    }

    @Override
    protected boolean update(String key, String value) {
        SharedPreferences.Editor editor = getSP(getUid()).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    @Override
    protected String find(String key) {
        SharedPreferences sp = getSP(getUid());
        return sp.getString(key, "");
    }

    /**
     * 根据用户的uid获取自己的缓存
     *
     * @param uid
     * @return
     */
    private SharedPreferences getSP(String uid) {
        String spName = SP_FILE_NAME + uid;
        return getContext().getSharedPreferences(spName, Activity.MODE_PRIVATE);
    }

}

