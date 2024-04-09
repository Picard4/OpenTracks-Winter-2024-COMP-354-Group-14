package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public class DistanceLeaderboardFragment extends LeaderboardFragment {

    @Override
    protected List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, SummedStatTrackUser> statsMap = new HashMap<>();
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            if (!trackUser.socialAllow)
                continue;

            if (!statsMap.containsKey(trackUser.nickname)) {
                statsMap.put(trackUser.nickname, new SummedStatTrackUser(trackUser));
            }
            else {
                SummedStatTrackUser existingRecord = statsMap.get(trackUser.nickname);
                existingRecord.getPlaceHolderTrackUser().trackStatistics.setTotalDistance(
                        new Distance(existingRecord.getPlaceHolderTrackUser().trackStatistics.getTotalDistance().distance_m()
                                + trackUser.trackStatistics.getTotalDistance().distance_m())
                );
                existingRecord.incrementSumFactorCount();
            }
        }
        List<SummedStatTrackUser> latestSummedLeaderboardData = new ArrayList<>(statsMap.values());
        latestSummedLeaderboardData.sort(new SortByAverageDistance());

        List<Ranking> rankingsData = new ArrayList<>();
        int rank = 0;
        int consecutiveTies = 0;
        String lastScore = "";
        for (SummedStatTrackUser summedStatTrackUser : latestSummedLeaderboardData) {
            Ranking nextRanking = new Ranking(
                    ++rank,
                    summedStatTrackUser.getPlaceHolderTrackUser().nickname,
                    summedStatTrackUser.getPlaceHolderTrackUser().location,
                    getDistanceDisplay(getAverageDistanceFromSummedStatTrackUser(summedStatTrackUser))
            );
            if (nextRanking.getScore().equals(lastScore)) {
                consecutiveTies++;
                nextRanking.setRank(rank - consecutiveTies);
            }
            else
                consecutiveTies = 0;
            rankingsData.add(nextRanking);
            lastScore = nextRanking.getScore();
        }
        return rankingsData;
    }

    private class SortByAverageDistance implements Comparator<SummedStatTrackUser> {
        @Override
        public int compare(SummedStatTrackUser user1, SummedStatTrackUser user2) {
            Distance user1AverageDistance = getAverageDistanceFromSummedStatTrackUser(user1);
            Distance user2AverageDistance = getAverageDistanceFromSummedStatTrackUser(user2);

            if (user1AverageDistance.lessThan(user2AverageDistance))
                return 1;
            else if (user1AverageDistance.greaterThan(user2AverageDistance))
                return -1;
            return 0;
        }
    }

    private String getDistanceDisplay(Distance distance) {
        return distance.distance_m() + " m";
    }

    private Distance getAverageDistanceFromSummedStatTrackUser(SummedStatTrackUser summedStatTrackUser) {
        return new Distance(summedStatTrackUser.getPlaceHolderTrackUser().trackStatistics.getTotalDistance().distance_m() / summedStatTrackUser.getSumFactorCount());
    }

    @Override
    protected List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, LeaderboardPagerAdapter.PlaceHolderTrackUser> statsMap = new HashMap<>();
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            if (!trackUser.socialAllow)
                continue;

            if (!statsMap.containsKey(trackUser.nickname) ||
                    statsMap.get(trackUser.nickname).trackStatistics.getTotalDistance().lessThan(trackUser.trackStatistics.getTotalDistance())) {
                statsMap.put(trackUser.nickname, trackUser);
            }
        }
        latestLeaderboardData = new ArrayList<>(statsMap.values());
        latestLeaderboardData.sort(new SortByBestDistance());

        List<Ranking> rankingsData = new ArrayList<>();
        int rank = 0;
        int consecutiveTies = 0;
        String lastScore = "";
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            Ranking nextRanking = new Ranking(
                    ++rank,
                    trackUser.nickname,
                    trackUser.location,
                    getDistanceDisplay(trackUser.trackStatistics.getTotalDistance())
            );
            if (nextRanking.getScore().equals(lastScore)) {
                consecutiveTies++;
                nextRanking.setRank(rank - consecutiveTies);
            }
            else
                consecutiveTies = 0;
            rankingsData.add(nextRanking);
            lastScore = nextRanking.getScore();
        }
        return rankingsData;
    }

    private class SortByBestDistance implements Comparator<LeaderboardPagerAdapter.PlaceHolderTrackUser> {
        @Override
        public int compare(LeaderboardPagerAdapter.PlaceHolderTrackUser user1, LeaderboardPagerAdapter.PlaceHolderTrackUser user2) {
            Distance user1Distance = user1.trackStatistics.getTotalDistance();
            Distance user2Distance = user2.trackStatistics.getTotalDistance();

            if (user1Distance.lessThan(user2Distance))
                return 1;
            else if (user1Distance.greaterThan(user2Distance))
                return -1;
            return 0;
        }
    }

}