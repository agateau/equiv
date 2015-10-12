package com.agateau.equiv.core;

/**
 * An item in a meal
 */
public class MealItem {
    Product mProduct;
    float mQuantity;

    public MealItem(Product product, float quantity) {
        mProduct = product;
        mQuantity = quantity;
    }

    public Product getProduct() {
        return mProduct;
    }

    public float getQuantity() {
        return mQuantity;
    }

    public void setQuantity(float quantity) {
        mQuantity = quantity;
    }

    public float getProteinWeight() {
        return mQuantity * mProduct.getProteins();
    }

    public String toString() {
        String unit = mProduct.getUnit();
        return String.format("%s (%s%s)", mProduct.getName(), FormatUtils.naturalRound(mQuantity), unit);
    }
}