package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennisguse.opentracks.data.models.Ranking;
import de.dennisguse.opentracks.ui.leaderboard.LeaderboardPagerAdapter;

public class MaxSpeedLeaderboardFragment extends LeaderboardFragment {

    @Override
    protected List<Ranking> calculateLatestAverageRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, List<Double>> userMaxSpeedsMap = new HashMap<>();
        Map<String, String> userLocationsMap = new HashMap<>();

        //put together all maximum speeds for each user and update their location. associate user's average speed with their last known location
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser user : latestLeaderboardData) {
            if (!user.socialAllow)
                continue;

            List<Double> speeds = userMaxSpeedsMap.get(user.nickname);
            if (speeds == null) {
                speeds = new ArrayList<>();
                userMaxSpeedsMap.put(user.nickname, speeds);
            }
            speeds.add(user.trackStatistics.getMaxSpeed().speed_mps());

            userLocationsMap.put(user.nickname, user.location);
        }

        //calculate the average of all maximum speeds for each user
        Map<String, Double> userAverageMaxSpeedsMap = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : userMaxSpeedsMap.entrySet()) {
            List<Double> speeds = entry.getValue();
            double sum = 0;
            for (double speed : speeds) {
                sum += speed;
            }
            double average = speeds.isEmpty() ? 0 : sum / speeds.size();
            userAverageMaxSpeedsMap.put(entry.getKey(), average);
        }

        //Sort users based on their average maximum speed
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(userAverageMaxSpeedsMap.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<Ranking> rankings = new ArrayList<>();
        int rank = 0;
        double lastAverageMaxSpeed = -1;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            String nickname = entry.getKey();
            double currentAverageMaxSpeed = entry.getValue();
            if (currentAverageMaxSpeed != lastAverageMaxSpeed) {
                rank = rankings.size() + 1;
            }
            String location = userLocationsMap.get(nickname);
            rankings.add(new Ranking(rank, nickname, location, String.format("%.2f mps", currentAverageMaxSpeed)));
            lastAverageMaxSpeed = currentAverageMaxSpeed;
        }
        return rankings;
    }

    @Override
    protected List<Ranking> calculateLatestBestRankingsData(List<LeaderboardPagerAdapter.PlaceHolderTrackUser> latestLeaderboardData) {
        Map<String, LeaderboardPagerAdapter.PlaceHolderTrackUser> maxSpeedMap = new HashMap<>();

        //Keep track of each users best max speed
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser user : latestLeaderboardData) {
            if (!user.socialAllow)
                continue;

            LeaderboardPagerAdapter.PlaceHolderTrackUser currentBest = maxSpeedMap.get(user.nickname);
            if (currentBest == null || user.trackStatistics.getMaxSpeed().speed_mps() > currentBest.trackStatistics.getMaxSpeed().speed_mps()) {
                maxSpeedMap.put(user.nickname, user);
            }
        }

        //Sort users based on their maximum speed
        List<LeaderboardPagerAdapter.PlaceHolderTrackUser> sortedUsers = new ArrayList<>(maxSpeedMap.values());
        sortedUsers.sort((user1, user2) -> Double.compare(user2.trackStatistics.getMaxSpeed().speed_mps(), user1.trackStatistics.getMaxSpeed().speed_mps()));

        List<Ranking> rankings = new ArrayList<>();
        int rank = 0;
        double lastMaxSpeed = -1;
        for (LeaderboardPagerAdapter.PlaceHolderTrackUser user : sortedUsers) {
            double currentMaxSpeed = user.trackStatistics.getMaxSpeed().speed_mps();
            if (currentMaxSpeed != lastMaxSpeed) {
                rank = rankings.size() + 1;
            }
            rankings.add(new Ranking(rank, user.nickname, user.location, String.format("%.2f mps", currentMaxSpeed)));
            lastMaxSpeed = currentMaxSpeed;
        }
        return rankings;
    }
}