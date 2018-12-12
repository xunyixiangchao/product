package com.leyou.library.le_library.comm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import library.liuyh.com.lelibrary.R;


public abstract class NineGridLayout extends ViewGroup {
    protected Context context;
    private int mColumns, mRows;
    private List<String> mUrlList = new ArrayList<>();
    private int unitSize, unit1Size, unit9Size, mSpacing;
    private int showCount;
    private boolean isFit;// 一张图片时是否会大点

    public NineGridLayout(Context context) {
        super(context);
        this.context = context;
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
        mSpacing = (int) typedArray.getDimension(R.styleable.NineGridLayout_spacing, 0f);
        unit9Size = (int) typedArray.getDimension(R.styleable.NineGridLayout_unit, 0f);
        unit1Size = unit9Size * 2 + mSpacing;
        isFit = typedArray.getBoolean(R.styleable.NineGridLayout_fit, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int unitMeasureSpec = MeasureSpec.makeMeasureSpec(unitSize, MeasureSpec.EXACTLY);
        measureChildren(unitMeasureSpec, unitMeasureSpec);
        setMeasuredDimension(unitSize * mColumns + (mColumns - 1) * mSpacing, unitSize * mRows + (mRows - 1) * mSpacing);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (showCount < 1) {
            if (getVisibility() == View.VISIBLE) {
                setVisibility(View.GONE);
            }
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < showCount; i++) {
            ImageView mImageView = (ImageView) getChildAt(i);
            if (mImageView == null) {
                mImageView = new ImageView(context);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                addView(mImageView);
            }
            loadImageView(mImageView, i);
            layoutChild(mImageView, i);
            if (mImageView.getVisibility() == GONE) {
                mImageView.setVisibility(VISIBLE);
            }
        }

        for (int i = showCount; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == VISIBLE) {
                childView.setVisibility(View.GONE);
            }
        }
    }

    private void loadImageView(ImageView view, final int position) {
        final String imageUrl = mUrlList.get(position);
        displayImage(view, imageUrl);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(position, imageUrl, mUrlList);
            }
        });
    }

    private void layoutChild(View view, int index) {
        int[] position = findPosition(index);
        int left = (unitSize + mSpacing) * position[1];
        int top = (unitSize + mSpacing) * position[0];
        int right = left + unitSize;
        int bottom = top + unitSize;
        view.layout(left, top, right, bottom);
    }

    public void setUrlList(List<String> urlList) {
        setUrlList(urlList, 9);
    }

    public void setUrlList(List<String> urlList, int maxCount) {
        if (urlList != null) {
            mUrlList = urlList;
        } else {
            mUrlList.clear();
        }
        int size = mUrlList.size();
        showCount = maxCount < size ? maxCount : size;

        measureRowAndColumn(showCount);

        unitSize = isFit && showCount == 1 ? unit1Size : unit9Size;

        if (showCount == 0) {
            if (getVisibility() == View.VISIBLE) {
                setVisibility(View.GONE);
            }
        } else {
            if (getVisibility() == View.GONE) {
                setVisibility(View.VISIBLE);
            }
        }
        requestLayout();
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    private void measureRowAndColumn(int size) {
        if (size == 0) {
            mRows = 0;
            mColumns = 0;
        } else if (size <= 3) {
            mRows = 1;
            mColumns = size;
        } else if (size <= 6) {
            mRows = 2;
            mColumns = 3;
            if (size == 4) {
                mColumns = 2;
            }
        } else {
            mRows = 3;
            mColumns = 3;
        }
    }

    public void isFit(boolean isFit) {
        this.isFit = isFit;
    }

    protected abstract void displayImage(ImageView imageView, String url);

    protected abstract void onClickImage(int position, String url, List<String> urlList);
}
