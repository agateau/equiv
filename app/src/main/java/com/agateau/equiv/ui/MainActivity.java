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
import android.widget.Toast;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Meal;
import com.agateau.utils.log.NLog;
import com.agateau.utils.ui.ActionBarViewTabBuilder;


public class MainActivity extends AppCompatActivity implements Meal.Listener {
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

        for (Meal meal : mKernel.getDay().getMeals()) {
            meal.registerListener(this);
        }
        updateTitle();
    }

    @Override
    protected void onDestroy() {
        for (Meal meal : mKernel.getDay().getMeals()) {
            meal.unregisterListener(this);
        }
        super.onDestroy();
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
        final MealView view = new MealView(this, meal, mKernel.getWeightFormatter());
        ActionBar.Tab tab = builder.addTab(view);
        tab.setTag(meal.getTag());
        updateTabText(tab, meal);
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
        } else if (id == R.id.action_share) {
            shareCustomProductList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMealChanged(Meal meal) {
        mKernel.saveDay(this);
        updateTitle();
        int tabPosition = mKernel.getDay().getMeals().indexOf(meal);
        if (tabPosition == -1) {
            throw new RuntimeException("Meal " + meal + " not in meals");
        }
        ActionBar.Tab tab = getSupportActionBar().getTabAt(tabPosition);
        updateTabText(tab, meal);
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

    private void shareCustomProductList() {
        if (!mKernel.getProductStore().hasCustomItems()) {
            Toast.makeText(this, R.string.no_custom_products, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_share_custom_products))
                .setPositiveButton(R.string.dialog_share_custom_products_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mKernel.shareCustomProductList(MainActivity.this);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    private void updateTitle() {
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return;
        }
        WeightFormatter formatter = mKernel.getWeightFormatter();
        String total = formatter.format(mKernel.getDay().getProteinWeight(), WeightFormatter.UnitFormat.NONE);
        String maxAllowed = formatter.format(mKernel.getConsumer().getMaxProteinPerDay());
        String title = String.format("Equiv %s / %s", total, maxAllowed);
        bar.setTitle(title);
    }

    private void updateTabText(ActionBar.Tab tab, Meal meal) {
        int nameId = getResources().getIdentifier("meal_name_" + meal.getTag(), "string", getPackageName());
        String name = getString(nameId);
        WeightFormatter formatter = mKernel.getWeightFormatter();
        String total = formatter.format(meal.getProteinWeight(), WeightFormatter.UnitFormat.SHORT);
        String title = String.format("%s\n%s", name, total);
        tab.setText(title);
    }

    public void openAddMealItemActivity(View view) {
        int index = getSupportActionBar().getSelectedNavigationIndex();
        Meal meal = mKernel.getDay().getMeals().get(index);
        MealItemDetailActivity.addMealItem(this, meal);
    }
}
