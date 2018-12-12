package com.ichsy.libs.core.frame.adapter.section;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.frame.adapter.BaseAdapterViewType;

/**
 * Created by liuyuhang on 2017/4/24.
 */

public class SectionViewGroup extends RelativeLayout {
    private boolean isSectionAdded = false;
    private LinearLayout mSectionView;
//    private View mSectionChildView;

    public SectionViewGroup(Context context) {
        this(context, null);
    }

    public SectionViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        ListView childAt = (ListView) getChildAt(0);
//        BaseFrameAdapter adapter = (BaseFrameAdapter) childAt.getAdapter();

//        onScrollListener.a

//        setOrientation(LinearLayout.VERTICAL);

//        mSectionView = new TextView(getContext());
        mSectionView = new LinearLayout(getContext());
        mSectionView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        mSectionView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
//        mSectionView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        ((TextView) mSectionView).setText("测试");
        addView(mSectionView);
    }

//    public void setSectionView(View view) {
////        mSectionChildView = view;
//        mSectionView.addView(view);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        View sectionView = getChildAt(0);
        removeView(mSectionView);
        addView(mSectionView);
        super.onLayout(changed, l, t, r, b);

        if (!isSectionAdded) {
            isSectionAdded = true;

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (childView instanceof ListView) {
                    ListView childListView = (ListView) getChildAt(i);
                    ListAdapter listAdapter = childListView.getAdapter();
                    if (!(listAdapter instanceof BaseSectionAdapter)) {
                        continue;
                    }
                    final BaseSectionAdapter adapter = (BaseSectionAdapter<?>) listAdapter;
//                    int sectionViewResId = adapter.onSectionViewCreate();

                    View childSection = LayoutInflater.from(getContext()).inflate(adapter.onSectionViewCreate(), null);
//            mSectionView.removeAllViews();

//                TextView textView = new TextView(getContext());
                    childSection.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                textView.setText("test text");
//                mSectionView.addView(textView);
                    mSectionView.addView(childSection);

                    childListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            public BaseSectionAdapter<?> adapter;

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            adapter.onSectionViewAttach(mSectionView, firstVisibleItem, adapter.getItem(firstVisibleItem));

                            if (adapter.getItemViewType(firstVisibleItem) == BaseAdapterViewType.VIEW_TYPE_SECTION) {
                                ViewUtil.setViewVisibility(View.GONE, mSectionView);
                            } else {
                                ViewUtil.setViewVisibility(View.VISIBLE, mSectionView);
//                    adapter.onSectionViewAttach();
                            }
                        }
                    });
                }

            }
        }

//        onScrollListener.ad

//        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int childCount = getChildCount();
//
////        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            if (!(childView instanceof ListView)) {
//                continue;
//            }
//            ListView childListView = (ListView) getChildAt(i);
//            final BaseSectionAdapter<?> adapter = (BaseSectionAdapter<?>) childListView.getAdapter();
//            int sectionViewResId = adapter.onSectionViewCreate();
//            View childSection = LayoutInflater.from(getContext()).inflate(sectionViewResId, null, false);
//            mSectionView.removeAllViews();
//
//            TextView textView  =new TextView(getContext());
////            textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
//            textView.setText("test text");
//            mSectionView.addView(textView);
//           mSectionView.requestLayout();
//        }


//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            if (!(childView instanceof ListView)) {
//                continue;
//            }
//
////        if (changed) {
//            ListView childListView = (ListView) getChildAt(i);
//            final BaseSectionAdapter<?> adapter = (BaseSectionAdapter<?>) childListView.getAdapter();
//
//            int sectionViewResId = adapter.onSectionViewCreate();
//
//            mSectionView = findViewById(sectionId);
//            if (null == mSectionView) {
//                mSectionView = LayoutInflater.from(getContext()).inflate(sectionViewResId, this, false);
//                mSectionView.setId(sectionId);
////            addView(mSectionView, 1);
//
//                mSectionView.layout(0, 0, 200, 200);
//
////            childListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//////            public BaseSectionAdapter<?> adapter;
////
////                @Override
////                public void onScrollStateChanged(AbsListView view, int scrollState) {
////
////                }
////
////                @Override
////                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                    if (adapter.getItemViewType(firstVisibleItem) == BaseAdapterViewType.VIEW_TYPE_SECTION) {
////                        ViewUtil.setViewVisibility(View.GONE, mSectionView);
////                    } else {
////                        ViewUtil.setViewVisibility(View.VISIBLE, mSectionView);
//////                    adapter.onSectionViewAttach();
////                    }
////                }
////            });
//            }
//        }

    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        public BaseSectionAdapter<?> adapter;

        public void init(BaseSectionAdapter<?> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (adapter.onSectionBind(firstVisibleItem)) {
                ViewUtil.setViewVisibility(View.GONE);
            } else {
                ViewUtil.setViewVisibility(View.VISIBLE);

//                    adapter.onSectionViewAttach();
            }
        }
    };

}
