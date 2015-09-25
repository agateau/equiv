package com.greenyetilab.equiv.ui;

import android.content.Context;

import com.greenyetilab.equiv.core.Consumer;
import com.greenyetilab.equiv.core.Day;
import com.greenyetilab.equiv.core.DayJsonIO;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.ProductList;
import com.greenyetilab.equiv.core.ProductListCsvIO;
import com.greenyetilab.equiv.core.FormatUtils;
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
    private final Context mContext;
    private ProductList mProductList = null;
    private int mCurrentTab = -1;
    private FormatUtils.ProteinFormat mProteinUnit = FormatUtils.ProteinFormat.POTATO;

    Kernel(Context context) {
        mContext = context;
        setupDay();
        setupConsumer();
    }

    public static Kernel getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Kernel(context);
            sInstance.loadProductList();
            sInstance.loadDay();
        }
        return sInstance;
    }

    private void loadDay() {
        NLog.i("");
        FileInputStream stream;
        try {
            stream = mContext.openFileInput(DAY_JSON);
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

    public void saveDay() {
        NLog.i("");
        FileOutputStream stream;
        try {
            stream = mContext.openFileOutput(DAY_JSON, Context.MODE_PRIVATE);
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

    private void loadProductList() {
        InputStream stream;
        try {
            stream = mContext.getAssets().open("products.csv");
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

    private void setupConsumer() {
        mConsumer.setName("Clara");
        mConsumer.setMaxProteinPerDay(4f);
    }

    public FormatUtils.ProteinFormat getProteinUnit() {
        return mProteinUnit;
    }
}
