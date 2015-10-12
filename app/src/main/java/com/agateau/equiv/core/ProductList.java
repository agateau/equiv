package com.agateau.equiv.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains all available products
 */
public class ProductList {
    ArrayList<Product> mItems;
    ArrayList<Product> mFavoriteItems;

    public ArrayList<Product> getItems() {
        return mItems;
    }

    public ArrayList<Product> getFavoriteItems() {
        mFavoriteItems.clear();
        for (Product product : mItems) {
            if (product.isFavorite()) {
                mFavoriteItems.add(product);
            }
        }
        return mFavoriteItems;
    }

    public void setItems(ArrayList<Product> items) {
        mItems = items;

        // Allocate ArrayList only once, with a reasonable default capacity
        mFavoriteItems = new ArrayList<>(mItems.size());
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

    public Set<String> getFavoriteUuids() {
        Set<String> set = new HashSet<>();
        for (Product product : mItems) {
            if (product.isFavorite()) {
                set.add(product.getUuid());
            }
        }
        return set;
    }
}
