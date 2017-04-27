/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Constants;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProteinWeightUnit;

import java.util.ArrayList;

/**
 * An adapter to show a list of products
 */
class ProductListAdapter extends ArrayAdapter<Product> {
    private final Kernel mKernel;
    private final Context mContext;

    private static class ViewHolder {
        ImageView categoryImageView;
        ImageView badgeImageView;
        TextView mainTextView;
        TextView equivTextView;
        CheckBox favoriteCheckBox;
    }

    private final CompoundButton.OnCheckedChangeListener mOnFavoriteCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Product product = (Product) buttonView.getTag();
            mKernel.getProductStore().setFavorite(product, isChecked);
        }
    };

    public ProductListAdapter(Context context, Kernel kernel, ArrayList<Product> items) {
        super(context, R.layout.product_item, items);
        mContext = context;
        mKernel = kernel;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.product_item, parent, false);
            vh = new ViewHolder();
            vh.categoryImageView = (ImageView) view.findViewById(R.id.product_category_image);
            vh.badgeImageView = (ImageView) view.findViewById(R.id.product_badge);
            vh.mainTextView = (TextView) view.findViewById(R.id.product_item_text);
            vh.equivTextView = (TextView) view.findViewById(R.id.product_item_equiv_text);
            vh.favoriteCheckBox = (CheckBox) view.findViewById(R.id.product_item_favorite);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        Product product = getItem(position);

        // imageView
        vh.categoryImageView.setImageDrawable(ProductCategoryUtils.getDrawableForCategory(mContext, product.getCategory()));

        // badge
        if (product.hasCustomDetails()) {
            int drawableId;
            if (product.hasDefaultDetails()) {
                drawableId = R.drawable.ic_modified_badge;
            } else {
                drawableId = R.drawable.ic_added_badge;
            }
            vh.badgeImageView.setVisibility(View.VISIBLE);
            vh.badgeImageView.setImageDrawable(mContext.getResources().getDrawable(drawableId));
        } else {
            vh.badgeImageView.setVisibility(View.GONE);
        }

        // textView
        vh.mainTextView.setText(product.getName());

        // equivTextView
        float proteins = product.getProteins();

        WeightFormatter formatter = mKernel.getWeightFormatter();

        float equiv;
        Product.Unit equivUnit = product.getUnit();
        int ref;
        if (mKernel.getProteinUnit() == ProteinWeightUnit.POTATO) {
            equiv = 100 / (proteins / Constants.PROTEIN_FOR_POTATO);
            ref = 100;
        } else {
            equiv = 1 / proteins;
            ref = 1;
        }

        String equivText = String.format("%s%s = %d%s",
                FormatUtils.naturalRound(equiv),
                equivUnit.toString(),
                ref,
                formatter.getUnitString(WeightFormatter.UnitFormat.FULL));
        vh.equivTextView.setText(equivText);

        // favoriteCheckBox
        // Reset onCheckedChangeListener so that mOnFavoriteCheckedChangeListener is not called
        // when we call setChecked() (can happen when a view is reused)
        vh.favoriteCheckBox.setOnCheckedChangeListener(null);
        vh.favoriteCheckBox.setChecked(mKernel.getProductStore().isFavorite(product));
        vh.favoriteCheckBox.setOnCheckedChangeListener(mOnFavoriteCheckedChangeListener);
        vh.favoriteCheckBox.setTag(product);
        return view;
    }

}
