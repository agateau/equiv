package com.agateau.equiv.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.agateau.equiv.core.ProductCategory;
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
        TextView mainTextView;
        TextView equivTextView;
        CheckBox favoriteCheckBox;
    }

    private final CompoundButton.OnCheckedChangeListener mOnFavoriteCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Product product = (Product) buttonView.getTag();
            mKernel.getProductList().setFavorite(product, isChecked);
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
            vh.mainTextView = (TextView) view.findViewById(R.id.product_item_text);
            vh.equivTextView = (TextView) view.findViewById(R.id.product_item_equiv_text);
            vh.favoriteCheckBox = (CheckBox) view.findViewById(R.id.product_item_favorite);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        Product product = getItem(position);

        // categoryImageView
        vh.categoryImageView.setImageDrawable(getDrawableForCategory(product.getCategory()));

        // mainTextView
        vh.mainTextView.setText(product.getName());

        // equivTextView
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
        vh.equivTextView.setText(equivText);

        // favoriteCheckBox
        // Reset onCheckedChangeListener so that mOnFavoriteCheckedChangeListener is not called
        // when we call setChecked() (can happen when a view is reused)
        vh.favoriteCheckBox.setOnCheckedChangeListener(null);
        vh.favoriteCheckBox.setChecked(mKernel.getProductList().isFavorite(product));
        vh.favoriteCheckBox.setOnCheckedChangeListener(mOnFavoriteCheckedChangeListener);
        vh.favoriteCheckBox.setTag(product);
        return view;
    }

    private Drawable getDrawableForCategory(ProductCategory category) {
        Resources resources = mContext.getResources();
        String name = "categories_";
        name = name.concat(category.getCategoryId());
        int id = resources.getIdentifier(name, "drawable", mContext.getPackageName());
        return resources.getDrawable(id);
    }
}
