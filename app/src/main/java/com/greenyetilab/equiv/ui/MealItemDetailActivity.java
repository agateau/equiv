package com.greenyetilab.equiv.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Kernel;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;


public class MealItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MEAL_TAG = "com.greenyetilab.equiv.MEAL_TAG";
    private ProductList mProductList;
    private Meal mMeal;
    private Product mProduct = null;
    private MenuItem mSaveMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_item_detail_activity);

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
                selectProduct(product);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_item_detail_activity_actions, menu);
        mSaveMenuItem = menu.findItem(R.id.action_save);
        mSaveMenuItem.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectProduct(Product product) {
        mProduct = product;
        TextView unitView = (TextView) findViewById(R.id.quantity_unit);
        EditText quantityEdit = (EditText) findViewById(R.id.quantity_edit);

        String unit = mProduct.getUnit();
        if (TextUtils.isEmpty(unit)) {
            unitView.setVisibility(View.GONE);
        } else {
            unitView.setVisibility(View.VISIBLE);
            unitView.setText(unit);
        }

        float quantity = product.getDefaultQuantity();
        quantityEdit.setText(String.format("%f", quantity));
        quantityEdit.requestFocus();

        mSaveMenuItem.setEnabled(true);
    }

    private void save() {
        EditText quantityEdit = (EditText) findViewById(R.id.quantity_edit);
        float quantity = Float.valueOf(quantityEdit.getText().toString());
        MealItem item = new MealItem(mProduct, quantity);
        mMeal.add(item);

        NavUtils.navigateUpFromSameTask(this);
    }
}
