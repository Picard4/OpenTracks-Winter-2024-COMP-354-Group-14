package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public class MovingTimeLeaderboardFragment extends LeaderboardFragment {

    // Temporary boolean to confirm that refresh works when expected to; delete once Issue 67 is finished.
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
        sortRankingsByDistance(latestRankingsData);
        return latestRankingsData;
    }

    @Override
    protected List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        // TODO: Replace the test data with code that gathers the appropriate Ranking data
        List<Ranking> latestRankingsData;
        if (!bestRefresh)
            latestRankingsData = getAltTestData();
        else
            latestRankingsData = getTestData();
        bestRefresh = true;
        sortRankingsByDistance(latestRankingsData);
        return latestRankingsData;
    }

    private void sortRankingsByDistance(List<Ranking> rankings) {
        rankings.sort((r1, r2) -> Double.compare(Double.parseDouble(r2.getScore()), Double.parseDouble(r1.getScore())));

        for (int i = 0; i < rankings.size(); i++) {
            Ranking ranking = rankings.get(i);
            ranking.setRank(i + 1);
        }
    }

    private List<Ranking> getTestData() {
        List<Ranking> rankings = new ArrayList<>();
        rankings.add(new Ranking(1, "Da bes", "Steamboat Springs",  Double.toString(20)));
        rankings.add(new Ranking(2, "Second place", "North California CA",  Double.toString(24)));
        rankings.add(new Ranking(3, "Tertiary", "Steamboat Springs, Color Red",  Double.toString(23)));
        rankings.add(new Ranking(4,  "Quad Runner", "Montreal",  Double.toString(22)));
        rankings.add(new Ranking(5,  "Quintuple champ", "Steamboat Springs",  Double.toString(26)));
        rankings.add(new Ranking(6,  "Gang of Six", "Montreal",  Double.toString(25)));
        rankings.add(new Ranking(7,  "Seven", "Steamboat Springs",  Double.toString(19)));
        rankings.add(new Ranking(8,  "Eight", "Montreal",  Double.toString(18)));
        rankings.add(new Ranking(9,  "Ninth", "Montreal",  Double.toString(30)));
        rankings.add(new Ranking(10,  "DoubleDigits", "Steamboat Springs",  Double.toString(16)));
        rankings.add(new Ranking(11,  "El e Ven", "Montreal",  Double.toString(15)));
        rankings.add(new Ranking(12,  "Twelve", "Montreal",  Double.toString(14)));
        rankings.add(new Ranking(13,  "XIII", "Montreal",  Double.toString(13)));
        rankings.add(new Ranking(14,  "Four Teen", "Steamboat Springs",  Double.toString(12)));
        rankings.add(new Ranking(15, "Fif Teen", "Montreal",  Double.toString(11)));
        rankings.add(new Ranking(16, "Six Teen", "Steamboat Springs",  Double.toString(10)));
        rankings.add(new Ranking(17,  "Seven+10", "Montreal",  Double.toString(29)));
        rankings.add(new Ranking(18,  "Eight een", "Montreal",  Double.toString(8)));
        rankings.add(new Ranking(19,  "Nineteen", "Steamboat Springs",  Double.toString(7)));
        rankings.add(new Ranking(20,  "10+10", "Steamboat Springs",  Double.toString(6)));
        rankings.add(new Ranking(21,  "Twenty-first", "Montreal",  Double.toString(5)));
        rankings.add(new Ranking(22,  "TwoTwo", "Montreal",  Double.toString(4)));
        rankings.add(new Ranking(23,  "TwoThree", "Steamboat Springs",  Double.toString(3)));
        rankings.add(new Ranking(24,  "The day", "Montreal",  Double.toString(28)));
        rankings.add(new Ranking(25,  "The saved day", "Montreal",  Double.toString(1)));
        return rankings;
    }

    private List<Ranking> getAltTestData() {
        List<Ranking> rankings = new ArrayList<>();
        rankings.add(new Ranking(1, "Da bes", "Steamboat Springs",  Double.toString(25)));
        rankings.add(new Ranking(2,  "The saved day", "Montreal",  Double.toString(1)));
        return rankings;
    }
}
