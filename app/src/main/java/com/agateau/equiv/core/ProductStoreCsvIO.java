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
 * Load and save product lists to/from CSV files
 */
public class ProductStoreCsvIO {
    private static final int VERSION = 1;

    public static void read(InputStream in, ProductStore productStore, Product.Source source) throws IOException {
        CsvStreamReader reader = new CsvStreamReader(in);
        if (!reader.loadNextRow()) {
            throw new RuntimeException( "csv is empty");
        }
        int version = reader.readIntCell();

        if (version == 1) {
            readV1(reader, productStore, source);
        } else {
            throw new RuntimeException(String.format(Locale.getDefault(), "Don't know how to read product csv version %d", version));
        }
    }

    private static void readV1(CsvStreamReader reader, ProductStore productStore, Product.Source source) throws IOException {
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

            ProductCategory category = productStore.findCategory(categoryId);
            if (category == null) {
                category = new ProductCategory(categoryId);
                productStore.addCategory(category);
            }
            Product.Details details = new Product.Details(category, name, unit, protein);
            productStore.load(uuid, details, source);
        }

        productStore.finishLoad();
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
