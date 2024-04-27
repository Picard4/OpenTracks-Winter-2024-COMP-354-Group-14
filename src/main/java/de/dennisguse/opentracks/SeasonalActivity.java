package de.dennisguse.opentracks;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

import de.dennisguse.opentracks.data.TrackSelection;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.ActivitySeasonalBinding;
import de.dennisguse.opentracks.ui.aggregatedStatistics.AggregatedStatisticsAdapter;
import de.dennisguse.opentracks.ui.aggregatedStatistics.AggregatedStatisticsModel;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;


/**
 * This view will allow users to see the list of each season and select the season they wish to see the aggregated data for.
 *
 * @author Woo Jun Ann, Zachary Therrien
 * */
public class SeasonalActivity extends AbstractActivity {
    public static final String EXTRA_TRACK_IDS = "track_ids";
    private final TrackSelection selection = new TrackSelection();
    private ActivitySeasonalBinding viewBinding;
    private AggregatedStatisticsModel viewModel;
    private AggregatedStatisticsAdapter adapter;
    private RecyclerView seasonsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Seasonal Activity");
        seasonsRecyclerView = findViewById(R.id.seasons_recyclerView);

        GoToIndividualSite(findViewById(R.id.Winter2022_Button));
        GoToIndividualSite(findViewById(R.id.Winter2023_Button));
        GoToIndividualSite(findViewById(R.id.Winter2024_Button));
        GoToAllTimeStats(findViewById(R.id.LifeTime_button));

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    private void GoToIndividualSite(Button button) {
        button.setOnClickListener(view -> {
            Intent intent = new Intent(SeasonalActivity.this, SeasonalActivityPerSeason.class);
            intent.putExtra("seasonTitle", button.getText());
            startActivity(intent);
        });
    }

    private void GoToAllTimeStats(Button button) {
        button.setOnClickListener(view -> {
            Intent intent = new Intent(SeasonalActivity.this, SeasonalActivityPerSeason.class);
            intent.putExtra("seasonTitle", button.getText());
            startActivity(intent);
        });
    }

    @Override
    protected View getRootView() {
        viewBinding = ActivitySeasonalBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
