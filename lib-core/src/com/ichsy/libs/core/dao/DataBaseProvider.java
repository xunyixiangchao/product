package com.ichsy.libs.core.dao;

/**
 * Created by liuyuhang on 16/5/4.
 */
public class DataBaseProvider extends BaseProvider {

    @Override
    protected boolean add(String key, String value) {
        return false;
    }

    @Override
    protected boolean delete(String key) {
        return false;
    }

    @Override
    protected boolean update(String key, String value) {
        return false;
    }

    @Override
    protected String find(String key) {
        return null;
    }
}
