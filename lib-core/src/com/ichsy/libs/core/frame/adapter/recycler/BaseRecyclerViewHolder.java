package com.ichsy.libs.core.frame.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ichsy.libs.core.comm.utils.ViewHolder;

/**
 * Created by liuyuhang on 2018/9/4.
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View convertView;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
    }

    public View getConvertView() {
        return convertView;
    }

    public <T extends View> T findViewById(int id) {
        return ViewHolder.get(convertView, id);
    }
}
