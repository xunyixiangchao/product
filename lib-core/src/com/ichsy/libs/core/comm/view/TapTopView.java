package com.ichsy.libs.core.comm.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.utils.DeviceUtil;


/**
 * listview的点击置顶
 *
 * @author liuyuhang
 */
public class TapTopView {

    private static final int ANIMATION_DURATION_TIME = 500;
    private float mMoveY;
    private float mUpY;
    private final float MOVE_DISTANCE = 50;

    private boolean isFirstEvent = true;
    //    private View mTitleBar;
    private int mFirstVisibleItem;
    private boolean isFirstLoad = true;

    //一下参数为导航栏动画参数
    private static boolean oldStatus = true;
    private static int mTitleHeight;

    private NavigationListener navigationListener;

    /**
     * 导航栏状态监听
     */
    public interface NavigationListener {
        /**
         * 导航栏隐藏
         */
        void onHide();

        /**
         * 导航栏显示
         */
        void onShow();
    }

    public void init(final Context context, final ListView listView, final View tapTopView, final int position) {

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isFirstEvent) {
                            mMoveY = event.getY();
                            isFirstEvent = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (listView.getFirstVisiblePosition() == 0) {
                            navigationListener.onShow();
                        } else {
                            mUpY = event.getY();
                            float difference = Math.abs(mMoveY - mUpY);
                            if (difference > MOVE_DISTANCE) {
                                if (mMoveY > mUpY) {
                                    navigationListener.onHide();
                                } else {
                                    navigationListener.onShow();
                                }
                            }
                        }

                        isFirstEvent = true;
                        break;
                    default: {

                    }
                }
                return false;
            }
        });

        tapTopView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFirstVisibleItem < position) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(position);
                        }
                    }, 100);
                } else {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(0);
                        }
                    });
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(0);
                            navigationListener.onShow();
                        }
                    }, 100);

                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mFirstVisibleItem = firstVisibleItem;
                if (mFirstVisibleItem == 0) {
                    startHideTapViewAnimation(tapTopView);
                } else if (firstVisibleItem > visibleItemCount / 2 && firstVisibleItem < position) {
                    startShowTapViewAnimation(tapTopView);
                    tapTopView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.home_down));
                } else if (firstVisibleItem >= position) {
                    startShowTapViewAnimation(tapTopView);
                    tapTopView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.home_up));
                }
            }
        });

    }

    public static boolean showHideTitleBar(final View titleBar, final boolean show) {
        return showHideTitleBar(titleBar, show, null);
    }

    /**
     * 显示还是隐藏顶部导航栏
     *
     * @param titleBar
     * @param show
     * @param runnable
     * @return 是否显示或者隐藏成功
     */
    private static ValueAnimator animator;

    public static boolean showHideTitleBar(final View titleBar, final boolean show, final Runnable runnable) {
        if (titleBar == null || show == oldStatus) {
            return false;
        }

//        if (navigationListener != null) {
//            if (show) {
//                navigationListener.onShow();
//            } else {
//                navigationListener.onHide();
//            }
//        }
        if (mTitleHeight <= 0) {
            mTitleHeight = titleBar.getMeasuredHeight();
        }

        if (null != animator) {
            animator.cancel();
        }

        if (show) {
            animator = ValueAnimator.ofInt(0, mTitleHeight);
        } else {
            animator = ValueAnimator.ofInt(mTitleHeight, 0);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer height = (Integer) animator.getAnimatedValue();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleBar.getLayoutParams();
                params.height = height;
                titleBar.setLayoutParams(params);
            }
        });

        if (runnable != null) {
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
//        if (oldStatus != show) {
        animator.setDuration(300);
        animator.start();
//        }
        oldStatus = show;
        return true;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public static void startShowTapViewAnimation(View tapTopView) {
        if (tapTopView.getVisibility() != View.VISIBLE) {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(ANIMATION_DURATION_TIME);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
//            int height = parentView.getMeasuredHeight();
//            if (height <= tapTopView.getMeasuredHeight()) {
            int height = DeviceUtil.getWindowHeight(tapTopView.getContext());
//            }
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, height, Animation.RELATIVE_TO_SELF);

            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(translateAnimation);

            tapTopView.startAnimation(animationSet);
            tapTopView.setVisibility(View.VISIBLE);
        }
    }

    public static void startHideTapViewAnimation(View tapTopView) {
        if (tapTopView.getVisibility() != View.GONE) {
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(ANIMATION_DURATION_TIME);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
//            int height = parentView.getMeasuredHeight();
//            if (height <= tapTopView.getMeasuredHeight()) {
            int height = DeviceUtil.getWindowHeight(tapTopView.getContext());
//            }
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, height);

            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(translateAnimation);

            tapTopView.startAnimation(animationSet);
            tapTopView.setVisibility(View.GONE);
        }
    }

}
