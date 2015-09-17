package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;

/**
 * Shows the meal items for a view
 */
public class MealView extends ListView {

    private final Meal mMeal;

    public MealView(Context context, Meal meal) {
        super(context);
        mMeal = meal;

        ArrayAdapter<MealItem> adapter = new ArrayAdapter<MealItem>(context, R.layout.meal_item, mMeal.getItems());
        setAdapter(adapter);
    }
}
