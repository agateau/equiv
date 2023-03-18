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
