package com.ichsy.libs.core.comm.view.viewpager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ichsy.core_library.R;

/**
 * viewpager顶部左右滑动的table布局
 * Created by liuyuhang on 2017/4/26.
 */

public class ViewPagerTableView extends LinearLayout {

    private ViewPager viewPager;
    /**
     * 存放菜单的view
     */
    private LinearLayout mMenuGroupLayout;

    private HorizontalScrollView mRootSrcollView;
    private View[] mMenuView;
    //底下滑动的view
    private View mTableIndicatorView;

    private RelativeLayout mLeftHomePageView;
    private View mLeftHomepageIndicatorView;

    private int mOldSelectPosition;

    private int mIndicatorViewGravity = 0;

    /**
     * 底部选择条是否有移动动画
     */
    private boolean mIsAnimationIndicatorView = false;
    /**
     * 是否固定最左边的按钮
     */
    private boolean mIsFirstItemFixedly = false;
    private int firstItemColor = -1;

    private ViewPagerTableItemUiCreator mViewPagerTableItemUiCreator;

    public int defaultColor, selectColor;

    private
    @ColorInt
    int mIndicatorColor;

    public interface ViewPagerTableItemUiCreator {
        /**
         * 创建每一个item
         *
         * @param inflater
         * @param position
         * @param title
         * @return
         */
        View onTableItemCreate(LayoutInflater inflater, int position, String title);

        /**
         * 选中每一个item事件
         *
         * @param itemViews
         * @param selectPosition
         * @param lastSelectPosition
         * @return 改变的view
         */
        View[] onTableItemChanged(View[] itemViews, int selectPosition, int lastSelectPosition);

        /**
         * 底部条的颜色
         *
         * @return
         */
        int onTableIndicatorViewColorRes();
    }

    public ViewPagerTableView(Context context) {
        this(context, null);
    }

    public ViewPagerTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setItemUiCreator(ViewPagerTableItemUiCreator uiCreator) {
        this.mViewPagerTableItemUiCreator = uiCreator;
    }

    /**
     * 底部选择条是否有移动动画
     */
    public void setIsAnimationIndicatorView() {
        this.mIsAnimationIndicatorView = true;
    }

    /**
     * 是否固定最左边的按钮
     */
    public void setIsFirstItemFixedly(boolean fixed) {
        this.mIsFirstItemFixedly = fixed;
    }

    public void setFirstItemColor(int color) {
        this.firstItemColor = color;
    }

    public void setIndicatorViewGravity(int gravity) {
        this.mIndicatorViewGravity = gravity;
    }

    public void setTabIndicatorColor(@ColorInt int color) {
        mIndicatorColor = color;
    }

    /**
     * 重新设置颜色
     *
     * @param defaultColor
     * @param selectColor
     */
    public void setColorStyle(int defaultColor, int selectColor) {
        this.defaultColor = defaultColor;
        this.selectColor = selectColor;

        setTabIndicatorColor(selectColor);
    }

    private ViewPagerTableItemUiCreator getDefaultTableItemUiCreator() {
        return new ViewPagerTableItemUiCreator() {
            @Override
            public View onTableItemCreate(LayoutInflater inflater, int position, String title) {
                View childItemView = inflater.inflate(R.layout.adapter_viewpager_tableview_indicator_item, null);
                TextView contentView = (TextView) childItemView.findViewById(R.id.tv_content);
                contentView.setText(title);
                return childItemView;
            }

            @Override
            public View[] onTableItemChanged(View[] itemViews, int selectPosition, int lastSelectPosition) {
                itemViews[selectPosition].findViewById(R.id.view_bottom).setBackgroundColor(mIndicatorColor);

                itemViews[lastSelectPosition].findViewById(R.id.view_bottom).setVisibility(View.INVISIBLE);
                itemViews[selectPosition].findViewById(R.id.view_bottom).setVisibility(View.VISIBLE);
                return itemViews;
            }

            /**
             * 底部条的颜色
             *
             * @return
             */
            @Override
            public int onTableIndicatorViewColorRes() {
                return 0;
            }
        };

    }

