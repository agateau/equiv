package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;
import com.greenyetilab.equiv.core.ProductList;

/**
 * Shows the meal items for a view
 */
public class MealView extends LinearLayout {
    private static final String TAG = "Meal";

    private final Meal mMeal;
    private final ProductList mProductList;
    private final ArrayAdapter<MealItem> mAdapter;

    public MealView(Context context, Meal meal, ProductList productList) {
        super(context);
        inflate(context, R.layout.meal_view, this);
        mMeal = meal;
        mProductList = productList;

        mAdapter = new ArrayAdapter<MealItem>(context, R.layout.meal_item, R.id.meal_item_text, mMeal.getItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView protideTextView = (TextView) view.findViewById(R.id.meal_item_protide_text);
                float protides = mMeal.getItems().get(position).getProtideWeight();
                protideTextView.setText(String.format("%.1f gP", protides));
                return view;
            }
        };
        ListView listView = (ListView) findViewById(R.id.meal_list_view);
        listView.setAdapter(mAdapter);

        mMeal.registerListener(new Meal.Listener() {
            @Override
            public void onMealChanged() {
                mAdapter.notifyDataSetChanged();
            }
        });

        Button button = (Button) findViewById(R.id.add_meal_item_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addMealItem();
            }
        });
    }

    public void addMealItem() {
        MealItem item = new MealItem(mProductList.getItems().get(0), 100);
        mMeal.add(item);
    }
}
