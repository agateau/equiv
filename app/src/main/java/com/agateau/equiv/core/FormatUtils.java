package com.agateau.equiv.core;

import java.util.Locale;

/**
 * Text utilities
 */
public class FormatUtils {

    public static String naturalRound(float value) {
        return naturalRound(Locale.getDefault(), value);
    }

    public static String naturalRound(Locale locale, float value) {
        int rounded = Math.round(value);
        if (Math.abs(value - rounded) < 0.01) {
            return String.valueOf(rounded);
        } else {
            String str = String.format(locale, "%.2f", value);
            if (str.charAt(str.length() - 1) == '0') {
                return str.substring(0, str.length() - 1);
            } else {
                return str;
            }
        }
    }

}
