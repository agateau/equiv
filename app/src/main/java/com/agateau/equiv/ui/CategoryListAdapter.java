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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agateau.equiv.R;
import com.agateau.equiv.core.ProductCategory;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<ProductCategory> {
    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public CategoryListAdapter(Context context, List<ProductCategory> categories) {
        super(context, R.layout.category_item, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.category_item, parent, false);
            vh = new ViewHolder();
            vh.imageView = (ImageView) view.findViewById(R.id.product_category_image);
            vh.textView = (TextView) view.findViewById(R.id.product_category_text);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        ProductCategory category = getItem(position);

        // imageView
        vh.imageView.setImageDrawable(ProductCategoryUtils.getDrawableForCategory(getContext(), category));

        // textView
        vh.textView.setText(ProductCategoryUtils.getTextForCategory(getContext(), category));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
