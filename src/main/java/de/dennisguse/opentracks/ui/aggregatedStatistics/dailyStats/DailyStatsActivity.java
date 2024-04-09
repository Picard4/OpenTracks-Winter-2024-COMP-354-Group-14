package de.dennisguse.opentracks.ui.aggregatedStatistics.dailyStats;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.databinding.DailyStatsBinding;
import de.dennisguse.opentracks.settings.PreferencesUtils;

public class DailyStatsActivity extends AbstractActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private DailyStatsBinding viewBinding;
    private Metric selectedMetric = null;
    private Frequency selectedFrequency = null;
    private final DailyPlottingModule dailyPlottingModule = new DailyPlottingModule();
    private LineChart line_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create and populate the spin_metrics spinner with Metric enums.
        Spinner spin_metrics = (Spinner)findViewById(R.id.daily_metric);
        ArrayAdapter<Metric> array_metrics = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Metric.values());
        spin_metrics.setAdapter(array_metrics);


        // Create and populate the spin_freq spinner with Metric enums.
        Spinner spin_freq = (Spinner)findViewById(R.id.daily_data_point);
        ArrayAdapter<Frequency> array_freq = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Frequency.values());
        spin_freq.setAdapter(array_freq);

        line_chart = (LineChart) findViewById(R.id.dailyChart);

        setDefaultMetricSpinner(spin_metrics,array_metrics);
        setDefaultFrequencySpinner(spin_freq,array_freq);
        // create and attach listener for export button object
        Button exportButton = findViewById(R.id.saveIMG);
        exportButton.setOnClickListener(this);

        // Attach one listener to both spinners
        spin_metrics.setOnItemSelectedListener(this);
        spin_freq.setOnItemSelectedListener(this);


        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    // Implement the onItemSelected method for the spin_metrics and spin_freq spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Check which spinner triggered the event
        if (parent.getId() == R.id.daily_metric) {
            // Handle metric selection
            selectedMetric = Metric.values()[position];
            Log.d("DAILY_STATS", "ENUM: \"" + selectedMetric.toString() + "\" selected.");
        } else if (parent.getId() == R.id.daily_data_point) {
            // Handle frequency selection
            selectedFrequency = Frequency.values()[position];
            Log.d("DAILY_STATS", "ENUM: \"" + selectedFrequency.toString() + "\" selected.");
        }

        // Check if both spinners have a value selected
        if (selectedMetric != null && selectedFrequency != null) {
            dailyPlottingModule.plotGraph(line_chart, selectedMetric, selectedFrequency);
            Log.d("DAILY_STATS_DRAW_CHART", "Metric: " + selectedMetric.toString() + ", Frequency: " + selectedFrequency.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Check which spinner triggered the event
        if (parent.getId() == R.id.daily_metric) {
            // Handle metric selection
            selectedMetric = null;
        } else if (parent.getId() == R.id.daily_data_point) {
            // Handle frequency selection
            selectedFrequency = null;
        }

        line_chart.clear();
    }

    @Override
    public void onClick(View parent) {
        if (parent.getId() == R.id.saveIMG) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";

            DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
            String name = df.format(new Date());

            int i = 1;
            if ((new File(path + name + ".png")).exists()) {
                while ((new File(path + name + " (" + i + ").png")).exists())
                    i++;
                name += " (" + i + ")";
            }

            line_chart.saveToPath(name, "/DCIM/");
        }
    }
    private void setDefaultMetricSpinner(Spinner spinner, ArrayAdapter<Metric> array_metrics)
    {
        int preferenceValue = PreferencesUtils.getMetricPreferenceValue();
        Metric item = Metric.findById(preferenceValue);
        int position = array_metrics.getPosition(item);
        spinner.setSelection(position);
    }
    private void setDefaultFrequencySpinner(Spinner spinner, ArrayAdapter<Frequency> array_frequency)
    {
        int preferenceValue = PreferencesUtils.getFrequencyPreferenceValue();
        Frequency item = Frequency.findById(preferenceValue);
        int position = array_frequency.getPosition(item);
        spinner.setSelection(position);
    }


    @Override
    protected View getRootView() {
        viewBinding = DailyStatsBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}