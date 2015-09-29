package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.greenyetilab.equiv.core.Consumer;
import com.greenyetilab.equiv.core.Day;
import com.greenyetilab.equiv.core.DayJsonIO;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.ProductList;
import com.greenyetilab.equiv.core.ProductListCsvIO;
import com.greenyetilab.equiv.core.ProteinWeightUnit;
import com.greenyetilab.utils.log.NLog;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Holds all the data
 */
public class Kernel {
    private static final String DAY_JSON = "day.json";

    private static Kernel sInstance = null;

    private final Consumer mConsumer = new Consumer();
    private final Day mDay = new Day();
    private ProductList mProductList = null;
    private int mCurrentTab = -1;
    private ProteinWeightUnit mProteinUnit = ProteinWeightUnit.PROTEIN;
    //private ProteinWeightUnit mProteinUnit = ProteinWeightUnit.POTATO;
    private final WeightFormatter mWeightFormater;

    Kernel(Context context) {
        setupDay();
        mWeightFormater = new WeightFormatter(context.getResources());
    }

    public static Kernel getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Kernel(context);
            sInstance.loadProductList(context);
            sInstance.loadDay(context);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            sInstance.updateFromPreferences(prefs);
        }
        return sInstance;
    }

    public static Kernel getExistingInstance() {
        return sInstance;
    }

    public WeightFormatter getWeightFormater() {
        return mWeightFormater;
    }

    public void updateFromPreferences(SharedPreferences prefs) {
        loadConsumer(prefs);
        mWeightFormater.setProteinFormat(mProteinUnit);
    }

    private void loadDay(Context context) {
        NLog.i("");
        FileInputStream stream;
        try {
            stream = context.openFileInput(DAY_JSON);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            DayJsonIO.read(stream, mDay, mProductList);
            stream.close();
        } catch (IOException | JSONException e) {
            NLog.e("Failed to load day: %s", e);
        }
    }

    public void saveDay(Context context) {
        NLog.i("");
        FileOutputStream stream;
        try {
            stream = context.openFileOutput(DAY_JSON, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            NLog.e("Failed to open %s for writing: %s", DAY_JSON, e);
            return;
        }
        try {
            DayJsonIO.write(stream, mDay);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            NLog.e("Failed to save day: %s", e);
        }
    }

    public Consumer getConsumer() {
        return mConsumer;
    }

    public Day getDay() {
        return mDay;
    }

    public ProductList getProductList() {
        return mProductList;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
    }

    private void loadProductList(Context context) {
        InputStream stream;
        try {
            stream = context.getAssets().open("products.csv");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open products.csv.", e);
        }
        try {
            mProductList = ProductListCsvIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read products.csv.", e);
        }
    }

    private void setupDay() {
        Meal meal = new Meal("breakfast");
        mDay.add(meal);

        meal = new Meal("lunch");
        mDay.add(meal);

        meal = new Meal("snack");
        mDay.add(meal);

        meal = new Meal("dinner");
        mDay.add(meal);
    }

    private void loadConsumer(SharedPreferences prefs) {
        mConsumer.setName("Clara");
        float maxProtein = PreferenceUtils.getFloat(prefs, "max_protein_per_day", 5f);
        mConsumer.setMaxProteinPerDay(maxProtein);
    }

    public ProteinWeightUnit getProteinUnit() {
        return mProteinUnit;
    }
}
