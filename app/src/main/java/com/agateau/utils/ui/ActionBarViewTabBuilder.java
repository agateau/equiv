package com.agateau.utils.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

/**
 * Helper class to create view-based action bar tabs
 */
public class ActionBarViewTabBuilder {
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final ActionBar.TabListener mTabListener;
    private final ViewPagerAdapter mViewPagerAdapter;

    public ActionBarViewTabBuilder(ActionBar actionBar, ViewPager viewPager) {
        mActionBar = actionBar;
        mViewPager = viewPager;

        mViewPagerAdapter = new ViewPagerAdapter();

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        mTabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
    }

    public void addTab(CharSequence text, View view) {
        ActionBar.Tab tab = mActionBar.newTab()
                .setText(text)
                .setTabListener(mTabListener);
        mActionBar.addTab(tab);
        mViewPagerAdapter.addView(view);
    }
}
