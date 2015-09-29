package com.greenyetilab.equiv.ui;

import android.content.SharedPreferences;

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
}
