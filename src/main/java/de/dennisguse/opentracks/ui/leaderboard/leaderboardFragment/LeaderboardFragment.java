package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Ranking;

public abstract class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;

    public LeaderboardFragment() {
        leaderboardAdapter = new LeaderboardAdapter(getLatestRankingsData());
    }

    public enum AggregationStrategy {
        Average,
        Best;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardRecyclerView = view.findViewById(R.id.leaderboardList);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        return view;
    }

    public void refreshRankingsData(AggregationStrategy aggregationStrategy) {
        leaderboardAdapter.setDisplayedRankingList(getLatestRankingsData(aggregationStrategy), aggregationStrategy);
    }

    protected abstract List<Ranking> getLatestRankingsData(AggregationStrategy aggregationStrategy);

    public void setCurrentAggregationStrategy(AggregationStrategy aggregationStrategy) {
        if (aggregationStrategy == currentAggregationStrategy)
            return;
        currentAggregationStrategy = aggregationStrategy;
        refreshRankingsData();
    }
}
