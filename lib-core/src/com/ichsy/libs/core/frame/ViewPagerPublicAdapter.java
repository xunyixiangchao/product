package com.ichsy.libs.core.frame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager公用的adapter
 * Created by liuyuhang on 16/6/21.
 */
public class ViewPagerPublicAdapter extends FragmentPagerAdapter {
    private final int STATUS_NOT_READY = 1;
    private final int STATUS_READY = 2;

    private List<ViewPagerVo> fragments;
    private FragmentManager fm;

    private class ViewPagerVo {
        private BaseFrameFragment fragment;
        private int status;
    }


//    private boolean dontDestroy = false;

    public ViewPagerPublicAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;

        fragments = new ArrayList<>();
    }

    public void cleanAdapter() {
        if (null != fragments) {
            fragments.clear();
        }
    }

    public void addFragment(BaseFrameFragment fragment) {
        ViewPagerVo viewPagerVo = new ViewPagerVo();
        viewPagerVo.fragment = fragment;
        viewPagerVo.status = STATUS_NOT_READY;
        fragments.add(viewPagerVo);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).fragment;
    }

    @Override
    public int getCount() {
        return null == fragments ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).fragment.getTitle();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPagerVo viewPagerVo = fragments.get(position);

        if (viewPagerVo.status == STATUS_NOT_READY) {
            viewPagerVo.status = STATUS_READY;
            return super.instantiateItem(container, position);
        } else {
            fm.beginTransaction().show(viewPagerVo.fragment).commitAllowingStateLoss();
            return viewPagerVo.fragment;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPagerVo viewPagerVo = fragments.get(position);
        fm.beginTransaction().hide(viewPagerVo.fragment).commitAllowingStateLoss();
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
