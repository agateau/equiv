package com.greenyetilab.equiv.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.ProteinWeightUnit;

/**
 * Fragment storing all the settings
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ListPreference mProteinWeightUnitPreference;
    private EditTextPreference mProteinPerDayPreference;
    private SharedPreferences mPreferences;
    private WeightFormatter mProteinFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProteinFormatter = new WeightFormatter(getResources());
        mProteinFormatter.setProteinFormat(ProteinWeightUnit.PROTEIN);

        addPreferencesFromResource(R.xml.preferences);
        mPreferences = getPreferenceManager().getSharedPreferences();
        mPreferences.registerOnSharedPreferenceChangeListener(this);

        mProteinWeightUnitPreference = (ListPreference) findPreference("protein_weight_unit");
        mProteinPerDayPreference = (EditTextPreference) findPreference("max_protein_per_day");
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
        Kernel.getExistingInstance().updateFromPreferences(prefs);
        updateSummaries();
    }

    private void updateSummaries() {
        Kernel kernel = Kernel.getExistingInstance();
        String summary;

        summary = mProteinWeightUnitPreference.getEntry().toString();
        mProteinWeightUnitPreference.setSummary(summary);

        float maxProtein = kernel.getConsumer().getMaxProteinPerDay();
        if (kernel.getProteinUnit() == ProteinWeightUnit.POTATO) {
            String proteinSummary = mProteinFormatter.format(maxProtein, WeightFormatter.UnitFormat.SHORT);
            String potatoSummary = kernel.getWeightFormater().format(maxProtein);
            summary = String.format("%s (= %s)", proteinSummary, potatoSummary);
        } else {
            summary = kernel.getWeightFormater().format(maxProtein, WeightFormatter.UnitFormat.SHORT);
        }
        mProteinPerDayPreference.setSummary(summary);
    }
}
