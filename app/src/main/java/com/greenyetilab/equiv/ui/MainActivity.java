package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Consumer;
import com.greenyetilab.equiv.core.Day;
import com.greenyetilab.equiv.core.DayJsonIO;
import com.greenyetilab.equiv.core.Kernel;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.ProductList;
import com.greenyetilab.utils.log.NLog;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String DAY_JSON = "day.json";
    private final Consumer mConsumer = Kernel.getInstance().getConsumer();
    private final Day mDay = Kernel.getInstance().getDay();
    private final ProductList mProductList = Kernel.getInstance().getProductList();
    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NLog.i("");
        if (mDay.isEmpty()) {
            loadDay();
        }

        setContentView(R.layout.activity_main);

        setupTabs();
        int tab = Kernel.getInstance().getCurrentTab();
        if (tab >= 0) {
            mTabHost.setCurrentTab(tab);
        }

        Meal.Listener listener = new Meal.Listener() {
            @Override
            public void onMealChanged() {
                updateTitle();
                saveDay();
            }
        };
        for (Meal meal : mDay.getMeals()) {
            meal.registerListener(listener);
        }

        updateTitle();
    }

    private void loadDay() {
        NLog.i("");
        FileInputStream stream;
        try {
            stream = openFileInput(DAY_JSON);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            DayJsonIO.read(stream, mDay, mProductList);
            stream.close();
        } catch (IOException | JSONException e) {
            NLog.e("Failed to load day: %s", e);
        }
    }

    private void saveDay() {
        NLog.i("");
        FileOutputStream stream;
        try {
            stream = openFileOutput(DAY_JSON, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            NLog.e("Failed to open %s for writing: %s", DAY_JSON, e);
            return;
        }
        try {
            DayJsonIO.write(stream, mDay);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            NLog.e("Failed to save day: %s", e);
        }
    }

    private void setupTabs() {
        mTabHost = (TabHost) findViewById(R.id.meal_tab_host);
        mTabHost.setup();
        for (Meal meal : mDay.getMeals()) {
            createTabSpec(mTabHost, meal);
        }
    }

    private void createTabSpec(TabHost tabHost, final Meal meal) {
        final MealView view = new MealView(this, meal, mProductList);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(meal.getTag());
        tabSpec.setIndicator(""); // Set by updateTab
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return view;
            }
        });
        final int mealTabIndex = tabHost.getTabWidget().getTabCount();
        tabHost.addTab(tabSpec);
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
        Kernel.getInstance().setCurrentTab(mTabHost.getCurrentTab());
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
            return true;
        } else if (id == R.id.action_new_day) {
            onNewDay();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNewDay() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.start_new_day)
                .setPositiveButton(R.string.start_new_day_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDay.clear();
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
        String title = String.format("Equiv %.1f / %.1f gP", mDay.getProteinWeight(), mConsumer.getMaxProteinPerDay());
        bar.setTitle(title);
    }

    private void updateTab(Meal meal, int tabIndex) {
        int nameId = getResources().getIdentifier("meal_name_" + meal.getTag(), "string", getPackageName());
        String name = getString(nameId);
        String title = String.format("%s\n%.1f gP", name, meal.getProteinWeight());

        TextView view = (TextView) mTabHost.getTabWidget().getChildAt(tabIndex).findViewById(android.R.id.title);
        view.setText(title);
    }
}
