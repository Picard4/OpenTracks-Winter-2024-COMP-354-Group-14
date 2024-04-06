package de.dennisguse.opentracks.ui.leaderboard;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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

    public LeaderboardPagerAdapter(FragmentManager fm) {
        super(fm);
        movingTimeLeaderboardFragment = new MovingTimeLeaderboardFragment();
        distanceLeaderboardFragment = new DistanceLeaderboardFragment();
        maxSpeedLeaderboardFragment = new MaxSpeedLeaderboardFragment();
        averageMovingSpeedLeaderboardFragment = new AverageMovingSpeedLeaderboardFragment();
        currentLeaderboardFragment = movingTimeLeaderboardFragment;
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
        if (position == LeaderboardType.MovingTime.value)
            currentLeaderboardFragment = movingTimeLeaderboardFragment;
        else if (position == LeaderboardType.Distance.value)
            currentLeaderboardFragment = distanceLeaderboardFragment;
        else if (position == LeaderboardType.MaxSpeed.value)
            currentLeaderboardFragment = maxSpeedLeaderboardFragment;
        else if (position == LeaderboardType.AverageMovingSpeed.value)
            currentLeaderboardFragment = averageMovingSpeedLeaderboardFragment;
    }

    public void refreshCurrentLeaderboardFragment() {
        currentLeaderboardFragment.refreshRankingsData();
    }

    @Override
    public int getCount() {
        return LeaderboardType.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        // Return the appropriate Fragment for each tab position
        if (position == LeaderboardType.MovingTime.value)
            return movingTimeLeaderboardFragment;
        else if (position == LeaderboardType.Distance.value)
            return distanceLeaderboardFragment;
        else if (position == LeaderboardType.MaxSpeed.value)
            return maxSpeedLeaderboardFragment;
        else if (position == LeaderboardType.AverageMovingSpeed.value)
            return averageMovingSpeedLeaderboardFragment;
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == LeaderboardType.MovingTime.value)
            return LeaderboardType.MovingTime.getTitle();
        else if (position == LeaderboardType.Distance.value)
            return LeaderboardType.Distance.getTitle();
        else if (position == LeaderboardType.MaxSpeed.value)
            return LeaderboardType.MaxSpeed.getTitle();
        else if (position == LeaderboardType.AverageMovingSpeed.value)
            return LeaderboardType.AverageMovingSpeed.getTitle();
        return null;
    }
}

