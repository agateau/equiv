package com.agateau.equiv.core;

/**
 * A product from the ProductList
 */
public class Product {
    private final String mUuid;
    private final String mName;
    private final String mUnit;
    private final float mProteins;

    public Product(String uuid, String name, String unit, float proteins) {
        mUuid = uuid;
        mName = name;
        mUnit = unit;
        mProteins = proteins;
    }

    public String getUuid() {
        return mUuid;
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
