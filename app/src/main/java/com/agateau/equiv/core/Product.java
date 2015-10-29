package com.agateau.equiv.core;

/**
 * A product from the ProductList
 */
public class Product {
    private final String mUuid;
    private final String mName;
    private final String mUnit;
    private final float mProteins;
    private final ProductCategory mCategory;

    public Product(String uuid, ProductCategory category, String name, String unit, float proteins) {
        mUuid = uuid;
        mCategory = category;
        mName = name;
        mUnit = unit;
        mProteins = proteins;
    }

    public String getUuid() {
        return mUuid;
    }

    public ProductCategory getCategory() {
        return mCategory;
    }

    public String getName() {
        return mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public float getProteins() {
        return mProteins;
    }

    public String toString() {
        return getName();
    }
}
