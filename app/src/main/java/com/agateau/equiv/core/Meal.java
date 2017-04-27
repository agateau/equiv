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

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A meal in a day
 */
public class Meal {
    final String mTag;
    final ArrayList<MealItem> mItems = new ArrayList<>();
    private LinkedList<Listener> mListeners = new LinkedList<>();

    public interface Listener {
        void onMealChanged(Meal meal);
    }

    public Meal(String tag) {
        mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public String toString() {
        return getTag();
    }

    public ArrayList<MealItem> getItems() {
        return mItems;
    }

    public void add(MealItem item) {
        mItems.add(item);
        notifyChanged();
    }

    public void set(int position, MealItem item) {
        mItems.set(position, item);
        notifyChanged();
    }

    public void remove(int mealItemPosition) {
        mItems.remove(mealItemPosition);
        notifyChanged();
    }

    public void removeProduct(Product product) {
        boolean changed = false;
        for (int idx = mItems.size() - 1; idx >= 0; --idx) {
            MealItem item = mItems.get(idx);
            if (item.getProduct() == product) {
                changed = true;
                mItems.remove(idx);
            }
        }
        if (changed) {
            notifyChanged();
        }
    }

    public float getProteinWeight() {
        float weight = 0;
        for(MealItem item : mItems) {
            weight += item.getProteinWeight();
        }
        return weight;
    }

    void clear() {
        mItems.clear();
        notifyChanged();
    }

    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    public boolean containsProduct(Product product) {
        for(MealItem item : mItems) {
            if (item.getProduct() == product) {
                return true;
            }
        }
        return false;
    }

    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

    private void notifyChanged() {
        for (Listener listener : mListeners) {
            listener.onMealChanged(this);
        }
    }
}
