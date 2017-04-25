package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainFriendship;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.Friendship;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.MyHelper;
import com.seniorproject.sallemapp.helpers.SaveFriendshipRequestAsync;
import com.seniorproject.sallemapp.helpers.UpdateFriendRequestAsync;


import java.util.ArrayList;

/**
 * Created by abdullahbamusa on 4/1/17.
 */

public class FriendRequestsListAdapter extends ArrayAdapter<DomainFriendship> {
    private ArrayList<DomainFriendship> mItems;
    private Context mAdpaterContext;
    public Button mAcceptButton;
    public Button mDeclineButton;
    public FriendRequestsListAdapter(@NonNull Context context, ArrayList<DomainFriendship> items) {
        super(context, R.layout.friendship_requests_list, items);
        mAdpaterContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View v = convertView;

        try {
            final DomainFriendship friendship = mItems.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) mAdpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.friendship_requests_list, null);
                mAcceptButton = (Button) v.findViewById(R.id.friendRequests_btnAccept);
                mAcceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position,0);
                    }
                });
                mDeclineButton = (Button)v.findViewById(R.id.friendRequests_btnDecline);
                mDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position,0);
                    }
                });

            }
            //Bind the UI elements to entity
            bindUser(friendship, v);

        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return v;
    }
    private void bindUser(DomainFriendship friendship, View v){
        ImageView avatar = (ImageView) v.findViewById(R.id.friendRequests_imgAvatar);
        TextView userName = (TextView) v.findViewById(R.id.friendRequests_lblUserName);
        if(friendship.getmDomainUser().getAvatar() == null){
            avatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
        else{
            Bitmap scaledImage= Bitmap.createScaledBitmap(friendship.getmDomainUser().getAvatar(), 185, 185, false);
            avatar.setImageBitmap(scaledImage);
        }
        userName.setText(friendship.getmDomainUser().getFirstName() + " "
                + friendship.getmDomainUser().getLasttName());
    }
}
