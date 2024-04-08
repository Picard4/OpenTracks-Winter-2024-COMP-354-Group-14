package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public class DistanceLeaderboardFragment extends LeaderboardFragment {
    private boolean averageRefresh;
    private boolean bestRefresh;

    @Override
    protected List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        // TODO: Replace the test data with code that gathers the appropriate Ranking data
        List<Ranking> latestRankingsData;
        if (!averageRefresh)
            // Get a different data set if this is the first time the rankings data is being collected
            latestRankingsData = getTestData();
        else
            latestRankingsData = getAltTestData();
        // All future rankings data collections should be refreshes
        averageRefresh = true;
        return latestRankingsData;
    }

    @Override
    protected List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        // TODO: Replace the test data with code that gathers the appropriate Ranking data
        List<Ranking> latestRankingsData;
        if (!bestRefresh)
            // Get a different data set if this is the first time the rankings data is being collected
            latestRankingsData = getAltTestData();
        else
            latestRankingsData = getTestData();
        // All future rankings data collections should be refreshes
        bestRefresh = true;
        return latestRankingsData;
    }

    private List<Ranking> getTestData() {
        List<Ranking> rankings = new ArrayList<>();
        rankings.add(new Ranking(1, "User 1", "A",  Double.toString(25)));
        rankings.add(new Ranking(2, "User 2", "B",  Double.toString(24)));
        rankings.add(new Ranking(3, "User 3", "C",  Double.toString(23)));
        rankings.add(new Ranking(4,  "User 4", "D",  Double.toString(22)));
        rankings.add(new Ranking(5,  "User 5", "E",  Double.toString(21)));
        rankings.add(new Ranking(6,  "User 6", "F",  Double.toString(20)));
        rankings.add(new Ranking(7,  "User 7", "G",  Double.toString(19)));
        rankings.add(new Ranking(8,  "User 8", "H",  Double.toString(18)));
        rankings.add(new Ranking(9,  "User 9", "I",  Double.toString(17)));
        rankings.add(new Ranking(10,  "User 10", "J",  Double.toString(16)));
        rankings.add(new Ranking(11,  "User 11", "K",  Double.toString(15)));
        rankings.add(new Ranking(12,  "User 12", "L",  Double.toString(14)));
        rankings.add(new Ranking(13,  "User 13", "M",  Double.toString(13)));
        rankings.add(new Ranking(14,  "User 14", "N",  Double.toString(12)));
        rankings.add(new Ranking(15, "User 15", "O",  Double.toString(11)));
        return rankings;
    }


    private List<Ranking> getAltTestData() {
        List<Ranking> rankings = new ArrayList<>();
        rankings.add(new Ranking(1, "First", "Steamboat Springs",  Double.toString(25)));
        rankings.add(new Ranking(2, "Second", "North California CA",  Double.toString(24)));
        rankings.add(new Ranking(3, "Third", "Steamboat Springs, Color Red",  Double.toString(23)));
        rankings.add(new Ranking(4,  "Fourth", "Montreal",  Double.toString(22)));
        rankings.add(new Ranking(5,  "Fifth", "Steamboat Springs",  Double.toString(21)));
        rankings.add(new Ranking(6,  "Sixth", "Montreal",  Double.toString(20)));
        rankings.add(new Ranking(7,  "Seven", "Steamboat Springs",  Double.toString(19)));
        return rankings;
    }
}