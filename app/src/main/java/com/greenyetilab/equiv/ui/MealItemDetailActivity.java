package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Constants;
import com.greenyetilab.equiv.core.FormatUtils;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.Product;
import com.greenyetilab.equiv.core.ProductList;

import java.util.Locale;


public class MealItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MEAL_TAG = "com.greenyetilab.equiv.MEAL_TAG";
    public static final String EXTRA_MEAL_ITEM_POSITION = "com.greenyetilab.equiv.MEAL_ITEM_POSITION";

    private static final int NEW_MEAL_ITEM_POSITION = -1;

    private ProductList mProductList;
    private Meal mMeal;
    private Product mProduct = null;
    private MenuItem mSaveMenuItem;
    private int mMealItemPosition = NEW_MEAL_ITEM_POSITION;
    private TextView mProductNameView;
    private LinearLayout mDetailsLayout;
    private EditText mQuantityEdit;
    private TextView mUnitView;
    private EditText mQuantityEquivEdit;
    private boolean mUpdating = false;

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
        mProductNameView = (TextView) findViewById(R.id.product_name_view);
        mDetailsLayout = (LinearLayout) findViewById(R.id.details_layout);
        mQuantityEdit = (EditText) findViewById(R.id.quantity_edit);
        mQuantityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateQuantityEquivEdit();
            }
        });

        mUnitView = (TextView) findViewById(R.id.quantity_unit);

        mQuantityEquivEdit = (EditText) findViewById(R.id.quantity_equiv_edit);
        mQuantityEquivEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateQuantityEdit();
            }
        });

        Kernel kernel = Kernel.getInstance(this);
        mProductList = kernel.getProductList();

        String mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        mMeal = kernel.getDay().getMealByTag(mealTag);

        mMealItemPosition = getIntent().getIntExtra(EXTRA_MEAL_ITEM_POSITION, -1);

        ListView listView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, R.layout.product_item, R.id.product_item_text, mProductList.getItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView equivTextView = (TextView) view.findViewById(R.id.product_item_equiv_text);
                Product product = mProductList.getItems().get(position);
                float proteins = product.getProteins();
                String equivValue = FormatUtils.naturalRound(100 * Constants.PROTEIN_FOR_POTATO / proteins);
                String equiv = String.format("%s%s = \n100g PDT", equivValue, product.getUnit());
                equivTextView.setText(equiv);
                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mProductList.getItems().get(position);
                onSelectProduct(product);
            }
        });

        if (mMealItemPosition != NEW_MEAL_ITEM_POSITION) {
            setTitle(R.string.edit_meal_item_title);
            initFromMealItem(mMeal.getItems().get(mMealItemPosition));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_item_detail_activity_actions, menu);
        if (mMealItemPosition == NEW_MEAL_ITEM_POSITION) {
            MenuItem removeMenuItem = menu.findItem(R.id.action_remove);
            removeMenuItem.setVisible(false);
        }
        mSaveMenuItem = menu.findItem(R.id.action_save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            save();
            return true;
        } else if (id == R.id.action_remove) {
            remove();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFromMealItem(MealItem mealItem) {
        mProduct = mealItem.getProduct();
        updateDetailsLayout();
        mQuantityEdit.setText(String.valueOf(mealItem.getQuantity()));
    }

    private void onSelectProduct(Product product) {
        mProduct = product;
        updateDetailsLayout();
        mQuantityEdit.requestFocus();
        updateMenuItems();
    }

    private void save() {
        float quantity = Float.valueOf(mQuantityEdit.getText().toString());
        MealItem item = new MealItem(mProduct, quantity);
        if (mMealItemPosition == -1) {
            mMeal.add(item);
        } else {
            mMeal.set(mMealItemPosition, item);
        }

        NavUtils.navigateUpFromSameTask(this);
    }

    private void remove() {
        mMeal.remove(mMealItemPosition);
        NavUtils.navigateUpFromSameTask(this);
    }

    private void updateMenuItems() {
        mSaveMenuItem.setEnabled(mProduct != null && !TextUtils.isEmpty(mQuantityEdit.getText()));
    }

    private void updateQuantityEquivEdit() {
        if (mUpdating) {
            return;
        }
        mUpdating = true;
        try {
            float quantity;
            try {
                quantity = Float.valueOf(mQuantityEdit.getText().toString());
            } catch (NumberFormatException e) {
                mQuantityEquivEdit.setText("");
                return;
            }
            String txt = String.format(Locale.ENGLISH, "%.1f", quantity * mProduct.getProteins() / Constants.PROTEIN_FOR_POTATO);
            mQuantityEquivEdit.setText(txt);
        } finally {
            mUpdating = false;
        }
    }

    private void updateQuantityEdit() {
        if (mUpdating) {
            return;
        }
        mUpdating = true;
        try {
            float quantity;
            try {
                quantity = Float.valueOf(mQuantityEquivEdit.getText().toString());
            } catch (NumberFormatException e) {
                mQuantityEdit.setText("");
                return;
            }
            String txt = String.format(Locale.ENGLISH, "%.1f", quantity / mProduct.getProteins() * Constants.PROTEIN_FOR_POTATO);
            mQuantityEdit.setText(txt);
        } finally {
            mUpdating = false;
        }
    }

    private void updateDetailsLayout() {
        mDetailsLayout.setVisibility(View.VISIBLE);
        mProductNameView.setText(mProduct.getName());

        String unit = mProduct.getUnit();
        mUnitView.setText(unit);
        mQuantityEdit.setText("1");
    }
}
