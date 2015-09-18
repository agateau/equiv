package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenyetilab.equiv.core.Meal;

/**
 * Custom tab showing meal_view name and protide weight
 */
public class MealTabView extends LinearLayout implements Meal.Listener {
    private final Meal mMeal;
    private final TextView mProtideView;

    public MealTabView(Context context, Meal meal) {
        super(context);
        mMeal = meal;
        mMeal.registerListener(this);

        TextView nameView = new TextView(context);
        nameView.setText(meal.getName());

        mProtideView = new TextView(context);

        setOrientation(VERTICAL);
        addView(nameView);
        addView(mProtideView);

        updateFromMeal();
    }

    public void updateFromMeal() {
        mProtideView.setText(String.format("%.1g g", mMeal.getProtideWeight()));
    }

    @Override
    public void onMealChanged() {
        updateFromMeal();
    }
}
