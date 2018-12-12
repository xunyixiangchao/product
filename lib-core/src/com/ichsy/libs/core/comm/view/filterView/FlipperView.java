package com.ichsy.libs.core.comm.view.filterView;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.comm.view.dialog.DialogUiBuilder;
import com.ichsy.libs.core.comm.view.dialog.RollUiBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 上下带动画轮播的广告view
 * Created by liuyuhang on 2017/4/28.
 */

public class FlipperView extends LinearLayout {
    private List<String> contentList;

    private int mContentTextSize = 0;
    private int mContentColor = 0;
    private int mContentTextColor = 0;

    public RollUiBuilder uiBuilder;
    private FlipperViewClickListener mFlipperViewClickListener;

    private int frequency; //滚动次数

    public interface FlipperViewClickListener {
        void onPositionClicked(int position);
    }


    public FlipperView(Context context) {
        this(context, null);
    }

    public FlipperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        contentList = new ArrayList<>();
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setContentList(List<String> list) {
        this.contentList = list;
        this.frequency = contentList.size();
        updateUi();
    }

    public void setRollFrequency(int frequency){
        this.frequency = frequency;
        updateUi();
    }

    public void setUiBuilder(RollUiBuilder uiBuilder) {
        this.uiBuilder = uiBuilder;
    }

    public void setFlipperViewClickListener(FlipperViewClickListener listener) {
        this.mFlipperViewClickListener = listener;
    }

    public List<String> getContentList() {
        if (contentList == null) {
            contentList = new ArrayList<>();
        }
        return this.contentList;
    }

    public void setContentBackground(@ColorInt int color) {
        this.mContentColor = color;
    }

    public void setContentTextColor(@ColorInt int color) {
        this.mContentTextColor = color;
    }

    public void setContentTextSize(int size) {
        this.mContentTextSize = size;
    }


    private void updateUi() {
        removeAllViews();

        int padding = ViewUtil.dip2px(getContext(), 5);
        final ViewFlipper viewFlipper = new ViewFlipper(getContext());
        viewFlipper.setInAnimation(getContext(), R.anim.push_anim_top_in);
        viewFlipper.setOutAnimation(getContext(), R.anim.push_anim_top_out);
        for (int i = 0;i<frequency;i++) {

            if (null == uiBuilder) {
                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER_VERTICAL);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(layoutParams);
                if (0 != mContentColor) {
                    textView.setBackgroundColor(mContentColor);
                }
                if (0 != mContentTextColor) {
                    textView.setTextColor(mContentTextColor);
                }
                if (0 != mContentTextSize) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mContentTextSize);
                }

                textView.setText(Html.fromHtml(contentList.get(i)));
                textView.setPadding(padding, padding, padding, padding);
                viewFlipper.addView(textView);
            } else {
                View itemView = uiBuilder.onViewCreate(LayoutInflater.from(getContext()));
                uiBuilder.onViewDraw(itemView, null,i);
                viewFlipper.addView(itemView);
            }
        }

        if (null != mFlipperViewClickListener) {
            viewFlipper.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlipperViewClickListener.onPositionClicked(viewFlipper.getDisplayedChild());
                }
            });
        }

        if (frequency > 1) {
            viewFlipper.startFlipping();
        }
        addView(viewFlipper);
    }
}
