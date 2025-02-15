package de.dennisguse.opentracks.ui.leaderboard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.LeaderboardAdapter;
import de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment.LeaderboardFragment;

public class LeaderboardActivity extends AppCompatActivity {
    private static final int TEN_BUTTON_VALUE = 10;
    private static final int TWENTY_FIVE_BUTTON_VALUE = 25;
    private static final int ALL_BUTTON_VALUE = 0;

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
                /*
                This method only exists out of obligation.
                We just need an onPageSelected override to ensure that the leaderboardPagerAdapter's
                currentLeaderboardFragment remains up to date when the user switches leaderboards.
                */
            }

            @Override
            public void onPageSelected(int position) {
                leaderboardPagerAdapter.setCurrentLeaderboardFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                This method only exists out of obligation.
                We just need an onPageSelected override to ensure that the leaderboardPagerAdapter's
                currentLeaderboardFragment remains up to date when the user switches leaderboards.
                */
            }
        });

        int optionAvailableColor = ContextCompat.getColor(getApplicationContext(), R.color.opentracks);
        int optionSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.opentracks_transparent);

        android.view.View tenButton = findViewById(R.id.btnTen);
        android.view.View twentyFiveButton = findViewById(R.id.btnTwentyFive);
        android.view.View allButton = findViewById(R.id.btnAll);

        android.view.View refreshButton = findViewById(R.id.btnRefresh);
        android.view.View backButton = findViewById(R.id.back);

        refreshButton.setOnClickListener(v -> leaderboardPagerAdapter.refreshLeaderboardFragmentData());

        backButton.setOnClickListener(v -> onBackPressed());

        // Get the current largest-number rank that can be shown and change the button colors accordingly.
        if (LeaderboardAdapter.getLargestNumberRankToDisplay() == TEN_BUTTON_VALUE)
            tenButton.setBackgroundColor(optionSelectedColor);
        else if (LeaderboardAdapter.getLargestNumberRankToDisplay() == TWENTY_FIVE_BUTTON_VALUE)
            twentyFiveButton.setBackgroundColor(optionSelectedColor);
        else
            allButton.setBackgroundColor(optionSelectedColor);

        tenButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.changeLargestNumberRankToDisplay(TEN_BUTTON_VALUE);
            findViewById(R.id.btnTen).setBackgroundColor(optionSelectedColor);
            findViewById(R.id.btnTwentyFive).setBackgroundColor(optionAvailableColor);
            findViewById(R.id.btnAll).setBackgroundColor(optionAvailableColor);
        });

        twentyFiveButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.changeLargestNumberRankToDisplay(TWENTY_FIVE_BUTTON_VALUE);
            findViewById(R.id.btnTen).setBackgroundColor(optionAvailableColor);
            findViewById(R.id.btnTwentyFive).setBackgroundColor(optionSelectedColor);
            findViewById(R.id.btnAll).setBackgroundColor(optionAvailableColor);
        });

        allButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.changeLargestNumberRankToDisplay(ALL_BUTTON_VALUE);
            findViewById(R.id.btnTen).setBackgroundColor(optionAvailableColor);
            findViewById(R.id.btnTwentyFive).setBackgroundColor(optionAvailableColor);
            findViewById(R.id.btnAll).setBackgroundColor(optionSelectedColor);
        });

        Spinner competencies = findViewById(R.id.spinner_competency);
        competencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)view).setText(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> competency_list = new ArrayList<>();
        competency_list.add("All");
        competency_list.add("Beginner");
        competency_list.add("Intermediate");
        competency_list.add("Expert");

        ArrayAdapter<String> competency_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, competency_list);
        competency_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        competencies.setAdapter(competency_adapter);

        android.view.View averageButton = findViewById(R.id.btnAverageScoreAggregation);
        android.view.View bestButton = findViewById(R.id.btnBestScoreAggregation);
        android.view.View everyoneButton = findViewById(R.id.btnEveryone);
        android.view.View thisSeasonButton = findViewById(R.id.btnThisSeason);
        android.view.View allResortsButton = findViewById(R.id.btnAllResorts);
 
        averageButton.setBackgroundColor(optionSelectedColor);
        everyoneButton.setBackgroundColor(optionSelectedColor);

        averageButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.setCurrentLeaderboardRankingListType(LeaderboardFragment.RankingListType.AVERAGE);
            averageButton.setBackgroundColor(optionSelectedColor);
            bestButton.setBackgroundColor(optionAvailableColor);
        });

        bestButton.setOnClickListener(v -> {
            leaderboardPagerAdapter.setCurrentLeaderboardRankingListType(LeaderboardFragment.RankingListType.BEST);
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