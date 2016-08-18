package com.agateau.equiv.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.agateau.equiv.R;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductCategory;
import com.agateau.equiv.core.ProductList;

import java.util.List;
import java.util.UUID;

public class CustomProductActivity extends AppCompatActivity {
    private static final String EXTRA_PRODUCT_UUID = "com.agateau.equiv.PRODUCT_UUID";

    private Product mProduct = null;
    private CategoryListAdapter mCategoryListAdapter;

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

        ProductList productList = Kernel.getExistingInstance().getProductList();
        List<ProductCategory> categoryList = productList.getCategoryList();
        mCategoryListAdapter = new CategoryListAdapter(this, categoryList);

        Spinner spinner = (Spinner) findViewById(R.id.product_category);
        spinner.setAdapter(mCategoryListAdapter);

        String uuid = getIntent().getStringExtra(EXTRA_PRODUCT_UUID);
        if (!TextUtils.isEmpty(uuid)) {
            mProduct = productList.findByUuid(UUID.fromString(uuid));
            initUiFromProduct();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_product_activity_actions, menu);
        if (mProduct == null) {
            MenuItem removeMenuItem = menu.findItem(R.id.action_remove);
            removeMenuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            save();
            finish();
            return true;
        }
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

        ProductList productList = Kernel.getExistingInstance().getProductList();
        if (mProduct == null) {
            // Add
            mProduct = new Product(category, name, unit, proteins);
            mProduct.setCustom(true);
            productList.add(mProduct);
        } else {
            // Edit
            mProduct.setCategory(category);
            mProduct.setName(name);
            mProduct.setUnit(unit);
            mProduct.setProteins(proteins);
            productList.handleProductUpdate(mProduct);
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
        edit.setText(FormatUtils.naturalRound(proteins));

        int radioButtonId = mProduct.getUnit() == Product.Unit.GRAM ? R.id.radio_unit_per_100g : R.id.radio_unit_per_u;
        RadioButton button = (RadioButton) findViewById(radioButtonId);
        button.setChecked(true);
    }
}
