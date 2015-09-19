package com.greenyetilab.equiv.ui;

import android.os.Bundle;
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
import com.greenyetilab.equiv.core.Kernel;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.ProductList;


public class MainActivity extends AppCompatActivity {
    private final Consumer mConsumer = Kernel.getInstance().getConsumer();
    private final Day mDay = Kernel.getInstance().getDay();
    private final ProductList mProductList = Kernel.getInstance().getProductList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupTabs();

        Meal.Listener listener = new Meal.Listener() {
            @Override
            public void onMealChanged() {
                updateTitle();
            }
        };
        for (Meal meal : mDay.getMeals()) {
            meal.registerListener(listener);
        }

        updateTitle();
    }

    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.meal_tab_host);
        tabHost.setup();
        for (Meal meal : mDay.getMeals()) {
            createTabSpec(tabHost, meal);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTitle() {
        String title = String.format("Equiv %.1f / %.1f gP", mDay.getProteinWeight(), mConsumer.getMaxProteinPerDay());
        getSupportActionBar().setTitle(title);
    }

    private void updateTab(Meal meal, int tabIndex) {
        int nameId = getResources().getIdentifier("meal_name_" + meal.getTag(), "string", getPackageName());
        String name = getString(nameId);
        String title = String.format("%s\n%.1f gP", name, meal.getProteinWeight());

        TabHost tabHost = (TabHost) findViewById(R.id.meal_tab_host);
        TextView view = (TextView) tabHost.getTabWidget().getChildAt(tabIndex).findViewById(android.R.id.title);
        view.setText(title);
    }
}
