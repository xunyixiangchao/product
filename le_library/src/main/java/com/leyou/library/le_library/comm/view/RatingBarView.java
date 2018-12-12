package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import library.liuyh.com.lelibrary.R;


/**
 * Created by Administrator on 2016/6/20.
 */
public class RatingBarView extends LinearLayout {

    private boolean mClickable = true;
    private OnRatingListener onRatingListener;
    private boolean isIndicator;
    private boolean isMatchParent;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private int rating;
    private int startSize;
    private int spacing;
    private int starCount;

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        isIndicator = ta.getBoolean(R.styleable.RatingBarView_isIndicator, false);
        isMatchParent = ta.getBoolean(R.styleable.RatingBarView_isMatchParent, false);
        starCount = ta.getInteger(R.styleable.RatingBarView_starCount, 5);
        startSize = ta.getDimensionPixelSize(R.styleable.RatingBarView_starSize, 20);
        spacing = ta.getDimensionPixelSize(R.styleable.RatingBarView_starSpacing, 10);
        starEmptyDrawable = ta.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = ta.getDrawable(R.styleable.RatingBarView_starFill);
        ta.recycle();

        rating = starCount;

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, attrs, i == 0 ? 0 : spacing, 0
                    , i == starCount - 1 ? 0 : spacing, 0);
            if (isIndicator) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickable) {
                            rating = indexOfChild(v) + 1;
                            setRating(rating);
                            if (onRatingListener != null) {
                                onRatingListener.onRating(rating);
                            }
                        }
                    }
                });
            }
            addView(imageView);
        }
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs,
                                       int left, int top, int right, int bottom) {
        ImageView imageView = new ImageView(context);
        LayoutParams para = new LayoutParams(0, Math.round(startSize));
        if (isMatchParent) {
            para.weight = 1f;
        } else {
            para.width = Math.round(startSize);
            para.height = Math.round(startSize);
            para.setMargins(left, top, right, bottom);
        }
        imageView.setLayoutParams(para);
        imageView.setImageDrawable(starFillDrawable);
        return imageView;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        rating = rating > this.starCount ? this.starCount : rating;
        rating = rating < 0 ? 0 : rating;
        for (int i = 0; i < rating; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        for (int i = this.starCount - 1; i >= rating; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }

    public interface OnRatingListener {
        void onRating(int RatingScore);
    }
}
