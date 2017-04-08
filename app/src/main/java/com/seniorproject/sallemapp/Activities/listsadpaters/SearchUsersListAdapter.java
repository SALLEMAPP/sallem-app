package com.seniorproject.sallemapp.Activities.listsadpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.seniorproject.sallemapp.Activities.ShowPostActivity;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.MyApplication;
import com.seniorproject.sallemapp.helpers.SaveFriendshipRequestAsync;

import java.util.ArrayList;

/**
 * Created by abdul on 31-Mar-2017.
 */

public class SearchUsersListAdapter extends ArrayAdapter<DomainUser> {
    private ArrayList<DomainUser> mItems;
    private Context mAdpaterContext;
    public Button mSendRequestButton;
    public SearchUsersListAdapter(@NonNull Context context, ArrayList<DomainUser> items) {
        super(context, R.layout.search_users_list, items);
        mAdpaterContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        try {
            final DomainUser user = mItems.get(position);
            if(v == null){
                LayoutInflater vi =
                        (LayoutInflater) mAdpaterContext.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE
                        );
                v = vi.inflate(R.layout.search_users_list, null);
                mSendRequestButton = (Button) v.findViewById(R.id.searchUsers_btnRequest);
                mSendRequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveFriendshipRequestAsync saveRequest = new SaveFriendshipRequestAsync(v.getContext(),
                                DomainUser.CURRENT_USER.getId(),
                                user.getId()
                                );
                        saveRequest.execute();

                    }
                });

            }
            //Bind the UI elements to entity
            bindUser(user, v);

        }
        catch (Exception e){
            //Log the exception so it can be reviewed.
            //Log.d("SALLEM APP", e.getStackTrace().toString());
            throw e;
        }
        return v;
    }
    private void bindUser(DomainUser user, View v){
        ImageView avatar = (ImageView) v.findViewById(R.id.searchUsers_imgAvatar);
        TextView userName = (TextView) v.findViewById(R.id.searchUsers_lblUserName);
        avatar.setImageBitmap(user.getAvatar());

        userName.setText(user.getFirstName() + " " + user.getLasttName());
    }
}
