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
package com.agateau.equiv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Contains all available products
 */
public class ProductStore {
    private final ArrayList<Product> mItems = new ArrayList<>();
    private final ArrayList<Product> mFavoriteItems = new ArrayList<>();
    private final Set<Product> mFavoriteItemSet = new HashSet<>();
    private final Map<String, ProductCategory> mCategoryMap = new HashMap<>();
    private OnProductStoreChangedListener mOnProductStoreChangedListener;

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

    public ArrayList<Product> getCustomItems() {
        ArrayList<Product> list = new ArrayList<>();
        for (Product item : mItems) {
            if (item.hasCustomDetails()) {
                list.add(item);
            }
        }
        return list;
    }

    public boolean hasCustomItems() {
        for (Product item : mItems) {
            if (item.hasCustomDetails()) {
                return true;
            }
        }
        return false;
    }

    public interface OnProductStoreChangedListener {
        void onFavoriteChanged();
        void onItemListChanged();
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
        if (mOnProductStoreChangedListener != null) {
            mOnProductStoreChangedListener.onFavoriteChanged();
        }
    }

    public boolean isFavorite(Product product) {
        return mFavoriteItemSet.contains(product);
    }

    public void load(UUID uuid, Product.Details details, Product.Source source) {
        Product product;
        if (source == Product.Source.CUSTOM) {
            product = findByUuid(uuid);
            if (product == null) {
                product = new Product(uuid, null);
                mItems.add(product);
            }
            product.setCustomDetails(details);
        } else {
            product = new Product(uuid, details);
            mItems.add(product);
        }
    }

    public void finishLoad() {
        onItemListChanged();
    }

    public void add(Product product) {
        mItems.add(product);
        onItemListChanged();
    }

    public void remove(Product product) {
        mItems.remove(product);
        mFavoriteItemSet.remove(product);
        updateFavoriteItemList();
        onItemListChanged();
    }

    public void handleProductUpdate(Product product) {
        onItemListChanged();
    }

    public Product findByUuid(UUID uuid) {
        for (Product product : mItems) {
            if (product.getUuid().equals(uuid)) {
                return product;
            }
        }
        return null;
    }

    public ProductCategory findCategory(String id) {
        return mCategoryMap.get(id);
    }

    public void addCategory(ProductCategory category) {
        mCategoryMap.put(category.getCategoryId(), category);
    }

    public List<ProductCategory> getCategoryList() {
        List<ProductCategory> lst = new ArrayList<>();
        lst.addAll(mCategoryMap.values());
        return lst;
    }

    public Set<UUID> getFavoriteUuids() {
        Set<UUID> set = new HashSet<>();
        for (Product product : mFavoriteItemSet) {
            set.add(product.getUuid());
        }
        return set;
    }

    public void setFavoriteUuids(Set<UUID> favoriteUuids) {
        mFavoriteItemSet.clear();
        for (Product product : mItems) {
            if (favoriteUuids.contains(product.getUuid())) {
                mFavoriteItemSet.add(product);
            }
        }
        updateFavoriteItemList();
    }

    public void setOnProductStoreChangedListener(OnProductStoreChangedListener onProductStoreChangedListener) {
        mOnProductStoreChangedListener = onProductStoreChangedListener;
    }

    private void updateFavoriteItemList() {
        mFavoriteItems.clear();
        mFavoriteItems.addAll(mFavoriteItemSet);
        Collections.sort(mFavoriteItems, mComparator);
    }

    private void onItemListChanged() {
        Collections.sort(mItems, mComparator);
        if (mOnProductStoreChangedListener != null) {
            mOnProductStoreChangedListener.onItemListChanged();
        }
    }
}
