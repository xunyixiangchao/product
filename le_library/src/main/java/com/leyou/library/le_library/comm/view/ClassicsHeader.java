package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import library.liuyh.com.lelibrary.R;

/**
 * Created by zhaoye on 2017/11/27.
 */

public class ClassicsHeader extends LinearLayout implements RefreshHeader {

    private ImageView mProgressView;//刷新动画视图

    public ClassicsHeader(Context context) {
        super(context);
        initView(context);
    }
    public ClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }
    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }
    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mProgressView = new ImageView(context);
        mProgressView.setImageResource(R.drawable.le_loading_anim);
        addView(mProgressView, DensityUtil.dp2px(50), DensityUtil.dp2px(50));
        setMinimumHeight(DensityUtil.dp2px(60));
    }
    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }
    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
        animationDrawable.start();//开始动画
    }
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
        animationDrawable.stop();//停止动画
        return 100;//延迟100毫秒之后再弹回
    }
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }
    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
    }
    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
    }
    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
    }
    @Override
    public void setPrimaryColors(@ColorInt int ... colors){
    }
}