package com.ichsy.libs.core.comm.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


public class BaseRecyclerView extends RecyclerView {

    /**
     * 记录当前第一个View
     */
    private View mCurrentView;

    private OnItemScrollChangeListener mItemScrollChangeListener;

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            /**
             * 滚动时，判断当前第一个View是否发生变化，发生才回调
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View newView = getChildAt(0);

                if (mItemScrollChangeListener != null) {
                    if (newView != null && newView != mCurrentView) {
                        mCurrentView = newView;
                        mItemScrollChangeListener.onChange(mCurrentView, getChildPosition(mCurrentView));

                    }
                }
            }
        });
    }

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener) {
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mCurrentView = getChildAt(0);

        if (mItemScrollChangeListener != null) {
            mItemScrollChangeListener.onChange(mCurrentView, getChildPosition(mCurrentView));
        }
    }

//    @Override
//    public void onScrollStateChanged(int arg0) {
//    }
//
//    /**
//     * 滚动时，判断当前第一个View是否发生变化，发生才回调
//     */
//    @Override
//    public void onScrolled(int arg0, int arg1) {
//        View newView = getChildAt(0);
//
//        if (mItemScrollChangeListener != null) {
//            if (newView != null && newView != mCurrentView) {
//                mCurrentView = newView;
//                mItemScrollChangeListener.onChange(mCurrentView, getChildPosition(mCurrentView));
//
//            }
//        }
//
//    }

    public interface OnItemScrollChangeListener {
        void onChange(View view, int position);
    }

}
