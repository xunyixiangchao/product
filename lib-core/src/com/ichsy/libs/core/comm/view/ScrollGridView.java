package com.ichsy.libs.core.comm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 可以放在listview中的gridview
 * Created by Dongzhiheng on 2016/6/8.
 */
public class ScrollGridView extends GridView {

    public ScrollGridView(Context context) {
        super(context);
    }

    public ScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}