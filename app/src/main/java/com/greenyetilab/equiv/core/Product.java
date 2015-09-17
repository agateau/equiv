package com.greenyetilab.equiv.core;

/**
 * A product from the ProductList
 */
public class Product {
    final String mName;
    private final String mUnit;
    final double mProtides;

    public Product(String name, String unit, double protides) {
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

    public double getProtides() {
        return mProtides;
    }
}
