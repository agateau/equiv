package com.greenyetilab.equiv.core;

import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * An item in a meal
 */
public class MealItem {
    Product mProduct;
    double mQuantity;

    public MealItem(Product product, double quantity) {
        mProduct = product;
        mQuantity = quantity;
    }

    public Product getProduct() {
        return mProduct;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(float quantity) {
        mQuantity = quantity;
    }

    public String toString() {
        double protides = mProduct.getProtides() * mQuantity;
        String unit = mProduct.getUnit();
        if (!TextUtils.isEmpty(unit)) {
            unit = " " + unit;
        }
        return String.format("%s (%.1f%s): %.1f gP", mProduct.getName(), mQuantity, unit, protides);
    }
}
