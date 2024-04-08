package de.dennisguse.opentracks.ui.friends;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import de.dennisguse.opentracks.R;

public class FriendsViewHolder {
    ImageView userImages;
    TextView username;

    FriendsViewHolder(View view){
        userImages = view.findViewById(R.id.imageView);
        username = view.findViewById(R.id.user_name);
    }


}
