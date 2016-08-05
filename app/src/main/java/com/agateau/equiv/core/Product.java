package com.agateau.equiv.core;

import java.text.CollationKey;
import java.text.Collator;
import java.text.ParseException;
import java.util.UUID;

/**
 * A product from the ProductList
 */
public class Product {
    private final UUID mUuid;
    private final String mName;
    private final Unit mUnit;
    private final float mProteins;
    private final ProductCategory mCategory;
    private final CollationKey mCollationKey;
    private boolean mIsCustom = false;

    public enum Unit {
        GRAM,
        PORTION;

        public String toString() {
            return this == GRAM ? "g" : "u";
        }

        public static Unit fromString(String unit) throws ParseException {
            switch (unit) {
            case "g":
                return GRAM;
            case "u":
                return PORTION;
            }
            throw new ParseException(String.format("Invalid unit value '%s'", unit), 0);
        }
    }

    /**
     * Create a new product with a random id
     */
    public Product(ProductCategory category, String name, Unit unit, float proteins) {
        this(UUID.randomUUID(), category, name, unit, proteins);
    }

    public Product(UUID uuid, ProductCategory category, String name, Unit unit, float proteins) {
        mUuid = uuid;
        mCategory = category;
        mName = name;
        mUnit = unit;
        mProteins = proteins;
        mCollationKey = Collator.getInstance().getCollationKey(mName);
    }

    public UUID getUuid() {
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

    public Unit getUnit() {
        return mUnit;
    }

    public boolean isCustom() {
        return mIsCustom;
    }

    public void setCustom(boolean custom) {
        mIsCustom = custom;
    }

    public float getProteins() {
        return mProteins;
    }

    public String toString() {
        return getName();
    }
}
