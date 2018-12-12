package com.leyou.library.le_library.comm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import library.liuyh.com.lelibrary.R;


/**
 * Created by zhaoye
 */

public class HomeClassicsHeader extends LinearLayout implements RefreshHeader {

    private ImageView mProgressView;//刷新动画视图
    private ImageView image;

    public HomeClassicsHeader(Context context) {
        super(context);
        initView(context);
    }
    public HomeClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }
    public HomeClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }
    @SuppressLint("ResourceType")
    private void initView(Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.home_classics_header,null);
        image = (ImageView) v.findViewById(R.id.header_image);
        addView(v);
//        setOrientation(LinearLayout.VERTICAL);
//        setBackgroundColor(R.color.le_bg_white);
//        mProgressView = new ImageView(context);
//        mProgressView.setImageResource(R.drawable.drop_down_loading);
////        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        addView(mProgressView);
//        TextView textView = new TextView(context);
//        textView.setText("下拉刷新");
//        textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
//        LayoutParams textlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        textlp.setMargins(0,10,0,10);
//        addView(textView,textlp);
//        setMinimumHeight(DensityUtil.dp2px(20));

//        setGravity(Gravity.CENTER);
//        mProgressView = new ImageView(context);
//        mProgressView.setImageResource(getResources().getColor(R.color.btg_global_black));
//        addView(mProgressView, DensityUtil.dp2px(50), DensityUtil.dp2px(50));
//        setMinimumHeight(DensityUtil.dp2px(60));
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
//        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
//        animationDrawable.start();//开始动画
    }
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
//        AnimationDrawable animationDrawable = (AnimationDrawable) mProgressView.getDrawable();
//        animationDrawable.stop();//停止动画
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