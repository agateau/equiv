package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.content.Intent;
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
    public static final String EXTRA_MEAL_ITEM_POSITION = "com.greenyetilab.equiv.MEAL_ITEM_POSITION";

    private ProductList mProductList;
    private Meal mMeal;
    private Product mProduct = null;
    private MenuItem mSaveMenuItem;
    private int mMealItemPosition;
    private EditText mQuantityEdit;
    private TextView mUnitView;

    public static void addMealItem(Context context, Meal meal) {
        Intent intent = new Intent(context, MealItemDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_TAG, meal.getTag());
        context.startActivity(intent);
    }

    public static void editMealItem(Context context, Meal meal, int position) {
        Intent intent = new Intent(context, MealItemDetailActivity.class);
        intent.putExtra(EXTRA_MEAL_TAG, meal.getTag());
        intent.putExtra(EXTRA_MEAL_ITEM_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_item_detail_activity);
        mQuantityEdit = (EditText) findViewById(R.id.quantity_edit);
        mUnitView = (TextView) findViewById(R.id.quantity_unit);

        mProductList = Kernel.getInstance().getProductList();

        String mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        mMeal = Kernel.getInstance().getDay().getMealByTag(mealTag);

        mMealItemPosition = getIntent().getIntExtra(EXTRA_MEAL_ITEM_POSITION, -1);

        ListView listView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, R.layout.product_item, mProductList.getItems());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mProductList.getItems().get(position);
                onSelectProduct(product);
            }
        });

        if (mMealItemPosition != -1) {
            initFromMealItem(mMeal.getItems().get(mMealItemPosition));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_item_detail_activity_actions, menu);
        mSaveMenuItem = menu.findItem(R.id.action_save);
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

    private void initFromMealItem(MealItem mealItem) {
        mProduct = mealItem.getProduct();
        initQuantityFromProduct();
        mQuantityEdit.setText(String.valueOf(mealItem.getQuantity()));
    }

    private void initQuantityFromProduct() {
        String unit = mProduct.getUnit();
        if (TextUtils.isEmpty(unit)) {
            mUnitView.setVisibility(View.GONE);
        } else {
            mUnitView.setVisibility(View.VISIBLE);
            mUnitView.setText(unit);
        }

        float quantity = mProduct.getDefaultQuantity();
        mQuantityEdit.setText(String.format("%f", quantity));
    }

    private void onSelectProduct(Product product) {
        mProduct = product;
        initQuantityFromProduct();
        mQuantityEdit.requestFocus();
        updateMenuItems();
    }

    private void save() {
        EditText quantityEdit = (EditText) findViewById(R.id.quantity_edit);
        float quantity = Float.valueOf(quantityEdit.getText().toString());
        MealItem item = new MealItem(mProduct, quantity);
        if (mMealItemPosition == -1) {
            mMeal.add(item);
        } else {
            mMeal.set(mMealItemPosition, item);
        }

        NavUtils.navigateUpFromSameTask(this);
    }

    private void updateMenuItems() {
        mSaveMenuItem.setEnabled(mProduct != null && !TextUtils.isEmpty(mQuantityEdit.getText()));
    }
}
