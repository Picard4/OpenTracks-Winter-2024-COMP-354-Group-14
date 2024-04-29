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

/**
 * The base class for every Leaderboard supported by OpenTracks.
 */
public abstract class LeaderboardFragment extends Fragment {

    private static DecimalFormat scoreDecimalFormat;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<Ranking> averageRankingList;
    private List<Ranking> bestRankingList;

    public LeaderboardFragment() {
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<>());
    }

    public enum RankingListType {
        AVERAGE,
        BEST;
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

    /**
     * Reads the sent latestLeaderboardData to rank every user in the latestLeaderboardData by the average of their scores across their provided tracks
     * that they are willing to share with the public.
     * What exactly the "score" is depends on the child class that implements this method.
     * @param latestLeaderboardData The leaderboard data that this LeaderboardFragment's Average Ranking list will be based on.
     * @return A list of Average Rankings calculated from the sent latestLeaderboardData, in order from first to last.
     */
    protected abstract List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData);

    /**
     * Reads the sent latestLeaderboardData to rank every user in the latestLeaderboardData by their best score across their provided tracks
     * that they are willing to share with the public.
     * What exactly the "score" is depends on the child class that implements this method.
     * @param latestLeaderboardData The leaderboard data that this LeaderboardFragment's Best Ranking list will be based on.
     * @return A list of Best Rankings calculated from the sent latestLeaderboardData, in order from first to last.
     */
    protected abstract List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData);

    /**
     * Updates every Ranking list stored in this LeaderboardFragment using the sent latestLeaderboardData.
     * @param latestLeaderboardData The data that every Ranking list stored in this LeaderboardFragment will be based on.
     */
    public void updateRankingLists(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        this.averageRankingList = calculateLatestAverageRankingsData(latestLeaderboardData);
        this.bestRankingList = calculateLatestBestRankingsData(latestLeaderboardData);
    }

    /**
     * Changes the Ranking list that this LeaderboardFragment is displaying in the GUI.
     * @param rankingListType The type of Ranking list that this LeaderboardFragment ought to display.
     */
    public void setDisplayedRankingList(RankingListType rankingListType) {
        if (rankingListType == RankingListType.AVERAGE)
            leaderboardAdapter.setDisplayedRankingList(averageRankingList);
        else if (rankingListType == RankingListType.BEST)
            leaderboardAdapter.setDisplayedRankingList(bestRankingList);
    }

    protected static DecimalFormat getScoreDecimalFormat() {
        if (scoreDecimalFormat == null)
            scoreDecimalFormat = new DecimalFormat("0.00000");
        return scoreDecimalFormat;
    }

    /**
     * A class that assists with calculating average scores by allowing the statistic that is being
     * tracked as a "score" to be summed within the PlaceHolderTrackUser.
     */
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