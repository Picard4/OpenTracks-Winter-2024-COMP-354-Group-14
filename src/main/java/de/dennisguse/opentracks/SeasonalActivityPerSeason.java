package de.dennisguse.opentracks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

import de.dennisguse.opentracks.data.TrackSelection;
import de.dennisguse.opentracks.databinding.ActivitySeasonalBinding;
import de.dennisguse.opentracks.databinding.ActivitySeasonalPerSeasonBinding;
import de.dennisguse.opentracks.ui.aggregatedStatistics.AggregatedStatisticsAdapter;
import de.dennisguse.opentracks.ui.aggregatedStatistics.AggregatedStatisticsModel;
import de.dennisguse.opentracks.ui.aggregatedStatistics.DummyDataGenerator;
import de.dennisguse.opentracks.ui.aggregatedStatistics.FilterDialogFragment;

public class SeasonalActivityPerSeason extends AbstractActivity {
    private final TrackSelection selection = new TrackSelection();
    private AggregatedStatisticsModel viewModel;
    private AggregatedStatisticsAdapter adapter;
    private ActivitySeasonalPerSeasonBinding viewBinding;
    private LocalDateTime from;
    private LocalDateTime to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();

        if (data != null)
        {
            changeTitle(data.getString("seasonTitle"));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AggregatedStatisticsAdapter(this, null);
        viewBinding.aggregatedRecyclerView.setLayoutManager(layoutManager);
        viewBinding.aggregatedRecyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(AggregatedStatisticsModel.class);
        selection.addActivityType("skiing");

        if(data != null && !Objects.equals(data.getString("seasonTitle"), getResources().getString(R.string.Life_Time_Data))){
            setFromToDate(data.getString("seasonTitle"));
            selection.addDateRange(from.atZone(ZoneId.systemDefault()).toInstant(), to.atZone(ZoneId.systemDefault()).toInstant());
            viewModel.updateSelection(selection);
        }

        viewModel.getAggregatedStats(selection).observe(this, aggregatedStatistics -> {
            if (aggregatedStatistics != null) {
                adapter.swapData(aggregatedStatistics);
            }
            checkListEmpty();
        });

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    private void checkListEmpty() {
        if (adapter.getItemCount() == 0) {
            viewBinding.aggregatedRecyclerView.setVisibility(View.GONE);
            viewBinding.seasonPerSeasonEmptyText.setVisibility(View.VISIBLE);
        } else {
            viewBinding.aggregatedRecyclerView.setVisibility(View.VISIBLE);
            viewBinding.seasonPerSeasonEmptyText.setVisibility(View.GONE);
        }
    }

    private void MakeYesNoButton(Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Heading to " + button.getText()).setMessage("Is this okay?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Do stuff
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
    }

    private void changeTitle(String newTitle) {
        MaterialToolbar materialToolbar = (MaterialToolbar)findViewById(R.id.season_materialtoolbar);
        materialToolbar.setTitle(newTitle);
    }

    @Override
    protected View getRootView() {
        viewBinding = ActivitySeasonalPerSeasonBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    private void setFromToDate(String title){
        if(Objects.equals(title, getResources().getString(R.string.Winter_2024))){
            from = LocalDateTime.of(2024, 1, 1, 0, 0);
            to = LocalDateTime.of(2024, 4, 8, 23, 59);
        }
        else if(Objects.equals(title, getResources().getString(R.string.Winter_2023))){
            from = LocalDateTime.of(2023, 1, 1, 0, 0);
            to = LocalDateTime.of(2023, 4, 8, 23, 59);
        }
        else if(Objects.equals(title, getResources().getString(R.string.Winter_2022))){
            from = LocalDateTime.of(2022, 1, 1, 0, 0);
            to = LocalDateTime.of(2022, 4, 8, 23, 59);
        }
    }
}