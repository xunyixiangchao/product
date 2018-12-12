package com.ichsy.libs.core.frame.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 讲listview转换成gridview的adapter
 * Created by liuyuhang on 2017/5/8.
 */
@Deprecated
public class ListForGridViewAdapter<T> extends BaseFrameAdapter<T> {
    private final
    @IdRes
    int ID_RES_CHILD_START = 101100110;

    public ListForGridViewAdapter(Context context) {
        super(context);
    }

    /**
     * 设置列数
     *
     * @param columns
     */
    public void setColumns(int columns) {

    }

    public
    @IdRes
    int getChildViewId(int position) {
        return ID_RES_CHILD_START + position;
    }

    /**
     * 在adapter的getView中，首次创建view，初始化并返回创建的布局
     *
     * @param position
     * @param inflater
     * @param parent
     * @return
     */
    @Override
    public View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent) {
        LinearLayout rowRootView = new LinearLayout(getContext());
        rowRootView.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 10; i++) {
            LinearLayout rowChildView = new LinearLayout(getContext());
            rowChildView.setId(getChildViewId(i));
            rowChildView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            rowRootView.addView(rowChildView);
        }

        return rowRootView;
    }

    /**
     * 在adapter的getView中，每次滑动listView会循环执行本方法
     *
     * @param position
     * @param item
     * @param convertView
     */
    @Override
    public void onViewAttach(int position, T item, View convertView) {

    }
}
