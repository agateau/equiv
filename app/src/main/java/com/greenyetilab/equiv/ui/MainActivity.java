package com.greenyetilab.equiv.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Day;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;
import com.greenyetilab.equiv.ui.AddProductActivity;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private final Day mDay = new Day();
    private final ProductList mProductList = new ProductList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupProductList();
        setupDay();

        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.meal_tab_host);
        tabHost.setup();
        for (Meal meal : mDay.getMeals()) {
            createTabSpec(tabHost, meal);
        }
    }

    private void setupProductList() {
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(new Product("Pommes de terre", "g", 0.02));
        products.add(new Product("Ballisto", "", 1.5));
        mProductList.setItems(products);
    }

    private void setupDay() {
        mDay.getMeals()[0].add(new MealItem(mProductList.getItems().get(1), 0.5));
        mDay.getMeals()[1].add(new MealItem(mProductList.getItems().get(0), 100));
        mDay.getMeals()[2].add(new MealItem(mProductList.getItems().get(1), 1));
        mDay.getMeals()[3].add(new MealItem(mProductList.getItems().get(0), 100));
    }

    private void createTabSpec(TabHost tabHost, Meal meal) {
        final MealView view = new MealView(this, meal);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(meal.getName());
        tabSpec.setIndicator(meal.getName());
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
}
