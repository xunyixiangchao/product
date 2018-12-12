package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.leyou.library.le_library.comm.helper.PullViewHelper;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import library.liuyh.com.lelibrary.R;

/**
 * Created by liuyuhang on 16/8/22.
 */
public class LePullRefreshHeader extends FrameLayout implements PtrUIHandler {
    private ImageView mAnimationView;

    private PullViewHelper.PullListener<PtrFrameLayout> mPullListener;

    public LePullRefreshHeader(Context context) {
        super(context);
        initViews(null);
    }


    public LePullRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public LePullRefreshHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }


    private void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.le_pullview_header, this);
        mAnimationView = (ImageView) header.findViewById(R.id.imageview_header);
        mAnimationView.setImageResource(R.drawable.le_loading_anim4);
    }

    public void setPullListener(PullViewHelper.PullListener<PtrFrameLayout> listener) {
        this.mPullListener = listener;
    }

    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    /**
     * prepare for loading
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    /**
     * perform refreshing UI
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
        animationDrawable.start();
    }

    /**
     * perform UI after refresh
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
        animationDrawable.stop();
        mAnimationView.setImageResource(R.drawable.le_loading_anim4);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos > 0) {
            mPullListener.onStartPull();
        } else {
            mPullListener.onStopPull();
        }

//        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                mPullListener.onStopPull();
//            }
//        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
//            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
//                mPullListener.onStartPull();
//            }
//        }
    }


}
