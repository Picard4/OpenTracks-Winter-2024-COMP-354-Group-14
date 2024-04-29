package de.dennisguse.opentracks.ui.leaderboard.leaderboardFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Ranking;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<Ranking> displayedRankingList;
    private static int largestNumberRankToDisplay = 10;

    public LeaderboardAdapter(List<Ranking> displayedRankingList) {
        this.displayedRankingList = displayedRankingList;
    }

    public static int getLargestNumberRankToDisplay() {
        return largestNumberRankToDisplay;
    }

    /**
     * Sets the largestNumberRankToDisplay, but does not change the leaderboard that is shown on-screen;
     * you need to call setDisplayedRankingList(List<Ranking> rankingListToDisplay) for that.
     * @param largestNumberRank The largest-number rank that can be displayed in the on-screen leaderboard.
     */
    public static void setLargestNumberRankToDisplay(int largestNumberRank) {
        largestNumberRankToDisplay = largestNumberRank;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_leaderboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ranking ranking = displayedRankingList.get(position);
        holder.usernameText.setText(ranking.getUsername());
        holder.locationText.setText(ranking.getLocation());
        holder.rankText.setText(String.valueOf(ranking.getRank()));
        holder.scoreText.setText(String.valueOf(ranking.getScore()));
    }

    @Override
    public int getItemCount() {
        return displayedRankingList.size();
    }

    /**
     * Changes the list of Rankings that is shown on-screen to the rankingListToDisplay,
     * or a filtered version of it if some rankings are restricted from being displayed.
     * @param rankingListToDisplay The list of Rankings to be filtered based on the largest-numbered rank we want to show, and displayed in the GUI.
     */
    public void setDisplayedRankingList(List<Ranking> rankingListToDisplay) {
        List<Ranking> filteredRankingListToDisplay = displayOnlySetAmountOfRanks(rankingListToDisplay);

        // This if statement is just here to improve efficiency if the user switched LeaderboardFragments but not anything else
        if (this.displayedRankingList == filteredRankingListToDisplay)
            return;

        this.displayedRankingList = filteredRankingListToDisplay;

        // Since the displayedRankingList could have been remade from the ground up, we have to call notifyDataSetChanged();
        notifyDataSetChanged();
    }

    private List<Ranking> displayOnlySetAmountOfRanks(List<Ranking> rankingListToDisplay) {
        // Display all the ranks if all ranks are needed or
        // the largest-numbered rank is less than the largest-numbered rank we wish to display.
        if (largestNumberRankToDisplay <= 0 ||
                rankingListToDisplay.get(rankingListToDisplay.size() - 1).getRank() <= largestNumberRankToDisplay)
            return rankingListToDisplay;

        // The Top X restriction is based on the Rank, not the User's Ranking.
        // If three Users are tied for 25th and we want the Top 25, the three 25th place Rankings should all be shown.
        List<Ranking> newRankingListToDisplay = new ArrayList<>();
        for (int i = 0; i < rankingListToDisplay.size(); i++) {
            Ranking nextRanking = rankingListToDisplay.get(i);
            if (nextRanking.getRank() > largestNumberRankToDisplay)
                break;
            newRankingListToDisplay.add(nextRanking);
        }
        return  newRankingListToDisplay;
    }

    /**
     * The class that Rankings are bound to so that they can be shown in the GUI.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView usernameText;
        TextView locationText;
        TextView rankText;
        TextView scoreText;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            usernameText = itemView.findViewById(R.id.usernameText);
            locationText = itemView.findViewById(R.id.locationText);
            rankText = itemView.findViewById(R.id.rankText);
            scoreText = itemView.findViewById(R.id.scoreText);
        }
    }
}