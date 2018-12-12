package com.ichsy.libs.core.comm.view.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ichsy.core_library.R;
import com.ichsy.core_library.R.id;
import com.ichsy.core_library.R.layout;
import com.ichsy.libs.core.comm.utils.ViewUtil;
import com.ichsy.libs.core.frame.BaseFrameActivity;

/**
 * @author LiuYuHang Date: 2015年5月12日
 * <p/>
 * Modifier： Modified Date： Modify：
 * <p/>
 * Copyright @ 2015 Corpration CHSY
 * @Package com.ichsy.public_libs.view.navigation
 * @File NavigationView.java
 */
public class NavigationController implements NavigationListener {

	private static NavigationSetting setting;

	public View mNavigationRootView;
	public LinearLayout mLeftLayout, mCenterLayout, mRightLayout;

	private Context mContext;
	private boolean isNavigationHide = false;
	private TextView rightButton;
	private String mNavigationTitle;

	private NavigationListener mNavigationListener;
	private NavigationSettingCallback mNavigationSettingCallback;

	public interface NavigationSettingCallback {
		/**
		 * 导航栏title变动的通知
		 *
		 * @param context
		 * @param title
		 */
		void onNavigationTitleChanged(Context context, String title);
	}

	public NavigationController(Context context, NavigationListener navigationListener) {
		mContext = context;
		mNavigationListener = navigationListener;

		// bindNavigation(((Activity) context).getWindow().getDecorView());
	}

	/**
	 * 绑定导航栏布局
	 *
	 * @param parentView Modifier： Modified Date： Modify：
	 */
	public void bindNavigation(View parentView) {

		mNavigationRootView = parentView.findViewById(id.navigation_root_layout);
		if (mNavigationRootView == null) {
			throw new NullPointerException("需要id为 navigation_root_layout 的布局");
		}
		ViewUtil.setViewVisibility(View.VISIBLE, mNavigationRootView);

		mLeftLayout = (LinearLayout) mNavigationRootView.findViewById(id.navigation_left_layout);
		mCenterLayout = (LinearLayout) mNavigationRootView.findViewById(id.navigation_center_layout);
		mRightLayout = (LinearLayout) mNavigationRootView.findViewById(id.navigation_right_layout);
		mLeftLayout.setGravity(Gravity.CENTER_VERTICAL);
		mRightLayout.setGravity(Gravity.CENTER_VERTICAL);

		// 绑定布局之后，需要做初始化设置
		initNavigationSetting(getSetting());
	}

	/**
	 * 初始化setting
	 *
	 * @param setting Modifier： Modified Date： Modify：
	 */

	private void initNavigationSetting(NavigationSetting setting) {
		if (setting.backgroudDrawableResid != -1) {
			setBackgroundResource(setting.backgroudDrawableResid);
		}

		if (setting.dividerResId != -1) {
			View dividerView = mNavigationRootView.findViewById(id.view_divider);
			dividerView.setBackgroundResource(setting.dividerResId);
		}
	}

	public static NavigationSetting getSetting() {
		if (null == setting) {
			setting = new NavigationSetting();
		}
		return setting;
	}

	/**
	 * 隐藏导航栏
	 *
	 * @param hide Modifier： Modified Date： Modify：
	 */
	public void hideNavigation(boolean hide) {
		hideNavigation(hide, false);
	}

