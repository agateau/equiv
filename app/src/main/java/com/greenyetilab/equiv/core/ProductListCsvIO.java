package com.greenyetilab.equiv.core;

import com.greenyetilab.utils.CsvStreamReader;
import com.greenyetilab.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Load a ProductList from a CSV file
 */
public class ProductListCsvIO {
    public static ProductList read(InputStream in) throws IOException {
        ProductList productList = new ProductList();
        final ArrayList<Product> items = new ArrayList<>();
        CsvStreamReader.Listener listener = new CsvStreamReader.Listener() {
            boolean mValid = false;
            String mName;
            String mUnit;
            float mProtein;
            @Override
            public void onCell(int row, int column, String value) {
                if (column == 0) {
                    mName = value;
                } else if (column == 1) {
                    mUnit = value;
                } else if (column == 2) {
                    mProtein = Float.parseFloat(value);
                    mValid = true;
                } else {
                    NLog.e("Row %d: unexpected column %d", row +1, column + 1);
                    mValid = false;
                }
            }
            @Override
            public void onStartRow() {
                mValid = false;
            }

            @Override
            public void onEndRow() {
                if (mValid) {
                    Product product = new Product(mName, mUnit, mProtein);
                    items.add(product);
                }
            }
        };
        CsvStreamReader reader = new CsvStreamReader(in, listener);
        reader.read();
        productList.setItems(items);
        return productList;
    }
}
