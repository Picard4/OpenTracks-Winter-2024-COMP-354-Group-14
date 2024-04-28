package de.dennisguse.opentracks.ui.friends;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.databinding.AggregatedStatsBinding;
import de.dennisguse.opentracks.databinding.FriendsPageBinding;

public class FriendsActivity extends AbstractActivity {
    ListView listView;
    private FriendsPageBinding viewBinding;
    private MenuItem searchMenuItem;

    ArrayList<String> username;
//    ArrayList<String> usernameCopy;

//    String[] username = {"John Doe",
//            "Jane Smith",
//            "David Johnson",
//            "Alex Harris",
//            "Jordan Anderson",
//            "Rachel Taylor",
//            "Andrew Wilson",
//            "Amy Clark"
//    };

    //placeholder for user images
    int images = R.drawable.friends_icon;
    String afterTextChanged = "";
    String beforeTextChanged = "";
    String onTextChanged = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        username = new ArrayList<>();
        username.add("John Doe");
        username.add("Jane Smith");
        username.add("David Johnson");
        username.add("Alex Harris");
        username.add("Jordan Anderson");
        username.add("Rachel Taylor");
        username.add("Andrew Wilson");
        username.add("Amy Clark");

//        usernameCopy = new ArrayList<>();
//        usernameCopy.add("John Doe");
//        usernameCopy.add("Jane Smith");
//        usernameCopy.add("David Johnson");
//        usernameCopy.add("Alex Harris");
//        usernameCopy.add("Jordan Anderson");
//        usernameCopy.add("Rachel Taylor");
//        usernameCopy.add("Andrew Wilson");
//        usernameCopy.add("Amy Clark");

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

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChanged = searchBar.getText().toString();
            }

//            int counter = 0;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                onTextChanged = searchBar.getText().toString();
                if (onTextChanged.isEmpty()) {
//                  Hide the 'friendsList'
                    listView.setVisibility(View.GONE);
                } else if(username.toString().contains(onTextChanged)) {
//                    for(int i = 0; i < username.size(); i++){
//                        if(!usernameCopy.get(i).toString().contains(s)){
//                            if(username.contains(usernameCopy.get(i))) {
//                                username.remove(i);
//                            }
//                        }
//                        else if(usernameCopy.get(i).toString().contains(s)){
//                            if(!username.contains(usernameCopy.get(i))){
//                                username.add(usernameCopy.get(i));
//                            }
//                        }
//                    }
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChanged = searchBar.getText().toString();
            }
        });

    }

    @Override
    protected View getRootView() {
        viewBinding = FriendsPageBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }


}