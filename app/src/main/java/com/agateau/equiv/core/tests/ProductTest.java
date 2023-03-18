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
package com.agateau.equiv.core.tests;

import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductCategory;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Product class
 */
public class ProductTest {
    @Test
    public void compareProductDetails() {
        ProductCategory category = new ProductCategory("bla");
        Product.Details d1 = new Product.Details(category, "x", Product.Unit.GRAM, 1);
        Product.Details d2 = new Product.Details(category, "x", Product.Unit.GRAM, 1);

        assertThat(d1.equals(d2), is(true));
    }

    @Test
    public void compareProductDetailsQuantityAlmostTheSame() {
        ProductCategory category = new ProductCategory("bla");
        Product.Details d1 = new Product.Details(category, "x", Product.Unit.GRAM, 1.395f);
        Product.Details d2 = new Product.Details(category, "x", Product.Unit.GRAM, 1.4f);
        assertThat(d1.equals(d2), is(true));

        d1 = new Product.Details(category, "x", Product.Unit.GRAM, 1.5f);
        d2 = new Product.Details(category, "x", Product.Unit.GRAM, 1.4f);
        assertThat(d1.equals(d2), is(false));
    }

    @Test
    public void setDetails() {
        ProductCategory category = new ProductCategory("bla");
        Product.Details d1 = new Product.Details(category, "d1", Product.Unit.GRAM, 1);
        Product product = new Product(UUID.randomUUID(), d1);

        assertThat(product.getName(), is("d1"));
        assertThat(product.hasCustomDetails(), is(false));

        Product.Details d2 = new Product.Details(category, "d2", Product.Unit.GRAM, 1);
        product.setCustomDetails(d2);

        assertThat(product.getName(), is("d2"));
        assertThat(product.hasCustomDetails(), is(true));
    }

    @Test
    public void setSameDetailsAsDefault() {
        ProductCategory category = new ProductCategory("bla");
        Product.Details d1 = new Product.Details(category, "default", Product.Unit.GRAM, 1);
        Product product = new Product(UUID.randomUUID(), d1);
        Product.Details d2 = new Product.Details(category, "custom", Product.Unit.GRAM, 1);
        product.setCustomDetails(d2);

        assertThat(product.getName(), is("custom"));
        assertThat(product.hasCustomDetails(), is(true));

        Product.Details d3 = new Product.Details(category, "default", Product.Unit.GRAM, 1);
        product.setCustomDetails(d3);

        assertThat(product.getName(), is("default"));
        assertThat(product.hasCustomDetails(), is(false));
    }

    @Test
    public void testHasDefaultDetails() {
        ProductCategory category = new ProductCategory("bla");
        Product product = new Product(UUID.randomUUID(), null);
        product.setCustomDetails(new Product.Details(category, "custom", Product.Unit.GRAM, 1));
        assertThat(product.hasDefaultDetails(), is(false));
        assertThat(product.hasCustomDetails(), is(true));

        product = new Product(UUID.randomUUID(), new Product.Details(category, "default", Product.Unit.GRAM, 1));
        assertThat(product.hasDefaultDetails(), is(true));
        assertThat(product.hasCustomDetails(), is(false));

        product.setCustomDetails(new Product.Details(category, "custom", Product.Unit.GRAM, 1));
        assertThat(product.hasDefaultDetails(), is(true));
        assertThat(product.hasCustomDetails(), is(true));
    }
}
