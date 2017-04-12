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
    private Details mDefaultDetails = null;
    private Details mCustomDetails = null;

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

    public enum Source {
        DEFAULT,
        CUSTOM
    }

    public static class Details {
        public final String name;
        public final Unit unit;
        public final float proteins;
        public final ProductCategory category;
        public final CollationKey collationKey;

        public Details(ProductCategory category, String name, Unit unit, float proteins) {
            this.category = category;
            this.name = name;
            this.unit = unit;
            this.proteins = proteins;
            this.collationKey = Collator.getInstance().getCollationKey(name);
        }
    }

    /**
     * Create a new product with a random id
     */
    public Product() {
        this(UUID.randomUUID(), null);
    }

    public Product(UUID uuid, Details details) {
        mUuid = uuid;
        mDefaultDetails = details;
    }

    public void setCustomDetails(Details details) {
        mCustomDetails = details;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public Details getDetails() {
        return mCustomDetails != null ? mCustomDetails : mDefaultDetails;
    }

    public ProductCategory getCategory() {
        return getDetails().category;
    }

    public String getName() {
        return getDetails().name;
    }

    public CollationKey getCollationKey() {
        return getDetails().collationKey;
    }

    public Unit getUnit() {
        return getDetails().unit;
    }

    public boolean isCustom() {
        return mCustomDetails != null;
    }

    public float getProteins() {
        return getDetails().proteins;
    }

    public String toString() {
        return getName();
    }
}
