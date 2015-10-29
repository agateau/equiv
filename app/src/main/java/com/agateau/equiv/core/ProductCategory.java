package com.agateau.equiv.core;

/**
 * The category of a product
 */
public class ProductCategory {
    private final String mCategoryId;

    public ProductCategory(String categoryId) {
        mCategoryId = categoryId;
    }

    public String getCategoryId() {
        return mCategoryId;
    }
}
