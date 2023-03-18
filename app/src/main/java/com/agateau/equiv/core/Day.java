/*
Copyright 2015 Aurélien Gâteau

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
