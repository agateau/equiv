package com.greenyetilab.equiv.ui;

import android.content.res.Resources;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Constants;
import com.greenyetilab.equiv.core.ProteinWeightUnit;

/**
 * Format weight strings according to units and locale
 */
public class WeightFormatter {
    public enum UnitFormat {
        NONE,
        SHORT,
        FULL
    }

    private ProteinWeightUnit mProteinFormat = ProteinWeightUnit.POTATO;
    private final Resources mResources;

    public WeightFormatter(Resources resources) {
        mResources = resources;
    }

    public void setProteinFormat(ProteinWeightUnit proteinFormat) {
        mProteinFormat = proteinFormat;
    }

    public String format(float protein) {
        return format(protein, UnitFormat.FULL);
    }

    public String format(float protein, UnitFormat unitFormat) {
        String txt;
        if (mProteinFormat == ProteinWeightUnit.PROTEIN) {
            txt = String.format("%.1f", protein);
        } else {
            float value = protein / Constants.PROTEIN_FOR_POTATO;
            if (value >= 1 || value == 0) {
                txt = String.format("%d", Math.round(value));
            } else {
                txt = String.format("%.1f", value);
            }
        }

        return txt + getUnitString(unitFormat);
    }

    public String getUnitString(UnitFormat unitFormat) {
        String unit = "";
        switch (unitFormat) {
            case NONE:
                break;
            case SHORT:
                unit = mResources.getText(R.string.weight_unit).toString();
                break;
            case FULL:
                unit = mResources.getText(mProteinFormat == ProteinWeightUnit.POTATO ?  R.string.potato_weight_unit : R.string.protein_weight_unit).toString();
                break;
        }
        return unit;
    }
}
