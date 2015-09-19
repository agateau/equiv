package com.greenyetilab.equiv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Kernel;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;


public class AddMealItemActivity extends AppCompatActivity {

    public static final String EXTRA_MEAL_TAG = "com.greenyetilab.equiv.MEAL_TAG";
    private ProductList mProductList;
    private Meal mMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_item);

        String mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        mMeal = Kernel.getInstance().getDay().getMealByTag(mealTag);
        mProductList = Kernel.getInstance().getProductList();

        ListView listView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, R.layout.product_item, mProductList.getItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mProductList.getItems().get(position);
                addProduct(product);
            }
        });
    }

    private void addProduct(Product product) {
        float quantity = product.getDefaultQuantity();
        MealItem item = new MealItem(product, quantity);
        mMeal.add(item);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
