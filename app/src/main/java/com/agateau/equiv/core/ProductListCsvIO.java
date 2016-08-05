package com.agateau.equiv.core;

import com.agateau.utils.CsvStreamReader;
import com.agateau.utils.CsvStreamWriter;
import com.agateau.utils.log.NLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Load a ProductList from a CSV file
 */
public class ProductListCsvIO {
    public static void read(InputStream in, final ProductList productList) throws IOException {
        final ArrayList<Product> products = new ArrayList<>();
        CsvStreamReader.Listener listener = new CsvStreamReader.Listener() {
            boolean mValid = false;
            UUID mUuid;
            String mCategoryId;
            String mName;
            Product.Unit mUnit;
            float mProtein;
            @Override
            public void onCell(int row, int column, String value) {
                if (column == 0) {
                    mUuid = UUID.fromString(value);
                } else if (column == 1) {
                    mCategoryId = value;
                } else if (column == 2) {
                    mName = value;
                } else if (column == 3) {
                    mProtein = Float.parseFloat(value);
                } else if (column == 4) {
                    try {
                        mUnit = Product.Unit.fromString(value);
                        mValid = true;
                    } catch (ParseException exc) {
                        NLog.e("Row %d: invalid unit %s", row + 1, value);
                        mValid = false;
                    }
                } else {
                    NLog.e("Row %d: unexpected column %d", row + 1, column + 1);
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
                    ProductCategory category = productList.findCategory(mCategoryId);
                    if (category == null) {
                        category = new ProductCategory(mCategoryId);
                        productList.addCategory(category);
                    }
                    Product product = new Product(mUuid, category, mName, mUnit, mProtein);
                    products.add(product);
                } else {
                    NLog.e("Invalid row %d", row + 1);
                }
            }
        };
        CsvStreamReader reader = new CsvStreamReader(in, listener);
        reader.read();
        productList.addAll(products);
    }

    public static void write(FileOutputStream out, ArrayList<Product> products) throws IOException {
        CsvStreamWriter writer = new CsvStreamWriter(out);
        for (Product product : products) {
            writer.writeCell(product.getUuid().toString());
            writer.writeCell(product.getCategory().toString());
            writer.writeCell(product.getName());
            writer.writeCell(Float.toString(product.getProteins()));
            writer.writeCell(product.getUnit().toString());
            writer.endRow();
        }
    }
}
