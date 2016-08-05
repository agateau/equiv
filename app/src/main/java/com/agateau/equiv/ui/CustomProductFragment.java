package com.agateau.equiv.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Product;
import com.agateau.equiv.core.ProductCategory;
import com.agateau.equiv.core.ProductList;

import java.util.List;

public class CustomProductFragment extends DialogFragment {
    private static final String ARG_PRODUCT_UUID = "productUuid";

    private String mProductUuid;

    private CustomProductListener mListener;

    interface CustomProductListener {
        public void onProductModified(String productUuid);
    }

    public static CustomProductFragment newInstance(String productUuid) {
        CustomProductFragment fragment = new CustomProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_UUID, productUuid);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProductUuid = getArguments().getString(ARG_PRODUCT_UUID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_product, container, false);

        List<ProductCategory> categoryList = Kernel.getExistingInstance().getProductList().getCategoryList();
        CategoryListAdapter adapter = new CategoryListAdapter(view.getContext(), categoryList);

        Spinner spinner = (Spinner) view.findViewById(R.id.product_category);
        spinner.setAdapter(adapter);

        Button cancelButton = (Button) view.findViewById(R.id.cancel_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button addButton = (Button) view.findViewById(R.id.add_custom_product_action);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CustomProductListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CustomProductListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void addProduct() {
        View view = getView();
        assert view != null;
        Spinner spinner = (Spinner) view.findViewById(R.id.product_category);
        ProductCategory category = (ProductCategory) spinner.getSelectedItem();

        EditText edit = (EditText) view.findViewById(R.id.product_name);
        String name = edit.getText().toString();

        edit = (EditText) view.findViewById(R.id.product_proteins);
        float proteins = Float.valueOf(edit.getText().toString());

        RadioButton button = (RadioButton) view.findViewById(R.id.radio_unit_per_100g);
        Product.Unit unit = button.isChecked() ? Product.Unit.GRAM : Product.Unit.PORTION;

        if (unit == Product.Unit.GRAM) {
            // User entered proteins per 100g but we store proteins per 1g
            proteins /= 100;
        }

        Product product = new Product(category, name, unit, proteins);
        product.setCustom(true);

        ProductList productList = Kernel.getExistingInstance().getProductList();
        productList.add(product);
        Kernel.getExistingInstance().saveCustomProductList(getActivity());
    }
}
