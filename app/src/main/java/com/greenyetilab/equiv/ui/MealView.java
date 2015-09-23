package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Meal;
import com.greenyetilab.equiv.core.MealItem;

/**
 * Shows the meal items for a view
 */
public class MealView extends LinearLayout {
    private final Meal mMeal;
    private final ArrayAdapter<MealItem> mAdapter;

    public MealView(Context context, Meal meal) {
        super(context);
        inflate(context, R.layout.meal_view, this);
        mMeal = meal;

        mAdapter = new ArrayAdapter<MealItem>(context, R.layout.meal_item, R.id.meal_item_text, mMeal.getItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView proteinTextView = (TextView) view.findViewById(R.id.meal_item_protein_text);
                float proteins = mMeal.getItems().get(position).getProteinWeight();
                proteinTextView.setText(String.format("%.1f gP", proteins));
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

        Button button = (Button) findViewById(R.id.show_add_meal_item_activity_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MealItemDetailActivity.addMealItem(getContext(), mMeal);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MealItemDetailActivity.editMealItem(getContext(), mMeal, position);
            }
        });
    }
}
