package com.greenyetilab.equiv.core;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Text utilities
 */
public class FormatUtils {
    public static DecimalFormat getDecimalFormat() {
        return (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
    }

    public static float parseFloat(String s) throws ParseException {
        return getDecimalFormat().parse(s).floatValue();
    }

    public enum ProteinFormat {
        PROTEIN,
        POTATO
    }
    public enum UnitFormat {
        NONE,
        SHORT,
        FULL
    }
    public static final String formatProteinWeight(float protein, ProteinFormat format) {
        return formatProteinWeight(protein, format, UnitFormat.FULL);
    }

    public static final String formatProteinWeight(float protein, ProteinFormat proteinFormat, UnitFormat unitFormat) {
        String txt;
        if (proteinFormat == ProteinFormat.PROTEIN) {
            txt = getDecimalFormat().format(protein);
        } else {
            float value = protein / Constants.PROTEIN_FOR_POTATO;
            if (value >= 1 || value == 0) {
                txt = String.format("%d", Math.round(value));
            } else {
                txt = getDecimalFormat().format(value);
            }
        }

        String unit = "";
        switch (unitFormat) {
            case NONE:
                break;
            case SHORT:
                unit = "g";
                break;
            case FULL:
                unit = proteinFormat == ProteinFormat.POTATO ? "g PDT" : "g P";
                break;
        }
        return txt + unit;
    }

    public static String naturalRound(float value) {
        int rounded = Math.round(value);
        if (Math.abs(value - rounded) < 0.2) {
            return String.valueOf(rounded);
        } else {
            return getDecimalFormat().format(value);
        }
    }

}
