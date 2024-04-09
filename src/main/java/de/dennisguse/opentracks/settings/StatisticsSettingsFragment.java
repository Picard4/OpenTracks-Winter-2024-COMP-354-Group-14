package de.dennisguse.opentracks.settings;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats.Frequency;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats.Metric;
import java.util.List;

public class StatisticsSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.settings_statistics);

        ListPreference metricPreference = findPreference(getString(R.string.plotting_metric_key));

        String[] metricEntries = new String[Metric.values().length]; //Metric display names
        String[] metricEntryValues = new String[Metric.values().length]; //Metric IDs

        int iterMetric = 0;
        for(Metric metric: Metric.values()) {
            metricEntries[iterMetric] = metric.toString();
            metricEntryValues[iterMetric] = Integer.toString(metric.getId());
            iterMetric++;
        }

        metricPreference.setEntries(metricEntries);
        metricPreference.setEntryValues(metricEntryValues);

        ListPreference freqPreference = findPreference(getString(R.string.plotting_frequency_key));
        String[] plottingFreqEntries = new String[Frequency.values().length]; //List of frequencies
        String[] plottingFreqValues = new String[Frequency.values().length]; //List of frequency IDs

        List<Frequency> allFreqValues = List.of(Frequency.values());
        int iterFreq = 0;
        for (Frequency freqValue: allFreqValues) {
            plottingFreqEntries[iterFreq] = Integer.toString(freqValue.getValue());
            plottingFreqValues[iterFreq] = Integer.toString(freqValue.getId());
            iterFreq++;
        }
        freqPreference.setEntries(plottingFreqEntries);
        freqPreference.setEntryValues(plottingFreqValues);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_stats_display_title);
    }
}