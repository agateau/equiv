/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.equiv.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import com.agateau.equiv.core.ProductStore;
import com.agateau.equiv.core.ProteinWeightUnit;
import com.agateau.utils.ui.ActionBarViewTabBuilder;

import java.util.Locale;
import java.util.UUID;


public class MealItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MEAL_TAG = "com.agateau.equiv.MEAL_TAG";
    public static final String EXTRA_MEAL_ITEM_POSITION = "com.agateau.equiv.MEAL_ITEM_POSITION";

    private static final int NEW_MEAL_ITEM_POSITION = -1;

    private Kernel mKernel;

    private ProductStore mProductStore;
    private ProductListAdapter mFullListAdapter;
    private ProductListAdapter mFavoritesListAdapter;
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

        mKernel = Kernel.getInstance(this);
        mProductStore = mKernel.getProductStore();

        String mealTag = null;
        if (savedInstanceState != null) {
            mealTag = savedInstanceState.getString("meal");
        }
        if (TextUtils.isEmpty(mealTag)) {
            mealTag = getIntent().getStringExtra(EXTRA_MEAL_TAG);
        }
        if (!TextUtils.isEmpty(mealTag)) {
            mMeal = mKernel.getDay().getMealByTag(mealTag);
            mMealItemPosition = getIntent().getIntExtra(EXTRA_MEAL_ITEM_POSITION, -1);

            setupTabs();
            setupMealEditor();
        }
    }

    private void setupTabs() {
        ListView fullListView = new ListView(this);
        ListView favoriteListView = new ListView(this);

        mFullListAdapter = new ProductListAdapter(this, mKernel, mProductStore.getItems());
        mFavoritesListAdapter = new ProductListAdapter(this, mKernel, mProductStore.getFavoriteItems());
        mProductStore.setOnProductStoreChangedListener(new ProductStore.OnProductStoreChangedListener() {
            @Override
            public void onFavoriteChanged() {
                mFavoritesListAdapter.notifyDataSetChanged();

                // Notify mFullListAdapter as well because it must update the state of its checkboxes
                mFullListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemListChanged() {
                mFullListAdapter.notifyDataSetChanged();
            }
        });

        fullListView.setAdapter(mFullListAdapter);
        favoriteListView.setAdapter(mFavoritesListAdapter);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getAdapter().getItem(position);
                onSelectProduct(product);
            }
        };
        fullListView.setOnItemClickListener(listener);
        favoriteListView.setOnItemClickListener(listener);

        AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getItemAtPosition(position);
                editCustomProduct(product);
                return true;
            }
        };

        fullListView.setLongClickable(true);
        favoriteListView.setLongClickable(true);

        fullListView.setOnItemLongClickListener(longClickListener);
        favoriteListView.setOnItemLongClickListener(longClickListener);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ActionBarViewTabBuilder builder = new ActionBarViewTabBuilder(actionBar, viewPager);
        builder.addTab(fullListView).setText(R.string.add_meal_item_tab_all);
        builder.addTab(favoriteListView).setText(R.string.add_meal_item_tab_favorites);
    }

    private void setupMealEditor() {
        TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    save();
                    return true;
                }
                return false;
            }
        };
        mProductNameView = (TextView) findViewById(R.id.product_name_view);
        mDetailsLayout = (LinearLayout) findViewById(R.id.details_layout);
        mQuantityEdit = (EditText) findViewById(R.id.quantity_edit);
        mQuantityEdit.setOnEditorActionListener(onEditorActionListener);
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
        mQuantityEquivEdit.setOnEditorActionListener(onEditorActionListener);
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

        if (mMealItemPosition != NEW_MEAL_ITEM_POSITION) {
            setTitle(R.string.edit_meal_item_title);
            initFromMealItem(mMeal.getItems().get(mMealItemPosition));
        }
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        String productUuid = bundle.getString("productUuid", "");
        if (!TextUtils.equals(productUuid, "")) {
            Product product = mProductStore.findByUuid(UUID.fromString(productUuid));
            onSelectProduct(product);
            mQuantityEquivEdit.setText(bundle.getString("quantityEquiv"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("meal", mMeal.getTag());
        if (mProduct != null) {
            bundle.putString("productUuid", mProduct.getUuid().toString());
            bundle.putString("quantityEquiv", mQuantityEquivEdit.getText().toString());
        }
        mKernel.writeFavorites(this);
        super.onSaveInstanceState(bundle);
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

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mFullListAdapter.getFilter().filter(newText);
                mFavoritesListAdapter.getFilter().filter(newText);
                return false;
            }
        });

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
        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        } else if (id == R.id.action_add_custom) {
            addCustomProduct();
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

    private void editCustomProduct(final Product product) {
        if (product.hasDefaultDetails() && !product.hasCustomDetails()) {
            // About to edit a default product for the first time, warn the user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.dialog_edit_default_product))
                    .setPositiveButton(R.string.dialog_continue, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            doEditCustomProduct(product);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .show();
        } else {
            doEditCustomProduct(product);
        }
    }

    private void doEditCustomProduct(Product product) {
        CustomProductActivity.startActivityWithProduct(this, product);
    }

    private void addCustomProduct() {
        CustomProductActivity.startActivity(this);
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
            String txt = FormatUtils.naturalRound(Locale.ENGLISH, quantity * ratio);
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
        String unit = mProduct.getUnit().toString();
        mQuantityUnitView.setText(unit);

        unit = mKernel.getWeightFormatter().getUnitString(WeightFormatter.UnitFormat.FULL);
        mQuantityEquivUnitView.setText(unit);
    }
}
