package com.greenyetilab.equiv.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Consumer;
import com.greenyetilab.equiv.core.Day;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private final Consumer mConsumer = new Consumer();
    private final Day mDay = new Day();
    private final ProductList mProductList = new ProductList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupProductList();
        setupDay();
        setupConsumer();

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

    private void setupProductList() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Pommes de terre", "g", 0.02f));
        products.add(new Product("Ballisto", "", 1.5f));
        mProductList.setItems(products);
    }

    private void setupDay() {
        Meal meal = new Meal(getString(R.string.meal_breakfast));
        meal.add(new MealItem(mProductList.getItems().get(1), 0.5f));
        mDay.add(meal);

        meal = new Meal(getString(R.string.meal_lunch));
        meal.add(new MealItem(mProductList.getItems().get(0), 100));
        mDay.add(meal);

        meal = new Meal(getString(R.string.meal_snack));
        meal.add(new MealItem(mProductList.getItems().get(1), 1));
        mDay.add(meal);

        meal = new Meal(getString(R.string.meal_dinner));
        meal.add(new MealItem(mProductList.getItems().get(0), 100));
        mDay.add(meal);
    }

    private void setupConsumer() {
        mConsumer.setName("Clara");
        mConsumer.setMaxProtidePerDay(4f);
    }

    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.meal_tab_host);
        tabHost.setup();
        for (Meal meal : mDay.getMeals()) {
            createTabSpec(tabHost, meal);
        }
    }

    private void createTabSpec(TabHost tabHost, Meal meal) {
        final MealView view = new MealView(this, meal, mProductList);
        MealTabView tabView = new MealTabView(this, meal);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(meal.getName());
        tabSpec.setIndicator(tabView);
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return view;
            }
        });
        tabHost.addTab(tabSpec);
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
        String title = String.format("Equiv %.1f / %.1f gP", mDay.getProtideWeight(), mConsumer.getMaxProtidePerDay());
        getSupportActionBar().setTitle(title);
    }
}
