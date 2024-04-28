package de.dennisguse.opentracks.ui.leaderboard;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Speed;
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
        MOVING_TIME(0, "Moving Time"),
        DISTANCE(1, "Distance"),
        MAX_SPEED(2, "Max Speed"),
        AVERAGE_MOVING_SPEED(3, "Average Speed");

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
        if (position == LeaderboardPagerAdapter.LeaderboardType.MOVING_TIME.value)
            currentLeaderboardFragment = movingTimeLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.DISTANCE.value)
            currentLeaderboardFragment = distanceLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MAX_SPEED.value)
            currentLeaderboardFragment = maxSpeedLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AVERAGE_MOVING_SPEED.value)
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
        if (position == LeaderboardPagerAdapter.LeaderboardType.MOVING_TIME.value)
            return movingTimeLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.DISTANCE.value)
            return distanceLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MAX_SPEED.value)
            return maxSpeedLeaderboardFragment;
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AVERAGE_MOVING_SPEED.value)
            return averageMovingSpeedLeaderboardFragment;
        return currentLeaderboardFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == LeaderboardPagerAdapter.LeaderboardType.MOVING_TIME.value)
            return LeaderboardPagerAdapter.LeaderboardType.MOVING_TIME.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.DISTANCE.value)
            return LeaderboardPagerAdapter.LeaderboardType.DISTANCE.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.MAX_SPEED.value)
            return LeaderboardPagerAdapter.LeaderboardType.MAX_SPEED.getTitle();
        else if (position == LeaderboardPagerAdapter.LeaderboardType.AVERAGE_MOVING_SPEED.value)
            return LeaderboardPagerAdapter.LeaderboardType.AVERAGE_MOVING_SPEED.getTitle();
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

    public void setNumberOfUsers(int numberOfUsers) {
        movingTimeLeaderboardFragment.setDisplayAmount(numberOfUsers);
        distanceLeaderboardFragment.setDisplayAmount(numberOfUsers);
        maxSpeedLeaderboardFragment.setDisplayAmount(numberOfUsers);
        averageMovingSpeedLeaderboardFragment.setDisplayAmount(numberOfUsers);
        refreshLeaderboardFragmentData();
    }
    private List<PlaceHolderTrackUser> readLatestLeaderboardData() {
        // This is where we get the data from the database for the runs that will be in the leaderboard.
        List<PlaceHolderTrackUser> testData = new ArrayList();

        TrackStatistics stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(500));
        stats.setMaxSpeed(new Speed(40));
        stats.setMovingTime(Duration.ofMinutes(900));
        testData.add(new PlaceHolderTrackUser("User One", "Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(550));
        stats.setMaxSpeed(new Speed(50));
        stats.setMovingTime(Duration.ofMinutes(950));
        testData.add(new PlaceHolderTrackUser("User One","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(100));
        stats.setMaxSpeed(new Speed(90));
        stats.setMovingTime(Duration.ofHours(2));
        testData.add(new PlaceHolderTrackUser("User Two", "Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(150));
        stats.setMaxSpeed(new Speed(100));
        stats.setMovingTime(Duration.ofHours(1));
        testData.add(new PlaceHolderTrackUser("User Two", "Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(1000));
        stats.setMaxSpeed(new Speed(20));
        stats.setMovingTime(Duration.ofHours(3));
        testData.add(new PlaceHolderTrackUser("User Three","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(900));
        stats.setMaxSpeed(new Speed(19));
        stats.setMovingTime(Duration.ofHours(4));
        testData.add(new PlaceHolderTrackUser("User Three","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(2000));
        stats.setMaxSpeed(new Speed(25));
        stats.setMovingTime(Duration.ofHours(6));
        testData.add(new PlaceHolderTrackUser("User Four","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(2500));
        stats.setMaxSpeed(new Speed(55));
        stats.setMovingTime(Duration.ofHours(6));
        testData.add(new PlaceHolderTrackUser("User Four","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(10000));
        stats.setMaxSpeed(new Speed(32));
        stats.setMovingTime(Duration.ofHours(4));
        testData.add(new PlaceHolderTrackUser("User Five","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(1000));
        stats.setMaxSpeed(new Speed(50));
        stats.setMovingTime(Duration.ofHours(1));
        testData.add(new PlaceHolderTrackUser("User Five","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(1000));
        stats.setMaxSpeed(new Speed(91));
        stats.setMovingTime(Duration.ofMinutes(95));
        testData.add(new PlaceHolderTrackUser("User Six","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(100));
        stats.setMaxSpeed(new Speed(65));
        stats.setMovingTime(Duration.ofMinutes(49));
        testData.add(new PlaceHolderTrackUser("User Seven","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(1200));
        stats.setMaxSpeed(new Speed(99));
        stats.setMovingTime(Duration.ofMinutes(200));
        testData.add(new PlaceHolderTrackUser("User Eight","North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(152));
        stats.setMaxSpeed(new Speed(102));
        stats.setMovingTime(Duration.ofMinutes(30));
        testData.add(new PlaceHolderTrackUser("User Nine", "North California CA", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(154));
        stats.setMaxSpeed(new Speed(150));
        stats.setMovingTime(Duration.ofMinutes(59));
        testData.add(new PlaceHolderTrackUser("User Ten","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(134));
        stats.setMaxSpeed(new Speed(130));
        stats.setMovingTime(Duration.ofMinutes(59));
        testData.add(new PlaceHolderTrackUser("User Eleven","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(159));
        stats.setMaxSpeed(new Speed(120));
        stats.setMovingTime(Duration.ofMinutes(34));
        testData.add(new PlaceHolderTrackUser("User Twelve","Steamboat Springs", true, stats));

        stats = new TrackStatistics();
        stats.setTotalDistance(new Distance(100));
        stats.setMaxSpeed(new Speed(90));
        stats.setMovingTime(Duration.ofHours(2));
        testData.add(new PlaceHolderTrackUser("EXCLUDE ME!","North California CA", false, stats));
        return testData;
    }

    // This is a placeholder class so that we can start on the LeaderboardFragment child classes without needing real data
    public class PlaceHolderTrackUser {
        public String nickname;
        public String location;
        public boolean socialAllow;
        public TrackStatistics trackStatistics;

        public PlaceHolderTrackUser(String nickname, String location, boolean socialAllow, TrackStatistics trackStatistics) {
            this.nickname = nickname;
            this.location = location;
            this.socialAllow = socialAllow;
            this.trackStatistics = trackStatistics;
        }
    }
}