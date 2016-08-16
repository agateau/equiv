package com.agateau.equiv.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductCategory;
import com.agateau.equiv.core.ProductList;

import java.util.List;

public class CustomProductActivity extends AppCompatActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CustomProductActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product);

        List<ProductCategory> categoryList = Kernel.getExistingInstance().getProductList().getCategoryList();
        CategoryListAdapter adapter = new CategoryListAdapter(this, categoryList);

        Spinner spinner = (Spinner) findViewById(R.id.product_category);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_product_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            addProduct();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addProduct() {
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

        Product product = new Product(category, name, unit, proteins);
        product.setCustom(true);

        ProductList productList = Kernel.getExistingInstance().getProductList();
        productList.add(product);
        Kernel.getExistingInstance().saveCustomProductList(this);
    }
}
