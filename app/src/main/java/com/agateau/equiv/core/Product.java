package com.agateau.equiv.core;

import java.text.CollationKey;
import java.text.Collator;

/**
 * A product from the ProductList
 */
public class Product {
    private final String mUuid;
    private final String mName;
    private final String mUnit;
    private final float mProteins;
    private final ProductCategory mCategory;
    private final CollationKey mCollationKey;

    public Product(String uuid, ProductCategory category, String name, String unit, float proteins) {
        mUuid = uuid;
        mCategory = category;
        mName = name;
        mUnit = unit;
        mProteins = proteins;
        mCollationKey = Collator.getInstance().getCollationKey(mName);
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

    public CollationKey getCollationKey() {
        return mCollationKey;
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
