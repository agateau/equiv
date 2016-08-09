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
        vh.textView.setText(category.toString());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
