package com.agateau.equiv.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Constants;
import com.agateau.equiv.core.FormatUtils;
import com.agateau.equiv.core.ProteinWeightUnit;

/**
 * Fragment storing all the settings
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ListPreference mProteinWeightUnitPreference;
    private EditTextPreference mMaxPerDayPreference;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        mPreferences = getPreferenceManager().getSharedPreferences();
        mPreferences.registerOnSharedPreferenceChangeListener(this);

        mProteinWeightUnitPreference = (ListPreference) findPreference("protein_weight_unit");
        mMaxPerDayPreference = (EditTextPreference) findPreference("max_per_day");
        updateSummaries();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Kernel kernel = Kernel.getExistingInstance();
        if (TextUtils.equals(key, mProteinWeightUnitPreference.getKey())) {
            float maxProteinPerDay = kernel.getConsumer().getMaxProteinPerDay();
            ProteinWeightUnit unit = PreferenceUtils.getProteinWeightUnit(prefs, "protein_weight_unit", ProteinWeightUnit.PROTEIN);
            float maxPerDay;
            if (unit == ProteinWeightUnit.POTATO) {
                maxPerDay = maxProteinPerDay / Constants.PROTEIN_FOR_POTATO;
            } else {
                maxPerDay = maxProteinPerDay;
            }
            mMaxPerDayPreference.setText(FormatUtils.naturalRound(maxPerDay));
        }
        kernel.updateFromPreferences(prefs);
        updateSummaries();
    }

    private void updateSummaries() {
        Kernel kernel = Kernel.getExistingInstance();
        String summary;

        summary = mProteinWeightUnitPreference.getEntry().toString();
        mProteinWeightUnitPreference.setSummary(summary);

        if (kernel.getProteinUnit() == ProteinWeightUnit.POTATO) {
            mMaxPerDayPreference.setTitle(R.string.max_potato_per_day_title);
        } else {
            mMaxPerDayPreference.setTitle(R.string.max_protein_per_day_title);
        }
        float maxProtein = kernel.getConsumer().getMaxProteinPerDay();
        summary = kernel.getWeightFormater().format(maxProtein, WeightFormatter.UnitFormat.SHORT);
        mMaxPerDayPreference.setSummary(summary);
    }
}
