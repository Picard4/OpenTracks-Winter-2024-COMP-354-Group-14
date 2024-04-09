package de.dennisguse.opentracks.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.dennisguse.opentracks.R;

public class FriendsAdapter extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String[] username;

    public FriendsAdapter(Context context, String[] username, int[] images) {
        super(context, R.layout.friends_list_item,R.id.user_name, username);
        this.context = context;
        this.images = images;
        this.username = username;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //final String PostKey = getItem(position);

        View friendsListItemView = convertView;
        FriendsViewHolder holder = null;
        if(friendsListItemView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            friendsListItemView = layoutInflater.inflate(R.layout.friends_list_item, parent, false);
            holder = new FriendsViewHolder(friendsListItemView);
            friendsListItemView.setTag(holder);
        }else{
            holder = (FriendsViewHolder) friendsListItemView.getTag();
        }

        holder.userImages.setImageResource(images[position]);
        holder.username.setText(username[position]);

        friendsListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FriendClActivity.class);
                //  intent.putExtra("PostKey", PostKey);
                // Start the activity
                getContext().startActivity(intent);

                // Toast.makeText(getContext(), "You clicked:" + username[position], Toast.LENGTH_SHORT).show();
            }
        });



        return friendsListItemView;
    }
}
