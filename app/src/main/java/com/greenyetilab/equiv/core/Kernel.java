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
    private int mCurrentTab = -1;

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

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
    }

    private void setupProductList() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Pommes de terre", "g", 0.02f));
        products.add(new Product("Balisto", "", 1.5f));
        mProductList.setItems(products);
    }

    private void setupDay() {
        Meal meal = new Meal("breakfast");
        mDay.add(meal);

        meal = new Meal("lunch");
        mDay.add(meal);

        meal = new Meal("snack");
        mDay.add(meal);

        meal = new Meal("dinner");
        mDay.add(meal);
    }

    private void setupConsumer() {
        mConsumer.setName("Clara");
        mConsumer.setMaxProteinPerDay(4f);
    }
}
