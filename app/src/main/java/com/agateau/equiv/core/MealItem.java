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
        Product.Unit unit = mProduct.getUnit();
        return String.format("%s (%s%s)", mProduct.getName(), FormatUtils.naturalRound(mQuantity), unit);
    }
}
