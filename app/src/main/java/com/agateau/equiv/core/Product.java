/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.equiv.core;

import java.text.CollationKey;
import java.text.Collator;
import java.text.ParseException;
import java.util.UUID;

/**
 * A product from the ProductStore
 */
public class Product {
    private final UUID mUuid;
    private String mName;
    private Unit mUnit;
    private float mProteins;
    private ProductCategory mCategory;
    private CollationKey mCollationKey;
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
        setName(name);
        mUnit = unit;
        mProteins = proteins;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public ProductCategory getCategory() {
        return mCategory;
    }

    public void setCategory(ProductCategory category) {
        mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        mCollationKey = Collator.getInstance().getCollationKey(mName);
    }

    public CollationKey getCollationKey() {
        return mCollationKey;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public void setUnit(Unit unit) {
        mUnit = unit;
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

    public void setProteins(float proteins) {
        mProteins = proteins;
    }

    public String toString() {
        return getName();
    }
}
