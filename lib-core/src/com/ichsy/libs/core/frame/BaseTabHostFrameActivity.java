package com.ichsy.libs.core.frame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ichsy.core_library.R;
import com.ichsy.libs.core.comm.view.ViewHelper;

/**
 * Created by liuyuhang on 16/10/25.
 */

public abstract class BaseTabHostFrameActivity extends BaseFrameActivity {
    private Fragment[] mTableFragments;
    private View[] mBottomViews;
    private int mCurrentFragmentPosition;//当前选择的fragment位置

    /**
     * 获取底部的导航栏高度
     */
    public abstract int getNavigationHeight();

    /**
     * 获取TableHostItem的数量
     *
     * @return
     */
    protected abstract int getTableHostCount();

    /**
     * tableItemView创建时候的回调
     *
     * @param position
     * @param childView
     */
    protected abstract void onTableHostViewCreated(int position, View childView);

    /**
     * 初始化布局，设置的view
     *
     * @return Modifier： Modified Date： Modify：
     */
    @Override
    protected int onLayoutInflate() {
        return R.layout.frame_main_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBottomTableHost();
    }

    /**
     * 初始化底部的TableHost
     */
    private void initBottomTableHost() {
        int tableHostCount = getTableHostCount();
        mTableFragments = new Fragment[tableHostCount];
        mBottomViews = new View[tableHostCount];

        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.layout_navigation_bottom);
        LayoutInflater inflater = LayoutInflater.from(this);

        bottomLayout.removeAllViews();
        for (int i = 0; i < tableHostCount; i++) {
            createTableHostItemView(inflater, i);

//            mHomePageOptionMap.put(i, new HomePageDataOption());
        }

        mBottomViews[0].performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getCurrentFragment() instanceof BaseFrameFragment) {
            ((BaseFrameFragment) getCurrentFragment()).onLazyOnResume(this);
        }
    }

    protected View createTableHostItemView(LayoutInflater inflater, final int position) {
        View childView = inflater.inflate(R.layout.frame_bottom_navigation_button, null);

        Button iconView = (Button) childView.findViewById(R.id.imageview_icon);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        childView.setLayoutParams(lp);


        onTableHostViewCreated(position, childView);

//        bottomLayout.addView(childView);

        ViewHelper.get(this).view(childView, iconView).listener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTableHostFragment(position);
            }
        });

        return childView;
    }

    /**
     * 切换fragment
     *
     * @param position
     */
    protected void swapTableHostFragment(int position) {
        mCurrentFragmentPosition = position;
        for (int i = 0; i < mTableFragments.length; i++) {
            mBottomViews[i].setSelected(i == position);
        }
//        TextView mBottomTextView = (TextView) mBottomViews[position].findViewById(R.id.textview_title);
        replaceFragment(R.id.layout_homepage_main, mTableFragments[position], true);


//        View mBottomNavigationView = findViewById(R.id.layout_navigation_bottom);
//        ViewUtil.setViewVisibility(mHomePageOptionMap.get(position).isBottomHide ? View.GONE : View.VISIBLE, mBottomNavigationView);
    }

    public Fragment getCurrentFragment() {
//        return mFragments[mCurrentFragmentPosition];
        return getFragment(mCurrentFragmentPosition);
    }

    public Fragment getFragment(int position) {
        return mTableFragments[position];
    }

    /**
     * 设置底部的分割线
     *
     * @param resId
     */
    public void setDivierResid(int resId) {
        findViewById(R.id.line_bottom).setBackgroundResource(resId);
    }


}
