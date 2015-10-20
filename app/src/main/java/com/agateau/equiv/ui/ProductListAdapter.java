package com.agateau.equiv.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private final CompoundButton.OnCheckedChangeListener mOnFavoriteCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Product product = (Product) buttonView.getTag();
            mKernel.getProductList().setFavorite(product, isChecked);
        }
    };

    public ProductListAdapter(Context context, Kernel kernel, ArrayList<Product> items) {
        super(context, R.layout.product_item, R.id.product_item_text, items);
        mKernel = kernel;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Product product = getItem(position);
        view.setTag(product);

        TextView equivTextView = (TextView) view.findViewById(R.id.product_item_equiv_text);

        float proteins = product.getProteins();

        WeightFormatter formatter = mKernel.getWeightFormater();

        float equiv;
        String equivUnit = product.getUnit();
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
                equivUnit,
                ref,
                formatter.getUnitString(WeightFormatter.UnitFormat.FULL));
        equivTextView.setText(equivText);

        CheckBox favoriteCheckBox = (CheckBox) view.findViewById(R.id.product_item_favorite);
        // Reset onCheckedChangeListener so that mOnFavoriteCheckedChangeListener is not called
        // when we call setChecked() (can happen when a view is reused)
        favoriteCheckBox.setOnCheckedChangeListener(null);
        favoriteCheckBox.setChecked(mKernel.getProductList().isFavorite(product));
        favoriteCheckBox.setOnCheckedChangeListener(mOnFavoriteCheckedChangeListener);
        favoriteCheckBox.setTag(product);
        return view;
    }
}
