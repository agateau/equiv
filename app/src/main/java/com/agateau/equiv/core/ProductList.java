package com.agateau.equiv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains all available products
 */
public class ProductList {
    private ArrayList<Product> mItems;
    private ArrayList<Product> mFavoriteItems;
    private Set<Product> mFavoriteItemSet = new HashSet<>();
    private FavoriteChangedListener mFavoriteChangedListener;

    private Comparator<Product> mComparator = new Comparator<Product>() {
        @Override
        public int compare(Product lhs, Product rhs) {
            int res = lhs.getCategory().getCategoryId().compareTo(rhs.getCategory().getCategoryId());
            if (res != 0) {
                return res;
            }
            return lhs.getCollationKey().compareTo(rhs.getCollationKey());
        }
    };

    public interface FavoriteChangedListener {
        void onFavoriteChanged();
    }

    public ArrayList<Product> getItems() {
        return mItems;
    }

    public ArrayList<Product> getFavoriteItems() {
        return mFavoriteItems;
    }

    public void setFavorite(Product product, boolean favorite) {
        if (favorite) {
            mFavoriteItemSet.add(product);
        } else {
            mFavoriteItemSet.remove(product);
        }
        updateFavoriteItemList();
        if (mFavoriteChangedListener != null) {
            mFavoriteChangedListener.onFavoriteChanged();
        }
    }

    public boolean isFavorite(Product product) {
        return mFavoriteItemSet.contains(product);
    }

    public void setItems(ArrayList<Product> items) {
        mItems = new ArrayList<>(items);
        Collections.sort(mItems, mComparator);
        mFavoriteItems = new ArrayList<>(items.size());
    }

    public Product findByUuid(String uuid) {
        for (Product product : mItems) {
            if (product.getUuid().equals(uuid)) {
                return product;
            }
        }
        return null;
    }

    public Product findByName(String productName) {
        for (Product product : mItems) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    public Set<String> getFavoriteUuids() {
        Set<String> set = new HashSet<>();
        for (Product product : mFavoriteItemSet) {
            set.add(product.getUuid());
        }
        return set;
    }

    public void setFavoriteUuids(Set<String> favoriteUuids) {
        mFavoriteItemSet.clear();
        for (Product product : mItems) {
            if (favoriteUuids.contains(product.getUuid())) {
                mFavoriteItemSet.add(product);
            }
        }
        updateFavoriteItemList();
    }

    public void setFavoriteChangedListener(FavoriteChangedListener favoriteChangedListener) {
        mFavoriteChangedListener = favoriteChangedListener;
    }

    private void updateFavoriteItemList() {
        mFavoriteItems.clear();
        mFavoriteItems.addAll(mFavoriteItemSet);
        Collections.sort(mFavoriteItems, mComparator);
    }
}
