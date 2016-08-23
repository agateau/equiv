package com.agateau.equiv.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.agateau.equiv.core.Constants;
import com.agateau.equiv.core.Consumer;
import com.agateau.equiv.core.Day;
import com.agateau.equiv.core.DayJsonIO;
import com.agateau.equiv.core.Meal;
import com.agateau.equiv.core.ProductStore;
import com.agateau.equiv.core.ProductStoreCsvIO;
import com.agateau.equiv.core.ProteinWeightUnit;
import com.agateau.utils.log.NLog;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Holds all the data
 */
public class Kernel {
    private static final String DAY_JSON = "day.json";
    private static final String CUSTOM_PRODUCTS_CSV = "custom-products.csv";

    private static Kernel sInstance = null;

    private final Consumer mConsumer = new Consumer();
    private final Day mDay = new Day();
    private ProductStore mProductStore = null;
    private int mCurrentTab = -1;
    private ProteinWeightUnit mProteinUnit = ProteinWeightUnit.POTATO;
    private final WeightFormatter mWeightFormater;

    Kernel(Context context) {
        setupDay();
        mWeightFormater = new WeightFormatter(context.getResources());
    }

    public static Kernel getInstance(Context context) {
        if (sInstance == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            sInstance = new Kernel(context);
            sInstance.loadProductList(context);
            sInstance.loadDay(context);
            sInstance.updateFromPreferences(prefs);
            sInstance.readFavorites(prefs);
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
        mProteinUnit = PreferenceUtils.getProteinWeightUnit(prefs, "protein_weight_unit", ProteinWeightUnit.PROTEIN);
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
            DayJsonIO.read(stream, mDay, mProductStore);
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

    public ProductStore getProductStore() {
        return mProductStore;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
    }

    private void loadProductList(Context context) {
        InputStream stream;
        mProductStore = new ProductStore();
        try {
            stream = context.getAssets().open("products.csv");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open products.csv.", e);
        }
        try {
            ProductStoreCsvIO.read(stream, mProductStore, ProductStoreCsvIO.ProductSource.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read products.csv.", e);
        }

        try {
            stream = context.openFileInput(CUSTOM_PRODUCTS_CSV);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            ProductStoreCsvIO.read(stream, mProductStore, ProductStoreCsvIO.ProductSource.CUSTOM);
        } catch (IOException e) {
            // Do not throw an exception here, the app is still usable even if we failed to load custom products
            NLog.e("Failed to read custom products from %s: %s.", CUSTOM_PRODUCTS_CSV, e);
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
        float maxPerDay = PreferenceUtils.getFloat(prefs, "max_per_day", 250f);
        float maxProtein;
        if (mProteinUnit == ProteinWeightUnit.POTATO) {
            maxProtein = maxPerDay * Constants.PROTEIN_FOR_POTATO;
        } else {
            maxProtein = maxPerDay;
        }
        mConsumer.setMaxProteinPerDay(maxProtein);
    }

    public void saveCustomProductList(Context context) {
        NLog.i("");
        FileOutputStream stream;
        try {
            stream = context.openFileOutput(CUSTOM_PRODUCTS_CSV, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            NLog.e("Failed to open %s for writing: %s", CUSTOM_PRODUCTS_CSV, e);
            return;
        }
        try {
            ProductStoreCsvIO.write(stream, mProductStore.getCustomItems());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            NLog.e("Failed to save custom productsto %s: %s", CUSTOM_PRODUCTS_CSV, e);
        }
    }

    public ProteinWeightUnit getProteinUnit() {
        return mProteinUnit;
    }

    private void readFavorites(SharedPreferences prefs) {
        Set<String> stringSet = prefs.getStringSet("favorites", null);
        if (stringSet == null) {
            return;
        }
        Set<UUID> favorites = new HashSet<>();
        for (String str : stringSet) {
            favorites.add(UUID.fromString(str));
        }
        mProductStore.setFavoriteUuids(favorites);
    }

    public void writeFavorites(Context context) {
        Set<UUID> favorites = mProductStore.getFavoriteUuids();
        Set<String> stringSet = new HashSet<>();
        for (UUID uuid : favorites) {
            stringSet.add(uuid.toString());
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putStringSet("favorites", stringSet).apply();
    }
}
