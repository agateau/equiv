package com.agateau.equiv.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Day;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductCategory;
import com.agateau.equiv.core.ProductStore;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CustomProductActivity extends AppCompatActivity {
    private static final String EXTRA_PRODUCT_UUID = "com.agateau.equiv.PRODUCT_UUID";

    private Product mProduct = null;
    private CategoryListAdapter mCategoryListAdapter;
    private MenuItem mSaveMenuItem = null;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CustomProductActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityWithProduct(Context context, Product product) {
        Intent intent = new Intent(context, CustomProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_UUID, product.getUuid().toString());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product);

        ProductStore productStore = Kernel.getExistingInstance().getProductStore();
        List<ProductCategory> categoryList = productStore.getCategoryList();
        mCategoryListAdapter = new CategoryListAdapter(this, categoryList);

        Spinner spinner = (Spinner) findViewById(R.id.product_category);
        spinner.setAdapter(mCategoryListAdapter);

        TextWatcher updateMenuItemsWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateMenuItems();
            }
        };
        EditText editName = (EditText) findViewById(R.id.product_name);
        EditText editProteins = (EditText) findViewById(R.id.product_proteins);
        editName.addTextChangedListener(updateMenuItemsWatcher);
        editProteins.addTextChangedListener(updateMenuItemsWatcher);

        String uuid = getIntent().getStringExtra(EXTRA_PRODUCT_UUID);
        if (!TextUtils.isEmpty(uuid)) {
            mProduct = productStore.findByUuid(UUID.fromString(uuid));
            initUiFromProduct();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_product_activity_actions, menu);
        if (mProduct == null) {
            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            deleteMenuItem.setVisible(false);
        }
        mSaveMenuItem = menu.findItem(R.id.action_save);
        updateMenuItems();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_delete) {
            deleteProduct();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            save();
            finish();
            return true;
        }
        updateMenuItems();
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        Spinner spinner = (Spinner) findViewById(R.id.product_category);
        ProductCategory category = (ProductCategory) spinner.getSelectedItem();

        EditText edit = (EditText) findViewById(R.id.product_name);
        String name = edit.getText().toString();

        edit = (EditText) findViewById(R.id.product_proteins);
        float proteins = Float.valueOf(edit.getText().toString());

        RadioButton button = (RadioButton) findViewById(R.id.radio_unit_per_100g);
        Product.Unit unit = button.isChecked() ? Product.Unit.GRAM : Product.Unit.PORTION;

        if (unit == Product.Unit.GRAM) {
            // User entered proteins per 100g but we store proteins per 1g
            proteins /= 100;
        }

        ProductStore productStore = Kernel.getExistingInstance().getProductStore();
        if (mProduct == null) {
            // Add
            mProduct = new Product(category, name, unit, proteins);
            mProduct.setCustom(true);
            productStore.add(mProduct);
        } else {
            // Edit
            mProduct.setCategory(category);
            mProduct.setName(name);
            mProduct.setUnit(unit);
            mProduct.setProteins(proteins);
            productStore.handleProductUpdate(mProduct);
        }

        Kernel.getExistingInstance().saveCustomProductList(this);
    }

    private void initUiFromProduct() {
        getSupportActionBar().setTitle(R.string.title_activity_edit_custom_product);
        assert mProduct != null;

        Spinner spinner = (Spinner) findViewById(R.id.product_category);
        int position = mCategoryListAdapter.getPosition(mProduct.getCategory());
        spinner.setSelection(position);

        EditText edit = (EditText) findViewById(R.id.product_name);
        edit.setText(mProduct.getName());

        edit = (EditText) findViewById(R.id.product_proteins);
        float proteins = mProduct.getProteins();
        if (mProduct.getUnit() == Product.Unit.GRAM) {
            proteins *= 100;
        }
        edit.setText(FormatUtils.naturalRound(Locale.ENGLISH, proteins));

        int radioButtonId = mProduct.getUnit() == Product.Unit.GRAM ? R.id.radio_unit_per_100g : R.id.radio_unit_per_u;
        RadioButton button = (RadioButton) findViewById(radioButtonId);
        button.setChecked(true);
    }

    private void deleteProduct() {
        assert mProduct != null;
        Kernel kernel = Kernel.getExistingInstance();

        Day day = kernel.getDay();
        if (day.containsProduct(mProduct)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.dialog_delete_product_in_use))
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doDeleteProduct();
                }
            })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show();
        } else {
            doDeleteProduct();
        }
    }

    private void doDeleteProduct() {
        Kernel kernel = Kernel.getExistingInstance();
        kernel.getDay().removeProduct(mProduct);
        kernel.getProductStore().remove(mProduct);
        kernel.saveCustomProductList(this);
        finish();
    }

    private void updateMenuItems() {
        if (mSaveMenuItem == null) {
            return;
        }
        EditText editName = (EditText) findViewById(R.id.product_name);
        EditText editProteins = (EditText) findViewById(R.id.product_proteins);
        mSaveMenuItem.setEnabled(
                !TextUtils.isEmpty(editName.getText())
                && !TextUtils.isEmpty(editProteins.getText())
        );
    }
}
