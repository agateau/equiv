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
