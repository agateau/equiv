package com.agateau.equiv.core;

import com.agateau.utils.CsvStreamReader;
import com.agateau.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Load a ProductList from a CSV file
 */
public class ProductListCsvIO {
    public static ProductList read(InputStream in) throws IOException {
        ProductList productList = new ProductList();
        final ArrayList<Product> items = new ArrayList<>();
        final HashMap<String, ProductCategory> categorySet = new HashMap<>();
        CsvStreamReader.Listener listener = new CsvStreamReader.Listener() {
            boolean mValid = false;
            String mUuid;
            String mCategoryId;
            String mName;
            String mUnit;
            float mProtein;
            @Override
            public void onCell(int row, int column, String value) {
                if (column == 0) {
                    mUuid = value;
                } else if (column == 1) {
                    mCategoryId = value;
                } else if (column == 2) {
                    mName = value;
                } else if (column == 3) {
                    mProtein = Float.parseFloat(value);
                } else if (column == 4) {
                    mUnit = value;
                    mValid = true;
                } else {
                    NLog.e("Row %d: unexpected column %d", row +1, column + 1);
                    mValid = false;
                }
            }
            @Override
            public void onStartRow(int row) {
                mValid = false;
            }

            @Override
            public void onEndRow(int row) {
                if (mValid) {
                    ProductCategory category = categorySet.get(mCategoryId);
                    if (category == null) {
                        category = new ProductCategory(mCategoryId);
                        categorySet.put(mCategoryId, category);
                    }
                    Product product = new Product(mUuid, category, mName, mUnit, mProtein);
                    items.add(product);
                } else {
                    NLog.e("Invalid row %d", row + 1);
                }
            }
        };
        CsvStreamReader reader = new CsvStreamReader(in, listener);
        reader.read();
        productList.setItems(items);
        return productList;
    }
}
