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
package com.agateau.equiv.ui;

import android.content.SharedPreferences;

import com.agateau.equiv.core.ProteinWeightUnit;

/**
 * Utilities to deal with SharedPreferences in a sane way
 */
public class PreferenceUtils {
    public static float getFloat(SharedPreferences prefs, String key, float defaultValue) {
        String strValue = prefs.getString(key, null);
        if (strValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static ProteinWeightUnit getProteinWeightUnit(SharedPreferences prefs, String key, ProteinWeightUnit defaultValue) {
        String strValue = prefs.getString(key, null);
        if (strValue == null) {
            return defaultValue;
        }
        try {
            return ProteinWeightUnit.valueOf(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
