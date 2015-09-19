package com.greenyetilab.equiv.core;

import java.util.ArrayList;

/**
 * Holds all the data
 */
public class Kernel {
    private final Consumer mConsumer = new Consumer();
    private final Day mDay = new Day();
    private final ProductList mProductList = new ProductList();

    private static Kernel sInstance = null;

    Kernel() {
        setupProductList();
        setupDay();
        setupConsumer();
    }

    public static Kernel getInstance() {
        if (sInstance == null) {
            sInstance = new Kernel();
        }
        return sInstance;
    }

    public Consumer getConsumer() {
        return mConsumer;
    }

    public Day getDay() {
        return mDay;
    }

    public ProductList getProductList() {
        return mProductList;
    }

    private void setupProductList() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Pommes de terre", "g", 0.02f));
        products.add(new Product("Ballisto", "", 1.5f));
        mProductList.setItems(products);
    }

    private void setupDay() {
        Meal meal = new Meal("breakfast");
        meal.add(new MealItem(mProductList.getItems().get(1), 0.5f));
        mDay.add(meal);

        meal = new Meal("lunch");
        meal.add(new MealItem(mProductList.getItems().get(0), 100));
        mDay.add(meal);

        meal = new Meal("snack");
        meal.add(new MealItem(mProductList.getItems().get(1), 1));
        mDay.add(meal);

        meal = new Meal("dinner");
        meal.add(new MealItem(mProductList.getItems().get(0), 100));
        mDay.add(meal);
    }

    private void setupConsumer() {
        mConsumer.setName("Clara");
        mConsumer.setMaxProteinPerDay(4f);
    }

}
