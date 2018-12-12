package com.ichsy.libs.core.dao;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.GsonHelper;

import java.lang.reflect.Type;

/**
 * DAO的基类，负责处理DAO的公共逻辑
 * Created by liuyuhang on 16/5/4.
 */
public abstract class BaseProvider {
    private static final String DEFAULT_UID_KEY = "global";
    private Context context;
    private String uid;

    /**
     * 增
     *
     * @param key
     * @param value
     * @return 是否成功
     */
    protected abstract boolean add(String key, String value);

    /**
     * 删
     *
     * @param key
     * @return 是否成功
     */
    protected abstract boolean delete(String key);

    /**
     * 改
     *
     * @param key
     * @param value
     * @return 是否成功
     */
    protected abstract boolean update(String key, String value);

    /**
     * 查
     *
     * @param key
     * @return 获取的数据
     */
    protected abstract String find(String key);


    /**
     * 初始化
     *
     * @param context
     * @param uid
     */
    protected void init(Context context, String uid) {
        this.context = context;
        this.uid = uid;
    }

    public Provider getProvider(Context context, String uid) {
        if (TextUtils.isEmpty(uid)) {
            return getProvider(context);
        } else {
            return new Provider(context, this, uid);
        }
    }

    /**
     * 获取默认的provider
     *
     * @param context
     * @return
     */
    public Provider getProvider(Context context) {
        return new Provider(context, this, DEFAULT_UID_KEY);
    }

    public Context getContext() {
        return context;
    }

    public String getUid() {
        return uid;
    }

    public class Provider {
        private BaseProvider dao;

        public Provider(Context context, BaseProvider dao, String uid) {
            if (null == context) {
                throw new NullPointerException("context must be no null");
            }
            this.dao = dao;
            this.dao.init(context.getApplicationContext(), uid);
        }

        /**
         * 保存缓存数据
         *
         * @param key
         * @param value
         * @return
         */
        public synchronized boolean putCache(String key, Object value) {
            Object data = dao.find(key);

            String valueString;
            if (value instanceof String) {
                valueString = (String) value;
            } else {
                valueString = GsonHelper.build().toJson(value);
            }

            if (null != data) {
                return dao.update(key, valueString);
            } else {
                return dao.add(key, valueString);
            }
        }

        /**
         * 获取缓存数据
         *
         * @param key
         * @return
         */
        @Nullable
        public String getCache(String key) {
            return dao.find(key);
        }

        /**
         * 获取缓存数据
         *
         * @param key
         * @return
         */
        @Nullable
        public <T> T getCache(String key, Class<T> clz) {
            String valueString = dao.find(key);
            if (TextUtils.isEmpty(valueString)) {
                return null;
            } else {
                return GsonHelper.build().fromJson(valueString, clz);
            }
//            return dao.find(key);
        }

        /**
         * 获取缓存数据
         *
         * @param key
         * @return
         */
        @Nullable
        public <T> T getCache(String key, Type type) {
            String valueString = dao.find(key);
            if (TextUtils.isEmpty(valueString)) {
                return null;
            } else {
                return GsonHelper.build().fromJson(valueString, type);
            }
//            return dao.find(key);
        }
    }

}
