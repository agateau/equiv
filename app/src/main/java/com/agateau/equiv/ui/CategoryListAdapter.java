package com.agateau.equiv.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.agateau.equiv.R;
import com.agateau.equiv.core.ProductCategory;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<ProductCategory> {
    public CategoryListAdapter(Context context, List<ProductCategory> categories) {
        super(context, R.layout.category_item, categories);
    }
}
