/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.utils.ui;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
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

    public ActionBar.Tab addTab(View view) {
        ActionBar.Tab tab = mActionBar.newTab()
                .setTabListener(mTabListener);
        mActionBar.addTab(tab);
        mViewPagerAdapter.addView(view);
        return tab;
    }
}
