package com.greenyetilab.equiv.core;

/**
 * Text utilities
 */
public class FormatUtils {
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
            txt = String.format("%.1f", protein);
        } else {
            float value = protein / Constants.PROTEIN_FOR_POTATO;
            if (value >= 1 || value == 0) {
                txt = String.format("%d", Math.round(value));
            } else {
                txt = String.format("%.1f", value);
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
        if (Math.abs(value - rounded) < 0.01) {
            return String.valueOf(rounded);
        } else {
            String str = String.format("%.2f", value);
            if (str.charAt(str.length() - 1) == '0') {
                return str.substring(0, str.length() - 1);
            } else {
                return str;
            }
        }
    }

}