	public void hideNavigation(boolean hide, boolean anim) {
		if (isNavigationHide == hide) {
			return;
		}

		isNavigationHide = hide;
		if (anim) {
			ValueAnimator valueAnimator;

			//获取导航栏高度
			final int height = ((BaseFrameActivity) mContext).getNavigationHeight();

			if (hide) {
				valueAnimator = ValueAnimator.ofInt(height, 0);
			} else {
				valueAnimator = ValueAnimator.ofInt(0, height);
			}
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					Integer animatedValue = (Integer) animation.getAnimatedValue();

					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mNavigationRootView.getLayoutParams();
					params.height = animatedValue;
					mNavigationRootView.setLayoutParams(params);
				}
			});
			valueAnimator.setInterpolator(new LinearInterpolator());
			valueAnimator.setDuration(200);
			valueAnimator.setRepeatCount(0);
			valueAnimator.start();
		} else {
			ViewUtil.setViewVisibility(hide ? View.GONE : View.VISIBLE, mNavigationRootView);
		}
	}

	public boolean isHide() {
		return isNavigationHide;
	}

	public void setDividerResource(int color) {
		View dividerView = mNavigationRootView.findViewById(id.view_divider);
		dividerView.setBackgroundResource(color);
	}

	/**
	 * 隐藏导航栏地下的线
	 *
	 * @param hide
	 */
	public void hideNavigationLine(boolean hide) {
		View dividerView = mNavigationRootView.findViewById(id.view_divider);
		dividerView.setVisibility(hide ? View.GONE : View.VISIBLE);
	}

	/**
	 * 设置导航栏的中心标题
	 *
	 * @param title Modifier： Modified Date： Modify：
	 */
	public void setTitle(CharSequence title) {
		if (TextUtils.isEmpty(title)) {
			return;
		}
		// android:textColor="@color/color_global_colorblack3"
		// android:textSize="18sp"
		// TextView textView = new TextView(mContext);
		// textView.setText(title);
		mNavigationTitle = title.toString();

		if (null != mNavigationSettingCallback) {
			mNavigationSettingCallback.onNavigationTitleChanged(mContext, title.toString());
		}

		TextView textView = (TextView) View.inflate(mContext, R.layout.frame_navigation_title_layout, null);
		textView.setMaxEms(10);
		textView.setText(title);
		setTitle(textView);
	}

	/**
	 * 设置标题的res
	 *
	 * @param titleRes
	 */
	public void setTitle(@StringRes int titleRes) {
		setTitle(mContext.getResources().getString(titleRes));
	}

	public String getTitle() {
		return mNavigationTitle;
	}

	public void setTitle(View view) {
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mCenterLayout.setLayoutParams(lp);

		ViewUtil.setViewToGroup(mCenterLayout, view);
//        if (mCenterLayout.getChildCount() > 0) {
//            mCenterLayout.removeAllViews();
//        }
//        mCenterLayout.addView(view);
	}

	/**
	 * 获取中间的按钮区域，需要通过findViewById获取具体按钮
	 *
	 * @return Modifier： Modified Date： Modify：
	 */
	public View getCenterView() {
		return mCenterLayout;
	}

	/**
	 * 设置左边的按钮
	 *
	 * @param title
	 * @param listener Modifier： Modified Date： Modify：
	 */
	public void setLeftButton(CharSequence title, OnClickListener listener) {
		TextView button = (TextView) View.inflate(mContext, R.layout.frame_navigation_title_button_layout, null);
		button.setText(title);
		button.setOnClickListener(listener);
		setLeftButton(button);
	}

	public void setLeftButton(View view) {
		hideLeftButton();
		mLeftLayout.addView(view);
	}

	public void hideLeftButton() {
		mLeftLayout.removeAllViews();
	}

	public void hideRightButton(boolean hide) {
		ViewUtil.setViewVisibility(hide ? View.GONE : View.VISIBLE, mRightLayout);
	}

	/**
	 * 获取左边的按钮区域，需要通过findViewById获取具体按钮
	 *
	 * @return Modifier： Modified Date： Modify：
	 */
	public View getLeftView() {
		return mLeftLayout;
	}

	/**
	 * 设置右边的按钮
	 *
	 * @param title
	 * @param listener Modifier： Modified Date： Modify：
	 */
	public void setRightButton(CharSequence title, OnClickListener listener) {
		rightButton = (TextView) View.inflate(mContext, R.layout.frame_navigation_title_button_layout, null);
//        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		rightButton.setText(title);
		rightButton.setOnClickListener(listener);
		setRightButton(rightButton);
	}

	public View getRightButton() {
		if (rightButton != null) {
			return rightButton;
		}
		return null;
	}

	public void setRightButton(View view) {
		ViewUtil.setViewToGroup(mRightLayout, view);
//        hideRightButton();
//        mRightLayout.addView(view);
	}
	public void setLeftButton(View view,int margin){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
		if (null == lp) {
			lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		lp.leftMargin = ViewUtil.dip2px(view.getContext(), margin);
		lp.rightMargin = ViewUtil.dip2px(view.getContext(), margin);
		view.setLayoutParams(lp);

		setLeftButton(view);
	}

	public void setRightButton(View view, int margin) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
		if (null == lp) {
			lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		lp.leftMargin = ViewUtil.dip2px(view.getContext(), margin);
		lp.rightMargin = ViewUtil.dip2px(view.getContext(), margin);
		view.setLayoutParams(lp);

		setRightButton(view);
	}

	public void setRightButton(int resId, OnClickListener listener) {
		View view = View.inflate(mContext, layout.frame_navigation_title_image_layout, null);
		ImageView rightButton = (ImageView) view.findViewById(id.navigation_image_layout);
		rightButton.setImageResource(resId);
		rightButton.setOnClickListener(listener);
		setRightButton(view);
	}

	private void hideRightButton() {
		mRightLayout.removeAllViews();
	}

	/**
	 * 获取右边的按钮区域，需要通过findViewById获取具体按钮
	 *
	 * @return Modifier： Modified Date： Modify：
	 */
	public View getRightView() {
		return mRightLayout;
	}

	/**
	 * 设置导航栏的背景色
	 *
	 * @param color Modifier： Modified Date： Modify：
	 */
	public void setBackgroundColor(int color) {
		mNavigationRootView.setBackgroundColor(color);
	}

	public int getHeight() {
		mNavigationRootView.measure(0, 0);
		return mNavigationRootView.getMeasuredHeight();
	}

	/**
	 * 设置导航栏的背景色
	 *
	 * @param resId Modifier： Modified Date： Modify：
	 */
	public void setBackgroundResource(int resId) {
		mNavigationRootView.setBackgroundResource(resId);
	}

	public void setNavigationSettingCallback(NavigationSettingCallback callback) {
		this.mNavigationSettingCallback = callback;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ichsy.public_libs.view.navigation.NavigationListener#popBack()
	 */
	@Override
	public void popBack() {
		if (checkListener()) {
			mNavigationListener.popBack();
		}
	}

	@Override
	public void pushActivity(Intent intent) {
		if (checkListener()) {
			mNavigationListener.pushActivity(intent);
		}
	}

	@Override
	public void pushActivity(Class clz) {
		if (checkListener()) {
			mNavigationListener.pushActivity(clz);
		}
	}

	@Override
	public void pushActivity(Class clz, Intent intent) {
		if (checkListener()) {
			mNavigationListener.pushActivity(clz, intent);
		}
	}

	public boolean checkListener() {
		if (mNavigationListener == null) {
//        }
//            throw new NullPointerException("navigationListener is null");
			return false;
		} else {
			return true;
		}
	}

}
