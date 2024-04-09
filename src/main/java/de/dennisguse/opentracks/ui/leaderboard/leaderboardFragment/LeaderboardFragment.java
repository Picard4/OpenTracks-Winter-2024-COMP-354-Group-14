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

    private int displayAmount = 0;

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

    public void setDisplayAmount(int newDisplayAmount){
        this.displayAmount = newDisplayAmount;
    }

    public void displayOnlySetAmount() {
        // Display all the ranks
        if (this.displayAmount <= 0)
            return;

        int currentDisplayAmount = this.displayAmount;

        // Average Ranking List
        // In case there is less rankings then the requested amount
        if (this.averageRankingList.size() < currentDisplayAmount)
            currentDisplayAmount = this.averageRankingList.size();

        List<Ranking> newAverageRankingList = new ArrayList<>();
        for (int i = 0; i < currentDisplayAmount; i++) {
            newAverageRankingList.add(this.averageRankingList.get(i));
        }
        this.averageRankingList = newAverageRankingList;

        // Best Ranking List
        // In case there is less rankings then the requested amount
        currentDisplayAmount = this.displayAmount;
        if (this.bestRankingList.size() < currentDisplayAmount)
            currentDisplayAmount = this.bestRankingList.size();

        List<Ranking> newBestRankingList = new ArrayList<>();
        for (int i = 0; i < currentDisplayAmount; i++) {
            newBestRankingList.add(this.bestRankingList.get(i));
        }
        this.bestRankingList = newBestRankingList;
    }
    public void updateRankingLists(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        this.averageRankingList = calculateLatestAverageRankingsData(latestLeaderboardData);
        this.bestRankingList = calculateLatestBestRankingsData(latestLeaderboardData);

        displayOnlySetAmount();
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

    protected class SummedStatTrackUser {
        private LeaderboardPagerAdapter.PlaceHolderTrackUser placeHolderTrackUser;
        private int sumFactorCount;

        public SummedStatTrackUser(LeaderboardPagerAdapter.PlaceHolderTrackUser placeHolderTrackUser) {
            this.placeHolderTrackUser = placeHolderTrackUser;
            sumFactorCount = 1;
        }

        public LeaderboardPagerAdapter.PlaceHolderTrackUser getPlaceHolderTrackUser() {
            return placeHolderTrackUser;
        }

        public int getSumFactorCount() {
            return sumFactorCount;
        }

        public void incrementSumFactorCount() {
            sumFactorCount++;
        }
    }
}