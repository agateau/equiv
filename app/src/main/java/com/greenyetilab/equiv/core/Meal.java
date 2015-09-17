package com.greenyetilab.equiv.core;

import java.util.ArrayList;

/**
 * A meal in a day
 */
public class Meal {
    final String mName;
    final ArrayList<MealItem> mItems = new ArrayList<>();

    public Meal(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public String toString() {
        return getName();
    }

    public ArrayList<MealItem> getItems() {
        return mItems;
    }

    public void add(MealItem item) {
        mItems.add(item);
    }

    float getProtideWeight() {
        float weight = 0;
        for(MealItem item : mItems) {
            weight += item.getQuantity() * item.getProduct().getProtides();
        }
        return weight;
    }

    void clear() {
        mItems.clear();
    }
}
