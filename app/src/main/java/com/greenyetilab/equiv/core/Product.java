package com.greenyetilab.equiv.core;

/**
 * A product from the ProductList
 */
public class Product {
    final String mName;
    private final String mUnit;
    final float mProtides;

    public Product(String name, String unit, float protides) {
        mName = name;
        mUnit = unit;
        mProtides = protides;
    }

    public String getName() {
        return mName;
    }

    public String getUnit() {
        return mUnit;
    }

    public float getProtides() {
        return mProtides;
    }

    public String toString() {
        return getName();
    }
}
