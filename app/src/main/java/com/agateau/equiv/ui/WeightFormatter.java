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

import android.content.res.Resources;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Constants;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.ProteinWeightUnit;

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
            txt = FormatUtils.naturalRound(protein);
        } else {
            float value = protein / Constants.PROTEIN_FOR_POTATO;
            if (value >= 10 || value == 0) {
                txt = String.format("%d", Math.round(value));
            } else {
                txt = FormatUtils.naturalRound(value);
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
