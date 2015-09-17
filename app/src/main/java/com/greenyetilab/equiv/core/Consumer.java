package com.greenyetilab.equiv.core;

/**
 * A food consumer
 */
public class Consumer {
    private String mName;
    private float mMaxProtidePerDay;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getMaxProtidePerDay() {
        return mMaxProtidePerDay;
    }

    public void setMaxProtidePerDay(float maxProtidePerDay) {
        mMaxProtidePerDay = maxProtidePerDay;
    }
}