    private void init() {
        removeAllViews();
        setOrientation(LinearLayout.HORIZONTAL);

//        View root = LayoutInflater.from(getContext()).inflate(R.layout.frame_viewpager_table_host_layout, null);
//        root.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        mMenuGroupLayout = (LinearLayout) root.findViewById(R.id.group_menu);
//        mRootSrcollView = (HorizontalScrollView) root.findViewById(R.id.sv_main);
//        mTableIndicatorView = root.findViewById(R.id.view_indicator);
//        mTableIndicatorView.setVisibility(View.GONE);
//        addView(root);

        mMenuGroupLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        @IdRes int id = 10010;
        mMenuGroupLayout.setId(id);
        mMenuGroupLayout.setLayoutParams(lp);
        mMenuGroupLayout.setOrientation(LinearLayout.HORIZONTAL);

        mRootSrcollView = new HorizontalScrollView(getContext());
        mRootSrcollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        /* 顶部滑动和低下一条 */
        RelativeLayout horizontalRootLayout = new RelativeLayout(getContext());
        horizontalRootLayout.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if (mIndicatorViewGravity != 0) {
            lp2.gravity = mIndicatorViewGravity;
        }
        horizontalRootLayout.setLayoutParams(lp2);
        horizontalRootLayout.addView(mMenuGroupLayout);

        mRootSrcollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRootSrcollView.setHorizontalScrollBarEnabled(false);

        if (mIsAnimationIndicatorView) {
            mTableIndicatorView = new View(getContext());
            mTableIndicatorView.setBackgroundColor(Color.parseColor("#00aa00"));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 10);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            layoutParams.addRule(RelativeLayout.BELOW, id);
            mTableIndicatorView.setLayoutParams(layoutParams);
            horizontalRootLayout.addView(mTableIndicatorView);
        }

        mRootSrcollView.addView(horizontalRootLayout);

        RelativeLayout rootView = new RelativeLayout(getContext());
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rootView.setGravity(Gravity.CENTER);
        rootView.addView(mRootSrcollView);

        //加入root到当前根视图
        addView(rootView);

        if (mIsFirstItemFixedly) {
            //顶部最左边固定不变的view
            mLeftHomePageView = new RelativeLayout(getContext());
            if (firstItemColor == -1) {
                mLeftHomePageView.setBackgroundColor(Color.parseColor("#ffffffff"));
            } else {
                mLeftHomePageView.setBackgroundColor(firstItemColor);
            }
            mLeftHomepageIndicatorView = new View(getContext());
            mLeftHomepageIndicatorView.setBackgroundColor(mIndicatorColor);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 10);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mLeftHomepageIndicatorView.setLayoutParams(layoutParams);
            mLeftHomePageView.addView(mLeftHomepageIndicatorView);
            rootView.addView(mLeftHomePageView);
        }

//        addView();
//        addView(mMenuGroupLayout);

        if (null == mViewPagerTableItemUiCreator) {
            mViewPagerTableItemUiCreator = getDefaultTableItemUiCreator();
        }
    }

    /**
     * 初始化view
     *
     * @param viewPager
     */
    public synchronized void setUpWithViewPager(final ViewPager viewPager) {
        init();
        this.viewPager = viewPager;
        PagerAdapter adapter = viewPager.getAdapter();
        if (null == adapter) {
            throw new IllegalArgumentException("请先给 " + viewPager.getClass().getCanonicalName() + " 设置adapter");
        }
        mOldSelectPosition = 0;
        mMenuGroupLayout.removeAllViews();
        int count = adapter.getCount();
        mMenuView = new View[count];
        updateTableViewTabStyle();
//        for (int i = 0; i < count; i++) {
//            mMenuView[i] = mViewPagerTableItemUiCreator.onTableItemCreate(LayoutInflater.from(getContext()), i, adapter.getPageTitle(i).toString());
//            mMenuView[i].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//
//            mMenuGroupLayout.addView(mMenuView[i]);
//
//            if (mIsFirstItemFixedly) {
//                if (i == 0) {
//                    View view = mViewPagerTableItemUiCreator.onTableItemCreate(LayoutInflater.from(getContext()), i, adapter.getPageTitle(i).toString());
//                    view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                    mLeftHomePageView.addView(view);
//                }
//            }
//
//            final int finalI = i;
//            //设置按钮的点击效果
//            mMenuView[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewPager.setCurrentItem(finalI);
//                }
//            });
//        }

        if (mIsFirstItemFixedly) {
            mLeftHomePageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuView[0].performClick();
                }
            });
        }

