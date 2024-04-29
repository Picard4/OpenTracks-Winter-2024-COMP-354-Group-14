package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public class MovingTimeLeaderboardFragment extends LeaderboardFragment {

    @Override
    protected List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, SummedStatTrackUser> statsMap = new HashMap<>();
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            if (!trackUser.socialAllow)
                continue;

            statsMap.computeIfAbsent(trackUser.nickname, k -> new SummedStatTrackUser(trackUser, trackUser.trackStatistics.getMovingTime().abs()));
            SummedStatTrackUser existingRecord = statsMap.get(trackUser.nickname);
            Duration oldDuration = (Duration)existingRecord.getScoreSum();
            existingRecord.setScoreSum(
                    oldDuration.plus(trackUser.trackStatistics.getMovingTime())
            );
            existingRecord.incrementSumFactorCount();
        }

        List<SummedStatTrackUser> latestSummedLeaderboardData = new ArrayList<>(statsMap.values());
        latestSummedLeaderboardData.sort(new SortByAverageMovingTime());

        List<Ranking> rankingsData = new ArrayList<>();
        int rank = 0;
        int consecutiveTies = 0;
        String lastScore = "";
        for (SummedStatTrackUser summedStatTrackUser : latestSummedLeaderboardData) {
            Duration totalMovingTime = (Duration)summedStatTrackUser.getScoreSum();
            Duration averageMovingTime = totalMovingTime.dividedBy(summedStatTrackUser.getSumFactorCount());
            Ranking nextRanking = new Ranking(
                    ++rank,
                    summedStatTrackUser.getPlaceHolderTrackUser().nickname,
                    summedStatTrackUser.getPlaceHolderTrackUser().location,
                    getTimeDisplay(averageMovingTime)
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

    private class SortByAverageMovingTime implements Comparator<SummedStatTrackUser> {
        @Override
        public int compare(SummedStatTrackUser user1, SummedStatTrackUser user2) {
            Duration user1MovingTime = (Duration) user1.getScoreSum();
            Duration user2MovingTime = (Duration) user2.getScoreSum();
            return user2MovingTime.compareTo(user1MovingTime);
        }
    }

    private String getTimeDisplay(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    @Override
    protected List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, LeaderboardPagerAdapter.PlaceHolderTrackUser> statsMap = new HashMap<>();
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            if (!trackUser.socialAllow)
                continue;

            statsMap.computeIfAbsent(trackUser.nickname, k -> trackUser);
            if (statsMap.get(trackUser.nickname).trackStatistics.getMovingTime().compareTo(trackUser.trackStatistics.getMovingTime()) < 0) {
                statsMap.put(trackUser.nickname, trackUser);
            }
        }

        latestLeaderboardData = new ArrayList<>(statsMap.values());
        latestLeaderboardData.sort(new SortByBestMovingTime());

        List<Ranking> rankingsData = new ArrayList<>();
        int rank = 0;
        int consecutiveTies = 0;
        String lastScore = "";
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser trackUser : latestLeaderboardData) {
            Ranking nextRanking = new Ranking(
                    ++rank,
                    trackUser.nickname,
                    trackUser.location,
                    getTimeDisplay(trackUser.trackStatistics.getMovingTime())
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

    private class SortByBestMovingTime implements Comparator<LeaderboardPagerAdapter.PlaceHolderTrackUser> {
        @Override
        public int compare(LeaderboardPagerAdapter.PlaceHolderTrackUser user1, LeaderboardPagerAdapter.PlaceHolderTrackUser user2) {
            return user2.trackStatistics.getMovingTime().compareTo(user1.trackStatistics.getMovingTime());
        }
    }
}
