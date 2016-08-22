package com.agateau.equiv.core;

import java.util.ArrayList;

/**
 * A day of meals
 */
public class Day {
    final ArrayList<Meal> mMeals = new ArrayList<>();

    public Day() {
    }

    public ArrayList<Meal> getMeals() {
        return mMeals;
    }

    public float getProteinWeight() {
        float weight = 0;
        for (Meal meal : mMeals) {
            weight += meal.getProteinWeight();
        }
        return weight;
    }

    public void clear() {
        for (Meal meal : mMeals) {
            meal.clear();
        }
    }

    public void add(Meal meal) {
        mMeals.add(meal);
    }

    public Meal getMealByTag(String tag) {
        for (Meal meal: mMeals) {
            if (meal.getTag().equals(tag)) {
                return meal;
            }
        }
        throw new RuntimeException("No meal with tag " + tag);
    }

    public boolean isEmpty() {
        for (Meal meal: mMeals) {
            if (!meal.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean containsProduct(Product product) {
        for (Meal meal: mMeals) {
            if (meal.containsProduct(product)) {
                return true;
            }
        }
        return false;
    }

    public void removeProduct(Product product) {
        for (Meal meal: mMeals) {
            meal.removeProduct(product);
        }
    }
}
