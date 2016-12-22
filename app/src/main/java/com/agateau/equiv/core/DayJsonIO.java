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

import android.util.JsonWriter;

import com.agateau.FileUtils;
import com.agateau.utils.log.NLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Knows how to serialize/deserialize a Day object to/from JSON
 *
 * {
 *     version: 1
 *     meals: [
 *         {
 *             tag: breakfast
 *             items: [
 *                 {
 *                     productUuid: "aaa-bbb-ccc",
 *                     quantity: 1.0
 *                 },
 *                 {
 *                     productUuid: "ddd-eee-fff",
 *                     quantity: 2.0
 *                 },
 *             ]
 *         },
 *         {
 *             tag: lunch
 *             items: [
 *             ]
 *         }
 *     ]
 * }
 */
public class DayJsonIO {
    public static void read(InputStream stream, Day day, ProductStore productStore) throws IOException, JSONException {
        String string = FileUtils.readUtf8(stream);
        fromString(string, day, productStore);
    }

    public static void write(OutputStream stream, Day day) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(stream, "UTF-8"));
        writer.beginObject();
        writer.name("version").value(1);
        writer.name("meals");
        writer.beginArray();
        for (Meal meal : day.getMeals()) {
            writeMeal(writer, meal);
        }
        writer.endArray();
        writer.endObject();
        writer.close();
    }

    private static void writeMeal(JsonWriter writer, Meal meal) throws IOException {
        writer.beginObject();
        writer.name("tag").value(meal.getTag());
        writer.name("items");
        writer.beginArray();
        for (MealItem mealItem : meal.getItems()) {
            writer.beginObject();
            writer.name("productUuid").value(mealItem.getProduct().getUuid().toString());
            writer.name("quantity").value(mealItem.getQuantity());
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }


    public static void fromString(String string, Day day, ProductStore productStore) throws JSONException {
        day.clear();
        JSONObject obj = new JSONObject(string);

        if (obj.getInt("version") != 1) {
            return;
        }

        JSONArray mealsJs = obj.getJSONArray("meals");
        for (int i = 0; i < mealsJs.length(); ++i) {
            JSONArray mealItemsJs = mealsJs.getJSONObject(i).getJSONArray("items");
            Meal meal = day.getMeals().get(i);
            initMeal(mealItemsJs, meal, productStore);
        }
    }

    private static void initMeal(JSONArray mealItemsJs, Meal meal, ProductStore productStore) throws JSONException {
        for (int i = 0; i < mealItemsJs.length(); ++i) {
            JSONObject mealItemJs = mealItemsJs.getJSONObject(i);

            UUID uuid = UUID.fromString(mealItemJs.getString("productUuid"));
            Product product = productStore.findByUuid(uuid);
            if (product == null) {
                NLog.e("No product with uuid " + uuid);
                continue;
            }

            float quantity = (float) mealItemJs.getDouble("quantity");

            MealItem item = new MealItem(product, quantity);
            meal.add(item);
        }
    }
}
