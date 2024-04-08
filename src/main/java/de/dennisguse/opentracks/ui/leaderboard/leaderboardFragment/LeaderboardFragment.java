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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public abstract class LeaderboardFragment extends Fragment {

    private static DecimalFormat scoreDecimalFormat;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<Ranking> averageRankingList;
    private List<Ranking> bestRankingList;

    public LeaderboardFragment() {
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<>());

    }

    public enum LeaderboardType {
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

    protected abstract List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData);
    protected abstract List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData);

    public void updateRankingLists(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        this.averageRankingList = calculateLatestAverageRankingsData(latestLeaderboardData);
        this.bestRankingList = calculateLatestBestRankingsData(latestLeaderboardData);
    }

    public void setDisplayedRankingList(LeaderboardType leaderboardType) {
        if (leaderboardType == LeaderboardType.Average)
            leaderboardAdapter.setDisplayedRankingList(averageRankingList);
        else if (leaderboardType == LeaderboardType.Best)
            leaderboardAdapter.setDisplayedRankingList(bestRankingList);
    }

    protected static DecimalFormat getScoreDecimalFormat() {
        if (scoreDecimalFormat == null)
            scoreDecimalFormat = new DecimalFormat("0.00000");
        return scoreDecimalFormat;
    }
}