//        setBottomViewVisibility(0, View.VISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition(position);
//                mMenuView[position].performClick();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mIsAnimationIndicatorView) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    selectPosition(0);

                    if (mIsFirstItemFixedly) {
                        ViewGroup.LayoutParams layoutParams = mLeftHomepageIndicatorView.getLayoutParams();
                        layoutParams.width = mMenuView[0].getMeasuredWidth();
                        mLeftHomepageIndicatorView.setLayoutParams(layoutParams);
                    }

                    //得到后取消监听
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }

    }

    /**
     * 更新tabview的颜色风格
     */
    public void updateTableViewTabStyle() {
        int count = mMenuView.length;

        mMenuGroupLayout.removeAllViews();
        PagerAdapter adapter = viewPager.getAdapter();
        for (int i = 0; i < count; i++) {
            mMenuView[i] = mViewPagerTableItemUiCreator.onTableItemCreate(LayoutInflater.from(getContext()), i, adapter.getPageTitle(i).toString());
            mMenuView[i].setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            mMenuGroupLayout.addView(mMenuView[i]);

            if (mIsFirstItemFixedly) {
                if (i == 0) {
                    View view = mViewPagerTableItemUiCreator.onTableItemCreate(LayoutInflater.from(getContext()), i, adapter.getPageTitle(i).toString());
                    view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    mLeftHomePageView.addView(view);
                }
            }

            final int finalI = i;
            //设置按钮的点击效果
            mMenuView[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }

        if (0 != mViewPagerTableItemUiCreator.onTableIndicatorViewColorRes()) {
            mTableIndicatorView.setBackgroundColor(mViewPagerTableItemUiCreator.onTableIndicatorViewColorRes());
        }

        int currentPosition = viewPager.getCurrentItem();
        mViewPagerTableItemUiCreator.onTableItemChanged(mMenuView, currentPosition, currentPosition);
    }

    /**
     * 设置选中某一个tab
     *
     * @param position
     */
    public void selectPosition(final int position) {
        final View oldMenuView = mMenuView[mOldSelectPosition];
        final View currentMenuView = mMenuView[position];

        if (mIsAnimationIndicatorView) {
            //下面横条的宽度变化动画
            int oldWidth = oldMenuView.getMeasuredWidth();
            int currentWidth = currentMenuView.getMeasuredWidth();

            ValueAnimator widthAnimation = ValueAnimator.ofInt(oldWidth, currentWidth);
            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animationValue = (int) animation.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = mTableIndicatorView.getLayoutParams();
                    layoutParams.width = animationValue;
                    mTableIndicatorView.setLayoutParams(layoutParams);
                }
            });
            widthAnimation.setInterpolator(new LinearInterpolator());
            widthAnimation.setDuration(200);
            widthAnimation.setRepeatCount(0);
            widthAnimation.start();

            if (0 != mViewPagerTableItemUiCreator.onTableIndicatorViewColorRes()) {
                mTableIndicatorView.setBackgroundColor(mViewPagerTableItemUiCreator.onTableIndicatorViewColorRes());
            }

            ValueAnimator valueAnimator = ValueAnimator.ofInt(oldMenuView.getLeft(), currentMenuView.getLeft());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animationValue = (int) animation.getAnimatedValue();
                    mTableIndicatorView.setX(animationValue);

                    if (mIsFirstItemFixedly && mIsAnimationIndicatorView) {
                        //第一个item因为是固定的需要单独处理一下
                        if (position == 0) {
                            if (animationValue <= mMenuView[0].getMeasuredWidth()) {
                                mLeftHomepageIndicatorView.setBackgroundColor(mIndicatorColor);
                            }
                        } else {
                            if (animationValue >= mMenuView[0].getMeasuredWidth()) {
                                mLeftHomepageIndicatorView.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                        }
                    }
                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.setRepeatCount(0);
            valueAnimator.start();
        }

        int scrollPosition = position - 2;
        if (scrollPosition < 0) {
            scrollPosition = 0;
        }
        mRootSrcollView.smoothScrollTo(mMenuView[scrollPosition].getLeft(), 0);

        mViewPagerTableItemUiCreator.onTableItemChanged(mMenuView, position, mOldSelectPosition);

        if (mIsFirstItemFixedly) {
            mViewPagerTableItemUiCreator.onTableItemChanged(new View[]{mLeftHomePageView}, position, mOldSelectPosition);
        }

        mOldSelectPosition = position;
    }

}
