package com.greenyetilab.equiv.core;

/**
 * A food consumer
 */
public class Consumer {
    private String mName;
    private float mMaxProteinPerDay;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getMaxProteinPerDay() {
        return mMaxProteinPerDay;
    }

    public void setMaxProteinPerDay(float maxProteinPerDay) {
        mMaxProteinPerDay = maxProteinPerDay;
    }
}
