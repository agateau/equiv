package com.agateau.equiv.core;

import java.util.ArrayList;
import java.util.Collection;
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
public class ProductList {
    private final ArrayList<Product> mItems = new ArrayList<>();
    private final ArrayList<Product> mFavoriteItems = new ArrayList<>();
    private final Set<Product> mFavoriteItemSet = new HashSet<>();
    private final Map<String, ProductCategory> mCategoryMap = new HashMap<>();
    private ProductListChangedListener mProductListChangedListener;

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
            if (item.isCustom()) {
                list.add(item);
            }
        }
        return list;
    }

    public interface ProductListChangedListener {
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
        if (mProductListChangedListener != null) {
            mProductListChangedListener.onFavoriteChanged();
        }
    }

    public boolean isFavorite(Product product) {
        return mFavoriteItemSet.contains(product);
    }

    public void add(Product product) {
        mItems.add(product);
        onItemListChanged();
    }

    public void addAll(Collection<Product> products) {
        mItems.addAll(products);
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

    public Product findByName(String productName) {
        for (Product product : mItems) {
            if (product.getName().equals(productName)) {
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

    public void setProductListChangedListener(ProductListChangedListener productListChangedListener) {
        mProductListChangedListener = productListChangedListener;
    }

    private void updateFavoriteItemList() {
        mFavoriteItems.clear();
        mFavoriteItems.addAll(mFavoriteItemSet);
        Collections.sort(mFavoriteItems, mComparator);
    }

    private void onItemListChanged() {
        Collections.sort(mItems, mComparator);
        if (mProductListChangedListener != null) {
            mProductListChangedListener.onItemListChanged();
        }
    }
}
