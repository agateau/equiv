package com.agateau.equiv.core;

import com.agateau.csv.CsvStreamReader;
import com.agateau.csv.CsvStreamWriter;
import com.agateau.utils.log.NLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * Load a ProductList from a CSV file
 */
public class ProductListCsvIO {
    private static final int VERSION = 1;

    public static void read(InputStream in, ProductList productList) throws IOException {
        CsvStreamReader reader = new CsvStreamReader(in);
        if (!reader.loadNextRow()) {
            throw new RuntimeException( "csv is empty");
        }
        int version = reader.readIntCell();

        if (version == 1) {
            readV1(reader, productList);
        } else {
            throw new RuntimeException(String.format(Locale.getDefault(), "Don't know how to read product csv version %d", version));
        }
    }

    private static void readV1(CsvStreamReader reader, ProductList productList) throws IOException {
        ArrayList<Product> products = new ArrayList<>();

        while (reader.loadNextRow()) {
            UUID uuid = UUID.fromString(reader.readCell());
            String categoryId = reader.readCell();
            String name = reader.readCell();
            float protein = reader.readFloatCell();
            String value = reader.readCell();

            Product.Unit unit;
            try {
                unit = Product.Unit.fromString(value);
            } catch (ParseException exc) {
                NLog.e("Row %d: invalid unit %s", reader.getRow() + 1, value);
                return;
            }

            ProductCategory category = productList.findCategory(categoryId);
            if (category == null) {
                category = new ProductCategory(categoryId);
                productList.addCategory(category);
            }
            Product product = new Product(uuid, category, name, unit, protein);
            products.add(product);
        }

        productList.addAll(products);
    }

    public static void write(FileOutputStream out, ArrayList<Product> products) throws IOException {
        CsvStreamWriter writer = new CsvStreamWriter(out);
        writer.writeCell(VERSION);
        writer.endRow();
        for (Product product : products) {
            writer.writeCell(product.getUuid().toString());
            writer.writeCell(product.getCategory().toString());
            writer.writeCell(product.getName());
            writer.writeCell(product.getProteins());
            writer.writeCell(product.getUnit().toString());
            writer.endRow();
        }
    }
}
