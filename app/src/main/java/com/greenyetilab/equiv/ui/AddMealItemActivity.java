package com.greenyetilab.equiv.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Kernel;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;


public class AddMealItemActivity extends AppCompatActivity {

    public static final String EXTRA_MEAL_TAG = "com.greenyetilab.equiv.MEAL_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_item);

        String mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        Meal meal = Kernel.getInstance().getDay().getMealByTag(mealTag);

        ProductList productList = Kernel.getInstance().getProductList();
        ListView listView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, R.layout.product_item, productList.getItems());
        listView.setAdapter(adapter);

        /*
        MealItem item = new MealItem(mProductList.getItems().get(0), 100);
        mMeal.add(item);
        */
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
