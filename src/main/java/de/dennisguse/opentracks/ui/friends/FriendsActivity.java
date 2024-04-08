package de.dennisguse.opentracks.ui.friends;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomappbar.BottomAppBar;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.databinding.AggregatedStatsBinding;
import de.dennisguse.opentracks.databinding.FriendsPageBinding;

public class FriendsActivity extends AbstractActivity {
    ListView listView;
    private FriendsPageBinding viewBinding;
    private MenuItem searchMenuItem;

    String[] username = {"John Doe",
            "Jane Smith",
            "David Johnson"
    };

    //placeholder for user images
    int[] images = {R.drawable.friends_icon,R.drawable.friends_icon,R.drawable.friends_icon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //viewBinding = FriendsPageBinding.inflate(getLayoutInflater());
        setContentView(R.layout.friends_page);
        listView = findViewById(R.id.friendsList);
        FriendsAdapter friendsAdapter = new FriendsAdapter (this, username, images);
        listView.setAdapter(friendsAdapter);

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar_layout).findViewById(R.id.bottom_app_bar);

        setSupportActionBar(bottomAppBar);

        Button searchButton = findViewById(R.id.searchButton);
        EditText searchBar = findViewById(R.id.searchBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchBar.getText().toString();
                // Use searchText for your search logic
                Toast.makeText(FriendsActivity.this, "Searching for: " + searchText, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected View getRootView() {
        viewBinding = FriendsPageBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }


}