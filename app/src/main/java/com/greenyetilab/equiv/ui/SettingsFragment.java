package com.greenyetilab.equiv.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.FormatUtils;

/**
 * Fragment storing all the settings
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference mProteinPerDayPreference;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        mPreferences = getPreferenceManager().getSharedPreferences();
        mPreferences.registerOnSharedPreferenceChangeListener(this);

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

    private void updateSummaries() {
        float maxProtein = Kernel.getExistingInstance().getConsumer().getMaxProteinPerDay();
        String summary = String.format("%sg", FormatUtils.naturalRound(maxProtein));
        mProteinPerDayPreference.setSummary(summary);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Kernel.getExistingInstance().updateFromPreferences(prefs);
        updateSummaries();
    }
}
