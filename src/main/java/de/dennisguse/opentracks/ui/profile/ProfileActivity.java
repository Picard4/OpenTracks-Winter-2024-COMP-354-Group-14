package de.dennisguse.opentracks.ui.profile;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import de.dennisguse.opentracks.databinding.ProfilePageBinding;

public class ProfileActivity extends AbstractActivity {
    private ProfilePageBinding viewBinding;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        viewBinding = ProfilePageBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar_layout).findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);

    }

    @Override
    protected View getRootView() {
        viewBinding = ProfilePageBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
