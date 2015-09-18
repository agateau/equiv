package com.greenyetilab.equiv.core;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A meal in a day
 */
public class Meal {
    final String mName;
    final ArrayList<MealItem> mItems = new ArrayList<>();
    private LinkedList<Listener> mListeners = new LinkedList<>();

    public interface Listener {
        void onMealChanged();
    }

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
        notifyChanged();
    }

    public float getProtideWeight() {
        float weight = 0;
        for(MealItem item : mItems) {
            weight += item.getProtideWeight();
        }
        return weight;
    }

    void clear() {
        mItems.clear();
        notifyChanged();
    }

    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

    private void notifyChanged() {
        for (Listener listener : mListeners) {
            listener.onMealChanged();
        }
    }
}
