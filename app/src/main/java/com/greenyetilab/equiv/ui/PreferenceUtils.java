package com.greenyetilab.equiv.ui;

import android.content.SharedPreferences;

import com.greenyetilab.equiv.core.ProteinWeightUnit;

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
