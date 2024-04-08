package de.dennisguse.opentracks.ui.leaderboard;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.AverageMovingSpeedLeaderboardFragment;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.MaxSpeedLeaderboardFragment;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.DistanceLeaderboardFragment;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.LeaderboardFragment;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.MovingTimeLeaderboardFragment;

public class LeaderboardPagerAdapter extends FragmentPagerAdapter {
    private MovingTimeLeaderboardFragment movingTimeLeaderboardFragment;
    private DistanceLeaderboardFragment distanceLeaderboardFragment;
    private MaxSpeedLeaderboardFragment maxSpeedLeaderboardFragment;
    private AverageMovingSpeedLeaderboardFragment averageMovingSpeedLeaderboardFragment;
    private LeaderboardFragment currentLeaderboardFragment;
    private LeaderboardFragment.LeaderboardType currentLeaderboardType;

    public LeaderboardPagerAdapter(FragmentManager fm) {
        super(fm);
        movingTimeLeaderboardFragment = new MovingTimeLeaderboardFragment();
        distanceLeaderboardFragment = new DistanceLeaderboardFragment();
        maxSpeedLeaderboardFragment = new MaxSpeedLeaderboardFragment();
        averageMovingSpeedLeaderboardFragment = new AverageMovingSpeedLeaderboardFragment();

        currentLeaderboardFragment = movingTimeLeaderboardFragment;
        currentLeaderboardType = LeaderboardFragment.LeaderboardType.Average;
        refreshLeaderboardFragmentData();
    }

    public enum LeaderboardType {
        MovingTime(0, "Moving Time"),
        Distance(1, "Distance"),
        MaxSpeed(2, "Max Speed"),
        AverageMovingSpeed(3, "Average Speed");

        // Assisted by https://stackoverflow.com/questions/1067352/can-i-set-enum-start-value-in-java
        private final int value;
        private final String title;

        private LeaderboardType(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }

    public void setCurrentLeaderboardFragment(int position) {
        if (position == LeaderboardPagerAdapter.LeaderboardType.MovingTime.value)
            currentLeaderboardFragment = movingTimeLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.Distance.value)
            currentLeaderboardFragment = distanceLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MaxSpeed.value)
            currentLeaderboardFragment = maxSpeedLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AverageMovingSpeed.value)
            currentLeaderboardFragment = averageMovingSpeedLeaderboardFragment;
        currentLeaderboardFragment.setDisplayedRankingList(currentLeaderboardType);
    }

    @Override
    public int getCount() {
        return LeaderboardPagerAdapter.LeaderboardType.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        // Return the appropriate Fragment for each tab position
        if (position == LeaderboardPagerAdapter.LeaderboardType.MovingTime.value)
            return movingTimeLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.Distance.value)
            return distanceLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MaxSpeed.value)
            return maxSpeedLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AverageMovingSpeed.value)
            return averageMovingSpeedLeaderboardFragment;
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == LeaderboardPagerAdapter.LeaderboardType.MovingTime.value)
            return LeaderboardPagerAdapter.LeaderboardType.MovingTime.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.Distance.value)
            return LeaderboardPagerAdapter.LeaderboardType.Distance.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MaxSpeed.value)
            return LeaderboardPagerAdapter.LeaderboardType.MaxSpeed.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AverageMovingSpeed.value)
            return LeaderboardPagerAdapter.LeaderboardType.AverageMovingSpeed.getTitle();
        return null;
    }

    public void setCurrentLeaderboardType(LeaderboardFragment.LeaderboardType leaderboardType) {
        if (leaderboardType == currentLeaderboardType)
            return;
        currentLeaderboardType = leaderboardType;
        currentLeaderboardFragment.setDisplayedRankingList(currentLeaderboardType);
    }

    public void refreshLeaderboardFragmentData() {
        List<PlaceHolderTrackUser> latestLeaderboardData = readLatestLeaderboardData();
        movingTimeLeaderboardFragment.updateRankingLists(latestLeaderboardData);
        distanceLeaderboardFragment.updateRankingLists(latestLeaderboardData);
        maxSpeedLeaderboardFragment.updateRankingLists(latestLeaderboardData);
        averageMovingSpeedLeaderboardFragment.updateRankingLists(latestLeaderboardData);
        currentLeaderboardFragment.setDisplayedRankingList(currentLeaderboardType);
    }

    private List<PlaceHolderTrackUser> readLatestLeaderboardData() {
        // This is where we get the data from the database for the runs that will be in the leaderboard.
        List<PlaceHolderTrackUser> testData = new ArrayList();
        testData.add(new PlaceHolderTrackUser("User One", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Two", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Three", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Four", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Five", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Six", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Seven", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Eight", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Nine", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("User Ten", true, new TrackStatistics()));
        testData.add(new PlaceHolderTrackUser("EXCLUDE ME!", false, new TrackStatistics()));
        return testData;
    }

    // This is a placeholder class so that we can start on the LeaderboardFragment child classes without needing real data
    public class PlaceHolderTrackUser {
        public String nickname;
        public boolean socialAllow;
        public TrackStatistics trackStatistics;

        public PlaceHolderTrackUser(String nickname, boolean socialAllow, TrackStatistics trackStatistics) {
            this.nickname = nickname;
            this.socialAllow = socialAllow;
            this.trackStatistics = trackStatistics;
        }
    }
}