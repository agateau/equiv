/*
Copyright 2017 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.equiv.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for ProductStore
 */
public class ProductStoreTest {
    @Test
    public void emptyStore() {
        ProductStore store = new ProductStore();
        assertThat(store.getItems().isEmpty(), is(true));
    }

    @Test
    public void loadDefaultProduct() {
        ProductStore store = new ProductStore();
        ProductCategory category = new ProductCategory("bla");
        store.addCategory(category);
        Product.Details details = new Product.Details(category, "carrot", Product.Unit.GRAM, 1);
        UUID uuid = UUID.randomUUID();
        store.load(uuid, details, Product.Source.DEFAULT);

        assertThat(store.hasCustomItems(), is(false));

        ArrayList<Product> products = store.getItems();
        assertThat(products.size(), is(1));

        Product product = products.get(0);
        assertThat(product.getDetails(), is(details));
    }

    @Test
    public void loadUserCreatedProduct() {
        ProductStore store = new ProductStore();
        ProductCategory category = new ProductCategory("bla");
        store.addCategory(category);
        Product.Details details = new Product.Details(category, "carrot", Product.Unit.GRAM, 1);
        UUID uuid = UUID.randomUUID();
        store.load(uuid, details, Product.Source.CUSTOM);

        assertThat(store.hasCustomItems(), is(true));

        ArrayList<Product> products = store.getItems();
        assertThat(products.size(), is(1));

        Product product = products.get(0);
        assertThat(product.getDetails(), is(details));
        assertThat(product.hasCustomDetails(), is(true));
    }

    @Test
    public void loadUserModifiedProduct() {
        ProductStore store = new ProductStore();
        ProductCategory category = new ProductCategory("bla");
        store.addCategory(category);
        Product.Details details = new Product.Details(category, "carrot", Product.Unit.GRAM, 1);
        UUID uuid = UUID.randomUUID();
        store.load(uuid, details, Product.Source.DEFAULT);
        assertThat(store.hasCustomItems(), is(false));

        Product.Details userDetails = new Product.Details(category, "carrotz", Product.Unit.GRAM, 2);
        store.load(uuid, userDetails, Product.Source.CUSTOM);
        assertThat(store.hasCustomItems(), is(true));

        ArrayList<Product> products = store.getItems();
        assertThat(products.size(), is(1));

        Product product = products.get(0);
        assertThat(product.getDetails(), is(userDetails));
        assertThat(product.hasCustomDetails(), is(true));
    }
}
