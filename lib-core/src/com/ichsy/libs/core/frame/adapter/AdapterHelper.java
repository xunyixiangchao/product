package com.ichsy.libs.core.frame.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter帮助类，协助处理ListView和RecyclerView
 * Created by liuyuhang on 2017/4/17.
 */

public class AdapterHelper<T> {
    private Context context;
    public List<T> list;

    private LayoutInflater mInflater;

    public AdapterHelper(Context context) {
//        , List<T> list
        this.context = context;
//        this.list = list;
    }

    public LayoutInflater getInflater() {
        if (null == mInflater) {
            mInflater = LayoutInflater.from(context);
        }
        return mInflater;
    }

    public Context getContext() {
        return context;
    }

//    public List<T> getList() {
//        return list;
//    }

    /**
     * 清空数据
     */
    public void clearData() {
        if (null == list) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
    }

    /**
     * 追加数据，一般是用于分页
     *
     * @param data
     */
    public void addData(List<T> data) {
        if (null == data || data.isEmpty()) return;

        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(data);
    }

//    /**
//     * 重新设置数据，一般是用于刷新adapter
//     *
//     * @param data
//     */
//    public void resetData(List<T> data) {
//        clearData();
//        addData(data);
//    }

    public T getItem(int position) {
        T item;
        if (null != list && position != list.size()) {
            item = list.get(position);
            return item;
        }
        return null;
//
//        if (null == list) return item;
//        if (position == list.size()) return item;
//
//        return list.get(position);
    }

    public int getCount() {
        int count;
        if (null == list) {
//            if (null != mAdapterListener) {
//                mAdapterListener.onBeginBindData();
//            }
            count = 0;
        } else {
            count = list.size();
        }
        return count;
    }
}
