package de.dennisguse.opentracks.ui.friends;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import de.dennisguse.opentracks.R;

public class FriendClActivity extends AppCompatActivity {
     private DatabaseReference clickPostRef;
     private String PostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        // PostKey = getIntent().getExtras().get("PostKey").toString();
        // clickPostRef = FirebaseDatabase.getInstance().getReference().getRef().child("?").child(PostKey);

    }
}