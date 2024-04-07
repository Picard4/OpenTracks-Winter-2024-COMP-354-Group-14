package de.dennisguse.opentracks.ui.leaderboard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.LeaderboardFragment;

public class LeaderboardActivity extends AppCompatActivity {

    private int numberOfUsers = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        LeaderboardPagerAdapter leaderboardPagerAdapter = new LeaderboardPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(leaderboardPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                leaderboardPagerAdapter.setCurrentLeaderboardFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        /* Button Events */
        
        findViewById(R.id.btnRefresh).setOnClickListener(v -> leaderboardPagerAdapter.refreshLeaderboardFragmentData());

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.btnTen).setOnClickListener(v -> {
            numberOfUsers = 10;
            //leaderboardPagerAdapter.setNumberOfUsers(numberOfUsers);
        });

        findViewById(R.id.btnTwentyFive).setOnClickListener(v -> {
            numberOfUsers = 25;
            //leaderboardPagerAdapter.setNumberOfUsers(numberOfUsers);
        });

        findViewById(R.id.btnFifty).setOnClickListener(v -> {
            numberOfUsers = 50;
            //leaderboardPagerAdapter.setNumberOfUsers(numberOfUsers);
        });

        android.view.View averageButton = findViewById(R.id.btnAverageScoreAggregation);
        android.view.View bestButton = findViewById(R.id.btnBestScoreAggregation);
        android.view.View everyoneButton = findViewById(R.id.btnEveryone);
        android.view.View thisSeasonButton = findViewById(R.id.btnThisSeason);
        android.view.View allResortsButton = findViewById(R.id.btnAllResorts);

        int optionAvailableColor = ContextCompat.getColor(getApplicationContext(), R.color.opentracks);
        int optionSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.opentracks_transparent);

        averageButton.setBackgroundColor(optionSelectedColor);
        everyoneButton.setBackgroundColor(optionSelectedColor);

        averageButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.setCurrentLeaderboardType(LeaderboardFragment.LeaderboardType.Average);
            averageButton.setBackgroundColor(optionSelectedColor);
            bestButton.setBackgroundColor(optionAvailableColor);
        });

        bestButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.setCurrentLeaderboardType(LeaderboardFragment.LeaderboardType.Best);
            averageButton.setBackgroundColor(optionAvailableColor);
            bestButton.setBackgroundColor(optionSelectedColor);
        });

        everyoneButton.setOnClickListener(v -> {
            
            everyoneButton.setBackgroundColor(optionSelectedColor);
            thisSeasonButton.setBackgroundColor(optionAvailableColor);
            allResortsButton.setBackgroundColor(optionAvailableColor);
        });

        thisSeasonButton.setOnClickListener(v -> {

            everyoneButton.setBackgroundColor(optionAvailableColor);
            thisSeasonButton.setBackgroundColor(optionSelectedColor);
            allResortsButton.setBackgroundColor(optionAvailableColor);
        });

        allResortsButton.setOnClickListener(v -> {

            everyoneButton.setBackgroundColor(optionAvailableColor);
            thisSeasonButton.setBackgroundColor(optionAvailableColor);
            allResortsButton.setBackgroundColor(optionSelectedColor);
        });
    }
}