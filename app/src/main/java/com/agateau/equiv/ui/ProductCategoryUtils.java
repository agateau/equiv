package com.agateau.equiv.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.agateau.equiv.core.ProductCategory;

/**
 * Methods to work with product categories
 */
public class ProductCategoryUtils {
    public static Drawable getDrawableForCategory(Context context, ProductCategory category) {
        Resources resources = context.getResources();
        String name = "categories_";
        name = name.concat(category.getCategoryId());
        int id = resources.getIdentifier(name, "drawable", context.getPackageName());
        return resources.getDrawable(id);
    }

    public static String getTextForCategory(Context context, ProductCategory category) {
        Resources resources = context.getResources();
        String name = "categories_";
        name = name.concat(category.getCategoryId());
        int id = resources.getIdentifier(name, "string", context.getPackageName());
        return resources.getString(id);
    }
}
