package com.greenyetilab.equiv.core;

import com.greenyetilab.equiv.R;

/**
 * A day of meals
 */
public class Day {
    final Meal[] mMeals = new Meal[4];

    public Day() {
        mMeals[0] = new Meal("R.string.meal_breakfast");
        mMeals[1] = new Meal("R.string.meal_lunch");
        mMeals[2] = new Meal("R.string.meal_snack");
        mMeals[3] = new Meal("R.string.meal_dinner");
    }

    public Meal[] getMeals() {
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
}
