package com.agateau.equiv.core;

import java.util.ArrayList;

/**
 * Contains all available products
 */
public class ProductList {
    ArrayList<Product> mItems;

    public ArrayList<Product> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Product> items) {
        mItems = items;
    }

    public Product findByUuid(String uuid) {
        for (Product product : mItems) {
            if (product.getUuid().equals(uuid)) {
                return product;
            }
        }
        return null;
    }

    public Product findByName(String productName) {
        for (Product product : mItems) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }
}
