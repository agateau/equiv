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

        int nameId = context.getResources().getIdentifier("meal_name_" + mMeal.getTag(), "string", context.getPackageName());
        String name = context.getString(nameId);

        TextView nameView = new TextView(context);
        nameView.setText(name);

        mProtideView = new TextView(context);

        setOrientation(VERTICAL);
        addView(nameView);
        addView(mProtideView);

        updateFromMeal();
    }

    public void updateFromMeal() {
        mProtideView.setText(String.format("%.1f g", mMeal.getProtideWeight()));
    }

    @Override
    public void onMealChanged() {
        updateFromMeal();
    }
}
