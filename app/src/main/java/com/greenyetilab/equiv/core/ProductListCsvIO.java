package com.greenyetilab.equiv.core;

import com.greenyetilab.utils.CsvStreamReader;
import com.greenyetilab.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Load a ProductList from a CSV file
 */
public class ProductListCsvIO {
    public static ProductList read(InputStream in) throws IOException {
        ProductList productList = new ProductList();
        final ArrayList<Product> items = new ArrayList<>();
        CsvStreamReader.Listener listener = new CsvStreamReader.Listener() {
            boolean mValid = false;
            String mCategory;
            String mName;
            String mUnit;
            float mPotatoWeight;
            @Override
            public void onCell(int row, int column, String value) {
                if (column == 0) {
                    mCategory = value;
                } else if (column == 1) {
                    mName = value;
                } else if (column == 2) {
                    mPotatoWeight = Float.parseFloat(value);
                } else if (column == 3) {
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
                    float protein = Constants.PROTEIN_FOR_POTATO * (100 / mPotatoWeight);
                    Product product = new Product(mName, mUnit, protein);
                    items.add(product);
                } else {
                    NLog.e("Invalid row %d", row + 1);
                }
            }
        };
        CsvStreamReader reader = new CsvStreamReader(in, listener);
        reader.read();
        Collections.sort(items, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        productList.setItems(items);
        return productList;
    }
}
