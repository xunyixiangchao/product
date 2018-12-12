package com.leyou.library.le_library.comm.view.refreshheader;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalong.refreshlayout.OnHeaderListener;
import com.dalong.refreshlayout.RefreshStatus;

import library.liuyh.com.lelibrary.R;

/**
 * 下拉刷新自定义头部
 * Created by zhaoye
 */

public class RefreshLinkedHeader extends LinearLayout implements OnHeaderListener {

    private ImageView mImageHeaderLoading;
    private ImageView mImageHeaderTop;
    private TextView mTextHeaderName;
    private LinearLayout mLayoutHeader;
    private View mHeaderView;
    private AnimationDrawable animationDrawable;
    private ObjectAnimator objectAnimator;

    public RefreshLinkedHeader(Context context) {
        super(context);
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.refresh_linked_header_view, this, true);
        mImageHeaderLoading = (ImageView) mHeaderView.findViewById(R.id.header_image);
        mImageHeaderTop = (ImageView) mHeaderView.findViewById(R.id.header_image_top);
        mTextHeaderName = (TextView) mHeaderView.findViewById(R.id.header_name);
        mLayoutHeader = (LinearLayout) mHeaderView.findViewById(R.id.header_layout);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new MarginLayoutParams(mImageHeaderLoading.getLayoutParams()));
//        layoutParams.height = ViewUtil.dip2px(context, 240);
//        layoutParams.width = ViewUtil.dip2px(context, 144);
//        mImageHeaderLoading.setLayoutParams(layoutParams);
//        mImageHeaderLoading.setImageResource(R.drawable.home_refresh);
        mImageHeaderLoading.setImageResource(R.drawable.le_loading_anim4);
        mImageHeaderTop.setVisibility(VISIBLE);
        mTextHeaderName.setVisibility(GONE);
        mLayoutHeader.setBackgroundColor(getResources().getColor(R.color.le_color_white));
//        objectAnimator = AnimationHelper.rotate(mImageHeaderLoading, 300, -1);
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
        switch (status) {
            case DEFAULT:
                break;
            case REFRESH_BEFORE:
                mTextHeaderName.setText("18年专注母婴");
                break;
            case REFRESH_AFTER:
                mTextHeaderName.setText("18年专注母婴");
                break;
            case REFRESH_READY:
                break;
            case REFRESH_DOING:
                mTextHeaderName.setText("18年专注母婴");

                ((AnimationDrawable) mImageHeaderLoading.getDrawable()).start();

                if (objectAnimator != null) {
                    objectAnimator.start();
                }
                break;
            case REFRESH_CANCEL:
                mTextHeaderName.setText("18年专注母婴");

                ((AnimationDrawable) mImageHeaderLoading.getDrawable()).stop();
                mImageHeaderLoading.setImageResource(R.drawable.le_loading_anim4);
                if (objectAnimator != null) {
                    objectAnimator.cancel();
                }
                break;
            case REFRESH_COMPLETE:
                ((AnimationDrawable) mImageHeaderLoading.getDrawable()).stop();
                mImageHeaderLoading.setImageResource(R.drawable.le_loading_anim4);
                if (objectAnimator != null) {
                    objectAnimator.cancel();
                }
                mTextHeaderName.setText("18年专注母婴");
                break;
            default:
                break;
        }
    }
}
