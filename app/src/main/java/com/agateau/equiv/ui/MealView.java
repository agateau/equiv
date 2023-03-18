/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.equiv.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Meal;
import com.agateau.equiv.core.MealItem;

/**
 * Shows the meal items for a view
 */
public class MealView extends LinearLayout implements Meal.Listener {
    private final Meal mMeal;
    private final ArrayAdapter<MealItem> mAdapter;

    public MealView(Context context, Meal meal, final WeightFormatter weightFormatter) {
        super(context);
        inflate(context, R.layout.meal_view, this);
        mMeal = meal;

        mAdapter = new ArrayAdapter<MealItem>(context, R.layout.meal_item, R.id.meal_item_text, mMeal.getItems()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView proteinTextView = (TextView) view.findViewById(R.id.meal_item_protein_text);
                float proteins = mMeal.getItems().get(position).getProteinWeight();
                proteinTextView.setText(weightFormatter.format(proteins));
                return view;
            }
        };
        ListView listView = (ListView) findViewById(R.id.meal_list_view);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MealItemDetailActivity.editMealItem(getContext(), mMeal, position);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mMeal.registerListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mMeal.unregisterListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onMealChanged(Meal meal) {
        mAdapter.notifyDataSetChanged();
    }
}
