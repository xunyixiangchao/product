package com.ichsy.libs.core.frame.adapter.delegate;

import java.util.List;

/**
 * Created by liuyuhang on 2018/9/4.
 */

public interface AdapterDataDelegate<T> {
    /**
     * 重新设置数据，一般是用于刷新adapter
     *
     * @param data
     */
    void resetData(List<T> data);

    /**
     * 追加数据，一般是用于分页
     *
     * @param data
     */
    void addData(final List<T> data);

    /**
     * 获取adapter中的数据
     *
     * @return
     */
    List<T> getData();

    /**
     * 清空数据
     */
    void clearData();
}
