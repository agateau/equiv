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
package com.agateau.equiv.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Meal;
import com.agateau.utils.log.NLog;
import com.agateau.utils.ui.ActionBarViewTabBuilder;


public class MainActivity extends AppCompatActivity {
    private Kernel mKernel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NLog.i("");
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        mKernel = Kernel.getInstance(this);

        setContentView(R.layout.activity_main);

        setupTabs();
        int tab = mKernel.getCurrentTab();
        if (tab >= 0) {
            getSupportActionBar().getTabAt(tab).select();
        }

        Meal.Listener listener = new Meal.Listener() {
            @Override
            public void onMealChanged() {
                updateTitle();
                mKernel.saveDay(MainActivity.this);
            }
        };
        for (Meal meal : mKernel.getDay().getMeals()) {
            meal.registerListener(listener);
        }
        updateTitle();
    }

    private void setupTabs() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ActionBarViewTabBuilder builder = new ActionBarViewTabBuilder(actionBar, viewPager);

        for (Meal meal : mKernel.getDay().getMeals()) {
            createTabSpec(builder, meal);
        }
    }

    private void createTabSpec(ActionBarViewTabBuilder builder, final Meal meal) {
        final MealView view = new MealView(this, meal, mKernel.getWeightFormater());
        ActionBar.Tab tab = builder.addTab(view);
        tab.setTag(meal.getTag());

        final int mealTabIndex = tab.getPosition();
        Meal.Listener listener = new Meal.Listener() {
            @Override
            public void onMealChanged() {
                updateTab(meal, mealTabIndex);
            }
        };
        meal.registerListener(listener);
        updateTab(meal, mealTabIndex);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mKernel.setCurrentTab(getSupportActionBar().getSelectedTab().getPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettings();
            return true;
        } else if (id == R.id.action_about) {
            showAbout();
            return true;
        } else if (id == R.id.action_new_day) {
            onNewDay();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        SettingsActivity.start(this);
    }

    private void showAbout() {
        AboutActivity.start(this);
    }

    private void onNewDay() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.start_new_day)
                .setPositiveButton(R.string.start_new_day_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mKernel.getDay().clear();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updateTitle() {
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return;
        }
        WeightFormatter formatter = mKernel.getWeightFormater();
        String total = formatter.format(mKernel.getDay().getProteinWeight(), WeightFormatter.UnitFormat.NONE);
        String maxAllowed = formatter.format(mKernel.getConsumer().getMaxProteinPerDay());
        String title = String.format("Equiv %s / %s", total, maxAllowed);
        bar.setTitle(title);
    }

    private void updateTab(Meal meal, int tabIndex) {
        int nameId = getResources().getIdentifier("meal_name_" + meal.getTag(), "string", getPackageName());
        String name = getString(nameId);
        WeightFormatter formatter = mKernel.getWeightFormater();
        String total = formatter.format(meal.getProteinWeight(), WeightFormatter.UnitFormat.SHORT);
        String title = String.format("%s\n%s", name, total);

        ActionBar.Tab tab = getSupportActionBar().getTabAt(tabIndex);
        tab.setText(title);
    }

    public void openAddMealItemActivity(View view) {
        int index = getSupportActionBar().getSelectedNavigationIndex();
        Meal meal = mKernel.getDay().getMeals().get(index);
        MealItemDetailActivity.addMealItem(this, meal);
    }
}
