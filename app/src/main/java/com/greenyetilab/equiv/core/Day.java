package com.greenyetilab.equiv.core;

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

    public float getProtideWeight() {
        float weight = 0;
        for (Meal meal : mMeals) {
            weight += meal.getProtideWeight();
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
}
