package com.ichsy.libs.core.comm.provider;

/**
 * Created by liuyuhang on 16/10/19.
 */
@Deprecated
public abstract class BaseDataManager {

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
}
