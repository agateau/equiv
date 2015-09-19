package com.greenyetilab.utils.log;

import android.util.Log;

/**
 * A Printer which uses Android log facilities
 */
public class AndroidPrinter implements NLog.Printer {
    @Override
    public void print(int level, String tag, String message) {
        if (level == NLog.DEBUG) {
            Log.d(tag, message);
        } else if (level == NLog.INFO) {
            Log.i(tag, message);
        } else {
            Log.e(tag, message);
        }
    }
}
