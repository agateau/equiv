package com.greenyetilab.equiv.core;

/**
 * A product from the ProductList
 */
public class Product {
    final String mName;
    private final String mUnit;
    final float mProteins;

    public Product(String name, String unit, float proteins) {
        mName = name;
        mUnit = unit;
        mProteins = proteins;
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
