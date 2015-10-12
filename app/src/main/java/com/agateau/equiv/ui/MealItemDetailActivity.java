package com.agateau.equiv.ui;

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

import com.agateau.equiv.R;
import com.agateau.equiv.core.Constants;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.Meal;
import com.agateau.equiv.core.MealItem;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductList;
import com.agateau.equiv.core.ProteinWeightUnit;

import java.util.Locale;


public class MealItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MEAL_TAG = "com.com.agateau.equiv.MEAL_TAG";
    public static final String EXTRA_MEAL_ITEM_POSITION = "com.com.agateau.equiv.MEAL_ITEM_POSITION";

    private static final int NEW_MEAL_ITEM_POSITION = -1;

    private Kernel mKernel;

    private ProductList mProductList;
    private Meal mMeal;
    private Product mProduct = null;
    private MenuItem mSaveMenuItem;
    private int mMealItemPosition = NEW_MEAL_ITEM_POSITION;
    private TextView mProductNameView;
    private LinearLayout mDetailsLayout;
    private EditText mQuantityEdit;
    private TextView mQuantityUnitView;
    private EditText mQuantityEquivEdit;
    private TextView mQuantityEquivUnitView;
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

        mQuantityUnitView = (TextView) findViewById(R.id.quantity_unit);

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

        mQuantityEquivUnitView = (TextView) findViewById(R.id.quantity_equiv_unit);

        mKernel = Kernel.getInstance(this);
        mProductList = mKernel.getProductList();

        String mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        mMeal = mKernel.getDay().getMealByTag(mealTag);

        mMealItemPosition = getIntent().getIntExtra(EXTRA_MEAL_ITEM_POSITION, -1);

        ListView listView = (ListView) findViewById(R.id.product_list_view);
        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, R.layout.product_item, R.id.product_item_text, mProductList.getItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView equivTextView = (TextView) view.findViewById(R.id.product_item_equiv_text);
                Product product = mProductList.getItems().get(position);
                float proteins = product.getProteins();

                WeightFormatter formatter = mKernel.getWeightFormater();

                float equiv;
                String equivUnit = product.getUnit();
                int ref;
                if (mKernel.getProteinUnit() == ProteinWeightUnit.POTATO) {
                    equiv = 100 / (proteins / Constants.PROTEIN_FOR_POTATO);
                    ref = 100;
                } else {
                    equiv = 1 / proteins;
                    ref = 1;
                }

                String equivText = String.format("%s%s = %d%s",
                        FormatUtils.naturalRound(equiv),
                        equivUnit,
                        ref,
                        formatter.getUnitString(WeightFormatter.UnitFormat.FULL));
                equivTextView.setText(equivText);
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
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        String productName = bundle.getString("product", "");
        if (!TextUtils.equals(productName, "")) {
            Product product = mProductList.findByName(productName);
            onSelectProduct(product);
            mQuantityEquivEdit.setText(bundle.getString("quantityEquiv"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (mProduct != null) {
            bundle.putString("product", mProduct.getName());
            bundle.putString("quantityEquiv", mQuantityEquivEdit.getText().toString());
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
        updateMenuItems();
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
        if (mSaveMenuItem == null) {
            return;
        }
        mSaveMenuItem.setEnabled(mProduct != null && !TextUtils.isEmpty(mQuantityEdit.getText()));
    }

    private float getEquivRatio() {
        if (mKernel.getProteinUnit() == ProteinWeightUnit.POTATO) {
            return mProduct.getProteins() / Constants.PROTEIN_FOR_POTATO;
        } else {
            return mProduct.getProteins();
        }
    }

    private void updateQuantityEquivEdit() {
        if (mProduct == null) {
            return;
        }
        updateQuantityEdits(mQuantityEdit, mQuantityEquivEdit, getEquivRatio());
    }

    private void updateQuantityEdit() {
        if (mProduct == null) {
            return;
        }
        updateQuantityEdits(mQuantityEquivEdit, mQuantityEdit, 1 / getEquivRatio());
    }

    private void updateQuantityEdits(TextView from, TextView to, float ratio) {
        if (mUpdating) {
            return;
        }
        mUpdating = true;
        try {
            float quantity;
            try {
                quantity = Float.valueOf(from.getText().toString());
            } catch (NumberFormatException e) {
                to.setText("");
                return;
            }
            String txt = String.format(Locale.ENGLISH, "%.2f", quantity * ratio);
            to.setText(txt);
        } finally {
            updateMenuItems();
            mUpdating = false;
        }
    }

    private void updateDetailsLayout() {
        mDetailsLayout.setVisibility(View.VISIBLE);
        mProductNameView.setText(mProduct.getName());

        mQuantityEdit.setText("");
        String unit = mProduct.getUnit();
        mQuantityUnitView.setText(unit);

        unit = mKernel.getWeightFormater().getUnitString(WeightFormatter.UnitFormat.FULL);
        mQuantityEquivUnitView.setText(unit);
    }
}