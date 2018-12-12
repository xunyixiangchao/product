package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dalong.refreshlayout.OnHeaderListener;
import com.dalong.refreshlayout.RefreshStatus;

import library.liuyh.com.lelibrary.R;

/**
 * 下拉刷新自定义头部
 * Created by zhaoye
 */

public class InterceptTouchRefreshHeader extends LinearLayout implements OnHeaderListener {

    private ImageView mAnimationView;

    public InterceptTouchRefreshHeader(Context context) {
        super(context);
        View header = LayoutInflater.from(getContext()).inflate(R.layout.le_pullview_header, this);
        mAnimationView = (ImageView) header.findViewById(R.id.imageview_header);
        mAnimationView.setImageResource(R.drawable.le_loading_anim4);
    }

    @Override
    public void onRefreshBefore(int scrollY, int refreshHeight, int headerHeight) {
        refreshStatus(RefreshStatus.REFRESH_BEFORE);
    }

    @Override
    public void onRefreshAfter(int scrollY, int refreshHeight, int headerHeight) {
        refreshStatus(RefreshStatus.REFRESH_AFTER);
    }

    @Override
    public void onRefreshReady(int scrollY, int refreshHeight, int headerHeight) {
        refreshStatus(RefreshStatus.REFRESH_READY);
    }

    @Override
    public void onRefreshing(int scrollY, int refreshHeight, int headerHeight) {
        refreshStatus(RefreshStatus.REFRESH_DOING);
    }

    @Override
    public void onRefreshComplete(int scrollY, int refreshHeight, int headerHeight, boolean isRefreshSuccess) {
        refreshStatus(RefreshStatus.REFRESH_COMPLETE);
    }

    @Override
    public void onRefreshCancel(int scrollY, int refreshHeight, int headerHeight) {
        refreshStatus(RefreshStatus.REFRESH_CANCEL);
    }

    public void refreshStatus(RefreshStatus status) {
        AnimationDrawable animationDrawable = null;
        switch (status) {
            case DEFAULT:
                break;
            case REFRESH_BEFORE:
                break;
            case REFRESH_AFTER:
                break;
            case REFRESH_READY:
                break;
            case REFRESH_DOING:
                animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
                animationDrawable.start();
                break;
            case REFRESH_CANCEL:
                animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
                animationDrawable.stop();
                mAnimationView.setImageResource(R.drawable.le_loading_anim4);
                break;
            case REFRESH_COMPLETE:
                animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
                animationDrawable.stop();
                mAnimationView.setImageResource(R.drawable.le_loading_anim4);
                break;
        }
    }
}
